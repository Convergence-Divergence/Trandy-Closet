package com.example.test

import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.ImageLoader
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.test.databinding.HomeActivityBinding
import kotlinx.android.synthetic.main.example_5_fragment.*
import org.xml.sax.InputSource
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.io.StringReader
import java.text.SimpleDateFormat
import javax.xml.parsers.DocumentBuilderFactory

class HomeActivity : AppCompatActivity() {


    // 날씨 부분 변수 선언
    val symbolView by lazy { mission1_symbol }
    val temperatureView by lazy { mission1_temperature }
    val upView by lazy { mission1_up_text }
    val downView by lazy { mission1_down_text }
    val recyclerView by lazy { mission1_recycler }
    val queue by lazy { Volley.newRequestQueue(this) }

    val list = mutableListOf<WeatherData>()
    val adapter_weather = MyAdapter(list)
    var stringToDate = SimpleDateFormat("yyyy-mm-dd")
    // 날씨 부분 변수 선언 끝

    internal lateinit var binding: HomeActivityBinding

    private val examplesAdapter = HomeOptionsAdapter {
        val fragment = it.createView()
        supportFragmentManager.beginTransaction()
            .run {
                if (fragment is Example5Fragment) {
                    return@run setCustomAnimations(
                        R.anim.slide_in_up,
                        R.anim.fade_out,
                        R.anim.fade_in,
                        R.anim.slide_out_down
                    )
                }
                return@run setCustomAnimations(
                    R.anim.slide_in_right,
                    R.anim.slide_out_left,
                    R.anim.slide_in_left,
                    R.anim.slide_out_right
                )
            }
            .add(R.id.homeContainer, fragment, fragment.javaClass.simpleName)
            .addToBackStack(fragment.javaClass.simpleName)
            .commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.homeToolbar)
        binding.examplesRv.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = examplesAdapter
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        }

        // 날씨 부분 시작
        recyclerView.let {
            val layoutManager = LinearLayoutManager(this)
//            layoutManager.orientation = LinearLayoutManager.HORIZONTAL
            it.layoutManager = layoutManager
            it.addItemDecoration(MyItemDecoration())
            it.adapter = adapter_weather
        }

        val currentRequest = StringRequest(
            Request.Method.POST,
            "http://api.openweathermap.org/data/2.5/weather?q=seoul&mode=xml&units=metric&appid=4e2d0b710b962dd6098c49dc317096f3",
            Response.Listener { response -> parseXMLCurrent(response) }, Response.ErrorListener {})

        val forecastRequest = StringRequest(Request.Method.POST,
            "https://api.openweathermap.org/data/2.5/forecast?q=seoul&mode=xml&units=metric&appid=4e2d0b710b962dd6098c49dc317096f3",
            Response.Listener { response -> parseXMLForecast(response) }, Response.ErrorListener {})

        queue.add(currentRequest)
        queue.add(forecastRequest)
        // 날씨 부분 끝
    }

    // 날씨 부분 시작
    fun parseXMLCurrent(response: String) {
        try {
            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            val doc = builder.parse(InputSource(StringReader(response)))
            doc.documentElement.normalize()

            val tempElement = doc.getElementsByTagName("temperature").item(0) as Element
            val temperature = tempElement.getAttribute("value")

            temperatureView.text = temperature
            upView.text = tempElement.getAttribute("max")
            downView.text = tempElement.getAttribute("min")

            val weatherElement = doc.getElementsByTagName("weather").item(0) as Element
            val symbol = weatherElement.getAttribute("icon")

            val imageLoader = ImageLoader(queue, object: ImageLoader.ImageCache {
                override fun getBitmap(url: String?): Bitmap? {
                    return null
                }

                public override fun putBitmap(url: String?, bitmap: Bitmap?) {
                }
            })

            val uriString = "http://openweathermap.org/img/w/$symbol.png"
            symbolView.setImageUrl(uriString, imageLoader)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun parseXMLForecast(response: String) {
        try {
            val factory = DocumentBuilderFactory.newInstance()
            val builder = factory.newDocumentBuilder()
            val doc: Document = builder.parse(InputSource(StringReader(response)))
            doc.documentElement.normalize()

            val nodeList = doc.getElementsByTagName("time")

            val minTempList = mutableListOf<Float>()
            val maxTempList = mutableListOf<Float>()

            var newDate: Long? = 0
            var oldDate: Long? = null

            for (i in 0 until nodeList.length) {
                val weatherData = WeatherData("", "", "", null)

                val timeNode = nodeList.item(i) as Element
                val tempNode = timeNode.getElementsByTagName("temperature").item(0) as Element

                weatherData.day = timeNode.getAttribute("from").substring(0..9)
                weatherData.max = tempNode.getAttribute("max")
                weatherData.min = tempNode.getAttribute("min")

                newDate = stringToDate.parse(weatherData.day).time.toLong()

                if (oldDate == null) {
                    oldDate = newDate
                }

                if (newDate == oldDate) {
                    maxTempList.add(weatherData.max.toFloat())
                    minTempList.add(weatherData.min.toFloat())
                }
                else {
                    weatherData.day = weatherData.day.substring(5..9)
                    weatherData.max = maxTempList.max().toString()
                    weatherData.min = minTempList.min().toString()

                    val symbolNode = timeNode.getElementsByTagName("symbol").item(0) as Element
                    val symbol = symbolNode.getAttribute("var")

                    val url = "http://openweathermap.org/img/w/$symbol.png"

                    val imageRequest = ImageRequest(url, Response.Listener {
                        weatherData.img = it
                        adapter_weather.notifyDataSetChanged()
                    }, 0, 0, ImageView.ScaleType.CENTER_CROP, null, Response.ErrorListener {})
                    queue.add(imageRequest)
                    list.add(weatherData)

                    oldDate = newDate

                    maxTempList.clear()
                    minTempList.clear()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    // 날씨 부분 끝

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> onBackPressed().let { true }
            else -> super.onOptionsItemSelected(item)
        }
    }
}

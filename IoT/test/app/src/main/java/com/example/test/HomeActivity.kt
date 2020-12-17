package com.example.test

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.test.databinding.HomeActivityBinding
import kotlinx.android.synthetic.main.fragment_photo.*
import kotlinx.android.synthetic.main.home_activity.*
import org.jetbrains.anko.longToast
import com.google.gson.JsonObject
import org.json.JSONObject
import org.tensorflow.lite.Interpreter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.FileInputStream
import java.io.IOException
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.util.*
import kotlin.collections.HashMap

class HomeActivity : AppCompatActivity() {



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

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = HomeActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.homeToolbar)

        bt_recom.setOnClickListener {
            val i = Intent(this, MyclothActivity::class.java)
            startActivity(i)
        }

        binding.examplesRv.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            adapter = examplesAdapter
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
        }

        getCurrentWeather()

        // 탠서플로우 써보까
        bt_pop.setOnClickListener {
            val i = Intent(this, ClassifierActivity::class.java)
            startActivity(i)
        }
        bt_test.setOnClickListener {
            var input = Array<FloatArray>(1){FloatArray(3)}
            input[0][0] = 255F
            input[0][1] = 94F
            input[0][2] = 0F
            var output = Array<FloatArray>(1, {FloatArray(12)})
            val tflite = getTfliteInterpreter("color.tflite")
            Log.d("머지이건","${tflite}")
            tflite!!.run(input, output)

            for(a in input[0]) {
                Log.d("인풋 3개", "${a}")
            }

            Log.d("머지이건","${output[0].size}")
            for(a in output[0].indices) {
                Log.d("아웃풋 12개", "인덱스 ${a}, 밸류 ${output[0][a]}")
            }
            tv_test.setText((output[0]).toString())
        }


        // 내 옷장서 일정 추가해보자
        bt_my.setOnClickListener {
            val i = Intent(this, CrudActivity::class.java)
            startActivity(i)
        }

    }






    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> onBackPressed().let { true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun getCurrentWeather() {
        var res: Call<JsonObject> = RetrofitClient
            .getInstance()
            .buildRetrofit()
            .getCurrentWeather("37.50", "127.05", "metric", "4e2d0b710b962dd6098c49dc317096f3")

        res.enqueue(object : Callback<JsonObject> {

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.d("실패", "Failure : ${t.message.toString()}")
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                var jsonObj = JSONObject(response.body().toString())
                Log.d("성공", "Success :: $jsonObj")

                if (jsonObj != null) {
                    Log.d("성공", "Success :: $jsonObj")
                    var iconName: String = ""
                    var nowTemp: String = ""
                    var maxTemp: String = ""
                    var minTemp: String = ""

                    var humidity: String = ""
                    var speed: String = ""
                    var main: String = ""
                    var description: String = ""

                    var iconUrl: String = ""


                    iconName = jsonObj.getJSONArray("weather").getJSONObject(0).getString("icon")
                    nowTemp = jsonObj.getJSONObject("main").getString("temp")
                    humidity = jsonObj.getJSONObject("main").getString("humidity")
                    minTemp = jsonObj.getJSONObject("main").getString("temp_min")
                    maxTemp = jsonObj.getJSONObject("main").getString("temp_max")
                    speed = jsonObj.getJSONObject("wind").getString("speed")
                    main = jsonObj.getJSONArray("weather").getJSONObject(0).getString("main")
                    description =
                        jsonObj.getJSONArray("weather").getJSONObject(0).getString("description")

                    description = transferWeather(description).toString()

                    iconUrl = "https://openweathermap.org/img/w/" + iconName + ".png"
                    Log.d("성공", "아이콘 유알엘 $iconUrl")
                    Glide.with(this@HomeActivity).load(iconUrl).into(iv_icon)

                    var msg: String =
                        description + " 습도 " + humidity + "%, 풍속 " + speed + "m/s" + " 온도 현재:" + nowTemp + " / 최저:" + minTemp + " / 최고:" + maxTemp
                    Log.d("성공", "Success :: $msg")

                    var text_wt = findViewById<TextView>(R.id.tv_wt)
                    var text_nowtemp = findViewById<TextView>(R.id.tv_nowtemp)
                    var text_hightemp = findViewById<TextView>(R.id.tv_hightemp)
                    var text_lowtemp = findViewById<TextView>(R.id.tv_lowtemp)
                    var text_hu = findViewById<TextView>(R.id.tv_hu)
                    var text_win = findViewById<TextView>(R.id.tv_win)

                    text_wt.text = "현재 날씨 " + description
                    text_nowtemp.text = "현재 기온 " + nowTemp + "ºC"
                    text_hightemp.text = "최고 기온 " + maxTemp + "ºC"
                    text_lowtemp.text = "최저 기온 " + minTemp + "ºC"
                    text_hu.text = "습도 " + humidity + "%"
                    text_win.text = "풍속 " + speed + "m/s"



                    var text_all_weather = findViewById<TextView>(R.id.tv_weather)
                    text_all_weather.text = msg
                }

            }
        })
    }

    private fun transferWeather(weather: String): String? {
        var weather = weather
        weather = weather.toLowerCase()
        if (weather == "haze") {
            return "안개"
        } else if (weather == "fog") {
            return "안개"
        } else if (weather == "clouds") {
            return "구름"
        } else if (weather == "few clouds") {
            return "구름 조금"
        } else if (weather == "scattered clouds") {
            return "구름 낌"
        } else if (weather == "broken clouds") {
            return "구름 많음"
        } else if (weather == "overcast clouds") {
            return "구름 많음"
        } else if (weather == "clear sky") {
            return "맑음"
        }
        return weather
    }

    // 모델 파일 인터프리터를 생성하는 공통 함수
    // loadModelFile 함수에 예외가 포함되어 있기 때문에 반드시 try, catch 블록이 필요하다.
    private fun getTfliteInterpreter(modelPath:String): Interpreter? {
        try
        {
            return Interpreter(loadModelFile(this@HomeActivity, modelPath))
        }
        catch (e:Exception) {
            e.printStackTrace()
        }
        return null
    }
    // 모델을 읽어오는 함수로, 텐서플로 라이트 홈페이지에 있다.
    // MappedByteBuffer 바이트 버퍼를 Interpreter 객체에 전달하면 모델 해석을 할 수 있다.
    @Throws(IOException::class)
    private fun loadModelFile(activity: Activity, modelPath:String): MappedByteBuffer {
        val fileDescriptor = activity.getAssets().openFd(modelPath)
        val inputStream = FileInputStream(fileDescriptor.getFileDescriptor())
        val fileChannel = inputStream.getChannel()
        val startOffset = fileDescriptor.getStartOffset()
        val declaredLength = fileDescriptor.getDeclaredLength()
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }
}

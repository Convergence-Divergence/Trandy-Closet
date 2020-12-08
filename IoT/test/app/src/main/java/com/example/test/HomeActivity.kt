package com.example.test

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.databinding.HomeActivityBinding
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

        getCurrentWeather()
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
                    var msg: String =
                        description + " 습도 " + humidity + "%, 풍속 " + speed + "m/s" + " 온도 현재:" + nowTemp + " / 최저:" + minTemp + " / 최고:" + maxTemp;
                    Log.d("성공", "Success :: $msg")

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
        return ""
    }
}

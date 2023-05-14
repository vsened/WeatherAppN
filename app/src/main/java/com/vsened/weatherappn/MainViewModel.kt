package com.vsened.weatherappn

import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.vsened.weatherappn.adapters.WeatherModel
import org.json.JSONArray
import org.json.JSONObject

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var fLocationClient: FusedLocationProviderClient

    val liveDataCurrent = MutableLiveData<WeatherModel>()
    val liveDataList = MutableLiveData<List<WeatherModel>>()
    @SuppressLint("StaticFieldLeak")
    private val context = getApplication<Application>().applicationContext

    private fun requestWeatherData(location: String) {
        val url = "https://api.weatherapi.com/v1/forecast.json?" +
                "key=${API_KEY}" +
                "&q=$location" +
                "&days=3" +
                "&aqi=no" +
                "&alerts=no"
        val queue = Volley.newRequestQueue(context)
        val request = StringRequest(
            Request.Method.GET,
            url,
            {
                    result -> parseWeatherData(result)
            },
            {
                    error ->
                Toast.makeText(context, "Check your internet connection!", Toast.LENGTH_SHORT).show()
            }
        )
        queue.add(request)
    }

    fun getCurrentLocation() {
        fLocationClient = LocationServices.getFusedLocationProviderClient(context)
        val ct = CancellationTokenSource()
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fLocationClient
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, ct.token)
            .addOnCompleteListener {
                requestWeatherData("${it.result.latitude},${it.result.longitude}")
            }
    }

    private fun parseWeatherData(result: String) {
        val json = JSONObject(result)
        val list = parseDays(json)
        liveDataList.value = list
        parseCurrentData(json, list[0])
    }

    private fun parseCurrentData(json: JSONObject, weatherItem: WeatherModel) {
        val item = WeatherModel(
            city = json.getJSONObject("location").getString("name"),
            time = json.getJSONObject("current").getString("last_updated"),
            condition = json.getJSONObject("current").getJSONObject("condition")
                .getString("text"),
            currentTemp = json.getJSONObject("current").getDouble("temp_c")
                .toInt().toString() + "°C",
            maxTemp = weatherItem.maxTemp,
            minTemp = weatherItem.minTemp,
            imageUrl = "https:" + json.getJSONObject("current").getJSONObject("condition")
                .getString("icon"),
            hours = weatherItem.hours
        )
        liveDataCurrent.value = item
    }

    private fun parseDays(json: JSONObject): List<WeatherModel>{
        val list = ArrayList<WeatherModel>()
        val response = json.getJSONObject("forecast").getJSONArray("forecastday")
        for (day in 0 until response.length()) {
            val dayWeather = WeatherModel(
                city = json.getJSONObject("location").getString("name"),
                time = response.getJSONObject(day).getString("date"),
                condition = response.getJSONObject(day).getJSONObject("day")
                    .getJSONObject("condition").getString("text"),
                currentTemp = "${response.getJSONObject(day).getJSONObject("day")
                    .getDouble("maxtemp_c").toInt()}°C / ${response.getJSONObject(day)
                    .getJSONObject("day").getDouble("mintemp_c").toInt()}°C",
                maxTemp = response.getJSONObject(day).getJSONObject("day")
                    .getString("maxtemp_c"),
                minTemp = response.getJSONObject(day).getJSONObject("day")
                    .getString("mintemp_c"),
                imageUrl = "https:" + response.getJSONObject(day).getJSONObject("day")
                    .getJSONObject("condition").getString("icon"),
                hours = response.getJSONObject(day).getJSONArray("hour").toString()
            )
            list.add(dayWeather)
            Log.d(TAG, dayWeather.toString())
        }
        return list
    }

    fun getHourlyTempList(hours: String): List<WeatherModel> {
        val jsonArray = JSONArray(hours)
        val hoursTemp = ArrayList<WeatherModel>()
        for (hour in 0 until jsonArray.length()) {
            val currentHour = WeatherModel(
                "",
                time = jsonArray.getJSONObject(hour).getString("time"),
                condition = jsonArray.getJSONObject(hour).getJSONObject("condition")
                    .getString("text"),
                currentTemp = jsonArray.getJSONObject(hour).getDouble("temp_c")
                    .toInt().toString() + "°C",
                "",
                "",
                imageUrl = "https:" + jsonArray.getJSONObject(hour).getJSONObject("condition")
                    .getString("icon"),
                ""
            )
            hoursTemp.add(currentHour)
        }
        return hoursTemp
    }


    companion object{
        private const val TAG = "MainViewModel"
        private const val API_KEY = "f4f09c08bf134ae89c6170817220506"
    }
}
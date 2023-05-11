package com.vsened.weatherappn

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vsened.weatherappn.adapters.WeatherModel

class MainViewModel : ViewModel() {

    val liveDataCurrent = MutableLiveData<WeatherModel>()
    val liveDataList = MutableLiveData<List<WeatherModel>>()


}
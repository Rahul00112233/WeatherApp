package com.example.apitutorial

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apitutorial.api.NetworkResponse
import com.example.apitutorial.api.RetrofitInstance
import com.example.apitutorial.model.WeatherModel
import kotlinx.coroutines.launch

class WeatherViewModel: ViewModel() {

    private val weatherApi = RetrofitInstance.weatherApi
    private val _weatherResult = MutableLiveData<NetworkResponse<WeatherModel>>()
    val weatherResult: LiveData<NetworkResponse<WeatherModel>> = _weatherResult

    fun getData(city : String){
        viewModelScope.launch{
            try {
                val response = weatherApi.getWeather("80af408bc1bb4e4da0b163413242405", city)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _weatherResult.value = NetworkResponse.Success(it)
                    }
                } else {
                    _weatherResult.value = NetworkResponse.Error("Failed to Load Data")
                }
            } catch (e :Exception){
                _weatherResult.value = NetworkResponse.Error("Failed to Load Data")
            }
        }
    }
}
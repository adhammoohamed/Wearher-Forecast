package com.example.weatherforecast.repoistory

import com.example.example.CurrentWeather
import com.example.weatherapp.model.Forecast
import com.example.weatherforecast.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class Repository @Inject constructor(private val apiService: ApiService) {

    suspend fun getCurrentWeather(lat: String, lon: String): Response<CurrentWeather>{
        return apiService.getCurrentWeather(lat, lon)
    }
    suspend fun getDayForecast(lat: String, lon: String): Response<Forecast> {
        return apiService.getDaysForecast(lat, lon)
    }
}
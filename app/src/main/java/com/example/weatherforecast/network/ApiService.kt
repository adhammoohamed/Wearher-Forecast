package com.example.weatherforecast.network

import com.example.example.CurrentWeather
import com.example.weatherapp.model.Forecast
import com.example.weatherforecast.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("weather?")
    suspend fun getCurrentWeather(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appId: String = Constants.API_KEY,
        @Query("units") units: String = "metric"
    ): Response<CurrentWeather>


    @GET("forecast?")
    suspend fun getDaysForecast(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") appId: String = Constants.API_KEY,
        @Query("units") units: String = "metric"
    ): Response<Forecast>
}
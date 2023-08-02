package com.example.weatherforecast.viewModel

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.example.CurrentWeather
import com.example.weatherapp.model.Forecast
import com.example.weatherforecast.repoistory.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class SharedViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private var _currentTemp = MutableLiveData<String>()
    private var _humidity = MutableLiveData<String>()
    private var _windSpeed = MutableLiveData<String>()
    val currentTemp: LiveData<String> get() = _currentTemp
    val humidity: LiveData<String> get() = _humidity
    val windSpeed: LiveData<String> get() = _windSpeed

    var _description = MutableLiveData<String>()
    val description: LiveData<String> get() = _description

    private var _currentWeather = MutableLiveData<CurrentWeather>()
    val currentWeather: LiveData<CurrentWeather> get() = _currentWeather

    var userLocation = MutableLiveData<Location>()

    //    val userLocation: LiveData<Location> get() = _userLocation

    private val _daysForecast = MutableLiveData<Forecast>()
    val daysForecast: LiveData<Forecast> get() = _daysForecast

    fun getCurrentWeather() {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val location = userLocation.value
                val response = repository.getCurrentWeather(
                    location?.latitude.toString(),
                    location?.longitude.toString()
                )
                if (response.isSuccessful && response.body() != null) {
                    _currentWeather.postValue(response.body())
                }
            } catch (e: Exception) {
                throw e
            }
        }
    }

    fun updateCurrentWeatherDate(currentWeather: CurrentWeather) {
        _currentTemp.postValue(currentWeather.main?.temp.toString())
        _humidity.postValue(currentWeather.main?.humidity.toString())
        _windSpeed.postValue(currentWeather.wind?.speed.toString())
        _description.value = currentWeather.weather[0].description.toString()
    }

    fun updateUserLocation(location: Location): Location {
        userLocation.value = location

        return userLocation.value!!
    }

    fun getDaysForecast() {
        GlobalScope.launch(Dispatchers.IO) {
            try {


                val location = userLocation.value
                val response = repository.getDayForecast(
                    location?.latitude.toString(),
                    location?.longitude.toString()
                )
                if (response.isSuccessful && response.body() != null) {
                    _daysForecast.postValue(response.body())
                }
            } catch (e: Exception) {
                throw e
            }
        }
    }
}
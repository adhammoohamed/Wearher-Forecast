package com.example.weatherforecast.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.example.CurrentWeather
import com.example.weatherapp.model.Forecast
import com.example.weatherforecast.repoistory.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private var _currentWeather = MutableLiveData<Response<CurrentWeather>>()

    val currentWeather: LiveData<Response<CurrentWeather>> get() = _currentWeather

    private var _daysForecast = MutableLiveData<Response<Forecast>>()
    val daysForecast: LiveData<Response<Forecast>> get() = _daysForecast

    fun getCurrentWeather(lat: String, lon: String): Response<CurrentWeather> {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getCurrentWeather(lat, lon)

            try {
                if (response.isSuccessful) {
                    _currentWeather.value = response
                }
            }catch (e: Exception){
                throw e
            }
        }
        return _currentWeather.value!!
    }

    fun getDaysForecast(lat: String, lon: String): Response<Forecast>{
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getDayForecast(lat, lon)
            try {
                if (response.isSuccessful){
                    _daysForecast.value = response
                }
            }catch (e: Exception){
                throw e
            }
        }
        return _daysForecast.value!!
    }
}
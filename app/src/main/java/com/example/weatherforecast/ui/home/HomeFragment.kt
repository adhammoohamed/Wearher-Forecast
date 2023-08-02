package com.example.weatherforecast.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.example.CurrentWeather
import com.example.weatherforecast.MainActivity
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.FragmentHomeBinding
import com.example.weatherforecast.viewModel.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: SharedViewModel
    private val adapter: CardAdapter = CardAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.cardsRecycler.adapter = adapter
        viewModel = (activity as MainActivity).getSharedViewModel()

        viewModel.currentWeather.observe(viewLifecycleOwner) {
            Log.d("Adham", "current Home: ${it?.name}")
            Log.d(
                "Adham",
                "forecast Home: ${it!!.weather[0].description}"
            )
        }

        updateUi()

        return binding.root
    }

    private fun updateUi() {
        viewModel.currentWeather.observe(viewLifecycleOwner) {
        binding.apply {
            cityName.text = viewModel.currentWeather.value?.name
                textTemperature.text = viewModel.currentTemp.value
                humidityTv.text = "${viewModel.humidity.value} %"
                windTv.text = "${viewModel.windSpeed.value} KM/H"
                weatherStatus.text = viewModel.description.value
        }
        updateAppDesign(it!!)
    }

        viewModel.daysForecast.observe(viewLifecycleOwner) {
            val forecastList = it.weatherList
            adapter.setData(forecastList)
        }
    }

    private fun updateAppDesign(weather: CurrentWeather) {
        binding.appBackground.setBackgroundResource(R.drawable.yellow_gradient_background)
        when (weather.weather[0].icon) {
            "01d", "01n" -> {
                val animationRes = R.raw.sunny_anim
                binding.weatherIv.setAnimation(animationRes)
            }

            "02d", "02n", "03d", "03n", "04n", "04d", "50d" -> {
                val animationRes = R.raw.cloudy_anim
                binding.appBackground.setBackgroundResource(R.drawable.teal_gradiant_background)
                binding.weatherIv.setAnimation(animationRes)
            }

            else -> {
                val animationRes = R.raw.rainy_anim
                binding.appBackground.setBackgroundResource(R.drawable.blue_gradient_background)
                binding.weatherIv.setAnimation(animationRes)
            }
        }
    }
}
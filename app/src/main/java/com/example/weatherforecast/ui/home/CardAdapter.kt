package com.example.weatherforecast.ui.home

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.weatherapp.model.WeatherList
import com.example.weatherforecast.R
import com.example.weatherforecast.databinding.CardListItemBinding
import java.text.SimpleDateFormat
import java.util.Locale

class CardAdapter : RecyclerView.Adapter<CardAdapter.CardsViewHolder>() {

    private var data = emptyList<WeatherList>()


    class CardsViewHolder(var binding: CardListItemBinding) : ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(weatherList: WeatherList) {
            binding.cardItem.setBackgroundResource(R.drawable.material_card_background)
            when (weatherList.weather[0].icon) {
                "01d", "01n" -> {
                    binding.apply {
                        cardIc.setImageResource(R.drawable.sun_ic)
                        cardTemp.text = weatherList.main?.temp.toString()
                        cardTv.text = weatherList.weather[0].description
                    }
                }

                "02d", "02n", "03d", "03n", "04n", "04d", "50d" -> {
                    binding.apply {
                        cardIc.setImageResource(R.drawable.clouds_ic)
                        cardTemp.text = weatherList.main?.temp.toString()
                        cardTv.text = weatherList.weather[0].description
                    }
                }

                else -> {
                    binding.apply {
                        cardIc.setImageResource(R.drawable.rainy_ic)
                        cardTemp.text = weatherList.main?.temp.toString()
                        cardTv.text = weatherList.weather[0].description
                    }
                }
            }
            binding.cardTimeTv.text = displayTime(weatherList.dtTxt.toString())
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun displayTime(dtTxt: String): CharSequence {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("MM-dd hh:mm a", Locale.getDefault())

            // Parse the API response time into a Date object
            val date = inputFormat.parse(dtTxt)

            // Format the Date object into the 12-hour format string
            return outputFormat.format(date)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardsViewHolder {
        val binding = CardListItemBinding.inflate(LayoutInflater.from(parent.context))
        return CardsViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: CardsViewHolder, position: Int) {
        var currentDay = data[position]
        holder.bind(currentDay)
    }

    fun setData(list: ArrayList<WeatherList>) {
        data = list
        notifyDataSetChanged()
    }
}
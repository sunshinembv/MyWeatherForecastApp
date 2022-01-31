package com.example.myweatherforecastapp.presentation.weather_forecast.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myweatherforecastapp.R
import com.example.myweatherforecastapp.databinding.ItemDailyForecastWeatherBinding
import com.example.myweatherforecastapp.presentation.weather_forecast.ui_model.DailyForecastWeatherItemUI

class DailyForecastWeatherAdapter :
    RecyclerView.Adapter<DailyForecastWeatherAdapter.DailyForecastWeatherViewHolder>() {

    private val differ = AsyncListDiffer(this, DailyForecastWeatherDiffUtilCallback())

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DailyForecastWeatherViewHolder {
        return DailyForecastWeatherViewHolder(
            ItemDailyForecastWeatherBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holderDailyForecast: DailyForecastWeatherViewHolder,
        position: Int
    ) {
        holderDailyForecast.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun updateForecastWeatherList(newDailyForecastWeatherItemListUI: List<DailyForecastWeatherItemUI>) {
        differ.submitList(newDailyForecastWeatherItemListUI)
    }

    class DailyForecastWeatherViewHolder(private val itemWeatherBinding: ItemDailyForecastWeatherBinding) :
        RecyclerView.ViewHolder(itemWeatherBinding.root) {

        fun bind(weatherItemUIDaily: DailyForecastWeatherItemUI) {

            with(itemWeatherBinding) {
                weatherDate.text = weatherItemUIDaily.date
                weatherDayOfWeek.text = weatherItemUIDaily.dayOfWeek
                weatherDayDegrees.text =
                    itemView.context.getString(R.string.temperature, weatherItemUIDaily.dayDegrees)
                weatherNightDegrees.text = itemView.context.getString(
                    R.string.temperature,
                    weatherItemUIDaily.nightDegrees
                )

                Glide.with(itemView)
                    .load(itemView.context.getString(R.string.icon_url, weatherItemUIDaily.icon))
                    .error(R.drawable.ic_download_error)
                    .placeholder(R.drawable.ic_downloading).into(weatherSmallIcon)
            }
        }
    }

    class DailyForecastWeatherDiffUtilCallback :
        DiffUtil.ItemCallback<DailyForecastWeatherItemUI>() {
        override fun areItemsTheSame(
            oldItemDaily: DailyForecastWeatherItemUI,
            newItemDaily: DailyForecastWeatherItemUI
        ): Boolean {
            return oldItemDaily.id == newItemDaily.id
        }

        override fun areContentsTheSame(
            oldItemDaily: DailyForecastWeatherItemUI,
            newItemDaily: DailyForecastWeatherItemUI
        ): Boolean {
            return oldItemDaily == newItemDaily
        }

    }
}
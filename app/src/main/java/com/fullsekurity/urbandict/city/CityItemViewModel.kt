package com.fullsekurity.urbandict.city

import android.content.res.Configuration
import androidx.databinding.ObservableField
import com.fullsekurity.urbandict.activity.Callbacks
import com.fullsekurity.urbandict.recyclerview.RecyclerViewItemViewModel
import com.fullsekurity.urbandict.repository.storage.City

class CityItemViewModel(private val callbacks: Callbacks) : RecyclerViewItemViewModel<City>() {

    private lateinit var city: City

    val country: ObservableField<String> = ObservableField("")
    val name: ObservableField<String> = ObservableField("")
    val latLng: ObservableField<String> = ObservableField("")

    override fun setItem(city: City) {
        country.set(", ${city.country}")
        name.set(city.name)
        latLng.set("[${city.coord.lat},${city.coord.lon}]")
        this.city = city
    }

    fun onCityClicked() {
        if (callbacks.fetchActivity().orientation == Configuration.ORIENTATION_PORTRAIT) {
            callbacks.fetchActivity().loadMapFragment(city)
        } else {
            callbacks.fetchActivity().onCityClicked(city)
        }
    }

}

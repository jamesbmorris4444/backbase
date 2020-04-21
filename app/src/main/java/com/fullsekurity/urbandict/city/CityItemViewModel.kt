package com.fullsekurity.urbandict.city

import androidx.databinding.ObservableField
import com.fullsekurity.urbandict.activity.Callbacks
import com.fullsekurity.urbandict.recyclerview.RecyclerViewItemViewModel
import com.fullsekurity.urbandict.repository.storage.City

class CityItemViewModel(private val callbacks: Callbacks) : RecyclerViewItemViewModel<City>() {

    val country: ObservableField<String> = ObservableField("")
    val name: ObservableField<String> = ObservableField("")

    override fun setItem(item: City) {
        country.set(", ${item.country}")
        name.set(item.name)
    }

}
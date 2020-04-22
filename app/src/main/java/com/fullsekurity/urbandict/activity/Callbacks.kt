package com.fullsekurity.urbandict.activity

import android.view.View
import com.fullsekurity.urbandict.city.CityListViewModel

interface Callbacks {
    fun fetchActivity(): MainActivity
    fun fetchRootView() : View
    fun fetchcityListViewModel() : CityListViewModel?
    fun startMainProgressBar()
    fun stopMainProgressBar()
}
package com.fullsekurity.urbandict.city

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.fullsekurity.urbandict.R
import com.fullsekurity.urbandict.activity.Callbacks
import com.fullsekurity.urbandict.databinding.CityListItemBinding
import com.fullsekurity.urbandict.recyclerview.RecyclerViewFilterAdapter
import com.fullsekurity.urbandict.repository.storage.City
import com.fullsekurity.urbandict.ui.UIViewModel
import com.fullsekurity.urbandict.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.urbandict.utils.ViewModelInjectorModule
import javax.inject.Inject


class CityAdapter(private val callbacks: Callbacks) : RecyclerViewFilterAdapter<City, CityItemViewModel>() {

    private var adapterFilter: AdapterFilter? = null

    @Inject
    lateinit var uiViewModel: UIViewModel

    init {
        DaggerViewModelDependencyInjector.builder()
            .viewModelInjectorModule(ViewModelInjectorModule(callbacks.fetchActivity()))
            .build()
            .inject(this)
    }

    override fun getFilter(): AdapterFilter {
        adapterFilter?.let {
            return it
        }
        return AdapterFilter()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitysViewHolder {
        val citysListItemBinding: CityListItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.city_list_item, parent, false)
        val citysItemViewModel = CityItemViewModel(callbacks)
        citysListItemBinding.cityItemViewModel = citysItemViewModel
        citysListItemBinding.uiViewModel = uiViewModel
        return CitysViewHolder(citysListItemBinding.root, citysItemViewModel, citysListItemBinding)
    }

    inner class CitysViewHolder internal constructor(itemView: View, viewModel: CityItemViewModel, viewDataBinding: CityListItemBinding) :
        ItemViewHolder<City, CityItemViewModel> (itemView, viewModel, viewDataBinding)

    override fun onBindViewHolder(holder: ItemViewHolder<City, CityItemViewModel>, position: Int) {
        super.onBindViewHolder(holder, position)
        if (position % 2 == 1) {
            holder.itemView.setBackgroundColor(Color.parseColor(uiViewModel.recyclerViewAlternatingColor1))
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor(uiViewModel.recyclerViewAlternatingColor2))
        }
    }

    override fun itemFilterable(city: City, constraint: String): Boolean {
        return city.name.startsWith(constraint)
    }

}
package com.fullsekurity.urbandict.city

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fullsekurity.urbandict.R
import com.fullsekurity.urbandict.activity.Callbacks
import com.fullsekurity.urbandict.activity.MainActivity
import com.fullsekurity.urbandict.databinding.CityListFragmentBinding
import com.fullsekurity.urbandict.ui.UIViewModel
import com.fullsekurity.urbandict.utils.Constants
import com.fullsekurity.urbandict.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.urbandict.utils.ViewModelInjectorModule
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class CityFragment : Fragment(), Callbacks {

    private lateinit var cityListViewModel: CityListViewModel
    private lateinit var binding: CityListFragmentBinding
    private lateinit var mainActivity: MainActivity

    companion object {
        fun newInstance(): CityFragment {
            return CityFragment()
        }
    }

    @Inject
    lateinit var uiViewModel: UIViewModel

    override fun onAttach(context: Context) {
        DaggerViewModelDependencyInjector.builder()
            .viewModelInjectorModule(ViewModelInjectorModule(activity as MainActivity))
            .build()
            .inject(this)
        super.onAttach(context)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).toolbar.title = Constants.URBANDICT_TITLE
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.city_list_fragment, container, false) as CityListFragmentBinding
        binding.lifecycleOwner = this
        cityListViewModel = ViewModelProvider(this, CityListViewModelFactory(this)).get(CityListViewModel::class.java)
        binding.cityListViewModel = cityListViewModel
        binding.uiViewModel = uiViewModel
        uiViewModel.currentTheme = (activity as MainActivity).currentTheme
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainActivity = activity as MainActivity
    }

    override fun fetchActivity(): MainActivity {
        return if (::mainActivity.isInitialized) {
            mainActivity
        } else {
            activity as MainActivity
        }
    }

    override fun fetchRootView(): View {
        return binding.root
    }

    override fun fetchcityListViewModel() : CityListViewModel? {
        return cityListViewModel
    }

}
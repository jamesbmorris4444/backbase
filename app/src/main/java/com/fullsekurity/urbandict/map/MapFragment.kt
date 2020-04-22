package com.fullsekurity.urbandict.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.fullsekurity.urbandict.R
import com.fullsekurity.urbandict.activity.MainActivity
import com.fullsekurity.urbandict.repository.storage.City
import com.fullsekurity.urbandict.utils.Constants
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*


class MapFragment : Fragment(), OnMapReadyCallback {

    private var city: City? = null

    companion object {
        fun newInstance(city: City): MapFragment {
            val bundle = Bundle()
            bundle.putParcelable("city", city)
            val fragment = MapFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private fun readBundle(bundle: Bundle?) {
        city = bundle?.getParcelable("city")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        readBundle(arguments)
        var rootView = inflater.inflate(R.layout.map_fragment, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        return rootView
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).toolbar.title = "${Constants.MAP_TITLE}${city?.name}, ${city?.country}"
    }

    override fun onMapReady(googleMap: GoogleMap) {
        city?.let { city ->
            var cityLatLng = LatLng(city.coord.lat, city.coord.lat)
            googleMap.addMarker(MarkerOptions().position(cityLatLng).title(city.name))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(cityLatLng))
        }
    }

}
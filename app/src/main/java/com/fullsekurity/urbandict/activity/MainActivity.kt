package com.fullsekurity.urbandict.activity

import android.content.Context
import android.content.DialogInterface
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.fragment.app.FragmentManager
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.fullsekurity.urbandict.R
import com.fullsekurity.urbandict.city.CityFragment
import com.fullsekurity.urbandict.city.CityListViewModel
import com.fullsekurity.urbandict.databinding.ActivityMainBinding
import com.fullsekurity.urbandict.map.MapFragment
import com.fullsekurity.urbandict.repository.Repository
import com.fullsekurity.urbandict.repository.storage.City
import com.fullsekurity.urbandict.repository.storage.Coordinates
import com.fullsekurity.urbandict.ui.UIViewModel
import com.fullsekurity.urbandict.utils.Constants.CITY_FRAGMENT_TAG
import com.fullsekurity.urbandict.utils.Constants.MAP_FRAGMENT_TAG
import com.fullsekurity.urbandict.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.urbandict.utils.ViewModelInjectorModule
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import javax.inject.Inject


class MainActivity : AppCompatActivity(), Callbacks, OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    lateinit var repository: Repository
    @Inject
    lateinit var uiViewModel: UIViewModel

    private lateinit var lottieBackgroundView: LottieAnimationView
    private lateinit var activityMainBinding: ActivityMainBinding
    var orientation: Int = Configuration.ORIENTATION_PORTRAIT
    val doubleFragments: ObservableField<Boolean> = ObservableField(false)
    private var city: City = City("US", "Dallas", 0, Coordinates(-0.000500, 51.476852))

    enum class UITheme {
        LIGHT, DARK, NOT_ASSIGNED,
    }

    lateinit var currentTheme: UITheme

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.plant(Timber.DebugTree())
        repository = Repository(this)
        DaggerViewModelDependencyInjector.builder()
            .viewModelInjectorModule(ViewModelInjectorModule(this))
            .build()
            .inject(this)
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        activityMainBinding.uiViewModel = uiViewModel
        activityMainBinding.mainActivity = this
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        lottieBackgroundView = main_background_lottie
        orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            loadCityFragment()
        } else {
            loadDoubleFragments()
        }
        val settings = getSharedPreferences("THEME", Context.MODE_PRIVATE)
        val name: String? = settings.getString("THEME", UITheme.LIGHT.name)
        if (name != null) {
            currentTheme = UITheme.valueOf(name)
        }
    }

    override fun onResume() {
        super.onResume()
        setupToolbar()
        val fileName: String = if (orientation == Configuration.ORIENTATION_PORTRAIT) uiViewModel.backgroundLottieJsonFileName else ""
        uiViewModel.lottieAnimation(lottieBackgroundView, fileName, LottieDrawable.INFINITE)
    }

    fun loadMapFragment(city: City) {
        supportFragmentManager.popBackStack(CITY_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
            .replace(R.id.main_activity_container, MapFragment.newInstance(city), MAP_FRAGMENT_TAG)
            .addToBackStack(CITY_FRAGMENT_TAG)
            .commitAllowingStateLoss()
    }

    private fun loadDoubleFragments() {
        doubleFragments.set(true)
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
            .replace(R.id.city_container, CityFragment.newInstance(), CITY_FRAGMENT_TAG)
            .commitAllowingStateLoss()
        onCityClicked(city)
    }

    private fun loadCityFragment() {
        doubleFragments.set(false)
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
            .replace(R.id.main_activity_container, CityFragment.newInstance(), CITY_FRAGMENT_TAG)
            .commitAllowingStateLoss()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        city?.let { city ->
            googleMap.setOnMarkerClickListener(this)
            var cityLatLng = LatLng(city.coord.lat, city.coord.lon)
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(cityLatLng).title(city.name))
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(cityLatLng))
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        basicAlert()
        return true
    }

    private fun basicAlert() {
        val builder = AlertDialog.Builder(this)
        val positiveButtonClick = { _: DialogInterface, _: Int -> }
        with(builder) {
            setMessage("${city.name}, ${city.country} [${city.coord.lat},${city.coord.lon}]")
            setPositiveButton("OK", DialogInterface.OnClickListener(function = positiveButtonClick))
            show()
        }
    }

    fun onCityClicked(city: City) {
        this.city = city
        val mapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map_container) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setupToolbar() {
        supportActionBar?.let { actionBar ->
            actionBar.setBackgroundDrawable(ColorDrawable(Color.parseColor(uiViewModel.primaryColor)))
            colorizeToolbarOverflowButton(toolbar, Color.parseColor(uiViewModel.toolbarTextColor))
            val backArrow = ContextCompat.getDrawable(this, R.drawable.toolbar_back_arrow)
            actionBar.setHomeAsUpIndicator(backArrow);
            toolbar.setTitleTextColor(Color.parseColor(uiViewModel.toolbarTextColor))
        }
    }

    private fun colorizeToolbarOverflowButton(toolbar: Toolbar, color: Int): Boolean {
        val overflowIcon = toolbar.overflowIcon ?: return false
        toolbar.overflowIcon = getTintedDrawable(overflowIcon, color)
        return true
    }

    private fun getTintedDrawable(inputDrawable: Drawable, color: Int): Drawable {
        val wrapDrawable = DrawableCompat.wrap(inputDrawable)
        DrawableCompat.setTint(wrapDrawable, color)
        DrawableCompat.setTintMode(wrapDrawable, PorterDuff.Mode.SRC_IN)
        return wrapDrawable
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_toggle_theme -> {
            if (currentTheme == UITheme.LIGHT) {
                currentTheme = UITheme.DARK
            } else {
                currentTheme = UITheme.LIGHT
            }
            uiViewModel.currentTheme = currentTheme
            val fileName: String = if (orientation == Configuration.ORIENTATION_PORTRAIT) uiViewModel.backgroundLottieJsonFileName else ""
            uiViewModel.lottieAnimation(lottieBackgroundView, fileName, LottieDrawable.INFINITE)
            setupToolbar()
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun fetchActivity(): MainActivity {
        return this
    }

    override fun fetchRootView(): View {
        return activityMainBinding.root
    }

    override fun fetchcityListViewModel() : CityListViewModel? { return null }

    override fun startMainProgressBar() {
        main_progress_bar.visibility = View.VISIBLE
    }

    override fun stopMainProgressBar() {
        main_progress_bar.visibility = View.GONE
    }

}
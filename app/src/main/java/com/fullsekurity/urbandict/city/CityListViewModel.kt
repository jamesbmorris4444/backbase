package com.fullsekurity.urbandict.city

import android.app.Application
import android.content.res.AssetManager
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fullsekurity.urbandict.R
import com.fullsekurity.urbandict.activity.Callbacks
import com.fullsekurity.urbandict.logger.LogUtils
import com.fullsekurity.urbandict.recyclerview.RecyclerViewViewModel
import com.fullsekurity.urbandict.repository.Repository
import com.fullsekurity.urbandict.repository.storage.City
import com.fullsekurity.urbandict.ui.UIViewModel
import com.fullsekurity.urbandict.utils.DaggerViewModelDependencyInjector
import com.fullsekurity.urbandict.utils.Utils
import com.fullsekurity.urbandict.utils.ViewModelInjectorModule
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.json.JSONException
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.InputStream
import javax.inject.Inject


class CityListViewModelFactory(private val callbacks: Callbacks) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CityListViewModel(callbacks) as T
    }
}

class CityListViewModel(private val callbacks: Callbacks) : RecyclerViewViewModel(callbacks.fetchActivity().application) {

    override var adapter: CityAdapter = CityAdapter(callbacks)
    override val itemDecorator: RecyclerView.ItemDecoration? = null
    val listIsVisible: ObservableField<Boolean> = ObservableField(true)

    @Inject
    lateinit var uiViewModel: UIViewModel
    @Inject
    lateinit var repository: Repository

    init {
        DaggerViewModelDependencyInjector.builder()
            .viewModelInjectorModule(ViewModelInjectorModule(callbacks.fetchActivity()))
            .build()
            .inject(this)
        showCities(getCitiesList())
    }

    override fun setLayoutManager(): RecyclerView.LayoutManager {
        return object : LinearLayoutManager(getApplication<Application>().applicationContext) {
            override fun canScrollHorizontally(): Boolean {
                return false
            }

            override fun canScrollVertically(): Boolean {
                return true
            }
        }
    }

    // observable used for two-way donations binding. Values set into this field will show in view.
    // Text typed into EditText in view will be stored into this field after each character is typed.
    var editTextNameInput: ObservableField<String> = ObservableField("")
    fun onTextNameChanged(key: CharSequence, start: Int, before: Int, count: Int) {
        // within "string", the "count" characters beginning at index "start" have just replaced old text that had length "before"
    }
    var hintTextName: ObservableField<String> = ObservableField(getApplication<Application>().applicationContext.getString(R.string.citys_hint_text))
    var editTextNameVisibility: ObservableField<Int> = ObservableField(View.VISIBLE)

    private fun showCities(cityList: List<City>?) {
        if (cityList == null) {
            listIsVisible.set(false)
        } else {
            listIsVisible.set(cityList.isNotEmpty())
            adapter.addAll(cityList.sortedBy{ city -> Utils.cityComparison(city) })
        }
    }

    private fun getCitiesList(): List<City>? {
        val am: AssetManager = callbacks.fetchActivity().resources.assets
        val inputStream: InputStream = am.open("cities.json")
        val builder = GsonBuilder()
        val gson = builder.create()
        var citysListJson: String? = null
        try {
            citysListJson = readTextStream(inputStream)
        } catch (e: FileNotFoundException) {
            LogUtils.E(LogUtils.FilterTags.withTags(LogUtils.TagFilter.CIT), e)
        } catch (e: JSONException) {
            LogUtils.E(LogUtils.FilterTags.withTags(LogUtils.TagFilter.CIT), "JSON Exception: ", e)
        } catch (e: Exception) {
            LogUtils.E(LogUtils.FilterTags.withTags(LogUtils.TagFilter.CIT), "Unknown Exception: ", e)
        }
        if (citysListJson == null) {
            return null
        } else {
            return gson.fromJson(citysListJson, object: TypeToken<List<City>>(){}.type)
        }
    }

    @Throws(java.lang.Exception::class)
    private fun readTextStream(inputStream: InputStream): String? {
        val result = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } != -1) {
            result.write(buffer, 0, length)
        }
        return result.toString("UTF-8")
    }

}
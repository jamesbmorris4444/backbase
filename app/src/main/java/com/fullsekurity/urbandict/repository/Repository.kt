package com.fullsekurity.urbandict.repository

import android.content.res.AssetManager
import com.fullsekurity.urbandict.R
import com.fullsekurity.urbandict.activity.Callbacks
import com.fullsekurity.urbandict.activity.MainActivity
import com.fullsekurity.urbandict.logger.LogUtils
import com.fullsekurity.urbandict.modal.StandardModal
import com.fullsekurity.urbandict.repository.storage.City
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.json.JSONException
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.util.concurrent.TimeUnit

class Repository(private val callbacks: Callbacks) {

    fun getCities(showCitys: (citysList: List<City>?) -> Unit) {
        var disposable: Disposable? = null
        disposable = getCitiesList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .timeout(15L, TimeUnit.SECONDS)
            .subscribe ({ citysResponse ->
                disposable?.dispose()
                showCitys(citysResponse)
            },
            { throwable ->
                disposable?.dispose()
                showCitys(null)
                getUrbanDictionaryCitysFailure(callbacks.fetchActivity(),"getUrbanDictionaryCitys", throwable)
            })
    }

    private fun getUrbanDictionaryCitysFailure(activity: MainActivity, method: String, throwable: Throwable) {
        LogUtils.E(LogUtils.FilterTags.withTags(LogUtils.TagFilter.EXC), method, throwable)
        StandardModal(
            activity,
            modalType = StandardModal.ModalType.STANDARD,
            titleText = activity.getString(R.string.std_modal_urban_dictionary_failure_title),
            bodyText = activity.getString(R.string.std_modal_urban_dictionary_failure_body),
            positiveText = activity.getString(R.string.std_modal_ok),
            dialogFinishedListener = object : StandardModal.DialogFinishedListener {
                override fun onPositive(string: String) { }
                override fun onNegative() { }
                override fun onNeutral() { }
                override fun onBackPressed() { }
            }
        ).show(activity.supportFragmentManager, "MODAL")
    }

    private fun getCitiesList(): Single<List<City>> {
        return Single.create { emitter -> emitter.onSuccess(getCitiesListFromJson()) }
    }

    private fun getCitiesListFromJson(): List<City> {
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
        val cityList: List<City> = gson.fromJson(citysListJson, object: TypeToken<List<City>>(){}.type)
        if (citysListJson == null) {
            return listOf()
        } else {
            return cityList
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
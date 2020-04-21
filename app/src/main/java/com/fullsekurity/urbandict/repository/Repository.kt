package com.fullsekurity.urbandict.repository

import com.fullsekurity.urbandict.R
import com.fullsekurity.urbandict.activity.Callbacks
import com.fullsekurity.urbandict.activity.MainActivity
import com.fullsekurity.urbandict.logger.LogUtils
import com.fullsekurity.urbandict.modal.StandardModal
import com.fullsekurity.urbandict.repository.network.APIClient
import com.fullsekurity.urbandict.repository.network.APIInterface
import com.fullsekurity.urbandict.repository.storage.City
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class Repository(private val callbacks: Callbacks) {

    private val citysService: APIInterface = APIClient.client

    fun getUrbanDictionaryCitys(term: String, showCitys: (citysList: List<City>?) -> Unit) {
        var disposable: Disposable? = null
        disposable = citysService.getCitys(term)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .timeout(15L, TimeUnit.SECONDS)
            .subscribe ({ citysResponse ->
                disposable?.dispose()
                showCitys(citysResponse.list)
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

}
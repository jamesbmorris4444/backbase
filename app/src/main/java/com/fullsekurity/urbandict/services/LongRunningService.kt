package com.fullsekurity.urbandict.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import com.fullsekurity.urbandict.logger.LogUtils


class LongRunningService : Service() {

    private val TAG = LongRunningService::class.java.simpleName
    private val binder = LocalBinder()
    private lateinit var handler: Handler
    private var progress = 0
    private var maxValue = 5000
    private var isPaused = true
    private lateinit var serviceCallbacks: ServiceCallbacks

    fun setServiceCallbacks(serviceCallbacks: ServiceCallbacks) {
        this.serviceCallbacks = serviceCallbacks
    }

    override fun onBind(intent: Intent?): IBinder? {
        LogUtils.D(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM), String.format("LocationService: onBind()"))
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        LogUtils.D(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM), String.format("LocationService: onDestroy()"))
        isPaused = true
        pretendLongRunningTask()
    }

    override fun onCreate() {
        LogUtils.D(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM), String.format("LocationService: onCreate()"))
        handler = Handler()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        LogUtils.D(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM), String.format("LocationService: onUnbind()"))
        return true
    }

    inner class LocalBinder : Binder() {
        fun getService(): LongRunningService = this@LongRunningService
    }

    fun startPretendLongRunningTask() {
        isPaused = false
        serviceCallbacks.setServiceProgress(0)
        progress = 0
        serviceCallbacks.setProgressMaxValue(maxValue)
        pretendLongRunningTask()
    }

    fun pausePretendLongRunningTask() {
        isPaused = true
        pretendLongRunningTask()
    }

    fun resumePretendLongRunningTask() {
        isPaused = false
        serviceCallbacks.setProgressMaxValue(maxValue)
        pretendLongRunningTask()
    }

    private fun pretendLongRunningTask() {
        val runnable: Runnable = object : Runnable {
            override fun run() {
                if (progress >= maxValue || isPaused) {
                    LogUtils.D(TAG, LogUtils.FilterTags.withTags(LogUtils.TagFilter.THM), String.format("pretendLongRunningTask: removeCallbacks()"))
                    handler.removeCallbacks(this)
                } else {
                    progress += 10 // increment the progress
                    handler.postDelayed(this, 100)
                    serviceCallbacks.setServiceProgress(progress)
                }
            }
        }
        handler.postDelayed(runnable, 100)
    }

}
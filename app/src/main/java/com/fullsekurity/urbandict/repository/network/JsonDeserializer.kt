package com.fullsekurity.urbandict.repository.network

import android.util.Log
import com.fullsekurity.urbandict.logger.LogUtils
import com.fullsekurity.urbandict.repository.storage.City
import com.fullsekurity.urbandict.utils.Constants
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type

internal class CitysJsonDeserializer : JsonDeserializer<Any> {

    private val TAG = CitysJsonDeserializer::class.java.simpleName

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Any? {
        // Jim Morris, 12/9/2019
        // This code does not execute, although it did while I was using Retrofit and OkHttp callbacks
        // This code stopped executing when I added the line
        //     .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        // to APIClient, which was added for the purpose of using RxJava calls for Retrofit and OkHttp, instead of callbacks
        var citys: ArrayList<City>? = null
        try {
            val jsonObject = json.asJsonObject
            val citysJsonArray = jsonObject.getAsJsonArray(Constants.URBANDICT_ARRAY_DATA_TAG)
            citys = ArrayList(citysJsonArray.size())
            for (i in 0 until citysJsonArray.size()) {
                val dematerialized = context.deserialize<Any>(citysJsonArray.get(i), City::class.java)
                citys.add(dematerialized as City)
            }
        } catch (e: JsonParseException) {
            LogUtils.E(LogUtils.FilterTags.withTags(LogUtils.TagFilter.EXC), e)
        }
        return citys
    }

}
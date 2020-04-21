package com.fullsekurity.urbandict.repository.network

import com.fullsekurity.urbandict.repository.storage.City
import com.fullsekurity.urbandict.utils.Constants
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface APIInterface {
    @Headers(
        Constants.URBANDICT_RAPID_API_HOST,
        Constants.URBANDICT_RAPID_API_KEY
    )
    @GET("define")
    fun getCitys(
        @Query(Constants.URBANDICT_TERM) term: String
    ): Flowable<CitysResponse>
}

data class CitysResponse (
    val list: List<City>
)
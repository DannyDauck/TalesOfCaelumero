package com.example.talesofcaelumora.data.remote

import com.example.talesofcaelumora.data.datamodel.TimeData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

const val BASE_URL = "https://worldtimeapi.org/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface DateTimeApiService {

    object DataTimeApi {
        val retrofitService: DateTimeApiService by lazy { retrofit.create(DateTimeApiService::class.java) }
    }

    @GET("timezone/{area}/{location}/")
    suspend fun getTime(
        @Path("area") area: String,
        @Path("location") location: String,
    ): TimeData

}


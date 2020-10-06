package ir.cafebazaar.foursquare.repository.api

import ir.cafebazaar.foursquare.repository.api.service.ApiVenueService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    private const val URL = "https://api.foursquare.com/v2/venues/"

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiVenueService: ApiVenueService = getRetrofit().create(ApiVenueService::class.java)
}
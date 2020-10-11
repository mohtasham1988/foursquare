package ir.cafebazaar.foursquare.repository.api.service

import ir.cafebazaar.foursquare.repository.model.Base
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiVenueService {

    @GET("explore/")
    fun getVenueExplore(@Query("client_id")  clientId : String,
                                @Query("client_secret")  clientSecret : String,
                                @Query("v")  v : String,
                                @Query("ll")   ll : String,
                                @Query("query")   query : String,
                                @Query("limit")   limit : Int,
                                @Query("offset")   offset : Int):Call<Base>

}
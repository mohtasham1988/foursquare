package ir.cafebazaar.foursquare.repository.api.service

import ir.cafebazaar.foursquare.repository.model.Base
import retrofit2.http.GET

interface ApiVenueService {

    @GET("explore")
    suspend fun getVenueExplore(): List<Base>

}
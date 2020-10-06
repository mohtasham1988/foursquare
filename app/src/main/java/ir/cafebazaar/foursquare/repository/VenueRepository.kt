package ir.cafebazaar.foursquare.repository

import ir.cafebazaar.foursquare.repository.api.RetrofitBuilder

class VenueRepository() {

    companion object

    val loadFromCash: Boolean = false

    suspend fun getVenueExplore() {
        if (loadFromCash)
            RetrofitBuilder.apiVenueService.getVenueExplore()
        else
            TODO() //Read From cash
    }
}
package ir.cafebazaar.foursquare.fragment.model

import ir.cafebazaar.foursquare.repository.VenueRepository
import ir.cafebazaar.foursquare.utils.Constant

class MainFragmentModel {
    var offset = 1
    var mainRepository = VenueRepository()

    fun getVenueList() = mainRepository.venueList

    suspend fun readVenueList() {
        mainRepository.readVenueList(
            Constant.clientId,
            Constant.clientSecret,
            "20190425",
            "1.283644,103.860753",
            "steak",
            10,
            offset
        )
    }
}
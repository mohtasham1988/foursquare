package ir.cafebazaar.foursquare.fragment.model

import ir.cafebazaar.foursquare.FoursquareApp
import ir.cafebazaar.foursquare.repository.VenueRepository
import ir.cafebazaar.foursquare.utils.Constant

class MainFragmentModel {
    private var mainRepository = VenueRepository()

    fun getVenueList() = mainRepository.venueList

    suspend fun fetchVenueList(offset:Int) {
        mainRepository.fetchVenueList(
            Constant.clientId,
            Constant.clientSecret,
            "20190425",
            "1.283644,103.860753",
            "steak",
            10,
            offset
        )
    }

    fun getOffset(): Int =
        FoursquareApp.mInstance.getSharedPreferences().getInt("offset", 0)

    fun addOffset() {
        FoursquareApp.mInstance.getSharedPreferences().edit().putInt("offset", getOffset() + 1)
            .apply()
    }

}
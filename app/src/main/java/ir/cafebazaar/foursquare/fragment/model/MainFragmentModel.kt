package ir.cafebazaar.foursquare.fragment.model

import androidx.lifecycle.LiveData
import ir.cafebazaar.foursquare.FoursquareApp
import ir.cafebazaar.foursquare.repository.VenueRepository
import ir.cafebazaar.foursquare.repository.model.Venue
import ir.cafebazaar.foursquare.utils.Constant

class MainFragmentModel {
    private var mainRepository = VenueRepository(this)


    suspend fun fetchVenueList(offset:Int,ll : String): LiveData<List<Venue>> {
        return mainRepository.fetchVenueList(
            Constant.clientId,
            Constant.clientSecret,
            "20190425",
            ll,
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
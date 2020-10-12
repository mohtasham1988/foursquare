package ir.cafebazaar.foursquare.fragment.model

import androidx.lifecycle.LiveData
import ir.cafebazaar.foursquare.FoursquareApp
import ir.cafebazaar.foursquare.repository.VenueRepository
import ir.cafebazaar.foursquare.repository.api.respons.BaseResponse
import ir.cafebazaar.foursquare.repository.model.Venue
import ir.cafebazaar.foursquare.utils.Constant

class MainFragmentModel {
    private var mainRepository = VenueRepository(this)
    var offset: Int = 10


    suspend fun fetchVenueList(ll : String): LiveData<BaseResponse<List<Venue>>> {
        return mainRepository.fetchVenueList(
            Constant.clientId,
            Constant.clientSecret,
            "20190425",
            ll,
            "",
            10,
            offset
        )
    }

    fun getLastOffset(): Int =
        FoursquareApp.mInstance.getSharedPreferences().getInt("offset", 10)

    fun setLastOffset(offset:Int) {
        FoursquareApp.mInstance.getSharedPreferences().edit().putInt("offset",offset)
            .apply()
    }

    fun resetOffset() {
        FoursquareApp.mInstance.getSharedPreferences().edit().putInt("offset", 10)
            .apply()
    }
}
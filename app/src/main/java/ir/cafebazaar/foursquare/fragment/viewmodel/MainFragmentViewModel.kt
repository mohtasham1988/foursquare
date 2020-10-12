package ir.cafebazaar.foursquare.fragment.viewmodel

import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import ir.cafebazaar.foursquare.fragment.model.MainFragmentModel
import ir.cafebazaar.foursquare.fragment.view.MainFragment
import ir.cafebazaar.foursquare.utils.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainFragmentViewModel() : ViewModel() {
    var mainFragmentModel = MainFragmentModel()


    suspend fun fetchVenueList(fragment: MainFragment, ll: String) = withContext(Dispatchers.Main) {
        Log.d("vhdmht", "fetchVenueList: getOffset() " + mainFragmentModel.getLastOffset())
        mainFragmentModel.fetchVenueList(ll).observe(fragment,
            Observer {
                if (it != null && it.status == Constant.Status.SUCCESS) {
                    fragment.refresh(it.data!!)
                    mainFragmentModel.offset += 10
                    fragment.loading =
                        it.data.size == 10  // if array less than 10 we reach to end list
                    Log.d("vhdmht", "getVenueList: " + it.data.size)
                } else {
                    Log.d("vhdmht", "getVenueList: err ")
                }
                fragment.showLoading(false)
            })
    }

    fun resetOffset() = mainFragmentModel.resetOffset()

}
package ir.cafebazaar.foursquare.fragment.viewmodel

import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import ir.cafebazaar.foursquare.fragment.model.MainFragmentModel
import ir.cafebazaar.foursquare.fragment.view.MainFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainFragmentViewModel() : ViewModel() {
    var mainFragmentModel = MainFragmentModel()


    suspend fun fetchVenueList(fragment: MainFragment, ll: String) = withContext(Dispatchers.Main) {
        Log.d("vhdmht", "fetchVenueList: ")
        mainFragmentModel.fetchVenueList(mainFragmentModel.getOffset(), ll).observe(fragment,
            Observer {
                if (it != null && it.size > 0) {
                    fragment.refresh(it)
                    fragment.loading = it.size == 10  // if array less than 10 we reach to end list
                    mainFragmentModel.addOffset()
                    Log.d("vhdmht", "getVenueList: ")
                } else {
                    fragment.showLoading(false)
                    Log.d("vhdmht", "getVenueList: err ")
                }
            })
    }
}
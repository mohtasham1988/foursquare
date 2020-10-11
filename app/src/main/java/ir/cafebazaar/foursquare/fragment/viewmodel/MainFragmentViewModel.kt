package ir.cafebazaar.foursquare.fragment.viewmodel

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import ir.cafebazaar.foursquare.fragment.model.MainFragmentModel
import ir.cafebazaar.foursquare.fragment.view.MainFragment
import ir.cafebazaar.foursquare.utils.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainFragmentViewModel() : ViewModel() {
    var mainFragmentModel = MainFragmentModel()

    fun getVenueList(fragment: MainFragment) {
        mainFragmentModel.getVenueList().observe(fragment, Observer {
            if (it.status == Constant.Status.SUCCESS && it.data?.response?.groups?.size!! > 0) {
                fragment.refresh(it.data.response.groups[1].items)
                mainFragmentModel.addOffset()
            } else
                fragment.showLoading(false)
        })
    }

    suspend fun fetchVenueList() = withContext(Dispatchers.IO) {
        mainFragmentModel.fetchVenueList(mainFragmentModel.getOffset())
    }
}
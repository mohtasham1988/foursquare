package ir.cafebazaar.foursquare.fragment.viewmodel

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import ir.cafebazaar.foursquare.fragment.model.MainFragmentModel
import ir.cafebazaar.foursquare.fragment.view.MainFragment
import ir.cafebazaar.foursquare.repository.VenueRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainFragmentViewModel(venueRepository: VenueRepository) : ViewModel() {
    var mainFragmentModel = MainFragmentModel()

    fun getVenueList(fragment: MainFragment) {
        mainFragmentModel.getVenueList().observe(fragment, Observer {
            if (it.data?.response?.groups?.size!! > 0)
                fragment.refresh(it.data.response.groups[1].items)
            else
                fragment.showLoading(false)
        })
    }

    suspend fun readVenueList() = withContext(Dispatchers.IO) {
        mainFragmentModel.readVenueList()
    }
}
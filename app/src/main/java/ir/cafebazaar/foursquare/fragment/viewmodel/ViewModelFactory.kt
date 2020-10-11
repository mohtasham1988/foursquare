package ir.cafebazaar.foursquare.fragment.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ir.cafebazaar.foursquare.repository.VenueRepository

class ViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainFragmentViewModel::class.java)) {
            return MainFragmentViewModel() as T
        }
        throw IllegalArgumentException("Unknown class name")
    }

}
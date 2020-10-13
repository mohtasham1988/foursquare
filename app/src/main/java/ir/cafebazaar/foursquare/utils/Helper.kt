package ir.cafebazaar.foursquare.utils

import android.content.Context
import android.location.Location
import ir.cafebazaar.foursquare.FoursquareApp

class Helper {

    companion object {
        val KEY_REQUESTING_LOCATION_UPDATES = "requesting_locaction_updates"

        fun requestingLocationUpdates(context: Context?): Boolean {
            return FoursquareApp.mInstance.getSharedPreferences()
                .getBoolean(
                    KEY_REQUESTING_LOCATION_UPDATES,
                    false
                )
        }


        fun setRequestingLocationUpdates(context: Context?, requestingLocationUpdates: Boolean) {
            FoursquareApp.mInstance.getSharedPreferences()
                .edit()
                .putBoolean(
                    KEY_REQUESTING_LOCATION_UPDATES,
                    requestingLocationUpdates
                )
                .apply()
        }

        fun getLocationText(location: Location?): String? {
            return if (location == null) "Unknown location" else "(" + location.latitude + ", " + location.longitude + ")"
        }

    }
}
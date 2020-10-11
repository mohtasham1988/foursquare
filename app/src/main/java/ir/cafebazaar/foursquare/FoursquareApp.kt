package ir.cafebazaar.foursquare

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

class FoursquareApp : Application() {

    companion object {
        lateinit var mInstance: FoursquareApp

    }

    override fun onCreate() {
        super.onCreate()
        mInstance = this
    }

    fun getSharedPreferences(): SharedPreferences =
          mInstance.applicationContext.getSharedPreferences(
            "default",
            Context.MODE_PRIVATE
        )

}
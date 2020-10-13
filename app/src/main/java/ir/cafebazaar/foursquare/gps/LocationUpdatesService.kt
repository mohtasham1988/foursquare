package ir.cafebazaar.foursquare.gps

import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.location.Location
import android.os.*
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.*
import ir.cafebazaar.foursquare.R
import ir.cafebazaar.foursquare.utils.Helper

class LocationUpdatesService : Service() {

    private val mBinder: IBinder = LocalBinder()

    private val UPDATE_INTERVAL_IN_MILLISECONDS: Long = 15 * 60 * 1000


    private val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS: Long = 2 * 60 * 1000

    private var mChangingConfiguration = false

    private var mNotificationManager: NotificationManager? = null


    private var mLocationRequest: LocationRequest? = null

    private var mFusedLocationClient: FusedLocationProviderClient? = null


    private var mLocationCallback: LocationCallback? = null

    private var mServiceHandler: Handler? = null

    private val TAG = LocationUpdatesService::class.java.simpleName
    private val CHANNEL_ID = "channel_01"
    private var mLocation: Location? = null

    companion object {
        private var PACKAGE_NAME = "ir.cafebazaar.foursquare"
        val ACTION_BROADCAST: String = PACKAGE_NAME + ".broadcast"
        val EXTRA_LOCATION: String = PACKAGE_NAME + ".location"
    }


    override fun onCreate() {
        Log.d(TAG, "onCreate: ")
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                onNewLocation(locationResult.lastLocation)
            }
        }
        createLocationRequest()
        getLastLocation()
        val handlerThread = HandlerThread(TAG)
        handlerThread.start()
        mServiceHandler = Handler(handlerThread.looper)
        mNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = getString(R.string.app_name)
            // Create the channel for the notification
            val mChannel = NotificationChannel(
                CHANNEL_ID,
                name,
                NotificationManager.IMPORTANCE_DEFAULT
            )

            // Set the Notification Channel for the Notification Manager.
            mNotificationManager!!.createNotificationChannel(mChannel)
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Log.i(TAG, "Service started")
        val startedFromNotification = intent.getBooleanExtra(
            "startedFromNotification",
            false
        )

        // We got here because the user decided to remove location updates from the notification.
        if (startedFromNotification) {
            removeLocationUpdates()
            stopSelf()
        }
        // Tells the system to not try to recreate the service after it has been killed.
        return START_NOT_STICKY
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        mChangingConfiguration = true
    }


    override fun onDestroy() {
        Log.d(TAG, "onDestroy: ")
        mServiceHandler!!.removeCallbacksAndMessages(null)
    }


    /**
     * Makes a request for location updates. Note that in this sample we merely log the
     * [SecurityException].
     */
    fun requestLocationUpdates() {
        Log.i(TAG, "Requesting location updates")
        Helper.setRequestingLocationUpdates(this, true)
        startService(Intent(applicationContext, LocationUpdatesService::class.java))

        try {
            mFusedLocationClient!!.requestLocationUpdates(
                mLocationRequest,
                mLocationCallback, Looper.myLooper()
            )
        } catch (unlikely: SecurityException) {
            Helper.setRequestingLocationUpdates(this, false)
            Log.e(
                TAG,
                "Lost location permission. Could not request updates. $unlikely"
            )
        }
    }

    /**
     * Removes location updates. Note that in this sample we merely log the
     * [SecurityException].
     */
    private fun removeLocationUpdates() {
        Log.i(TAG, "Removing location updates")
        try {
            mFusedLocationClient!!.removeLocationUpdates(mLocationCallback)
            Helper.setRequestingLocationUpdates(this, false)
            stopSelf()
        } catch (unlikely: SecurityException) {
            Helper.setRequestingLocationUpdates(this, true)
            Log.e(
                TAG,
                "Lost location permission. Could not remove updates. $unlikely"
            )
        }
    }


    private fun getLastLocation() {
        try {
            mFusedLocationClient!!.lastLocation
                .addOnCompleteListener { task ->
                    if (task.isSuccessful && task.result != null) {
                        mLocation = task.result
                    } else {
                        Log.w(TAG, "Failed to get location.")
                    }
                }
        } catch (unlikely: SecurityException) {
            Log.e(TAG, "Lost location permission.$unlikely")
        }
    }

    private fun onNewLocation(location: Location) {
        Log.i(TAG, "New location: $location")
        mLocation = location

        // Notify anyone listening for broadcasts about the new location.
        val intent = Intent(ACTION_BROADCAST)
        intent.putExtra(EXTRA_LOCATION, location)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)

        // Update notification content if running as a foreground service.
        if (serviceIsRunningInForeground(this)) {
//            mNotificationManager!!.notify(LocationUpdatesService.NOTIFICATION_ID, getNotification())
        }
    }

    /**
     * Sets the location request parameters.
     */
    private fun createLocationRequest() {
        mLocationRequest = LocationRequest().apply {
            interval = UPDATE_INTERVAL_IN_MILLISECONDS
            fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
            priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
            maxWaitTime = 60 * 60 * 1000
        }

    }

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    class LocalBinder : Binder() {
        val service: LocationUpdatesService
            get() = LocationUpdatesService()
    }

    /**
     * Returns true if this is a foreground service.
     *
     * @param context The [Context].
     */
    private fun serviceIsRunningInForeground(context: Context): Boolean {
        val manager = context.getSystemService(
            ACTIVITY_SERVICE
        ) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (javaClass.name == service.service.className) {
                if (service.foreground) {
                    return true
                }
            }
        }
        return false
    }

    override fun onBind(intent: Intent?): IBinder? {
        // Called when a client (MainActivity in case of this sample) comes to the foreground
        // and binds with this service. The service should cease to be a foreground service
        // when that happens.
        Log.i(TAG, "in onBind()")
        stopForeground(true)
        mChangingConfiguration = false
        return mBinder
    }

    override fun onRebind(intent: Intent?) {
        // Called when a client (MainActivity in case of this sample) returns to the foreground
        // and binds once again with this service. The service should cease to be a foreground
        // service when that happens.
        Log.i(TAG, "in onRebind()")
        stopForeground(true)
        mChangingConfiguration = false
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.i(TAG, "Last client unbound from service")

        // Called when the last client (MainActivity in case of this sample) unbinds from this
        // service. If this method is called due to a configuration change in MainActivity, we
        // do nothing. Otherwise, we make this service a foreground service.
        if (!mChangingConfiguration && Helper.requestingLocationUpdates(this)) {
            Log.i(TAG, "Starting foreground service")
            /*
            // TODO(developer). If targeting O, use the following code.
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
                mNotificationManager.startServiceInForeground(new Intent(this,
                        LocationUpdatesService.class), NOTIFICATION_ID, getNotification());
            } else {
                startForeground(NOTIFICATION_ID, getNotification());
            }
             */
            //startForeground(NOTIFICATION_ID, getNotification())
        }
        return true // Ensures onRebind() is called when a client re-binds.
    }
}
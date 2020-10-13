package ir.cafebazaar.foursquare.fragment.view

import android.content.*
import android.location.Location
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.cafebazaar.foursquare.FoursquareApp
import ir.cafebazaar.foursquare.R
import ir.cafebazaar.foursquare.adapter.VenueAdapter
import ir.cafebazaar.foursquare.databinding.FragmentMainBinding
import ir.cafebazaar.foursquare.fragment.viewmodel.MainFragmentViewModel
import ir.cafebazaar.foursquare.fragment.viewmodel.ViewModelFactory
import ir.cafebazaar.foursquare.gps.LocationUpdatesService
import ir.cafebazaar.foursquare.interfaces.iVenueListener
import ir.cafebazaar.foursquare.repository.model.Venue
import ir.cafebazaar.foursquare.utils.Helper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class MainFragment : Fragment(), iVenueListener {
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var binding: FragmentMainBinding
    private lateinit var venueAdapter: VenueAdapter
    private val scope = CoroutineScope(Job() + Dispatchers.Main)
    var loading = true

    private val viewModel =
        ViewModelFactory().create(MainFragmentViewModel::class.java)


    private var mService: LocationUpdatesService? = null
    private var myReceiver: MyReceiver? = null
    private var mBound = false

    private class MyReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val location =
                intent.getParcelableExtra<Location>(LocationUpdatesService.EXTRA_LOCATION)
            if (location != null) {
                Toast.makeText(
                    context, Helper.getLocationText(location),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private val mServiceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder: LocationUpdatesService.LocalBinder =
                service as LocationUpdatesService.LocalBinder
            mService = binder.service
            mBound = true
        }

        override fun onServiceDisconnected(name: ComponentName) {
            mService = null
            mBound = false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        myReceiver = MyReceiver()

        viewModel.resetOffset()
        mLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.layoutManager = mLayoutManager

        venueAdapter = VenueAdapter(this)
        binding.recyclerView.adapter = venueAdapter
        fetchData()

        var pastVisiblesItems: Int
        var visibleItemCount: Int
        var totalItemCount: Int

        binding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = mLayoutManager.childCount
                    totalItemCount = mLayoutManager.itemCount
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition()
                    if (loading) {
                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            loading = false
                            fetchData()
                        }
                    }
                }
            }
        })
    }

    private fun fetchData() {
        showLoading(true)
        scope.launch {
            viewModel.fetchVenueList(this@MainFragment, "36.294281, 59.602459")
        }
    }

    override fun onClick() {
        fragmentManager?.beginTransaction()?.replace(
            R.id.container,
            DetailsFragment()
        )
            ?.addToBackStack("detailsFragment")?.commit()
    }

    fun refresh(it: List<Venue>) {

        val size = venueAdapter.mList.size
        venueAdapter.mList.addAll(it)
        venueAdapter.notifyItemRangeInserted(size, venueAdapter.mList.size)
    }

    fun showLoading(isShow: Boolean) {
        binding.progressBar.visibility = (if (isShow) View.VISIBLE else View.GONE)
    }

    override fun onStart() {
        super.onStart()
        activity?.bindService(
            Intent(activity, LocationUpdatesService::class.java), mServiceConnection,
            Context.BIND_AUTO_CREATE

        )
    }

    override fun onResume() {
        super.onResume()
        LocalBroadcastManager.getInstance(FoursquareApp.mInstance.applicationContext)
            .registerReceiver(
                myReceiver!!,
                IntentFilter(LocationUpdatesService.ACTION_BROADCAST)
            )
    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(FoursquareApp.mInstance.applicationContext)
            .unregisterReceiver(
                myReceiver!!
            )
        super.onPause()
    }

    override fun onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            activity!!.unbindService(mServiceConnection)
            mBound = false
        }

        super.onStop()
    }
}
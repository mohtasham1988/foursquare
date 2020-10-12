package ir.cafebazaar.foursquare.fragment.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ir.cafebazaar.foursquare.R
import ir.cafebazaar.foursquare.adapter.VenueAdapter
import ir.cafebazaar.foursquare.databinding.FragmentMainBinding
import ir.cafebazaar.foursquare.fragment.viewmodel.MainFragmentViewModel
import ir.cafebazaar.foursquare.fragment.viewmodel.ViewModelFactory
import ir.cafebazaar.foursquare.interfaces.iVenueListener
import ir.cafebazaar.foursquare.repository.model.Venue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class MainFragment : Fragment(), iVenueListener {
    private lateinit var mLayoutManager: LinearLayoutManager
    private lateinit var binding: FragmentMainBinding
    lateinit var venueAdapter: VenueAdapter
    val scope = CoroutineScope(Job() + Dispatchers.Main)
    var loading = true

    private val viewModel =
        ViewModelFactory().create(MainFragmentViewModel::class.java)

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
                    visibleItemCount = mLayoutManager.getChildCount()
                    totalItemCount = mLayoutManager.getItemCount()
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
            viewModel.fetchVenueList(this@MainFragment,"36.294281, 59.602459")
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
}
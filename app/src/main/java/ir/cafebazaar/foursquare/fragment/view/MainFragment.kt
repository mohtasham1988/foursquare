package ir.cafebazaar.foursquare.fragment.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ir.cafebazaar.foursquare.R
import ir.cafebazaar.foursquare.adapter.VenueAdapter
import ir.cafebazaar.foursquare.databinding.FragmentMainBinding
import ir.cafebazaar.foursquare.fragment.viewmodel.MainFragmentViewModel
import ir.cafebazaar.foursquare.interfaces.iVenueListener

class MainFragment : Fragment(), iVenueListener {
    private lateinit var binding: FragmentMainBinding
    private val viewModel =
        ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainFragmentViewModel::class.java)

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
        binding.recyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = VenueAdapter(this)
    }

    override fun onClick() {
        fragmentManager?.beginTransaction()?.replace(
            R.id.container,
            DetailsFragment()
        )
            ?.addToBackStack("detailsFragment")?.commit()
    }
}
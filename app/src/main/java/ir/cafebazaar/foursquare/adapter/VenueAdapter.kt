package ir.cafebazaar.foursquare.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.cafebazaar.foursquare.databinding.RowVenueBinding
import ir.cafebazaar.foursquare.repository.model.Item

class VenueAdapter :
    RecyclerView.Adapter<VenueAdapter.ViewHolder>() {
     var mList = ArrayList<Item>()
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val binding = RowVenueBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(itemView: RowVenueBinding) :
        RecyclerView.ViewHolder(itemView.root)
}
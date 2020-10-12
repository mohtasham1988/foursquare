package ir.cafebazaar.foursquare.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ir.cafebazaar.foursquare.databinding.RowVenueBinding
import ir.cafebazaar.foursquare.interfaces.iVenueListener
import ir.cafebazaar.foursquare.repository.model.Venue
import kotlinx.android.synthetic.main.row_venue.view.*
import java.math.RoundingMode
import java.text.DecimalFormat

class VenueAdapter(var delegate: iVenueListener) :
    RecyclerView.Adapter<VenueAdapter.ViewHolder>() {
    var mList = ArrayList<Venue>()
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
        val item = mList[position]
        holder.itemView.title.text = item.name
        holder.itemView.categories.text = item.categories[0].name
        val df = DecimalFormat("###,###,###")
        df.roundingMode = RoundingMode.CEILING
        df.format(item.location?.distance).let {
            holder.itemView.dist.text = "distance = $it"
        }
    }

    override fun getItemCount(): Int = mList.size

    inner class ViewHolder(itemView: RowVenueBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        init {
            itemView.root.setOnClickListener {
                delegate.onClick()
            }
        }
    }

}
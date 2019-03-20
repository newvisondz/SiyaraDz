package com.newvisiondz.sayaradz.adapters

import android.content.Context
import android.support.design.card.MaterialCardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation.findNavController
import com.newvisiondz.sayaradz.R
import com.newvisiondz.sayaradz.model.Bid
import com.newvisiondz.sayaradz.views.fragments.TabsDirections

class BidsAdapter(private val bids: MutableList<Bid>, private val context: Context) :
    RecyclerView.Adapter<BidsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view =LayoutInflater.from(this.context).inflate(R.layout.fragment_bid_card,viewGroup,false)
        val viewHolder = ViewHolder(view)
        viewHolder.card.setOnClickListener {
            val bidId = 2
            val action = TabsDirections.actionTabsToBidView()
            action.setBidId(bidId)
            findNavController(view).navigate(action)
            Log.i("Navigating","Tabs (Bids) to Models. BidId: $bidId")
        }
        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
         val bid= this.bids[i]
        viewHolder.bidTitle.text = bid.title
        viewHolder.bidOwner.text = bid.owner
        viewHolder.bidPrice.text = bid.price
        viewHolder.bidCity.text = bid.city
        viewHolder.bidImage.setImageResource(bid.imageId)
    }

    override fun getItemCount(): Int {
        return bids.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card = view.findViewById<MaterialCardView>(R.id.bid_card)
        val bidTitle = view.findViewById<TextView>(R.id.bid_title)
        val bidImage = view.findViewById<ImageView>(R.id.bid_image)
        val bidOwner = view.findViewById<TextView>(R.id.bid_owner)
        val bidCity = view.findViewById<TextView>(R.id.bid_city)
        val bidPrice = view.findViewById<TextView>(R.id.bid_price)
    }

    public fun addBid(bids:List<Bid>) {
        for (bid in bids) {
            this.bids.add(bid)
        }
        notifyDataSetChanged()
    }
}

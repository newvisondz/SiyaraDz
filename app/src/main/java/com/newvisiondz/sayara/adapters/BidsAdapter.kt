package com.newvisiondz.sayara.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.model.Bid
import com.newvisiondz.sayara.views.fragments.TabsDirections
import kotlinx.android.synthetic.main.fragment_bid_card.view.*

class BidsAdapter(private val bids: MutableList<Bid>, private val context: Context) :
    androidx.recyclerview.widget.RecyclerView.Adapter<BidsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(this.context).inflate(R.layout.fragment_bid_card, viewGroup, false)
        val viewHolder = ViewHolder(view)
        viewHolder.card.setOnClickListener {
            val bidId = 2
            val action = TabsDirections.actionTabsToBidView()
            action.setBidId(bidId)
            findNavController(view).navigate(action)
        }
        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val bid = this.bids[i]
        viewHolder.bidModel.text = bid.price.toString()
        viewHolder.bidOwner.text = bid.adresse
        viewHolder.bidPrice.text = bid.price.toString()
        viewHolder.bidAdress.text = bid.adresse
        if (bid.uris.size !=0) {
            viewHolder.bidImage.setImageURI(bid.uris[0])
        }
    }

    override fun getItemCount(): Int {
        return bids.size
    }

    inner class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val card = view.bid_card!!
        val bidModel = view.bid_model!!
        val bidImage = view.bid_image!!
        val bidOwner = view.bid_owner!!
        val bidAdress = view.bidAdress!!
        val bidPrice = view.bid_price!!
    }

    fun addBid(bids: List<Bid>) {
        this.bids.addAll(bids)
        notifyDataSetChanged()
    }
}

package com.newvisiondz.sayara.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.NavHostFragment
import com.newvisiondz.sayara.R


class BidView : androidx.fragment.app.Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bid_view, container, false)
        var bidId = 0
        if (arguments != null) bidId = BidViewArgs.fromBundle(arguments!!).bidId
        Log.i("Navigation Success", "From BidView, BidId: $bidId")

        view?.findViewById<Button>(R.id.compare_button)?.setOnClickListener {
            val action = BidViewDirections.actionBidViewToBidForm()
            //action.setBidId(bidId)
            NavHostFragment.findNavController(this).navigate(action)
            Log.i("Navigating", "BidView to BidForm, BidId: $bidId")
        }

        return view
    }
}

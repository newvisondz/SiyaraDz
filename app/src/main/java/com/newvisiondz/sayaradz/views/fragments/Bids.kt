package com.newvisiondz.sayaradz.views.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.newvisiondz.sayaradz.R
import com.newvisiondz.sayaradz.utils.MessagesUtils
import com.newvisiondz.sayaradz.adapters.BidsAdapter
import com.newvisiondz.sayaradz.model.Bid
import kotlinx.android.synthetic.main.data_entry_dialog.view.*
import kotlinx.android.synthetic.main.fragment_bids.*
import kotlinx.android.synthetic.main.fragment_bids.view.*
import java.util.*


class Bids : androidx.fragment.app.Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private var tmpUris = mutableListOf<Uri>()
    private lateinit var bidsList: MutableList<Bid>
    private var bidsAdapter: BidsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bids, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bidsList = mutableListOf(
            Bid(0, "Mercedes Classe C 220 AMG Line 2015", "ZED AUTO", "Dar Beida", 12.9, 12200.0),
            Bid(1, "Volkswagen Golf 6 Match 2 2013 ", "ZED AUTO", "Dar Beida", 12.9, 12200.0),
            Bid(2, "Mercedes Classe C 220 AMG Line 2015", "ZED AUTO", "Dar Beida", 12.9, 12200.0),
            Bid(3, "Volkswagen Golf 6 Match 2 2013 ", "ZED AUTO", "Dar Beida", 14.9, 1233.0)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(view)
        add_new_bid.setOnClickListener {
            val mBuilder = AlertDialog.Builder(context!!)
            val mView = layoutInflater.inflate(R.layout.data_entry_dialog, null)
            mBuilder.setView(mView)
            val dialog = mBuilder.create()
            dialog.setCanceledOnTouchOutside(true)
            dialog.show()
            mView.btnImg.setOnClickListener {
                val intent = Intent()
                    .setType("image/*").putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                    .setAction(Intent.ACTION_GET_CONTENT)
                startActivityForResult(Intent.createChooser(intent, "Select a file"), 301)
            }
            mView.btnOk.setOnClickListener {
                addItem(mView)
                //TODO post request here
                dialog.dismiss()
            }
            mView.btnCancel.setOnClickListener {
                dialog.cancel()
            }
        }
        view.swipeRefreshBids.setOnRefreshListener {
            val message = MessagesUtils()
            message.displaySnackBar(view, "Nice ")
            swipeRefreshBids.isRefreshing = false
        }
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }


    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    private fun addItem(mView: View) {
        val newBid = Bid(
            Random().nextInt(),
            mView.Model.text.toString(),
            mView.Owner.text.toString(),
            mView.adress.text.toString(),
            mView.price.text.toString().toDouble(),
            mView.current_miles.text.toString().toDouble()
        )
        newBid.uris = tmpUris
        bidsList.add(newBid)
        bids_list.adapter!!.notifyDataSetChanged()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == 301) && (resultCode == Activity.RESULT_OK)) {
            if (data!!.data != null) {
                tmpUris.add(data.data!!)
            } else if (data.clipData != null) {
                val clipArray = data.clipData
                for (i in 0 until clipArray!!.itemCount) {
                    tmpUris.add(clipArray.getItemAt(i).uri)
                }
            }
        }
    }

    private fun initRecyclerView(view: View) {
        view.bids_list.setHasFixedSize(true)
        view.bids_list.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        bidsAdapter = BidsAdapter(bidsList, context as Context)
        view.bids_list.adapter = bidsAdapter
    }

}

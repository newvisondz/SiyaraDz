package com.newvisiondz.sayaradz.views.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.newvisiondz.sayaradz.R
import com.newvisiondz.sayaradz.adapters.BidsAdapter
import com.newvisiondz.sayaradz.adapters.BrandsAdapter
import com.newvisiondz.sayaradz.model.Bid
import com.newvisiondz.sayaradz.model.Brand
import kotlinx.android.synthetic.main.data_entry_dialog.view.*
import kotlinx.android.synthetic.main.fragment_bids.*


class Bids : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bids, container, false)

        val viewManager = LinearLayoutManager(this.context)
        val list = mutableListOf(
            Bid(0, "Mercedes Classe C 220 AMG Line 2015", "ZED AUTO", "Dar Beida", "5.500.000,00", R.drawable.mercedes),
            Bid(1, "Volkswagen Golf 6 Match 2 2013 ", "ZED AUTO", "Dar Beida", "2.500.000,00", R.drawable.volkswagen),
            Bid(2, "Mercedes Classe C 220 AMG Line 2015", "ZED AUTO", "Dar Beida", "5.500.000,00", R.drawable.mercedes),
            Bid(3, "Volkswagen Golf 6 Match 2 2013 ", "ZED AUTO", "Dar Beida", "2.500.000,00", R.drawable.volkswagen)
        )
        val bidsAdapter = BidsAdapter(list, this.context as Context)

        view.findViewById<RecyclerView>(R.id.bids_list).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = bidsAdapter
        }


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add_bid.setOnClickListener {
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
                //                addItem(mView)
                dialog.dismiss()
            }
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

}

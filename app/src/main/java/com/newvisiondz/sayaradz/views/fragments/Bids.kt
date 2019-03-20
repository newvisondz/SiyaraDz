package com.newvisiondz.sayaradz.views.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
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


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Bids.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Bids.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class Bids : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_bids, container, false)

        val viewManager = LinearLayoutManager(this.context)
        val list = mutableListOf(
            Bid(0,"Mercedes Classe C 220 AMG Line 2015","ZED AUTO","Dar Beida","5.500.000,00",R.drawable.mercedes),
            Bid(1,"Volkswagen Golf 6 Match 2 2013 ","ZED AUTO","Dar Beida","2.500.000,00",R.drawable.volkswagen),
            Bid(2,"Mercedes Classe C 220 AMG Line 2015","ZED AUTO","Dar Beida","5.500.000,00",R.drawable.mercedes),
            Bid(3,"Volkswagen Golf 6 Match 2 2013 ","ZED AUTO","Dar Beida","2.500.000,00",R.drawable.volkswagen)
        )
        val bidsAdapter = BidsAdapter(list,this.context as Context)

        view.findViewById<RecyclerView>(R.id.bids_list).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = bidsAdapter
        }


        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            // TODO:
            //throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Bids.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Bids().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

package com.newvisiondz.sayaradz.views.fragments


import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.newvisiondz.sayaradz.R
import com.newvisiondz.sayaradz.adapters.ModelsAdapter
import com.newvisiondz.sayaradz.model.Model

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [Models.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [Models.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class Models : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_models, container, false)
        val viewManager = LinearLayoutManager(this.context)
        var brandId = 0
        if (arguments != null) brandId = ModelsArgs.fromBundle(arguments).brandId
        Log.i("Navigation Success", "From Models, BrandId: $brandId")
        val list = mutableListOf(
            Model(0, "Mercedes-AMG GT", "Roadster", "7.500.000,00", R.drawable.mercedes),
            Model(1, "Volkswagen Golf", "Série 8", "5.000.000,00", R.drawable.volkswagen),
            Model(2, "Škoda Octavia", "Sedan", "4.500.000,00", R.drawable.mercedes),
            Model(3, "Škoda Octavia", "Wagon", "4.700.000,00", R.drawable.volkswagen)
        )
        val modelsAdapter = ModelsAdapter(list, this.context as Context)

        view.findViewById<RecyclerView>(R.id.models_list).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = modelsAdapter
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
        }
//        else {
//            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
//        }
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
         * @return A new instance of fragment Models.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Models().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

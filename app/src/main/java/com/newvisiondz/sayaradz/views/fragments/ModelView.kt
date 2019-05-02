package com.newvisiondz.sayaradz.views.fragments

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.newvisiondz.sayaradz.R
import com.newvisiondz.sayaradz.Utils.PrefrencesHandler
import com.newvisiondz.sayaradz.model.Model
import com.newvisiondz.sayaradz.services.RetrofitClient
import kotlinx.android.synthetic.main.fragment_model_view.view.*
import retrofit2.Call
import retrofit2.Response


class ModelView : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private var userInfo: SharedPreferences? = null
    private var prefsHandler = PrefrencesHandler()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_model_view, container, false)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userInfo = context!!.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
        var recievedModel: Model
        val modelId = arguments!!.getString("modelId")
        val manufacturer = arguments!!.getString("manufacturerId")
        val call = RetrofitClient(context!!).serverDataApi.getModelDetails(
            prefsHandler.getUserToken(userInfo!!)!!, manufacturer!!, modelId!!
        )
        call.enqueue(object : retrofit2.Callback<Model> {
            override fun onFailure(call: Call<Model>, t: Throwable) {
                Log.i("Exception", "${t.localizedMessage} Probably server Error")
            }

            override fun onResponse(call: Call<Model>, response: Response<Model>) {
                recievedModel = response.body()!!
            }
        })
        view.datasheet_button.setOnClickListener {

            val args = Bundle()
            args.putString("modelId", modelId)
            it.findNavController().navigate(R.id.action_modelView_to_dataSheetView, args)
        }

        view.order_button.setOnClickListener {
            val action = ModelViewDirections.actionModelViewToOrderForm()
            //action.setModelId(modelIdRcv)
            NavHostFragment.findNavController(this).navigate(action)
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

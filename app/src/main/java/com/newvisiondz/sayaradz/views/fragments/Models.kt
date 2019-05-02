package com.newvisiondz.sayaradz.views.fragments


import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.newvisiondz.sayaradz.R
import com.newvisiondz.sayaradz.Utils.JsonFormatter
import com.newvisiondz.sayaradz.Utils.PrefrencesHandler
import com.newvisiondz.sayaradz.adapters.ModelsAdapter
import com.newvisiondz.sayaradz.model.Model
import com.newvisiondz.sayaradz.services.RetrofitClient
import kotlinx.android.synthetic.main.fragment_models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Models : Fragment() {
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var models: MutableList<Model>
    private lateinit var modelsAdapter: ModelsAdapter
    var jsonFormatter = JsonFormatter()
    private var userInfo: SharedPreferences? = null
    private var prefsHandler = PrefrencesHandler()
    private var brandName = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userInfo = context!!.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
        return inflater.inflate(R.layout.fragment_models, container, false)
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getContent()
        swipeRefreshModels.setOnRefreshListener {
            Toast.makeText(context, "Nice", Toast.LENGTH_LONG).show()
        }
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

    private fun getContent() {
        brandName = arguments!!.getString("brandName")!!
        progressModel.visibility = View.VISIBLE
        val call = RetrofitClient(context!!)
            .serverDataApi
            .getAllModels(prefsHandler.getUserToken(userInfo!!)!!, brandName)

        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    val listType = object : TypeToken<MutableList<Model>>() {}.type
                    models = jsonFormatter.listFormatter(response.body()!!, listType, "models")
                    initRecyclerView()
                    progressModel.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun initRecyclerView() {
        modelsAdapter = ModelsAdapter(this.models, this.context as Context,brandName)
        models_list.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }
        models_list.adapter = modelsAdapter
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

}

package com.newvisiondz.sayaradz.views.fragments

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.newvisiondz.sayaradz.R
import com.newvisiondz.sayaradz.model.Model
import com.newvisiondz.sayaradz.services.RetrofitClient
import com.newvisiondz.sayaradz.utils.getUserToken
import kotlinx.android.synthetic.main.fragment_versions.*
import kotlinx.android.synthetic.main.fragment_versions.view.*
import retrofit2.Call
import retrofit2.Response


class Versions : Fragment() {
    private var userInfo: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_versions, container, false)
        userInfo = context!!.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
        var recievedModel: Model
        val modelId = VersionsArgs.fromBundle(arguments).modelId
        val manufacturer = VersionsArgs.fromBundle(arguments).manufacturerId
        val call = RetrofitClient(context!!).serverDataApi.getModelDetails(
            getUserToken(userInfo!!)!!, manufacturer, modelId
        )
        call.enqueue(object : retrofit2.Callback<Model> {
            override fun onFailure(call: Call<Model>, t: Throwable) {
                Log.i("Exception", "${t.localizedMessage} Probably server Error")
            }

            override fun onResponse(call: Call<Model>, response: Response<Model>) {
                recievedModel = response.body()!!
                Glide.with(context!!)
                    .load("http://sayaradz-sayaradz-2.7e14.starter-us-west-2.openshiftapps.com${recievedModel.images[0]}")
                    .fitCenter()
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.loading_animation)
                            .error(R.drawable.ic_broken_image)
                    )
                    .into(view.user_image)
                for (color in recievedModel.colors) {
                    when (color.name) {
                        "red" -> {
                            redBox.isChecked = true;redBox.setBackgroundColor(Color.parseColor(color.value))
                        }
                        "blue" -> {
                            blueBox.isChecked = true;blueBox.setBackgroundColor(Color.parseColor(color.value))
                        }
                        "gray" -> {
                            grayBox.isChecked = true;grayBox.setBackgroundColor(Color.parseColor(color.value))
                        }
                        "black" -> {
                            blackBox.isChecked = true;blackBox.setBackgroundColor(Color.parseColor(color.value))
                        }
                        "white" -> {
                            whiteBox.isChecked = true;whiteBox.setBackgroundColor(Color.parseColor(color.value))
                        }
                    }
                }
                for (option in recievedModel.options) {
                    when (option.name) {
                        "places" -> {
                            for (value in option.values) {
                                when (value.value) {
                                    "4" -> {
                                        place4.isChecked = true
                                    }
                                    "6" -> {
                                        place6.isChecked = true
                                    }
                                    "8" -> {
                                        place8.isChecked = true
                                    }
                                }
                            }
                        }
                        "moteur" -> {
                            for (value in option.values) {
                                when (value.value) {
                                    "diesel" -> {
                                        diesel.isChecked = true
                                    }
                                    //TODO other engins brands & power type & the engine power
                                }
                            }
                        }
                    }
                }
            }
        })
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.datasheet_button.setOnClickListener {
            val args = Bundle()
//            args.putString("modelId", modelId)
            it.findNavController().navigate(R.id.action_modelView_to_dataSheetView, args)
        }

        view.order_button.setOnClickListener {
            val action = VersionsDirections.actionModelViewToOrderForm()
            NavHostFragment.findNavController(this).navigate(R.id.action_modelView_to_orderForm)
        }
    }

}

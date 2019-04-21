package com.newvisiondz.sayaradz.views.fragments

import android.app.DatePickerDialog
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
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.newvisiondz.sayaradz.R
import com.newvisiondz.sayaradz.Utils.PrefrencesHandler
import com.newvisiondz.sayaradz.model.User
import com.newvisiondz.sayaradz.services.RetrofitClient
import kotlinx.android.synthetic.main.fragment_profile_form.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.util.*


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ProfileForm : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private var prefrencesHandler = PrefrencesHandler()
    private var userInfo: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        userInfo = context!!.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile_form, container, false)
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

    override fun onResume() {
        super.onResume()
        val userInfoTmp = prefrencesHandler.getUserInfo(userInfo!!)
        user_date.setOnClickListener { datePicker() }
        user_last_name.setText(userInfoTmp[0])
        user_first_name.setText(userInfoTmp[3])
        user_mail.text = userInfoTmp[2]

        button_confirm.setOnClickListener {
            progressForm.visibility = View.VISIBLE
            val paramObject = JSONObject()
            paramObject.put("firstName", user_first_name.text.toString())
            paramObject.put("lastName", user_last_name.text.toString())
            paramObject.put("address", user_adr.text.toString())
            paramObject.put("phone", user_tel.text.toString())

            val call = RetrofitClient(context!!)
                .serverDataApi
                .updateUser(prefrencesHandler.getUserToken(userInfo!!)!!, paramObject.toString())

            call.enqueue(object : retrofit2.Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful) {
                        progressForm.visibility = View.GONE
                        val res: String = response.body()!!.get("ok").asString
                        if (res == "1") it.findNavController().navigate(R.id.action_profileForm_to_tabs)
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileForm().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun datePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)


        val dpd = DatePickerDialog(
            activity as Context,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                this.user_date.text = "$year-$monthOfYear-$dayOfMonth"
            },
            year,
            month,
            day
        )

        dpd.show()
    }
}

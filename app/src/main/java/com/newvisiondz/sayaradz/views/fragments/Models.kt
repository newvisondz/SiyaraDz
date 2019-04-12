package com.newvisiondz.sayaradz.views.fragments


import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.newvisiondz.sayaradz.R
import com.newvisiondz.sayaradz.Utils.JsonFormatter
import com.newvisiondz.sayaradz.adapters.ModelsAdapter
import com.newvisiondz.sayaradz.model.Color
import com.newvisiondz.sayaradz.model.Model
import com.newvisiondz.sayaradz.model.Option
import com.newvisiondz.sayaradz.model.Value
import com.newvisiondz.sayaradz.services.RetrofitClient
import kotlinx.android.synthetic.main.fragment_models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class Models : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    private lateinit var models:MutableList<Model>
    private lateinit var modelsAdapter: ModelsAdapter
    var jsonFormatter = JsonFormatter()

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
//                 models = mutableListOf(
//            Model("2", "Mercedes-AMG GT", Color("3", "red", "weq"), "7.500.000,00", R.drawable.mercedes),
//            Model("2", "Volkswagen Golf", Color("3", "yellow", "weq"), "5.000.000,00", R.drawable.volkswagen),
//            Model("2", "Škoda Octavia", Color("3", "green", "weq"), "4.500.000,00", R.drawable.mercedes),
//            Model("2", "Škoda Octavia", Color("3", "blue", "weq"), "4.700.000,00", R.drawable.volkswagen)
//        )

        return inflater.inflate(R.layout.fragment_models, container, false)
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getContent()
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
        val call = RetrofitClient()
            .serverDataApi
            .getAllModels(
                "bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVjYWU2ZjEzZmQ1ZTZlMDAxODVkODY2YiIsInR5cGUiOiJBVVRPTU9CSUxJU1RFIiwiaWF0IjoxNTU0OTM1NTczLCJleHAiOjE1NTU1NDAzNzN9.p0WrgJDm4TafU5qZ6ddow9zwcDUQSSxodM-iUiUc4zA"
                , arguments!!.getString("brandName")!!
            )

        call.enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    val listType = object : TypeToken<MutableList<Model>>() {}.type
                    var jsontest = response.body()!!.asJsonObject
                    var test = jsontest.getAsJsonArray("models")
                    Log.i("json",test.toString())
                    var json ="{\"models\":[{\"id\":\"5cae7f470092b1001a797e86\",\"name\":\"model1\",\"colors\":[{\"id\":\"5cae7f470092b1001a797e8c\",\"name\":\"red\",\"value\":\"#124568\",\"createdAt\":\"2019-04-10T23:41:59.431Z\",\"updatedAt\":\"2019-04-10T23:41:59.431Z\"}],\"options\":[{\"name\":\"places\",\"values\":[{\"value\":\"6\",\"id\":\"5cae7f470092b1001a797e8b\"},{\"value\":\"4\",\"id\":\"5cae7f470092b1001a797e8a\"},{\"value\":\"8\",\"id\":\"5cae7f470092b1001a797e89\"}]},{\"name\":\"moteur\",\"values\":[{\"value\":\"diesel\",\"id\":\"5cae7f470092b1001a797e88\"},{\"value\":\"d\",\"id\":\"5cae7f470092b1001a797e87\"}]}]},{\"id\":\"5cae7fa40092b1001a797e8d\",\"name\":\"model246\",\"colors\":[{\"id\":\"5cae7fa40092b1001a797e93\",\"name\":\"red\",\"value\":\"#124568\",\"createdAt\":\"2019-04-10T23:43:32.923Z\",\"updatedAt\":\"2019-04-10T23:43:32.923Z\"}],\"options\":[{\"name\":\"places\",\"values\":[{\"value\":\"6\",\"id\":\"5cae7fa40092b1001a797e92\"},{\"value\":\"4\",\"id\":\"5cae7fa40092b1001a797e91\"},{\"value\":\"8\",\"id\":\"5cae7fa40092b1001a797e90\"}]},{\"name\":\"moteur\",\"values\":[{\"value\":\"diesel\",\"id\":\"5cae7fa40092b1001a797e8f\"},{\"value\":\"d\",\"id\":\"5cae7fa40092b1001a797e8e\"}]}]}],\"count\":4}"
//                    models = jsonFormatter.modelsFormatter(response.body()!!, listType, "models")
                    try {
                        models = Gson().fromJson(test,listType)
                    }
                   catch (e:Exception){
                       Log.i("json",e.message)
                   }
                    initRecyclerView()
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun initRecyclerView() {
        modelsAdapter = ModelsAdapter(this.models, this.context as Context)
        models_list.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }
        models_list.adapter = modelsAdapter
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

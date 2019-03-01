package com.example.siyaradz.views

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.example.siyaradz.R
import com.example.siyaradz.adapters.MarquesAdapter
import com.example.siyaradz.model.FabricantsList
import com.example.siyaradz.services.RetrofitClient
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.activity_brand_display.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class BrandDisplay : AppCompatActivity() {
    private lateinit var brands: FabricantsList
    private var adapter: MarquesAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_brand_display)
        getContent()
    }

    private fun getContent() {

        val call = RetrofitClient()
            .serverDataApi
            .getAllBrands(" id", "id marque", "1")

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    //brands = response.body()!!
                    //var json: JSONObject = response.body() as JSONObjectn
                    var json=response.body()!!.asJsonObject
                    Log.i("response",json.getAsJsonArray("fabricants").toString())
                    //initRecycerView()
                }
            }
            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun initRecycerView() {
        val layoutManger = LinearLayoutManager(this)
        brand_recycler_view.layoutManager = (layoutManger)
        brand_recycler_view.setHasFixedSize(true)
        this.adapter = MarquesAdapter(this.brands.brands!!, this)
        brand_recycler_view.adapter = this.adapter
    }
}

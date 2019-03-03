package com.example.siyaradz.views

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.example.siyaradz.R
import com.example.siyaradz.Utils.JSONFormatter
import com.example.siyaradz.adapters.MarquesAdapter
import com.example.siyaradz.model.Marque
import com.example.siyaradz.services.RetrofitClient
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_brand_display.*
import retrofit2.Call
import retrofit2.Response


class BrandDisplay : AppCompatActivity() {
    private lateinit var brands: MutableList<Marque>
    private var adapter: MarquesAdapter? = null
    private val layoutManger = LinearLayoutManager(this)
    private val jsonFormatter = JSONFormatter()
    private var pageNumber: Int = 1


    private var isloading: Boolean = true
    private var pastVisibleItems: Int = 0
    private var visibleItemsCount: Int = 0
    private var totalItemsCount: Int = 0
    private var previousTotal: Int = 0

    private var viewThreshold = 6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_brand_display)
        getContent()

        this.brand_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                this@BrandDisplay.visibleItemsCount = layoutManger.childCount
                this@BrandDisplay.totalItemsCount = layoutManger.itemCount
                this@BrandDisplay.pastVisibleItems = layoutManger.findFirstVisibleItemPosition()

                if (dy > 0) {
                    if (isloading) {
                        if (totalItemsCount > previousTotal) {
                            isloading = false
                            previousTotal = totalItemsCount
                        }
                    }
                    if (!isloading && (totalItemsCount - visibleItemsCount) <= (pastVisibleItems + viewThreshold)) {
                        pageNumber++
                        performPagination()
                        isloading = true
                    }
                }

            }
        })

    }

    private fun getContent() {
        val call = RetrofitClient()
            .serverDataApi
            .getAllBrands(" id", "id marque", pageNumber.toString(), viewThreshold.toString())

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    val listType = object : TypeToken<MutableList<Marque>>() {}.type
                    Log.i("response", response.body().toString())
                    brands = jsonFormatter.jsonFormatter(response.body()!!, listType, "fabricants")
                    initRecycerView()
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun initRecycerView() {

        brand_recycler_view.layoutManager = (layoutManger)
        brand_recycler_view.setHasFixedSize(true)
        this.adapter = MarquesAdapter(this.brands, this)
        brand_recycler_view.adapter = this.adapter
    }

    private fun performPagination() {
        val call = RetrofitClient()
            .serverDataApi
            .getAllBrands(" id", "id marque", pageNumber.toString(), viewThreshold.toString())

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    val listType = object : TypeToken<List<Marque>>() {}.type
                    adapter!!.addBrand(jsonFormatter.jsonFormatter(response.body()!!, listType, "fabricants"))
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }
}

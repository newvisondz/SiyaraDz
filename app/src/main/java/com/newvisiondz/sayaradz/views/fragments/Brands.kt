package com.newvisiondz.sayaradz.views.fragments

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import com.newvisiondz.sayaradz.R
import com.newvisiondz.sayaradz.Utils.JSONFormatter
import com.newvisiondz.sayaradz.Utils.PrefrencesHandler
import com.newvisiondz.sayaradz.adapters.BrandsAdapter
import com.newvisiondz.sayaradz.model.Brand
import com.newvisiondz.sayaradz.services.RetrofitClient
import kotlinx.android.synthetic.main.fragment_brands.*
import kotlinx.android.synthetic.main.fragment_neuf.*
import retrofit2.Call
import retrofit2.Response


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class Brands : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    private var prefs = PrefrencesHandler()
    private var userInfo: SharedPreferences? = null

    private lateinit var brands: MutableList<Brand>
    private var adapter: BrandsAdapter? = null
    // private val layoutManger = LinearLayoutManager(context)
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
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_brands, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        getContent()
    }

    override fun onResume() {
        super.onResume()
        this.brands_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                this@Brands.visibleItemsCount = brands_list.layoutManager!!.childCount
                this@Brands.totalItemsCount = brands_list.layoutManager!!.itemCount
                this@Brands.pastVisibleItems =
                    (brands_list.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

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
            .getAllBrands(
                "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVjOTBkZGFkOWZjYTkxMjY3ZTc0NDY4NyIsInR5cGUiOiJBRE1JTiIsImlhdCI6MTU1Mjk5ODk4OSwiZXhwIjoxNTUzNjAzNzg5fQ.TgbhPdVzftgqVejbftalfAoivFF59bmKTirogRFE4vE"
                , (pageNumber).toString(), (viewThreshold).toString()
            )

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    val listType = object : TypeToken<MutableList<Brand>>() {}.type
                    brands = jsonFormatter.jsonFormatter(response.body()!!, listType, "manufacturers")
                    initRecycerView()
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    fun initRecycerView() {
        brands_list.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = BrandsAdapter(brands, this.context as Context)
        }

    }

    private fun performPagination() {
        //  prgsBar.visibility = View.VISIBLE
        val call = RetrofitClient()
            .serverDataApi
            .getAllBrands(
                "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVjOTBkZGFkOWZjYTkxMjY3ZTc0NDY4NyIsInR5cGUiOiJBRE1JTiIsImlhdCI6MTU1Mjk5ODk4OSwiZXhwIjoxNTUzNjAzNzg5fQ.TgbhPdVzftgqVejbftalfAoivFF59bmKTirogRFE4vE"
                , (pageNumber).toString(), (viewThreshold).toString()
            )

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    val listType = object : TypeToken<List<Brand>>() {}.type
                    lateinit var tmp: MutableList<Brand>
                    tmp = jsonFormatter.jsonFormatter(response.body()!!, listType, "fabricants")
                   if (tmp.size != 0) {
                       brands.addAll(tmp)
                       adapter!!.addBrand(tmp)
                   }
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                t.printStackTrace()
            }
        })
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
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Brands().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

package com.newvisiondz.sayaradz.views.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import com.newvisiondz.sayaradz.R
import com.newvisiondz.sayaradz.Utils.JsonFormatter
import com.newvisiondz.sayaradz.Utils.PrefrencesHandler
import com.newvisiondz.sayaradz.adapters.BrandsAdapter
import com.newvisiondz.sayaradz.model.Brand
import com.newvisiondz.sayaradz.services.RetrofitClient
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_neuf.*
import retrofit2.Call
import retrofit2.Response


class Neuf : Fragment() {

    private var prefs = PrefrencesHandler()
    private var userInfo: SharedPreferences? = null

    private lateinit var brands: MutableList<Brand>
    private var adapter: BrandsAdapter? = null
    private val layoutManger = LinearLayoutManager(context)
    private val jsonFormatter = JsonFormatter()
    private var pageNumber: Int = 1


    private var isloading: Boolean = true
    private var pastVisibleItems: Int = 0
    private var visibleItemsCount: Int = 0
    private var totalItemsCount: Int = 0
    private var previousTotal: Int = 0

    private var viewThreshold = 6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_neuf, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        userInfo = context!!.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
        getContent()
        this.brands_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                this@Neuf.visibleItemsCount = layoutManger.childCount
                this@Neuf.totalItemsCount = layoutManger.itemCount
                this@Neuf.pastVisibleItems = layoutManger.findFirstVisibleItemPosition()

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
                prefs.getUserToken(userInfo!!)!!, " id",
                "id name", pageNumber.toString(), viewThreshold.toString()
            )

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    val listType = object : TypeToken<MutableList<Brand>>() {}.type
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
        brands_recycler_view.layoutManager = (layoutManger)
        brands_recycler_view.setHasFixedSize(true)
        this.adapter = BrandsAdapter(this.brands, context!!)
        brands_recycler_view.adapter = this.adapter
    }

    private fun performPagination() {
        prgsBar.visibility = View.VISIBLE
        val call = RetrofitClient()
            .serverDataApi
            .getAllBrands(
                prefs.getUserToken(userInfo!!)!!,
                " id",
                "id name",
                pageNumber.toString(),
                viewThreshold.toString()
            )

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    val listType = object : TypeToken<List<Brand>>() {}.type
                    lateinit var tmp: MutableList<Brand>
                    tmp = jsonFormatter.jsonFormatter(response.body()!!, listType, "fabricants")
                    brands.addAll(tmp)
                    adapter!!.addBrand(tmp)
                    prgsBar.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            Neuf().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        val searchItem = menu!!.findItem(R.id.marque_filter)
//        val searchView = searchItem.actionView as SearchView
//
//        searchView.imeOptions = EditorInfo.IME_ACTION_DONE
//
//        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String): Boolean {
//                return false
//            }
//
//            override fun onQueryTextChange(newText: String): Boolean {
//                adapter!!.filter.filter(newText)
//                return false
//            }
//        })
    }


}

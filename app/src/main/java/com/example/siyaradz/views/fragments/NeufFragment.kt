package com.example.siyaradz.views.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.*
import android.view.inputmethod.EditorInfo
import com.example.siyaradz.R
import com.example.siyaradz.Utils.JSONFormatter
import com.example.siyaradz.Utils.PrefrencesHandler
import com.example.siyaradz.adapters.MarquesAdapter
import com.example.siyaradz.model.Marque
import com.example.siyaradz.services.RetrofitClient
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_neuf.*
import retrofit2.Call
import retrofit2.Response


class NeufFragment : Fragment() {

    private var prefs = PrefrencesHandler()
    private var userInfo: SharedPreferences? = null

    private lateinit var brands: MutableList<Marque>
    private var adapter: MarquesAdapter? = null
    private val layoutManger = LinearLayoutManager(context)
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
                this@NeufFragment.visibleItemsCount = layoutManger.childCount
                this@NeufFragment.totalItemsCount = layoutManger.itemCount
                this@NeufFragment.pastVisibleItems = layoutManger.findFirstVisibleItemPosition()

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
                "id marque", pageNumber.toString(), viewThreshold.toString()
            )

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {

                    val listType = object : TypeToken<MutableList<Marque>>() {}.type
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
        this.adapter = MarquesAdapter(this.brands, context!!)
        brands_recycler_view.adapter = this.adapter
    }

    private fun performPagination() {
        prgsBar.visibility = View.VISIBLE
        val call = RetrofitClient()
            .serverDataApi
            .getAllBrands(
                prefs.getUserToken(userInfo!!)!!,
                " id",
                "id marque",
                pageNumber.toString(),
                viewThreshold.toString()
            )

        call.enqueue(object : retrofit2.Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                if (response.isSuccessful) {
                    val listType = object : TypeToken<List<Marque>>() {}.type
                    lateinit var tmp: MutableList<Marque>
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
            NeufFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        val searchItem = menu!!.findItem(R.id.marque_filter)
        val searchView = searchItem.actionView as SearchView

        searchView.imeOptions = EditorInfo.IME_ACTION_DONE

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                adapter!!.filter.filter(newText)
                return false
            }
        })
    }


}

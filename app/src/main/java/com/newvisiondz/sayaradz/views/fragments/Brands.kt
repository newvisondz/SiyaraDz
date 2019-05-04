package com.newvisiondz.sayaradz.views.fragments

import android.app.Application
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.newvisiondz.sayaradz.R
import com.newvisiondz.sayaradz.adapters.BrandsAdapter
import com.newvisiondz.sayaradz.model.Brand
import com.newvisiondz.sayaradz.views.viewModel.BrandsViewModel
import com.newvisiondz.sayaradz.views.viewModel.BrandsViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_brands.*
import kotlinx.android.synthetic.main.fragment_brands.view.*


class Brands : Fragment() {

    private var mViewModel: BrandsViewModel? = null
    private var brands = mutableListOf<Brand>()
    private var adapter: BrandsAdapter? = null
    private var pageNumber: Int = 1


    private var isloading: Boolean = true
    private var pastVisibleItems: Int = 0
    private var visibleItemsCount: Int = 0
    private var totalItemsCount: Int = 0
    private var previousTotal: Int = 0

    private var viewThreshold = 6


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_brands, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewModel()
        initRecycerView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.swipeRefresh.setOnRefreshListener {
            brands.clear()
            adapter!!.clearBrands()
            mViewModel!!.getBrandsData()
            brands_list.adapter!!.notifyDataSetChanged()
            pageNumber = 1
            isloading = false
            swipeRefresh.isRefreshing = false
        }
    }

    override fun onPause() {
        super.onPause()
        pageNumber = 1
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
                        mViewModel!!.performPagination(pageNumber, viewThreshold)
                        isloading = true
                    }
                }
            }
        }
        )
        activity!!.action_search
            .setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    mViewModel!!.filterBrands(query)
                    brands_list.adapter!!.notifyDataSetChanged()
                    return true
                }

                override fun onQueryTextChange(query: String): Boolean {
                    return false
                }
            })
        activity!!.action_search
            .setOnCloseListener {
                false
            }
    }

    private fun initRecycerView() {
        brands_list.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }
        adapter = BrandsAdapter(brands, this.context as Context)
        brands_list.adapter = adapter
    }

    private fun initViewModel() {
        val brandsObserver =
            Observer<MutableList<Brand>> { newBrands ->
                brands.clear()
                brands.addAll(newBrands!!)
                if (adapter == null) {
                    adapter = BrandsAdapter(
                        brands,
                        context!!
                    )
                    brands_list.adapter = adapter
                } else {
                    brands_list.adapter!!.notifyDataSetChanged()
                }
            }

        mViewModel = ViewModelProviders.of(
            this,
            BrandsViewModelFactory(
                context!!.applicationContext as Application
            )
        )
            .get(BrandsViewModel::class.java)
        mViewModel!!.brandsList.observe(this, brandsObserver)
    }

}
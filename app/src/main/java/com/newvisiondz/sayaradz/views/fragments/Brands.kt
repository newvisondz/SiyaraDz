package com.newvisiondz.sayaradz.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.newvisiondz.sayaradz.R
import com.newvisiondz.sayaradz.adapters.BrandsAdapter
import com.newvisiondz.sayaradz.databinding.FragmentBrandsBinding
import com.newvisiondz.sayaradz.model.Brand
import com.newvisiondz.sayaradz.views.viewModel.BrandsViewModel
import com.newvisiondz.sayaradz.views.viewModel.BrandsViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*


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
        val binding: FragmentBrandsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_brands, container, false)

        binding.brandsList.adapter = BrandsAdapter(brands, context!!)
        val application = requireNotNull(this.activity).application
        mViewModel = ViewModelProviders.of(
            this,
            BrandsViewModelFactory(
                application
            )
        ).get(BrandsViewModel::class.java)
        mViewModel!!.brandsList.observe(this, Observer { newBrands ->
            brands.clear()
            brands.addAll(newBrands!!)
            if (adapter == null) {
                adapter = BrandsAdapter(
                    brands,
                    context!!
                )
                binding.brandsList.adapter = adapter
            } else {
                binding.brandsList.adapter?.notifyDataSetChanged()
            }
        })
        binding.swipeRefresh.setOnRefreshListener {
            brands.clear()
            adapter!!.clearBrands()
            mViewModel!!.getBrandsData()
            binding.brandsList.adapter!!.notifyDataSetChanged()
            pageNumber = 1
            isloading = false
            binding.swipeRefresh.isRefreshing = false
        }
        activity!!.action_search
            .setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    mViewModel!!.filterBrands(query)
                    return true
                }

                override fun onQueryTextChange(query: String): Boolean {
                    return false
                }
            })
        binding.brandsList.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                this@Brands.visibleItemsCount = binding.brandsList.layoutManager!!.childCount
                this@Brands.totalItemsCount = binding.brandsList.layoutManager!!.itemCount
                this@Brands.pastVisibleItems =
                    (binding.brandsList.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
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
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        pageNumber = 1
    }

//    override fun onResume() {
//        super.onResume()
//        this.brands_list.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                this@Brands.visibleItemsCount = brands_list.layoutManager!!.childCount
//                this@Brands.totalItemsCount = brands_list.layoutManager!!.itemCount
//                this@Brands.pastVisibleItems =
//                    (brands_list.layoutManager as androidx.recyclerview.widget.LinearLayoutManager).findFirstVisibleItemPosition()
//                if (dy > 0) {
//                    if (isloading) {
//                        if (totalItemsCount > previousTotal) {
//                            isloading = false
//                            previousTotal = totalItemsCount
//                        }
//                    }
//                    if (!isloading && (totalItemsCount - visibleItemsCount) <= (pastVisibleItems + viewThreshold)) {
//                        pageNumber++
//                        mViewModel!!.performPagination(pageNumber, viewThreshold)
//                        isloading = true
//                    }
//                }
//            }
//        }
//        )
//
//    }

}
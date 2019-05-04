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
import com.newvisiondz.sayaradz.adapters.ModelsAdapter
import com.newvisiondz.sayaradz.model.Model
import com.newvisiondz.sayaradz.views.viewModel.ModelsViewModel
import com.newvisiondz.sayaradz.views.viewModel.ModelsViewModelsFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_models.*


class Models : Fragment() {
    private var models = mutableListOf<Model>()
    private lateinit var modelsAdapter: ModelsAdapter
    private var brandName = ""

    private var pageNumber: Int = 1

    private var isloading: Boolean = true
    private var pastVisibleItems: Int = 0
    private var visibleItemsCount: Int = 0
    private var totalItemsCount: Int = 0
    private var previousTotal: Int = 0

    private var viewThreshold = 6

    var mViewModel: ModelsViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_models, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        brandName = arguments!!.getString("brandName")!!
        initViewModel()
        initRecyclerView()
        this.models_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                this@Models.visibleItemsCount = models_list.layoutManager!!.childCount
                this@Models.totalItemsCount = models_list.layoutManager!!.itemCount
                this@Models.pastVisibleItems =
                    (models_list.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
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
        })
        swipeRefreshModels.setOnRefreshListener {
            models.clear()
            mViewModel!!.getModelData()
            models_list.adapter!!.notifyDataSetChanged()
            pageNumber = 1
            isloading = false
            swipeRefreshModels.isRefreshing = false
        }
        activity!!.action_search
            .setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    mViewModel!!.filterModelsData(query)
                    return true
                }

                override fun onQueryTextChange(query: String): Boolean {
                    return false
                }
            })
    }


    private fun initRecyclerView() {
        modelsAdapter = ModelsAdapter(this.models, this.context as Context, brandName)
        models_list.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
        }
        models_list.adapter = modelsAdapter
    }


    private fun initViewModel() {
        val brandsObserver =
            Observer<MutableList<Model>> { newBrands ->
                models.clear()
                models.addAll(newBrands!!)
                if (modelsAdapter == null) {
                    modelsAdapter = ModelsAdapter(
                        models,
                        context!!, arguments!!.getString("brandName")!!
                    )
                    models_list.adapter = modelsAdapter
                } else {
                    models_list.adapter!!.notifyDataSetChanged()
                }
            }

        mViewModel = ViewModelProviders.of(
            this,
            ModelsViewModelsFactory(
                context!!.applicationContext as Application, brandName
            )
        )
            .get(ModelsViewModel::class.java)
        mViewModel!!.modelsList.observe(this, brandsObserver)
    }

}

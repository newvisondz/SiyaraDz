package com.newvisiondz.sayara.views.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.adapters.ModelsAdapter
import com.newvisiondz.sayara.databinding.FragmentModelsBinding
import com.newvisiondz.sayara.model.Model
import com.newvisiondz.sayara.views.viewModel.ModelsViewModel
import com.newvisiondz.sayara.views.viewModel.ModelsViewModelsFactory
import kotlinx.android.synthetic.main.activity_main.*

class Models : Fragment() {
    private var models = mutableListOf<Model>()
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
        val binding: FragmentModelsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_models, container, false)
        val application = requireNotNull(this.activity).application
        brandName = ModelsArgs.fromBundle(arguments!!).brandName
        mViewModel = ViewModelProviders.of(
            this, ModelsViewModelsFactory(
                application, this
            )
        ).get(ModelsViewModel::class.java)
        binding.modelsList.adapter = ModelsAdapter(models, context!!, brandName)

        mViewModel!!.getModelData(brandName)
        mViewModel!!.modelsList.observe(this, Observer { newModels ->
            models.clear()
            models.addAll(newModels)
            binding.modelsList.adapter!!.notifyDataSetChanged()
            binding.progressModels.visibility=View.GONE
        })

        binding.modelsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                this@Models.visibleItemsCount = binding.modelsList.layoutManager!!.childCount
                this@Models.totalItemsCount = binding.modelsList.layoutManager!!.itemCount
                this@Models.pastVisibleItems =
                    (binding.modelsList.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                if (dy > 0) {
                    if (isloading) {
                        if (totalItemsCount > previousTotal) {
                            isloading = false
                            previousTotal = totalItemsCount
                        }
                    }
                    if (!isloading && (totalItemsCount - visibleItemsCount) <= (pastVisibleItems + viewThreshold)) {
                        pageNumber++
                        mViewModel!!.performPagination(pageNumber, viewThreshold, brandName)
                        isloading = true
                    }
                }
            }
        })
        binding.swipeRefreshModels.setOnRefreshListener {
            models.clear()
            mViewModel!!.getModelData(brandName)
            binding.modelsList.adapter!!.notifyDataSetChanged()
            pageNumber = 1
            isloading = false
            binding.swipeRefreshModels.isRefreshing = false
        }
        activity!!.action_search
            .setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    mViewModel!!.filterModelsData(query, brandName)
                    return true
                }

                override fun onQueryTextChange(query: String): Boolean {
                    return false
                }
            })

        return binding.root
    }
}

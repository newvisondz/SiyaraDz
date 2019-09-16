package com.newvisiondz.sayara.screens.compareversions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.screens.versions.SpinnerAdapter
import com.newvisiondz.sayara.databinding.FragmentCompareVersionsBinding
import com.newvisiondz.sayara.model.Version
import com.newvisiondz.sayara.model.VersionCompare
import com.newvisiondz.sayara.utils.optionsMapping

class CompareVersions : Fragment() {
    private val version1 = mutableListOf<Version>()
    private var compareList = mutableListOf<VersionCompare>()
    private var compareList2 = mutableListOf<VersionCompare>()
    private lateinit var v1: Version
    private lateinit var v2: Version
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentCompareVersionsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_compare_versions, container, false)
        val application = requireNotNull(activity).application
        val manufacturer = arguments?.getString("brandName")
        val modelId = arguments?.getString("modelId")
        val viewModelFactory = CompareVersionViewModelFactory(application)
        val versionCompareViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(CompareVersionViewModel::class.java)
        binding.viewModel = versionCompareViewModel
        binding.lifecycleOwner = this
        versionCompareViewModel.getAllVersions(manufacturer!!, modelId!!)
        binding.spinnerV1.adapter =
            SpinnerAdapter(context!!, R.layout.spinner_element, version1)
        binding.spinnerV2.adapter =
            SpinnerAdapter(context!!, R.layout.spinner_element, version1)
        val adapter1 = CompareVersionAdapter(compareList)
        val adapter2 = CompareVersionAdapter(compareList2)
        binding.optionsListSpinner1.adapter = adapter1
        binding.optionsListSpinner2.adapter = adapter2
        versionCompareViewModel.versionList.observe(this, Observer { list ->
            version1.clear()
            version1.addAll(list)
            compareList = optionsMapping(version1[0])
            compareList2 = optionsMapping(version1[0])
            adapter1.items=compareList
            adapter2.items=compareList2
            (binding.optionsListSpinner1.adapter as CompareVersionAdapter).notifyDataSetChanged()
            (binding.optionsListSpinner2.adapter as CompareVersionAdapter).notifyDataSetChanged()

            (binding.spinnerV1.adapter as SpinnerAdapter).notifyDataSetChanged()
            (binding.spinnerV2.adapter as SpinnerAdapter).notifyDataSetChanged()
        })
        versionCompareViewModel.versionDetails2.observe(this, Observer { v1Res ->
            v1 = v1Res
            compareList.clear()
            compareList.addAll(optionsMapping(v1Res))
            adapter1.items=compareList
            (binding.optionsListSpinner1.adapter as CompareVersionAdapter).notifyDataSetChanged()
        })
        versionCompareViewModel.versionDetails1.observe(this, Observer { v2Res ->
            v2 = v2Res
            compareList2.clear()
            compareList2.addAll(optionsMapping(v2Res))
            adapter2.items=compareList2
            (binding.optionsListSpinner2.adapter as CompareVersionAdapter).notifyDataSetChanged()

        })

        binding.spinnerV2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                versionCompareViewModel.getVersionDetails(
                    manufacturer,
                    modelId,
                    version1[position].id,
                    0
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        binding.spinnerV1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                versionCompareViewModel.getVersionDetails(
                    manufacturer!!,
                    modelId!!,
                    version1[position].id,
                    1
                )
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        return binding.root
    }
}

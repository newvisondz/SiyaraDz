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
    private val versions = mutableListOf<Version>()
    private var compareList = mutableListOf<VersionCompare>()
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
        val viewModelFactory = CompareVersionViewModelFactory(
            application,
            manufacturer!!,
            modelId!!
        )
        val versionCompareViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(CompareVersionViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = versionCompareViewModel
        binding.spinnerV1.adapter =
            SpinnerAdapter(context!!, R.layout.spinner_element, versions)
        binding.spinnerV2.adapter =
            SpinnerAdapter(context!!, R.layout.spinner_element, versions)
        val adapter = CompareVersionAdapter()
        binding.optionsList.adapter = adapter
        versionCompareViewModel.doneGettingResult.observe(this, Observer {
            if (it == true) {
                compareList = optionsMapping(v1, v2)
                adapter.submitList(compareList)
            }
        })

        versionCompareViewModel.versionList.observe(this, Observer { list ->
            versions.clear()
            versions.addAll(list)
            v1 = versions[0]
            v2 = versions[0]
            (binding.spinnerV1.adapter as SpinnerAdapter).notifyDataSetChanged()
            (binding.spinnerV2.adapter as SpinnerAdapter).notifyDataSetChanged()
        })
        versionCompareViewModel.versionDetails1.observe(this, Observer { v1Res ->
            v1 = v1Res
        })
        versionCompareViewModel.versionDetails2.observe(this, Observer { v2Res ->
            v2 = v2Res
        })

        binding.spinnerV2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                versionCompareViewModel.getVersionDetails(manufacturer, modelId, versions[position].id, 1)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        binding.spinnerV1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                versionCompareViewModel.getVersionDetails(manufacturer, modelId, versions[position].id, 2)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        return binding.root
    }
}

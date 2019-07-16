package com.newvisiondz.sayara.views.fragments

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
import com.newvisiondz.sayara.adapters.SpinnerAdapter
import com.newvisiondz.sayara.databinding.FragmentCompareVersionsBinding
import com.newvisiondz.sayara.databinding.FragmentCompareVersionsBindingImpl
import com.newvisiondz.sayara.model.Version
import com.newvisiondz.sayara.views.viewModel.CompareVersionViewModel
import com.newvisiondz.sayara.views.viewModel.CompareVersionViewModelFactory

class CompareVersions : Fragment() {
    private val versions = mutableListOf<Version>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentCompareVersionsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_compare_versions, container, false)
        val application = requireNotNull(activity).application
        val manufacturer = arguments?.getString("brandName")
        val modelId = arguments?.getString("modelId")
        val viewModelFactory = CompareVersionViewModelFactory(application, manufacturer!!, modelId!!)
        val versionCompareViewModel =
            ViewModelProviders.of(this, viewModelFactory).get(CompareVersionViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = versionCompareViewModel
        binding.brandName = manufacturer
        binding.modelId = modelId
//        binding.versionId1 = binding.spinnerV1.selectedItem.toString()
//        binding.versionId2 = binding.spinnerV2.selectedItem.toString()
        binding.spinnerV1.adapter = SpinnerAdapter(context!!, R.layout.spinner_element, versions)
        binding.spinnerV2.adapter = SpinnerAdapter(context!!, R.layout.spinner_element, versions)

        versionCompareViewModel.versionList.observe(this, Observer {
            versions.clear()
            versions.addAll(it)
            (binding.spinnerV1.adapter as SpinnerAdapter).notifyDataSetChanged()
            (binding.spinnerV2.adapter as SpinnerAdapter).notifyDataSetChanged()
        })

        binding.spinnerV2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                binding.versionId2 = versions[position].id
                binding.invalidateAll()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        binding.spinnerV1.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                binding.versionId1 = versions[position].id
                binding.invalidateAll()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        return binding.root
    }
}

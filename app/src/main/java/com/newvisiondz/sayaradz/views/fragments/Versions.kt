package com.newvisiondz.sayaradz.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.newvisiondz.sayaradz.R
import com.newvisiondz.sayaradz.adapters.SpinnerAdapter
import com.newvisiondz.sayaradz.databinding.FragmentVersionsBinding
import com.newvisiondz.sayaradz.model.Version
import com.newvisiondz.sayaradz.views.viewModel.VersionsViewModel
import com.newvisiondz.sayaradz.views.viewModel.VersionsViewModelFactory


class Versions : Fragment() {

    private val versions = mutableListOf<Version>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentVersionsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_versions, container, false)
        val application = requireNotNull(activity).application
        val viewModelFactory = VersionsViewModelFactory(application)
        val versionViewModel = ViewModelProviders.of(this, viewModelFactory).get(VersionsViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = versionViewModel
        binding.versionsSpinner.adapter = SpinnerAdapter(context!!, R.layout.spinner_element, versions)

        val modelId = VersionsArgs.fromBundle(arguments).modelId
        val manufacturer = VersionsArgs.fromBundle(arguments).manufacturerId

        versionViewModel.getAllVersions(manufacturer, modelId)

        versionViewModel.model.observe(this, Observer { newModel ->

        })
        versionViewModel.versionList.observe(this, Observer { newVersions ->
            versions.clear()
            versions.addAll(newVersions)
            (binding.versionsSpinner.adapter as SpinnerAdapter).notifyDataSetChanged()
        })
        binding.datasheetButton.setOnClickListener {
            //TODO params
            it.findNavController().navigate(VersionsDirections.actionModelViewToDataSheetView())
        }

        binding.orderButton.setOnClickListener {
            //TODO params
            //todo format String in the adapter
            it.findNavController().navigate(VersionsDirections.actionModelViewToDataSheetView())
        }
        return binding.root
    }
}

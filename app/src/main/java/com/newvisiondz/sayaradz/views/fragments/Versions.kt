package com.newvisiondz.sayaradz.views.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.newvisiondz.sayaradz.R
import com.newvisiondz.sayaradz.adapters.SliderAdapter
import com.newvisiondz.sayaradz.adapters.SpinnerAdapter
import com.newvisiondz.sayaradz.adapters.versionadapters.PlacesAdapter
import com.newvisiondz.sayaradz.databinding.FragmentVersionsTestBinding
import com.newvisiondz.sayaradz.model.Value
import com.newvisiondz.sayaradz.model.Version
import com.newvisiondz.sayaradz.views.viewModel.VersionsViewModel
import com.newvisiondz.sayaradz.views.viewModel.VersionsViewModelFactory


class Versions : Fragment(), PlacesAdapter.SingleClickListener {
    private var modelId = ""
    private var manufacturer = ""
    private var modelImages = mutableListOf<String>()
    private val versions = mutableListOf<Version>()
    private var adapter: PlacesAdapter? = null
    val tmpList = mutableListOf(
        Value("1", "nice"), Value("2", "bad"), Value("1", "cool")
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentVersionsTestBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_versions_test, container, false)
        val application = requireNotNull(activity).application
        val viewModelFactory = VersionsViewModelFactory(application)
        val versionViewModel = ViewModelProviders.of(this, viewModelFactory).get(VersionsViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = versionViewModel
        modelId = arguments?.getString("modelId")!!
        manufacturer = arguments?.getString("manufacturerId")!!
        modelImages = arguments?.getStringArrayList("modelImages")!!


        binding.versionsSpinner.adapter = SpinnerAdapter(context!!, R.layout.spinner_element, versions)
        adapter = PlacesAdapter(tmpList, context!!)
        binding.placesList.adapter = adapter
        adapter!!.setOnItemClickListener(this)

        binding.imageSlider.sliderAdapter = SliderAdapter(context!!, modelImages)
        versionViewModel.getAllVersions(manufacturer, modelId)

        versionViewModel.version.observe(this, Observer { newVersion ->
            Log.i("Nice shit", newVersion.options[0].name)
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

        binding.versionsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                versionViewModel.getVersionDetails(manufacturer, modelId, versions[position].id)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        return binding.root
    }

    override fun onItemClickListener(position: Int, view: View) {
        adapter!!.selectedItem()
        Toast.makeText(context, tmpList[position].value, Toast.LENGTH_SHORT).show()
    }
}

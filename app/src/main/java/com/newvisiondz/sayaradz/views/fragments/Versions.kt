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
import com.newvisiondz.sayaradz.adapters.versionadapters.ColorsAdapter
import com.newvisiondz.sayaradz.adapters.versionadapters.EngineAdapter
import com.newvisiondz.sayaradz.adapters.versionadapters.FuelAdapter
import com.newvisiondz.sayaradz.adapters.versionadapters.PlacesAdapter
import com.newvisiondz.sayaradz.databinding.FragmentVersionsTestBinding
import com.newvisiondz.sayaradz.model.Value
import com.newvisiondz.sayaradz.model.Version
import com.newvisiondz.sayaradz.views.viewModel.VersionsViewModel
import com.newvisiondz.sayaradz.views.viewModel.VersionsViewModelFactory


class Versions : Fragment(), PlacesAdapter.SingleClickListener, EngineAdapter.SingleClickListener,
    FuelAdapter.SingleClickListener, ColorsAdapter.SingleClickListener {


    private var modelId = ""
    private var manufacturer = ""
    private var modelImages = mutableListOf<String>()
    private val versions = mutableListOf<Version>()
    private var placesAdapter: PlacesAdapter? = null
    private var engineAdapter: EngineAdapter? = null
    private var fuelAdapter: FuelAdapter? = null
    private var colorAdapter: ColorsAdapter? = null
    val tmpPlaces = mutableListOf(
        Value("1", "nice"), Value("2", "bad"), Value("1", "cool")
    )
    val tmpEngine = mutableListOf(
        Value("1", "toyota"), Value("2", "chev"), Value("1", "Nice")
    )
    val tmpFuel = mutableListOf(
        Value("1", "Essence"), Value("2", "Diesel")
    )
    val tmpColor = mutableListOf(
        Value("1", "red"), Value("2", "blue"), Value("2", "green")
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
        placesAdapter = PlacesAdapter(tmpPlaces, context!!)
        engineAdapter = EngineAdapter(tmpEngine, context!!)
        fuelAdapter = FuelAdapter(tmpFuel, context!!)
        fuelAdapter = FuelAdapter(tmpFuel, context!!)
        colorAdapter = ColorsAdapter(tmpColor, context!!)
        binding.placesList.adapter = placesAdapter
        binding.engineBoxList.adapter = engineAdapter
        binding.engineTypeList.adapter = fuelAdapter
        binding.carsColorsList.adapter = colorAdapter
        placesAdapter!!.setOnItemClickListener(this)
        engineAdapter!!.setOnItemClickListener(this)
        fuelAdapter!!.setOnItemClickListener(this)
        colorAdapter!!.setOnItemClickListener(this)

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
            //todo format String in the placesAdapter
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

    override fun onPlacesClickListener(position: Int, view: View) {
        placesAdapter!!.selectedItem()
        Toast.makeText(context, tmpPlaces[position].value, Toast.LENGTH_SHORT).show()
    }

    override fun onEnginClickListner(position: Int, view: View) {
        engineAdapter!!.selectedItem()
        Toast.makeText(context, tmpEngine[position].value, Toast.LENGTH_SHORT).show()
    }

    override fun onFuelClickListner(position: Int, view: View) {
        fuelAdapter!!.selectedItem()
        Toast.makeText(context, tmpFuel[position].value, Toast.LENGTH_SHORT).show()
    }

    override fun onColorClickListner(position: Int, view: View) {
        colorAdapter!!.selectedItem()
        Toast.makeText(context, tmpColor[position].value, Toast.LENGTH_SHORT).show()
    }


}

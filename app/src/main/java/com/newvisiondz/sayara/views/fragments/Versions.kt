package com.newvisiondz.sayara.views.fragments

import android.app.AlertDialog
import android.os.Bundle
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
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.adapters.SliderAdapter
import com.newvisiondz.sayara.adapters.SpinnerAdapter
import com.newvisiondz.sayara.adapters.versionadapters.*
import com.newvisiondz.sayara.databinding.FragmentVersionsBinding
import com.newvisiondz.sayara.model.Color
import com.newvisiondz.sayara.model.Command
import com.newvisiondz.sayara.model.Value
import com.newvisiondz.sayara.model.Version
import com.newvisiondz.sayara.views.viewModel.VersionsViewModel
import com.newvisiondz.sayara.views.viewModel.VersionsViewModelFactory


class Versions : Fragment(), PlacesAdapter.SingleClickListener, EngineAdapter.SingleClickListener,
    FuelAdapter.SingleClickListener, ColorsAdapter.SingleClickListener, EnginePowerAdapter.SingleClickListener {


    private var modelId = ""
    private var manufacturer = ""
    private var versionId = ""
    private var modelImages = mutableListOf<String>()
    private val versions = mutableListOf<Version>()
    private lateinit var placesAdapter: PlacesAdapter
    private lateinit var engineAdapter: EngineAdapter
    private lateinit var fuelAdapter: FuelAdapter
    private lateinit var enginePowerAdapter: EnginePowerAdapter
    private lateinit var colorAdapter: ColorsAdapter
    private val tmpPlaces = mutableListOf<Value>()
    private val tmpEngine = mutableListOf<Value>()
    private val tmpFuel = mutableListOf<Value>()
    private val tmpEnginePower = mutableListOf<Value>()
    private val tmpColor = mutableListOf<Color>()
    private val userChoices = mutableMapOf<String, String>()

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
        modelId = arguments?.getString("modelId")!!
        manufacturer = arguments?.getString("manufacturerId")!!
        modelImages = arguments?.getStringArrayList("modelImages")!!


        binding.versionsSpinner.adapter = SpinnerAdapter(context!!, R.layout.spinner_element, versions)
        initializeAdapters(binding)

        binding.imageSlider.sliderAdapter = SliderAdapter(context!!, modelImages)
        versionViewModel.getAllVersions(manufacturer, modelId)

        versionViewModel.version.observe(this, Observer { newVersion ->
            tmpColor.clear()
            tmpColor.addAll(newVersion.colors)
            userChoices.remove("color")
            ColorsAdapter.sSelected = -1
            binding.carsColorsList.adapter?.notifyDataSetChanged()
            for (item in newVersion.options) {
                when (item.name) {
                    "places" -> {
                        tmpPlaces.clear()
                        PlacesAdapter.sSelected = -1
                        userChoices.remove("place")
                        tmpPlaces.addAll(item.values)
                        binding.placesList.adapter?.notifyDataSetChanged()
                    }
                    "Type du carburant" -> {
                        tmpFuel.clear()
                        tmpFuel.addAll(item.values)
                        FuelAdapter.sSelected = -1
                        userChoices.remove("fuel")
                        binding.engineTypeList.adapter?.notifyDataSetChanged()
                    }
                    "moteur" -> {
                        tmpEnginePower.clear()
                        tmpEnginePower.addAll(item.values)
                        EnginePowerAdapter.sSelected = -1
                        userChoices.remove("enginePower")
                        binding.enginePower.adapter?.notifyDataSetChanged()
                    }
                    "Boite" -> {
                        tmpEngine.clear()
                        tmpEngine.addAll(item.values)
                        EngineAdapter.sSelected = -1
                        userChoices.remove("engine")
                        binding.engineBoxList.adapter?.notifyDataSetChanged()
                    }
                }
            }
        })
        versionViewModel.versionList.observe(this, Observer { newVersions ->
            versions.clear()
            versions.addAll(newVersions)
            (binding.versionsSpinner.adapter as SpinnerAdapter).notifyDataSetChanged()
        })
        versionViewModel.commandDetails.observe(this, Observer { command ->
            if (command.cars.isNotEmpty()) {
//                displayDialog(command, versionViewModel)
//                MaterialStyledDialog.Builder(context)
//                    .setTitle("the price is ${command.price}!")
//                    .setDescription("do you want to proceed")
//                    .setHeaderDrawable(R.drawable.logo)
//                    .withDarkerOverlay(true)
//                    .withIconAnimation(true)
//                    .onPositive { dialog, which ->
//
//                    }
//                    .show()
            } else {
                Toast.makeText(context, "This car is no longer available", Toast.LENGTH_LONG).show()
            }
        })
//        binding.datasheetButton.setOnClickListener {
//            //TODO params
//            it.findNavController().navigate(VersionsDirections.actionModelViewToDataSheetView())
//        }

        binding.orderButton.setOnClickListener {
            //TODO do something about order Form
            //todo format String in the placesAdapter
            versionViewModel.sendCommand(manufacturer, modelId, versionId, userChoices.values.toMutableList())
//            it.findNavController().navigate(VersionsDirections.actionModelViewToOrderForm())
        }

        binding.versionsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                versionViewModel.getVersionDetails(manufacturer, modelId, versions[position].id)
                versionId = versions[position].id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        return binding.root
    }

    private fun displayDialog(
        command: Command,
        versionViewModel: VersionsViewModel
    ) {
        val dialogBuilder = AlertDialog.Builder(context)
            .setMessage("le prix etait ${command.price}")
            .setTitle("Confirmation d'achat")
            .setCancelable(true)
            .setPositiveButton("Proceed") { dialog, id ->
                versionViewModel.confirmCommande(command.cars[0])
                //todo observe another variable when command is successfully saved to navigate to tabs
            }
            .setNegativeButton("Cancel") { dialog, id ->
                dialog.cancel()
            }
        val alert = dialogBuilder.create()
        alert.show()
    }

    private fun initializeAdapters(binding: FragmentVersionsBinding) {
        placesAdapter = PlacesAdapter(tmpPlaces)
        engineAdapter = EngineAdapter(tmpEngine)
        enginePowerAdapter = EnginePowerAdapter(tmpEnginePower)
        fuelAdapter = FuelAdapter(tmpFuel)
        colorAdapter = ColorsAdapter(tmpColor)
        binding.placesList.adapter = placesAdapter
        binding.engineBoxList.adapter = engineAdapter
        binding.engineTypeList.adapter = fuelAdapter
        binding.carsColorsList.adapter = colorAdapter
        binding.enginePower.adapter = enginePowerAdapter
        placesAdapter.setOnItemClickListener(this)
        engineAdapter.setOnItemClickListener(this)
        fuelAdapter.setOnItemClickListener(this)
        colorAdapter.setOnItemClickListener(this)
        enginePowerAdapter.setOnItemClickListener(this)
    }

    override fun onPlacesClickListener(position: Int, view: View) {
        placesAdapter.selectedItem()
        if (userChoices["place"].isNullOrEmpty()) {
            userChoices["place"] = tmpPlaces[position].id
        } else {
            if (userChoices["place"] == tmpPlaces[position].id) {
                userChoices.remove("place")
                PlacesAdapter.sSelected = -1
            } else {
                userChoices["place"] = tmpPlaces[position].id
            }
        }
    }


    override fun onEnginClickListner(position: Int, view: View) {
        engineAdapter.selectedItem()
        if (userChoices["engine"].isNullOrEmpty()) {
            userChoices["engine"] = tmpEngine[position].id
        } else {
            if (userChoices["engine"] == tmpEngine[position].id) {
                userChoices.remove("engine")
                EngineAdapter.sSelected = -1
            } else {
                userChoices["engine"] = tmpEngine[position].id
            }
        }
    }

    override fun onFuelClickListner(position: Int, view: View) {
        fuelAdapter.selectedItem()
        if (userChoices["fuel"] == null) {
            userChoices["fuel"] = tmpFuel[position].id
        } else {
            if (userChoices["fuel"] == tmpFuel[position].id) {
                userChoices.remove("fuel")
                FuelAdapter.sSelected = -1
            } else {
                userChoices["fuel"] = tmpFuel[position].id
            }
        }
    }

    override fun onColorClickListner(position: Int, view: View) {
        colorAdapter.selectedItem()
        if (userChoices["color"] == null) {
            userChoices["color"] = tmpColor[position].id
            Toast.makeText(context, tmpColor[position].name, Toast.LENGTH_SHORT).show()

        } else {
            if (userChoices["color"] == tmpColor[position].id) {
                userChoices.remove("color")
                ColorsAdapter.sSelected = -1
            } else {
                Toast.makeText(context, tmpColor[position].name, Toast.LENGTH_SHORT).show()
                userChoices["color"] = tmpColor[position].id
            }
        }
    }

    override fun onEnginPowerClickListner(position: Int, view: View) {
        enginePowerAdapter.selectedItem()
        if (userChoices["enginePower"] == null) {
            userChoices["enginePower"] = tmpEnginePower[position].id
        } else {
            if (userChoices["enginePower"] == tmpEnginePower[position].id) {
                userChoices.remove("enginePower")
                EnginePowerAdapter.sSelected = -1
            } else {
                userChoices["enginePower"] = tmpEnginePower[position].id
            }
        }
    }
}

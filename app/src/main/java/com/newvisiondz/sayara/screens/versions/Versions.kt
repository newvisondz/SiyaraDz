package com.newvisiondz.sayara.screens.versions

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
import cn.pedant.SweetAlert.SweetAlertDialog
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.databinding.FragmentVersionsBinding
import com.newvisiondz.sayara.model.*
import com.newvisiondz.sayara.screens.modeltabs.ModelTabsArgs
import com.newvisiondz.sayara.screens.versions.versionadapters.*
import com.stripe.android.ApiResultCallback
import com.stripe.android.Stripe
import com.stripe.android.model.Card
import com.stripe.android.model.Token
import kotlinx.android.synthetic.main.e_payment_layout.view.*
import java.lang.Exception


class Versions(args: Bundle?) : Fragment(), PlacesAdapter.SingleClickListener,
    EngineAdapter.SingleClickListener,
    FuelAdapter.SingleClickListener, ColorsAdapter.SingleClickListener,
    EnginePowerAdapter.SingleClickListener {

    private lateinit var stripe: Stripe
    private val argsRcv = args
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
    private lateinit var  versionViewModel: VersionsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentVersionsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_versions, container, false)
        val application = requireNotNull(activity).application
        stripe = Stripe(context!!, "pk_test_ZCo9GAsquoGzEJmQhXef01dq00nx22J82z")

        val viewModelFactory = VersionsViewModelFactory(application)
        versionViewModel = ViewModelProviders.of(this, viewModelFactory).get(VersionsViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = versionViewModel
        argsRcv?.let {
            modelId = ModelTabsArgs.fromBundle(it).modelId
            manufacturer = ModelTabsArgs.fromBundle(it).manufacturerId
            modelImages = ModelTabsArgs.fromBundle(it).modelImages.toMutableList()
        }!!


        binding.versionsSpinner.adapter =
            SpinnerAdapter(context!!, R.layout.spinner_element, versions)
        initializeAdapters(binding)
        binding.imageSlider.sliderAdapter =
            SliderAdapter(context!!, modelImages, "${context!!.getString(R.string.baseUrl)}/")
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
            displayDialog(command, versionViewModel)

            if (command.cars.isNotEmpty()) {
            } else {
                Toast.makeText(context, "Cette voiture n'est plus disponible", Toast.LENGTH_LONG)
                    .show()
            }
        })
        versionViewModel.commandConfirmed.observe(this, Observer { confirmed ->
            paymentDialog(confirmed)
//todo fix naviagtion
        })

        binding.orderVersion.setOnClickListener {
            //TODO do something about order Form
            //todo format String in the placesAdapter
            versionViewModel.sendCommand(
                manufacturer,
                modelId,
                versionId,
                userChoices.values.toMutableList()
            )
        }
        binding.compareButton.setOnClickListener {
            it.findNavController().navigate(
                VersionsDirections.actionVersionsToCompareVersions(
                    manufacturer,
                    modelId
                )
            )
        }

        binding.versionsSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
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
            .setMessage("Le prix estime est ${command.price}")
            .setTitle("Confirmation d'achat")
            .setCancelable(true)
            .setIcon(R.drawable.ic_money)
            .setPositiveButton("Proceed") { _, _ ->
                versionViewModel.confirmCommande(command.cars[0], command.price)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.cancel()
            }
        val alert = dialogBuilder.create()
        @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        alert.window.attributes.windowAnimations = R.style.RtlOverlay_DialogWindowTitle_AppCompat
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

    fun paymentDialog(command: CommandConfirmed) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(context!!)
        val mView = layoutInflater.inflate(R.layout.e_payment_layout, null)
        builder.setView(mView).setCancelable(true)
        builder.create().setCanceledOnTouchOutside(true)
        val dialog = builder.show()
        mView.payment_info_submit.setOnClickListener {
            payWithCreditCard(mView,command)
            dialog.dismiss()
        }
        mView.payment_info_cancel.setOnClickListener {
            dialog.cancel()
        }
    }



    private fun payWithCreditCard(
        dialog: View,
        command: CommandConfirmed
    ) {
        val card: Card = Card.create("4242424242424242", 12, 2020, "123")
        tokenizeCard(card,command)
//        val cardToSave: Card? = dialog.card_multiline_widget.card
//        if (cardToSave == null) {
//            SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
//                .setTitleText("Invalid Card Data!")
//                .show()
//            return
//        } else {
//
////            if (cardToSave.validateCVC()
////                && cardToSave.validateCard()
////                && cardToSave.validateExpiryDate()
////                && cardToSave.validateNumber()
////            ) {
////
////            }
//        }
    }

    private fun tokenizeCard(card: Card, command: CommandConfirmed) {
        stripe.createToken(
            card,
            object : ApiResultCallback<Token> {
                override fun onError(e: Exception) {
                    SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("A problem occured!")
                        .show()
                }

                override fun onSuccess(token: Token) {
                       versionViewModel.sendPayementTokentoBackend(command.id,token.id)
                }
            }
        )
    }
}

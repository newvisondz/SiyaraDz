package com.newvisiondz.sayara.screens.usedcardetails

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import cn.pedant.SweetAlert.SweetAlertDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.databinding.FragmentUsedCarDetailBinding
import com.newvisiondz.sayara.exceptions.LowerAmount
import com.newvisiondz.sayara.model.UsedCar
import com.newvisiondz.sayara.screens.versions.SliderAdapter
import com.stripe.android.ApiResultCallback
import com.stripe.android.Stripe
import com.stripe.android.model.Card
import com.stripe.android.model.Token
import kotlinx.android.synthetic.main.add_new_bid.view.*
import kotlinx.android.synthetic.main.e_payment_layout.view.*


class UsedCarDetailFragment : Fragment() {
    var viewModel: UsedCarDetailViewModel? = null
    var usedCar: UsedCar = UsedCar()
    private var ownerResponse: Boolean? = null

    private var usedCarId = ""
    private var bidId = ""
    private var acceptOrRefuse: Boolean? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentUsedCarDetailBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_used_car_detail, container, false)

        val application = requireNotNull(activity).application
        ownerResponse = UsedCarDetailFragmentArgs.fromBundle(arguments!!).myUsedCar
        usedCar = UsedCarDetailFragmentArgs.fromBundle(arguments!!).usedCar
        usedCarId = usedCar.id

        if (ownerResponse!!) {
            binding.bidsCarList.adapter = BidsAdapter(true,
                BidsAdapter.Listener {
                    bidId = it.bidId
                    acceptOrRefuse = true
//                    paymentDialog()
                    viewModel?.acceptBid(acceptOrRefuse!!, usedCarId, bidId)
                }, BidsAdapter.Listener {
                    bidId = it.bidId
                    acceptOrRefuse = false
                    viewModel?.acceptBid(acceptOrRefuse!!, usedCarId, bidId)
                })
        } else {
            binding.bidsCarList.adapter = BidsAdapter(false, null, null)
        }

        val viewModelFactory = UsedCarDetailViewModelFactory(application)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(UsedCarDetailViewModel::class.java)
        binding.viewModel = viewModel


        if (ownerResponse!!) {
            binding.makeNewOffere.visibility = View.GONE
        }
        viewModel?.getOwnerInfo(usedCar.owner)

        binding.usedCar = usedCar
        binding.lifecycleOwner = this
        viewModel?.createWithSuccess?.observe(this, Observer {
            if (it == true) {
                Toast.makeText(context, "Created with Success !", Toast.LENGTH_SHORT).show()
            }
        })
        viewModel?.bidResponse?.observe(this, Observer {
            if (it == true) {
                displayMessage("Accepted")
            } else if (it == false) {
                displayMessage("Rejected !")
            }
        })

        binding.executePendingBindings()
        val dividerItemDecoration = DividerItemDecoration(
            binding.bidsCarList.context,
            LinearLayout.HORIZONTAL
        )
        binding.bidsCarList.addItemDecoration(dividerItemDecoration)
        binding.makeNewOffere.setOnClickListener {
            displayNewBidDialog(usedCar.price)
        }
        viewModel?.getAllBidsOfCar(usedCar.id)
        binding.imageSlider.sliderAdapter =
            SliderAdapter(context!!, usedCar.images, context!!.getString(R.string.baseUrl))
        binding.usedCarPhone.setOnClickListener {
            Dexter.withActivity(context as Activity)
                .withPermission(Manifest.permission.CALL_PHONE)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        val callIntent = Intent(Intent.ACTION_CALL)
                        callIntent.data = Uri.parse("tel:${binding.usedCarPhone.text}")
                        startActivity(callIntent)
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permission: PermissionRequest,
                        token: PermissionToken
                    ) {
                        Toast.makeText(
                            context,
                            "You need to grant me permission !",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }).check()

        }
        return binding.root
    }

    private fun displayNewBidDialog(ownerPrice: Double) {
        val builder = AlertDialog.Builder(context!!)
        val mView = layoutInflater.inflate(R.layout.add_new_bid, null)
        builder.setView(mView).setCancelable(true)
        builder.create().setCanceledOnTouchOutside(true)
        val dialog = builder.show()
        mView.Add.setOnClickListener {
            try {
                val newPrice = mView.new_bid_price.text.toString().toDouble()
                if (newPrice < ownerPrice) throw LowerAmount()
                viewModel?.createNewBid(usedCar.id, newPrice)
            } catch (e: LowerAmount) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Insert a valid price", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
        mView.cancel.setOnClickListener {
            dialog.cancel()
        }
    }

    private fun displayMessage(message: String) {
        SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
            .setTitleText("Here's a message!")
            .setContentText(message)
            .show()
    }

}

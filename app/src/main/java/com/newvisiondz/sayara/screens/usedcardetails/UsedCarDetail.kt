package com.newvisiondz.sayara.screens.usedcardetails

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
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.databinding.FragmentUsedCarDetailBinding
import com.newvisiondz.sayara.model.UsedCar
import com.newvisiondz.sayara.screens.versions.SliderAdapter
import kotlinx.android.synthetic.main.add_new_bid.view.*
import java.lang.Exception


class UsedCarDetail : Fragment() {
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
        ownerResponse = UsedCarDetailArgs.fromBundle(arguments!!).myUsedCar
        usedCar = UsedCarDetailArgs.fromBundle(arguments!!).usedCar
        usedCarId = usedCar.id

        if (ownerResponse!!) {
            binding.bidsCarList.adapter = BidsAdapter(true,
                BidsAdapter.Listener {
                    bidId = it.bidId
                    acceptOrRefuse = true
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
            if (it == true){
                displayMessage("Accepted")
            }else  if (it == false){
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
            displayNewBidDialog()
        }
        viewModel?.getAllBidsOfCar(usedCar.id)
        binding.imageSlider.sliderAdapter =
            SliderAdapter(context!!, usedCar.images, context!!.getString(R.string.baseUrl))
        return binding.root
    }

    private fun displayNewBidDialog() {
        val builder = AlertDialog.Builder(context!!)
        val mView = layoutInflater.inflate(R.layout.add_new_bid, null)
        builder.setView(mView).setCancelable(true)
        builder.create().setCanceledOnTouchOutside(true)
        val dialog = builder.show()
        mView.Add.setOnClickListener {
            try {
                viewModel?.createNewBid(usedCar.id, mView.new_bid_price.text.toString().toDouble())
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "insert a valid price", Toast.LENGTH_SHORT).show()
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

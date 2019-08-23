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
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.databinding.FragmentUsedCarDetailBinding
import com.newvisiondz.sayara.model.UsedCar
import com.newvisiondz.sayara.screens.versions.SliderAdapter
import kotlinx.android.synthetic.main.add_new_bid.view.*
import java.lang.Exception


class UsedCarDetail : Fragment() {
    var viewModel: UsedCarDetailViewModel? = null
    var usedCar: UsedCar = UsedCar()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentUsedCarDetailBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_used_car_detail, container, false)
        val application = requireNotNull(activity).application
        binding.bidsCarList.adapter = BidsAdapter()
        val viewModelFactory = UsedCarDetailViewModelFactory(application)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(UsedCarDetailViewModel::class.java)
        binding.viewModel = viewModel
        usedCar = UsedCarDetailArgs.fromBundle(arguments!!).usedCar

        binding.usedCar = usedCar
        binding.lifecycleOwner = this
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
        builder.setView(mView).setTitle("Chooose an Action").setCancelable(true)
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
}

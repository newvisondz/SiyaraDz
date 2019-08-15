package com.newvisiondz.sayara.screens.bids

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.databinding.DataEntryDialogBinding
import com.newvisiondz.sayara.databinding.FragmentBidsBinding
import com.newvisiondz.sayara.utils.datePicker
import com.newvisiondz.sayara.utils.displaySnackBar
import kotlinx.android.synthetic.main.data_entry_dialog.*
import kotlinx.android.synthetic.main.fragment_bids.*


class Bids : Fragment() {
    companion object {
        const val TAKE_PHOTO = 123
        const val OPEN_GALLERY = 321
    }

    private var tmpUris = mutableListOf<Uri>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentBidsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_bids, container, false)
        val application = requireNotNull(this.activity).application
        val viewModel =
            ViewModelProviders.of(this, BidsViewModelFactory(application)).get(BidsViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.bidsList.adapter = BidsAdapter(BidsAdapter.Listener {
            Toast.makeText(context, it.carBrand, Toast.LENGTH_SHORT).show()
        })
        binding.addNewBid.setOnClickListener {
            val mBuilder = AlertDialog.Builder(
                context!!, android.R.style.Theme_Light_NoTitleBar_Fullscreen
            )
            val bindingDialog: DataEntryDialogBinding =
                DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.data_entry_dialog, null, false)
            bindingDialog.viewModel = viewModel
            mBuilder.setView(bindingDialog.root)
            val dialog = mBuilder.create()
            dialog.setCanceledOnTouchOutside(true)
            dialog.window?.attributes?.windowAnimations = R.style.PauseDialogAnimation
            dialog.show()
            bindingDialog.btnImg.setOnClickListener {
                takePhoto()
            }
//            bindingDialog.btnOk.setOnClickListener {
//                //                addItem(bindingDialog.root)
//                //TODO make button gets it's values form layout
//                dialog.dismiss()
//            }
            bindingDialog.btnCancel.setOnClickListener {
                dialog.cancel()
            }
            bindingDialog.carDistance.setOnClickListener {
//                datePicker(car_distance, context!!)
            }
            bindingDialog.color.setOnClickListener {
                ColorPickerDialogBuilder
                    .with(context)
                    .setTitle("Choose color")
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setOnColorSelectedListener { selectedColor: Int ->
                        Toast.makeText(
                            context,
                            "onColorSelected: 0x ${Integer.toHexString(selectedColor)}",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        binding.viewModel!!.newItem.value?.color = Integer.toHexString(selectedColor)

                        bindingDialog.color.setBackgroundColor(selectedColor)
                    }
                    .build()
                    .show()
            }

        }
        binding.swipeRefreshBids.setOnRefreshListener {
            displaySnackBar(binding.bidsLayout, "Nice ")
            swipeRefreshBids.isRefreshing = false
        }
        return binding.root
    }


    private fun addItem(mView: View) {
//        val newBid = Bid(
//            Random().nextInt(),
//            mView.brand_spinner.selectedItem.toString(),
//            mView.chassis_number.text.toString().toInt(),
//            mView.color.text.toString(),
//            mView.price.text.toString().toDouble(),
//            mView.current_miles.text.toString().toDouble()
//        )
//        newBid.uris = tmpUris
//        bidsList.add(newBid)
//        bids_list.adapter!!.notifyDataSetChanged()
    }

    private fun takePhoto() {
        val imgIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(imgIntent, TAKE_PHOTO)
    }

    fun openGallery() {
        val intent = Intent()
            .setType("image/*").putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            .setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(
            Intent.createChooser(intent, "Select a file"),
            OPEN_GALLERY
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == OPEN_GALLERY) && (resultCode == Activity.RESULT_OK)) {
            if (data!!.data != null) {
                tmpUris.add(data.data!!)
            } else if (data.clipData != null) {
                val clipArray = data.clipData
                for (i in 0 until clipArray!!.itemCount) {
                    tmpUris.add(clipArray.getItemAt(i).uri)
                }
            }
        } else if ((requestCode == TAKE_PHOTO) && (resultCode == Activity.RESULT_OK)) {
            val bitmapRes = data?.extras?.get("data") as Bitmap
        }
    }

    private fun initRecyclerView(view: View) {
//        bidsAdapter = BidsAdapter(bidsList, context as Context)
//        view.bids_list.adapter = bidsAdapter
    }

}

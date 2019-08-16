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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.databinding.DataEntryDialogBinding
import com.newvisiondz.sayara.databinding.FragmentBidsBinding
import com.newvisiondz.sayara.utils.datePicker
import com.newvisiondz.sayara.utils.displaySnackBar
import kotlinx.android.synthetic.main.camera_gallery.view.*
import kotlinx.android.synthetic.main.data_entry_dialog.view.*
import kotlinx.android.synthetic.main.fragment_bids.*


class Bids : Fragment() {
    companion object {
        const val TAKE_PHOTO = 123
        const val OPEN_GALLERY = 321
    }

    private lateinit var bitmapRes: Bitmap
    private var imageSourceChoice = 0
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
        binding.lifecycleOwner = this

        binding.bidsList.adapter = BidsAdapter(BidsAdapter.Listener {
            Toast.makeText(context, it.carBrand, Toast.LENGTH_SHORT).show()
        })

        viewModel.insertIsDone.observe(this, Observer {
            if (it == true) {
                (binding.bidsList.adapter as BidsAdapter).notifyDataSetChanged()
                viewModel.insertIsDone.value = null
                //todo optimize this code
            }
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
            //interaction handlers
            bindingDialog.btnImg.setOnClickListener {
                openImageSoureDialog()
            }
            bindingDialog.btnOk.setOnClickListener {
                if (imageSourceChoice == 1) viewModel.newItem.uris = tmpUris
                else if (imageSourceChoice == 2) viewModel.newItem.bitmap = bitmapRes
                viewModel.addItemToList()
                dialog.dismiss()
                imageSourceChoice = 0
            }

            bindingDialog.btnCancel.setOnClickListener {
                dialog.cancel()
            }
            bindingDialog.carDate.setOnClickListener {
                datePicker(bindingDialog.root.car_date, context!!)
            }
            bindingDialog.color.setOnClickListener {
                colorPicker(binding, bindingDialog)
            }
        }
        binding.swipeRefreshBids.setOnRefreshListener {
            displaySnackBar(binding.bidsLayout, "Nice ")
            swipeRefreshBids.isRefreshing = false
        }
        return binding.root
    }

    private fun colorPicker(
        binding: FragmentBidsBinding,
        bindingDialog: DataEntryDialogBinding
    ) {
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
                binding.viewModel!!.newItem.color = Integer.toHexString(selectedColor)

                bindingDialog.color.setBackgroundColor(selectedColor)
            }
            .build()
            .show()
    }

    private fun takePhoto() {
        val imgIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(imgIntent, TAKE_PHOTO)
    }

    private fun openGallery() {
        val intent = Intent()
            .setType("image/*").putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            .setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(
            Intent.createChooser(intent, "Select a file"),
            OPEN_GALLERY
        )
    }

    private fun openImageSoureDialog() {
        val builder = AlertDialog.Builder(context!!)
        val mView = layoutInflater.inflate(R.layout.camera_gallery, null)
        builder.setView(mView).setTitle("Chooose an Action").setCancelable(true)
        builder.create().setCanceledOnTouchOutside(true)
        val dialog = builder.show()
        mView.cameraButton.setOnClickListener {
            takePhoto()
            dialog.dismiss()
        }
        mView.galleryButton.setOnClickListener {
            openGallery()
            dialog.dismiss()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == OPEN_GALLERY) && (resultCode == Activity.RESULT_OK)) {
            imageSourceChoice = 1
            if (data!!.data != null) {
                tmpUris.add(data.data!!)
            } else if (data.clipData != null) {
                val clipArray = data.clipData
                for (i in 0 until clipArray!!.itemCount) {
                    tmpUris.add(clipArray.getItemAt(i).uri)
                }
            }
        } else if ((requestCode == TAKE_PHOTO) && (resultCode == Activity.RESULT_OK)) {
            imageSourceChoice = 2
            bitmapRes = data?.extras?.get("data") as Bitmap
        }
    }
}

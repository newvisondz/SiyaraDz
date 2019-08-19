package com.newvisiondz.sayara.screens.bids

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.BasePermissionListener
import com.karumi.dexter.listener.single.PermissionListener
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.databinding.DataEntryDialogBinding
import com.newvisiondz.sayara.databinding.FragmentBidsBinding
import com.newvisiondz.sayara.model.CarInfo
import com.newvisiondz.sayara.utils.datePicker
import com.newvisiondz.sayara.utils.displaySnackBar
import kotlinx.android.synthetic.main.camera_gallery.view.*
import kotlinx.android.synthetic.main.data_entry_dialog.view.*
import kotlinx.android.synthetic.main.fragment_bids.*
import java.security.Permission


class Bids : Fragment() {
    companion object {
        const val TAKE_PHOTO = 123
        const val OPEN_GALLERY = 321
    }

    private lateinit var bitmapRes: Bitmap
    private var imageSourceChoice = 0
    private var tmpUris = mutableListOf<Uri>()
    private var currentBrandId: String = ""
    private var currentModel: String = ""

    private val brands = mutableListOf<CarInfo>()
    private val models = mutableListOf<CarInfo>()
    private val versions = mutableListOf<CarInfo>()

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
            Toast.makeText(context, it.carBrandId, Toast.LENGTH_SHORT).show()
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



            bindingDialog.brandSpinner.adapter = InfoSpinner(context!!, R.layout.spinner_element, brands)
            bindingDialog.modelSpinner.adapter = InfoSpinner(context!!, R.layout.spinner_element, models)
            bindingDialog.versionSpinner.adapter = InfoSpinner(context!!, R.layout.spinner_element, versions)

            viewModel.brandList.observe(this, Observer { newBrands ->
                brands.clear()
                brands.addAll(newBrands)
                (bindingDialog.brandSpinner.adapter as InfoSpinner).notifyDataSetChanged()
            })
            viewModel.modelList.observe(this, Observer { newModels ->
                models.clear()
                models.addAll(newModels)
                (bindingDialog.modelSpinner.adapter as InfoSpinner).notifyDataSetChanged()
            })
            viewModel.versionList.observe(this, Observer { newVersions ->
                versions.clear()
                versions.addAll(newVersions)
                (bindingDialog.versionSpinner.adapter as InfoSpinner).notifyDataSetChanged()
            })
            bindingDialog.root.brand_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    currentBrandId = brands[position].id
                    viewModel.newItem.carBrandId = currentBrandId
                    viewModel.getModelsList(currentBrandId)
                    versions.clear()
                    (bindingDialog.versionSpinner.adapter as InfoSpinner).notifyDataSetChanged()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    if (brands.size > 0) {
                        currentBrandId = brands[0].id
                        viewModel.getModelsList(currentBrandId)
                    }

                }
            }
            bindingDialog.modelSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    currentModel = models[position].id
                    viewModel.getVersionList(currentBrandId, currentModel)
                    viewModel.newItem.carModel = models[position].id
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    if (models.size > 0) {
                        viewModel.getVersionList(currentBrandId, models[0].id)
                    }
                }
            }
            bindingDialog.versionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    viewModel.newItem.version = versions[position].id
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
            val dialog = mBuilder.create()
            dialog.setCanceledOnTouchOutside(true)
            dialog.window?.attributes?.windowAnimations = R.style.PauseDialogAnimation
            dialog.show()

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
        Dexter.withActivity(context as Activity)
            .withPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : BasePermissionListener() {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    val values = ContentValues()
                    values.put(MediaStore.Images.Media.TITLE, "New Picture")
                    values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
                    val imageUri = context?.contentResolver?.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values
                    )
                    val imgIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    imgIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                    startActivityForResult(imgIntent, TAKE_PHOTO)
                }
            }
            ).check()

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

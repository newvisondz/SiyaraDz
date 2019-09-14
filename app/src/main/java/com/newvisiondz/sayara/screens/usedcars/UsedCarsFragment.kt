package com.newvisiondz.sayara.screens.usedcars

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.*
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.karumi.dexter.Dexter
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.BasePermissionListener
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.databinding.AddUsedCarFormBinding
import com.newvisiondz.sayara.databinding.FragmentUsedCarsBinding
import com.newvisiondz.sayara.model.CarInfo
import com.newvisiondz.sayara.screens.tabs.TabsDirections
import com.newvisiondz.sayara.utils.colorMap
import com.newvisiondz.sayara.utils.datePicker
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_used_car_form.view.*
import kotlinx.android.synthetic.main.camera_gallery.view.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class UsedCarsFragment : Fragment() {
    companion object {
        const val TAKE_PHOTO = 123
        const val OPEN_GALLERY = 321
    }

    lateinit var binding: FragmentUsedCarsBinding

    private var tmpUris = mutableListOf<Uri>()
    private var currentBrandId: String = ""
    private var currentModel: String = ""
    private lateinit var photoURI: Uri
    private lateinit var dialog: AlertDialog


    private var isloading: Boolean = true
    private var pastVisibleItems: Int = 0
    private var visibleItemsCount: Int = 0
    private var totalItemsCount: Int = 0
    private var previousTotal: Int = 0

    private var viewThreshold = 6
    private var pageNumber: Int = 1

    private val brands = mutableListOf<CarInfo>()
    private val models = mutableListOf<CarInfo>()
    private val versions = mutableListOf<CarInfo>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_used_cars, container, false)
        val application = requireNotNull(this.activity).application
        val viewModel =
            ViewModelProviders.of(this, UsedCarsViewModelFactory(application))
                .get(UsedCarsViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setHasOptionsMenu(true)
        binding.swipeRefreshBids.setOnRefreshListener {
            viewModel.getAllUsedCars()
            pageNumber = 1
            isloading = false
            binding.swipeRefreshBids.isRefreshing = false
        }
        viewModel.errorObservable.observe(this, Observer {
            if (it == true) {
                Toast.makeText(context, "Please fill all the inputs !", Toast.LENGTH_SHORT).show()
            }
        })

        binding.searchFilter.setOnClickListener {
            val alertDialog = AlertDialog.Builder(context!!, R.style.DialogTheme).create()
            alertDialog.setTitle("Choose a Filter")
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Price") { dialog, _ ->
                try {
                    viewModel.filterUsedCars(
                        "",
                        maxPrice = binding.maxFilter.text.toString().toDouble(),
                        minPrice = binding.minFilter.text.toString().toDouble()
                    )
                } catch (e: NumberFormatException) {
                    Toast.makeText(context, "Bad number format", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Distance") { dialog, _ ->
                try {
                    viewModel.filterUsedCars(
                        "",
                        binding.maxFilter.text.toString().toDouble(),
                        binding.minFilter.text.toString().toDouble()
                    )
                } catch (e: NumberFormatException) {
                    Toast.makeText(context, "Bad number format", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            alertDialog.show()
        }
        activity!!.action_search
            .setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    viewModel.filterUsedCars(query)
                    return true
                }

                override fun onQueryTextChange(query: String): Boolean {
                    return false
                }
            })
        binding.usedCarsList.adapter = UsedCarsAdapter(UsedCarsAdapter.Listener {
            findNavController().navigate(TabsDirections.actionTabsToUsedCarsDetails(it,false))
        })

        viewModel.bidsList.observe(this, Observer {
            (binding.usedCarsList.adapter as UsedCarsAdapter).notifyDataSetChanged()
        })
        viewModel.insertIsDone.observe(this, Observer {
            if (it == true) {
                (binding.usedCarsList.adapter as UsedCarsAdapter).notifyDataSetChanged()
                viewModel.insertIsDone.value = null
                //todo optimize this code
                dialog.let(AlertDialog::dismiss)
                models.clear()
                versions.clear()
            }
        })
        binding.addNewBid.setOnClickListener {
            val mBuilder = AlertDialog.Builder(
                context!!, android.R.style.ThemeOverlay_Material
            )
            val bindingDialog: AddUsedCarFormBinding =
                DataBindingUtil.inflate(
                    LayoutInflater.from(context),
                    R.layout.add_used_car_form,
                    null,
                    false
                )
            bindingDialog.viewModel = viewModel
            mBuilder.setView(bindingDialog.root)

//Adapters

            bindingDialog.brandSpinner.adapter =
                InfoSpinner(context!!, R.layout.spinner_element, brands)
            bindingDialog.modelSpinner.adapter =
                InfoSpinner(context!!, R.layout.spinner_element, models)
            bindingDialog.versionSpinner.adapter =
                InfoSpinner(context!!, R.layout.spinner_element, versions)
            bindingDialog.colorSpinner.adapter = ColorSpinner(
                context!!, R.layout.color_spinner_item,
                colorMap.keys.toList()
            )
//Done Adapters
//Observers
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
//Done Observers
//spinnerItem selected item
            bindingDialog.colorSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        viewModel.newItemServer.color = colorMap[position]?.value!!
                    }

                }

            bindingDialog.brandSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        currentBrandId = brands[position].id
                        viewModel.newItemServer.manufacturerId = currentBrandId
                        viewModel.newItemServer.manufacturer = brands[position].name
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
            bindingDialog.modelSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        currentModel = models[position].id
                        viewModel.getVersionList(currentBrandId, currentModel)
                        viewModel.newItemServer.modelId = models[position].id
                        viewModel.newItemServer.model = models[position].name
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        if (models.size > 0) {
                            viewModel.getVersionList(currentBrandId, models[0].id)
                        }
                    }
                }
            bindingDialog.versionSpinner.onItemSelectedListener =
                object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        parent: AdapterView<*>?,
                        view: View?,
                        position: Int,
                        id: Long
                    ) {
                        viewModel.newItemServer.versionId = versions[position].id
                        viewModel.newItemServer.version = versions[position].name
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {}
                }
            dialog = mBuilder.create()
            dialog.setCanceledOnTouchOutside(true)
            dialog.window?.attributes?.windowAnimations = R.style.PauseDialogAnimation
            dialog.show()

            bindingDialog.btnImg.setOnClickListener {
                openImageSoureDialog()
            }
            bindingDialog.btnOk.setOnClickListener {
                viewModel.newItemServer.uris = tmpUris
                viewModel.addItemToList()
            }

            bindingDialog.btnCancel.setOnClickListener {
                dialog.cancel()
            }
            bindingDialog.carDate.setOnClickListener {
                datePicker(bindingDialog.root.car_date, context!!)
            }
//            bindingDialog.colorSpinner.setOnClickListener {
//                //                colorPicker(binding, bindingDialog)
//            }
        }
        binding.usedCarsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                visibleItemsCount = binding.usedCarsList.layoutManager!!.childCount
                totalItemsCount = binding.usedCarsList.layoutManager!!.itemCount
                pastVisibleItems =
                    (binding.usedCarsList.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                if (dy > 0) {
                    if (isloading) {
                        if (totalItemsCount > previousTotal) {
                            isloading = false
                            previousTotal = totalItemsCount
                        }
                    }
                    if (!isloading && (totalItemsCount - visibleItemsCount) <= (pastVisibleItems + viewThreshold)) {
                        pageNumber++
                        viewModel.performPagination(pageNumber, viewThreshold)
                        isloading = true
                    }
                }
            }
        })
        binding.usedCarsList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    binding.addNewBid.shrink()
                } else {
                    binding.addNewBid.extend()
                }
            }
        })

        return binding.root
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

    override fun onPause() {
        super.onPause()
        pageNumber = 1
    }

    private fun openImageSoureDialog() {
        val builder = AlertDialog.Builder(context!!)
        val mView = layoutInflater.inflate(R.layout.camera_gallery, null)
        builder.setView(mView).setTitle("Chooose an Action").setCancelable(true)
        builder.create().setCanceledOnTouchOutside(true)
        val dialog = builder.show()
        mView.cameraButton.setOnClickListener {
            dispatchTakePictureIntent()
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
            if (data!!.data != null) {
                tmpUris.add(data.data!!)
            } else if (data.clipData != null) {
                val clipArray = data.clipData
                for (i in 0 until clipArray!!.itemCount) {
                    tmpUris.add(clipArray.getItemAt(i).uri)
                }
            }
        } else if ((requestCode == TAKE_PHOTO) && (resultCode == Activity.RESULT_OK)) {
            tmpUris.add(photoURI)
        }
    }

    @Throws(IOException::class)
    fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName: String = "JPEG_" + timeStamp + "_"
        val storageDir: File = context!!.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!

        return File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",         /* suffix */
            storageDir      /* directory */
        )
    }


    private fun dispatchTakePictureIntent() {
        Dexter.withActivity(context as Activity)
            .withPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : BasePermissionListener() {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                        // Ensure that there's a camera activity to handle the intent
                        takePictureIntent.resolveActivity(context!!.packageManager)?.also {
                            // Create the File where the photo should go
                            val photoFile: File? = try {
                                createImageFile()
                            } catch (ex: IOException) {
                                null
                            }
                            // Continue only if the File was successfully created
                            photoFile?.also {
                                photoURI = FileProvider.getUriForFile(
                                    context!!,
                                    "com.example.android.fileprovider",
                                    it
                                )
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                                startActivityForResult(takePictureIntent, TAKE_PHOTO)
                            }
                        }
                    }
                }
            }
            ).check()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.display_filter) {
            if (binding.filtersGroup.visibility == View.VISIBLE) binding.filtersGroup.visibility =
                View.GONE
            else binding.filtersGroup.visibility = View.VISIBLE
            return true
        }
        return false
    }
}

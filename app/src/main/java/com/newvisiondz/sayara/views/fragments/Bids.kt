package com.newvisiondz.sayara.views.fragments

import android.app.Activity
import android.content.Context
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
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.adapters.BidsAdapter
import com.newvisiondz.sayara.model.Bid
import com.newvisiondz.sayara.utils.displaySnackBar
import kotlinx.android.synthetic.main.data_entry_dialog.view.*
import kotlinx.android.synthetic.main.fragment_bids.*
import kotlinx.android.synthetic.main.fragment_bids.view.*
import java.util.*


class Bids : androidx.fragment.app.Fragment() {
    companion object {
        const val TAKE_PHOTO = 123
        const val OPEN_GALLERY = 321
    }

    private var tmpUris = mutableListOf<Uri>()
    private lateinit var bidsList: MutableList<Bid>
    private var bidsAdapter: BidsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bids, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bidsList = mutableListOf(
            Bid(0, "Mercedes Classe C 220 AMG Line 2015", 1283, "Dar Beida", 12.9, 12200.0),
            Bid(1, "Volkswagen Golf 6 Match 2 2013 ", 18233, "Dar Beida", 12.9, 12200.0),
            Bid(2, "Mercedes Classe C 220 AMG Line 2015", 128312391, "Dar Beida", 12.9, 12200.0),
            Bid(3, "Volkswagen Golf 6 Match 2 2013 ", 4217931, "Dar Beida", 14.9, 1233.0)
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView(view)
        add_new_bid.setOnClickListener {
            val mBuilder = AlertDialog.Builder(
                context!!, android.R.style.Theme_Light_NoTitleBar_Fullscreen
            )
            val mView = layoutInflater.inflate(R.layout.data_entry_dialog, null)
            mBuilder.setView(mView)
            val dialog = mBuilder.create()
            dialog.setCanceledOnTouchOutside(true)
            dialog.window.attributes.windowAnimations = R.style.PauseDialogAnimation
            dialog.show()
            mView.btnImg.setOnClickListener {
                takePhoto()
            }
            mView.btnOk.setOnClickListener {
                addItem(mView)
                //TODO post request here
                dialog.dismiss()
            }
            mView.btnCancel.setOnClickListener {
                dialog.cancel()
            }
            mView.color.setOnClickListener {
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
                        val res = Integer.toHexString(selectedColor)
                        mView.color.setBackgroundColor(selectedColor)
                    }
                    .build()
                    .show()
            }
        }
        view.swipeRefreshBids.setOnRefreshListener {
            displaySnackBar(view, "Nice ")
            swipeRefreshBids.isRefreshing = false
        }
    }


    private fun addItem(mView: View) {
        val newBid = Bid(
            Random().nextInt(),
            mView.model_spinner.selectedItem.toString(),
            mView.chassis_number.text.toString().toInt(),
            mView.color.text.toString(),
            mView.price.text.toString().toDouble(),
            mView.current_miles.text.toString().toDouble()
        )
        newBid.uris = tmpUris
        bidsList.add(newBid)
        bids_list.adapter!!.notifyDataSetChanged()
    }

    fun takePhoto() {
        val imgIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(imgIntent, TAKE_PHOTO)
    }

    fun openGallery() {
        val intent = Intent()
            .setType("image/*").putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            .setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Select a file"), OPEN_GALLERY)
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
        bidsAdapter = BidsAdapter(bidsList, context as Context)
        view.bids_list.adapter = bidsAdapter
    }

}

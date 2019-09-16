package com.newvisiondz.sayara.screens.myusedcars


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.databinding.FragmentMyUsedCarsBinding
import com.newvisiondz.sayara.screens.usedcars.UsedCarsAdapter
import com.newvisiondz.sayara.utils.isOnline


class MyUsedCars : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentMyUsedCarsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my_used_cars, container, false)
        val application = requireNotNull(this.activity).application

        val mViewModel =
            ViewModelProviders.of(this, UsedCarsViewModelFactory(application))
                .get(UsedCarsViewModel::class.java)
        binding.viewModel = mViewModel
        binding.lifecycleOwner = this
        binding.myBidsList.adapter = UsedCarsAdapter(UsedCarsAdapter.Listener{
            findNavController().navigate(MyUsedCarsDirections.actionMyUsedCarsToUsedCarDetail(it,true))
        })


        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val alertDialog = AlertDialog.Builder(context!!,R.style.DialogTheme).create()
                alertDialog.setTitle("Attention !")
                alertDialog.setMessage("Voue êtes entrain de supprimer cet élément !")
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Annuler") { dialog, which ->
                    binding.myBidsList.adapter?.notifyItemChanged(viewHolder.adapterPosition)
                }
                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Terminer") { dialog, which ->
                    if (isOnline(context!!)) {
                        mViewModel.deleteUsedCarAd(viewHolder.adapterPosition)
                    } else Toast.makeText(
                        context,
                        "You'll have to be online to delete this used car permanantly !",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                alertDialog.show()
            }
        }).attachToRecyclerView(binding.myBidsList)

        mViewModel.deletedWithSuccess.observe(this, Observer {
            if (it == true) {
                Toast.makeText(context, "Deleted !", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(
                    context,
                    "There was a problem with the server or your internet !",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        })
        return binding.root
    }
}

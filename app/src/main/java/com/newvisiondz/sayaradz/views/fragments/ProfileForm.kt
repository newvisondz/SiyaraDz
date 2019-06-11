package com.newvisiondz.sayaradz.views.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.newvisiondz.sayaradz.R
import com.newvisiondz.sayaradz.databinding.FragmentProfileFormBinding
import com.newvisiondz.sayaradz.views.viewModel.ProfileFormViewModel
import com.newvisiondz.sayaradz.views.viewModel.ProfileFormViewModelFactory
import kotlinx.android.synthetic.main.fragment_profile_form.*
import java.util.*


class ProfileForm : Fragment() {
    private var userInfo: SharedPreferences? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userInfo = context!!.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
        val binding: FragmentProfileFormBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile_form, container, false)
        val application = requireNotNull(this.activity).application
        val viewModelFactory = ProfileFormViewModelFactory(application)
        val profileFormViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProfileFormViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = profileFormViewModel
        profileFormViewModel.updateWithSuccess.observe(this, Observer {
            if (it == true) {
                view?.findNavController()?.navigate(ProfileFormDirections.actionProfileFormToTabs())
            }
        })

        binding.userDate.setOnClickListener { datePicker() }
        return binding.root
    }

    private fun datePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(
            activity as Context,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                this.user_date.text = "$year-$monthOfYear-$dayOfMonth"
            },
            year,
            month,
            day
        )

        dpd.show()
    }
}

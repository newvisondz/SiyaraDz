package com.newvisiondz.sayara.views.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.databinding.FragmentProfileFormBinding
import com.newvisiondz.sayara.views.viewModel.ProfileFormViewModel
import com.newvisiondz.sayara.views.viewModel.ProfileFormViewModelFactory
import kotlinx.android.synthetic.main.fragment_profile_form.*
import java.util.*


class ProfileForm : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentProfileFormBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile_form, container, false)
        val application = requireNotNull(this.activity).application
        val viewModelFactory = ProfileFormViewModelFactory(application)
        val profileFormViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProfileFormViewModel::class.java)
        binding.lifecycleOwner = this
        binding.viewModel = profileFormViewModel

        profileFormViewModel.updateWithSuccess.observe(this, Observer {
            if (it == true) {
                Snackbar.make(binding.profileFormConstraint, "Update with success", Snackbar.LENGTH_INDEFINITE)
                    .setAction(
                        "Done"
                    ) { view?.findNavController()?.navigate(ProfileFormDirections.actionProfileFormToTabs()) }.show()

            } else if (it == false)  {
                Snackbar.make(binding.profileFormConstraint, "Try again..!!", Snackbar.LENGTH_INDEFINITE).show()
            }
        })
        binding.userDate.setOnClickListener { datePicker() }
        return binding.root
    }

    private fun datePicker() {
        val c = Calendar.getInstance()
        val yearFromCal = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(
            activity as Context,
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                this.user_date.text = context?.getString(R.string.date_format, year, monthOfYear, dayOfMonth)
            },
            yearFromCal,
            month,
            day
        )

        dpd.show()
    }
}
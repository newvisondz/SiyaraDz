package com.newvisiondz.sayara.screens.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.Scope
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.databinding.FragmentProfileBinding
import com.newvisiondz.sayara.screens.entryScreens.LoginActivity
import com.newvisiondz.sayara.screens.tabs.TabsDirections


class Profile : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(this.activity).application

        val binding: FragmentProfileBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        val viewModelFactory = ProfileViewModelFactory(application)
        val profileViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProfileViewModel::class.java)
        binding.viewModel = profileViewModel
        binding.lifecycleOwner = this

        binding.buttonProfile.setOnClickListener {
            it.findNavController().navigate(TabsDirections.actionTabsToProfileForm())
        }

        binding.buttonUsedCars.setOnClickListener {
            it.findNavController().navigate(TabsDirections.actionTabsToUsedCars())
        }

        binding.buttonMymodels.setOnClickListener {
//            it.findNavController().navigate(TabsDirections.actionTabsToMyModels())
        }

        binding.buttonMyversions.setOnClickListener {
//            it.findNavController().navigate(TabsDirections.actionTabsToMyVersions())
        }
        binding.buttonMyoffers.setOnClickListener {
            it.findNavController().navigate(TabsDirections.actionTabsToMyOffers())
        }
        binding.viewModel!!.logInType.observe(this, Observer {
            val intent = Intent(context, LoginActivity::class.java)
            if (it == "facebook") {
                logOutFacebook(intent)
            } else if (it == "google") {
                logOutGoogle(intent)
            }
        })
        binding.viewModel!!.imgUrl.observe(this, Observer { url ->
            Glide.with(view!!).load(url).circleCrop().into(binding.userImage)
        })
        return binding.root
    }

    private fun logOutGoogle(intent: Intent) {
        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope(Scopes.APP_STATE))
            .requestServerAuthCode(getString(R.string.server_client_id))
            .requestEmail()
            .build()
        val account = GoogleSignIn.getClient(context!!, signInOptions)

        account.signOut().addOnSuccessListener {
            startActivity(intent)
            (context as Activity).finish()
        }
    }

    private fun logOutFacebook(intent: Intent) {
        LoginManager.getInstance().logOut()
        startActivity(intent)
        (context as Activity).finish()
    }
}

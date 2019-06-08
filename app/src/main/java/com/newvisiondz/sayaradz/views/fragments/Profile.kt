package com.newvisiondz.sayaradz.views.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.Scope
import com.newvisiondz.sayaradz.R
import com.newvisiondz.sayaradz.utils.clearUserInfo
import com.newvisiondz.sayaradz.utils.getUserInfo
import com.newvisiondz.sayaradz.views.LoginActivity
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*


class Profile : androidx.fragment.app.Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private var userInfo: SharedPreferences? = null
    private var userInfoTmp = arrayOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userInfo = context!!.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
        userInfoTmp = getUserInfo(userInfo!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        view.button_profile.setOnClickListener {
            val action = TabsDirections.actionTabsToProfileForm()
            //action.setBidId(bidId)
            NavHostFragment.findNavController(this).navigate(action)
            Log.i("Navigating", "abs to Profile Form,")
        }

        view.button_mybids.setOnClickListener {
            val action = TabsDirections.actionTabsToMyBids()
            //action.setBidId(bidId)
            NavHostFragment.findNavController(this).navigate(action)
            Log.i("Navigating", "Tabs to My Bids,")
        }

        view.button_mymodels.setOnClickListener {
            val action = TabsDirections.actionTabsToMyModels()
            //action.setBidId(bidId)
            NavHostFragment.findNavController(this).navigate(action)
            Log.i("Navigating", "Tabs to My Models,")
        }

        view.button_myversions.setOnClickListener {
            val action = TabsDirections.actionTabsToMyVersions()
            //action.setBidId(bidId)
            NavHostFragment.findNavController(this).navigate(action)
            Log.i("Navigating", "Tabs to My Versions,")
        }

        view.button_myoffers.setOnClickListener {
            val action = TabsDirections.actionTabsToMyOffers()
            //action.setBidId(bidId)
            NavHostFragment.findNavController(this).navigate(action)
            Log.i("Navigating", "Tabs to My Offers,")
        }
        view.button_logout.setOnClickListener {
            Log.i("prefs", userInfoTmp[0] + "type is " + userInfoTmp[4])
            val intent = Intent(context, LoginActivity::class.java)
            if (userInfoTmp[4] == "facebook") {
                LoginManager.getInstance().logOut()
                clearUserInfo(userInfo!!)
                Log.i("prefs", "done")
                startActivity(intent)
                (context as Activity).finish()
            } else if (userInfoTmp[4] == "google") {
                val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestScopes(Scope(Scopes.APP_STATE))
                    .requestServerAuthCode(getString(R.string.server_client_id))
                    .requestEmail()
                    .build()
                val account = GoogleSignIn.getClient(context!!, signInOptions)

                Log.i("prefs", "signing out")
                account.signOut().addOnSuccessListener {
                    clearUserInfo(userInfo!!)
                    Log.i("prefs", "done")
                    startActivity(intent)
                    (context as Activity).finish()
                }
            }
        }
        return view
    }

    override fun onResume() {
        super.onResume()

        user_dis_name.text = userInfoTmp[0]
        if (userInfoTmp[4] == "google") {
            Glide.with(view!!).load(userInfoTmp[1]).into(user_image)
        } else {
            Glide.with(view!!).load("https://graph.facebook.com/${userInfoTmp[1]}/picture?type=large").into(user_image)
        }
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

}

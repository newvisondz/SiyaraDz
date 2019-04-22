package com.newvisiondz.sayaradz.views.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
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
import com.newvisiondz.sayaradz.Utils.PrefrencesHandler
import com.newvisiondz.sayaradz.views.LoginActivity
import com.newvisiondz.sayaradz.views.MainActivity
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*


class Profile : Fragment() {
    private var listener: OnFragmentInteractionListener? = null
    private var userInfo: SharedPreferences? = null
    private var prefrencesHandler = PrefrencesHandler()
    private var userInfoTmp = arrayOf<String>()
    //    private var googleAUth=
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        userInfo = context!!.getSharedPreferences("userinfo", Context.MODE_PRIVATE)
        userInfoTmp = prefrencesHandler.getUserInfo(userInfo!!)
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
            if (userInfoTmp[4] == "facebook") {
                LoginManager.getInstance().logOut()
                prefrencesHandler.clearUserInfo(userInfo!!)
            } else {
                val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestScopes(Scope(Scopes.APP_STATE))
                    .requestServerAuthCode(getString(R.string.server_client_id))
                    .requestEmail()
                    .build()
                prefrencesHandler.clearUserInfo(userInfo!!)
                var account = GoogleSignIn.getClient(context!!, signInOptions)
                account.signOut()
                Log.i("bids", "success")

            }
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
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

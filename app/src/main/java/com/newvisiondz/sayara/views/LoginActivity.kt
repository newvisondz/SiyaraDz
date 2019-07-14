package com.newvisiondz.sayara.views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.databinding.ActivityLoginBinding
import com.newvisiondz.sayara.services.auth.FacebookAuthentification
import com.newvisiondz.sayara.services.auth.GoogleAuthentification


class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var callbackManager = CallbackManager.Factory.create()!!

    private var authGoogle: GoogleAuthentification? = null
    private var authFacebook: FacebookAuthentification? = null
    private var userInfo: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityLoginBinding>(this, R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        userInfo = getSharedPreferences("userinfo", Context.MODE_PRIVATE)
        authFacebook = FacebookAuthentification(this, auth)
        authGoogle = GoogleAuthentification(this, auth)



        binding.loging.setOnClickListener {
            binding.progressLogin.visibility = View.VISIBLE
            authGoogle!!.signIn()
            binding.progressLogin.visibility = View.GONE
        }
        binding.loginFb.setPermissions("email", "public_profile")
        binding.loginFb.setOnClickListener {
            LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(result: LoginResult?) {
                    binding.progressLogin.visibility = View.VISIBLE
//                    authFacebook!!.signIn(result!!)
                    authFacebook!!.handleFacebookAccessToken(result?.accessToken!!)
                    binding.progressLogin.visibility = View.GONE
                }

                override fun onCancel() {
                    Log.d("facebooklog", "facebook:onCancel")
                }

                override fun onError(error: FacebookException?) {
                    Log.d("facebooklog", "facebook:onError", error)

                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GoogleAuthentification.REQ_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)
                authGoogle?.firebaseAuthWithGoogle(account!!, this)
//                authGoogle?.handleResult(account)
            } catch (e: ApiException) {
                Log.w("GOOGLE", "Google sign in failed", e)
            }
        }
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }


}

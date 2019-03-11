package com.example.siyaradz.views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.siyaradz.R
import com.example.siyaradz.services.Auth.GoogleAuthentification
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.Scopes
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Scope

class MainActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {
    private var callbackManager = CallbackManager.Factory.create()!!
    private var authentification: GoogleAuthentification? = null
    private var userInfo: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userInfo = getSharedPreferences("userinfo", Context.MODE_PRIVATE)

        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope(Scopes.APP_STATE))
            .requestServerAuthCode(getString(R.string.server_client_id))
            .requestEmail()
            .build()

        authentification = GoogleAuthentification(this, signInOptions)
        authentification!!.signInButton.setOnClickListener {
            authentification!!.signIn(this)
        }
//        authentification!!.signOutButton.setOnClickListener {
//            authentification!!.signOut()
//        }

    }


    override fun onConnectionFailed(p0: ConnectionResult) {
        Toast.makeText(this, "Une faible connection internet ", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == authentification!!.REQ_CODE) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            this.authentification!!.handleResult(result)
        }
    }
}

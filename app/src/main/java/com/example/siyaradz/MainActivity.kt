package com.example.siyaradz

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.util.Util
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), GoogleApiClient.OnConnectionFailedListener {

    private var googleClient: GoogleApiClient? = null
    private val REQ_CODE: Int = 9001
    private lateinit var userName: TextView
    private lateinit var signInButton: SignInButton
    private lateinit var signOutButton : Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        var signInOptions= GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        googleClient =GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build()

        userName = findViewById(R.id.userName)
        signInButton = findViewById(R.id.googleSignIn)
        signOutButton=findViewById(R.id.googleSignOut)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        userName.visibility = View.GONE


        signInButton.setOnClickListener{
            signIn()
        }
        signOutButton.setOnClickListener{
            signOut()
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        Toast.makeText(this,"failed",Toast.LENGTH_SHORT)
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

    public fun signIn() {
       intent=Auth.GoogleSignInApi.getSignInIntent(googleClient)
        startActivityForResult(intent,REQ_CODE)
    }

    public fun signOut() {
        Auth.GoogleSignInApi.signOut(googleClient).setResultCallback {
            updateUi(false)
        }
    }
    public fun handleResult(result:GoogleSignInResult){
        if (result.isSuccess){
            var account =result.signInAccount
            var name = account?.displayName
            var email =account?.email
            userName.text = email
            updateUi(true)

        }else{
            updateUi(false)
        }

    }
    public fun  updateUi(isLogedIn:Boolean) {
        if (isLogedIn) {
            userName.visibility=View.VISIBLE
            signInButton.visibility=View.GONE

        }else {
            userName.visibility=View.GONE
            signInButton.visibility=View.VISIBLE
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode ==REQ_CODE) {
            var result =Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            handleResult(result)
        }
    }
}

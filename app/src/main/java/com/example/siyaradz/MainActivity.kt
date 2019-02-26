package com.example.siyaradz

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.facebook.CallbackManager

import kotlinx.android.synthetic.main.activity_main.*
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.FacebookCallback
import com.facebook.login.widget.LoginButton

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var callbackManager = CallbackManager.Factory.create()!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)


        var loginButton = findViewById<LoginButton>(R.id.login_button)
        loginButton.setReadPermissions("email")


        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                val builder = AlertDialog.Builder(this@MainActivity)
                builder.setMessage(if (loginResult.accessToken!=null) "Logged in" else "Login error")
                Log.i("token",loginResult.accessToken.token)

                var client =RetrofitClient.instance

                val dialog:AlertDialog = builder.create()
                dialog.show()

            }

            override fun onCancel() {
                println("canceled")
            }

            override fun onError(exception: FacebookException) {
                println("error")
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}

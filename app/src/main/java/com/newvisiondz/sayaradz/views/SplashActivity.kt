package com.newvisiondz.sayaradz.views

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.newvisiondz.sayaradz.R
import com.newvisiondz.sayaradz.Utils.NetworkUtils
import com.newvisiondz.sayaradz.Utils.PrefrencesHandler
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : AppCompatActivity() {
    private var prefrencesHandler = PrefrencesHandler()
    private lateinit var userInfo: SharedPreferences
    private var netUilts = NetworkUtils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        userInfo = getSharedPreferences("userinfo", Context.MODE_PRIVATE)

        if (!netUilts.isOnline(this)) {
            val dialogBuilder = AlertDialog.Builder(this@SplashActivity)
                .setMessage("You'll need internet connection in order to use this app")
                .setTitle("No internet connection")
                .setNeutralButton("Try again!") { dialog, id ->
                    checkNet.visibility = View.VISIBLE
                    dialog.dismiss()
                }
            val alert = dialogBuilder.create()
            alert.show()
            getStarted.visibility = View.GONE
        }
        checkNet.setOnClickListener {
            if (netUilts.isOnline(this)) {
                checkNet.visibility = View.GONE
                getStarted.visibility = View.VISIBLE
            }
        }

        getStarted.setOnClickListener {
            if (prefrencesHandler.getUserToken(userInfo).equals("Not Found")) {
                val intent = Intent(
                    applicationContext,
                    LoginActivity::class.java
                )
                startActivity(intent)
            } else {
                val intent = Intent(
                    applicationContext,
                    MainActivity::class.java
                )
                startActivity(intent)
            }
            finish()
        }
        //TODO splash activity needs a better design
    }
}

package com.newvisiondz.sayaradz.views

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.newvisiondz.sayaradz.R
import com.newvisiondz.sayaradz.Utils.PrefrencesHandler
import kotlinx.android.synthetic.main.content_splash.*


class SplashActivity : AppCompatActivity() {
    private var anim: Animation? = null
    private var userInfo: SharedPreferences? = null
    private var prefsHandler = PrefrencesHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        userInfo = getSharedPreferences("userinfo", Context.MODE_PRIVATE)
        anim = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in_long)
        anim!!.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation?) {
                val token = prefsHandler.getUserToken(userInfo!!)
                if (token.equals("Not Found")) {
                    val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                    startActivity(intent)
                } else {
                    val intent = Intent(this@SplashActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationStart(animation: Animation?) {
            }
        })
        splashText.startAnimation(anim)
    }

}

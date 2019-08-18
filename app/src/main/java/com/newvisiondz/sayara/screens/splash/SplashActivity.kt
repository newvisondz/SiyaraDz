package com.newvisiondz.sayara.screens.splash

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.databinding.ActivitySplashBinding
import com.newvisiondz.sayara.screens.entryScreens.LoginActivity
import com.newvisiondz.sayara.screens.entryScreens.MainActivity
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : AppCompatActivity() {
    private lateinit var alertDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_splash)
        val viewModelFactory = SplashViewModelFactory(application, lifecycle)
        val splashViewModel = ViewModelProviders.of(this, viewModelFactory).get(SplashViewModel::class.java)
        binding.viewModel = splashViewModel
        binding.lifecycleOwner = this

        val uptodown: Animation = AnimationUtils.loadAnimation(this, R.anim.uptodown)

        splashBg.animation = uptodown
        logo.animation = uptodown
        textlogo.animation = AnimationUtils.loadAnimation(this, R.anim.downtoup)


        splashViewModel.online.observe(this, Observer { online ->
            alertDialog = showDialog(this@SplashActivity)
            if (!online) {
                alertDialog.show()
            } else
                alertDialog.dismiss()
        })

        splashViewModel.userConnected.observe(this, Observer { connected ->
            if (!connected) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            finish()
        })
        val runnable = Runnable {
            splashViewModel.navigateToMainActivity()
        }
        Handler().postDelayed(runnable, 2000)
    }

    private fun showDialog(context: Context): AlertDialog {
        val dialogBuilder = AlertDialog.Builder(context)
            .setMessage("You'll need internet connection in order to use this app")
            .setTitle("No internet connection")
            .setNeutralButton("Try again!") { dialog, _ ->
                dialog.dismiss()
            }
        return dialogBuilder.create()
    }



}

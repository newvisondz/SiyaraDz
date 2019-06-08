package com.newvisiondz.sayaradz.views

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.newvisiondz.sayaradz.R
import com.newvisiondz.sayaradz.databinding.ActivitySplashBinding
import com.newvisiondz.sayaradz.views.viewModel.SplashViewModel
import com.newvisiondz.sayaradz.views.viewModel.SplashViewModelFactory


class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivitySplashBinding>(this, R.layout.activity_splash)
        val viewModelFactory = SplashViewModelFactory(application,lifecycle)
        val splashViewModel = ViewModelProviders.of(this, viewModelFactory).get(SplashViewModel::class.java)
        binding.viewModel = splashViewModel
        binding.lifecycleOwner = this

        splashViewModel.online.observe(this, Observer { online ->
            if (!online) {
                val dialogBuilder = AlertDialog.Builder(this@SplashActivity)
                    .setMessage("You'll need internet connection in order to use this app")
                    .setTitle("No internet connection")
                    .setNeutralButton("Try again!") { dialog, _ ->
                        //                        checkNet.visibility = View.VISIBLE
                        dialog.dismiss()
                    }
                val alert = dialogBuilder.create()
                alert.show()
//                getStarted.visibility = View.GONE
            }
        })
        binding.getStarted.setOnClickListener {
            splashViewModel.navigateToMainActivity()
            Log.i("Splash", "Button is clicked")
        }
        splashViewModel.userConnected.observe(this, Observer { connected ->
            Log.i("Splash", "Observing with $connected")
            if (!connected) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            finish()
        })
//
//        binding.checkNet.setOnClickListener {
//            if (isOnline(this)) {
//                checkNet.visibility = View.GONE
//                getStarted.visibility = View.VISIBLE
//            }
//        }

//        binding.getStarted.setOnClickListener {
//            if (prefrencesHandler.getUserToken(userInfo).equals("Not Found")) {
//                val intent = Intent(
//                    applicationContext,
//                    LoginActivity::class.java
//                )
//                startActivity(intent)
//            } else {
//                val intent = Intent(
//                    applicationContext,
//                    MainActivity::class.java
//                )
//                startActivity(intent)
//            }
//            finish()
//        }
        //TODO splash activity needs a better design
    }

}

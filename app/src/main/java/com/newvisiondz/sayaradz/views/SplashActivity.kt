package com.newvisiondz.sayaradz.views

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
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
        val viewModelFactory = SplashViewModelFactory(application, lifecycle)
        val splashViewModel = ViewModelProviders.of(this, viewModelFactory).get(SplashViewModel::class.java)
        binding.viewModel = splashViewModel
        binding.lifecycleOwner = this

        splashViewModel.online.observe(this, Observer { online ->
            val alertDialog = showDialog(this@SplashActivity)
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

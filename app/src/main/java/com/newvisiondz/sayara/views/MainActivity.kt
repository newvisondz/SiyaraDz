package com.newvisiondz.sayara.views

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    val TAG="FirebaseMessages"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val navController = findNavController(R.id.navigation_host)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, _: Bundle? ->
            if (nd.id != nc.graph.findNode(R.id.tabs)?.id && nd.id != nc.graph.findNode(R.id.models)?.id) {
                binding.actionSearch.visibility = View.GONE
            } else {
                binding.actionSearch.visibility = View.VISIBLE
            }
        }
    }

}

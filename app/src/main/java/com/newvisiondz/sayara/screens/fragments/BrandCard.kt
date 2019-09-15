package com.newvisiondz.sayara.screens.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.google.android.material.card.MaterialCardView
import com.newvisiondz.sayara.R

class BrandCard : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_brands, container, false)
        val card = view?.findViewById<MaterialCardView>(R.id.model_card)
        card?.setOnClickListener {
            findNavController(this).navigate(R.id.action_tabs_to_models)
            Log.i("Nav", "Hello")
        }
        return view
    }

}

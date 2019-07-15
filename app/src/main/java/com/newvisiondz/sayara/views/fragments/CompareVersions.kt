package com.newvisiondz.sayara.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.databinding.FragmentCompareVersionsBinding
import com.newvisiondz.sayara.databinding.FragmentCompareVersionsBindingImpl

class CompareVersions : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentCompareVersionsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_compare_versions, container, false)
        return binding.root
    }
}

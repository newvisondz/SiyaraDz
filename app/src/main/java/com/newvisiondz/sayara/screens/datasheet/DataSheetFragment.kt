package com.newvisiondz.sayara.screens.datasheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.databinding.FragmentDataSheetViewBinding
import com.newvisiondz.sayara.model.Version
import com.newvisiondz.sayara.screens.modeltabs.ModelTabsArgs
import com.newvisiondz.sayara.screens.versions.SpinnerAdapter
import okhttp3.internal.notify

class DataSheetFragment(args: Bundle?) : Fragment() {
    private val versions= mutableListOf<Version>()
    var manufacturerId = ""
    var modeId = ""
    private val argsRcv = args

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentDataSheetViewBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_data_sheet_view, container, false)
        val application = requireNotNull(this.activity).application
        val viewModel = ViewModelProviders.of(
            this, DataSheetViewModelFactory(
                application
            )
        ).get(DataSheetViewModel::class.java)
        binding.viewModel=viewModel
        binding.lifecycleOwner=this
        argsRcv?.let {
            modeId = ModelTabsArgs.fromBundle(it).modelId
            manufacturerId = ModelTabsArgs.fromBundle(it).manufacturerId
        }!!
        binding.spinnerVersionDataSheet.adapter=
            SpinnerAdapter(context!!, R.layout.spinner_element, versions)
        viewModel.getVersionList(manufacturerId,modeId)
        viewModel.versionsList.observe(this, Observer {list->
            versions.addAll(list)
            (binding.spinnerVersionDataSheet.adapter as SpinnerAdapter).notifyDataSetChanged()
        })

        binding.spinnerVersionDataSheet.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.getVersionDetail(manufacturerId, modeId, versions[position].id)
//                    versionId = versions[position].id
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
        return binding.root
    }
}

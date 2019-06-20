package com.newvisiondz.sayaradz.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.newvisiondz.sayaradz.R

class DataSheetView : androidx.fragment.app.Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //        val modelId = DataSheetViewArgs.fromBundle(arguments).modelId
//        Log.i("Navigation Success","From DataSheetView, ModelId: $modelId")
        return inflater.inflate(R.layout.fragment_data_sheet_view, container, false)
    }
}

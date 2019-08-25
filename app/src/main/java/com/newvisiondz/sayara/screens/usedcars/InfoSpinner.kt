package com.newvisiondz.sayara.screens.usedcars

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.model.CarInfo
import kotlinx.android.synthetic.main.spinner_element.view.*

class InfoSpinner (context: Context, resource: Int, var list: MutableList<CarInfo>) :
    ArrayAdapter<CarInfo>(context, resource, list) {

    override fun getCount(): Int {
        return list.size
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        return initView(position, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, parent)
    }

    private fun initView(position: Int, parent: ViewGroup): View {
        val customView = LayoutInflater.from(parent.context).inflate(R.layout.spinner_element, parent, false)
        val versionName = customView.spinner_item
        versionName.text = list[position].name
        return customView
    }
}
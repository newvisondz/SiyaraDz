package com.newvisiondz.sayara.screens.usedcars

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.utils.colorMap
import kotlinx.android.synthetic.main.color_spinner_item.view.*

class ColorSpinner(context: Context, resource: Int, var list: List<Int>) :
    ArrayAdapter<Int>(context, resource, list) {

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
        val customView =
            LayoutInflater.from(parent.context).inflate(R.layout.color_spinner_item, parent, false)
        val colorName = customView.color_name
        val colorItem = colorMap[position]
        colorName.text = colorItem?.name
        customView.color_preview.setBackgroundColor(Integer.parseInt(colorItem?.value!!))
        return customView
    }
}
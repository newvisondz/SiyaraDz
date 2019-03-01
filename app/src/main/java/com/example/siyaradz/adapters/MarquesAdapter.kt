package com.example.siyaradz.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.siyaradz.R
import com.example.siyaradz.model.Marque
import kotlinx.android.synthetic.main.brand_marks_items.view.*

class MarquesAdapter(private val marques: List<Marque>, private val context: Context) :
    RecyclerView.Adapter<MarquesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view =LayoutInflater.from(this.context).inflate(R.layout.brand_marks_items,viewGroup,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
         var marque= this.marques[i]
        viewHolder.brandName.text=marque.marque
    }

    override fun getItemCount(): Int {
        return marques.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val brandName = itemView.brand_name
    }
}

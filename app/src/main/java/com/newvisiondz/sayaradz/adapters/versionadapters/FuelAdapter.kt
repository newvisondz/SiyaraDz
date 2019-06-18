package com.newvisiondz.sayaradz.adapters.versionadapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.newvisiondz.sayaradz.R
import com.newvisiondz.sayaradz.model.Value
import kotlinx.android.synthetic.main.radio_button_item_styled.view.*

class FuelAdapter(private var options: MutableList<Value>, private var context: Context) :
    RecyclerView.Adapter<FuelAdapter.ViewHolder>() {


    companion object {
        private var sClickListener: SingleClickListener? = null
        private var sSelected = -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(this.context).inflate(R.layout.radio_button_item_styled, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = options.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val option = options[position]
        holder.radio.text = option.value
        holder.radio.isChecked = (sSelected == position)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val radio: RadioButton = itemView.radio_styled

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            sSelected = adapterPosition
            sClickListener?.onFuelClickListner(adapterPosition, view!!)
        }
    }


    fun selectedItem() {
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(clickListener: SingleClickListener) {
        sClickListener = clickListener
    }

    interface SingleClickListener {
        fun onFuelClickListner(position: Int, view: View)
    }
}
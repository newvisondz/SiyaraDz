package com.example.siyaradz.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import com.example.siyaradz.R
import com.example.siyaradz.model.Marque
import kotlinx.android.synthetic.main.brand_marks_items.view.*


class MarquesAdapter(private var marques: MutableList<Marque>, private val context: Context) :
    RecyclerView.Adapter<MarquesAdapter.ViewHolder>(), Filterable {

    private var marquesFull: MutableList<Marque> = marques.toMutableList()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(this.context).inflate(R.layout.brand_marks_items, viewGroup, false)
        view.setOnClickListener {
            Toast.makeText(context,"working",Toast.LENGTH_SHORT).show()
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val marque = this.marques[i]
        viewHolder.brandName.text = marque.marque

    }

    override fun getItemCount(): Int {
        return marques.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val brandName = itemView.brand_name!!
    }

    fun addBrand(brands: List<Marque>) {
        for (marque in brands) {
            this.marques.add(marque)
            this.marquesFull.add(marque)
        }
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    marques = marquesFull
                } else {
                    val filteredList = ArrayList<Marque>()
                    for (row in marquesFull) {
                        if (row.marque!!.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    marques = filteredList
                }
                val filterResults = Filter.FilterResults()
                filterResults.values = marques
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: Filter.FilterResults) {
                marques = filterResults.values as ArrayList<Marque>
                notifyDataSetChanged()
            }
        }
    }

}

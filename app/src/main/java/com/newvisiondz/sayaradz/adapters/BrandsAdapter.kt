package com.newvisiondz.sayaradz.adapters

import android.content.Context
import android.support.design.card.MaterialCardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.Navigation.findNavController
import com.newvisiondz.sayaradz.R
import com.newvisiondz.sayaradz.model.Brand
import com.newvisiondz.sayaradz.views.fragments.TabsDirections
import kotlinx.android.synthetic.main.brand_marks_items.view.*


class BrandsAdapter(private var brands: MutableList<Brand>, private val context: Context) :
    RecyclerView.Adapter<BrandsAdapter.ViewHolder>(), Filterable {

    private var marquesFull: MutableList<Brand> = brands.toMutableList()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(this.context).inflate(R.layout.brand_marks_items, viewGroup, false)
        val viewHolder = ViewHolder(view)
        viewHolder.card.setOnClickListener {
            val brandId = 2
            val action = TabsDirections.actionTabsToModels()
            action.setBrandId(brandId)
            findNavController(view).navigate(action)
            Log.i("Navigating","Tabs (Brands) to Models. BrandId: $brandId")
        }
        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val marque = this.brands[i]
        viewHolder.brandName.text = marque.name

    }

    override fun getItemCount(): Int {
        return brands.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val brandName = itemView.brand_name!!
        val card = itemView.brand_card
        val brandImage =itemView.brand_image
        val brandManufacturer = itemView.brand_manufacturer
    }

    fun addBrand(brands: List<Brand>) {
        for (marque in brands) {
            this.brands.add(marque)
            this.marquesFull.add(marque)
        }
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    brands = marquesFull
                } else {
                    val filteredList = ArrayList<Brand>()
                    for (row in marquesFull) {
                        if (row.name!!.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    brands = filteredList
                }
                val filterResults = Filter.FilterResults()
                filterResults.values = brands
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: Filter.FilterResults) {
                brands = filterResults.values as ArrayList<Brand>
                notifyDataSetChanged()
            }
        }
    }

}

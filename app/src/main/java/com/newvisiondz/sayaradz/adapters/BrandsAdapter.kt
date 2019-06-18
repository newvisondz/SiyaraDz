package com.newvisiondz.sayaradz.adapters

import android.content.Context
import android.graphics.Bitmap
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.newvisiondz.sayaradz.R
import com.newvisiondz.sayaradz.model.Brand
import com.newvisiondz.sayaradz.views.fragments.TabsDirections
import kotlinx.android.synthetic.main.brand_marks_items.view.*


class BrandsAdapter(private var brands: MutableList<Brand>, private val context: Context) :
    RecyclerView.Adapter<BrandsAdapter.ViewHolder>(), Filterable {

    private var imageCache: LruCache<String, Bitmap>
    private var marquesFull: MutableList<Brand> = brands.toMutableList()

    init {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 8
        imageCache = LruCache(cacheSize)
    }
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(this.context).inflate(R.layout.brand_marks_items, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val marque = this.brands[i]
        viewHolder.brandName.text = marque.name

        Glide.with(context)
            .asBitmap()
            .load("http://sayaradz3-sayaradz3.b9ad.pro-us-east-1.openshiftapps.com/${marque.logo}")
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            .into(viewHolder.brandImage)
        viewHolder.card.setOnClickListener {
            it.findNavController().navigate(TabsDirections.actionTabsToModels(marque.id))
        }
    }

    override fun getItemCount(): Int {
        return brands.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val brandName = itemView.brand_name!!
        val card = itemView.brand_card!!
        val brandImage = itemView.brand_image!!
//        val brandManufacturer = itemView.brand_manufacturer
    }

    fun addBrands(brands: List<Brand>) {
        for (marque in brands) {
            this.brands.add(marque)
            this.marquesFull.add(marque)
        }
        notifyDataSetChanged()
    }

    fun clearBrands() {
        brands.clear()
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    brands = marquesFull
                } else {
                    val filteredList = ArrayList<Brand>()
                    for (row in marquesFull) {
                        if (row.name.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    brands = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = brands
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                brands = filterResults.values as ArrayList<Brand>
                notifyDataSetChanged()
            }
        }
    }
}

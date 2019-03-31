package com.newvisiondz.sayaradz.adapters

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.util.LruCache
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.navigation.Navigation.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.newvisiondz.sayaradz.R
import com.newvisiondz.sayaradz.model.Brand
import com.newvisiondz.sayaradz.services.RetrofitClient
import com.newvisiondz.sayaradz.views.fragments.TabsDirections
import kotlinx.android.synthetic.main.brand_marks_items.view.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response


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
        val viewHolder = ViewHolder(view)
        viewHolder.card.setOnClickListener {
            val brandId = 2
            val action = TabsDirections.actionTabsToModels()
            action.setBrandId(brandId)
            findNavController(view).navigate(action)
            Log.i("Navigating", "Tabs (Brands) to Models. BrandId: $brandId")
        }
        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val marque = this.brands[i]
        viewHolder.brandName.text = marque.name
        val bitmap = imageCache.get(marque.id)

        Glide.with(context)
            .asBitmap()
            .load("https://sayara-dz.herokuapp.com${marque.logo}")
            .transition(withCrossFade(DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(viewHolder.brandImage)

//        if (bitmap != null) {
//            viewHolder.brandImage.setImageBitmap(marque.image)
//        } else {
//            getImage(marque, viewHolder)
//
//        }

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
            override fun performFiltering(charSequence: CharSequence): Filter.FilterResults {
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

    private fun getImage(marque: Brand, viewHolder: ViewHolder) {
        val call = RetrofitClient()
            .serverDataApi
            .getBrandImage(marque.logo)

        call.enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    val bitmap = BitmapFactory.decodeStream(response.body()!!.byteStream())
                    marque.image = bitmap
                    imageCache.put(marque.id, bitmap)
                    viewHolder.brandImage.setImageBitmap(marque.image)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }


}

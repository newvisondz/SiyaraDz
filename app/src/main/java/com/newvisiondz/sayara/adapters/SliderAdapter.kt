package com.newvisiondz.sayara.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.newvisiondz.sayara.R
import com.smarteist.autoimageslider.SliderViewAdapter
import kotlinx.android.synthetic.main.slider_item_models.view.*

class SliderAdapter(private val context: Context, private var modelsImages: MutableList<String>) :
    SliderViewAdapter<SliderAdapter.SliderAdapterVH>() {
    override fun getCount(): Int = modelsImages.size
    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.slider_item_models, null)
        return SliderAdapterVH(inflate)
    }
    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {
        Glide.with(context)
            .asBitmap()
            .load("${context.getString(R.string.baseUrl)}/${modelsImages[position]}")
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(viewHolder.imageViewBackground)
    }


    class SliderAdapterVH(itemView: View) : SliderViewAdapter.ViewHolder(itemView) {
        var imageViewBackground: ImageView = itemView.iv_auto_image_slider
    }
}
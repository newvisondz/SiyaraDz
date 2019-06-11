package com.newvisiondz.sayaradz.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.newvisiondz.sayaradz.R
import com.newvisiondz.sayaradz.model.Model
import kotlinx.android.synthetic.main.fragment_model_card.view.*

class ModelsAdapter(
    private val models: MutableList<Model>,
    private val context: Context,
    private val manufacturerId: String
) :
    RecyclerView.Adapter<ModelsAdapter.ViewHolder>() {

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val model = this.models[i]
        viewHolder.modelName.text = model.name
        Glide.with(context)
            .asBitmap()
            .load("http://sayaradz-sayaradz-2.7e14.starter-us-west-2.openshiftapps.com${model.images[0]}")
            .transition(
                BitmapTransitionOptions.withCrossFade(
                    DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
                )
            )
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(viewHolder.modelImage)
//        viewHolder.modelAttribute.text = model.colors.name
//        viewHolder.modelPrice.text = model.price
//        viewHolder.modelImage.setImageResource(model.imageId)
        viewHolder.card.setOnClickListener {
            val args = Bundle()
            args.putString("modelId", model.id)
            args.putString("manufacturerId", manufacturerId)
            it.findNavController().navigate(R.id.action_models_to_modelView, args)
        }
    }

    override fun getItemCount(): Int {
        return models.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(this.context).inflate(R.layout.fragment_model_card, viewGroup, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card = view.model_card!!
        val modelName = view.model_name!!
        val modelImage = view.model_image!!
        val modelPrice = view.model_price!!
        val modelAttribute = view.model_attribute!!
    }
}
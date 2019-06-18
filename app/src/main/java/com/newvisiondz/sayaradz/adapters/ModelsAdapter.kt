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
import com.bumptech.glide.request.RequestOptions
import com.newvisiondz.sayaradz.R
import com.newvisiondz.sayaradz.model.Model
import com.newvisiondz.sayaradz.views.fragments.ModelsDirections
import kotlinx.android.synthetic.main.fragment_model_card.view.*
import java.util.*

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
            .load("http://sayaradz3-sayaradz3.b9ad.pro-us-east-1.openshiftapps.com/${model.images[0]}")
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(viewHolder.modelImage)
//        viewHolder.modelAttribute.text = model.colors.name
//        viewHolder.modelPrice.text = model.price
//        viewHolder.modelImage.setImageResource(model.imageId)
        viewHolder.card.setOnClickListener {
            val args=Bundle()
            args.putStringArrayList("modelImages", model.images as ArrayList<String>?)
            it.findNavController().navigate(ModelsDirections.actionModelsToModelView(model.id,manufacturerId))
        }
    }

    override fun getItemCount(): Int {
        return models.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.fragment_model_card, viewGroup, false)
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
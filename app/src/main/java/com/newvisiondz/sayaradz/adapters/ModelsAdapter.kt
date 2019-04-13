package com.newvisiondz.sayaradz.adapters

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.newvisiondz.sayaradz.R
import com.newvisiondz.sayaradz.model.Model
import com.newvisiondz.sayaradz.views.fragments.ModelsDirections
import kotlinx.android.synthetic.main.fragment_model_card.view.*

class ModelsAdapter(private val models: MutableList<Model>, private val context: Context) :
    RecyclerView.Adapter<ModelsAdapter.ViewHolder>() {
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        val model= this.models[i]
        viewHolder.modelName.text = model.name
//        viewHolder.modelAttribute.text = model.colors.name
//        viewHolder.modelPrice.text = model.price
//        viewHolder.modelImage.setImageResource(model.imageId)
        viewHolder.card.setOnClickListener {
            val args=Bundle()
            args.putString("modelId",model.id)
            it.findNavController().navigate(R.id.action_models_to_modelView,args)
        }
    }

    override fun getItemCount(): Int {
        return models.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(this.context).inflate(R.layout.fragment_model_card,viewGroup,false)
        return ViewHolder(view)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card = view.model_card!!
        val modelName = view.model_name!!
        val modelImage = view.model_image
        val modelPrice = view.model_price
        val modelAttribute = view.model_attribute
    }
}
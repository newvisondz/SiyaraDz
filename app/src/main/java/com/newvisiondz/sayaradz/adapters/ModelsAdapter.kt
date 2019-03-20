package com.newvisiondz.sayaradz.adapters

import android.content.Context
import android.support.design.card.MaterialCardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import com.newvisiondz.sayaradz.R
import com.newvisiondz.sayaradz.model.Model
import com.newvisiondz.sayaradz.views.fragments.ModelsDirections

class ModelsAdapter(private val models: MutableList<Model>, private val context: Context) :
    RecyclerView.Adapter<ModelsAdapter.ViewHolder>() {
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {

        val model= this.models[i]
        viewHolder.modelName.text = model.name
        viewHolder.modelAttribute.text = model.attribute
        viewHolder.modelPrice.text = model.price
        viewHolder.modelImage.setImageResource(model.imageId)

    }

    override fun getItemCount(): Int {
        return models.size
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(this.context).inflate(R.layout.fragment_model_card,viewGroup,false)
        val viewHolder = ViewHolder(view)
        viewHolder.card.setOnClickListener {
            val modelId = 1
            val action = ModelsDirections.actionModelsToModelView()
            action.setModelId(modelId)
            Navigation.findNavController(view).navigate(action)
            Log.i("Navigating","Models to ModelView, ModelId: $modelId")
        }
        return viewHolder
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card = view.findViewById<MaterialCardView>(R.id.model_card)
        val modelName = view.findViewById<TextView>(R.id.model_name)
        val modelImage = view.findViewById<ImageView>(R.id.model_image)
        val modelPrice = view.findViewById<TextView>(R.id.model_price)
        val modelAttribute = view.findViewById<TextView>(R.id.model_attribute)
    }
}
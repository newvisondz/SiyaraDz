package com.newvisiondz.sayara.utils

import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.model.Bid
import com.newvisiondz.sayara.model.Color
import com.newvisiondz.sayara.model.Value
import com.newvisiondz.sayara.model.VersionCompare
import com.newvisiondz.sayara.screens.bids.BidsAdapter


@BindingAdapter("loadImageUrl")
fun loadImageUrl(imgView: ImageView, bid: Bid?) {
    bid?.let {
        Glide.with(imgView.context)
            .load(bid.image)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(imgView)

    }
}
@BindingAdapter("loadLocalImage")
fun loadLocalImage(imgView: ImageView, bid: Bid?){
    bid?.let {
        if (bid.bitmap != null){
            imgView.setImageBitmap(bid.bitmap)
        }else if (bid.uris.isNotEmpty()) {
            imgView.setImageURI(bid.uris[0])
        }else {
            Glide.with(imgView.context)
                .load(bid.image)
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.loading_animation)
                        .error(R.drawable.ic_broken_image)
                )
                .into(imgView)
        }
    }
}

@BindingAdapter("setBidPrice")
fun TextView.setBidPrice(bid: Bid?) {
    bid?.let {
        text = this.context.getString(R.string.bid_price_text, bid.price.toString())
    }
}

@BindingAdapter("setBidBrand")
fun TextView.setBidBrand(bid: Bid?) {
    bid?.let {
        text = bid.carBrand
    }
}

@BindingAdapter("setEngineGear")
fun TextView.setEngineGear(bid: Bid?) {
    bid?.let {
        text = bid.gearBoxType
    }
}


@BindingAdapter("setBidAdresse")
fun TextView.setBidddresse(bid: Bid?) {
    bid?.let {
        text = bid.adresse
    }
}


@BindingAdapter("setRadioEngineChecked")
fun RadioButton.setRadioEngineChecked(item: Value?) {
    item?.let {
        text = item.value
    }
}

@BindingAdapter("setRadioPlaceChecked")
fun RadioButton.setRadioPlaceChecked(item: Value?) {
    item?.let {
        text = item.value
    }
}

@BindingAdapter("setRadioFuelChecked")
fun RadioButton.setRadioFuelChecked(item: Value?) {
    item?.let {
        text = item.value
    }
}

@BindingAdapter("setRadioColorChecked")
fun RadioButton.setRadioColorChecked(item: Color?) {
    item?.let {
        text = it.name
        background = ColorDrawable(android.graphics.Color.parseColor(it.value))
    }
}

@BindingAdapter("setRadioEnginePowerChecked")
fun RadioButton.setRadioEnginePowerChecked(item: Value?) {
    item?.let {
        text = it.value
    }
}

@BindingAdapter("bidsListData")
fun bidsListData(recyclerView: RecyclerView, data: List<Bid>?) {
    val adapter = recyclerView.adapter as BidsAdapter
    adapter.submitList(data)

}

@BindingAdapter("setCompareVersionText")
fun TextView.setCompareVersionText(item: VersionCompare?) {
    item?.let {
        when (this.id) {
            R.id.option_name -> {
                text = item.optionName
            }
            R.id.spinner_v1 -> {
                text = item.firstValue
            }
            R.id.spinner_v2 -> {
                text = item.secondValue
            }
        }
    }
}


@InverseBindingAdapter(attribute = "app:setDouble", event = "android:textAttrChanged")
fun captureTextValue(view: TextView): Double {
    return view.text.toString().toDouble()
}

@BindingAdapter("setDouble")
fun TextView.setDouble(value: Double?) {
    value?.let {
        text = value.toString()
    }
}

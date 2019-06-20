package com.newvisiondz.sayaradz.adapters

import android.graphics.drawable.ColorDrawable
import android.widget.RadioButton
import androidx.databinding.BindingAdapter
import com.newvisiondz.sayaradz.model.Color
import com.newvisiondz.sayaradz.model.Value
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
fun RadioButton.setRadioFuelChecked(item:Value?) {
    item?.let {
        text=item.value
    }
}
@BindingAdapter("setRadioColorChecked")
fun RadioButton.setRadioColorChecked(item:Color?) {
    item?.let {
        text=it.value
        background = ColorDrawable(android.graphics.Color.parseColor(it.value))
    }
}


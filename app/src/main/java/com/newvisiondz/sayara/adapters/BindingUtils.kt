package com.newvisiondz.sayara.adapters

import android.graphics.drawable.ColorDrawable
import android.widget.RadioButton
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.model.Color
import com.newvisiondz.sayara.model.Value
import com.newvisiondz.sayara.model.VersionCompare

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



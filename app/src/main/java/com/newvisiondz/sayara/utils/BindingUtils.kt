package com.newvisiondz.sayara.utils

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.newvisiondz.sayara.R
import com.newvisiondz.sayara.model.*
import com.newvisiondz.sayara.screens.myorders.MyOrdersAdapter
import com.newvisiondz.sayara.screens.usedcardetails.BidsAdapter
import com.newvisiondz.sayara.screens.usedcars.UsedCarsAdapter


@BindingAdapter("loadLocalImage")
fun loadLocalImage(imgView: ImageView, usedCar: UsedCar?) {
    usedCar?.let {
        try {
            when {
                usedCar.images.isNotEmpty() -> Glide.with(imgView.context)
                    .load("${imgView.context.getString(R.string.baseUrl)}${usedCar.images[0]}")
                    .apply(
                        RequestOptions()

                            .placeholder(R.drawable.loading_animation)
                            .error(R.drawable.ic_broken_image)
                    )
                    .into(imgView)

                (usedCar.uris.size > 0) -> imgView.setImageURI(usedCar.uris[0])
                else -> {
                    imgView.setImageResource(R.drawable.mercedes)
                }
            }
        } catch (e: Exception) {
            Toast.makeText(
                imgView.context,
                "Something went wrong loading image !",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}

@BindingAdapter("setBidPrice")
fun TextView.setBidPrice(usedCar: UsedCar?) {
    usedCar?.let {
        text = this.context.getString(R.string.bid_price_text, usedCar.price.toString())
    }
}

@BindingAdapter("setUsedCarManufacturer")
fun TextView.setUsedCarManufacturer(usedCar: UsedCar?) {
    usedCar?.let {
        text = context.getString(R.string.manufacturer, it.manufacturer)
    }
}

@BindingAdapter("setUsedCarModel")
fun TextView.setUsedCarModel(usedCar: UsedCar?) {
    usedCar?.let {
        text = context.getString(R.string.model, it.model)
    }
}

@BindingAdapter("setUsedCarVersion")
fun TextView.setUsedCarVersion(usedCar: UsedCar?) {
    usedCar?.let {
        text = context.getString(R.string.version, it.version)
    }
}

@BindingAdapter("setUsedCarDate")
fun TextView.setUsedCarDate(usedCar: UsedCar?) {
    usedCar?.let {
        text = try {
            context.getString(R.string.registration_date, it.yearOfRegistration.substring(0, 9))
        } catch (e: StringIndexOutOfBoundsException) {
            context.getString(R.string.registration_date, it.yearOfRegistration)
        }
    }
}

@BindingAdapter("setUsedCarMiles")
fun TextView.setUsedCarMiles(usedCar: UsedCar?) {
    usedCar?.let {
        text = context.getString(R.string.miles, it.currentMiles.toString())
    }
}

@BindingAdapter("setBackgroundColor")
fun View.setBackgroundColor(usedCar: UsedCar?) {
    usedCar?.let {
        try {
            setBackgroundColor(android.graphics.Color.parseColor(usedCar.color))
        } catch (e: java.lang.Exception) {

        }

    }
}

@BindingAdapter("setColor")
fun View.setColor(color: String?) {
    color?.let {
        try {
            setBackgroundColor(android.graphics.Color.parseColor(color))
        } catch (e: java.lang.Exception) {

        }

    }
}

@BindingAdapter("setBidDate")
fun TextView.setBidDate(usedCar: UsedCar?) {
    usedCar?.let {
        text = try {
            it.yearOfRegistration.substring(0, 9)
        } catch (e: StringIndexOutOfBoundsException) {
            it.yearOfRegistration
        }
    }
}

@BindingAdapter("setBidBrand")
fun TextView.setBidBrand(usedCar: UsedCar?) {
    usedCar?.let {
        text = usedCar.manufacturer
    }
}

@BindingAdapter("setBidModel")
fun TextView.setBidddresse(usedCar: UsedCar?) {
    usedCar?.let {
        text = usedCar.model
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
fun bidsListData(recyclerView: RecyclerView, data: List<UsedCar>?) {
    val adapter = recyclerView.adapter as UsedCarsAdapter
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
//            R.id.spinner_v2 -> {
//                text = item.secondValue
//            }
        }
    }
}


@InverseBindingAdapter(attribute = "app:setDouble", event = "android:textAttrChanged")
fun captureTextValue(view: TextView): Double {
    return try {
        view.text.toString().toDouble()
    } catch (e: java.lang.Exception) {
        0.0
    }
}

@BindingAdapter("setDouble")
fun TextView.setDouble(value: Double?) {
    value?.let {
        text = value.toString()
    }
}


@BindingAdapter("setBidCreator")
fun TextView.setBidCreator(bid: Bid?) {
    bid?.let {
        text =
            context.getString(R.string.firstLastName, bid.creator.lastName)
    }
}

@BindingAdapter("setBidCreatorPhone")
fun TextView.setBidCreatorPhone(bid: Bid?) {
    bid?.let {
        text = bid.creator.phone
    }
}

@BindingAdapter("setBidPrice")
fun TextView.setBidPrice(bid: Bid?) {
    bid?.let {
        text = bid.price.toString()
    }
}

@BindingAdapter("bidsListItems")
fun bidsListItems(recyclerView: RecyclerView, data: List<Bid>?) {
    val adapter = recyclerView.adapter as BidsAdapter
    adapter.submitList(data)
}

@BindingAdapter("setStringType")
fun TextView.setStringType(info: String?) {
    info.let {
        text = if (id == R.id.used_car_bid_price) {
            context.getString(R.string.bid_price_text, info)
        } else info
    }
}

@BindingAdapter("setUserName")
fun TextView.setUserName(user: User?) {
    user?.let {
        text = user.lastName
    }
}

@BindingAdapter("setUserPhone")
fun TextView.setUserPhone(user: User?) {
    user?.let {
        text = user.phone
    }
}

@BindingAdapter("setUserEmail")
fun TextView.setUserEmail(user: User?) {
    user?.let {
        text = it.email
    }
}

@BindingAdapter("setPayedBoolean")
fun TextView.setPayedBoolean(commandConfirmed: CommandConfirmed?) {
    commandConfirmed?.let {
        text = if (it.payed) {
            "Deja payer"
        } else {
            "Pas encore !"
        }
    }
}

@BindingAdapter("setAcceptedBoolean")
fun TextView.setAcceptedBoolean(commandConfirmed: CommandConfirmed?) {
    commandConfirmed?.let {
        text = if (it.accepted) {
            "Commande accpete"
        } else {
            "Pas encore !"
        }
    }
}

@BindingAdapter("setCreationDateBoolean")
fun TextView.setCreationDateBoolean(commandConfirmed: CommandConfirmed?) {
    commandConfirmed?.let {
        text = it.createdAt.toString().substring(0,16)
    }
}

@BindingAdapter("commandsListItems")
fun commandsListItems(recyclerView: RecyclerView, data: List<CommandConfirmed>?) {
    val adapter = recyclerView.adapter as MyOrdersAdapter
    adapter.submitList(data)
}
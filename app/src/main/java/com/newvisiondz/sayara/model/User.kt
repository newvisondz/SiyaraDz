package com.newvisiondz.sayara.model

data class User(
    var id: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var phone: String = "",
    var address: String = ""
)

data class UserBid(
    var id: String = "",
    var usedCar: UsedCar = UsedCar(),
    var price: String = ""
)
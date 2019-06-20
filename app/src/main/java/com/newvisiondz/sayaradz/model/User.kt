package com.newvisiondz.sayaradz.model

class User() {
    private var lastName:String?=null
    private var firstName:String?=null
    var email:String?=null
    private var address:String?=null
    private var phone:String?=null


    constructor(_lastName: String, _firstName:String,_address:String, _telNum:String) :this(){
        this.firstName=_firstName
        this.lastName=_lastName
        this.address=_address
        this.phone=_telNum
    }

//TODO ajouter la date cote back--end
}

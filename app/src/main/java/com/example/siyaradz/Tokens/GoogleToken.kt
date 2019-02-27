package com.example.siyaradz.Tokens

class GoogleToken(){
    private var email:String?=null
    private var id:String?=null
    private var token:String?=null

    constructor(email:String,id:String,token:String):this(){
        this.email=email
        this.id=id
        this.token=token
    }

    override fun toString(): String {
        return this.email!!
    }
}
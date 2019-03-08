package com.example.siyaradz.Tokens

class GoogleToken(private var _email: String, private var _id: String, private var _token: String) {
    var email: String = _email
    var id: String = _id
    var token: String = _token


    override fun toString(): String {
        return this.email
    }
}
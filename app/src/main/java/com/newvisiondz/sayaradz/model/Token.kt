package com.newvisiondz.sayaradz.model

class Token(_email: String, _id: String, _token: String) {
    var email: String = _email
    var id: String = _id
    var token: String = _token

    override fun toString(): String {
        return this.email
    }
}
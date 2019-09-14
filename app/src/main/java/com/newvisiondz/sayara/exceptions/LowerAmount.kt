package com.newvisiondz.sayara.exceptions

class LowerAmount: Exception() {
    override val message: String?
        get() = "Ce montant est inférieur que celui imposer par le client"
}
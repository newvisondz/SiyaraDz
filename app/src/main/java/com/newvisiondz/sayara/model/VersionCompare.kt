package com.newvisiondz.sayara.model

class VersionCompare {
    var optionName: String = ""
    var firstValue: String = ""
    var secondValue: String = ""
    override fun equals(other: Any?): Boolean {
        return (this.optionName == (other as VersionCompare).optionName) && (this.firstValue == other.firstValue) && (this.secondValue == other.secondValue)
    }

    override fun hashCode(): Int {
        return this.optionName.hashCode() + this.firstValue.hashCode() + this.secondValue.hashCode()
    }
}
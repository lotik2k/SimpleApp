package com.movista.app.utils

object LocationUtils {

    fun getCityByAddress(address: String): String {
        val index = address.indexOf(",")
        return if (index >= 0) address.substring(0, index) else address
    }
}
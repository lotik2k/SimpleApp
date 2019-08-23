package com.movista.app.utils

import android.annotation.SuppressLint
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.*

class JsonDateDeserializer : JsonDeserializer<Date> {

    @SuppressLint("SimpleDateFormat")
    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Date {
        val str = json.asJsonPrimitive.asString
        val format = SimpleDateFormat("yyyy-MM-dd hh:mm")
        return format.parse(str)
    }
}

package com.hse.education.domain.entity

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.objectbox.annotation.Convert
import io.objectbox.annotation.Entity
import io.objectbox.annotation.Id
import io.objectbox.converter.PropertyConverter

@Entity
data class User(
    @Id var id: Long = 0,
    var globalId: Long = 0,
    var email: String = "",
    var password: String = "",
    var userName: String = "",
    @Convert(converter = InterestConverter::class, dbType = String::class)
    var interest: MutableSet<Int> = mutableSetOf(),
    var link: String = ""
)

// Конвертер для хранения Set<Int> как JSON в String
class InterestConverter : PropertyConverter<MutableSet<Int>, String> {
    private val gson = Gson()

    override fun convertToDatabaseValue(entityProperty: MutableSet<Int>?): String {
        return gson.toJson(entityProperty ?: emptySet<Int>())
    }

    override fun convertToEntityProperty(databaseValue: String?): MutableSet<Int> {
        val type = object : TypeToken<MutableSet<Int>>() {}.type
        return gson.fromJson(databaseValue ?: "[]", type) ?: mutableSetOf()
    }
}
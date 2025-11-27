package com.example.nanithappybirthday.model

enum class AgeUnit {
    MONTH,
    YEAR
}

data class Age(val value: Int, val unit: AgeUnit) {
    fun getFormattedUnit(): String {
        return unit.name + (if (value != 1) "S" else "")
    }
}
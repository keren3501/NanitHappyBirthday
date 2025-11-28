package com.example.nanithappybirthday.model

import android.content.Context
import com.example.nanithappybirthday.R

enum class AgeUnit {
    MONTH,
    YEAR
}

data class Age(val value: Int, val unit: AgeUnit) {
    fun getFormattedUnit(context: Context): String {
        return unit.name + (if (value != 1) context.getString(R.string.plural_suffix) else "")
    }
}
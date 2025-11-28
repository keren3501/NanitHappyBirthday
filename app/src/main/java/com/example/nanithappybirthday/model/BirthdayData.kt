package com.example.nanithappybirthday.model

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

data class BirthdayData(val name: String, val dob: Long, val theme: String) {

    private fun getAgeAsMonths(): Long {
        return ChronoUnit.MONTHS.between(
            Instant.ofEpochMilli(dob).atZone(ZoneId.systemDefault()).toLocalDate(),
            LocalDate.now()
        )
    }

    fun getFormattedAge(): Age {
        val ageAsMonths = getAgeAsMonths()

        return if (ageAsMonths < 12) {
            Age(ageAsMonths.toInt(), AgeUnit.MONTH)
        } else {
            Age((ageAsMonths / 12).toInt(), AgeUnit.YEAR)
        }
    }

}
package com.example.nanithappybirthday.model

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit

data class BirthdayData(val name: String, val dob: Long, val theme: String) {

    fun getAgeAsMonths(): Long {
        return ChronoUnit.MONTHS.between(
            Instant.ofEpochSecond(dob).atZone(ZoneId.systemDefault()).toLocalDate(),
            LocalDate.now()
        )
    }

}
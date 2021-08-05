package com.example.randomuser.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@JsonClass(generateAdapter = true)
data class Result(
    val results: List<User>
)

@Entity(tableName = "user_database")
@JsonClass(generateAdapter = true)
data class User(
    // Email is not the usual choice for a primary key but the ID object has a tendency to be null,
    // and email would be unique.
    // uuid could be used, but this is nested in the login data. The login data is private
    // information and it doesn't seem critical in this particular case.
    @PrimaryKey
    val email: String,
    @Embedded val name: Name,
    @Embedded val location: Location,
    @Embedded val picture: Picture,
    @Embedded val dob: DOB,
    val phone: String,
    val cell: String,
    @Json(name = "nat") val nationality: String
) {
    fun getFullName(): String {
        return "${name.first} ${name.last}"
    }

    fun getStreetAddress(): String {
        return "${location.street.number} ${location.street.name}"
    }

    fun getCityStatePost(): String {
        return "${location.city}, ${location.state}, ${location.postCode}"
    }

    fun getBirthday(): String {
        val date = getBirthdayDate()
        // Month returns in all caps. Only the first letter should be capitalized
        val month = date.month.toString().lowercase().replaceFirstChar {
            it.uppercase()
        }
        return "$month ${date.dayOfMonth}, ${date.year}"
    }

    fun getUpcomingBirthday(): Long {
        val birthday = getBirthdayDate()
        val currentDate = LocalDateTime.now()
        val calendar = Calendar.getInstance()
        // get current date in millis
        val currentDateMillis = calendar.timeInMillis
        // get potential next birthday time
        calendar.set(currentDate.year, birthday.monthValue, birthday.dayOfMonth)
        val currentYearBirthdayMillis = calendar.timeInMillis

        return if (currentDateMillis > currentDateMillis){
            calendar.set(currentDate.year + 1, birthday.monthValue, birthday.dayOfMonth)
            calendar.timeInMillis
        } else {
            currentYearBirthdayMillis
        }
    }

    private fun getBirthdayDate(): LocalDate {
        val format = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        return LocalDate.parse(dob.date, format)
    }
}

@JsonClass(generateAdapter = true)
data class Name(
    val first: String,
    val last: String
)

@JsonClass(generateAdapter = true)
data class Location(
    @Embedded
    val street: Street,
    val city: String,
    val state: String,
    val country: String,
    @Json(name = "postcode") @ColumnInfo(name="post_code") val postCode: String,
    @Embedded val coordinates: Coordinates
)

@JsonClass(generateAdapter = true)
data class Street(
    val number: Int,
    val name: String,
)

@JsonClass(generateAdapter = true)
data class Coordinates(
    val latitude: String,
    val longitude: String
)

@JsonClass(generateAdapter = true)
data class Picture(
    val thumbnail: String,
    val medium: String,
    val large: String
)

@JsonClass(generateAdapter = true)
data class DOB(
    val date: String,
    val age: Int
)

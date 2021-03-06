package com.example.randomuser.model

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson

/**
 * This is an adapter that extracts the list contained in the 'results' field from the api's JSON
 * response.
 */
@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
internal annotation class Wrapped
class ResultAdapter {

    @Wrapped
    @FromJson
    fun fromJson(result: Result): List<User> {
        return result.results
    }

    @ToJson
    fun toJson(@Wrapped users: List<User>): Result {
        throw UnsupportedOperationException("ResultAdapter only reads from JSON")
    }
}

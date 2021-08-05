package com.example.randomuser

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.randomuser.model.ResultAdapter
import com.example.randomuser.network.UserApiService
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Tests for the api call
 */
@ExperimentalCoroutinesApi
class ApiServiceUnitTests {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: UserApiService
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create(
                Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .add(ResultAdapter())
                    .build()
            ))
            .build()
            .create(UserApiService::class.java)
    }

    @Test
    fun `data when getUsers is successful, size is 2`() {
        enqueue("users.json", 200)
        runBlocking {
            val response = service.getUsers(2).body()

            assertNotNull(response)
            assertEquals(2, response?.size)
        }
    }

    @Test
    fun `data when api is missing required field, throws exception`() {
        enqueue("users_malformed.json", 200)
        runBlocking {
            try {
                service.getUsers(2).body()
                fail("Should throw com.squareup.moshi.JsonDataException")
            } catch (e: Exception) {
                assertTrue(e is JsonDataException)
            }

        }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    /**
     * Generate a mocked response from a JSON file.
     * The JSON could be passed directly as a string but the return from the api is quite long and
     * difficult to format as a string.
     */
    private fun enqueue(file: String, responseCode: Int) {
        val inStream = javaClass.classLoader!!.getResourceAsStream(file)
        val source = inStream.source().buffer()
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(responseCode)
                .setBody(source.readString(Charsets.UTF_8))
        )
    }
}
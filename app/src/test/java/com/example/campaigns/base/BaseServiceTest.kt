package com.example.campaigns.base

import com.example.campaigns.util.Util
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.After
import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

abstract class BaseServiceTest<Service : Any>(private val clazz: Class<Service>) {

    lateinit var service: Service
    lateinit var mockWebServer: MockWebServer

//    @OptIn(ExperimentalCoroutinesApi::class)
//    @get:Rule
//    var coroutinesTestRule = MainCoroutineRule()

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(mockWebServer.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(clazz)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    protected fun enqueueResponse(
        fileName: String,
        headers: Map<String, String> = emptyMap(),
        socketPolicy: SocketPolicy = SocketPolicy.KEEP_OPEN
    ) {
        val mockResponse = MockResponse().apply {
            this.socketPolicy = socketPolicy
            for ((key, value) in headers) {
                addHeader(key, value)
            }
        }

        mockWebServer.enqueue(
            mockResponse
                .setBody(Util.getJsonStringFromFile(fileName))
        )
    }
} 
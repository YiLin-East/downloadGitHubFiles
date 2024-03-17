package com.me.downloadfiles.retrofit2
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Streaming

interface ApiService {
    @Headers(
        "Accept: text/event-stream; application/json",
        "Accept-Charset: UTF-8",
        "Cache-Control: no-cache",
        "Connection: keep-alive",
    )
    @Streaming
    @POST("v1/chat/completions")
    fun flowChat(
    ): Call<ResponseBody>


    fun configure()
    fun files()
}

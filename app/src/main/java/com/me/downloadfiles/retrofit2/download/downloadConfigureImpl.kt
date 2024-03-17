package com.me.downloadfiles.retrofit2.download


import com.google.gson.GsonBuilder
import com.me.downloadfiles.retrofit2.AddressData
import com.me.downloadfiles.retrofit2.ApiManager
import com.me.downloadfiles.retrofit2.JsonUnit
import com.me.downloadfiles.retrofit2.PubState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Address
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.io.Reader

suspend fun downloadConfigureImpl(): Flow<Boolean> = callbackFlow {
    withContext(Dispatchers.IO) {
        val call = ApiManager.file.downConfigure()
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                launch {
                    withContext(Dispatchers.IO) {
                        if (response.isSuccessful) {
                            response.body()?.use { body ->
                                var reading: Reader? = null
                                try {
                                    reading = body.charStream()
                                    val gsonObj = GsonBuilder().create()
                                        .fromJson(reading, ConfigureData::class.java)
                                    JsonUnit().saveJsonToNative(
                                        File(
                                            AddressData.backgroundFileAddress,
                                            PubState.chatBackgroundConfigureJsonName
                                        ), gsonObj
                                    )
                                    send(true)
                                } catch (e: Exception) {
                                    close(null)
                                } finally {
                                    try {
                                        reading?.close()
                                    } catch (e: Exception) {
                                        //
                                    }
                                }
                            }
                        } else send(false)
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                close(null)
            }
        })
    }
}
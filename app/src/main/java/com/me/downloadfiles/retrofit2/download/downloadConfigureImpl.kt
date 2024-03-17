package com.me.downloadfiles.retrofit2.download


import android.util.Log
import com.google.gson.GsonBuilder
import com.me.downloadfiles.retrofit2.AddressData
import com.me.downloadfiles.retrofit2.ApiManager
import com.me.downloadfiles.retrofit2.JsonUnit
import com.me.downloadfiles.retrofit2.PubState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
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
    val TAG = "text"
        val call = ApiManager.file.downConfigure()
    Log.e(TAG, "downloadConfigureImpl: ", )
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                launch {
                    withContext(Dispatchers.IO) {
                        if (response.isSuccessful) {
                            Log.e(TAG, "onResponse:isSuccessful", )
                            response.body()?.use { body ->
                                var reading: Reader? = null
                                try {
                                    reading = body.charStream()
                                    val gsonObj = GsonBuilder().create().fromJson(reading, ConfigureData::class.java)
                                    val saveResult = JsonUnit().saveJsonObjectToFile(
                                       saveFile =  File(
                                            AddressData.backgroundFileAddress,
                                            PubState.chatBackgroundConfigureJsonName
                                        ),

                                        jsonObject = gsonObj
                                    )

                                    Log.e("TAG", "onResponse: gsonObj->$gsonObj", )
                                    if (saveResult){
                                        send(true)
                                    } else send(false)

                                } catch (e: Exception) {
                                    Log.e(TAG, "onResponse: ",e )
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

        awaitClose {
            call.cancel()
        }

}
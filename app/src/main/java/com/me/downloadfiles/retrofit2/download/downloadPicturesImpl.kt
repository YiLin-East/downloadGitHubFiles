package com.me.downloadfiles.retrofit2.download


import android.util.Log
import com.me.downloadfiles.retrofit2.ApiManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

/**
 * 图片批量下载
 */
@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
suspend fun downloadPicturesImpl(fileNames: List<String>, basePath: File): Flow<String> {
    val TAG = "test"
    return withContext(Dispatchers.IO) {
        if (fileNames.isEmpty()) {
            emptyFlow()
        } else {
            val flatMapMergeSize = fileNames.size.coerceAtMost(3).coerceAtLeast(1)
            val fileNameFlow = flow<String> {
                fileNames.forEach {
                    Log.e(TAG, "downloadPicturesImpl:图片名称-》$it", )
                    emit(it)
                }
            }

            fileNameFlow.flatMapMerge(flatMapMergeSize) {
                flow {
                    val call = ApiManager.file.downPicture(it)
                    call.enqueue(object : Callback<ResponseBody> {
                        override fun onResponse(
                            call: Call<ResponseBody>,
                            response: Response<ResponseBody>
                        ) {
                            if (response.isSuccessful) {
                                Log.e(TAG, "onResponse:downloadPicturesImpl isSuccessful", )
                                launch {
                                    withContext(Dispatchers.IO) {
                                        val finally = File(basePath, it)
                                        if (!finally.exists()) finally.exists()
                                        try {
                                            finally.outputStream().use { outputStream ->
                                                response.body()?.byteStream()?.copyTo(outputStream)
                                            }
                                            Log.e("TAG", "onResponse: 图片文件下载-》$it", )
                                            emit(it)
                                        } catch (e: Exception) {
                                            emit("null")
                                        }
                                    }
                                }
                            }
                        }

                        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                            launch {
                                emit("null")
                            }
                        }
                    })
                }
            }
        }
    }
}
package com.me.downloadfiles.retrofit2.download


import com.me.downloadfiles.retrofit2.ApiManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
@OptIn(ExperimentalCoroutinesApi::class)
suspend fun downloadPicturesImpl(fileNames: List<String>, basePath: File): Flow<String> {
    return withContext(Dispatchers.IO) {
        if (fileNames.isEmpty()) {
            emptyFlow()
        } else {
            val flatMapMergeSize = fileNames.size.coerceAtMost(3).coerceAtLeast(1)
            val fileNameFlow = flow<String> {
                fileNames.forEach {
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
                                launch {
                                    withContext(Dispatchers.IO) {
                                        val finally = File(basePath, it)
                                        try {
                                            finally.outputStream().use { outputStream ->
                                                response.body()?.byteStream()?.copyTo(outputStream)
                                            }
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
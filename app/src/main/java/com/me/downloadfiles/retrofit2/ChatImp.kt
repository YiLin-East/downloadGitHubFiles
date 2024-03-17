//package com.me.downloadfiles.retrofit2
//
//import android.util.Log
//import com.google.gson.GsonBuilder
//import com.google.gson.JsonSyntaxException
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.channels.awaitClose
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.callbackFlow
//import kotlinx.coroutines.launch
//import okhttp3.MediaType.Companion.toMediaType
//import okhttp3.RequestBody.Companion.toRequestBody
//import okhttp3.ResponseBody
//import okio.IOException
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import java.net.SocketTimeoutException
//import java.net.UnknownHostException
//import java.util.concurrent.TimeoutException
//
//private const val STREAM_PREFIX = "data:"
//private const val STREAM_END_TOKEN = "$STREAM_PREFIX [DONE]"
//private const val TAG = "chatImp_log"
//
///**
// * 聊天接口具体实现
// *
// * @param chatCompletion 聊天上下文
// * @return 回复内容flow
// */
//suspend fun chatImpl(
//    chatCompletion: ChatCompletionRequest,
//): Flow<ChatCompletionChunk?> = callbackFlow {
//    val encodeBody = enCodeChatMsg(chatCompletion)
//    val requestBody = encodeBody.toRequestBody("application/json; charset=utf-8".toMediaType())
//    val gson = GsonBuilder().create()
//    val call = ApiManager.start.flowChat(ApiManager.apiToken(), requestBody = requestBody)
//    call.enqueue(object : Callback<ResponseBody> {
//        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//            if (response.isSuccessful) {
//                val nowKey = response.headers()["B2"]?.toLongOrNull()
//                nowKey?.let { FireBaseReportUtil.sendNumberReport(it,"now_chat") }
//                response.body()?.let { responseBody ->
//                    launch(Dispatchers.IO) { // 使用 `launch` 启动新的协程来处理流
//                        responseBody.byteStream().use { inputStream ->
//                            inputStream.bufferedReader().use { reader ->
//                                for (line in reader.lineSequence()) {
////                                    Log.e(TAG, "onResponse:line $line", )
//                                    if (line.startsWith(STREAM_END_TOKEN)) {
//                                        break
//                                    }
//
//                                    // 确认每一行的头部字符为data
//                                    if (line.startsWith(STREAM_PREFIX)) {
//                                        val json = line.removePrefix(STREAM_PREFIX).trim()
//                                        try {
//                                            if (json.startsWith("{")) {
//                                                val chunk = gson.fromJson(json, ChatCompletionChunk::class.java)
////                                                Log.e(TAG, "chunk:$chunk ", )
//                                                send(chunk)
//                                            } else {
//                                                // JSON 不是一个对象，可能是结束标记，忽略这一行
////                                                Log.e(TAG, "Skipping non-object JSON line: $line")
//                                            }
//                                        } catch (e: Exception) {
//                                            if (e is JsonSyntaxException) {
//                                                Log.e(TAG, "Error JsonSyntaxException SSE line: $line", e)
//                                                FireBaseReportUtil.catchException(CatchExceptionType.JSON_EXCEPTION,"chatImp_json_analysis")
//                                            }
//                                        }
//                                    } else {
//                                        if (line.contains("insufficient_quota")) close(ChatMessageErrorException("insufficient_quota"))
//                                        if (line.contains("account_deactivated")) close(ChatMessageErrorException("account_deactivated"))
//                                        if (line.contains("context_length_exceeded")) close(TokenOverFlow("token_overFlow"))
//                                    }
//                                }
//                                close() // 完成读取，关闭流
//                            }
//                        }
//                    }
//                }
//            } else {
////                Log.e(TAG, "Failed Response: ${response.code()}")
//                close(Exception("Server error: ${response.code()}"))
//            }
//        }
//
//        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//            when (t) {
//                is SocketTimeoutException -> {
////                    Log.e(TAG, "Socket Timeout Occurred", t)
//                    close(TimeoutException("Socket timeout occurred"))
//                }
//
//                is UnknownHostException ->{
//                    close(UnknownHostException("Unable to resolve host"))
//                }
//
//                is IOException -> {
//                    if (t.message?.contains("timeout", ignoreCase = true) == true) {
////                        Log.e(TAG, "I/O Operation Timeout", t)
//                        close(TimeoutException("I/O operation timeout occurred. Details: ${t.message}"))
//                    } else {
//                        // 其他 I/O 异常，可以是网络中断、失败的 DNS 查找等
////                        Log.e(TAG, "General I/O Exception", t)
//                        close(IOException("Network error occurred. Details: ${t.message}"))
//                    }
//                }
//                else -> {
//                    // 非 I/O 异常，可能是其它运行时异常
////                    Log.e(TAG, "Non-I/O Exception Occurred", t)
//                    close(Exception(t))
//                }
//            }
//        }
//    })
//
//    awaitClose { call.cancel() } // 等待流收集被取消，如果被取消就取消网络请求
//}
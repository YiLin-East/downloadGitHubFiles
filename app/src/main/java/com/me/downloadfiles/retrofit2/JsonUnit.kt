package com.me.downloadfiles.retrofit2

import android.util.Log
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

class JsonUnit {

    /**
     * 读取本地文件夹中json
     *
     * @param fileName 要读取的文件的 File 对象
     * @return Result 包装的字符串，读取成功时包含文件内容，失败时包含异常
     */
    fun readJsonFromFile(fileName: File): Result<String> {
        return try {
            // 如果文件不存在或无法读取，则抛出 IOException
            if (!fileName.exists() || !fileName.canRead()) {
                Log.e("readJsonFromFile", "readJsonFromFile: not exist not read", )
                throw IOException("File does not exist or cannot be read")
            }
            val content = fileName.readText(Charsets.UTF_8)
            Result.success(content)
        } catch (e: IOException) {
            Log.e("readJsonFromFile", "readJsonFromFile: ${e.printStackTrace()}", )
            Result.failure(e)
        } catch (e: SecurityException){
            Log.e("readJsonFromFile", "readJsonFromFile: ${e.printStackTrace()}", )
            Result.failure(e)
        }
    }
    suspend fun saveJsonToNative(saveFile: File, jsonObject: Any): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val jsonData = GsonBuilder().create().toJson(jsonObject)
                saveFile.writeText(jsonData)
                Result.success(true)
            } catch (e: Exception) {
                Result.failure<Boolean>(e)
            }
        }
    }
}
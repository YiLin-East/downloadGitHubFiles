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
            Log.e("readJsonFromFile", "readJsonFromFile: fileName->$fileName", )
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
                Log.e("saveJsonToNative", "onResponse:file name->$saveFile", )
                if (!saveFile.exists()) saveFile.createNewFile()
                val jsonData = GsonBuilder().create().toJson(jsonObject)
                saveFile.writeText(jsonData)
                Result.success(true)
            } catch (e: Exception) {
                Log.e("saveJsonToNative", "saveJsonToNative: ",e )
                Result.failure<Boolean>(e)
            }
        }
    }


    suspend fun saveJsonObjectToFile(jsonObject: Any, saveFile: File): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                // Ensure that parent directory exists
                if (!saveFile.parentFile.exists()) {
                    saveFile.parentFile.mkdirs()
                }

                // Create new file
                if (!saveFile.exists()) {
                    saveFile.createNewFile()
                }

                // Convert jsonObject to JSON string
                val jsonString = GsonBuilder().create().toJson(jsonObject)

                // Write JSON string to file
                saveFile.writeText(jsonString)

                true // Indicates successful write
            } catch (e: Exception) {
                e.printStackTrace()
                false // Indicates failed write
            }
        }
    }

}
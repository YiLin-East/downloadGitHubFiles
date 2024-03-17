package com.me.downloadfiles.retrofit2.download


import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.me.downloadfiles.retrofit2.AddressData
import com.me.downloadfiles.retrofit2.JsonUnit
import com.me.downloadfiles.retrofit2.PubState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * 解析 聊天背景配置文件
 *  [com.xphotokit.chatgptassist.publictag.PubState.chatBackgroundConfigureJsonName]
 *
 *  json配置文件与背景图 都在同一个目录下
 */
object ParseConfigureJson{
    private val jsonFile by lazy { File(AddressData.backgroundFileAddress, PubState.chatBackgroundConfigureJsonName) }
    private var configureData : ConfigureData? = null

    suspend fun getFileByType(type: Int): File?{
        return if (configureData == null){
            val parseResult = parse(jsonFile)
            if (!parseResult) return null
            fileName(type)
        } else {
            fileName(type)
        }
    }

    private fun fileName(type: Int): File?{
        val name = configureData?.data?.find { it.type == type }?.fileName ?: return null
        return File(AddressData.backgroundFileAddress,name)
    }


    suspend fun parse(json: File = jsonFile) = withContext(Dispatchers.IO){
        val readJson = JsonUnit().readJsonFromFile(json)
        return@withContext if (readJson.isSuccess){
            try {
                configureData = GsonBuilder().create().fromJson<ConfigureData>(readJson.getOrNull(),ConfigureData::class.java)
                true
            } catch (e:Exception){
                false
            }
        } else {
            false
        }
    }

    fun getAllFilename(): List<String>{
        return  configureData?.data?.mapNotNull { it.fileName } ?: emptyList()
    }


}

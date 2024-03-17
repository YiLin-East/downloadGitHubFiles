package com.me.downloadfiles.retrofit2.download


import android.util.Log
import com.me.downloadfiles.retrofit2.AddressData
import com.me.downloadfiles.retrofit2.PubState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext
import java.io.File

/**
 * 下载配置文件 与 背景tup
 */
class DownloadBackgroundResourceFiles {
    private val TAG by lazy { this.javaClass.simpleName }

    private suspend fun checkDown(): Boolean{
        return if (File(AddressData.backgroundFileAddress, PubState.chatBackgroundConfigureJsonName).exists()){
            return false
        } else true
    }

    suspend fun process() {
        withContext(Dispatchers.IO){
            if (!checkDown()) return@withContext
            downloadConfigureImpl().collect{
                if (it){
                    Log.e(TAG, "process:downloadConfigureImpl result ->$it ", )
                    parse()
                    downloadFiles(ParseConfigureJson.getAllFilename())
                }
            }
        }
    }


    private suspend fun parse(){
        ParseConfigureJson.parse()
    }

    private suspend fun downloadFiles(fileNames: List<String>){
        downloadPicturesImpl(fileNames, AddressData.backgroundFileAddress).collect{
            Log.e(TAG, "downloadFiles: $it", )
        }
    }



}
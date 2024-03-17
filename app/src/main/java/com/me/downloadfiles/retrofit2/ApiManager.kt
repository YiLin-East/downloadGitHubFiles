package com.me.downloadfiles.retrofit2

object ApiManager {

    val file: FileService by lazy { HttpManager.create(FileService::class.java) }

}
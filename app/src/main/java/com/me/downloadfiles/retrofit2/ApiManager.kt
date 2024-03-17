package com.me.downloadfiles.retrofit2

object ApiManager {

    val start: ApiService by lazy { HttpManager.create(ApiService::class.java) }
    val file: FileService by lazy { HttpManager.create(FileService::class.java) }

}
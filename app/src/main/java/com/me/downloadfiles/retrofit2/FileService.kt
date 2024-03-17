package com.me.downloadfiles.retrofit2

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface FileService {

    @GET("fdsfsdfs")
    fun downConfigure(

    ): Call<ResponseBody>

    @GET("abc/{file_name}")
    fun downPicture(
        @Path("file_name") fileName: String
    ): Call<ResponseBody>

}
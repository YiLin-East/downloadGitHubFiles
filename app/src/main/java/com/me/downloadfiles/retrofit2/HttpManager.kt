package com.me.downloadfiles.retrofit2

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object HttpManager {

    private val mRetrofit: Retrofit = Retrofit.Builder()
        .client(initOkHttpClient())
        .baseUrl("https://github.com/")
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()

    /**
     * 获取 apiService
     */
    fun <T> create(apiService: Class<T>): T {
        return mRetrofit.create(apiService)
    }

    private fun initOkHttpClient(): OkHttpClient {
        val build = OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)


        //日志拦截器
        if (true){
            val logInterceptor = HttpLoggingInterceptor { message: String ->
                Log.i("okhttp", "header->$message")
            }
            logInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
            build.addInterceptor(logInterceptor)

            val logInterceptorBody = HttpLoggingInterceptor { message: String ->
                Log.i("okhttp", "body->$message")
            }
            logInterceptor.level = HttpLoggingInterceptor.Level.BODY
            build.addInterceptor(logInterceptorBody)
        }

//        //网络状态拦截
//        build.addInterceptor(Interceptor { chain ->
//            val request = chain.request()
//            chain.proceed(request)
//        })

        return build.build()
    }
}
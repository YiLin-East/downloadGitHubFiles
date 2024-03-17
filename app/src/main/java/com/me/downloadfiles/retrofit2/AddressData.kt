package com.me.downloadfiles.retrofit2

import android.app.Application
import java.io.File

object AddressData {

    val imageCacheFileAddress by lazy { File(Application().filesDir,"image_cache") }
    val backgroundFileAddress by lazy { File(Application().filesDir,"image_background") }
}
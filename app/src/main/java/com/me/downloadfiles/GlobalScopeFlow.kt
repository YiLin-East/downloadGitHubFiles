package com.me.downloadfiles

import kotlinx.coroutines.flow.MutableSharedFlow

object GlobalScopeFlow {
    val textFlow = MutableSharedFlow<String>(1)
}
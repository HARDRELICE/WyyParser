package com.hardrelice.wyyparser.utils

import android.content.Context

object ContextEverywhere {
    var ApplicationContext: Context? = null

    fun initial(context: Context?) {
        ApplicationContext = context
    }

    private fun getInstance(): Context? {
        return ApplicationContext
    }

    var context: Context? = getInstance()
}

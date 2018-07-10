package com.rahmitufanoglu.mygames

import android.app.Application
import android.content.Context
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher

class MainApplication : Application() {

    private var refWatcher: RefWatcher? = null

    override fun onCreate() {
        super.onCreate()
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        refWatcher = LeakCanary.install(this)
    }

    companion object {
        fun getRefWatcher(context: Context): RefWatcher {
            val application = context.applicationContext as MainApplication
            return application.refWatcher!!
        }
    }
}

package com.a65apps.pandaananass.tetsapplication.app

import android.app.Application
import com.a65apps.pandaananass.tetsapplication.di.app.AppComponent
import com.a65apps.pandaananass.tetsapplication.di.app.AppModule
import com.a65apps.pandaananass.tetsapplication.di.app.DaggerAppComponent

class AppDelegate : Application() {
    private var appComponent: AppComponent? = null

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        appComponent = DaggerAppComponent
                .builder()
                .appModule(AppModule(this))
                .build()
        appComponent?.inject(this)
    }

    fun getAppComponent(): AppComponent? {
        if (appComponent == null) {
            init()
        }
        return appComponent
    }
}
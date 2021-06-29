package com.example.application

import android.app.Application
import com.a65apps.pandaananass.tetsapplication.api.AppContainer
import com.a65apps.pandaananass.tetsapplication.api.ComponentOwner
import com.example.application.app.AppComponent
import com.example.application.app.DaggerAppComponent

class AppDelegate : Application(), ComponentOwner {
    private var appComponent: AppComponent? = null

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        appComponent = DaggerAppComponent
            .factory()
            .create(this)
    }

    override fun getAppComponent(): AppContainer? {
        if (appComponent == null) {
            init()
        }
        return appComponent
    }
}

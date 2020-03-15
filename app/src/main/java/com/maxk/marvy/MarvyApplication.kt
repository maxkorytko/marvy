package com.maxk.marvy

import android.app.Application
import com.maxk.marvy.di.DefaultServiceLocator
import com.maxk.marvy.di.ServiceLocator

class MarvyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        ServiceLocator.set(DefaultServiceLocator())
    }
}
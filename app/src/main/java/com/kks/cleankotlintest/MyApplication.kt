package com.kks.cleankotlintest

import android.app.Application
import com.kks.cleankotlintest.di.networkModule
import com.kks.cleankotlintest.di.presentationModule
import com.kks.cleankotlintest.common.ReleaseTree
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

open class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin()
        plantTimber()
    }

    protected open fun startKoin() {
        startKoin {
            if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
            androidContext(this@MyApplication)
            modules(
                listOf(
                    presentationModule,
                    networkModule
                )
            )
        }
    }

    protected open fun plantTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(object : Timber.DebugTree() {
                override fun createStackElementTag(element: StackTraceElement): String {
                    return String.format(
                        "Class:%s: Line: %s, Method: %s",
                        super.createStackElementTag(element),
                        element.lineNumber,
                        element.methodName
                    )
                }
            })
        } else {
            Timber.plant(ReleaseTree())
        }
    }
}
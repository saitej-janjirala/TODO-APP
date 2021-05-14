package com.saitejajanjirala.todo_app

import android.app.Application
import com.saitejajanjirala.todo_app.data.DatabaseService
import com.saitejajanjirala.todo_app.di.components.ApplicationComponent
import com.saitejajanjirala.todo_app.di.components.DaggerApplicationComponent
import com.saitejajanjirala.todo_app.di.modules.ApplicationModule
import javax.inject.Inject

class BaseApplication:Application() {
    lateinit var applicationComponent: ApplicationComponent
    @Inject
    lateinit var databaseService: DatabaseService

    override fun onCreate() {
        super.onCreate()
        getDependencies()
    }

    private fun getDependencies() {
        applicationComponent=DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
        applicationComponent.inject(this)
    }
}
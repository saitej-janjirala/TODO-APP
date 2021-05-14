package com.saitejajanjirala.todo_app.di.components

import com.saitejajanjirala.todo_app.BaseApplication
import com.saitejajanjirala.todo_app.data.DatabaseService
import com.saitejajanjirala.todo_app.di.modules.ApplicationModule
import dagger.Component
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton

@Component(modules = [ApplicationModule::class])
@Singleton
interface ApplicationComponent {

    fun inject(baseApplication: BaseApplication)

    fun getDatabaseService():DatabaseService

    fun getCompositeDisposable():CompositeDisposable
}
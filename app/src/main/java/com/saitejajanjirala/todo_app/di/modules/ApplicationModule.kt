package com.saitejajanjirala.todo_app.di.modules

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.saitejajanjirala.todo_app.data.DatabaseService
import com.saitejajanjirala.todo_app.di.ApplicationContext
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class ApplicationModule(private val application: Application) {

    @ApplicationContext
    @Provides
    fun provideContext(): Context = application

    @Provides
    fun providesDatabaseService(): DatabaseService = Room.databaseBuilder(
        application,
        DatabaseService::class.java,
        "tasks-db"
    ).build()

    @Provides
    fun providesCompositeDisposable(): CompositeDisposable = CompositeDisposable()
}
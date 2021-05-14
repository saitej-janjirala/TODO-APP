package com.saitejajanjirala.todo_app.di.modules

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.saitejajanjirala.todo_app.ViewModelProviderFactory
import com.saitejajanjirala.todo_app.activities.main.MainViewModel
import com.saitejajanjirala.todo_app.activities.taskdetail.TaskDetailViewModel
import com.saitejajanjirala.todo_app.data.DatabaseService
import com.saitejajanjirala.todo_app.di.ActivityContext
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class ActivityModule(private val activity:AppCompatActivity) {


    @ActivityContext
    @Provides
    fun provideContext(): Context = activity

    @Provides
    fun provideMainViewModel(
        databaseService:DatabaseService,
        compositeDisposable: CompositeDisposable):MainViewModel =ViewModelProviderFactory(
        MainViewModel::class
    ) {
        MainViewModel(compositeDisposable, databaseService)
    }.create(MainViewModel::class.java)

    @Provides
    fun provideTaskDetailViewModel(
        databaseService:DatabaseService,
        compositeDisposable: CompositeDisposable):TaskDetailViewModel =ViewModelProviderFactory(
        TaskDetailViewModel::class
    ) {
        TaskDetailViewModel(compositeDisposable, databaseService)
    }.create(TaskDetailViewModel::class.java)

}
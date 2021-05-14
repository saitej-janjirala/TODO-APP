package com.saitejajanjirala.todo_app.activities.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saitejajanjirala.todo_app.data.DatabaseService
import com.saitejajanjirala.todo_app.data.Task
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MainViewModel(
    val compositeDisposable: CompositeDisposable,
    val databaseService: DatabaseService
) : ViewModel() {
    var taskAdded = MutableLiveData<Long?>()
    var message = MutableLiveData<String>()
    var tasks = MutableLiveData<List<Task>>()
    var taskDeleted=MutableLiveData<Int?>()
    var updatedTask=MutableLiveData<Task>()
    init {
        getData()
    }

    fun deleteTask(task: Task) {
        compositeDisposable.add(
            databaseService
                .getTaskDao()
                .deleteTask(task)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    taskDeleted.postValue(it)
                    message.postValue("Task Deleted Successfully")
                }, {
                    message.postValue("Unable to delete the task")
                })
        )
    }

     fun getData() {
        compositeDisposable.add(
            databaseService
                .getTaskDao()
                .getAllTasks()
                .subscribeOn(Schedulers.io())
                .subscribe({
                    tasks.postValue(it)
                }, {
                    message.postValue("Unable to fetch the tasks")
                })
        )
    }

    fun addTask(task: Task) {
        compositeDisposable.add(
            databaseService
                .getTaskDao()
                .insertTask(task)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    taskAdded.postValue(it)
                }, {
                    message.postValue("Unable to add the task")
                })
        )
    }

    fun getTAskByID(id:Long){
        compositeDisposable.add(
            databaseService
                .getTaskDao()
                .getTaskById(id)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    updatedTask.postValue(it)
                },{
                })
        )
    }
}
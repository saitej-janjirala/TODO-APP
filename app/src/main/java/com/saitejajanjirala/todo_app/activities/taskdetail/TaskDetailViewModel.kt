package com.saitejajanjirala.todo_app.activities.taskdetail

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saitejajanjirala.todo_app.data.DatabaseService
import com.saitejajanjirala.todo_app.data.Task
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class TaskDetailViewModel(
    val compositeDisposable: CompositeDisposable,
    val databaseService: DatabaseService
) : ViewModel() {
    val message = MutableLiveData<String>()
    val dataSaved = MutableLiveData<Int>()
    val isChanged=MutableLiveData<Boolean>()
    lateinit var task: Task
    lateinit var changedTask: Task
    fun setTaskData(mTask: Task) {
        task = mTask.copy()
        changedTask=mTask.copy()
    }

    fun saveData() {
        changedTask.timeinmillis=System.currentTimeMillis()
        compositeDisposable.add(
            databaseService.getTaskDao()
                .updateTask(changedTask)
                .subscribeOn(Schedulers.io())
                .subscribe({
                    dataSaved.postValue(it)
                    message.postValue("task updated")
                }, {
                    message.postValue("unable to update data")
                })
        )
    }

    fun descriptionChange(description: String?) {
        Log.i("description","=>$description")
        if (description != null) {
            changedTask.description=description
            checkIfChanged()
        }
    }

    fun titleChange(title: String?) {
        if (title != null) {
            changedTask.title=title
            checkIfChanged()
        }
    }

    private fun checkIfChanged(){
        isChanged.postValue(changedTask.title!=task.title || changedTask.description!=task.description)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}
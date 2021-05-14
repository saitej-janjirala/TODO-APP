package com.saitejajanjirala.todo_app.activities.taskdetail

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.saitejajanjirala.todo_app.BaseApplication
import com.saitejajanjirala.todo_app.R
import com.saitejajanjirala.todo_app.data.Task
import com.saitejajanjirala.todo_app.databinding.ActivityTaskDetailBinding
import com.saitejajanjirala.todo_app.di.components.DaggerActivityComponent
import com.saitejajanjirala.todo_app.di.modules.ActivityModule
import javax.inject.Inject

class TaskDetailActivity : AppCompatActivity() {
    lateinit var mBinding: ActivityTaskDetailBinding
    lateinit var currentTask: Task

    @Inject
    lateinit var taskDetailViewModel: TaskDetailViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_task_detail)
        DaggerActivityComponent.builder()
            .applicationComponent((application as BaseApplication).applicationComponent)
            .activityModule(ActivityModule(this))
            .build()
            .inject(this)
        val bundle = intent.extras
        bundle?.let {
            currentTask = it.getSerializable("task") as Task
            taskDetailViewModel.setTaskData(currentTask)
            mBinding.currenttask = taskDetailViewModel.changedTask
        }
        setListeners()
    }
    private fun setListeners(){
        taskDetailViewModel.isChanged.observe(this, Observer {
            if(it!=null){
                if(it) {
                    mBinding.saveEditing.visibility = View.VISIBLE
                }
                else{
                    mBinding.saveEditing.visibility = View.INVISIBLE
                }
            }
        })
        taskDetailViewModel.dataSaved.observe(this, Observer {
            if(it!=null){
                setResult(Activity.RESULT_OK)
                finish()
            }
        })
        mBinding.cancelEditing.setOnClickListener {
            setResult(Activity.RESULT_CANCELED)
            finish()
        }
        mBinding.saveEditing.setOnClickListener {
            taskDetailViewModel.saveData()
        }
        mBinding.titleText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val isValid: String? = isValidTitle()
                mBinding.currenttask = taskDetailViewModel.changedTask
                taskDetailViewModel.titleChange(mBinding.titleText.text.toString())
                if (isValid == null) {
                    mBinding.titleLayout.isErrorEnabled = false
                } else {
                    mBinding.titleLayout.error = isValid
                    taskDetailViewModel.isChanged.postValue(false)
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        mBinding.descriptionText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val isValid: String? = isValidDescription()
                mBinding.currenttask = taskDetailViewModel.changedTask
                taskDetailViewModel.descriptionChange(mBinding.descriptionText.text.toString())
                if (isValid == null) {
                    mBinding.descriptionLayout.isErrorEnabled = false
                } else {
                    mBinding.descriptionLayout.error = isValid
                    taskDetailViewModel.isChanged.postValue(false)
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

    }

    fun isValidDescription(): String? {
        if (mBinding.descriptionText.text.isNullOrBlank() || mBinding.descriptionText.text.isNullOrEmpty()) {
            return "description can't be empty"
        } else if (mBinding.descriptionText.text.toString().split("\\s+".toRegex()).size > 150) {
            return "description can't exceed 150 words"
        }
        return null
    }

    fun isValidTitle(): String? {
        if (mBinding.titleText.text.isNullOrBlank() || mBinding.titleText.text.isNullOrEmpty()) {
            return "title can't be empty"
        } else if (mBinding.titleText.text.toString().split("\\s+".toRegex()).size > 20) {
            return "tile can't exceed 20 words"
        }
        return null
    }
}
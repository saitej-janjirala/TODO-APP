package com.saitejajanjirala.todo_app.activities.main

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.saitejajanjirala.todo_app.BaseApplication
import com.saitejajanjirala.todo_app.R
import com.saitejajanjirala.todo_app.activities.taskdetail.TaskDetailActivity
import com.saitejajanjirala.todo_app.adapters.TaskAdapter
import com.saitejajanjirala.todo_app.data.Task
import com.saitejajanjirala.todo_app.databinding.ActivityMainBinding
import com.saitejajanjirala.todo_app.di.components.DaggerActivityComponent
import com.saitejajanjirala.todo_app.di.modules.ActivityModule
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), TaskAdapter.onTaskItemClickListener {
    lateinit var mBinding: ActivityMainBinding

    @Inject
    lateinit var mainViewModel: MainViewModel
    lateinit var tasksList: ArrayList<Task>
    lateinit var taskAdapter: TaskAdapter
    lateinit var layoutManager: StaggeredGridLayoutManager
    var deletedObj: Task? = null
     var updatedPosition:Int=-1
    val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            object : ActivityResultCallback<ActivityResult> {
                override fun onActivityResult(result: ActivityResult?) {
                    if (result != null) {
                        if (result.resultCode == Activity.RESULT_OK) {
                            if(updatedPosition!=-1 && tasksList.size>updatedPosition){
                                mainViewModel.getTAskByID(tasksList[updatedPosition].id)
                            }
                        } else if (result.resultCode == Activity.RESULT_CANCELED) {
                            //do nothing
                            Log.i("task cancelled?","yes")
                        }
                    }
                }

            })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        DaggerActivityComponent.builder()
            .applicationComponent((application as BaseApplication).applicationComponent)
            .activityModule(ActivityModule(this))
            .build()
            .inject(this)
        prepareAdapter()
        setListeners()
    }

    private fun prepareAdapter() {
        tasksList = ArrayList()
        taskAdapter = TaskAdapter(this, tasksList, this)
        layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mBinding.tasksView.layoutManager = layoutManager
        mBinding.tasksView.adapter = taskAdapter
    }

    private fun setListeners() {
        mainViewModel.message.observe(this, Observer {
            if (it != null) {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })
        mainViewModel.tasks.observe(this, Observer {
            if (it != null) {
                tasksList.clear()
                tasksList.addAll(it)
                taskAdapter.notifyDataSetChanged()
            }
        })
        mainViewModel.taskDeleted.observe(this, Observer {
            if (it != null) {
                if (deletedObj != null && tasksList.contains(deletedObj)) {
                    tasksList.remove(deletedObj)
                    taskAdapter.notifyDataSetChanged()
                }
            }
        })
        mainViewModel.updatedTask.observe(this, Observer {
            if(it!=null && updatedPosition!=-1 && updatedPosition<tasksList.size){
                tasksList.set(updatedPosition,it)
                taskAdapter.notifyItemChanged(updatedPosition)
                updatedPosition=-1
            }
        })
        mBinding.addTaskButton.setOnClickListener {
            val dialog = Dialog(this)
            val popupView = layoutInflater.inflate(R.layout.add_task_dialog, null)
            val titleLayout: TextInputLayout = popupView.findViewById(R.id.title_layout)
            val titleText: TextInputEditText = popupView.findViewById(R.id.title_text)
            val descriptionLayout: TextInputLayout = popupView.findViewById(R.id.description_layout)
            val descriptionText: TextInputEditText = popupView.findViewById(R.id.description_text)
            val dialogProgress: ProgressBar = popupView.findViewById(R.id.task_dialog_progress)
            val cancelDialog: TextView = popupView.findViewById(R.id.cancel_dialog)
            val addButton: Button = popupView.findViewById(R.id.add_button)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(popupView)
            val window = dialog.window
            dialog.setCancelable(false)
            window?.apply {
                setLayout(
                    ConstraintLayout.LayoutParams.MATCH_PARENT,
                    ConstraintLayout.LayoutParams.WRAP_CONTENT
                )
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
            cancelDialog.setOnClickListener {
                dialog.dismiss()
            }
            addButton.setOnClickListener {
                var title = titleText.text
                var description = descriptionText.text
                if (description.isNullOrEmpty() || description.isNullOrBlank()) {
                    descriptionLayout.error = "description can't be empty"
                } else {
                    if (description.toString().split("\\s+".toRegex()).size > 150) {
                        descriptionLayout.error = "description can't exceed 150 words"
                    } else {
                        descriptionLayout.isErrorEnabled = false
                    }
                }

                if (title.isNullOrBlank() || title.isNullOrEmpty()) {
                    titleLayout.error = "Title can't be empty"
                } else {
                    if (title.toString().split("\\s+".toRegex()).size > 20) {
                        titleLayout.error = "Title can't exceed 20 words"
                    } else {
                        titleLayout.isErrorEnabled = false
                    }
                }
                if (!descriptionLayout.isErrorEnabled && !titleLayout.isErrorEnabled) {
                    dialogProgress.visibility = View.VISIBLE
                    cancelDialog.isEnabled = false
                    addButton.isEnabled = false
                    descriptionText.isEnabled = false
                    titleText.isEnabled = false
                    val task = Task(
                        title = title.toString(),
                        description = description.toString(),
                        createdAt = Date(),
                        timeinmillis = System.currentTimeMillis()
                    )
                    mainViewModel.addTask(task)
                }

            }
            mainViewModel.taskAdded.observe(this, Observer {
                if (it != null) {
                    dialogProgress.visibility = View.GONE
                    cancelDialog.isEnabled = true
                    addButton.isEnabled = true
                    descriptionText.isEnabled = true
                    titleText.isEnabled = true
                    descriptionLayout.isErrorEnabled = true
                    titleLayout.isErrorEnabled = true
                    Handler().postDelayed({
                        Toast.makeText(this, "task added", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                        mainViewModel.taskAdded.postValue(null)
                        mainViewModel.getData()
                        mBinding.tasksView.smoothScrollToPosition(0)
                    }, 200)

                }
            })
            dialog.show()
        }
    }

    override fun onTaskClick(position: Int) {
        openTaskDetail(position)
    }

    override fun onSettingsClick(position: Int, imageView: ImageView) {
        val popup = PopupMenu(this@MainActivity, imageView)
        popup.menuInflater.inflate(R.menu.task_item_menu, popup.menu)

        popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem): Boolean {
                when (item.itemId) {
                    R.id.edit -> {
                        openTaskDetail(position)
                    }
                    R.id.delete -> {
                        if (tasksList.size > position) {
                            val obj = tasksList[position].copy()
                            mainViewModel.deleteTask(obj)
                            deletedObj = obj
                        }
                    }
                }
                return true
            }
        })
        popup.show()
    }

    private fun openTaskDetail(position: Int) {
        updatedPosition=position
        val intent = Intent(this, TaskDetailActivity::class.java)
        val bundle = Bundle()
        bundle.putSerializable("task", tasksList[position])
        intent.putExtras(bundle)
        startForResult.launch(intent)
    }
}
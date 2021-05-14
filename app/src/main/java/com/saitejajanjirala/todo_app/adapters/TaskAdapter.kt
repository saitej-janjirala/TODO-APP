package com.saitejajanjirala.todo_app.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.saitejajanjirala.todo_app.R
import com.saitejajanjirala.todo_app.data.Task
import com.saitejajanjirala.todo_app.utils.Utils

class TaskAdapter(
    val context: Context,
    val tasksList: ArrayList<Task>,
    val ontaskItemClickListener: onTaskItemClickListener
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {
    public class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskLayout: ConstraintLayout = itemView.findViewById(R.id.task_layout)
        val taskTitle: TextView = itemView.findViewById(R.id.task_title)
        val taskDescription: TextView = itemView.findViewById(R.id.task_description)
        val taskDate: TextView = itemView.findViewById(R.id.task_date)
        val taskSettings:ImageView=itemView.findViewById(R.id.settings_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasksList[position]
        holder.taskLayout.setOnClickListener {
            if(position!=RecyclerView.NO_POSITION){
                ontaskItemClickListener.onTaskClick(position)
            }
        }
        holder.taskSettings.setOnClickListener {
            if(position!=RecyclerView.NO_POSITION){
                ontaskItemClickListener.onSettingsClick(position,holder.taskSettings)
            }
        }
        holder.taskTitle.text = task.title
        holder.taskDescription.text = task.description
        holder.taskDate.text = Utils.getTimeStamp(task.createdAt)
    }

    override fun getItemCount() = tasksList.size
    public interface onTaskItemClickListener {
        public fun onTaskClick(position: Int)
        public fun onSettingsClick(position: Int, imageView: ImageView)
    }
}
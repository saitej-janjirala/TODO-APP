package com.saitejajanjirala.todo_app.data

import androidx.room.*
import io.reactivex.Single

@Dao
interface TaskDao {

    @Insert
    fun insertTask(task: Task):Single<Long>

    @Update
    fun updateTask(task: Task):Single<Int>

    @Delete
    fun deleteTask(task: Task):Single<Int>

    @Query("select * from tasks order by timestamp desc")
    fun getAllTasks():Single<List<Task>>

    @Query("select * from tasks where id=:id limit 1")
    fun getTaskById(id:Long):Single<Task>

    @Query("select count(*) from tasks")
    fun getCount(): Single<Int>

}
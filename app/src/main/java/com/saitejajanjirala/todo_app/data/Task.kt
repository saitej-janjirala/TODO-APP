package com.saitejajanjirala.todo_app.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Long = 0,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "description") var description: String,
    @ColumnInfo(name = "createdat") var createdAt: Date,
    @ColumnInfo(name = "timestamp") var timeinmillis:Long
) : Serializable {
}
package com.saitejajanjirala.todo_app.utils

import java.util.*

object Utils {
    fun getTimeStamp(date: Date):String{
        val arr=date.toString().split(" ")
        return arr[0]+" "+arr[1]+" "+arr[2]+" "+arr[5]+"\n"+arr[3]
    }

}
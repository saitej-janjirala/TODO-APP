package com.saitejajanjirala.todo_app.di.components

import com.saitejajanjirala.todo_app.activities.main.MainActivity
import com.saitejajanjirala.todo_app.activities.taskdetail.TaskDetailActivity
import com.saitejajanjirala.todo_app.activities.taskdetail.TaskDetailViewModel
import com.saitejajanjirala.todo_app.di.ActivityScope
import com.saitejajanjirala.todo_app.di.modules.ActivityModule
import dagger.Component

@ActivityScope
@Component(modules = [ActivityModule::class],dependencies = [ApplicationComponent::class])
interface ActivityComponent {
    fun inject(activity: MainActivity)
    fun inject(activity:TaskDetailActivity)
}
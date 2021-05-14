package com.saitejajanjirala.todo_app.di

import javax.inject.Qualifier


@Qualifier
@Retention(AnnotationRetention.SOURCE)
annotation class ApplicationContext

@Qualifier
@Retention(AnnotationRetention.SOURCE)
annotation class ActivityContext

package com.slack.exercise.dagger

import com.slack.exercise.App
import com.slack.exercise.search.di.SearchModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Component providing Application scoped instances.
 */
@Singleton
@Component(modules = [AppModule::class, AndroidSupportInjectionModule::class, SearchModule::class])
interface AppComponent : AndroidInjector<DaggerApplication> {
    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: App): Builder

        fun build(): AppComponent
    }
}

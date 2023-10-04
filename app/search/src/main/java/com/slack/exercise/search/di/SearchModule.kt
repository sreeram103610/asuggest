package com.slack.exercise.search.di

import com.slack.exercise.search.data.api.SlackSearchClient
import com.slack.exercise.search.data.api.DefaultSlackSearchClient
import com.slack.exercise.search.data.api.SlackSearchApi
import com.slack.exercise.search.dataprovider.UserSearchResultDataProvider
import com.slack.exercise.search.dataprovider.UserSearchResultDataProviderImpl
import com.slack.exercise.search.ui.usersearch.UserSearchFragment
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import retrofit2.Retrofit
import javax.inject.Scope
import javax.inject.Singleton

@Module
abstract class SearchModule {

    @SearchScope
    @ContributesAndroidInjector(modules = [Provider::class])
    abstract fun searchFragment() : UserSearchFragment

    @Module
    internal object Provider{

        @SearchScope
        @Provides
        fun provideUserSearchResultDataProvider(
            dataProvider: UserSearchResultDataProviderImpl
        ): UserSearchResultDataProvider = dataProvider

        @SearchScope
        @Provides
        fun provideSlackApi(apiImpl: DefaultSlackSearchClient): SlackSearchClient = apiImpl

        @SearchScope
        @Provides
        fun provideSlackSearchApi(retrofit: Retrofit): SlackSearchApi = retrofit.create(SlackSearchApi::class.java)
    }
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class SearchScope

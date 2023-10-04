package com.slack.exercise.search.di

import com.slack.exercise.search.data.api.SlackSearchClient
import com.slack.exercise.search.data.api.DefaultSlackSearchClient
import com.slack.exercise.search.data.api.SlackSearchApi
import com.slack.exercise.search.data.repo.DefaultSearchRepo
import com.slack.exercise.search.data.repo.SearchRepo
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
    @ContributesAndroidInjector
    abstract fun searchFragment() : UserSearchFragment

    @SearchScope
    @Provides
    fun provideUserSearchResultDataProvider(
        dataProvider: UserSearchResultDataProviderImpl
    ): UserSearchResultDataProvider = dataProvider


    @Singleton
    @Provides
    internal abstract fun provideSearchRepo(searchRepo: SearchRepo) : DefaultSearchRepo

    @Singleton
    @Provides
    internal abstract fun provideSlackApi(apiImpl: DefaultSlackSearchClient): SlackSearchClient

    @Singleton
    @Provides
    fun provideSlackSearchApi(retrofit: Retrofit): SlackSearchApi = retrofit.create(SlackSearchApi::class.java)
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class SearchScope

package com.slack.exercise.search.di

import com.slack.exercise.search.data.api.DefaultBlockedPhrasesFileApi
import com.slack.exercise.search.data.api.SlackSearchClient
import com.slack.exercise.search.data.api.DefaultSlackSearchClient
import com.slack.exercise.search.data.api.BlockedPhrasesFileApi
import com.slack.exercise.search.data.api.SlackSearchApi
import com.slack.exercise.search.data.repo.BlockedPhrasesRepo
import com.slack.exercise.search.data.repo.DefaultBlockedPhrasesRepo
import com.slack.exercise.search.data.repo.DefaultSearchRepo
import com.slack.exercise.search.data.repo.SearchRepo
import com.slack.exercise.search.domain.dataprovider.UserSearchResultDataProvider
import com.slack.exercise.search.domain.dataprovider.UserSearchResultDataProviderImpl
import com.slack.exercise.search.ui.usersearch.UserSearchFragment
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import retrofit2.Retrofit
import javax.inject.Scope
import javax.inject.Singleton

@Module(includes = [DomainModule::class, SearchModule.Provider::class])
abstract class SearchModule {

    @SearchScope
    @ContributesAndroidInjector
    abstract fun searchFragment() : UserSearchFragment

    @Module
    object Provider {
        @Singleton
        @Provides
        fun provideSlackSearchApi(retrofit: Retrofit): SlackSearchApi =
            retrofit.create(SlackSearchApi::class.java)

    }

    @Singleton
    @Binds
    internal abstract fun provideBlockedPhrasesRepo(repo: DefaultBlockedPhrasesRepo): BlockedPhrasesRepo

    @Singleton
    @Binds
    internal abstract fun providesDenyFileApi(api: DefaultBlockedPhrasesFileApi): BlockedPhrasesFileApi

    @Singleton
    @Binds
    internal abstract fun provideSearchRepo(searchRepo: DefaultSearchRepo) : SearchRepo

    @Singleton
    @Binds
    internal abstract fun provideSlackApi(apiImpl: DefaultSlackSearchClient): SlackSearchClient

}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class SearchScope

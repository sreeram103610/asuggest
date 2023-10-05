package com.slack.exercise.search.di

import com.slack.exercise.search.domain.dataprovider.UserSearchResultDataProvider
import com.slack.exercise.search.domain.dataprovider.UserSearchResultDataProviderImpl
import com.slack.exercise.search.domain.usecase.DefaultGetUsersUsecase
import com.slack.exercise.search.domain.usecase.GetUsersUsecase
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class DomainModule {

    @Binds
    @Singleton
    internal abstract fun provideUserSearchResultData(provider: UserSearchResultDataProviderImpl): UserSearchResultDataProvider

    @Binds
    @Singleton
    internal abstract fun provideGetUsersUsecase(usecase: DefaultGetUsersUsecase): GetUsersUsecase
}

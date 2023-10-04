package com.slack.exercise.search.di

import com.slack.exercise.search.data.repo.DefaultBlockedPhrasesRepo
import dagger.assisted.AssistedFactory
import kotlinx.coroutines.CoroutineScope

@AssistedFactory
interface DefaultBlockedPhrasesRepoFactory {
    fun create(scope: CoroutineScope): DefaultBlockedPhrasesRepo
}
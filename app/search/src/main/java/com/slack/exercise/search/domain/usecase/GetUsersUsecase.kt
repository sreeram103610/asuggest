package com.slack.exercise.search.domain.usecase

import com.slack.exercise.search.data.repo.BlockedPhrasesRepo
import com.slack.exercise.search.data.repo.SearchRepo
import com.slack.exercise.search.data.repo.model.RepoErrorType
import com.slack.exercise.search.data.repo.model.RepoResult
import com.slack.exercise.search.domain.model.DomainResult
import com.slack.exercise.search.domain.model.InternetError
import com.slack.exercise.search.domain.model.NoUsersFound
import com.slack.exercise.search.domain.model.UserSearchResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * Use case for fetching users based on a search term.
 */
interface GetUsersUsecase {

    /**
     * Retrieves users matching the given search term.
     *
     * @param searchItem The term to search users by.
     * @return A [Flow] of [DomainResult] containing the list of matching users or an error.
     */
    operator fun invoke(searchItem: String): Flow<DomainResult<List<UserSearchResult>>>
}

/**
 * Default implementation of [GetUsersUsecase].
 *
 * @property searchRepo The repository to search users.
 * @property blockedPhrasesRepo The repository to manage blocked search phrases.
 */
internal class DefaultGetUsersUsecase @Inject constructor(
    private val searchRepo: SearchRepo,
    private val blockedPhrasesRepo: BlockedPhrasesRepo
) : GetUsersUsecase {

    /**
     * Executes the user search, handles the blocked phrases, and maps the results or errors into [DomainResult].
     *
     * @param searchItem The search term to find users.
     * @return A [Flow] of [DomainResult] containing the list of users or an error.
     */
    override fun invoke(searchItem: String): Flow<DomainResult<List<UserSearchResult>>> {
        val validatedSearchTerm = searchItem.trim().lowercase()
        return flow {
            emit(DomainResult.Loading)
            if (blockedPhrasesRepo.phraseExists(validatedSearchTerm)) {
                emit(DomainResult.Error(NoUsersFound))
            } else {
                emitAll(
                    searchRepo.searchUsers(validatedSearchTerm)
                        .map {
                            when (it) {
                                is RepoResult.Available -> {
                                    if (it.repoData.isEmpty()) {
                                        blockedPhrasesRepo.addPhrase(validatedSearchTerm)
                                        DomainResult.Error(NoUsersFound)
                                    } else {
                                        DomainResult.Loaded(it.repoData.map { userDto ->
                                            UserSearchResult.from(
                                                userDto
                                            )
                                        })
                                    }
                                }

                                is RepoResult.NotAvailable -> when (it.error) {
                                    RepoErrorType.NetworkError,
                                    RepoErrorType.ServerError,
                                    RepoErrorType.UnknownError -> DomainResult.Error(InternetError)
                                }
                            }
                        }
                )
            }
        }
    }
}

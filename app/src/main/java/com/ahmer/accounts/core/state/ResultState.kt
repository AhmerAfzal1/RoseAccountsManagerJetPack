package com.ahmer.accounts.core.state

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

sealed interface ResultState<out T> {

    data object Loading : ResultState<Nothing>

    data class Success<T>(val data: T) : ResultState<T>

    data class Error(
        val entity: ErrorEntity,
        var consumed: Boolean = false
    ) : ResultState<Nothing>

    companion object {
        fun <T> flowRequest(
            dispatcher: CoroutineDispatcher = Dispatchers.Main,
            block: suspend () -> T
        ): Flow<ResultState<T>> {
            return flow {
                emit(Loading)
                val result = block()
                emit(Success(result))
            }.catch {
                emit(Error(ErrorEntity(it)))
            }.flowOn(dispatcher)
        }

        fun <T> flowMap(block: () -> Flow<ResultState<T>>): Flow<ResultState<T>> {
            return flow {
                emit(Loading)
                emitAll(block())
            }
        }
    }
}
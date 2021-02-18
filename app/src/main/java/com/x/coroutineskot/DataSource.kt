package com.x.coroutineskot

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataSource @Inject constructor(
    private val apiService: ContentApiService){
    val getData: Flow<List<TodoEntity>> = flow {
        emit(apiService.getToDO())
    }

}
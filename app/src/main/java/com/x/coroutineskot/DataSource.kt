package com.x.coroutineskot

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton
/**
 * This class use to call different datasource network api
 * api service is source of apis
 *
 * */
@Singleton
class DataSource @Inject constructor(
    private val apiService: ContentApiService){
    val getData: Flow<List<TodoEntity>> = flow {
        emit(apiService.getToDO())
    }

}
package com.x.coroutineskot

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkRepository @Inject constructor(
    private val apiService: ContentApiService,
    private val dto: TourDao,
    private val dataSource: DataSource
) {
    val getData: Flow<Result<List<TodoEntity>>> = flow {
        dataSource.getData.onStart {  }
            .catch { e -> dto.getTodos().collect { list -> emit(Result.ErrorWithData(e, list)) } }
            .collect { it ->
                println("testX network data "+it.size)
                dto.saveToDos(it)
                dto.getTodos().collect { list -> emit(Result.Success(list)) }
            }


    }.flowOn(Dispatchers.IO)

    fun getDateOne():Flow<Result<List<TodoEntity>>>{
        return flow {
            dataSource.getData.onStart {  }
                .catch { e -> dto.getTodos().collect { list -> emit(Result.ErrorWithData(e, list)) } }
                .collect { it ->
                    println("testX network data "+it.size)
                    dto.saveToDos(it)
                    dto.getTodos().collect { list -> emit(Result.Success(list)) }
                }


        }.flowOn(Dispatchers.IO)
    }
}
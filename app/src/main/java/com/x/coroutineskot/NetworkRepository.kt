package com.x.coroutineskot

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

/***
 * NetworkRepository used to call network API from @link DTViewModel
 * all the parameter objects will be created and pass to the constructor by the
 * dependency system automatically. You don't have to create them and pass them by yourself
 *
 * Whether You use getData Variable or getDateOne() method to fetch data
 * Here is the flow of procedure how Network fetch works
 * First of all it create flow builder using flow{} that returns flow object
 * within that it calls for network api from where it fetch data using (dataSource.getData() api which also return flow)
 * and on start{} tells user that network call is in progress
 * catch is called when network call failed then you simply return data from database wrapped in Result.ErrorWithData() state
 * indicating Api call is failed but data is available from database if it is there
 * and network call is SUCCESS then it saves the resultant data into database and then return data from database wrapped in Result.Success() state
 *
 * */
@Singleton
class NetworkRepository @Inject constructor(
    private val apiService: ContentApiService,
    private val dto: TourDao,
    private val dataSource: DataSource
) {

    /***
     * Fetch data and assigning to a Flow variable that you can observe
     * */
    val getData: Flow<Result<List<TodoEntity>>> = flow {
        /** onStart  Returns a flow that invokes the given [action] Result.Loading **before** this flow starts to be collected.
         *  flowOf("a", "b", "c")
         *     .onStart { emit("Begin") }
         *     .collect { println(it) } // prints Begin, a, b, c
         */

        dataSource.getData.onStart { emit(Result.Loading) }
            .catch { e -> dto.getTodos().collect { list -> emit(Result.ErrorWithData(e, list)) } }
            .collect { it ->
                println("testX network data " + it.size)
                dto.saveToDos(it)
                dto.getTodos().collect { list -> emit(Result.Success(list)) }
            }


    }.flowOn(Dispatchers.IO)

    /***
     * This method fetch data from network API and Returns a Flow object
     * to observe on
     * */
    fun getDateOne(): Flow<Result<List<TodoEntity>>> {
        /** onStart  Returns a flow that invokes the given [action] Result.Loading **before** this flow starts to be collected.
         *  flowOf("a", "b", "c")
         *     .onStart { emit("Begin") }
         *     .collect { println(it) } // prints Begin, a, b, c
         */

        return flow {
            dataSource.getData.onStart { emit(Result.Loading) }
                .catch { e ->
                    dto.getTodos().collect { list -> emit(Result.ErrorWithData(e, list)) }
                }
                .collect { it ->
                    println("testX network data " + it.size)
                    dto.saveToDos(it)
                    dto.getTodos().collect { list -> emit(Result.Success(list)) }
                }


        }.flowOn(Dispatchers.IO)
    }
}
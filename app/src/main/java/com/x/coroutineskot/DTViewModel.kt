/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.x.coroutineskot

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DTViewModel @ViewModelInject constructor(
    private val dataSource: DataSource,
    private val apiService: ContentApiService,
    private val contentDao: TourDao,
    private val repo: NetworkRepository
) : ViewModel() {

    /*val growZone: Flow<List<TourDataEntity>> = flow { emit(emptyList())
    }*/
    /*  val plants: MutableLiveData<List<TourDataEntity>> = MutableLiveData<List<TourDataEntity>>()*/
    val growZone: MutableStateFlow<List<TourDataEntity>> = MutableStateFlow(emptyList())
    val growZone2: MutableStateFlow<Result<List<TodoEntity>>> = MutableStateFlow(Result.Loading)
    val plants: LiveData<Result<List<TodoEntity>>> = growZone2.asLiveData()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            println("dsfdsb oss")

            contentDao.getTours().collect { list ->
                println("dsfdsb o ")
                growZone.value = list
            }

        }
    }

    var job: Job? = null
    fun getRepo() {
        job?.let { job2 -> job2.cancel()  }
        job = viewModelScope.launch {
            repo.getDateOne() .collect { it -> growZone2.value = it }
            /*       NetworkRepository(
                       apiService,
                       contentDao,
                       dataSource
                   ).getData.collect { it -> growZone2.value = it }*/

        }
    }
}

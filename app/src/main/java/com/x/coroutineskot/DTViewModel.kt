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

import android.content.Context
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/***
 * ViewModel for @see MainActivity
 * all the parameter objects will be created and pass to the constructor by the
 * dependency system automatically. You don't have to create them and pass them by yourself
 * */
class DTViewModel @ViewModelInject constructor(
    private val dataSource: DataSource,
    private val apiService: ContentApiService,
    private val contentDao: TourDao,
    private val repo: NetworkRepository
) : ViewModel() {

    /*val growZone: Flow<List<TourDataEntity>> = flow { emit(emptyList())
    }*/
    /*  val plants: MutableLiveData<List<TourDataEntity>> = MutableLiveData<List<TourDataEntity>>()*/

    /**
     * growZone is a MutableStateFlow is a Flow which is known as a HOt Connection which means
     * that on whatever source it observers,
     * it will always be active to listen any changes on that source and
     * notify the observers.
     * (Here we are using a database table TourDataEntity as source see below in  init{} scope contentDao.getTours())
     *
     * see it's uses in {@see MainActivity's viewModel.growZone.asLiveData().}
     * */
    val growZone: MutableStateFlow<List<TourDataEntity>> = MutableStateFlow(emptyList())

    /**
     * growZone2 is also a MutableStateFlow which observer change from Network Api as a source
     * see @see getRepo() method below for uses
     * */
    val growZone2: MutableStateFlow<Result<List<TodoEntity>>> = MutableStateFlow(Result.Loading)

    /**
     * Live data created from growZone2
     * Live data is immutable so it doesn't expose data and provide
     * better abstraction
     * see uses ain mainActivity's viewModel.plants.observe for uses
     *
     * */
    val plants: LiveData<Result<List<TodoEntity>>> = growZone2.asLiveData()

    init {
        viewModelScope.launch(Dispatchers.IO) {

            contentDao.getTours().collect { list ->

                growZone.value = list
            }

        }
    }

    /***
     * Whether you use launch or async it will always returns a Job object
     * getRepo() used for network api call to fetch data
     * */
    var job: Job? = null
    fun getRepo() {
        // if job is not null please cancel previous job before creating new one
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

    /**
     * start WorkManager to perform operation even if the app is closed
     * @see WorkManagerExample class
     * */
    fun loadDataUsingWorkManager(context: Context){
        //val request = OneTimeWorkRequestBuilder<WorkManagerExample>().build()
        val request = OneTimeWorkRequestBuilder<WorkManagerExample>()
        val data = workDataOf("data as parameter" to "ss")
        request.setInputData(data)
        WorkManager.getInstance(context).enqueue(request.build())
    }
}

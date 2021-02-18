package com.x.coroutineskot

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    /**I am using Hilt DI for database dependency Injection*/
    @Inject
    lateinit var contentDao: TourDao

    /**Using ViewModel to leverage MVVM pattern using viewModels() support library*/
    private val viewModel: DTViewModel by viewModels()
    val TAG = "testX "
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //older wy to instantiate a viewModel
        //    val viewModel = ViewModelProviders.of(this).get(DTViewModel::class.java)

        /**
         * @viewModel.growZone is a MutableStateFlow
         * that allow you listen or observe dataset from a particular table when
         * anything change in that table
         * here we are converting MutableStateFlow to livedate to meake it observable
         * */
        viewModel.growZone.asLiveData().observe(this) { list ->
            println(TAG + "database observer " + list.size)
        }

        /**
         * @viewModel.plants is a livedata
         * that allow you listen or observe data fetched from network API call
         * @see DTViewModel's getRepo() method for more
         *
         * Note: You may get result even if network failed in this case data will be delivered by Database.
         * Please {@see NetworkRepository's getDateOne() method or getData variable for more detail about network api calling flow}
         * */
        viewModel.plants.observe(this) { list ->
            /** Here we are checking whether the result was successful or not
             * for that purpose we are using @see Result @Sealed class to donate the state and data for the different api response
             * like lading, failure, success etc
             *
             */
            when (list) {
                is Result.Success -> {
                    println(TAG + "plants " + list.data.size)
                }
                is Result.Loading -> println(TAG + "plants loading")
                is Result.ErrorWithData -> {

                    Toast.makeText(this@MainActivity, "error " + list.data.size, 1).show()
                    println(TAG + "plants error " + list.data.size + " " + list.exception.localizedMessage)
                }
                else -> {
                    println(TAG + "plants else")
                }
            }

        }

        /**
         * Initiating network call to fetch data
         * */
        viewModel.getRepo()


        findViewById<View>(R.id.btn).setOnClickListener {
            /**
             * start WorkManager to perform operation even if the app is closed
             * @see WorkManagerExample class
             * */
            viewModel.loadDataUsingWorkManager(applicationContext)
        }
        findViewById<View>(R.id.fab).setOnClickListener {
            val tourDataEntity = TourDataEntity()
            tourDataEntity.createdAt = Date().time as java.lang.Long
            tourDataEntity.title = "ss"

            println(TAG + "database entry created")
            /**
             * Here I am inserting a new data into TourDataEntity Table
             * lifecycleScope is provided by a support library
             * Note: A coroutine must be called from a Coroutine Scope
             * like : lifecycleScope, viewModelScope, GlobalScope or etc
             * lifecycleScope.launch(Dispatchers.IO) tells Coroutine scope to run coroutine in a
             * background thread i.e (Dispatchers.IO)
             * */
            lifecycleScope.launch(Dispatchers.IO) {
                println(TAG + contentDao.insertContents(tourDataEntity))
            }
        }

        /**
         * There are two ways to run coroutine
         * one is using launch another is using async.
         * launch is used when you don't expect result from an operation
         * async is used when you expect result from an operation
         * below is the different type of examples to see how coroutines works
         * Note: When you call join() on a launch or call await() on async
         * the thread will wait to finish the corresponding coroutine like synchronous execution
         * */
        lifecycleScope.launch {
            delay(2000)

            println("$TAG World! after 2000 ms")
        }
        lifecycleScope.launch {

            lifecycleScope.launch {
                delay(4000)
                println("$TAG World join after 4000 ms")
            }
            println("$TAG World2!")

            val t = lifecycleScope.async {
                doSomethingUsefulOne()
                // println("$TAG World!")
            }
            val tw = lifecycleScope.async {
                doSomethingUsefulTwo()
                // println("$TAG World!")
            }
            // wait for the result
            t.await()
            tw.await()
            //println("$TAG The answer is ${+ tw.await()}")
        }
        println("$TAG hello")


    }

    /***
     * suspend function can only be call from a coroutine or another suspend function
     * suspend function used to suspend the execution of a coroutine until it gets completed
     * */
    suspend fun doSomethingUsefulOne(): Int {
        delay(1000L) // pretend we are doing something useful here
        println("$TAG one")
        return 13
    }

    suspend fun doSomethingUsefulTwo(): Int {
        delay(1000L) // pretend we are doing something useful here
        println("$TAG two")
        return 26
    }
}
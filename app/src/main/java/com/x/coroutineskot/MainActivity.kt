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
    @Inject
    lateinit var contentDao: TourDao
    private val viewModel: DTViewModel by viewModels()
    var i: Int = 0
    val TAG = "testX "
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //    val viewModel = ViewModelProviders.of(this).get(DTViewModel::class.java)
        viewModel.growZone.asLiveData().observe(this) { list ->
            println(TAG + "ssf " + list.size)
        }

        viewModel.plants.observe(this) { list ->
            i++;
            when (list) {
                is Result.Success -> {
                    println(TAG + "plants " + list.data.size + " " + i)
                }
                is Result.Loading -> println(TAG + "plants loading")
                is Result.ErrorWithData -> {

                   Toast.makeText(this@MainActivity, "error " + list.data.size + " " + i, 1).show()
                    println(TAG + "plants error " + list.data.size + " " + list.exception.localizedMessage)
                }
                else -> {
                    println(TAG + "plants else")
                }
            }


            //  println(TAG + "ssf " + list.size)
        }
        viewModel.getRepo()

        findViewById<View>(R.id.fab).setOnClickListener{
            val tourDataEntity = TourDataEntity()
            tourDataEntity.createdAt = Date().time as java.lang.Long
            tourDataEntity.title = "ss"

            println(TAG + "cdfd")

            lifecycleScope.launch(Dispatchers.IO) {
                println(TAG + contentDao.insertContents(tourDataEntity))
            }
        }
        lifecycleScope.launch{
            delay(2000)
            println("$TAG World!")
        }
        lifecycleScope.launch{

            lifecycleScope.launch {
                delay(4000)
                println("$TAG World join")
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
            t.await()
            tw.await()
            //println("$TAG The answer is ${+ tw.await()}")
        }
//very imp it gives you time of code executed inside this        measureTimeMillis {  }
        println("$TAG hello")
    }

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
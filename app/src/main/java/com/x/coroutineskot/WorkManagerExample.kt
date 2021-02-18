package com.x.coroutineskot

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.delay

/****
 * WorkManager class to perform operations in background
 * even if app is closed
 * a Jetpack replacement for Service and JobScheduler classes
 *
 */
class WorkManagerExample(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    val TAG = "WORKMANAGER Example "
    override suspend fun doWork(): Result {
        println(TAG + " input data " + inputData.getString("data as parameter"))
        delay(5000)
        //dowork
        println(TAG + " Process successful after 5 sec")
        //see how to create work androidx.work.Data here https://www.codota.com/code/java/classes/androidx.work.Data$Builder
        //Result.success(androidx.work.Data)
        return Result.success()
    }

}
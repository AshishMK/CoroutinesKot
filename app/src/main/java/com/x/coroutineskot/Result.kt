package com.x.coroutineskot

import java.lang.Exception


/**
 * Sealed class which is used as a replacement of ENUM
 * to represent the different state of an operation or process.
 * unlike ENUMs, Sealed class can represent state with data
 * */
sealed class Result <out R>{
    data class Success<out T>(val data: T):Result<T>()
    object Loading:Result<Nothing>()
    data class Error(val exception: Throwable):Result<Nothing>()
    data class ErrorWithData<out T>(val exception: Throwable,val data: T):Result<T>()
}
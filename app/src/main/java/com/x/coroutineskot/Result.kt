package com.x.coroutineskot

import java.lang.Exception

sealed class Result <out R>{
    data class Success<out T>(val data: T):Result<T>()
    object Loading:Result<Nothing>()
    data class Error(val exception: Throwable):Result<Nothing>()
    data class ErrorWithData<out T>(val exception: Throwable,val data: T):Result<T>()
}
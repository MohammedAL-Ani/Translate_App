package com.example.mytest

sealed class Status<out T>{
    object Loading : Status<Nothing>()
    data class Fail(val message: String) : Status<Nothing>()
    data class Success<out T>(val data: T): Status<T>()
}
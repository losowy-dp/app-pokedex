package com.example.apppokedex.util

sealed class Resourses<T>(
    val data: T? = null,
    val message: String? = null
){
    class Success<T>(
        data: T
    ): Resourses<T>(data)

    class Error<T>(
        message: String?,
        data: T?=null

    ): Resourses<T>(data,message)

    class Loading<T>(
        data: T?=null
    ): Resourses<T>(data)
}

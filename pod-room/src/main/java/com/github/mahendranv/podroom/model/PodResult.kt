package com.github.mahendranv.podroom.model

sealed class PodResult<out T> {

    data class Failure(
        val errorCode: Int,
        val t: Throwable
    ) : PodResult<Nothing>()

    data class Success<T>(val data: T) : PodResult<T>()

}
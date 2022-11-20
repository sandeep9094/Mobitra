package org.digital.tracking.model

sealed class ResponseResult<out T> {
    object InProgress : ResponseResult<Nothing>()
    data class Success<out T>(val data: T) : ResponseResult<T>()
    data class Error(val errorMessage: String) : ResponseResult<Nothing>()
}

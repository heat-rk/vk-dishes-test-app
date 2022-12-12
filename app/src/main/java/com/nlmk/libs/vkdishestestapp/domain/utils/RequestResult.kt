package com.nlmk.libs.vkdishestestapp.domain.utils

sealed interface RequestResult<T : Any> {
    data class Success<T : Any>(val value: T) : RequestResult<T>
    data class Failure<T : Any>(val throwable: Throwable) : RequestResult<T>
}

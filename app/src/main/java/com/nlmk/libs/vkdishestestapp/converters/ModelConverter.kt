package com.nlmk.libs.vkdishestestapp.converters

interface ModelConverter<S : Any, T : Any> {
    fun convert(source: S): T
}

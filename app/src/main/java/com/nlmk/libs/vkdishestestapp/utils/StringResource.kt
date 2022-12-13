package com.nlmk.libs.vkdishestestapp.utils

import androidx.annotation.StringRes

sealed class StringResource {
    class ByString(val text: String?): StringResource()
    class ByRes(@StringRes val text: Int, vararg val formatArgs: Any): StringResource()
}

fun strRes(text: String?) =
    StringResource.ByString(text)

fun strRes(@StringRes text: Int, vararg formatArgs: Any) =
    StringResource.ByRes(text, *formatArgs)

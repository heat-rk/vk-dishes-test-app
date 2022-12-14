package com.nlmk.libs.vkdishestestapp.utils

import androidx.annotation.StringRes

sealed class StringResource {
    data class ByString(val text: String?): StringResource()

    class ByRes(@StringRes val text: Int, vararg val formatArgs: Any): StringResource() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ByRes

            if (text != other.text) return false
            if (!formatArgs.contentEquals(other.formatArgs)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = text
            result = 31 * result + formatArgs.contentHashCode()
            return result
        }
    }
}

fun strRes(text: String?) =
    StringResource.ByString(text)

fun strRes(@StringRes text: Int, vararg formatArgs: Any) =
    StringResource.ByRes(text, *formatArgs)

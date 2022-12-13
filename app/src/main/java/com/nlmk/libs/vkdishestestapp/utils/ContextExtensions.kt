package com.nlmk.libs.vkdishestestapp.utils

import android.content.Context

@Suppress("SpreadOperator")
fun Context.getString(stringResource: StringResource?) =
    when (stringResource) {
        is StringResource.ByRes -> getString(stringResource.text, *stringResource.formatArgs)
        is StringResource.ByString -> stringResource.text
        null -> null
    }

package com.nlmk.libs.vkdishestestapp.data.utils

import com.nlmk.libs.vkdishestestapp.domain.utils.RequestResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.ConnectException
import java.util.concurrent.CancellationException
import java.util.concurrent.ExecutionException

suspend fun <T : Any> safeDataCall(
    dispatcher: CoroutineDispatcher,
    call: suspend () -> T
) = try {
    withContext(dispatcher) {
        RequestResult.Success(call())
    }
} catch (e: ConnectException) {
    RequestResult.Failure(e)
} catch (e: IOException) {
    RequestResult.Failure(e)
} catch (e: CancellationException) {
    RequestResult.Failure(e)
} catch (e: ExecutionException) {
    RequestResult.Failure(e)
} catch (e: InterruptedException) {
    RequestResult.Failure(e)
} catch (e: IllegalArgumentException) {
    RequestResult.Failure(e)
}

package io.github.nandindustries.sdk.reactive

import io.github.nandindustries.sdk.reactive.model.TransactionCompletionResult
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

internal class TransactionCompletionPublisher {
    val result = MutableSharedFlow<TransactionCompletionResult>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    fun broadcastTransactionCompletion(transactionCompletionResult: TransactionCompletionResult) {
        result.tryEmit(transactionCompletionResult)
    }
}

package io.github.nandindustries.sdk.reactive

import io.github.nandindustries.sdk.reactive.api.TransactionCompletionStream
import io.github.nandindustries.sdk.reactive.model.TransactionCompletionResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asSharedFlow

internal class TransactionCompletionStreamImpl(
    private val transactionCompletionPublisher: TransactionCompletionPublisher,
) : TransactionCompletionStream {

    override fun onTransactionCompletionResult(): Flow<TransactionCompletionResult> =
        transactionCompletionPublisher.result.asSharedFlow()
}

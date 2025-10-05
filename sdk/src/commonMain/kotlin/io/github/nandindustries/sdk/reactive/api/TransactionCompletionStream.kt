package io.github.nandindustries.sdk.reactive.api

import io.github.nandindustries.sdk.reactive.model.TransactionCompletionResult
import kotlinx.coroutines.flow.Flow

interface TransactionCompletionStream {
    fun onTransactionCompletionResult(): Flow<TransactionCompletionResult>
}

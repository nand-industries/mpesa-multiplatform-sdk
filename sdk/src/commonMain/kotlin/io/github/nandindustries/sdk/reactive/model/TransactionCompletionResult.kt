package io.github.nandindustries.sdk.reactive.model

import io.github.nandindustries.sdk.domain.entity.CustomerToBusinessTransactionResult

sealed interface TransactionCompletionResult {

    data class C2BTransactionCompleted(
        val result: CustomerToBusinessTransactionResult,
    ) : TransactionCompletionResult

    data object C2BTransactionCancelledBeforeStarted : TransactionCompletionResult
}

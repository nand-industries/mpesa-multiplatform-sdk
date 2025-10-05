package io.github.nandindustries.sdk.reactive.ext

import io.github.nandindustries.sdk.domain.entity.CustomerToBusinessTransactionResult
import io.github.nandindustries.sdk.reactive.model.TransactionCompletionResult

fun isSuccessfulC2BTransaction(result: TransactionCompletionResult): Boolean = when (result) {
    is TransactionCompletionResult.C2BTransactionCompleted ->
        result.result is CustomerToBusinessTransactionResult.SuccessfulTransaction

    TransactionCompletionResult.C2BTransactionCancelledBeforeStarted -> false
}

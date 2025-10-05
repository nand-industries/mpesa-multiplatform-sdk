package io.github.nandindustries.sdk.reactive

import io.github.nandindustries.sdk.MpesaMultiplatformSdk
import io.github.nandindustries.sdk.reactive.model.TransactionCompletionResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * Observes transaction completion events from the SDK and notifies the given [onResult] callback.
 *
 * This is a convenience wrapper used by the iOS to listen for transaction results.
 */
fun observeTransactionCompletion(onResult: (TransactionCompletionResult) -> Unit) {
    CoroutineScope(Dispatchers.Main).launch {
        MpesaMultiplatformSdk.transactionCompletionStream.onTransactionCompletionResult()
            .collectLatest { result ->
                onResult(result)
            }
    }
}

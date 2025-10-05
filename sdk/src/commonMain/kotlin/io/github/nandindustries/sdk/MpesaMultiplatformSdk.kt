package io.github.nandindustries.sdk

import io.github.nandindustries.sdk.di.DependencyRegistry
import io.github.nandindustries.sdk.di.SdkDependencyContainer
import io.github.nandindustries.sdk.reactive.api.TransactionCompletionStream

/**
 * Public entry-point that exposes the SDK dependencies available to host applications.
 */
interface MpesaMultiplatformSdkComponent {
    val transactionCompletionStream: TransactionCompletionStream
}

object MpesaMultiplatformSdk : MpesaMultiplatformSdkComponent {
    override val transactionCompletionStream: TransactionCompletionStream
        get() = DependencyRegistry.current().transactionCompletionStream

    internal fun replaceDependencyContainer(container: SdkDependencyContainer) {
        DependencyRegistry.update(container)
    }
}

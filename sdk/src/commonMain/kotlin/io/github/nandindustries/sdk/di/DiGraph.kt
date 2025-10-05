package io.github.nandindustries.sdk.di

import io.github.nandindustries.sdk.data.remote.HttpClientFactory
import io.github.nandindustries.sdk.domain.usecase.CustomerToBusinessUseCase
import io.github.nandindustries.sdk.domain.usecase.CustomerToBusinessUseCaseImpl
import io.github.nandindustries.sdk.reactive.TransactionCompletionPublisher
import io.github.nandindustries.sdk.reactive.TransactionCompletionStreamImpl
import io.github.nandindustries.sdk.reactive.api.TransactionCompletionStream
import io.github.nandindustries.sdk.data.remote.mpesa.MpesaClientImpl
import kotlin.concurrent.Volatile

/**
 * Provides all dependencies required by the SDK without leaking any DI framework to consumers.
 */
internal interface SdkDependencyContainer {
    val transactionCompletionPublisher: TransactionCompletionPublisher
    val customerToBusinessUseCase: CustomerToBusinessUseCase
    val transactionCompletionStream: TransactionCompletionStream
}

internal class DefaultSdkDependencyContainer(
    private val httpClientFactory: HttpClientFactory = HttpClientFactory(),
) : SdkDependencyContainer {

    override val transactionCompletionPublisher: TransactionCompletionPublisher by lazy {
        TransactionCompletionPublisher()
    }

    private val mpesaClient by lazy {
        MpesaClientImpl(httpClientFactory)
    }

    override val customerToBusinessUseCase: CustomerToBusinessUseCase by lazy {
        CustomerToBusinessUseCaseImpl(mpesaClient)
    }

    override val transactionCompletionStream: TransactionCompletionStream by lazy {
        TransactionCompletionStreamImpl(transactionCompletionPublisher)
    }
}

internal object DependencyRegistry {
    @Volatile
    private var container: SdkDependencyContainer = DefaultSdkDependencyContainer()

    fun current(): SdkDependencyContainer = container

    fun update(container: SdkDependencyContainer) {
        this.container = container
    }
}

internal object SdkInternalDependencies {
    private val container: SdkDependencyContainer
        get() = DependencyRegistry.current()

    val transactionCompletionPublisher: TransactionCompletionPublisher
        get() = container.transactionCompletionPublisher

    val customerToBusinessUseCase: CustomerToBusinessUseCase
        get() = container.customerToBusinessUseCase

    val transactionCompletionStream: TransactionCompletionStream
        get() = container.transactionCompletionStream
}

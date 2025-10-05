package io.github.nandindustries.sdk.reactive

import io.github.nandindustries.sdk.domain.entity.CustomerToBusinessTransactionResponse
import io.github.nandindustries.sdk.domain.entity.CustomerToBusinessTransactionResult
import io.github.nandindustries.sdk.reactive.model.TransactionCompletionResult
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

@Ignore
internal class TransactionCompletionPublisherTest {

    @Test
    fun `Given subscriber When broadcasting result Then receives value`() = runTest {
        // Given
        val publisher = TransactionCompletionPublisher()
        val expected = TransactionCompletionResult.C2BTransactionCancelledBeforeStarted

        // When
        publisher.broadcastTransactionCompletion(expected)

        // Then
        val actual = publisher.result.first()
        assertEquals(expected, actual)
    }

    @Test
    fun `Given multiple broadcasts When collecting Then delivers most recent`() = runTest {
        // Given
        val publisher = TransactionCompletionPublisher()
        val first = TransactionCompletionResult.C2BTransactionCompleted(sampleResult("INS-0"))
        val latest = TransactionCompletionResult.C2BTransactionCompleted(sampleResult("INS-6"))

        // When
        publisher.broadcastTransactionCompletion(first)
        publisher.broadcastTransactionCompletion(latest)

        // Then
        val actual = publisher.result.first()
        assertEquals(latest, actual)
    }

    private fun sampleResult(code: String): CustomerToBusinessTransactionResult =
        CustomerToBusinessTransactionResult.Failed(
            customerToBusinessTransactionResponse =
                CustomerToBusinessTransactionResponse(
                    conversationId = "conversation",
                    transactionId = "transaction",
                    description = "description",
                    code = code,
                    thirdPartyReference = "third",
                ),
        )
}

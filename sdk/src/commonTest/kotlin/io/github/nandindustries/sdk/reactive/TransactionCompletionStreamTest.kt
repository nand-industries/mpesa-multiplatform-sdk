package io.github.nandindustries.sdk.reactive

import io.github.nandindustries.sdk.reactive.model.TransactionCompletionResult
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

@Ignore
internal class TransactionCompletionStreamTest {

    @Test
    fun `Given publisher emission When observing stream Then receives completion`() = runTest {
        // Given
        val publisher = TransactionCompletionPublisher()
        val stream = TransactionCompletionStreamImpl(publisher)
        val expected = TransactionCompletionResult.C2BTransactionCancelledBeforeStarted

        // When
        publisher.broadcastTransactionCompletion(expected)

        // Then
        val actual = stream.onTransactionCompletionResult().first()
        assertEquals(expected, actual)
    }
}

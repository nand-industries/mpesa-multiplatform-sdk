package io.github.nandindustries.sdk.ui.component.transactiondetailscard

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.nandindustries.sdk.resources.Res
import io.github.nandindustries.sdk.resources.amount_label
import io.github.nandindustries.sdk.resources.customer_msisdn_label
import io.github.nandindustries.sdk.resources.transaction_reference_label
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.abs

internal data class TransactionDetail(val label: String, val value: String)

@Composable
internal fun TransactionDetailsMinimalCard(
    data: TransactionDetailsCardData,
    extraDetails: List<TransactionDetail> = emptyList(),
    modifier: Modifier = Modifier,
) {
    val formattedAmount = formatAmount(data.amount)
    val formattedMsisdn = formatPhoneNumber(data.customerMsisdn)
    val details = listOf(
        TransactionDetail(
            label = stringResource(Res.string.amount_label),
            value = formattedAmount,
        ),
        TransactionDetail(
            label = stringResource(Res.string.transaction_reference_label),
            value = data.transactionReference,
        ),
        TransactionDetail(
            label = stringResource(Res.string.customer_msisdn_label),
            value = formattedMsisdn,
        ),
    ) + extraDetails

    OutlinedCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
    ) {
        Column {
            details.forEachIndexed { index, detail ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = detail.label,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                    )
                    Text(
                        text = detail.value,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Medium,
                        ),
                    )
                }
                if (index < details.lastIndex) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                            .height(1.dp)
                            .clip(RoundedCornerShape(50))
                            .background(MaterialTheme.colorScheme.outline),
                    )
                }
            }
        }
    }
}

private fun formatAmount(amount: Double): String {
    val integerPart = abs(amount).toLong()
    val decimalPart = ((abs(amount) * 100).toLong() % 100).toString().padStart(2, '0')
    val integerWithSeparators = integerPart.toString().reversed()
        .chunked(3)
        .joinToString(",")
        .reversed()
    return if (amount < 0) "-$integerWithSeparators.$decimalPart" else "$integerWithSeparators.$decimalPart"
}

private fun formatPhoneNumber(rawNumber: String): String {
    val digits = rawNumber.filter { it.isDigit() }
    return try {
        when {
            digits.length in 9..11 -> {
                val operatorCode = digits.substring(0, 2)
                val firstPart = digits.substring(2, 5)
                val secondPart = digits.substring(5, digits.length.coerceAtMost(9))
                "(258) $operatorCode $firstPart $secondPart"
            }

            digits.length >= 12 -> {
                val countryCode = digits.substring(0, 3)
                val operatorCode = digits.substring(3, 5)
                val firstPart = digits.substring(5, 8)
                val secondPart = digits.substring(8, 12)
                "($countryCode) $operatorCode $firstPart $secondPart"
            }

            else -> digits
        }
    } catch (_: Exception) {
        digits // Fallback in case of any error
    }
}

@Preview
@Composable
private fun TransactionDetailsMinimalCardPreview() {
    TransactionDetailsMinimalCard(
        data = TransactionDetailsCardData(
            amount = 1250.0,
            transactionReference = "TRX-20250622-987",
            customerMsisdn = "258841234567",
        ),
        modifier = Modifier.padding(16.dp),
    )
}
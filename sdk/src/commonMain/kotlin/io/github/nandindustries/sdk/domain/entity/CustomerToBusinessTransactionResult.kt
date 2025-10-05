package io.github.nandindustries.sdk.domain.entity

import io.github.nandindustries.sdk.resources.Res
import io.github.nandindustries.sdk.resources.transaction_cancelled_description
import io.github.nandindustries.sdk.resources.transaction_cancelled_subtitle
import io.github.nandindustries.sdk.resources.transaction_cancelled_title
import io.github.nandindustries.sdk.resources.transaction_failed_description
import io.github.nandindustries.sdk.resources.transaction_failed_subtitle
import io.github.nandindustries.sdk.resources.transaction_failed_title
import io.github.nandindustries.sdk.resources.transaction_insufficient_balance_description
import io.github.nandindustries.sdk.resources.transaction_insufficient_balance_subtitle
import io.github.nandindustries.sdk.resources.transaction_insufficient_balance_title
import io.github.nandindustries.sdk.resources.transaction_internal_error_description
import io.github.nandindustries.sdk.resources.transaction_internal_error_subtitle
import io.github.nandindustries.sdk.resources.transaction_internal_error_title
import io.github.nandindustries.sdk.resources.transaction_invalid_amount_description
import io.github.nandindustries.sdk.resources.transaction_invalid_amount_subtitle
import io.github.nandindustries.sdk.resources.transaction_invalid_amount_title
import io.github.nandindustries.sdk.resources.transaction_invalid_msisdn_description
import io.github.nandindustries.sdk.resources.transaction_invalid_msisdn_subtitle
import io.github.nandindustries.sdk.resources.transaction_invalid_msisdn_title
import io.github.nandindustries.sdk.resources.transaction_success_description
import io.github.nandindustries.sdk.resources.transaction_success_subtitle
import io.github.nandindustries.sdk.resources.transaction_success_title
import io.github.nandindustries.sdk.resources.transaction_timeout_description
import io.github.nandindustries.sdk.resources.transaction_timeout_subtitle
import io.github.nandindustries.sdk.resources.transaction_timeout_title
import io.github.nandindustries.sdk.resources.transaction_unknown_error_description
import io.github.nandindustries.sdk.resources.transaction_unknown_error_subtitle
import io.github.nandindustries.sdk.resources.transaction_unknown_error_title
import org.jetbrains.compose.resources.StringResource

sealed class CustomerToBusinessTransactionResult(
    open val titleRes: StringResource,
    open val subtitleRes: StringResource,
    open val descriptionRes: StringResource,
    open val customerToBusinessTransactionResponse: CustomerToBusinessTransactionResponse?,
) {
    data class SuccessfulTransaction(
        override val customerToBusinessTransactionResponse: CustomerToBusinessTransactionResponse,
    ) : CustomerToBusinessTransactionResult(
        titleRes = Res.string.transaction_success_title,
        subtitleRes = Res.string.transaction_success_subtitle,
        descriptionRes = Res.string.transaction_success_description,
        customerToBusinessTransactionResponse = customerToBusinessTransactionResponse,
    )

    data class InternalError(override val customerToBusinessTransactionResponse: CustomerToBusinessTransactionResponse) :
        CustomerToBusinessTransactionResult(
            titleRes = Res.string.transaction_internal_error_title,
            subtitleRes = Res.string.transaction_internal_error_subtitle,
            descriptionRes = Res.string.transaction_internal_error_description,
            customerToBusinessTransactionResponse = customerToBusinessTransactionResponse,
        )

    data class CancelledTransaction(override val customerToBusinessTransactionResponse: CustomerToBusinessTransactionResponse) :
        CustomerToBusinessTransactionResult(
            titleRes = Res.string.transaction_cancelled_title,
            subtitleRes = Res.string.transaction_cancelled_subtitle,
            descriptionRes = Res.string.transaction_cancelled_description,
            customerToBusinessTransactionResponse = customerToBusinessTransactionResponse,
        )

    data class TimeoutTransaction(override val customerToBusinessTransactionResponse: CustomerToBusinessTransactionResponse) :
        CustomerToBusinessTransactionResult(
            titleRes = Res.string.transaction_timeout_title,
            subtitleRes = Res.string.transaction_timeout_subtitle,
            descriptionRes = Res.string.transaction_timeout_description,
            customerToBusinessTransactionResponse = customerToBusinessTransactionResponse,
        )

    data class InvalidAmount(override val customerToBusinessTransactionResponse: CustomerToBusinessTransactionResponse) :
        CustomerToBusinessTransactionResult(
            titleRes = Res.string.transaction_invalid_amount_title,
            subtitleRes = Res.string.transaction_invalid_amount_subtitle,
            descriptionRes = Res.string.transaction_invalid_amount_description,
            customerToBusinessTransactionResponse = customerToBusinessTransactionResponse,
        )

    data class InsufficientBalance(override val customerToBusinessTransactionResponse: CustomerToBusinessTransactionResponse) :
        CustomerToBusinessTransactionResult(
            titleRes = Res.string.transaction_insufficient_balance_title,
            subtitleRes = Res.string.transaction_insufficient_balance_subtitle,
            descriptionRes = Res.string.transaction_insufficient_balance_description,
            customerToBusinessTransactionResponse = customerToBusinessTransactionResponse,
        )

    data class InvalidMsisdn(override val customerToBusinessTransactionResponse: CustomerToBusinessTransactionResponse) :
        CustomerToBusinessTransactionResult(
            titleRes = Res.string.transaction_invalid_msisdn_title,
            subtitleRes = Res.string.transaction_invalid_msisdn_subtitle,
            descriptionRes = Res.string.transaction_invalid_msisdn_description,
            customerToBusinessTransactionResponse = customerToBusinessTransactionResponse,
        )

    data class Failed(override val customerToBusinessTransactionResponse: CustomerToBusinessTransactionResponse) :
        CustomerToBusinessTransactionResult(
            titleRes = Res.string.transaction_failed_title,
            subtitleRes = Res.string.transaction_failed_subtitle,
            descriptionRes = Res.string.transaction_failed_description,
            customerToBusinessTransactionResponse = customerToBusinessTransactionResponse,
        )

    data class UnknownError(val error: String?) : CustomerToBusinessTransactionResult(
        titleRes = Res.string.transaction_unknown_error_title,
        subtitleRes = Res.string.transaction_unknown_error_subtitle,
        descriptionRes = Res.string.transaction_unknown_error_description,
        customerToBusinessTransactionResponse = null,
    )
}
package io.github.nandindustries.sdk.presentation.uistate

import org.jetbrains.compose.resources.StringResource

internal data class InputTransactionDetailsUiState(
    val serviceProviderCode: String,
    val maxAmount: Long = 0,
    val areFieldsEnabled: Boolean = true,
    val amountValidationErrorMessage: StringResource? = null,
    val phoneNumberValidationErrorMessage: StringResource? = null,
    val isContinueButtonEnabled: Boolean = false,
    val isEditableAmount: Boolean = true,
)
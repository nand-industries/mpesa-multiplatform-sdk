package io.github.nandindustries.sdk.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import io.github.nandindustries.sdk.config.MpesaMultiplatformSdkInitializer
import io.github.nandindustries.sdk.presentation.uistate.InputTransactionDetailsUiState
import io.github.nandindustries.sdk.reactive.TransactionCompletionPublisher
import io.github.nandindustries.sdk.reactive.model.TransactionCompletionResult
import io.github.nandindustries.sdk.resources.Res
import io.github.nandindustries.sdk.resources.error_incomplete_phone_number
import io.github.nandindustries.sdk.resources.error_invalid_phone_number
import io.github.nandindustries.sdk.resources.error_phone_prefix
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import org.jetbrains.compose.resources.StringResource

internal class InputTransactionDetailsViewModel(
    private val defaultAmount: String? = null,
    private val defaultPhoneNumber: String? = null,
    private val transactionReference: String,
    private val thirdPartyReference: String,
    private val transactionCompletionPublisher: TransactionCompletionPublisher,
) : ViewModel() {
    private val _state: MutableStateFlow<InputTransactionDetailsUiState> =
        MutableStateFlow(
            value = InputTransactionDetailsUiState(
                maxAmount = MAXIMUM_INPUT_AMOUNT,
                serviceProviderCode = MpesaMultiplatformSdkInitializer.getConfig().serviceProviderCode,
            ),
        )
    val state = _state.asStateFlow()

    private val _amount = mutableStateOf("")
    val amount: State<String> get() = _amount

    private val _phoneNumber = mutableStateOf("")
    val phoneNumber: State<String> get() = _phoneNumber

    private val _destination: Channel<Destination> = Channel(capacity = Channel.Factory.BUFFERED)
    val destination = _destination.receiveAsFlow()

    private var isAmountValidationCorrect: Boolean = false
    private var isPhoneValidationCorrect: Boolean = false

    init {
        setupDefaultAmount()
        setupDefaultPhoneNumber()
    }

    fun onAmountValueChange(newAmount: String) {
        validateAndSanitizeAmount(newAmount)
    }

    fun onPhoneNumberChange(newPhoneNumber: String) {
        validateAndSanitizePhoneNumber(newPhoneNumber)
    }

    fun onContinueButtonClicked() {
        _destination.trySend(
            Destination.ProcessTransaction(
                amount = getAmount().toString(),
                customerMsisdn = getPhoneNumber(),
                transactionReference = transactionReference,
                thirdPartyReference = thirdPartyReference,
            ),
        )
    }

    fun onNavigateBack() {
        transactionCompletionPublisher.broadcastTransactionCompletion(
            TransactionCompletionResult.C2BTransactionCancelledBeforeStarted,
        )
        _destination.trySend(Destination.BackNavigation)
    }

    private fun validateAndSanitizeAmount(amount: String) {
        val digitsOnly = amount.filter { it.isDigit() }

        if (digitsOnly.isEmpty()) {
            updateAmount("")
            return
        }

        val sanitizedAmount =
            digitsOnly.toLongOrNull()?.coerceAtMost(MAXIMUM_INPUT_AMOUNT) ?: MAXIMUM_INPUT_AMOUNT
        updateAmount(sanitizedAmount.toString())
    }

    private fun validateAndSanitizePhoneNumber(phoneNumber: String) {
        val digitsOnly = phoneNumber.filter { it.isDigit() }
        val startsWithVodacomPrefix = digitsOnly.startsWith("84") || digitsOnly.startsWith("85")
        val hasEnoughDigits = digitsOnly.length == MAXIMUM_PHONE_NUMBER_DIGITS

        val errorMessage = when {
            !startsWithVodacomPrefix && !hasEnoughDigits -> Res.string.error_invalid_phone_number
            !startsWithVodacomPrefix -> Res.string.error_phone_prefix
            !hasEnoughDigits -> Res.string.error_incomplete_phone_number
            else -> null
        }

        isPhoneValidationCorrect = startsWithVodacomPrefix && hasEnoughDigits

        if (digitsOnly.length <= MAXIMUM_PHONE_NUMBER_DIGITS) {
            _phoneNumber.value = digitsOnly
            updateContinueButtonState(errorMessage)
        }
    }

    private fun getAmount() = _amount.value.toLong() / 100.0

    private fun getPhoneNumber() = MOZAMBIQUE_COUNTRY_CODE + _phoneNumber.value

    private fun setupDefaultAmount() {
        defaultAmount?.let { amount ->
            val amountInMinorUnits = requireNotNull(amount.toMinorUnitsOrNull()) {
                "Default amount must be a valid numeric value with up to two decimal places."
            }
            require(amountInMinorUnits <= MAXIMUM_INPUT_AMOUNT) {
                "Default amount exceeds maximum allowed amount. Maximum allowed amount is ${MAXIMUM_INPUT_AMOUNT.toExactAmount()} MZN."
            }
            require(amountInMinorUnits >= MINIMUM_VALIDATION_AMOUNT) {
                "Default amount is below the minimum allowed amount. Minimum allowed amount is ${MINIMUM_VALIDATION_AMOUNT.toExactAmount()} MZN."
            }
            _state.update { it.copy(isEditableAmount = false) }
            updateAmount(amountInMinorUnits.toString())
        }
    }

    private fun setupDefaultPhoneNumber() {
        defaultPhoneNumber?.let { phoneNumber ->
            require(phoneNumber.length == MAXIMUM_PHONE_NUMBER_DIGITS) {
                "Default phone number must be exactly $MAXIMUM_PHONE_NUMBER_DIGITS digits."
            }
            validateAndSanitizePhoneNumber(phoneNumber)
        }
    }

    private fun updateAmount(newValue: String) {
        _amount.value = newValue
        isAmountValidationCorrect =
            newValue.toLongOrNull()?.let { it >= MINIMUM_VALIDATION_AMOUNT } ?: false
        updateContinueButtonState()
    }

    private fun updateContinueButtonState(phoneErrorMessage: StringResource? = _state.value.phoneNumberValidationErrorMessage) {
        _state.update {
            it.copy(
                isContinueButtonEnabled = isAmountValidationCorrect && isPhoneValidationCorrect,
                phoneNumberValidationErrorMessage = phoneErrorMessage,
            )
        }
    }

    private fun String.toMinorUnitsOrNull(): Long? {
        val normalized = trim().replace(",", ".")
        if (normalized.isEmpty()) return null

        val pattern = Regex("""^\d+(\.\d{0,2})?$""")
        if (!pattern.matches(normalized)) return null

        val parts = normalized.split('.', limit = 2)
        val wholePart = parts.getOrNull(0).orEmpty()
        val fractionPart = parts.getOrNull(1).orEmpty().padEnd(2, '0')

        val integralValue = wholePart.toLongOrNull() ?: return null
        val fractionalValue = fractionPart.take(2).toLongOrNull() ?: return null

        return integralValue * 100 + fractionalValue
    }

    private fun Long.toExactAmount() = this / 100.0

    sealed interface Destination {
        data class ProcessTransaction(
            val amount: String,
            val customerMsisdn: String,
            val transactionReference: String,
            val thirdPartyReference: String,
        ) : Destination

        data object BackNavigation : Destination
    }

    companion object {
        private const val MAXIMUM_INPUT_AMOUNT = 100000L * 100
        private const val MINIMUM_VALIDATION_AMOUNT = 5L * 100
        private const val MAXIMUM_PHONE_NUMBER_DIGITS = 9
        private const val MOZAMBIQUE_COUNTRY_CODE = "258"
    }
}
package io.github.nandindustries.demo.presentation.uistate

import io.github.nandindustries.demo.fakedata.Product

data class HomeUiState(
    val products: List<Product> = emptyList(),
    val showFailureDialog: Boolean = false,
    val showSuccessMessage: Boolean = false
) {
    val totalAmount: Double = products.sumOf { it.price }
    val totalAmountFormatted: String = "%.2f".format(totalAmount)
}

sealed interface HomeEffect {
    data class ToastRemoved(val productName: String) : HomeEffect
}

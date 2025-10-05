package io.github.nandindustries.demo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.nandindustries.demo.fakedata.Product
import io.github.nandindustries.demo.fakedata.productList
import io.github.nandindustries.demo.presentation.uistate.HomeEffect
import io.github.nandindustries.demo.presentation.uistate.HomeUiState
import io.github.nandindustries.sdk.MpesaMultiplatformSdk
import io.github.nandindustries.sdk.domain.entity.CustomerToBusinessTransactionResult
import io.github.nandindustries.sdk.reactive.model.TransactionCompletionResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val initialProducts: List<Product> = productList,
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState(products = initialProducts.toList()))
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    private val _effects = Channel<HomeEffect>(Channel.BUFFERED)
    val effects: Flow<HomeEffect> = _effects.receiveAsFlow()

    init {
        viewModelScope.launch {
            MpesaMultiplatformSdk.transactionCompletionStream
                .onTransactionCompletionResult()
                .collectLatest { result ->
                    when (result) {
                        is TransactionCompletionResult.C2BTransactionCancelledBeforeStarted -> {
                            _state.update { it.copy(showFailureDialog = true) }
                        }
                        is TransactionCompletionResult.C2BTransactionCompleted -> {
                            val success = result.result is CustomerToBusinessTransactionResult.SuccessfulTransaction
                            if (success) {
                                // Clear products on success and show green banner
                                productList.clear()
                                _state.update { it.copy(products = emptyList(), showSuccessMessage = true) }
                            } else {
                                _state.update { it.copy(showFailureDialog = true) }
                            }
                        }
                    }
                }
        }
    }

    fun removeProduct(product: Product) {
        productList.remove(product) // keep your shared list in sync if you need it
        _state.update { it.copy(products = it.products.filterNot { p -> p.id == product.id }) }
        viewModelScope.launch {
            _effects.send(HomeEffect.ToastRemoved(product.name))
        }
    }

    fun consumeFailureDialog() {
        _state.update { it.copy(showFailureDialog = false) }
    }

    fun consumeSuccessMessage() {
        _state.update { it.copy(showSuccessMessage = false) }
    }
}

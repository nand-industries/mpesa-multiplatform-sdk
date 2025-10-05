package io.github.nandindustries.demo.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import io.github.nandindustries.demo.presentation.uistate.HomeEffect
import io.github.nandindustries.demo.presentation.viewmodel.HomeViewModel
import io.github.nandindustries.demo.ui.component.ProductCard

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is HomeEffect.ToastRemoved ->
                    Toast.makeText(context, "${effect.productName} removido do carrinho", Toast.LENGTH_SHORT).show()
            }
        }
    }
    Scaffold(
        topBar = {
            Text(
                text = "M-Pesa Multiplatform Demo - Android",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 32.dp, bottom = 8.dp),
            )
        },
        modifier = modifier,
    ) { paddingValues ->

        if (state.showFailureDialog) {
            AlertDialog(
                onDismissRequest = { viewModel.consumeFailureDialog() },
                confirmButton = {
                    TextButton(onClick = { viewModel.consumeFailureDialog() }) { Text("OK") }
                },
                title = { Text("Pagamento falhou") },
                text = { Text("O pagamento falhou") },
            )
        }
        Column(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (state.showSuccessMessage) {
                Text(
                    text = "Pagamento efetuado com sucesso!",
                    color = Color(0xFF2E7D32),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    textAlign = TextAlign.Center,
                )
            }
            if (state.products.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("carinho de compras vazio")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                ) {
                    itemsIndexed(state.products) { _, item ->
                        ProductCard(
                            item,
                            onRemoveClick = { product -> viewModel.removeProduct(product) },
                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                        )
                    }
                }
            }
            TextButton(
                onClick = { navController.navigate("payment/${state.totalAmountFormatted}") },
                enabled = state.products.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) { Text("Pagar com M-Pesa") }
        }
    }
}

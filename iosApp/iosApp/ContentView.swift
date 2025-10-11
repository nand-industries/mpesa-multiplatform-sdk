import UIKit
import SwiftUI
import sdk

struct ComposePaymentView: UIViewControllerRepresentable {
    let amount: String
    let onDismiss: () -> Void

    func makeUIViewController(context: Context) -> UIViewController {
        MainKt.MainViewController(
            businessName: "Nand Industries",
            businessLogoUrl: "https://avatars.githubusercontent.com/u/191469385?s=200&v=4",
            transactionReference: "TESTIOS12345",
            thirdPartyReference: String(NSUUID().uuidString.prefix(5)),
            defaultAmount: amount,
            defaultPhoneNumber: "851234567",
            onDismissInputTransactionDetailsStep: onDismiss
        )
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {
    }
}

struct ContentView: View {
    @State private var products: [Product] = sampleProducts
    @State private var showingPayment = false
    @State private var showAlert = false
    @State private var alertMessage = ""
    @State private var paymentSuccess: Bool? = nil

    var body: some View {
        VStack {
            Text("M-Pesa Multiplatform Demo - iOS")
                .font(.title)
                .frame(maxWidth: .infinity, alignment: .center)
                .padding(.top, 32)

            if paymentSuccess == true {
                Text("Pagamento efetuado com sucesso!")
                    .foregroundColor(Color(red: 0.18, green: 0.49, blue: 0.2))
            }

            if products.isEmpty {
                Text("carinho de compras vazio")
                    .padding()
            } else {
                List {
                    ForEach(products) { product in
                        ProductRow(product: product, onRemove: remove)
                    }
                }
            }

            Button("Pagar com M-Pesa") {
                showingPayment = true
            }
            .padding()
            .disabled(products.isEmpty)
        }
        .sheet(isPresented: $showingPayment) {
            let total = products.reduce(0.0) { $0 + $1.price }
            let formattedTotal = String(format: "%.2f", total)
            ComposePaymentView(amount: formattedTotal) {
                showingPayment = false
            }
            .ignoresSafeArea()
        }
        .alert(alertMessage, isPresented: $showAlert) {
            Button("OK", role: .cancel) {
            }
        }
        .onAppear {
            TransactionCompletionObserverKt.observeTransactionCompletion { result in
                if result is TransactionCompletionResultC2BTransactionCancelledBeforeStarted {
                    alertMessage = "Transação cancelada antes de iniciar"
                    paymentSuccess = false
                    showAlert = true
                } else if let completed = result as? TransactionCompletionResultC2BTransactionCompleted {
                    if TransactionCompletionResultExtensionsKt.isSuccessfulC2BTransaction(result: completed) {
                        paymentSuccess = true
                        products.removeAll()
                    } else {
                        alertMessage = "O pagamento falhou"
                        paymentSuccess = false
                        showAlert = true
                    }
                }
            }
        }
    }

    private func remove(_ product: Product) {
        products.removeAll {
            $0 == product
        }
        alertMessage = "\(product.name) removido do carrinho"
        showAlert = true
    }
}

struct ProductRow: View {
    let product: Product
    let onRemove: (Product) -> Void

    var body: some View {
        HStack {
            Rectangle()
                .fill(Color.random)
                .frame(width: 100, height: 100)
                .cornerRadius(16)
            VStack(alignment: .leading) {
                Text(product.name)
                    .font(.headline)
                Text(product.description)
                    .font(.caption)
                    .foregroundColor(.gray)
                    .lineLimit(2)
                Spacer()
                HStack {
                    Text(String(format: "%.2f MZN", product.price))
                        .fontWeight(.semibold)
                        .foregroundColor(Color(red: 0.18, green: 0.49, blue: 0.2))
                    Spacer()
                    Button(action: { onRemove(product) }) {
                        Image(systemName: "trash")
                    }
                }
            }
            .padding(.leading, 8)
        }
        .padding(.vertical, 8)
    }
}

private extension Color {
    static var random: Color {
        let colors: [Color] = [
            Color(red: 0.94, green: 0.60, blue: 0.60),
            Color(red: 0.56, green: 0.79, blue: 0.98),
            Color(red: 0.65, green: 0.84, blue: 0.65),
            Color(red: 1.00, green: 0.96, blue: 0.62),
            Color(red: 0.81, green: 0.58, blue: 0.85),
            Color(red: 1.00, green: 0.80, blue: 0.50)
        ]
        return colors.randomElement()!
    }
}

struct Product: Identifiable, Equatable {
    let id = UUID()
    let name: String
    let description: String
    let price: Double
}

let sampleProducts: [Product] = [
    Product(
        name: "Clean Code",
        description: "Robert C. Martin — Práticas para escrever código limpo, legível e de fácil manutenção.",
        price: 1800.00
    ),
    Product(
        name: "The Pragmatic Programmer",
        description: "Andrew Hunt e David Thomas — Guia clássico para melhorar a forma de pensar e trabalhar como desenvolvedor.",
        price: 2000.00
    ),
    Product(
        name: "Design Patterns: Elements of Reusable Object-Oriented Software",
        description: "Erich Gamma et al. — Padrões de projeto fundamentais para arquitetura de software orientado a objetos.",
        price: 2200.00
    ),
    Product(
        name: "Kotlin in Action",
        description: "Dmitry Jemerov e Svetlana Isakova — Abordagem prática para dominar o Kotlin em aplicações modernas.",
        price: 1700.00
    ),
    Product(
        name: "Effective Java",
        description: "Joshua Bloch — Melhores práticas para criar código Java robusto, eficiente e sustentável.",
        price: 1900.00
    )
]

import Foundation

struct Product: Identifiable, Equatable {
    let id: Int
    let name: String
    let description: String
    let price: Double
}

let sampleProducts: [Product] = [
    Product(id: 1, name: "Camiseta Clássica", description: "Camiseta de algodão 100% com corte moderno.", price: 79.90),
    Product(id: 2, name: "Tênis Esportivo", description: "Ideal para corrida ou ginásio.", price: 249.99),
    Product(id: 3, name: "Mochila Casual", description: "Mochila leve e resistente para o quotidiano.", price: 129.50),
    Product(id: 4, name: "Fone Bluetooth", description: "Som de alta qualidade com cancelamento de ruído.", price: 199.90),
    Product(id: 5, name: "Relógio Digital", description: "Relógio com pulseira de silicone e cronómetro.", price: 89.00)
]

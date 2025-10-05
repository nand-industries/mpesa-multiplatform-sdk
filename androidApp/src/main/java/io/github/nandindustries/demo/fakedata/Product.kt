package io.github.nandindustries.demo.fakedata

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
)

val productList =
    mutableListOf(
        Product(
            id = 1,
            name = "Clean Code",
            description = "Robert C. Martin — Práticas para escrever código limpo, legível e de fácil manutenção.",
            price = 1800.00,
        ),
        Product(
            id = 2,
            name = "The Pragmatic Programmer",
            description = "Andrew Hunt e David Thomas — Guia essencial para aprimorar a forma de pensar e trabalhar como desenvolvedor.",
            price = 2000.00,
        ),
        Product(
            id = 3,
            name = "Design Patterns: Elements of Reusable Object-Oriented Software",
            description = "Erich Gamma et al. — Padrões de projeto fundamentais para arquitetura de software orientado a objetos.",
            price = 2200.00,
        ),
        Product(
            id = 4,
            name = "Kotlin in Action",
            description = "Dmitry Jemerov e Svetlana Isakova — Abordagem prática para dominar o Kotlin em aplicações modernas.",
            price = 1700.00,
        ),
        Product(
            id = 5,
            name = "Effective Java",
            description = "Joshua Bloch — Melhores práticas para criar código Java robusto, eficiente e sustentável.",
            price = 1900.00,
        ),
        Product(
            id = 6,
            name = "Refactoring: Improving the Design of Existing Code",
            description = "Martin Fowler — Técnicas para refatorar código sem alterar seu comportamento, melhorando sua estrutura.",
            price = 2100.00,
        ),
    )

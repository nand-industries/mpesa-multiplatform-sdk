package io.github.nandindustries.sdk.data.remote.mpesa.model

internal object MpesaResponseCodes {
    const val SUCCESS = "INS-0"
    const val INTERNAL_ERROR = "INS-1"
    const val INVALID_API_KEY = "INS-2"
    const val USER_NOT_ACTIVE = "INS-4"
    const val CANCELLED_BY_CUSTOMER = "INS-5"
    const val TRANSACTION_FAILED = "INS-6"
    const val REQUEST_TIMEOUT = "INS-9"
    const val DUPLICATE_TRANSACTION = "INS-10"
    const val INVALID_SHORTCODE = "INS-13"
    const val INVALID_REFERENCE = "INS-14"
    const val INVALID_AMOUNT = "INS-15"
    const val TEMPORARY_OVERLOADING = "INS-16"
    const val INVALID_TRANSACTION_REFERENCE = "INS-17"
    const val INVALID_TRANSACTION_ID = "INS-18"
    const val INVALID_THIRD_PARTY_REFERENCE = "INS-19"
    const val MISSING_PARAMETERS = "INS-20"
    const val VALIDATION_FAILED = "INS-21"
    const val INVALID_OPERATION_TYPE = "INS-22"
    const val UNKNOWN_STATUS = "INS-23"
    const val INVALID_INITIATOR = "INS-24"
    const val INVALID_SECURITY_CREDENTIAL = "INS-25"
    const val NOT_AUTHORIZED = "INS-26"
    const val DIRECT_DEBIT_MISSING = "INS-993"
    const val DIRECT_DEBIT_EXISTS = "INS-994"
    const val CUSTOMER_PROFILE_PROBLEMS = "INS-995"
    const val ACCOUNT_NOT_ACTIVE = "INS-996"
    const val LINKING_TRANSACTION_NOT_FOUND = "INS-997"
    const val INVALID_MARKET = "INS-998"
    const val INITIATOR_AUTH_ERROR = "INS-2001"
    const val RECEIVER_INVALID = "INS-2002"
    const val INSUFFICIENT_BALANCE = "INS-2006"
    const val INVALID_MSISDN = "INS-2051"
    const val INVALID_LANGUAGE_CODE = "INS-2057"

    data class CodeInfo(
        val title: String,
        val description: String,
    )

    val details =
        mapOf(
            SUCCESS to
                    CodeInfo(
                        title = "Sucesso",
                        description = "O pedido foi processado com sucesso.",
                    ),
            INTERNAL_ERROR to
                    CodeInfo(
                        title = "Erro Interno",
                        description = "Ocorreu um erro interno durante o processamento do pedido.",
                    ),
            INVALID_API_KEY to
                    CodeInfo(
                        title = "Chave API Inválida",
                        description = "A chave API fornecida é inválida ou está em falta.",
                    ),
            USER_NOT_ACTIVE to
                    CodeInfo(
                        title = "Utilizador Inactivo",
                        description = "A conta do utilizador não está activa e não pode realizar transacções.",
                    ),
            CANCELLED_BY_CUSTOMER to
                    CodeInfo(
                        title = "Cancelado pelo Cliente",
                        description = "A transacção foi cancelada pelo cliente.",
                    ),
            TRANSACTION_FAILED to
                    CodeInfo(
                        title = "Falha na Transacção",
                        description = "A transacção não pôde ser concluída com sucesso.",
                    ),
            REQUEST_TIMEOUT to
                    CodeInfo(
                        title = "Tempo de Espera Esgotado",
                        description = "O pedido expirou antes de ser processado.",
                    ),
            DUPLICATE_TRANSACTION to
                    CodeInfo(
                        title = "Transacção Duplicada",
                        description = "Foi identificada uma transacção duplicada.",
                    ),
            INVALID_SHORTCODE to
                    CodeInfo(
                        title = "Código Curto Inválido",
                        description = "O código curto fornecido é inválido.",
                    ),
            INVALID_REFERENCE to
                    CodeInfo(
                        title = "Referência Inválida",
                        description = "A referência da transacção é inválida.",
                    ),
            INVALID_AMOUNT to
                    CodeInfo(
                        title = "Valor Inválido",
                        description = "O valor especificado para a transacção é inválido.",
                    ),
            TEMPORARY_OVERLOADING to
                    CodeInfo(
                        title = "Sobrecarga Temporária",
                        description = "O sistema está temporariamente sobrecarregado. Tente novamente mais tarde.",
                    ),
            INVALID_TRANSACTION_REFERENCE to
                    CodeInfo(
                        title = "Referência de Transacção Inválida",
                        description = "A referência da transacção não corresponde a nenhum registo.",
                    ),
            INVALID_TRANSACTION_ID to
                    CodeInfo(
                        title = "ID de Transacção Inválido",
                        description = "O ID da transacção fornecido é inválido.",
                    ),
            INVALID_THIRD_PARTY_REFERENCE to
                    CodeInfo(
                        title = "Referência de Terceiro Inválida",
                        description = "A referência da entidade terceira é inválida.",
                    ),
            MISSING_PARAMETERS to
                    CodeInfo(
                        title = "Parâmetros em Falta",
                        description = "Faltam parâmetros obrigatórios no pedido.",
                    ),
            VALIDATION_FAILED to
                    CodeInfo(
                        title = "Validação Falhou",
                        description = "O pedido não passou nas validações necessárias.",
                    ),
            INVALID_OPERATION_TYPE to
                    CodeInfo(
                        title = "Tipo de Operação Inválido",
                        description = "O tipo de operação especificado é inválido.",
                    ),
            UNKNOWN_STATUS to
                    CodeInfo(
                        title = "Estado Desconhecido",
                        description = "A transacção terminou com um estado desconhecido.",
                    ),
            INVALID_INITIATOR to
                    CodeInfo(
                        title = "Iniciador Inválido",
                        description = "A informação do iniciador da transacção é inválida.",
                    ),
            INVALID_SECURITY_CREDENTIAL to
                    CodeInfo(
                        title = "Credenciais de Segurança Inválidas",
                        description = "As credenciais de segurança fornecidas são inválidas.",
                    ),
            NOT_AUTHORIZED to
                    CodeInfo(
                        title = "Não Autorizado",
                        description = "O solicitante não está autorizado a executar esta acção.",
                    ),
            DIRECT_DEBIT_MISSING to
                    CodeInfo(
                        title = "Débito Directo em Falta",
                        description = "Não foi encontrado um débito directo configurado.",
                    ),
            DIRECT_DEBIT_EXISTS to
                    CodeInfo(
                        title = "Débito Directo Existente",
                        description = "Já existe um débito directo associado a esta conta.",
                    ),
            CUSTOMER_PROFILE_PROBLEMS to
                    CodeInfo(
                        title = "Problemas no Perfil do Cliente",
                        description = "Foram encontrados problemas no perfil do cliente.",
                    ),
            ACCOUNT_NOT_ACTIVE to
                    CodeInfo(
                        title = "Conta Inactiva",
                        description = "A conta do cliente encontra-se inactiva.",
                    ),
            LINKING_TRANSACTION_NOT_FOUND to
                    CodeInfo(
                        title = "Transacção de Ligação Não Encontrada",
                        description = "Não foi possível encontrar a transacção de ligação.",
                    ),
            INVALID_MARKET to
                    CodeInfo(
                        title = "Mercado Inválido",
                        description = "O mercado especificado é inválido.",
                    ),
            INITIATOR_AUTH_ERROR to
                    CodeInfo(
                        title = "Erro de Autenticação do Iniciador",
                        description = "Falha na autenticação do iniciador da transacção.",
                    ),
            RECEIVER_INVALID to
                    CodeInfo(
                        title = "Destinatário Inválido",
                        description = "As informações do destinatário são inválidas.",
                    ),
            INSUFFICIENT_BALANCE to
                    CodeInfo(
                        title = "Saldo Insuficiente",
                        description = "A conta não possui saldo suficiente para completar a transacção.",
                    ),
            INVALID_MSISDN to
                    CodeInfo(
                        title = "MSISDN Inválido",
                        description = "O número de telefone (MSISDN) fornecido é inválido.",
                    ),
            INVALID_LANGUAGE_CODE to
                    CodeInfo(
                        title = "Código de Idioma Inválido",
                        description = "O código de idioma fornecido é inválido ou não suportado.",
                    ),
        )

    fun getTitleAndDescription(code: String): CodeInfo =
        details[code] ?: throw Exception("Invalid response code: $code")
}

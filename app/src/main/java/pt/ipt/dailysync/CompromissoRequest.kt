package pt.ipt.dailysync

data class CompromissoRequest(
    val titulo: String,
    val descricao: String,
    val data: String,
    val localizacao: String?
)

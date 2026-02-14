package pt.ipt.dailysync

data class Compromisso(
    val id: Int,
    val titulo: String,
    val descricao: String,
    val data: String,
    val localizacao: String?
)

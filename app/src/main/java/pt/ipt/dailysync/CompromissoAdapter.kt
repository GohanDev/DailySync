package pt.ipt.dailysync

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CompromissoAdapter(
    private val lista: List<Compromisso>,
    private val onDelete: (Int) -> Unit,
    private val onEdit: (Int) -> Unit
) : RecyclerView.Adapter<CompromissoAdapter.CompromissoViewHolder>() {

    class CompromissoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titulo: TextView = itemView.findViewById(R.id.textTitulo)
        val descricao: TextView = itemView.findViewById(R.id.textDescricao)
        val data: TextView = itemView.findViewById(R.id.textData)
        val localizacao: TextView = itemView.findViewById(R.id.textLocalizacao)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompromissoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_compromisso, parent, false)
        return CompromissoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CompromissoViewHolder, position: Int) {
        val compromisso = lista[position]

        holder.titulo.text = compromisso.titulo
        holder.descricao.text = compromisso.descricao
        holder.data.text = compromisso.data
        holder.localizacao.text = compromisso.localizacao ?: ""

        holder.btnDelete.setOnClickListener {
            onDelete(position)
        }

        holder.itemView.setOnClickListener {
            onEdit(position)
        }
    }

    override fun getItemCount(): Int = lista.size
}

package pt.ipt.dailysync

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CompromissoAdapter(
    private val lista: List<Compromisso>,
    private val onDelete: (Compromisso) -> Unit,
    private val onEdit: (Compromisso) -> Unit
) : RecyclerView.Adapter<CompromissoAdapter.VH>() {

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textTitulo: TextView = itemView.findViewById(R.id.textTitulo)
        val textDescricao: TextView = itemView.findViewById(R.id.textDescricao)
        val textData: TextView = itemView.findViewById(R.id.textData)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_compromisso, parent, false)
        return VH(v)
    }

    override fun getItemCount(): Int = lista.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        val c = lista[position]
        holder.textTitulo.text = c.titulo
        holder.textDescricao.text = c.descricao
        holder.textData.text = c.data

        holder.btnDelete.setOnClickListener { onDelete(c) }
        holder.btnEdit.setOnClickListener { onEdit(c) }
    }
}

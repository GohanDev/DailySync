package pt.ipt.dailysync

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CompromissoAdapter(
    private val lista: MutableList<Compromisso>,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<CompromissoAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titulo: TextView = view.findViewById(R.id.tvTitulo)
        val descricao: TextView = view.findViewById(R.id.tvDescricao)
        val data: TextView = view.findViewById(R.id.tvData)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_compromisso, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val compromisso = lista[position]
        holder.titulo.text = compromisso.titulo
        holder.descricao.text = compromisso.descricao
        holder.data.text = compromisso.data

        holder.btnDelete.setOnClickListener {
            onDeleteClick(position)
        }
    }

    override fun getItemCount(): Int = lista.size
}

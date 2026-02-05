package pt.ipt.dailysync

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CompromissoAdapter(
    private val lista: List<Compromisso>
) : RecyclerView.Adapter<CompromissoAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitulo: TextView = itemView.findViewById(R.id.txtTitulo)
        val txtData: TextView = itemView.findViewById(R.id.txtData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_compromisso, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val compromisso = lista[position]
        holder.txtTitulo.text = compromisso.titulo
        holder.txtData.text = compromisso.data
    }

    override fun getItemCount(): Int = lista.size
}

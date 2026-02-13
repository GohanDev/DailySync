package pt.ipt.dailysync

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var lista: MutableList<Compromisso>
    private lateinit var adapter: CompromissoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recycler = findViewById<RecyclerView>(R.id.recyclerCompromissos)
        val fab = findViewById<FloatingActionButton>(R.id.fabAdicionar)

        lista = mutableListOf(
            Compromisso(1, "Aula de DAM", "Android Studio", "08/02/2026"),
            Compromisso(2, "Reunião de Projeto", "Trabalho API", "09/02/2026")
        )

        adapter = CompromissoAdapter(lista,
            onDeleteClick = { position ->
                confirmarRemocao(position)
            }
        )

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        fab.setOnClickListener {
            val novoId = lista.size + 1
            lista.add(
                Compromisso(
                    novoId,
                    "Novo Compromisso $novoId",
                    "Descrição automática",
                    "10/02/2026"
                )
            )
            adapter.notifyDataSetChanged()
        }
    }

    private fun confirmarRemocao(position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar compromisso")
            .setMessage("Tem a certeza que deseja eliminar?")
            .setPositiveButton("Sim") { _, _ ->
                lista.removeAt(position)
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "Compromisso eliminado", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}

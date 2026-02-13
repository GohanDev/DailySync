package pt.ipt.dailysync

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var lista: MutableList<Compromisso>
    private lateinit var adapter: CompromissoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recycler = findViewById<RecyclerView>(R.id.recyclerCompromissos)
        val btnAdicionar = findViewById<Button>(R.id.btnAdicionar)

        lista = mutableListOf(
            Compromisso(1, "Aula de DAM", "Android Studio", "08/02/2026"),
            Compromisso(2, "Reunião de Projeto", "Trabalho API", "09/02/2026")
        )

        adapter = CompromissoAdapter(lista)

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        btnAdicionar.setOnClickListener {
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
}

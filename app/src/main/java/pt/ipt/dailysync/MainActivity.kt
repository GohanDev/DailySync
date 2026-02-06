package pt.ipt.dailysync

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    private val compromissos = mutableListOf<Compromisso>()
    private lateinit var adapter: CompromissoAdapter
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recycler = findViewById<RecyclerView>(R.id.recyclerCompromissos)
        val btnAdicionar = findViewById<Button>(R.id.btnAdicionar)

        carregarCompromissos()

        adapter = CompromissoAdapter(compromissos)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        btnAdicionar.setOnClickListener {
            mostrarDialogAdicionar()
        }
    }

    private fun mostrarDialogAdicionar() {
        val layout = layoutInflater.inflate(R.layout.dialog_adicionar, null)

        val etTitulo = layout.findViewById<EditText>(R.id.etTitulo)
        val etData = layout.findViewById<EditText>(R.id.etData)

        AlertDialog.Builder(this)
            .setTitle("Novo compromisso")
            .setView(layout)
            .setPositiveButton("Adicionar") { _, _ ->
                val titulo = etTitulo.text.toString()
                val data = etData.text.toString()

                if (titulo.isNotEmpty() && data.isNotEmpty()) {
                    compromissos.add(Compromisso(titulo, data))
                    adapter.notifyItemInserted(compromissos.size - 1)
                    guardarCompromissos()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun guardarCompromissos() {
        val prefs = getSharedPreferences("dailysync_prefs", Context.MODE_PRIVATE)
        val json = gson.toJson(compromissos)
        prefs.edit().putString("lista_compromissos", json).apply()
    }

    private fun carregarCompromissos() {
        val prefs = getSharedPreferences("dailysync_prefs", Context.MODE_PRIVATE)
        val json = prefs.getString("lista_compromissos", null)

        if (json != null) {
            val type = object : TypeToken<MutableList<Compromisso>>() {}.type
            val listaGuardada: MutableList<Compromisso> = gson.fromJson(json, type)
            compromissos.addAll(listaGuardada)
        }
    }
}

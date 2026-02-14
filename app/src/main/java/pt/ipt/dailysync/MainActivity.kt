package pt.ipt.dailysync

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var lista: MutableList<Compromisso>
    private lateinit var adapter: CompromissoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recycler = findViewById<RecyclerView>(R.id.recyclerCompromissos)
        val fab = findViewById<FloatingActionButton>(R.id.fabAdicionar)

        lista = mutableListOf()
        adapter = CompromissoAdapter(lista) { position ->
            confirmarRemocao(lista[position].id)
        }

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        carregarCompromissos()

        fab.setOnClickListener {
            Toast.makeText(this, "Adicionar ainda não implementado na API", Toast.LENGTH_SHORT).show()
        }
    }

    private fun carregarCompromissos() {
        RetrofitClient.instance.getCompromissos()
            .enqueue(object : Callback<List<Compromisso>> {
                override fun onResponse(
                    call: Call<List<Compromisso>>,
                    response: Response<List<Compromisso>>
                ) {
                    if (response.isSuccessful) {
                        lista.clear()
                        lista.addAll(response.body() ?: emptyList())
                        adapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(this@MainActivity, "Erro ao carregar dados", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Compromisso>>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Falha na ligação à API", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun confirmarRemocao(id: Int) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar compromisso")
            .setMessage("Tem a certeza que deseja eliminar?")
            .setPositiveButton("Sim") { _, _ ->
                eliminarCompromisso(id)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun eliminarCompromisso(id: Int) {
        RetrofitClient.instance.eliminarCompromisso(id)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    carregarCompromissos()
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Erro ao eliminar", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
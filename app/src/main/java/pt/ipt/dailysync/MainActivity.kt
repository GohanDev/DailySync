package pt.ipt.dailysync

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var lista: MutableList<Compromisso>
    private lateinit var adapter: CompromissoAdapter

    private val LOCATION_REQ_CODE = 1001

    // Para guardar referência ao EditText da localização do dialog aberto
    private var currentLocalizacaoEdit: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recycler = findViewById<RecyclerView>(R.id.recyclerCompromissos)
        val fab = findViewById<FloatingActionButton>(R.id.fabAdicionar)
        val btnSobre = findViewById<Button>(R.id.btnSobre)

        lista = mutableListOf()

        adapter = CompromissoAdapter(
            lista,
            onDelete = { position -> eliminarCompromisso(lista[position].id) },
            onEdit = { position -> mostrarDialogEditar(lista[position]) }
        )

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        carregarCompromissos()

        fab.setOnClickListener { mostrarDialogAdicionar() }

        btnSobre.setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }
    }

    private fun carregarCompromissos() {
        RetrofitClient.instance.getCompromissos()
            .enqueue(object : Callback<List<Compromisso>> {
                override fun onResponse(call: Call<List<Compromisso>>, response: Response<List<Compromisso>>) {
                    if (response.isSuccessful) {
                        lista.clear()
                        lista.addAll(response.body() ?: emptyList())
                        adapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(this@MainActivity, "Erro ao carregar (API)", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<Compromisso>>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Falha na ligação à API", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun mostrarDialogAdicionar() {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_compromisso, null)

        val editTitulo = view.findViewById<EditText>(R.id.editTitulo)
        val editDescricao = view.findViewById<EditText>(R.id.editDescricao)
        val editData = view.findViewById<EditText>(R.id.editData)
        val editLocalizacao = view.findViewById<EditText>(R.id.editLocalizacao)
        val btnGPS = view.findViewById<Button>(R.id.btnGPS)

        configurarDatePicker(editData)

        btnGPS.setOnClickListener {
            currentLocalizacaoEdit = editLocalizacao
            obterLocalizacaoGPS()
        }

        AlertDialog.Builder(this)
            .setTitle("Adicionar Compromisso")
            .setView(view)
            .setPositiveButton("Guardar") { _, _ ->
                val titulo = editTitulo.text.toString().trim()
                val desc = editDescricao.text.toString().trim()
                val data = editData.text.toString().trim()
                val loc = editLocalizacao.text.toString().trim().ifEmpty { null }

                if (titulo.isEmpty() || desc.isEmpty() || data.isEmpty()) {
                    Toast.makeText(this, "Preenche Título, Descrição e Data", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val request = CompromissoRequest(titulo, desc, data, loc)

                RetrofitClient.instance.adicionarCompromisso(request)
                    .enqueue(object : Callback<Compromisso> {
                        override fun onResponse(call: Call<Compromisso>, response: Response<Compromisso>) {
                            if (response.isSuccessful) carregarCompromissos()
                            else Toast.makeText(this@MainActivity, "Erro a gravar (API)", Toast.LENGTH_SHORT).show()
                        }

                        override fun onFailure(call: Call<Compromisso>, t: Throwable) {
                            Toast.makeText(this@MainActivity, "Erro de rede ao gravar", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun mostrarDialogEditar(compromisso: Compromisso) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_compromisso, null)

        val editTitulo = view.findViewById<EditText>(R.id.editTitulo)
        val editDescricao = view.findViewById<EditText>(R.id.editDescricao)
        val editData = view.findViewById<EditText>(R.id.editData)
        val editLocalizacao = view.findViewById<EditText>(R.id.editLocalizacao)
        val btnGPS = view.findViewById<Button>(R.id.btnGPS)

        editTitulo.setText(compromisso.titulo)
        editDescricao.setText(compromisso.descricao)
        editData.setText(compromisso.data)
        editLocalizacao.setText(compromisso.localizacao ?: "")

        configurarDatePicker(editData)

        btnGPS.setOnClickListener {
            currentLocalizacaoEdit = editLocalizacao
            obterLocalizacaoGPS()
        }

        AlertDialog.Builder(this)
            .setTitle("Editar Compromisso")
            .setView(view)
            .setPositiveButton("Guardar") { _, _ ->
                val titulo = editTitulo.text.toString().trim()
                val desc = editDescricao.text.toString().trim()
                val data = editData.text.toString().trim()
                val loc = editLocalizacao.text.toString().trim().ifEmpty { null }

                if (titulo.isEmpty() || desc.isEmpty() || data.isEmpty()) {
                    Toast.makeText(this, "Preenche Título, Descrição e Data", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val request = CompromissoRequest(titulo, desc, data, loc)

                RetrofitClient.instance.editarCompromisso(compromisso.id, request)
                    .enqueue(object : Callback<Compromisso> {
                        override fun onResponse(call: Call<Compromisso>, response: Response<Compromisso>) {
                            if (response.isSuccessful) carregarCompromissos()
                            else Toast.makeText(this@MainActivity, "Erro a editar (API)", Toast.LENGTH_SHORT).show()
                        }

                        override fun onFailure(call: Call<Compromisso>, t: Throwable) {
                            Toast.makeText(this@MainActivity, "Erro de rede ao editar", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun configurarDatePicker(editText: EditText) {
        editText.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    editText.setText("$day/${month + 1}/$year")
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
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

    // ===== GPS =====
    private fun obterLocalizacaoGPS() {
        val fine = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarse = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)

        if (fine != PackageManager.PERMISSION_GRANTED && coarse != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_REQ_CODE
            )
            return
        }

        val fused = LocationServices.getFusedLocationProviderClient(this)
        fused.lastLocation
            .addOnSuccessListener { loc ->
                if (loc != null) {
                    val texto = "${loc.latitude}, ${loc.longitude}"
                    currentLocalizacaoEdit?.setText(texto)
                } else {
                    Toast.makeText(this, "Sem localização (ativa o GPS / define no emulador)", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Erro ao obter GPS", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQ_CODE) {
            if (grantResults.isNotEmpty() && grantResults.any { it == PackageManager.PERMISSION_GRANTED }) {
                obterLocalizacaoGPS()
            } else {
                Toast.makeText(this, "Permissão de localização negada", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

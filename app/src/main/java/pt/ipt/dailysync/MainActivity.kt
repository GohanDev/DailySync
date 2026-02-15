package pt.ipt.dailysync

import android.Manifest
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.LocationServices
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: CompromissoAdapter
    private val lista = mutableListOf<Compromisso>()

    private val api: ApiService by lazy { RetrofitClient.api }

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    private val pedirPermissaoLocalizacao =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (!granted) toast("Permissão de localização negada.")
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler = findViewById(R.id.recyclerCompromissos)
        recycler.layoutManager = LinearLayoutManager(this)

        adapter = CompromissoAdapter(
            lista,
            onDelete = { comp -> confirmarApagar(comp) },
            onEdit = { comp -> abrirDialogCompromisso(comp) }
        )
        recycler.adapter = adapter

        findViewById<FloatingActionButton>(R.id.fabAdd).setOnClickListener {
            abrirDialogCompromisso(null)
        }

        findViewById<Button>(R.id.btnSobre).setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }

        carregarLista()
    }

    private fun carregarLista() {
        api.getCompromissos().enqueue(object : Callback<List<Compromisso>> {
            override fun onResponse(
                call: Call<List<Compromisso>>,
                response: Response<List<Compromisso>>
            ) {
                if (response.isSuccessful) {
                    lista.clear()
                    lista.addAll(response.body() ?: emptyList())
                    adapter.notifyDataSetChanged()
                } else {
                    toast("Erro a carregar (${response.code()})")
                }
            }

            override fun onFailure(call: Call<List<Compromisso>>, t: Throwable) {
                toast("Falha de rede: ${t.message}")
            }
        })
    }

    private fun abrirDialogCompromisso(compromisso: Compromisso?) {
        val v = layoutInflater.inflate(R.layout.dialog_compromisso, null)

        val txtTituloDialog = v.findViewById<TextView>(R.id.txtTituloDialog)
        val editTitulo = v.findViewById<EditText>(R.id.editTitulo)
        val editDescricao = v.findViewById<EditText>(R.id.editDescricao)
        val editData = v.findViewById<EditText>(R.id.editData)
        val editLocalizacao = v.findViewById<EditText>(R.id.editLocalizacao)
        val btnGPS = v.findViewById<Button>(R.id.btnGPS)

        val isEditar = compromisso != null
        txtTituloDialog.text = if (isEditar) "Editar Compromisso" else "Adicionar Compromisso"

        if (isEditar) {
            editTitulo.setText(compromisso!!.titulo)
            editDescricao.setText(compromisso.descricao)
            editData.setText(compromisso.data)
            editLocalizacao.setText(compromisso.localizacao ?: "")
        }

        editData.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    val mm = month + 1
                    editData.setText(String.format(Locale.getDefault(), "%02d/%02d/%04d", day, mm, year))
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        btnGPS.setOnClickListener {
            obterLocalizacaoAtual { loc ->
                editLocalizacao.setText("${loc.latitude}, ${loc.longitude}")
                editLocalizacao.setSelection(editLocalizacao.text.length)
            }
        }

        AlertDialog.Builder(this)
            .setView(v)
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Guardar") { _, _ ->
                val titulo = editTitulo.text.toString().trim()
                val descricao = editDescricao.text.toString().trim()
                val data = editData.text.toString().trim()
                val localizacao = editLocalizacao.text.toString().trim()

                if (titulo.isEmpty() || descricao.isEmpty() || data.isEmpty()) {
                    toast("Preenche título, descrição e data.")
                    return@setPositiveButton
                }

                val req = CompromissoRequest(titulo, descricao, data, localizacao)

                if (isEditar) atualizar(compromisso!!.id, req)
                else adicionar(req)
            }
            .show()
    }

    private fun adicionar(req: CompromissoRequest) {
        api.addCompromisso(req).enqueue(object : Callback<Compromisso> {
            override fun onResponse(call: Call<Compromisso>, response: Response<Compromisso>) {
                if (response.isSuccessful) {
                    toast("Compromisso adicionado!")
                    carregarLista()
                } else toast("Erro ao adicionar (${response.code()})")
            }

            override fun onFailure(call: Call<Compromisso>, t: Throwable) {
                toast("Falha de rede: ${t.message}")
            }
        })
    }

    private fun atualizar(id: Int, req: CompromissoRequest) {
        api.updateCompromisso(id, req).enqueue(object : Callback<Compromisso> {
            override fun onResponse(call: Call<Compromisso>, response: Response<Compromisso>) {
                if (response.isSuccessful) {
                    toast("Compromisso atualizado!")
                    carregarLista()
                } else toast("Erro ao editar (${response.code()})")
            }

            override fun onFailure(call: Call<Compromisso>, t: Throwable) {
                toast("Falha de rede: ${t.message}")
            }
        })
    }

    private fun confirmarApagar(comp: Compromisso) {
        AlertDialog.Builder(this)
            .setTitle("Apagar")
            .setMessage("Queres apagar \"${comp.titulo}\"?")
            .setNegativeButton("Cancelar", null)
            .setPositiveButton("Apagar") { _, _ -> apagar(comp.id) }
            .show()
    }

    private fun apagar(id: Int) {
        api.deleteCompromisso(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    toast("Compromisso apagado!")
                    carregarLista()
                } else toast("Erro ao apagar (${response.code()})")
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                toast("Falha de rede: ${t.message}")
            }
        })
    }

    private fun obterLocalizacaoAtual(onOk: (Location) -> Unit) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            pedirPermissaoLocalizacao.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { loc ->
                if (loc != null) onOk(loc) else toast("Não foi possível obter localização.")
            }
            .addOnFailureListener { e ->
                toast("Erro GPS: ${e.message}")
            }
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}

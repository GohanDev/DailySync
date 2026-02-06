package pt.ipt.dailysync

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recycler = findViewById<RecyclerView>(R.id.recyclerCompromissos)

        val compromissos = listOf(
            Compromisso("Aula de DAM", "05/02/2026"),
            Compromisso("Trabalho de grupo", "07/02/2026"),
            Compromisso("Estudar para teste", "10/02/2026")
        )

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = CompromissoAdapter(compromissos)
    }
}


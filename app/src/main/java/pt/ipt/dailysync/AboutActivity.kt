package pt.ipt.dailysync

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import pt.ipt.dailysync.databinding.ActivityAboutBinding

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textCurso.text = "Curso: Engenharia Informática"
        binding.textDisciplina.text = "Disciplina: Desenvolvimento de Aplicações Móveis"
        binding.textAno.text = "Ano Letivo: 2025/2026"

        binding.textAutor.text = """
            Nº: 24706
            Nome: Bruno Alves
        """.trimIndent()

        binding.textTecnologias.text = """
            Tecnologias utilizadas:
            - Kotlin
            - Android Studio
            - Retrofit
            - Node.js
            - Express
            - Render (deploy API)
        """.trimIndent()
    }
}

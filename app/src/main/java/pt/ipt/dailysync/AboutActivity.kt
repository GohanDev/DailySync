package pt.ipt.dailysync

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        // Botão voltar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Sobre"
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}

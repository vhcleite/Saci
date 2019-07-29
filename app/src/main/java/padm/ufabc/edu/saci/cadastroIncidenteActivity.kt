package padm.ufabc.edu.saci

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;

import kotlinx.android.synthetic.main.activity_cadastro_incidente.*

class cadastroIncidenteActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro_incidente)
        setSupportActionBar(toolbar)

        val spinner = findViewById<Spinner>(R.id.spinner_classificacao_incidente)

        spinner.adapter = ArrayAdapter.createFromResource(this, R.array.classificacao_array, android.R.layout.simple_spinner_item)


    }

//        // Specify the layout to use when the list of choices appears
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        // Apply the adapter to the spinner
//        spinner.setAdapter(adapter)

}






package padm.ufabc.edu.saci

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;

import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(toolbar)


        loginButton = findViewById<Button>(R.id.button_login)

        loginButton.setOnClickListener {
            val mapintent = Intent(applicationContext, MapActivity::class.java)
            startActivity(mapintent)
        }


    }

}

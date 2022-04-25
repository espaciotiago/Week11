package tech.yeswecode.week11

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import tech.yeswecode.week11.databinding.ActivityMainBinding
import tech.yeswecode.week11.models.User
import tech.yeswecode.week11.utils.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.usernameTxt.setText("espaciotiago")
        binding.passwordTxt.setText("123")
        binding.loginBtn.setOnClickListener {
            val username = binding.usernameTxt.text.toString()
            val password = binding.passwordTxt.text.toString()
            if(username.isNotEmpty() && password.isNotEmpty()) {
                loginOrSignup(username, password)
            } else {
                Toast.makeText(this, "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginOrSignup(username: String, password: String) {
        val query = db.collection(USERS_COLLECTION).whereEqualTo(USERNAME_FIELD,username)
        query.get().addOnCompleteListener {
            if(it.result!!.size() == 0) {
                val user = User(UUID.randomUUID().toString(), username, password)
                db.collection(USERS_COLLECTION).document(user.id).set(user)
                goToHome(user)
            } else {
                lateinit var existingUser: User
                for(document in it.result!!) {
                    existingUser = document.toObject(User::class.java)
                    break
                }
                if (existingUser.password == password) {
                    goToHome(existingUser)
                } else {
                    Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun goToHome(user: User) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.apply { putExtra(USER_EXTRA, user) }
        startActivity(intent)
    }
}
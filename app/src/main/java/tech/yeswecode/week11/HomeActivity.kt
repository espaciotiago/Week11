package tech.yeswecode.week11

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import tech.yeswecode.week11.databinding.ActivityHomeBinding
import tech.yeswecode.week11.databinding.ActivityMainBinding
import tech.yeswecode.week11.models.User
import tech.yeswecode.week11.utils.USERS_COLLECTION

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var db = FirebaseFirestore.getInstance()
    private var users = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getUsersSnapshot()
    }

    private fun getUsers() {
        db.collection(USERS_COLLECTION).get().addOnCompleteListener {
            for(document in it.result!!) {
                val user = document.toObject(User::class.java)
                users.add(user)
            }
            Log.e(">>>", ""+users)
        }
    }

    private fun getUsersSnapshot() {
        db.collection(USERS_COLLECTION).addSnapshotListener { value, e ->
            if (e != null) {
                Log.w("HomeActivity", "Listen failed.", e)
                return@addSnapshotListener
            }
            for (document in value!!) {
                val user = document.toObject(User::class.java)
                users.add(user)
            }
            Log.e(">>>", ""+users)
        }
    }
}
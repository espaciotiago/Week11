package tech.yeswecode.week11

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import tech.yeswecode.week11.adapters.OnUserSelected
import tech.yeswecode.week11.adapters.UsersAdapter
import tech.yeswecode.week11.databinding.ActivityHomeBinding
import tech.yeswecode.week11.models.User
import tech.yeswecode.week11.utils.*

class HomeActivity : AppCompatActivity(), OnUserSelected {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: UsersAdapter

    private lateinit var currentUser: User
    private var db = FirebaseFirestore.getInstance()
    private var users = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        linearLayoutManager = LinearLayoutManager(this)
        adapter = UsersAdapter(users, this)
        binding.usersRecyclerview.adapter = adapter
        binding.usersRecyclerview.layoutManager = linearLayoutManager

        currentUser = intent.getSerializableExtra(USER_EXTRA) as User
        getUsers()
    }

    private fun getUsers() {
        db.collection(USERS_COLLECTION).get().addOnCompleteListener {
            for(document in it.result!!) {
                val user = document.toObject(User::class.java)
                if(user.id != currentUser.id) {
                    users.add(user)
                }
            }
            adapter.notifyDataSetChanged()
        }
    }

    override fun onUserSelected(selected: User) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.apply {
            putExtra(USER_EXTRA, currentUser)
            putExtra(RECEIVER_EXTRA, selected)
        }
        startActivity(intent)
    }
}
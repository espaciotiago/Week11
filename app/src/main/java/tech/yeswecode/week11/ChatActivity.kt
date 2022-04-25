package tech.yeswecode.week11

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import tech.yeswecode.week11.adapters.ChatAdapter
import tech.yeswecode.week11.databinding.ActivityChatBinding
import tech.yeswecode.week11.models.ChatMessage
import tech.yeswecode.week11.models.User
import tech.yeswecode.week11.utils.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: ChatAdapter

    private lateinit var chatReference: String
    private lateinit var sender: User
    private lateinit var receiver: User
    private var messages = ArrayList<ChatMessage>()

    private var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sender = intent.getSerializableExtra(USER_EXTRA) as User
        receiver = intent.getSerializableExtra(RECEIVER_EXTRA) as User

        linearLayoutManager = LinearLayoutManager(this)
        adapter = ChatAdapter(messages, sender, receiver)
        binding.chatRecyclerView.adapter = adapter
        binding.chatRecyclerView.layoutManager = linearLayoutManager

        binding.sendBtn.setOnClickListener {
            sendMessage()
        }

        getChat()
    }

    private fun getChat() {
        val query = db.collection(CHATS_COLLECTION)
            .whereIn(USERS_COLLECTION, listOf(listOf(sender.id, receiver.id)))
        query.get().addOnCompleteListener{
            val list = it.result!!
            if(list.isEmpty) {
                getChatSecondary()
            } else {
                for(document in it.result!!) {
                    chatReference = document.id
                    getMessages()
                    break
                }
            }
        }
    }

    private fun getChatSecondary() {
        val query = db.collection(CHATS_COLLECTION)
            .whereIn(USERS_COLLECTION, listOf(listOf(receiver.id, sender.id)))
        query.get().addOnCompleteListener{
            val list = it.result!!
            if(list.isEmpty) {
                createNewChat()
            } else {
                for(document in it.result!!) {
                    chatReference = document.id
                    getMessages()
                    break
                }
            }
        }
    }

    private fun createNewChat() {
        val usersArray = ArrayList<String>()
        usersArray.add(sender.id)
        usersArray.add(receiver.id)

        val chatId = UUID.randomUUID().toString()

        val map = HashMap<String, Any>()
        map[USERS_COLLECTION] = usersArray

        db.collection(CHATS_COLLECTION).document(chatId).set(map)
        chatReference = chatId
    }

    private fun getMessages() {
        // TODO: Fix bug when is the first time that sends a message, not refreshing the UI.
        val query = db.collection(CHATS_COLLECTION)
            .document(chatReference).collection(MESSAGES_COLLECTION)
        query.addSnapshotListener { value, e ->
            if (e != null) {
                Log.w("ChatActivity", "Listen failed.", e)
                return@addSnapshotListener
            }
            for (document in value!!) {
                val message = document.toObject(ChatMessage::class.java)
                val filter = messages.firstOrNull { it.id == message.id }
                if(filter == null || messages.isEmpty()) {
                    messages.add(message)
                }
            }
            adapter.notifyDataSetChanged()
        }
    }

    private fun sendMessage() {
        val message = binding.messageTextView.text.toString()
        if(!message.isEmpty()) {
            val chatMessage = ChatMessage(UUID.randomUUID().toString(), message, sender.id, receiver.id, Date())
            db.collection(CHATS_COLLECTION).document(chatReference).collection(MESSAGES_COLLECTION).document().set(chatMessage)
            binding.messageTextView.setText("")
        }
    }
}
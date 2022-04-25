package tech.yeswecode.week11.services

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import tech.yeswecode.week11.models.ChatMessage
import tech.yeswecode.week11.utils.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class ChatFirebaseProtocol: ChatProviderProtocol {

    private var db = FirebaseFirestore.getInstance()

    override fun getChat(
        user1: String,
        user2: String,
        completion: (chatReference: String?) -> Unit
    ) {
        val query = db.collection(CHATS_COLLECTION)
            .whereIn(USERS_COLLECTION, listOf(listOf(user1, user2)))
        query.get().addOnCompleteListener{
            val list = it.result!!
            if(list.isEmpty) {
                completion(null)
            } else {
                for(document in it.result!!) {
                    completion(document.id)
                    break
                }
            }
        }
    }

    override fun getMessages(chatReference: String, completion: (messages: ArrayList<ChatMessage>) -> Unit) {
        // TODO: Fix bug when is the first time that sends a message, not refreshing the UI.
        val messages = ArrayList<ChatMessage>()
        val query = db.collection(CHATS_COLLECTION)
            .document(chatReference).collection(MESSAGES_COLLECTION)
        query.addSnapshotListener { value, e ->
            if (e != null) {
                Log.w("ChatActivity", "Listen failed.", e)
                return@addSnapshotListener
            }
            for (document in value!!) {
                val message = document.toObject(ChatMessage::class.java)
                messages.add(message)
            }
            completion(messages)
        }
    }

    override fun createNewChat(user1: String, user2: String, completion: (chatReference: String) -> Unit) {
        val usersArray = ArrayList<String>()
        usersArray.add(user1)
        usersArray.add(user2)

        val chatId = UUID.randomUUID().toString()

        val map = HashMap<String, Any>()
        map[USERS_COLLECTION] = usersArray

        db.collection(CHATS_COLLECTION).document(chatId).set(map)
        completion(chatId)
    }

    override fun createNewMessage(chatReference: String, message: ChatMessage, completion: () -> Unit) {
        db.collection(CHATS_COLLECTION).document(chatReference).collection(MESSAGES_COLLECTION).document(message.id).set(message)
        completion()
    }

}
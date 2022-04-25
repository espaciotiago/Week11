package tech.yeswecode.week11.services

import tech.yeswecode.week11.models.ChatMessage
import tech.yeswecode.week11.models.User
import java.util.*
import kotlin.collections.ArrayList

class ChatMockProvider(private val sender: User,
                       private val receiver: User): ChatProviderProtocol {

    override fun getChat(
        user1: String,
        user2: String,
        completion: (chatReference: String?) -> Unit
    ) {
        completion("ref")
    }

    override fun getMessages(
        chatReference: String,
        completion: (messages: ArrayList<ChatMessage>) -> Unit
    ) {
        val messages = ArrayList<ChatMessage>()
        messages.add(ChatMessage("1", "Mensaje # 1", sender.id, receiver.id, Date()))
        messages.add(ChatMessage("2", "Mensaje # 2", receiver.id, receiver.id, Date()))
        messages.add(ChatMessage("3", "Mensaje # 3", sender.id, receiver.id, Date()))

        completion(messages)
    }

    override fun createNewChat(
        user1: String,
        user2: String,
        completion: (chatReference: String) -> Unit
    ) {
        completion("ref")
    }

    override fun createNewMessage(
        chatReference: String,
        message: ChatMessage,
        completion: () -> Unit
    ) {
        completion()
    }
}
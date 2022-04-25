package tech.yeswecode.week11.viewModels

import tech.yeswecode.week11.models.ChatMessage
import tech.yeswecode.week11.models.User
import tech.yeswecode.week11.services.ChatProviderProtocol
import java.util.*
import kotlin.collections.ArrayList

class ChatViewModel(private var delegate: OnChatResponse,
                    private var provider: ChatProviderProtocol,
                    val sender: User,
                    val receiver: User) {

    private var chatReference: String = ""
    var messages = ArrayList<ChatMessage>()

    fun getChat() {
        provider.getChat(sender.id, receiver.id) {
            if(it != null) {
                chatReference = it
                getMessages()
            } else {
                getChatSecondary()
            }
        }
    }

    fun sendMessage(message: String) {
        if(message.isNotEmpty()) {
            val chatMessage = ChatMessage(UUID.randomUUID().toString(), message, sender.id, receiver.id, Date())
            provider.createNewMessage(chatReference, chatMessage) {
                delegate.onNewMessageSended()
            }
        }
    }

    private fun createNewChat() {
        provider.createNewChat(sender.id, receiver.id) {
            chatReference = it
        }
    }

    private fun getChatSecondary() {
        provider.getChat(receiver.id, sender.id) {
            if(it != null) {
                chatReference = it
                getMessages()
            } else {
                createNewChat()
            }
        }
    }

    private fun getMessages() {
        provider.getMessages(chatReference) { listOfMessages ->
            for(message in listOfMessages) {
                val filter = messages.firstOrNull { it.id == message.id }
                if(filter == null || messages.isEmpty()) {
                    messages.add(message)
                }
            }
            delegate.onMessagesReceived()
        }
    }
}
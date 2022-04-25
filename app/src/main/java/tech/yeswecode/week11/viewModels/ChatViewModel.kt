package tech.yeswecode.week11.viewModels

import tech.yeswecode.week11.models.ChatMessage
import tech.yeswecode.week11.models.User
import tech.yeswecode.week11.services.ChatProviderProtocol
import tech.yeswecode.week11.utils.MESSAGES_COLLECTION
import java.util.*
import kotlin.collections.ArrayList

class ChatViewModel(provider: ChatProviderProtocol, sender: User, receiver: User) {

    private var provider: ChatProviderProtocol = provider
    private var chatReference: String = ""

    var sender: User = sender
    var receiver: User = receiver
    var messages = ArrayList<ChatMessage>()

    fun getChat(completion: (messages: ArrayList<ChatMessage>)->Unit) {
        provider.getChat(sender.id, receiver.id) {
            if(it != null) {
                chatReference = it!!
                getMessages(completion)
            } else {
                getChatSeconday(completion)
            }
        }
    }

    fun sendMessage(message: String, completion: ()->Unit) {
        if(!message.isEmpty()) {
            val chatMessage = ChatMessage(UUID.randomUUID().toString(), message, sender.id, receiver.id, Date())
            provider.createNewMessage(chatReference, chatMessage, completion)
        }
    }

    private fun createNewChat() {
        provider.createNewChat(sender.id, receiver.id) {
            chatReference = it
        }
    }

    private fun getChatSeconday(completion: (messages: ArrayList<ChatMessage>)->Unit) {
        provider.getChat(receiver.id, sender.id) {
            if(it != null) {
                chatReference = it!!
                getMessages(completion)
            } else {
                createNewChat()
            }
        }
    }

    private fun getMessages(completion: (messages: ArrayList<ChatMessage>)->Unit) {
        provider.getMessages(chatReference) {
            for(message in it) {
                val filter = messages.firstOrNull { it.id == message.id }
                if(filter == null || messages.isEmpty()) {
                    messages.add(message)
                }
            }
            completion(messages)
        }
    }
}
package tech.yeswecode.week11.services

import tech.yeswecode.week11.models.ChatMessage

interface ChatProviderProtocol {
    fun getChat(user1:String, user2:String, completion: (chatReference: String?)->Unit)
    fun getMessages(chatReference: String, completion: (messages: ArrayList<ChatMessage>)->Unit)
    fun createNewChat(user1:String, user2:String, completion: (chatReference: String)->Unit)
    fun createNewMessage(chatReference: String, message: ChatMessage, completion: ()->Unit)
}
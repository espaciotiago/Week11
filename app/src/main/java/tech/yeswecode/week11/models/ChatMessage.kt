package tech.yeswecode.week11.models

import java.util.*

data class ChatMessage(val id: String = "",
                       val message: String = "",
                       val sender: String = "",
                       val receiver: String = "",
                       val date: Date = Date())
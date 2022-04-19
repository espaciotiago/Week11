package tech.yeswecode.week11.models

import java.io.Serializable

data class User(
    val id: String = "",
    val username: String = "",
    val password: String = ""
) : Serializable

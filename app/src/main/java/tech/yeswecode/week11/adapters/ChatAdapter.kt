package tech.yeswecode.week11.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import tech.yeswecode.week11.R
import tech.yeswecode.week11.models.ChatMessage
import tech.yeswecode.week11.models.User

class ChatAdapter(private val dataSet: ArrayList<ChatMessage>,
                  private val user1: User,
                  private val user2: User) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userTextView: TextView = view.findViewById(R.id.userTextView)
        val messageTextView: TextView = view.findViewById(R.id.messageTextView)
        val chatHolder: ConstraintLayout = view.findViewById(R.id.chatHolder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return ChatAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = dataSet[position]
        holder.messageTextView.text = message.message
        if(user1.id == message.sender) {
            holder.userTextView.text = user1.username
            holder.chatHolder.setBackgroundColor(Color.parseColor("#FFFFFF"))
        } else {
            holder.userTextView.text = user2.username
            holder.chatHolder.setBackgroundColor(Color.parseColor("#F8F7FF"))
        }
    }

    override fun getItemCount() = dataSet.size
}
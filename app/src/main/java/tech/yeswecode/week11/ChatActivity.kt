package tech.yeswecode.week11

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import tech.yeswecode.week11.adapters.ChatAdapter
import tech.yeswecode.week11.databinding.ActivityChatBinding
import tech.yeswecode.week11.models.User
import tech.yeswecode.week11.services.ChatFirebaseProtocol
import tech.yeswecode.week11.services.ChatMockProvider
import tech.yeswecode.week11.utils.*
import tech.yeswecode.week11.viewModels.ChatViewModel

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var adapter: ChatAdapter
    private lateinit var vm: ChatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sender = intent.getSerializableExtra(USER_EXTRA) as User
        val receiver = intent.getSerializableExtra(RECEIVER_EXTRA) as User
        vm = ChatViewModel(ChatFirebaseProtocol(), sender, receiver)

        // Use this for mock and testing
        //vm = ChatViewModel(ChatMockProvider(sender, receiver), sender, receiver)

        linearLayoutManager = LinearLayoutManager(this)
        adapter = ChatAdapter(vm.messages, vm.sender, vm.receiver)
        binding.chatRecyclerView.adapter = adapter
        binding.chatRecyclerView.layoutManager = linearLayoutManager

        binding.sendBtn.setOnClickListener {
            sendMessage()
        }

        getChat()
    }

    private fun getChat() {
        vm.getChat {
            //TODO: Positionate scroll on the last message
            adapter.notifyDataSetChanged()
        }
    }

    private fun sendMessage() {
        val message = binding.messageTextView.text.toString()
        vm.sendMessage(message) {
            //TODO: Close the keyboard and positionate scroll on the last message
            binding.messageTextView.setText("")
        }
    }
}
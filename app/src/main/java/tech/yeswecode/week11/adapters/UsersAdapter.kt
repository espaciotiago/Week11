package tech.yeswecode.week11.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import tech.yeswecode.week11.R
import tech.yeswecode.week11.models.User

class UsersAdapter(private val dataSet: ArrayList<User>,
                   private val delegate: OnUserSelected):
    RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userNameTextView: TextView = view.findViewById(R.id.nameTextView)
        val itemHolder: ConstraintLayout = view.findViewById(R.id.itemHolder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = dataSet[position]
        holder.userNameTextView.text = user.username
        holder.itemHolder.setOnClickListener {
            delegate.onUserSelected(user)
        }
    }

    override fun getItemCount() = dataSet.size
}
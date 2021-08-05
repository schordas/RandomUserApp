package com.example.randomuser.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.randomuser.databinding.UserListItemBinding
import com.example.randomuser.model.User

class UserListAdapter(private val clickListener: UserListener) :
    ListAdapter<User, UserListAdapter.UserViewHolder>(DiffCallback) {

        class UserViewHolder(
            private var binding: UserListItemBinding
        ): RecyclerView.ViewHolder(binding.root) {

            fun bind(clickListener: UserListener, user: User) {
                binding.user = user
                binding.clickListener = clickListener
                binding.executePendingBindings()
            }
        }

    companion object DiffCallback: DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            // We assume the email is unique as it is the primary key in this case
            return oldItem.email == newItem.email
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            // Normally I would create a getter in the relevant data class to compare items in the
            // object but I was pressed for time and didn't get to this
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return UserViewHolder(
            UserListItemBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(clickListener, user)
    }
}

/**
 * Click listener for navigation to [UserDetailFragment]
 */
class UserListener(val clickListener: (user: User) -> Unit) {
    fun onClick(user: User) = clickListener(user)
}

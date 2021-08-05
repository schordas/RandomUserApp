package com.example.randomuser.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.randomuser.R
import com.example.randomuser.databinding.FragmentUserListBinding
import com.example.randomuser.ui.adapter.UserListAdapter
import com.example.randomuser.ui.adapter.UserListener
import com.example.randomuser.ui.viewmodel.SharedUserViewModel
import com.example.randomuser.ui.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserListFragment : Fragment() {

    // This view model is designed for the list so we don't share it
    private val userViewModel: UserViewModel by viewModels()
    // This is a shared view model to pass data to the detail
    private val sharedViewModel: SharedUserViewModel by activityViewModels()

    private var _binding: FragmentUserListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Observe view state LiveData
        userViewModel.state.observe(viewLifecycleOwner, {
            // set up ui appropriately according to view state
            when(it) {
                is UserListViewState.Success -> {
                    val adapter = binding.recyclerView.adapter as UserListAdapter
                    adapter.submitList(it.data)
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.loadingProgressIndicator.visibility = View.GONE
                    binding.networkFailedImage.visibility = View.GONE
                }
                is UserListViewState.Failure -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.networkFailedImage.visibility = View.VISIBLE
                    binding.loadingProgressIndicator.visibility = View.GONE
                    // TODO: handle failure state
                }
                is UserListViewState.Loading -> {
                    if (!binding.swipeRefreshLayout.isRefreshing) {
                        binding.networkFailedImage.visibility = View.GONE
                        binding.loadingProgressIndicator.visibility = View.VISIBLE
                    }
                }
            }
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = userViewModel

            // Build adapter with click listener
            recyclerView.adapter = UserListAdapter(UserListener { user ->
                // save user to shared view model so the detail can access it
                sharedViewModel.onUserClicked(user)
                // navigate to detail
                findNavController()
                    .navigate(R.id.action_userListFragment_to_userDetailFragment)
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

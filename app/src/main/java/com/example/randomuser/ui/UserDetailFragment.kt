package com.example.randomuser.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.randomuser.databinding.FragmentUserDetailBinding
import com.example.randomuser.model.Location
import com.example.randomuser.model.User
import com.example.randomuser.ui.viewmodel.SharedUserViewModel
import com.example.randomuser.util.appAvailable
import java.util.*

class UserDetailFragment : Fragment() {

    private val sharedViewModel: SharedUserViewModel by activityViewModels()

    private var _binding: FragmentUserDetailBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        val root = binding.root
        // Inflate the layout for this fragment
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = sharedViewModel
            detailFragment = this@UserDetailFragment
        }
    }

    /**
     * Attempt to open a map app that focuses on a location based on coordinates.
     * The addresses could be potentially fake, otherwise this could be passed as well.
     */
    fun onLocationClick(location: Location) {
        val uri = Uri.parse(
            "geo:${location.coordinates.latitude},${location.coordinates.longitude}?z=8"
        )
        val intent = Intent(Intent.ACTION_VIEW, uri)
        if (intent.appAvailable(context)) {
            context?.startActivity(intent)
        }
    }

    /**
     * Attempt to generate a new calendar event in a calendar app.
     */
    fun onCalendarClick(user: User) {
        val date = user.getUpcomingBirthday()
        val eventTitle = "${user.getFullName()}'s Birthday"
        val intent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, eventTitle)
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, date)
        }

        // The appAvailable check fails for this, it seems to be an issue with the emulator as far
        // as I can tell
        context?.startActivity(intent)
    }

    /**
     * Attempt to launch a dialer app with the clicked number
     */
    fun onPhoneClicked(phoneNumber: String) {
        val number = phoneNumber.replace("[0-9]", "")
        val uri = Uri.parse("tel:$number")
        val intent = Intent(Intent.ACTION_DIAL, uri)

        // The appAvailable check fails for this, it seems to be an issue with the emulator as far
        // as I can tell
        context?.startActivity(intent)
    }

    /**
     * Attempt to launch a external email app.
     * Opens a new email composition to the email address that was clicked.
     */
    fun onEmailClicked(email: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, "Hello from AirVet!")
        }

        if (intent.appAvailable(context)) {
            context?.startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

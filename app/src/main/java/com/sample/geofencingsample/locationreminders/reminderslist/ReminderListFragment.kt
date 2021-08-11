package com.sample.geofencingsample.locationreminders.reminderslist

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.location.LocationServices
import com.sample.geofencingsample.R
import com.sample.geofencingsample.authentication.AuthenticationActivity
import com.sample.geofencingsample.base.BaseFragment
import com.sample.geofencingsample.base.NavigationCommand
import com.sample.geofencingsample.databinding.FragmentRemindersBinding
import com.sample.geofencingsample.utils.setDisplayHomeAsUpEnabled
import com.sample.geofencingsample.utils.setTitle
import com.sample.geofencingsample.utils.setup
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReminderListFragment : BaseFragment() {
    //use Koin to retrieve the ViewModel instance
    override val _viewModel: RemindersListViewModel by viewModel()
    private lateinit var binding: FragmentRemindersBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_reminders, container, false
            )
        binding.viewModel = _viewModel

        setHasOptionsMenu(true)
        setDisplayHomeAsUpEnabled(false)
        setTitle(getString(R.string.app_name))

        binding.refreshLayout.setOnRefreshListener {
            binding.refreshLayout.isRefreshing = true
            _viewModel.loadReminders().observe(viewLifecycleOwner, {
                binding.refreshLayout.isRefreshing = false
            })
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = this
        setupRecyclerView()
        binding.addReminderFAB.setOnClickListener {
            navigateToAddReminder()
        }
    }

    override fun onResume() {
        super.onResume()
        //load the reminders list on the ui
        _viewModel.loadReminders()
    }

    private fun navigateToAddReminder() {
        //use the navigationCommand live data to navigate between the fragments
        _viewModel.navigationCommand.postValue(
            NavigationCommand.To(
                ReminderListFragmentDirections.toSaveReminder()
            )
        )
    }


    private fun setupRecyclerView() {
        val adapter = RemindersListAdapter { item ->
            deleteReminder(item)
        }
//        setup the recycler view using the extension function
        binding.reminderssRecyclerView.setup(adapter)
    }

    private fun deleteReminder(reminder: ReminderDataItem) {
        LocationServices.getGeofencingClient(requireContext())
            .removeGeofences(listOf(reminder.id))
            .addOnSuccessListener {
                Toast.makeText(
                    context,
                    "Geofences have been removed!",
                    Toast.LENGTH_LONG
                ).show()
            }
            .addOnFailureListener {
                Toast.makeText(
                    context,
                    "Failed to remove geofences",
                    Toast.LENGTH_LONG
                ).show()
                Log.d("ReminderListFragment", "error ==> " + it.message)
            }
        _viewModel.deleteReminder(reminder.id)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
//                DONE: add the logout implementation
                context?.let {
                    AuthUI.getInstance()
                        .signOut(it)
                        .addOnCompleteListener {
                            val intent =
                                Intent(activity, AuthenticationActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            startActivity(intent)

                        }
                }
            }
        }
        return super.onOptionsItemSelected(item)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
//        display logout as menu item
        inflater.inflate(R.menu.main_menu, menu)
    }

}

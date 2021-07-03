package com.example.contactlist.ui.contactsList

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.contactlist.R
import com.example.contactlist.adapters.ContactListAdapter
import com.example.contactlist.dataModel.ContactModel
import com.example.contactlist.database.AppDatabase
import com.example.contactlist.databinding.ContactListFragmentBinding
import com.example.contactlist.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

internal val TAG = ContactList::class.java.canonicalName

class ContactList : Fragment(R.layout.contact_list_fragment) {


    private lateinit var _binding: ContactListFragmentBinding
    private val binding: ContactListFragmentBinding get() = _binding

    private lateinit var viewModel: ContactListViewModel

    private lateinit var contactListAdapter: ContactListAdapter

    private val contactInterface = object : AppInterface.ContactInterface {
        override fun onContactSelected(item: ContactModel) {
            val bundle = Bundle()
            bundle.putParcelable(AppConstance.Navigation.CONTACT_KEY, item)
            findNavController().navigate(R.id.action_contactList_to_contactDetailsFragment, bundle)
        }
    }

    lateinit var contactHelperClass: ContactHelperClass

    init {
        Log.i(TAG, "init: ")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "onViewCreated: ")
        _binding = ContactListFragmentBinding.bind(view)

        viewModel = ViewModelProvider(
            this,
            AppViewModelFactory(
                Repository.getInstance(
                    appDatabase = AppDatabase.getInstance(requireContext()),
                    context = requireContext()
                ),
                null
            )
        ).get(ContactListViewModel::class.java)

        setupRecyclerView()
        getContactCount()
        setupContactData()
        contactHelperClass = ContactHelperClass(
            requireContext(), Repository(
                requireContext(),
                AppDatabase.getInstance(requireContext())
            )
        )

        binding.swipeToRefresh.setOnRefreshListener {
            refreshContactData()
        }
    }

    private fun refreshContactData() {
        val newDataSet = contactHelperClass.getContactList()

        lifecycleScope.launch(Dispatchers.IO) {
            viewModel.syncLocalDatabase(newDataSet as List<ContactModel>)
        }

        binding.swipeToRefresh.isRefreshing = false
    }


    private fun setupContactData() {
        Log.i(TAG, "setupContactData: ")
        lifecycleScope.launch {
            viewModel.getContacts().collectLatest {
                contactListAdapter.submitData(it)
            }
        }
    }

    private fun setupRecyclerView() {
        contactListAdapter = ContactListAdapter(contactInterface)
        binding.contactRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = contactListAdapter
        }
    }

    private fun getContactCount() {
        viewModel.getContactCount().observe(viewLifecycleOwner, {
            if (it == 0) {
                Toast.makeText(requireContext(), "No Contacts available", Toast.LENGTH_LONG).show()
            }
        })
    }

}
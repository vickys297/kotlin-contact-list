package com.example.contactlist.ui.contactDetails

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.contactlist.R
import com.example.contactlist.dataModel.ContactModel
import com.example.contactlist.databinding.ContactDetailsFragmentBinding
import com.example.contactlist.util.AppConstance
import java.util.*


internal val TAG_CONTACT_DETAILS = ContactDetailsFragment::class.java.canonicalName

class ContactDetailsFragment : Fragment(R.layout.contact_details_fragment) {

    private lateinit var viewModel: ContactDetailsViewModel

    private lateinit var _binding: ContactDetailsFragmentBinding
    private val binding: ContactDetailsFragmentBinding get() = _binding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG_CONTACT_DETAILS, "onViewCreated: ")
        _binding = ContactDetailsFragmentBinding.bind(view)
        viewModel =
            ViewModelProvider(this@ContactDetailsFragment).get(ContactDetailsViewModel::class.java)
        setupBinding()
        viewModel.contactData.observe(viewLifecycleOwner, dataObserver)
        arguments?.let {
            it.getParcelable<ContactModel>(AppConstance.Navigation.CONTACT_KEY)
                ?.let { contactModel ->
                    Log.i(TAG_CONTACT_DETAILS, "onViewCreated: $contactModel")
                    viewModel.contactData.postValue(contactModel)
                }
        }

    }

    private fun setupBinding() {
        binding.apply {
            dataModel = this@ContactDetailsFragment.viewModel
            lifecycleOwner = this@ContactDetailsFragment
            executePendingBindings()
        }
    }

    private val dataObserver = Observer<ContactModel> { contactModel ->

        Glide.with(requireContext())
            .load(R.drawable.user_profile_fallback)
            .circleCrop()
            .into(binding.contactImage)


        Glide.with(requireContext())
            .load(R.drawable.user_detail_fallback)
            .centerCrop()
            .into(binding.userPicture)


        contactModel.pictureUri?.let {
            Glide.with(requireContext())
                .load(Uri.parse(contactModel.pictureUri))
                .centerCrop()
                .error(R.drawable.user_profile_fallback)
                .into(binding.userPicture)
        }

        contactModel.thumbnailUri?.let {
            Glide.with(requireContext())
                .load(Uri.parse(contactModel.pictureUri))
                .circleCrop()
                .error(R.drawable.user_profile_fallback)
                .into(binding.contactImage)
        }

        val layout = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        layout.setMargins(8, 8, 8, 8)

        if (contactModel.phone.isNotEmpty()) {
            for (p in contactModel.phone) {
                val textView = TextView(requireContext())
                textView.text = p
                textView.textSize = 18F
                textView.compoundDrawablePadding = 16
                textView.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_baseline_phone_24,
                    0,
                    0,
                    0
                )
                textView.layoutParams = layout
                binding.contactPhoneEmail.addView(textView)
            }
        }


        if (contactModel.email.isNotEmpty()) {
            for (e in contactModel.email) {
                val textView = TextView(requireContext())
                textView.layoutParams = layout
                textView.textSize = 18F
                textView.compoundDrawablePadding = 16

                textView.text = e
                textView.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_baseline_email_24,
                    0,
                    0,
                    0
                )
                binding.contactPhoneEmail.addView(textView)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.contactPhoneEmail.removeAllViews()
    }


}
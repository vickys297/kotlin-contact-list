package com.example.contactlist.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.contactlist.R
import com.example.contactlist.dataModel.ContactModel
import com.example.contactlist.databinding.RecyclerContactListBinding
import com.example.contactlist.util.AppInterface

class ContactListAdapter(private val contactInterface: AppInterface.ContactInterface) :
    PagingDataAdapter<ContactModel, ContactListAdapter.ContactViewHolder>(DIFF_CALLBACK) {


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ContactModel>() {
            override fun areItemsTheSame(oldItem: ContactModel, newItem: ContactModel): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ContactModel, newItem: ContactModel): Boolean {
                return oldItem == newItem
            }

        }
    }


    class ContactViewHolder(private val binding: RecyclerContactListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ContactModel): RecyclerContactListBinding {
            return binding.apply {
                dataModel = item
                executePendingBindings()
            }
        }
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val item = getItem(position)
        item?.run {
            holder.bind(item).let { bind ->
                holder.itemView.tag = this@run
                bind.contactName.text = item.name
                bind.contactName.isSelected = true


                Glide.with(bind.root)
                    .load(R.drawable.user_profile_fallback)
                    .circleCrop()
                    .into(bind.imageView)

                item.pictureUri?.let {
                    Glide.with(bind.root)
                        .load(Uri.parse(item.pictureUri))
                        .fallback(R.drawable.user_profile_fallback)
                        .error(R.drawable.user_profile_fallback)
                        .circleCrop()
                        .into(bind.imageView)
                }

                bindPhoneAndEmail(item, holder.itemView, bind)

                holder.itemView.setOnClickListener {
                    contactInterface.onContactSelected(item)
                }
            }
        }
    }

    private fun bindPhoneAndEmail(
        item: ContactModel,
        itemView: View,
        contactPhoneEmail: RecyclerContactListBinding
    ) {
        contactPhoneEmail.contactPhoneEmail.removeAllViews()

        val layout = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT
        )
        layout.setMargins(8, 8, 8, 8)

        if (item.phone.isNotEmpty()) {
            for (p in item.phone) {
                val textView = TextView(itemView.context)
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
                contactPhoneEmail.contactPhoneEmail.addView(textView)
            }
        }


        if (item.email.isNotEmpty()) {
            for (e in item.email) {
                val textView = TextView(itemView.context)
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
                contactPhoneEmail.contactPhoneEmail.addView(textView)
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        return ContactViewHolder(
            RecyclerContactListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }
}
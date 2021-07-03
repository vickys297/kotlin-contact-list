package com.example.contactlist

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultCallback
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.contactlist.dataModel.ContactModel
import com.example.contactlist.database.AppDatabase
import com.example.contactlist.databinding.ActivityLauncherBinding
import com.example.contactlist.util.ContactHelperClass
import com.example.contactlist.util.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

internal val TAG_Launcher = LauncherActivity::class.java.canonicalName

class LauncherActivity : AppCompatActivity() {


    private lateinit var _binding: ActivityLauncherBinding
    private val binding: ActivityLauncherBinding get() = _binding
    private lateinit var contactHelperClass: ContactHelperClass

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this@LauncherActivity, R.layout.activity_launcher)
        setContentView(binding.root)

        contactHelperClass = ContactHelperClass.getInstance(
            this@LauncherActivity, Repository.getInstance(
                context = this@LauncherActivity,
                AppDatabase.getInstance(this)
            )
        )

        binding.button.setOnClickListener {
            getContactList()
        }

        checkContactPermission()
    }

    private fun getContactList() {
        val arrayContactList = contactHelperClass.getContactList()
        insertContactList(arrayContactList)
    }

    private fun insertContactList(arrayContactModel: ArrayList<ContactModel>) {

        val appRepository =
            Repository.getInstance(this@LauncherActivity, AppDatabase.getInstance(this))

        lifecycleScope.launch(Dispatchers.IO) {
            appRepository.contactDatabase.updateList(arrayContactModel as List<ContactModel>)
        }

        startMainActivity()
    }


    private fun showContactPermissionRequiredDialog() {
        binding.progressBar.visibility = View.GONE
        binding.contactPermissionRequired.visibility = View.VISIBLE
    }

    private fun checkContactPermission() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(
                this@LauncherActivity,
                Manifest.permission.READ_CONTACTS
            ) -> {
                // You can use the API that requires the permission.
                startMainActivity()
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.


                requestPermissions(
                    arrayOf(Manifest.permission.READ_CONTACTS),
                    100
                )

            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            100 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() &&
                            grantResults[0] == PackageManager.PERMISSION_GRANTED)
                ) {
                    // Permission is granted. Continue the action or workflow
                    // in your app.
                    getContactList()
                } else {
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                    // At the same time, respect the user's decision. Don't link to
                    // system settings in an effort to convince the user to change
                    // their decision.
                    showContactPermissionRequiredDialog()
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
                showContactPermissionRequiredDialog()
            }
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this@LauncherActivity, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        })
        finish()
    }
}
package com.example.contactlist

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.contactlist.R
import com.example.contactlist.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {


    private lateinit var _binding: ActivityMainBinding
    private val binding: ActivityMainBinding get() = _binding
    private lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        setContentView(binding.root)


        val navGraph =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navGraph.navController

    }

    override fun onDestroy() {
        super.onDestroy()

    }
}
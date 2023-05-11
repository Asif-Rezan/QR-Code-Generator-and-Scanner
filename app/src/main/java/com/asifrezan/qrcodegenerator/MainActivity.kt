package com.asifrezan.qrcodegenerator

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.asifrezan.qrcodegenerator.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.analytics.FirebaseAnalytics

class MainActivity : AppCompatActivity() {


    private lateinit var binding:ActivityMainBinding;
    lateinit var firebaseAnalytics: FirebaseAnalytics



    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationBarId)
        val navController = findNavController(R.id.fragmentContainerView2)
//        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeFregment,R.id.settingsFragment,R.id.todoFragment))
//        setupActionBarWithNavController(navController,appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)

        bottomNavigationView.setItemIconTintList(ContextCompat.getColorStateList(this, R.drawable.selector));






    }













}
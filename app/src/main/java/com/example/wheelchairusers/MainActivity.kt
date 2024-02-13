package com.example.wheelchairusers


import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()




        val NavHostFragment = supportFragmentManager.findFragmentById(R.id.mainContainer) as NavHostFragment
        navController = NavHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)


        var database = FirebaseDatabase.getInstance().getReference("users")
        database.child(auth.currentUser!!.uid).get().addOnSuccessListener {

            if (it.exists()){
                val admin = it.child("admin").value
                val carer = it.child("carers").value

                val menu = bottomNavigationView.menu
                val menuItem = menu.findItem(R.id.helpFragment)

                menuItem?.isVisible = admin==true||carer==true
            }
        }

        setupWithNavController(bottomNavigationView , navController)



    }


    }




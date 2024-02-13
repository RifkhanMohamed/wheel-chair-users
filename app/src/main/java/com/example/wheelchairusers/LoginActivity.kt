package com.example.wheelchairusers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        performLogin()
        registerRedirect()
    }

    private fun performLogin() {
        val email = findViewById<EditText>(R.id.editText_email_login)
        val password = findViewById<EditText>(R.id.editText_password_login)
        val loginButton = findViewById<Button>(R.id.login)
        val loginProgressBar = findViewById<ProgressBar>(R.id.loginProgressBar)
        loginButton.setOnClickListener{
            val inputEmail = email.text.toString()
            val inputPassword = password.text.toString()
            loginProgressBar.visibility = View.VISIBLE

            if (inputEmail.isEmpty() || inputPassword.isEmpty()){
                if(inputEmail.isEmpty()){
                    email.error = "Enter your email address"
                }
                if (inputPassword.isEmpty()){
                    password.error = "Enter your password"
                }
                loginProgressBar.visibility = View.GONE
                Toast.makeText(this, "Enter valid details", Toast.LENGTH_SHORT).show()
            }
            else if(!inputEmail.matches(emailPattern.toRegex())){
                loginProgressBar.visibility = View.GONE
                email.error = "Enter valid email address"
                Toast.makeText(this, "Enter valid email address", Toast.LENGTH_SHORT).show()
            }
            else if(inputPassword.length>6){
                loginProgressBar.visibility = View.GONE
                password.error = "Enter password more than 6 characters"
                Toast.makeText(this, "Enter password more than 6 characters", Toast.LENGTH_SHORT).show()
            }
            else{
                auth.signInWithEmailAndPassword(inputEmail,inputPassword).addOnCompleteListener{
                    if (it.isSuccessful){
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this, "Please check your email and password", Toast.LENGTH_SHORT).show()
                        loginProgressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun registerRedirect(){
        val registerText: TextView = findViewById(R.id.textView_register_now)
        registerText.setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}
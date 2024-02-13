package com.example.wheelchairusers

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.wheelchairusers.model.Users
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlin.properties.Delegates

class RegisterActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+"
    private var carer =false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        val switchButton = findViewById<Switch>(R.id.carer)

        switchButton.setOnCheckedChangeListener { _, isChecked ->
            carer = isChecked
        }
        performSignUp()
        loginRedirect()


    }

    private fun performSignUp() {
        val name = findViewById<EditText>(R.id.editText_name_register)
        val id = findViewById<EditText>(R.id.editText_id_register)
        val email = findViewById<EditText>(R.id.editText_email_register)
        val phone = findViewById<EditText>(R.id.editText_phone_register)
        val password = findViewById<EditText>(R.id.editText_password_register)
        val registerButton = findViewById<Button>(R.id.register)

        val registerProgressBar = findViewById<ProgressBar>(R.id.registerProgressBar)
        registerButton.setOnClickListener{
            val inputName = name.text.toString()
            val inputId = id.text.toString()
            val inputEmail = email.text.toString()
            val inputPhone = phone.text.toString()
            val inputPassword = password.text.toString()


            registerProgressBar.visibility = View.VISIBLE

            if (inputName.isEmpty() || inputId.isEmpty() || inputEmail.isEmpty() || inputPhone.isEmpty() || inputPassword.isEmpty()){
                if (inputName.isEmpty()){
                    name.error = "Enter your name"
                }
                if (inputId.isEmpty()){
                    id.error = "Enter your student id"
                }
                if (inputEmail.isEmpty()){
                    email.error = "Enter your email address"
                }
                if (inputPhone.isEmpty()){
                    phone.error = "Enter your phone number"
                }
                if (inputPassword.isEmpty()){
                    password.error = "Enter the password"
                }
                Toast.makeText(this, "Enter valid details", Toast.LENGTH_SHORT).show()
                registerProgressBar.visibility = View.GONE
            }
            else if(!inputEmail.matches(emailPattern.toRegex())){
                registerProgressBar.visibility = View.GONE
                email.error = "Enter valid email address"
                Toast.makeText(this, "Enter valid email address", Toast.LENGTH_SHORT).show()
            }
            else if(inputPhone.length != 10){
                registerProgressBar.visibility = View.GONE
                phone.error = "Phone number should be 10 digits"
                Toast.makeText(this, "Enter valid phone number and number should be 10 digits", Toast.LENGTH_SHORT).show()
            }
            else if(inputPassword.length>6){
                registerProgressBar.visibility = View.GONE
                password.error = "Enter password more than 6 characters"
                Toast.makeText(this, "Enter password more than 6 characters", Toast.LENGTH_SHORT).show()
            }
            else{
                auth.createUserWithEmailAndPassword(inputEmail,inputPassword).addOnCompleteListener{
                    if(it.isSuccessful){
                        val databaseRef = database.reference.child("users").child(auth.currentUser!!.uid)
                        val users : Users = Users(inputName,inputEmail,inputPhone,inputId,auth.currentUser!!.uid,
                            admin = false,
                            carers = carer
                        )

                        databaseRef.setValue(users).addOnCompleteListener{
                            if (it.isSuccessful){
                                val intent = Intent(this, LoginActivity::class.java)
                                startActivity(intent)
                            }else{
                                Toast.makeText(this, "Something went wrong, try again", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }else{
                        Toast.makeText(this, "Something went wrong, try again", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun loginRedirect(){
        val loginText: TextView = findViewById(R.id.textView_login_now)
        loginText.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
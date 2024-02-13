package com.example.wheelchairusers

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.auth.FirebaseAuth


class SplashScreen : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        auth = FirebaseAuth.getInstance()

        val appName:TextView= findViewById<TextView>(R.id.appName)
        val lottie:LottieAnimationView= findViewById<LottieAnimationView>(R.id.animationView)

        appName.animate().translationY(-1400.0F).setDuration(2700).setStartDelay(0);
        lottie.animate().translationX(2000.0F).setDuration(2000).setStartDelay(2900);

    Handler(Looper.getMainLooper()).postDelayed({
//var intent =Intent(this,LoginActivity::class.java)
        var intent = if (auth.currentUser!=null){
            Intent(this, MainActivity::class.java)
        }else{
            Intent(this, LoginActivity::class.java)
        }

        startActivity(intent)
        finish()
    }, 5000)
    }
}
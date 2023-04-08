package com.myprojects.pragati.activities

import SharedPrefManager
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.myprojects.pragati.R

class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val sharedPrefManager = SharedPrefManager(this)

        Handler().postDelayed({
            if (sharedPrefManager.getToken() != null) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
            }
            finish()
        }, 1000)
    }
}
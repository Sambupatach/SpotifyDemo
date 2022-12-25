package com.lowes.demoapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.lowes.demoapp.R

private const val TAG = "SplashScreenActivity"
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG,"onResume")
        Handler(Looper.getMainLooper()).postDelayed({
            Log.d(TAG,"Launch HomeActivity")
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }, 1000)

    }
}
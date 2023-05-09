package com.asifrezan.qrcodegenerator.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import com.asifrezan.qrcodegenerator.MainActivity
import com.asifrezan.qrcodegenerator.R

class SplashScreen : AppCompatActivity() {

    private val SPLASH_DISPLAY_LENGTH = 3000L // 3 seconds
    private var progressBarVisible = false
    lateinit var progressBar : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)



        val splashTimer = object : Thread() {
            override fun run() {
                try {
                    sleep(3000) // 3 seconds delay

                } catch (e: InterruptedException) {
                    e.printStackTrace()

                } finally {
                    val intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
        splashTimer.start()
    }

}
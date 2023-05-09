package com.asifrezan.qrcodegenerator.activities
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.asifrezan.qrcodegenerator.R

class ShowScanResult : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_scan_result)








        val code = intent.getStringExtra("code")
        val textView = findViewById<TextView>(R.id.textViewId)
        textView.text = code









    }









}
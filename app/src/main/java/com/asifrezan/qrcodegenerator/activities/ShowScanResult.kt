package com.asifrezan.qrcodegenerator.activities
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.asifrezan.qrcodegenerator.R

class ShowScanResult : AppCompatActivity() {
    private lateinit var search_button : ImageView
    private lateinit var copy_button : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_scan_result)


        search_button = findViewById(R.id.search_button)
        copy_button = findViewById(R.id.copy_button)



        val code = intent.getStringExtra("code")
        val textView = findViewById<TextView>(R.id.textViewId)
        textView.text = code


        search_button.setOnClickListener(View.OnClickListener {
            val query = code
            val url = "https://www.google.com/search?q=$query"
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)
            startActivity(intent)
        })

        copy_button.setOnClickListener(View.OnClickListener {

            val textToCopy = code
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText("text", textToCopy))
            Toast.makeText(this, "Text copied to clipboard", Toast.LENGTH_SHORT).show()

        })










    }









}
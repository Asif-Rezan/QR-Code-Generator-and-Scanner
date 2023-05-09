package com.asifrezan.qrcodegenerator.activities

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.asifrezan.qrcodegenerator.R
import com.asifrezan.qrcodegenerator.data.MyData
import com.asifrezan.qrcodegenerator.db.DBHelper
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.time.LocalDateTime
import java.util.*

class EditActivity : AppCompatActivity() {

    private var myData: MyData? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        val id = intent.getLongExtra("id", -1)
        if (id == 1L) {
            // handle invalid ID
            return
        }

        val dbHelper = DBHelper(this)
        myData = dbHelper.getDataById(id)
        if (myData == null) {
           Toast.makeText(this,"invalid Id"+id,Toast.LENGTH_SHORT).show()
            return
        }

        // populate UI fields with data


        val editText = findViewById<EditText>(R.id.editText)
           editText.setText(myData!!.name)
        val imageView = findViewById<ImageView>(R.id.image_view)



        val contents =  editText.text.toString()
        val format = BarcodeFormat.QR_CODE

        try {
            val barcodeEncoder = BarcodeEncoder()
            val bitmap: Bitmap = barcodeEncoder.encodeBitmap(contents, format, 200, 200)
            imageView.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
            Toast.makeText(this, "Failed to generate QR code", Toast.LENGTH_SHORT).show()
        }




        val button = findViewById<Button>(R.id.button)


        button.setOnClickListener {
            val contents =  editText.text.toString()


            val currentDateTime = LocalDateTime.now()

            val dbHelper = DBHelper(this)
            dbHelper.updateData(id,contents,currentDateTime.toString())




            val format = BarcodeFormat.QR_CODE

            try {
                val barcodeEncoder = BarcodeEncoder()
                val bitmap: Bitmap = barcodeEncoder.encodeBitmap(contents, format, 200, 200)
                imageView.setImageBitmap(bitmap)
            } catch (e: WriterException) {
                e.printStackTrace()
                Toast.makeText(this, "Failed to generate QR code", Toast.LENGTH_SHORT).show()
            }

        }






    }




    }

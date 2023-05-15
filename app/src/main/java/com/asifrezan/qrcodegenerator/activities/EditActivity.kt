package com.asifrezan.qrcodegenerator.activities

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
import com.asifrezan.qrcodegenerator.BuildConfig
import com.asifrezan.qrcodegenerator.R
import com.asifrezan.qrcodegenerator.data.MyData
import com.asifrezan.qrcodegenerator.db.DBHelper
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.util.*

class EditActivity : AppCompatActivity() {

    private var myData: MyData? = null
    var bitmap: Bitmap? = null
    var generated_code = false


    @SuppressLint("SuspiciousIndentation")
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
        val ratingButton = findViewById<ImageView>(R.id.ratingButton)
        val downloadButton = findViewById<ImageView>(R.id.downloadButton)
        val shareButton = findViewById<ImageView>(R.id.shareButton)



        val contents =  editText.text.toString()
        val format = BarcodeFormat.QR_CODE

        try {
            val barcodeEncoder = BarcodeEncoder()
            bitmap = barcodeEncoder.encodeBitmap(contents, format, 1000, 1000)
            imageView.setImageBitmap(bitmap)
            generated_code = true
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
                val bitmap: Bitmap = barcodeEncoder.encodeBitmap(contents, format, 1000, 1000)
                imageView.setImageBitmap(bitmap)
                generated_code = true
            } catch (e: WriterException) {
                e.printStackTrace()
                Toast.makeText(this, "Failed to generate QR code", Toast.LENGTH_SHORT).show()
            }

        }






        downloadButton.setOnClickListener(View.OnClickListener {

            //   val bitmap = takeScreenshot(imageView)
            if (generated_code)
            {
                saveScreenshot(bitmap!!)

            }
            else
            {
                Toast.makeText(this, "Generate a code to download", Toast.LENGTH_SHORT).show()
            }




        })

        ratingButton.setOnClickListener(View.OnClickListener {

            val packageName = "com.asifrezan.qrcodegenerator"
            val playStoreUri = Uri.parse("market://details?id=$packageName")
            val playStoreIntent = Intent(Intent.ACTION_VIEW, playStoreUri)
            playStoreIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
            try {
                startActivity(playStoreIntent)
            }
            catch (e: ActivityNotFoundException) {
                val webPlayStoreUri = Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                val webPlayStoreIntent = Intent(Intent.ACTION_VIEW, webPlayStoreUri)
                startActivity(webPlayStoreIntent)
            }


        })


        shareButton.setOnClickListener(View.OnClickListener {

            if (generated_code)
            {



                val file = File(this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "qr-code.png")
                val outputStream = FileOutputStream(file)
                bitmap?.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.flush()
                outputStream.close()

                // Create the intent to share the image
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "image/png"
                val uri = FileProvider.getUriForFile(this, "${BuildConfig.APPLICATION_ID}.fileprovider", file)
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                // Get read permission for the content URI
                this.let {
                    try {
                        val contentUri = FileProvider.getUriForFile(it, "${BuildConfig.APPLICATION_ID}.fileprovider", file)
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        intent.setDataAndType(contentUri, "image/png")
                        startActivity(intent)
                    } catch (e: ActivityNotFoundException) {

                        e.printStackTrace()
                    }
                }

                // Show the share sheet
                startActivity(Intent.createChooser(shareIntent, "Share image"))

            }
            else
            {
                Toast.makeText(this, "Generate a code to share", Toast.LENGTH_SHORT).show()
            }








//                val file = File(requireContext().externalCacheDir, "image.png")
//                val outputStream = FileOutputStream(file)
//                bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
//                outputStream.flush()
//                outputStream.close()
//
//                // Create the intent to share the image
//                val shareIntent = Intent(Intent.ACTION_SEND)
//                shareIntent.type = "image/png"
//                val uri = FileProvider.getUriForFile(requireContext(), "${BuildConfig.APPLICATION_ID}.fileprovider", file)
//                shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
//
//                // Show the share sheet
//                startActivity(Intent.createChooser(shareIntent, "Share image"))



        })









    }





    private fun takeScreenshot(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun saveScreenshot(bitmap: Bitmap) {
        val fileName = "qr_${System.currentTimeMillis()}.png"
        val dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/Qrcode/"
        val dir = File(dirPath)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val file = File(dirPath, fileName)
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.flush()
        showNotification(fileName)
        Toast.makeText(this,"Download complited", Toast.LENGTH_SHORT).show()
        outputStream.close()
        MediaScannerConnection.scanFile(this, arrayOf(file.toString()), arrayOf("image/jpeg"), null)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Downloads"
            val descriptionText = "Download completed"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("download_channel", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showNotification(fileName: String) {
//        val file = File(Environment.getExternalStorageDirectory(), fileName)
//        val filePath = file.absolutePath

        val dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString() + "/Qrcode/"

        //  val fileName = System.currentTimeMillis().toString() + ".png"
        val filePath = "$dirPath$fileName"

        // ... create the intent that opens the gallery app ...

        val file = File(filePath)
        val contentUri = FileProvider.getUriForFile(
            this,
            "${this.packageName}.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(contentUri, "image/jpeg")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)


        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = NotificationCompat.Builder(this, "download_channel")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Download completed")
            .setContentText("Your file has been downloaded successfully")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(this)
        notificationManager.notify(1, builder.build())
    }




    }

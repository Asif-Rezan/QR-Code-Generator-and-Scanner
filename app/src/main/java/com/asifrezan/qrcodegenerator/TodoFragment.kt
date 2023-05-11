package com.asifrezan.qrcodegenerator

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.asifrezan.qrcodegenerator.data.MyData
import com.asifrezan.qrcodegenerator.db.DBHelper
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime


class TodoFragment : Fragment() {

    private var myData: MyData? = null
    lateinit var bitmap: Bitmap

    companion object {
        private const val ARG_ID = "arg_id"

        fun newInstance(id: Int): TodoFragment {
            val fragment = TodoFragment()
            val args = Bundle().apply {
                putInt(ARG_ID, id)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        createNotificationChannel()

        val view = inflater.inflate(R.layout.fragment_todo, container, false)
        val imageView = view.findViewById<ImageView>(R.id.image_view)
        val button = view.findViewById<Button>(R.id.button)
        val editText = view.findViewById<EditText>(R.id.editText)
        val titleEditText = view.findViewById<EditText>(R.id.titleEditText)
        val downloadButton = view.findViewById<Button>(R.id.downloadButton)

        button.setOnClickListener {
            val contents =  editText.text.toString()
            val title = titleEditText.text.toString()
            var flag:Boolean
                flag =true

            if (title.isEmpty()) {
                titleEditText.setError("Title is required");
                flag = false;
            }
            if (contents.isEmpty())
            {
                editText.setError("Please enter text for genarate QR Code");
                flag = false;
            }

            if(flag) {


                val currentDateTime = LocalDateTime.now()


            val dbHelper = DBHelper(requireContext())
            dbHelper.addData(title, contents,currentDateTime.toString())

            val format = BarcodeFormat.QR_CODE

            try {
                val barcodeEncoder = BarcodeEncoder()
                bitmap = barcodeEncoder.encodeBitmap(contents, format, 1000, 1000)
                imageView.setImageBitmap(bitmap)
            } catch (e: WriterException) {
                e.printStackTrace()
                Toast.makeText(context, "Failed to generate QR code", Toast.LENGTH_SHORT).show()
            }

        }
        }


        downloadButton.setOnClickListener(View.OnClickListener {

         //   val bitmap = takeScreenshot(imageView)
            saveScreenshot(bitmap)

        })




        return view
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
        Toast.makeText(requireContext(),"Download complited", Toast.LENGTH_SHORT).show()
        outputStream.close()
        MediaScannerConnection.scanFile(requireContext(), arrayOf(file.toString()), arrayOf("image/jpeg"), null)
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
                requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
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
            requireContext(),
            "${requireContext().packageName}.fileprovider",
            file
        )

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(contentUri, "image/jpeg")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)


        val pendingIntent = PendingIntent.getActivity(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder = NotificationCompat.Builder(requireContext(), "download_channel")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Download completed")
            .setContentText("Your file has been downloaded successfully")
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(requireContext())
        notificationManager.notify(1, builder.build())
    }


















}
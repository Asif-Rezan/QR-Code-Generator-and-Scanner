package com.asifrezan.qrcodegenerator

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.asifrezan.qrcodegenerator.data.MyData
import com.asifrezan.qrcodegenerator.db.DBHelper
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import java.time.LocalDateTime


class TodoFragment : Fragment() {

    private var myData: MyData? = null

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
        val view = inflater.inflate(R.layout.fragment_todo, container, false)
        val imageView = view.findViewById<ImageView>(R.id.image_view)
        val button = view.findViewById<Button>(R.id.button)
        val editText = view.findViewById<EditText>(R.id.editText)
        val titleEditText = view.findViewById<EditText>(R.id.titleEditText)
       // val downloadButton = view.findViewById<Button>(R.id.downloadButton)

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
                val bitmap: Bitmap = barcodeEncoder.encodeBitmap(contents, format, 200, 200)
                imageView.setImageBitmap(bitmap)
            } catch (e: WriterException) {
                e.printStackTrace()
                Toast.makeText(context, "Failed to generate QR code", Toast.LENGTH_SHORT).show()
            }

        }
        }

      //  downloadButton.setOnClickListener {


//            try {
//                val bitmap = (imageView.drawable as BitmapDrawable).bitmap
//                val filename = "QRCode.png"
//                val file = File(context?.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), filename)
//                val fOut = FileOutputStream(file)
//                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut)
//                fOut.flush()
//                fOut.close()
//                val uri = FileProvider.getUriForFile(requireContext(), BuildConfig.APPLICATION_ID + ".provider", file)
//                val intent = Intent(Intent.ACTION_CREATE_DOCUMENT)
//                intent.addCategory(Intent.CATEGORY_OPENABLE)
//                intent.type = "image/png"
//                intent.putExtra(Intent.EXTRA_TITLE, filename)
//                intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri)
//                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
//                startActivity(intent)
//
//
//            } catch (e: Exception) {
//                e.printStackTrace()
//                Toast.makeText(context, "Failed to download QR code", Toast.LENGTH_SHORT).show()
//            }
  //      }





















        return view
    }




}
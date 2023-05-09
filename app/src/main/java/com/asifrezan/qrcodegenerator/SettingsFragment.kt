package com.asifrezan.qrcodegenerator

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.asifrezan.qrcodegenerator.activities.ShowScanResult
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {



    companion object {
        private const val TAG = "SettingsFragment"
        private const val CAMERA_PERMISSION_CODE = 100
    }

    private lateinit var cameraSource: CameraSource
    private val CAMERA_PERMISSION_REQUEST_CODE = 100

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)




        // Check for camera permission
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_CODE
            )
        } else {
            startCamera()
        }
    }

    private fun startCamera() {
        val barcodeDetector = BarcodeDetector.Builder(requireContext())
            .setBarcodeFormats(Barcode.QR_CODE)
            .build()

        if (!barcodeDetector.isOperational) {
            Log.w(TAG, "Detector dependencies are not yet available.")
            return
        }

        cameraSource = CameraSource.Builder(requireContext(), barcodeDetector)
            .setAutoFocusEnabled(true)
            .build()

        val previewView = view?.findViewById<SurfaceView>(R.id.camera_preview)

        previewView?.holder?.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.CAMERA
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            requireActivity(),
                            arrayOf(Manifest.permission.CAMERA),
                            CAMERA_PERMISSION_REQUEST_CODE
                        )
                        return
                    }
                    cameraSource.start(previewView.holder)
                } catch (e: Exception) {
                    Log.e(TAG, "Error starting camera source: ${e.message}")
                }
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                // Do nothing
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })

        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
                // Do nothing
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val qrCodes = detections.detectedItems
                if (qrCodes.size() > 0) {
                    val qrCodeValue = qrCodes.valueAt(0).displayValue
                    Log.d(TAG, "QR code detected: $qrCodeValue")

                    val vibrator = ContextCompat.getSystemService(requireContext(), Vibrator::class.java)

                    if (vibrator?.hasVibrator() == true) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibrator.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE))
                        } else {
                            @Suppress("DEPRECATION")
                            vibrator.vibrate(500)
                        }
                    }


                    activity!!.runOnUiThread {


                        val intent = Intent(requireContext(), ShowScanResult::class.java)
                        intent.putExtra("code", qrCodeValue)
                        cameraSource.stop()
                        startActivity(intent)
                        Log.e("eee","eee")



                    }
                }
            }
        })
    }


//    private fun startCamera() {
//        val barcodeDetector = BarcodeDetector.Builder(requireContext())
//            .setBarcodeFormats(Barcode.QR_CODE)
//            .build()
//
//        if (!barcodeDetector.isOperational) {
//            Log.w(TAG, "Detector dependencies are not yet available.")
//            return
//        }
//
//        cameraSource = CameraSource.Builder(requireContext(), barcodeDetector)
//            .setAutoFocusEnabled(true)
//            .build()
//
//        var previewView = view?.findViewById<SurfaceView>(R.id.camera_preview)
//
//        previewView?.holder?.addCallback(object : SurfaceHolder.Callback {
//            override fun surfaceCreated(holder: SurfaceHolder) {
//                try {
//                    if (ActivityCompat.checkSelfPermission(
//                            requireContext(),
//                            Manifest.permission.CAMERA
//                        ) != PackageManager.PERMISSION_GRANTED
//                    ) {
//                        // TODO: Consider calling
//                        //    ActivityCompat#requestPermissions
//                        // here to request the missing permissions, and then overriding
//                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                        //                                          int[] grantResults)
//                        // to handle the case where the user grants the permission. See the documentation
//                        // for ActivityCompat#requestPermissions for more details.
//                        return
//                    }
//                    cameraSource.start(previewView?.holder)
//                } catch (e: IOException) {
//                    Log.e(TAG, "Error starting camera source: ${e.message}")
//                }
//            }
//
//            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
//                // Do nothing
//            }
//
//            override fun surfaceDestroyed(holder: SurfaceHolder) {
//                cameraSource.stop()
//            }
//        })
//
//        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
//            override fun release() {
//                // Do nothing
//            }
//
//            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
//                val qrCodes = detections.detectedItems
//                if (qrCodes.size() > 0) {
//                    val qrCodeValue = qrCodes.valueAt(0).displayValue
//                    Log.d(TAG, "QR code detected: $qrCodeValue")
//
//
//
//                    activity?.runOnUiThread {
//                      //  Toast.makeText(requireContext(), qrCodeValue, Toast.LENGTH_SHORT).show()
//
//                        val intent = Intent(requireContext(), ShowScanResult::class.java)
//                        intent.putExtra("code", qrCodeValue)
//                        startActivity(intent)
//                        cameraSource.stop()
//
//
//
//                    }
//                }
//            }
//        })
//    }

    override fun onDestroy() {
        super.onDestroy()
        cameraSource.release()
    }

}

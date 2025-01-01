package com.miawoltn.toasterlibrary

import android.Manifest
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.vmadalin.easypermissions.EasyPermissions
import java.io.ByteArrayOutputStream
import java.io.File


class Helpers {
    companion object {
        val PERMISSION_REQUEST_CODE = 100
        @Throws(java.lang.Exception::class)
        fun encodeFileToBase64Binary(fileName: String?): String? {
            val file = File(fileName)
            return Base64.encodeToString(FileCodecBase64.loadFileAsBytesArray(fileName), Base64.DEFAULT)
        }

         fun hasCameraPermission(context: Context?): Boolean {
            return EasyPermissions.hasPermissions(context, Manifest.permission.CAMERA)
        }

        fun hasLocationPermission(context: Context?): Boolean {
            return EasyPermissions.hasPermissions(context, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        }

         fun requestCameraPermission(context: Context?, activity: Activity) {
            if(hasCameraPermission(context)) return
            EasyPermissions.requestPermissions(
                host = activity,
                rationale = "You need to grant camera access to capture face",
                requestCode = PERMISSION_REQUEST_CODE,
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
        }

        fun requestLocationPermission(context: Context, activity: Activity) {
            if(hasLocationPermission(context)) return
            EasyPermissions.requestPermissions(
                host = activity,
                rationale = "Please enable location to be able to capture beneficiaries without NIN.",
                requestCode = PERMISSION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
            )
        }

        fun base64ImageToBitmap(encodedImage: String?): Bitmap? {
            return try {
                val decodedString = Base64.decode(encodedImage, 0)
                 BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            }catch (e: Exception) {
                e.printStackTrace()
                null
            }

        }

        fun bitmapToBase64(bitmap: Bitmap): String? {
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream)
            return Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT)
        }

        fun decodeJwtPayload(token: String): String {
            val parts = token.split(".")
            return try {
                val charset = charset("UTF-8")
//                val header = String(Base64.decode(parts[0].toByteArray(charset), 0), charset)
                val payload = String(Base64.decode(parts[1].toByteArray(charset), 0), charset)
                payload
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }
    }
}
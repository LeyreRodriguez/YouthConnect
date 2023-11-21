package com.example.youthconnect.View.QR


import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream

class DisplayQRCode {



     fun generateQRCode(text: String): Bitmap? {
        val width = 400
        val height = 400
        val multiFormatWriter = MultiFormatWriter()
        return try {
            val bitMatrix: BitMatrix =
                multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, width, height)
            val bitmap = createBitmap(width, height, Bitmap.Config.RGB_565)

            for (x in 0 until width) {
                for (y in 0 until height) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            bitmap
        } catch (e: WriterException) {
            e.printStackTrace()
            null
        }
    }




    fun generateQRCodeAndUpload(text: String) {
        val qrBitmap = generateQRCode(text)
        qrBitmap?.let {
            // Crear un ByteArray de la imagen en formato JPEG
            val outputStream = ByteArrayOutputStream()
            qrBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            val data = outputStream.toByteArray()

            // Referencia al Firebase Storage
            val storage = Firebase.storage
            val storageRef = storage.reference

            // Nombre del archivo en el Storage (puedes cambiarlo si lo deseas)
            val fileName = text

            // Referencia al archivo en el Storage
            val fileRef = storageRef.child(fileName)

            // Subir el archivo al Storage
            val uploadTask = fileRef.putBytes(data)

            // Manejar el éxito o el fallo de la carga
            uploadTask.addOnSuccessListener {
                // La imagen se cargó exitosamente
                fileRef.downloadUrl.addOnSuccessListener { uri ->
                    val downloadUrl = uri.toString()
                    // Haz lo que necesites con la URL de descarga (p. ej., guardarla en la base de datos)
                }.addOnFailureListener {
                    // Manejar errores al obtener la URL de descarga
                }
            }.addOnFailureListener {
                // Manejar fallos en la carga de la imagen
            }
        }
    }






}
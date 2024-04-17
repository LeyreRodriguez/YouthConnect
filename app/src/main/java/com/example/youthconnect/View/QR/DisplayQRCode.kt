package com.example.youthconnect.View.QR


import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.Color
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix


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








}
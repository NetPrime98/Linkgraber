package com.example.linkgrabber.data.local

import android.content.Context
import android.os.Environment
import java.io.File

object FileManager {
    
    /**
     * دریافت مسیر دانلود برای ذخیره ویدیوها
     * @param context برنامه context
     * @param useExternalStorage اگر true، از فضای خارجی استفاده کند
     * @return مسیر دانلود
     */
    fun getDownloadDirectory(context: Context, useExternalStorage: Boolean = true): File {
        return if (useExternalStorage && Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "LinkGrabber").apply {
                if (!exists()) mkdirs()
            }
        } else {
            File(context.getExternalFilesDir("downloads") ?: context.cacheDir, "LinkGrabber").apply {
                if (!exists()) mkdirs()
            }
        }
    }
    
    /**
     * دریافت مسیر فایل موقت
     */
    fun getTempDirectory(context: Context): File {
        return File(context.cacheDir, "temp").apply {
            if (!exists()) mkdirs()
        }
    }
    
    /**
     * حذف فایل موقت
     */
    fun clearTempDirectory(context: Context) {
        getTempDirectory(context).deleteRecursively()
    }
    
    /**
     * دریافت فضای خالی برای ذخیره
     */
    fun getAvailableSpace(directory: File): Long {
        return directory.freeSpace
    }
    
    /**
     * ایجاد نام فایل منحصر به فرد
     */
    fun generateUniqueFileName(title: String, extension: String = "mp4"): String {
        val timestamp = System.currentTimeMillis()
        val cleanedTitle = title.replace(Regex("[^a-zA-Z0-9]"), "_").take(30)
        return "${cleanedTitle}_${timestamp}.$extension"
    }
}

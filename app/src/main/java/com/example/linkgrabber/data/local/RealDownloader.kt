package com.example.linkgrabber.data.local

import android.content.Context
import com.example.linkgrabber.data.model.DownloadItem
import com.example.linkgrabber.data.remote.PermissionManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

class RealDownloader(
    private val context: Context
) {
    
    suspend fun downloadFileWithProgress(
        mediaUrl: String,
        fileName: String = "download",
        onProgress: (RealDownloadProgress) -> Unit
    ): Result<File> = withContext(Dispatchers.IO) {
        try {
            // بررسی اجازه‌ها
            if (!PermissionManager.hasAllRequiredPermissions(context)) {
                return@withContext Result.failure(
                    Exception("اجازه‌های لازم موجود نیست")
                )
            }
            
            // دریافت مسیر دانلود
            val downloadDir = FileManager.getDownloadDirectory(context)
            val file = File(downloadDir, fileName)
            
            // بررسی فضای خالی
            val availableSpace = FileManager.getAvailableSpace(downloadDir)
            if (availableSpace < 100 * 1024 * 1024) { // کمتر از 100MB
                return@withContext Result.failure(
                    Exception("فضای خالی کافی نیست")
                )
            }
            
            // اتصال HTTP
            val url = URL(mediaUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.apply {
                connectTimeout = 30000  // 30 ثانیه
                readTimeout = 60000     // 1 دقیقه
                requestMethod = "GET"
                setRequestProperty("User-Agent", "LinkGrabber/1.0")
            }
            
            // دریافت اندازه فایل
            val contentLength = connection.contentLength.toLong()
            if (contentLength <= 0) {
                return@withContext Result.failure(
                    Exception("نتوانستند اندازه فایل را دریافت کنند")
                )
            }
            
            // دانلود فایل
            val input = connection.inputStream
            file.outputStream().use { output ->
                val buffer = ByteArray(8192)
                var bytesRead: Int
                var totalBytes = 0L
                
                while (input.read(buffer).also { bytesRead = it } != -1) {
                    output.write(buffer, 0, bytesRead)
                    totalBytes += bytesRead
                    
                    // گزارش پیشرفت
                    val progress = (totalBytes * 100) / contentLength
                    onProgress(RealDownloadProgress(
                        progress = progress.toInt(),
                        downloadedBytes = totalBytes,
                        totalBytes = contentLength,
                        speed = calculateSpeed(totalBytes)
                    ))
                }
            }
            
            connection.disconnect()
            return@withContext Result.success(file)
            
        } catch (e: Exception) {
            onProgress(RealDownloadProgress(error = e.message ?: "خطای نامشخص"))
            return@withContext Result.failure(e)
        }
    }
    
    private fun calculateSpeed(bytes: Long): String {
        val mb = bytes / (1024 * 1024)
        return if (mb > 0) "$mb MB" else "${bytes / 1024} KB"
    }
}

data class RealDownloadProgress(
    val progress: Int = 0,
    val downloadedBytes: Long = 0L,
    val totalBytes: Long = 0L,
    val speed: String = "",
    val error: String? = null
)

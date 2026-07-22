package com.example.linkgrabber.data.model

data class DownloadItem(
    val id: String,
    val title: String,
    val url: String,
    val platform: String,
    val thumbnail: String,
    val downloadedAt: Long,
    val quality: String,
    val fileSize: Long = 0L,
    val filePath: String = "",
    val status: String = "completed"
)

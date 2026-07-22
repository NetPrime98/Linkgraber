package com.example.linkgrabber.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class MediaPlatform(val displayName: String, val brandColor: Color, val iconResId: Int) {
    YOUTUBE("YouTube", Color(0xFFFF0000), android.R.drawable.ic_media_play),
    INSTAGRAM("Instagram", Color(0xFFE1306C), android.R.drawable.ic_menu_camera),
    TIKTOK("TikTok", Color(0xFF000000), android.R.drawable.ic_media_play),
    FACEBOOK("Facebook", Color(0xFF1877F2), android.R.drawable.ic_dialog_info),
    TWITTER("Twitter", Color(0xFF1DA1F2), android.R.drawable.ic_dialog_info),
    REDDIT("Reddit", Color(0xFFFF4500), android.R.drawable.ic_menu_info_details),
    UNKNOWN("Unknown", Color.Gray, android.R.drawable.ic_menu_more)
}

@Composable
fun PlatformIcon(
    platform: MediaPlatform,
    modifier: Modifier = Modifier,
    size: Dp = 32.dp
) {
    Surface(
        shape = CircleShape,
        color = platform.brandColor.copy(alpha = 0.2f),
        modifier = modifier.size(size)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(platform.brandColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = platform.iconResId),
                contentDescription = platform.displayName,
                tint = platform.brandColor,
                modifier = Modifier.size(size * 0.6f)
            )
        }
    }
}

@Composable
fun PlatformBadge(
    platform: MediaPlatform,
    modifier: Modifier = Modifier,
    size: Dp = 24.dp,
    showLabel: Boolean = false
) {
    Surface(
        shape = CircleShape,
        color = platform.brandColor,
        modifier = modifier.size(size)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = platform.displayName.first().toString(),
                color = Color.White,
                fontSize = (size.value / 2).sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun PlatformBadgeWithLabel(
    platform: MediaPlatform,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        color = platform.brandColor.copy(alpha = 0.1f),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .background(platform.brandColor.copy(alpha = 0.05f))
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = platform.displayName,
                color = platform.brandColor,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

private fun Modifier.fillMaxSize() = this.then(
    Modifier
        .matchParentSize()
)

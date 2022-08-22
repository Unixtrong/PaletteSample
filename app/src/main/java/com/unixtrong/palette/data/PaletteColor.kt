package com.unixtrong.palette.data

import androidx.compose.ui.graphics.Color

data class PreviewColor(
    val target: ColorTarget?,
    val backgroundColor: Color,
    val titleTextColor: Color,
    val bodyTextColor: Color,
)

enum class ColorTarget(val cnName: String) {
    DOMINANT("主色"),
    VIBRANT("活力色"),
    LIGHT_VIBRANT("活力亮色"),
    DARK_VIBRANT("活力暗色"),
    MUTED("柔和色"),
    LIGHT_MUTED("柔和亮色"),
    DARK_MUTED("柔和暗色"),
}
package com.unixtrong.palette

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.compose.rememberImagePainter
import coil.imageLoader
import coil.request.ImageRequest
import com.unixtrong.palette.data.ColorTarget
import com.unixtrong.palette.data.PreviewColor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var palette by remember { mutableStateOf<Palette?>(null) }
    val imageLauncher = rememberLauncherForActivityResult(GetContent()) {
        imageUri = it
    }
    val context = LocalContext.current
    LaunchedEffect(imageUri) {
        imageUri ?: return@LaunchedEffect
        val imageLoader = context.imageLoader
        palette = withContext(Dispatchers.IO) {
            val request = ImageRequest.Builder(context).data(imageUri).build()
            imageLoader.execute(request).drawable
                ?.toBitmap()?.copy(Bitmap.Config.ARGB_8888, false)
                ?.let { Palette.from(it).generate() }
        }
    }

    Column(modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
                .clickable { imageLauncher.launch("image/*") }
                .padding(12.dp),
            contentAlignment = Alignment.Center,
        ) {
            if (imageUri != null) {
                Image(
                    painter = rememberImagePainter(imageUri),
                    modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                    contentDescription = "",
                )
            } else {
                Box(modifier = Modifier.wrapContentSize(), contentAlignment = Alignment.Center) {
                    val w = with(LocalDensity.current) { 20.dp.toPx() }
                    val r = with(LocalDensity.current) { 12.dp.toPx() }
                    val stroke = Stroke(
                        width = 10F,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(w, w / 2), 0F)
                    )
                    Canvas(
                        Modifier.size(200.dp)
                    ) {
                        drawRoundRect(
                            color = Color.Gray.copy(alpha = 0.3F),
                            style = stroke,
                            cornerRadius = CornerRadius(r, r)
                        )
                    }
                    Text(
                        text = "点击选择图片",
                        color = Color.Gray.copy(alpha = 0.3F),
                        style = MaterialTheme.typography.h6,
                    )
                }
            }
        }
        PaletteGrid(
            colors = palette?.toPreviewColors() ?: emptyList(),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
        )
    }
}

@Composable
fun PaletteGrid(
    colors: List<PreviewColor>,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        ColorPreview(colors.getOrNull(0), modifier = Modifier.weight(1F))
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
        ) {
            ColorPreview(colors.getOrNull(1), modifier = Modifier.weight(1F))
            Spacer(modifier = Modifier.width(12.dp))
            ColorPreview(colors.getOrNull(2), modifier = Modifier.weight(1F))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
        ) {
            ColorPreview(colors.getOrNull(3), modifier = Modifier.weight(1F))
            Spacer(modifier = Modifier.width(12.dp))
            ColorPreview(colors.getOrNull(4), modifier = Modifier.weight(1F))
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F)
        ) {
            ColorPreview(colors.getOrNull(5), modifier = Modifier.weight(1F))
            Spacer(modifier = Modifier.width(12.dp))
            ColorPreview(colors.getOrNull(6), modifier = Modifier.weight(1F))
        }
    }
}

@Composable
fun ColorPreview(
    color: PreviewColor?,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color?.backgroundColor ?: Color.Black, shape = RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = color?.target?.cnName ?: "颜色缺失",
            color = color?.titleTextColor ?: Color.White,
            style = MaterialTheme.typography.h5,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = color?.target?.cnName?.let { "文本${it}" } ?: "颜色缺失",
            color = color?.titleTextColor ?: Color.White,
            style = MaterialTheme.typography.body1,
        )
    }
}

private fun Palette.toPreviewColors(): List<PreviewColor> = listOf(
    dominantSwatch.toPreviewColor(ColorTarget.DOMINANT),
    vibrantSwatch.toPreviewColor(ColorTarget.VIBRANT),
    mutedSwatch.toPreviewColor(ColorTarget.MUTED),
    lightVibrantSwatch.toPreviewColor(ColorTarget.LIGHT_VIBRANT),
    lightMutedSwatch.toPreviewColor(ColorTarget.LIGHT_MUTED),
    darkVibrantSwatch.toPreviewColor(ColorTarget.DARK_VIBRANT),
    darkMutedSwatch.toPreviewColor(ColorTarget.DARK_MUTED),
)

private fun Palette.Swatch?.toPreviewColor(colorTarget: ColorTarget) = PreviewColor(
    target = colorTarget,
    backgroundColor = this?.rgb?.let { Color(it) } ?: Color.Black,
    titleTextColor = this?.titleTextColor?.let { Color(it) } ?: Color.White,
    bodyTextColor = this?.bodyTextColor?.let { Color(it) } ?: Color.White,
)

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(Modifier)
}
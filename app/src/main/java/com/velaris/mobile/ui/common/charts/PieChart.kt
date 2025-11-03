package com.velaris.mobile.ui.common.charts

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

fun generateColorForLabel(label: String, alpha: Float = 1f): Color {
    val hash = label.hashCode()
    val hue = ((hash % 360) + 360) % 360f
    val saturation = 0.6f + ((hash % 40 + 40) % 40) / 100f
    val lightness = 0.5f + ((hash % 20 + 20) % 20) / 100f
    return Color.hsl(hue, saturation.coerceIn(0f, 1f), lightness.coerceIn(0f, 1f), alpha)
}

@Composable
fun PieChart(
    values: List<Float>,
    labels: List<String>? = null,
    colors: List<Color>? = null,
    modifier: Modifier
) {
    if (values.isEmpty()) return

    val total = values.sum().takeIf { it != 0f } ?: 1f
    val segmentColors = colors ?: labels?.map { generateColorForLabel(it) } ?: List(values.size) {
        generateColorForLabel("Segment $it")
    }

    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    var progress by remember { mutableFloatStateOf(0f) }
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1500)
    )
    LaunchedEffect(Unit) { progress = 1f }

    val textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface, fontSize = 14.sp)
    val textMeasurer = rememberTextMeasurer()

    Column(modifier = modifier, verticalArrangement = Arrangement.Center) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .pointerInput(Unit) {
                    detectTapGestures { tap ->
                        val center = Offset(size.width / 2f, size.height / 2f)
                        val dx = tap.x - center.x
                        val dy = tap.y - center.y
                        val angle = ((Math.toDegrees(atan2(dy, dx).toDouble()) + 360) % 360).toFloat()
                        var startAngle = 0f
                        values.forEachIndexed { i, value ->
                            val sweep = value / total * 360f
                            if (angle in startAngle..(startAngle + sweep)) {
                                selectedIndex = if (selectedIndex == i) null else i
                                return@detectTapGestures
                            }
                            startAngle += sweep
                        }
                    }
                }
        ) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = size.minDimension / 2f * 0.8f
            var startAngle = -90f

            values.forEachIndexed { i, value ->
                val sweepAngle = value / total * 360f * animatedProgress
                drawArc(
                    color = segmentColors[i],
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2)
                )

                // procent w środku segmentu
                val middleAngle = startAngle + sweepAngle / 2f
                val rad = Math.toRadians(middleAngle.toDouble())
                val textRadius = radius * 0.5f
                val x = center.x + cos(rad) * textRadius
                val y = center.y + sin(rad) * textRadius
                val percentText = String.format(Locale.US, "%.0f%%", value / total * 100)
                drawText(
                    textMeasurer,
                    AnnotatedString(percentText),
                    topLeft = Offset(x.toFloat() - 10f, y.toFloat() - 10f),
                    style = textStyle
                )

                startAngle += sweepAngle
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // przewijana legenda
        val scrollState = rememberScrollState()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            values.forEachIndexed { i, _ ->
                val label = labels?.getOrNull(i) ?: "Segment $i"
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        Modifier
                            .size(16.dp)
                            .background(segmentColors[i])
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = label, style = textStyle)
                }
            }
        }
    }
}

@Preview
@Composable
fun PieChartPreview() {
    val mockValues = listOf(10f, 20f, 30f, 40f, 50f, 60f, 70f, 80f, 90f)
    val mockLabels = listOf(
        "Cards", "LEGO", "Figurines", "Other", "Comics",
        "Games", "Books", "Movies", "Music"
    )
    val mockColors = listOf(
        Color(0xFF1E88E5), Color(0xFFD32F2F), Color(0xFFFBC02D),
        Color(0xFF388E3C), Color(0xFF7B1FA2), Color(0xFFFF5722),
        Color(0xFF009688), Color(0xFFFFC107), Color(0xFF795548)
    )
    MaterialTheme {
        PieChart(values = mockValues, labels = mockLabels, colors = mockColors, modifier = Modifier.size(300.dp))
    }
}
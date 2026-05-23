package io.github.illusion.mobileapp.ui.screens.main

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import org.jetbrains.compose.resources.painterResource

// If you set a different `packageOfResClass` in your Gradle setup, replace the
// imports below with `<your-package>.Res` and `<your-package>.ic_*`.
//
// Recommended in composeApp/build.gradle.kts:
//   compose.resources {
//       publicResClass    = true
//       packageOfResClass = "io.github.illusion.mobileapp.resources"
//       generateResClass  = always
//   }
import io.github.illusion.mobileapp.resources.Res
import io.github.illusion.mobileapp.resources.ic_clipboard
import io.github.illusion.mobileapp.resources.ic_clock
import io.github.illusion.mobileapp.resources.ic_crown
import io.github.illusion.mobileapp.resources.ic_emblem
import io.github.illusion.mobileapp.resources.ic_flame
import io.github.illusion.mobileapp.resources.ic_inventory
import io.github.illusion.mobileapp.resources.ic_runner
import io.github.illusion.mobileapp.resources.ic_shield
import io.github.illusion.mobileapp.resources.ic_shoe
import io.github.illusion.mobileapp.resources.ic_sparkle
import io.github.illusion.mobileapp.resources.ic_streak_active
import io.github.illusion.mobileapp.resources.ic_streak_inactive
import io.github.illusion.mobileapp.resources.ic_sword

enum class MainIconType {
    Sword,
    Fire,
    Run,
    Target,
    Shield,
    Sparkle,
    Backpack,
    Clipboard,
    Crown,
    Warning,
    Close,
    Boot,
    Clock,
    Check,
    Emblem,
    StreakActive,
    StreakInactive,
}

@Composable
fun MainIcon(
    type: MainIconType,
    tint: Color,
    modifier: Modifier = Modifier,
) {
    when (type) {
        // ----- PNG-backed icons (use designer's colours, ignore tint) -----
        MainIconType.Sword -> ImageIcon(Res.drawable.ic_sword, modifier)
        MainIconType.Fire -> ImageIcon(Res.drawable.ic_flame, modifier)
        MainIconType.Run -> ImageIcon(Res.drawable.ic_runner, modifier)
        MainIconType.Shield -> ImageIcon(Res.drawable.ic_shield, modifier)
        MainIconType.Sparkle -> ImageIcon(Res.drawable.ic_sparkle, modifier)
        MainIconType.Backpack -> ImageIcon(Res.drawable.ic_inventory, modifier)
        MainIconType.Clipboard -> ImageIcon(Res.drawable.ic_clipboard, modifier)
        MainIconType.Crown -> ImageIcon(Res.drawable.ic_crown, modifier)
        MainIconType.Boot -> ImageIcon(Res.drawable.ic_shoe, modifier)
        MainIconType.Clock -> ImageIcon(Res.drawable.ic_clock, modifier)
        MainIconType.Emblem -> ImageIcon(Res.drawable.ic_emblem, modifier)
        MainIconType.Check, MainIconType.StreakActive ->
            ImageIcon(Res.drawable.ic_streak_active, modifier)
        MainIconType.StreakInactive ->
            ImageIcon(Res.drawable.ic_streak_inactive, modifier)

        // ----- Canvas fallbacks (tintable) -----
        MainIconType.Target -> CanvasIcon(modifier) { u -> drawTarget(tint, u) }
        MainIconType.Warning -> CanvasIcon(modifier) { u -> drawWarning(tint, u) }
        MainIconType.Close -> CanvasIcon(modifier) { u -> drawClose(tint, u) }
    }
}

@Composable
private fun ImageIcon(
    resource: org.jetbrains.compose.resources.DrawableResource,
    modifier: Modifier,
) {
    Image(
        painter = painterResource(resource),
        contentDescription = null,
        modifier = modifier,
    )
}

@Composable
private inline fun CanvasIcon(
    modifier: Modifier,
    crossinline block: DrawScope.(unit: Float) -> Unit,
) {
    Canvas(modifier = modifier) {
        val u = minOf(size.width, size.height) / 24f
        block(u)
    }
}

// ---------------------------------------------------------------------------
// Canvas drawings — only for icons not present in the PNG pack.
// All shapes are expressed in a virtual 24x24 viewport and scaled to the
// Canvas size.
// ---------------------------------------------------------------------------

private fun pathOf(block: Path.() -> Unit): Path = Path().apply(block)

private fun DrawScope.drawTarget(c: Color, u: Float) {
    val center = Offset(12f * u, 12f * u)
    val stroke = Stroke(width = 1.6f * u, cap = StrokeCap.Round)
    drawCircle(c, radius = 9f * u, center = center, style = stroke)
    drawCircle(c, radius = 6f * u, center = center, style = stroke)
    drawCircle(c, radius = 3f * u, center = center, style = stroke)
    drawCircle(c, radius = 1f * u, center = center)
    drawLine(c, Offset(12f * u, 1f * u), Offset(12f * u, 4f * u), strokeWidth = 1.6f * u, cap = StrokeCap.Round)
    drawLine(c, Offset(12f * u, 20f * u), Offset(12f * u, 23f * u), strokeWidth = 1.6f * u, cap = StrokeCap.Round)
    drawLine(c, Offset(1f * u, 12f * u), Offset(4f * u, 12f * u), strokeWidth = 1.6f * u, cap = StrokeCap.Round)
    drawLine(c, Offset(20f * u, 12f * u), Offset(23f * u, 12f * u), strokeWidth = 1.6f * u, cap = StrokeCap.Round)
}

private fun DrawScope.drawWarning(c: Color, u: Float) {
    val triangle = pathOf {
        moveTo(12f * u, 2f * u)
        lineTo(22.5f * u, 21f * u)
        lineTo(1.5f * u, 21f * u)
        close()
    }
    drawPath(triangle, c.copy(alpha = 0.2f))
    drawPath(triangle, c, style = Stroke(width = 1.8f * u, join = StrokeJoin.Round))
    drawLine(
        color = c,
        start = Offset(12f * u, 9f * u),
        end = Offset(12f * u, 15f * u),
        strokeWidth = 2f * u,
        cap = StrokeCap.Round,
    )
    drawCircle(c, radius = 1.1f * u, center = Offset(12f * u, 18f * u))
}

private fun DrawScope.drawClose(c: Color, u: Float) {
    val w = 2f * u
    drawLine(c, Offset(5f * u, 5f * u), Offset(19f * u, 19f * u), strokeWidth = w, cap = StrokeCap.Round)
    drawLine(c, Offset(19f * u, 5f * u), Offset(5f * u, 19f * u), strokeWidth = w, cap = StrokeCap.Round)
}

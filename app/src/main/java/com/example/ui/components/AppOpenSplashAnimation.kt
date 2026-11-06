package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun AppOpenSplashAnimation(
    onAnimationFinished: () -> Unit
) {
    var animationStage by remember { mutableStateOf(0) }
    val transitionDuration = 2200

    // Pulse animation for neon halo
    val infiniteTransition = rememberInfiniteTransition(label = "pulse_transition")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.14f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    // Stage animations
    val logoScale by animateFloatAsState(
        targetValue = if (animationStage >= 1) 1f else 0.65f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "logo_scale"
    )
    val logoAlpha by animateFloatAsState(
        targetValue = if (animationStage >= 1) 1f else 0f,
        animationSpec = tween(700, easing = LinearOutSlowInEasing),
        label = "logo_alpha"
    )

    val textAlpha by animateFloatAsState(
        targetValue = if (animationStage >= 2) 1f else 0f,
        animationSpec = tween(600),
        label = "text_alpha"
    )

    val progressAlpha by animateFloatAsState(
        targetValue = if (animationStage >= 3) 1f else 0f,
        animationSpec = tween(500),
        label = "progress_alpha"
    )

    LaunchedEffect(Unit) {
        delay(150)
        animationStage = 1 // Logo pops in
        delay(600)
        animationStage = 2 // Text reveals
        delay(700)
        animationStage = 3 // Status updates
        delay(1100)
        onAnimationFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0D14))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onAnimationFinished()
            },
        contentAlignment = Alignment.Center
    ) {
        // Ambient background glow
        Box(
            modifier = Modifier
                .size(340.dp)
                .scale(pulseScale)
                .alpha(pulseAlpha * 0.4f)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF38BDF8).copy(alpha = 0.35f),
                            Color(0xFFFFD700).copy(alpha = 0.15f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            // Glowing Brain Icon Container
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .scale(logoScale)
                    .alpha(logoAlpha)
            ) {
                // Outer glowing halo ring
                Box(
                    modifier = Modifier
                        .size(190.dp)
                        .scale(pulseScale)
                        .alpha(pulseAlpha)
                        .border(
                            width = 2.dp,
                            brush = Brush.linearGradient(
                                listOf(
                                    Color(0xFFFFFFFF).copy(alpha = 0.8f),
                                    Color(0xFF00E5FF).copy(alpha = 0.6f),
                                    Color(0xFFFFC01E).copy(alpha = 0.4f)
                                )
                            ),
                            shape = RoundedCornerShape(44.dp)
                        )
                )

                // Logo Card
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(RoundedCornerShape(36.dp))
                        .background(Color(0xFF151515))
                        .border(
                            width = 1.5.dp,
                            brush = Brush.verticalGradient(
                                listOf(
                                    Color(0xFFFFFFFF).copy(alpha = 0.6f),
                                    Color(0xFF2D3B4F)
                                )
                            ),
                            shape = RoundedCornerShape(36.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.zeros_logo),
                        contentDescription = "Zeros App Logo",
                        modifier = Modifier
                            .size(140.dp)
                            .clip(RoundedCornerShape(30.dp))
                    )
                }
            }

            Spacer(Modifier.height(32.dp))

            // Animated Typography
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.alpha(textAlpha)
            ) {
                Text(
                    text = "Z E R O S",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 6.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(6.dp))

                Text(
                    text = "INTELLIGENT MULTI-BRANCH POS & CHAIN OS",
                    color = Color(0xFFFFC01E),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(36.dp))

            // Connecting Status Pills
            Box(
                modifier = Modifier
                    .alpha(progressAlpha)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF1E2838))
                    .border(1.dp, Color(0xFF2D3B4F), RoundedCornerShape(20.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Bolt,
                        contentDescription = null,
                        tint = Color(0xFF00E5FF),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "Syncing 10 Sweet Shop Nodes...",
                        color = Color(0xFFE2E8F0),
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        // Skip button in bottom-right corner
        Button(
            onClick = onAnimationFinished,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color(0xFF94A3B8)
            ),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .testTag("skip_splash_button")
        ) {
            Text("Tap to Enter", fontSize = 12.sp)
            Icon(Icons.Default.ChevronRight, contentDescription = null, modifier = Modifier.size(16.dp))
        }
    }
}

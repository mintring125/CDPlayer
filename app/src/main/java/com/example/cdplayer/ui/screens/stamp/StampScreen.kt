package com.example.cdplayer.ui.screens.stamp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Star
import kotlinx.coroutines.launch
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun StampScreen(
    viewModel: StampViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val hapticFeedback = LocalHapticFeedback.current

    // Track haptic milestones so we fire each only once per charge
    var lastHapticMilestone by remember { mutableIntStateOf(0) }

    // Haptic feedback at charge milestones
    LaunchedEffect(uiState.chargeProgress) {
        val milestone = when {
            uiState.chargeProgress >= 1f -> 4
            uiState.chargeProgress >= 0.75f -> 3
            uiState.chargeProgress >= 0.50f -> 2
            uiState.chargeProgress >= 0.25f -> 1
            else -> 0
        }
        if (milestone > lastHapticMilestone && milestone in 1..3) {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
        if (milestone == 4 && lastHapticMilestone < 4) {
            hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        }
        lastHapticMilestone = milestone
    }

    // Reset milestone tracker when not charging
    LaunchedEffect(uiState.isCharging) {
        if (!uiState.isCharging) lastHapticMilestone = 0
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // --- Header ---
            StampHeader(
                currentBoard = uiState.currentBoardNumber,
                totalStamps = uiState.totalStamps,
                canGoPrev = uiState.currentBoardNumber > 1,
                onPrevBoard = { viewModel.navigateToBoard(uiState.currentBoardNumber - 1) },
                onNextBoard = { viewModel.navigateToBoard(uiState.currentBoardNumber + 1) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- Stamp Board ---
            StampBoard(
                stamps = uiState.stamps,
                justCollected = uiState.justCollectedStamp,
                isCompleted = uiState.stamps.size >= 12 && uiState.justCollectedStamp == null,
                hideJustCollected = uiState.showBigReveal,
                onAnimationComplete = { viewModel.onStampAnimationComplete() }
            )

            Spacer(modifier = Modifier.weight(1f))

            // --- Stamp Button ---
            StampButton(
                isCharging = uiState.isCharging,
                chargeProgress = uiState.chargeProgress,
                canCollect = uiState.canCollectStamp,
                boardFull = uiState.stamps.size >= 12,
                alreadyCollectedToday = uiState.alreadyCollectedToday,
                onChargeStart = { viewModel.onChargeStart() },
                onChargeEnd = { viewModel.onChargeEnd() }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }

        // --- Big Stamp Reveal Overlay ---
        if (uiState.showBigReveal && uiState.justCollectedStamp != null) {
            BigStampReveal(
                stampImageName = uiState.justCollectedStamp!!.stampImageName,
                onRevealComplete = { viewModel.onBigRevealComplete() }
            )
        }

        // --- Completion Celebration Overlay ---
        if (uiState.showCompletionCelebration) {
            CompletionCelebration(
                boardNumber = uiState.currentBoardNumber,
                onDismiss = { viewModel.onCompletionCelebrationDismissed() }
            )
        }
    }
}

// ==================== Header ====================

@Composable
private fun StampHeader(
    currentBoard: Int,
    totalStamps: Int,
    canGoPrev: Boolean,
    onPrevBoard: () -> Unit,
    onNextBoard: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "스탬프 모으기",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Total stamps badge
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = Color(0xFFFFD700),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "모은 스탬프: ${totalStamps}개",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Board navigation
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = onPrevBoard,
                enabled = canGoPrev,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    Icons.Filled.KeyboardArrowLeft,
                    contentDescription = "이전 판",
                    tint = if (canGoPrev) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                )
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) {
                Text(
                    text = "스탬프판 #$currentBoard",
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            IconButton(
                onClick = onNextBoard,
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    Icons.Filled.KeyboardArrowRight,
                    contentDescription = "다음 판",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

// ==================== Stamp Board ====================

@Composable
private fun StampBoard(
    stamps: List<com.example.cdplayer.data.local.entity.StampEntity>,
    justCollected: com.example.cdplayer.data.local.entity.StampEntity?,
    isCompleted: Boolean,
    hideJustCollected: Boolean = false,
    onAnimationComplete: () -> Unit
) {
    val stampMap = remember(stamps) {
        stamps.associateBy { it.position }
    }

    // Get completion date from the last stamp collected
    val completionDate = remember(stamps, isCompleted) {
        if (isCompleted && stamps.isNotEmpty()) {
            val lastStamp = stamps.maxByOrNull { it.collectedAt }
            lastStamp?.let {
                val sdf = java.text.SimpleDateFormat("yyyy.MM.dd", java.util.Locale.getDefault())
                sdf.format(java.util.Date(it.collectedAt))
            }
        } else null
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            tonalElevation = 2.dp,
            shadowElevation = 4.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (row in 0 until 3) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        for (col in 0 until 4) {
                            val position = row * 4 + col
                            val stamp = stampMap[position]
                            val isJustCollected = justCollected != null && stamp?.position == justCollected.position
                            val shouldHide = isJustCollected && hideJustCollected

                            Box(
                                modifier = Modifier.weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                if (stamp != null && !shouldHide) {
                                    FilledStampSlot(
                                        stampImageName = stamp.stampImageName,
                                        isJustCollected = isJustCollected,
                                        position = position,
                                        onAnimationComplete = onAnimationComplete
                                    )
                                } else {
                                    EmptyStampSlot()
                                }
                            }
                        }
                    }
                }
            }
        }

        // Completion overlay - diagonal red stamp
        if (isCompleted && completionDate != null) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clip(RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                // Semi-transparent overlay
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Black.copy(alpha = 0.05f))
                )

                // Diagonal "스탬프 완성" stamp
                Column(
                    modifier = Modifier.rotate(-15f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Red border stamp style
                    Box(
                        modifier = Modifier
                            .border(
                                width = 3.dp,
                                color = Color(0xFFE53935),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 20.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "스탬프 완성",
                                color = Color(0xFFE53935),
                                fontSize = 22.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 4.sp
                            )
                            Text(
                                text = completionDate,
                                color = Color(0xFFE53935).copy(alpha = 0.8f),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyStampSlot() {
    // Pulsing animation for empty slots
    val infiniteTransition = rememberInfiniteTransition(label = "emptyPulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    val borderColor = MaterialTheme.colorScheme.outline.copy(alpha = pulseAlpha)
    val textColor = MaterialTheme.colorScheme.onSurface.copy(alpha = pulseAlpha * 0.8f)

    Box(
        modifier = Modifier
            .size(72.dp)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = borderColor,
                style = Stroke(
                    width = 2.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(
                        floatArrayOf(8.dp.toPx(), 6.dp.toPx()),
                        0f
                    )
                )
            )
        }
        Text(
            text = "?",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

@Composable
private fun FilledStampSlot(
    stampImageName: String,
    isJustCollected: Boolean,
    position: Int,
    onAnimationComplete: () -> Unit
) {
    // Random rotation per slot, stable across recompositions
    val rotation = remember(position) { Random(position * 42).nextFloat() * 10f - 5f }

    // Slam-down animation for newly collected stamps
    val scale = remember { Animatable(if (isJustCollected) 2.5f else 1f) }
    val alpha = remember { Animatable(if (isJustCollected) 0f else 1f) }

    LaunchedEffect(isJustCollected) {
        if (isJustCollected) {
            // Run scale and alpha in parallel
            launch {
                scale.animateTo(
                    targetValue = 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
            }
            launch {
                alpha.animateTo(
                    targetValue = 1f,
                    animationSpec = tween(200)
                )
            }
            // Wait for slam to finish, then signal completion
            kotlinx.coroutines.delay(800)
            onAnimationComplete()
        }
    }

    // Sparkle particles for just-collected stamp
    val showSparkles = isJustCollected && scale.value > 1.05f

    Box(
        modifier = Modifier
            .size(72.dp)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .scale(scale.value)
                .alpha(alpha.value)
                .rotate(rotation)
                .shadow(
                    elevation = 3.dp,
                    shape = CircleShape,
                    clip = false
                )
                .clip(CircleShape)
                .background(Color(0xFFF5F0E8))
        ) {
            AsyncImage(
                model = "file:///android_asset/stamps/$stampImageName",
                contentDescription = "스탬프",
                modifier = Modifier.fillMaxSize()
            )
        }

        if (showSparkles) {
            SparkleParticles()
        }
    }
}

@Composable
private fun SparkleParticles() {
    val infiniteTransition = rememberInfiniteTransition(label = "sparkle")
    val sparkleProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sparkleProgress"
    )

    val sparkleColor = Color(0xFFFFD700)

    Canvas(modifier = Modifier.size(80.dp)) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val maxRadius = size.width / 2

        for (i in 0 until 8) {
            val angle = (i * 45f + sparkleProgress * 360f) * (Math.PI / 180f)
            val radius = maxRadius * (0.6f + sparkleProgress * 0.4f)
            val x = centerX + (radius * cos(angle)).toFloat()
            val y = centerY + (radius * sin(angle)).toFloat()
            val starAlpha = (1f - sparkleProgress).coerceIn(0f, 1f)
            val starSize = 4.dp.toPx() * (1f - sparkleProgress * 0.5f)

            drawCircle(
                color = sparkleColor.copy(alpha = starAlpha),
                radius = starSize,
                center = Offset(x, y)
            )
        }
    }
}

// ==================== Stamp Button ====================

@Composable
private fun StampButton(
    isCharging: Boolean,
    chargeProgress: Float,
    canCollect: Boolean,
    boardFull: Boolean,
    alreadyCollectedToday: Boolean,
    onChargeStart: () -> Unit,
    onChargeEnd: () -> Unit
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary
    val surfaceColor = MaterialTheme.colorScheme.surfaceVariant

    // Breathing animation when idle
    val infiniteTransition = rememberInfiniteTransition(label = "breathing")
    val breathScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathScale"
    )

    // Scale up during charge
    val chargeScale by animateFloatAsState(
        targetValue = if (isCharging) 1.1f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessLow),
        label = "chargeScale"
    )

    // Flash on complete
    val flashAlpha by animateFloatAsState(
        targetValue = if (chargeProgress >= 1f) 0.8f else 0f,
        animationSpec = tween(150),
        label = "flashAlpha"
    )

    val effectiveScale = if (isCharging) chargeScale else breathScale
    val buttonSize = 100.dp

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(buttonSize + 24.dp) // Extra space for progress ring
                .scale(effectiveScale)
        ) {
            // Charge progress ring
            if (isCharging || chargeProgress > 0f) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val strokeWidth = 6.dp.toPx()
                    // Background ring
                    drawArc(
                        color = surfaceColor,
                        startAngle = -90f,
                        sweepAngle = 360f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                        topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                        size = androidx.compose.ui.geometry.Size(
                            size.width - strokeWidth,
                            size.height - strokeWidth
                        )
                    )
                    // Progress ring
                    drawArc(
                        color = primaryColor,
                        startAngle = -90f,
                        sweepAngle = chargeProgress * 360f,
                        useCenter = false,
                        style = Stroke(width = strokeWidth, cap = StrokeCap.Round),
                        topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                        size = androidx.compose.ui.geometry.Size(
                            size.width - strokeWidth,
                            size.height - strokeWidth
                        )
                    )
                }
            }

            // Gathering sparkles during charge
            if (isCharging) {
                ChargeSparkles(progress = chargeProgress)
            }

            // Main button
            Surface(
                shape = CircleShape,
                color = when {
                    boardFull -> surfaceColor
                    alreadyCollectedToday -> MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f)
                    else -> primaryColor
                },
                shadowElevation = if (isCharging) 8.dp else 4.dp,
                modifier = Modifier
                    .size(buttonSize)
                    .then(
                        if (canCollect) {
                            Modifier.pointerInput(Unit) {
                                awaitEachGesture {
                                    awaitFirstDown(requireUnconsumed = false)
                                    onChargeStart()
                                    waitForUpOrCancellation()
                                    onChargeEnd()
                                }
                            }
                        } else {
                            Modifier
                        }
                    )
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    when {
                        boardFull -> {
                            Text(
                                text = "완성!",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        alreadyCollectedToday -> {
                            Text(
                                text = "✓",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = onPrimaryColor
                            )
                        }
                        else -> {
                            Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "스탬프 모으기",
                                modifier = Modifier.size(40.dp),
                                tint = onPrimaryColor
                            )
                        }
                    }
                }
            }

            // Flash overlay on completion
            if (flashAlpha > 0f) {
                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = flashAlpha),
                    modifier = Modifier.size(buttonSize)
                ) {}
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = when {
                boardFull -> "스탬프판 완성!"
                alreadyCollectedToday -> "오늘의 스탬프 완료!"
                else -> "꾸욱 눌러보세요!"
            },
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ChargeSparkles(progress: Float) {
    val infiniteTransition = rememberInfiniteTransition(label = "chargeSparkle")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "chargeRotation"
    )

    val sparkleColor = MaterialTheme.colorScheme.primary

    Canvas(modifier = Modifier.size(130.dp)) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val maxRadius = size.width / 2
        val particleCount = (12 * progress).toInt().coerceAtLeast(1)

        for (i in 0 until particleCount) {
            val baseAngle = (i * 360f / 12f + rotation) * (Math.PI / 180f)
            // Particles converge inward as progress increases
            val radiusFactor = 1f - progress * 0.5f
            val radius = maxRadius * radiusFactor * (0.7f + (i % 3) * 0.1f)
            val x = centerX + (radius * cos(baseAngle)).toFloat()
            val y = centerY + (radius * sin(baseAngle)).toFloat()
            val dotSize = 3.dp.toPx() * (0.5f + progress * 0.5f)

            drawCircle(
                color = sparkleColor.copy(alpha = 0.4f + progress * 0.4f),
                radius = dotSize,
                center = Offset(x, y)
            )
        }
    }
}

// ==================== Big Stamp Reveal ====================

@Composable
private fun BigStampReveal(
    stampImageName: String,
    onRevealComplete: () -> Unit
) {
    // Phase 1: Pop in (0 -> 1.0 scale with bounce)
    val scale = remember { Animatable(0f) }
    // Phase 2: Shrink and fade out
    val alpha = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        // Phase 1: Pop in with overshoot
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMedium
            )
        )

        // Pause to let the user see the stamp
        kotlinx.coroutines.delay(1000)

        // Phase 2: Shrink and fade out simultaneously
        launch {
            scale.animateTo(
                targetValue = 0.15f,
                animationSpec = tween(500, easing = FastOutSlowInEasing)
            )
        }
        launch {
            // Fade starts slightly later
            kotlinx.coroutines.delay(200)
            alpha.animateTo(
                targetValue = 0f,
                animationSpec = tween(300, easing = FastOutSlowInEasing)
            )
        }

        kotlinx.coroutines.delay(500)
        onRevealComplete()
    }

    // Dim overlay
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f * alpha.value)),
        contentAlignment = Alignment.Center
    ) {
        // Big stamp image
        Box(
            modifier = Modifier
                .size(200.dp)
                .scale(scale.value)
                .alpha(alpha.value)
                .shadow(
                    elevation = 12.dp,
                    shape = CircleShape,
                    clip = false
                )
                .clip(CircleShape)
                .background(Color(0xFFF5F0E8))
        ) {
            AsyncImage(
                model = "file:///android_asset/stamps/$stampImageName",
                contentDescription = "스탬프",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Sparkle ring around the big stamp
        if (scale.value > 0.5f) {
            val infiniteTransition = rememberInfiniteTransition(label = "revealSparkle")
            val sparkleRotation by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1500, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "revealSparkleRotation"
            )

            Canvas(
                modifier = Modifier
                    .size(240.dp)
                    .alpha(alpha.value)
            ) {
                val centerX = size.width / 2
                val centerY = size.height / 2
                val radius = size.width / 2

                for (i in 0 until 12) {
                    val angle = (i * 30f + sparkleRotation) * (Math.PI / 180f)
                    val x = centerX + (radius * cos(angle)).toFloat()
                    val y = centerY + (radius * sin(angle)).toFloat()
                    val starColor = if (i % 2 == 0) Color(0xFFFFD700) else Color(0xFFFF6B6B)

                    drawCircle(
                        color = starColor.copy(alpha = 0.8f),
                        radius = 5.dp.toPx(),
                        center = Offset(x, y)
                    )
                }
            }
        }
    }
}

// ==================== Completion Celebration ====================

@Composable
private fun CompletionCelebration(
    boardNumber: Int,
    onDismiss: () -> Unit
) {
    // Confetti particles
    val confettiColors = listOf(
        Color(0xFFFF6B6B), Color(0xFFFFD93D), Color(0xFF6BCB77),
        Color(0xFF4D96FF), Color(0xFFFF6BE6), Color(0xFFFF9F43)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        // Confetti canvas in the background
        ConfettiAnimation(colors = confettiColors)

        // Celebration card
        val cardScale = remember { Animatable(0.5f) }
        LaunchedEffect(Unit) {
            cardScale.animateTo(
                1f,
                spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
        }

        Surface(
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp,
            shadowElevation = 16.dp,
            modifier = Modifier
                .scale(cardScale.value)
                .padding(32.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "\uD83C\uDF89",
                    fontSize = 48.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "축하해요!",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "스탬프판 #${boardNumber} 완성!",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "참 잘했어요!",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "다음 스탬프판 시작!",
                        modifier = Modifier.padding(vertical = 4.dp),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun ConfettiAnimation(colors: List<Color>) {
    data class ConfettiParticle(
        val x: Float,
        val speed: Float,
        val delay: Float,
        val size: Float,
        val color: Color,
        val wobbleSpeed: Float
    )

    val particles = remember {
        List(40) {
            ConfettiParticle(
                x = Random.nextFloat(),
                speed = 0.3f + Random.nextFloat() * 0.7f,
                delay = Random.nextFloat(),
                size = 4f + Random.nextFloat() * 8f,
                color = colors[Random.nextInt(colors.size)],
                wobbleSpeed = 1f + Random.nextFloat() * 3f
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "confetti")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "confettiTime"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        for (p in particles) {
            val t = ((time + p.delay) % 1f)
            val y = t * size.height * 1.2f - size.height * 0.1f
            val xOffset = sin(t * p.wobbleSpeed * Math.PI * 2).toFloat() * 30.dp.toPx()
            val px = p.x * size.width + xOffset
            val alpha = if (t > 0.8f) (1f - t) / 0.2f else 1f

            drawCircle(
                color = p.color.copy(alpha = alpha.coerceIn(0f, 1f)),
                radius = p.size,
                center = Offset(px, y)
            )
        }
    }
}

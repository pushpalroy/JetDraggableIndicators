package com.appmason.jetdraggableindicators.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appmason.jetdraggableindicators.ui.theme.JetDraggableIndicatorsTheme
import kotlinx.coroutines.launch
import kotlin.math.abs

/**
 * Displays a custom indicator for a pager view, allowing for interactive selection and visualization
 * of the current page. The indicators are designed to diminish in size for pages further away from
 * the current selection, providing a focused visual effect.
 *
 * The function integrates drag gestures to enable changing the current page by dragging across the indicators,
 * with visual feedback during drag operations. It also automatically adjusts the scroll position of the indicators
 * to ensure the current selection is always visible within a constrained viewport size.
 *
 * @param modifier The [Modifier] to be applied to the indicator container. Defaults to [Modifier].
 * @param state The current state of the pager, containing information about the current page and allowing
 * control over the pager's scroll position.
 * @param itemCount The total number of pages/items in the pager.
 * @param onPageSelect A callback function that is invoked when a new page is selected through drag gestures.
 * The function is passed the index of the selected page.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CustomIndicator(
    modifier: Modifier = Modifier,
    state: PagerState,
    itemCount: Int,
    onPageSelect: (Int) -> Unit,
) {
    val haptics = LocalHapticFeedback.current
    val threshold = with(LocalDensity.current) {
        ((80.dp / (itemCount.coerceAtLeast(1))) + 10.dp).toPx()
    }
    val accumulatedDragAmount = remember { mutableFloatStateOf(0f) }
    var enableDrag by remember { mutableStateOf(false) }
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(state.currentPage) {
        coroutineScope.launch {
            lazyListState.animateScrollToItem(index = state.currentPage)
        }
    }

    Box(
        modifier = modifier.background(
            color = if (enableDrag) MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
            else Color.Transparent,
            shape = RoundedCornerShape(50)
        ),
        contentAlignment = Alignment.Center
    ) {
        LazyRow(
            state = lazyListState,
            modifier = Modifier
                .padding(8.dp)
                .widthIn(max = 100.dp)
                .pointerInput(Unit) {
                    detectDragGesturesAfterLongPress(
                        onDragStart = {
                            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
                            accumulatedDragAmount.floatValue = 0f
                            enableDrag = true
                        },
                        onDrag = { change, dragAmount ->
                            if (enableDrag) {
                                change.consume()
                                accumulatedDragAmount.floatValue += dragAmount.x
                                if (abs(accumulatedDragAmount.floatValue) >= threshold) {
                                    val nextPage = if (accumulatedDragAmount.floatValue < 0) state.currentPage + 1 else state.currentPage - 1
                                    val correctedNextPage = nextPage.coerceIn(0, itemCount - 1)

                                    if (correctedNextPage != state.currentPage) {
                                        haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                        onPageSelect(correctedNextPage)
                                    }
                                    accumulatedDragAmount.floatValue = 0f
                                }
                            }
                        },
                        onDragEnd = {
                            enableDrag = false
                            accumulatedDragAmount.floatValue = 0f
                        }
                    )
                },
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(itemCount) { i ->
                val distance = abs(i - state.currentPage)
                val size = 10.dp - (1.dp.times(distance)).coerceAtMost(4.dp)
                Box(
                    modifier = Modifier
                        .size(size)
                        .clip(CircleShape)
                        .align(Alignment.Center)
                        .background(
                            color = if (i == state.currentPage) Color(0xFF03A9F4) else Color.Gray
                        )
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Preview(showBackground = true)
@Composable
fun CustomIndicatorPreview() {
    JetDraggableIndicatorsTheme {
        CustomIndicator(
            itemCount = 8,
            state = rememberPagerState(
                initialPage = 0,
                initialPageOffsetFraction = 0f
            ) { 0 },
            onPageSelect = {}
        )
    }
}

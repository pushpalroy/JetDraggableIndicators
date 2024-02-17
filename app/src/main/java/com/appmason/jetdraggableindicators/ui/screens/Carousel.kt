package com.appmason.jetdraggableindicators.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.appmason.jetdraggableindicators.ui.theme.JetDraggableIndicatorsTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Carousel() {
    val colorList = remember {
        listOf(
            Color(0xFF354499),
            Color(0xFF0195A8),
            Color(0xFF6D9E35),
            Color(0xFFBDAD23),
            Color(0xFFA86808),
            Color(0xFF5B8827),
            Color(0xFF0E6F65),
            Color(0xFF752583)
        )
    }
    val state = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) { colorList.size }

    val coroutineScope = rememberCoroutineScope()
    Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            modifier = Modifier.height(280.dp),
            state = state,
        ) { page ->
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(color = colorList[page])
                        .fillMaxSize()
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        CustomIndicator(
            modifier = Modifier,
            state = state,
            itemCount = colorList.size,
            onPageSelect = { page ->
                coroutineScope.launch {
                    state.scrollToPage(page)
                }
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CarouselPreview() {
    JetDraggableIndicatorsTheme {
        Carousel()
    }
}

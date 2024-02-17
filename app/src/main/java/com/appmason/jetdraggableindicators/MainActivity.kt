package com.appmason.jetdraggableindicators

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.appmason.jetdraggableindicators.ui.screens.Carousel
import com.appmason.jetdraggableindicators.ui.theme.JetDraggableIndicatorsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetDraggableIndicatorsTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    Carousel()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CarouselPreview() {
    JetDraggableIndicatorsTheme {
        Carousel()
    }
}
package com.sololo.compose

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch

class TestScaffoldActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            scaffoldDemo()
        }
    }
}

@Preview
@Composable
fun scaffoldDemo() {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    Scaffold(
        scaffoldState = scaffoldState,
        drawerContent = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "drawerContent")
            }
        },
        topBar = {
            TopAppBar(
                title = { Text(text = "TopAppBar") },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch {
                            if (scaffoldState.drawerState.isClosed) {
                                scaffoldState.drawerState.open()
                            } else {
                                scaffoldState.drawerState.close()
                            }
                        }
                    }) {
                        Icon(imageVector = Icons.Filled.Menu, contentDescription = null)
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = { Text(text = "floatingButton") },
                onClick = { scope.launch { scaffoldState.drawerState.open() } })
        },
        floatingActionButtonPosition = FabPosition.End,
        content = {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "content")
            }
        },
        bottomBar = {
            BottomAppBar(backgroundColor = Color(0xFFAEE99F)) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    // 水平排列方式
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    // 垂直排列方式
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Btn1",
                        modifier = Modifier
                            .background(color = Color.LightGray)
                            .weight(1f)
                            .fillMaxHeight()
                    )
                    ClickableText(
                        text = AnnotatedString("Btn2"),
                        modifier = Modifier
                            .background(color = Color.Yellow)
                            .weight(1f),
                        onClick = {
                            Toast.makeText(context, "click", Toast.LENGTH_SHORT).show()
                        })
                    Text(
                        text = "Btn3",
                        modifier = Modifier
                            .background(color = Color.Magenta)
                            .weight(1f)
                    )
                }
            }
        }
    )
}
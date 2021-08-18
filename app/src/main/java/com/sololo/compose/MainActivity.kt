package com.sololo.compose

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.sololo.compose.ui.theme.ComposeDemoTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
//                    Greeting("Android")
                    mainContent()
                }
//                MapViewContainer(
//                    context = LocalContext.current,
//                    root = LocalView.current as ViewGroup
//                )

                HomeScreen()
                Log.i("Compose", "onCreate: ${LocalView.current}")
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun Greeting(name: String) {
    var expanded by remember { mutableStateOf(false) }
    val radius by animateDpAsState(targetValue = if (expanded) 20.dp else 0.dp)
    val backgroundColor by animateColorAsState(targetValue = if (expanded) Color.Red else Color.LightGray)
    Card(modifier = Modifier.padding(10.dp), shape = RoundedCornerShape(topStart = radius)) {
        Column(Modifier.clickable { expanded = !expanded }) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null
            )
            AnimatedVisibility(visible = expanded) {
                Text(
                    text = "Hello $name!",
                    modifier = Modifier
                        .padding(10.dp)
                        .background(color = backgroundColor)
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "preview name")
@Composable
fun DefaultPreview() {
    ComposeDemoTheme {
        var text = stringResource(R.string.congratulate, "New Year", 2021)
        text = LocalContext.current.resources.getQuantityString(R.plurals.runtime_format, 1, 1)
        val size = dimensionResource(id = R.dimen.padding_small)
        val color = colorResource(id = R.color.colorGrey)
        val drawable = painterResource(id = R.drawable.ic_launcher_background)
        Greeting(name = stringResource(id = R.string.send_btn))
    }
}

@Preview(showBackground = true)
@Composable
fun mainContent(names: List<String> = List(100) { "hello compose $it" }) {
    val counterState = remember { mutableStateOf(0) }
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxHeight()) {
        listContent(names = names, modifier = Modifier.weight(1f))

        Divider(color = Color.Transparent, thickness = 21.dp)
        Counter(count = counterState.value, updateCount = {
            counterState.value = it
            launchTestScaffoldActivity(context)
        })
    }
}

@Composable
fun listContent(names: List<String>, modifier: Modifier) {
    LazyColumn(modifier = modifier) {
        items(items = names) {
            Greeting(name = it)
            Divider(color = Color.Blue)
        }
    }
}

@Composable
fun Counter(count: Int, updateCount: (Int) -> Unit) {
    Button(
        onClick = { updateCount(count + 1) },
        colors = ButtonDefaults.buttonColors(backgroundColor = if (count > 5) Color.Green else Color.Gray)
    ) {
        Text(text = "clicked $count times")
    }
}

@Composable
fun MapViewContainer(context: Context, root: ViewGroup) {
    Log.i("Compose", "onCreate: ${LocalView.current}")
    val mapView = LayoutInflater.from(context).inflate(R.layout.custom_layout, root, false)
    AndroidView({ mapView }) {

    }
}

@Composable
fun AdViewContainer() {
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            LinearLayout(it).apply {
                setOnClickListener {

                }
            }
        },
        update = {
            it.gravity = Gravity.CENTER_HORIZONTAL
        }
    )
}


@Composable
fun SystemBroadcastReceiver(systemAction: String, onSystemEvent: (intent: Intent?) -> Unit) {
    val context = LocalContext.current
    val currentOnSystemEvent by rememberUpdatedState(systemAction)

    DisposableEffect(context, systemAction) {
        val intentFilter = IntentFilter(systemAction)
        val broadcast = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                onSystemEvent(intent)
            }
        }
        context.registerReceiver(broadcast, intentFilter)
        onDispose {
            context.unregisterReceiver(broadcast)
        }
    }
}

@Composable
fun HomeScreen() {
    SystemBroadcastReceiver(systemAction = Intent.ACTION_BATTERY_CHANGED) {
        Log.d("Compose", "intent:  ${it?.extras}")
    }
}

fun launchTestScaffoldActivity(context: Context) {
    context.startActivity(Intent(context, TestScaffold::class.java))
}
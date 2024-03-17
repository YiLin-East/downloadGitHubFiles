package com.me.downloadfiles

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.me.downloadfiles.retrofit2.download.DownloadBackgroundResourceFiles
import com.me.downloadfiles.ui.theme.DownloadFilesTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SetFlow(textFlow = GlobalScopeFlow.textFlow)

            DownloadFilesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }

        lifecycleScope.launch {
            DownloadBackgroundResourceFiles().process()
        }
    }
}


@Composable
fun SetFlow(textFlow: Flow<String>) {
    GreetingWithFlow(textFlow)
}

@Composable
fun GreetingWithFlow(textFlow: Flow<String>) {
    // 使用 remember 维护消息的累加状态
    val accumulatedMessages = remember { mutableStateOf("") }

    // 使用 LaunchedEffect 来收集新消息并将其累加到之前的消息上
    LaunchedEffect(textFlow) {
        textFlow.collect { newMessage ->
            accumulatedMessages.value += if (accumulatedMessages.value.isEmpty()) {
                newMessage
            } else {
                "\n$newMessage"
            }
        }
    }

    // 将累加的消息传递给 Greeting 函数
    Greeting(messages = accumulatedMessages.value)
}

@Composable
fun Greeting(messages: String, modifier: Modifier = Modifier) {
    // 在这里添加您的 Greeting UI
    // 此处假设您的 UI 是一个简单的文本显示
    Text(
        text = messages,
        modifier = modifier,
        color = Color.Red
    )
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DownloadFilesTheme {
        Greeting("Android")
    }
}
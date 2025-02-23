# Compose Callable

This library is strongly inspired by the [react-call](https://github.com/desko27/react-call).

## 
<table>
<tr>
<td>Default</td>
<td>Compose Callable</td>
</tr>
<tr>
<td>

```kotlin
@Composable
fun SampleScreen() {
    var message: String? by remember { mutableStateOf(null) }

    message?.let {
        AlertDialog(
            onDismissRequest = { message = null },
            confirmButton = {
                TextButton(
                    onClick = {
                        message = null
                        // Next step
                    }
                ) {
                    Text("Confirm")
                }
            },
            text = { Text(it) }
        )
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Button(onClick = { message = "Sure?" }) {
            Text("Submit")
        }
    }
}
```

</td>
<td>

```tsx
@Composable
fun SampleScreen() {
    val state = remember { CallableState<String, Boolean>() }
    val coroutineScope = rememberCoroutineScope()

    CallableHost(state) {
        AlertDialog(
            onDismissRequest = { resume(false) },
            confirmButton = {
                TextButton(onClick = { resume(true) }) {
                    Text("Confirm")
                }
            },
            text = { Text(it) }
        )
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Button(
            onClick = {
                coroutineScope.launch {
                    val confirmed = state.call("Sure?")
                    // Next step
                }
            }
        ) {
            Text("Submit")
        }
    }
}
```

</td>
</tr>
</table>


## Setup
```kotlin
dependencies {
    implementation("com.moriatsushi.compose.callable:compose-callable:1.0.0-alpha01")
}
```

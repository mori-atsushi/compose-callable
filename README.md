# Compose Callable

This library is heavily inspired by [react-call](https://github.com/desko27/react-call).

## Enhance Dialog Flow Readability

In standard dialog components, the logic for displaying the dialog and handling its result is often
separated, making the flow harder to follow.
By leveraging coroutines, this library makes dialog flow more readable and maintainable.

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
    var message: String? by remember {
        mutableStateOf(null)
    }

    message?.let {
        ConfirmDialog(
            onDismissRequest = {
                message = null
                // 2️⃣ Next step (Cancel)
            },
            onConfirm = {
                message = null
                // 2️⃣ Next step (Confirm)
            },
            text = it,
        )
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Button(
            onClick = {
                // 1️⃣ Show dialog
                message = "Sure?"
            }
        ) {
            Text("Submit")
        }
    }
}
```

</td>
<td>

```kotlin
@Composable
fun SampleScreen() {
    val dialogState = remember {
        CallableState<String, Boolean>()
    }
    val coroutineScope = rememberCoroutineScope()

    CallableHost(dialogState) {
        ConfirmDialog(
            onDismissRequest = { resume(false) },
            onConfirm = { resume(true) },
            text = it,
        )
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Button(
            onClick = {
                coroutineScope.launch {
                    // 1️⃣ Show dialog
                    val confirmed =
                        dialogState.call("Sure?")
                    // 2️⃣ Next step
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

To include this library in your project, add the following dependency:

```kotlin
dependencies {
    implementation("com.moriatsushi.compose.callable:compose-callable:1.0.0-alpha03")
}
```

## Conflict Strategy

When calling a component, there might already be an active call in progress.
The library offers a configurable strategy for handling such conflicts, defined by the
`ConflictStrategy` enum.
You can choose one of the following strategies:

* `ConflictStrategy.CANCEL_AND_OVERWRITE` (default):
If a new call is made while another call is still active, the current call is canceled immediately,
and the new call takes precedence.

* `ConflictStrategy.ENQUEUE`:
Instead of canceling the active call, the new call is enqueued. It will be processed only after the
current call completes.

You can specify the desired strategy when creating your `CallableState` instance:

```kotlin
val state = CallableState<String, Boolean>(ConflictStrategy.ENQUEUE)
```

## Using with ViewModel

If the caller's coroutine scope is canceled, the active component will automatically be dismissed.
This means that if the view is destroyed, the component disappears as well.
To persist the component state even when the view is destroyed, manage it within a ViewModel.

```kotlin
class SampleViewModel : ViewModel() {
    val dialogState = CallableState<String, Boolean>()

    fun showDialog() {
        viewModelScope.launch {
            // 1️⃣ Show dialog
            val confirmed = dialogState.call("Sure?")
            // 2️⃣ Next step
        }
    }
}
```

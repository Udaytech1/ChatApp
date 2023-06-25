package com.chatapp.compose.ui.comman_widgets

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

@Composable
fun DisposableEffectWithLifeCycle(
    onResume: () -> Unit,
    onPause: () -> Unit,
    onCreate: () -> Unit,
    onDestroy: () -> Unit,
    lifecycleOwner: LifecycleOwner,
) {
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> {
                    onCreate()
                }

                Lifecycle.Event.ON_RESUME -> {
                    onResume()
                }

                Lifecycle.Event.ON_PAUSE -> {
                    onPause()
                }

                Lifecycle.Event.ON_DESTROY -> {
                    onDestroy()
                }

                else -> {

                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            onDestroy()
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}
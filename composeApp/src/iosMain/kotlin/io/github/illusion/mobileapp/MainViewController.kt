package io.github.illusion.mobileapp

import androidx.compose.ui.window.ComposeUIViewController
import io.github.illusion.mobileapp.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}
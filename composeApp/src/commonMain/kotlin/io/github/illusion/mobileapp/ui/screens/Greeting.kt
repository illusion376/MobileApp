package io.github.illusion.mobileapp.ui.screens

import io.github.illusion.mobileapp.getPlatform

class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}
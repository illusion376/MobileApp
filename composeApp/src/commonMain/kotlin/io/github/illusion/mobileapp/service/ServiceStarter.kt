package io.github.illusion.mobileapp.service

import androidx.compose.runtime.Composable

@Composable
expect fun rememberServiceStarter(): (String) -> Unit


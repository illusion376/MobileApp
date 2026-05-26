package io.github.illusion.mobileapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.yandex.mapkit.MapKitFactory
import io.github.illusion.mobileapp.di.initKoin
import org.koin.android.ext.koin.androidContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        MapKitFactory.setApiKey("d1738cb9-479c-4140-9e32-db56b6b933e0")
        MapKitFactory.initialize(this)

        initKoin{
            androidContext(this@MainActivity.applicationContext)
        }
        setContent {
            App()
        }
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}

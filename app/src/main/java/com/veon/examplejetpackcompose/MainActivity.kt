package com.veon.examplejetpackcompose

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.veon.examplejetpackcompose.ui.theme.ExampleJetpackComposeTheme
import org.prebid.mobile.AdSize
import org.prebid.mobile.Host
import org.prebid.mobile.PrebidMobile
import org.prebid.mobile.api.data.InitializationStatus
import org.prebid.mobile.api.exceptions.AdException
import org.prebid.mobile.api.rendering.BannerView
import org.prebid.mobile.api.rendering.listeners.BannerViewListener
import org.prebid.mobile.eventhandlers.AuctionBannerEventHandler
import org.prebid.mobile.eventhandlers.AuctionListener

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        PrebidMobile.setPrebidServerAccountId("com.ideation.portall")
        PrebidMobile.setPrebidServerHost(Host.createCustomHost("https://prebid.jazzdsp.com/openrtb2/auction"))
        PrebidMobile.setCustomStatusEndpoint("https://prebid.jazzdsp.com/status")
        PrebidMobile.setTimeoutMillis(100000)
        PrebidMobile.setShareGeoLocation(true)
        PrebidMobile.useExternalBrowser = true
        PrebidMobile.initializeSdk(applicationContext) { status ->
            if (status == InitializationStatus.SUCCEEDED) {
                Toast.makeText(applicationContext, "initialized successfully!", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(applicationContext, "initialization error: $status\n${status.description}", Toast.LENGTH_LONG).show()
            }
        }

        setContent {
            ExampleJetpackComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(all = 16.dp)
    ) {
        Text("Composable example 320x50 banner")

        AndroidView(modifier = Modifier.fillMaxSize(),
            factory = { context ->
                // Creates view
                android.widget.FrameLayout(context).apply {

                    // listener for wrapping GAM rendering
                    val eventHandler = AuctionBannerEventHandler(
                        context,
                        "/6355419/Travel/Europe/France/Paris",
                        0.10F,
                        AdSize(320, 50)
                    )

                    // lister for understand where from demand
                    eventHandler.setAuctionEventListener(object : AuctionListener {
                        override fun onPRBWin(price: Float) {
                            Toast.makeText(context, "onPRBWin", Toast.LENGTH_LONG).show()
                        }
                        override fun onGAMWin(view: View?) {
                            Toast.makeText(context, "onGAMWin", Toast.LENGTH_LONG).show()
                        }
                    })

                    // configure banner placement
                    val adUnit = BannerView(context, "mybl_android_slot1_content_home_320x50", eventHandler)
                    // lister for custom tracking or custom display creative
                    adUnit.setBannerListener(object : BannerViewListener {
                        override fun onAdUrlClicked(url: String?) {
                            Toast.makeText(context, url, Toast.LENGTH_LONG).show()
                        }
                        override fun onAdLoaded(bannerView: BannerView?) {
                            Toast.makeText(context, "onAdLoaded", Toast.LENGTH_LONG).show()
                        }
                        override fun onAdDisplayed(bannerView: BannerView?) {
                            Toast.makeText(context, "onAdDisplayed", Toast.LENGTH_LONG).show()
                        }
                        override fun onAdFailed(bannerView: BannerView?, exception: AdException?) {
                            Toast.makeText(context, "onAdFailed", Toast.LENGTH_LONG).show()
                        }
                        override fun onAdClicked(bannerView: BannerView?) {
                            Toast.makeText(context, "onAdClicked", Toast.LENGTH_LONG).show()
                        }
                        override fun onAdClosed(bannerView: BannerView?) {
                            Toast.makeText(context, "onAdClosed", Toast.LENGTH_LONG).show()
                        }
                    })

                    this.addView(adUnit)
                    adUnit.loadAd()
                }
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ExampleJetpackComposeTheme {
        Greeting("Android")
    }
}
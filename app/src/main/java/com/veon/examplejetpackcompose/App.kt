package com.veon.examplejetpackcompose

import android.app.Application
import android.widget.Toast
import org.prebid.mobile.Host
import org.prebid.mobile.PrebidMobile
import org.prebid.mobile.api.data.InitializationStatus

class App : Application() {

    override fun onCreate() {
        super.onCreate()
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
                Toast.makeText(
                    applicationContext,
                    "initialization error: $status\n${status.description}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
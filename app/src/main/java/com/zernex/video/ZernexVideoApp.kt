package com.zernex.video

import android.app.Application
import android.util.Log

class ZernexVideoApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Log.i("ZernexVideo", "Zernex Video Player Application démarrée avec succès.")
    }
}

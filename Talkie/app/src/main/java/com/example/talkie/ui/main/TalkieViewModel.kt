package com.example.talkie.ui.main

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.Message
import android.os.Messenger
import android.os.RemoteException
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.talkie.FLAG_POST_MESSAGE
import java.lang.NumberFormatException

class TalkieViewModel : ViewModel() {

    private var messenger: Messenger? = null
    private val serviceConnection = WalkieServiceConnection()

    fun send(num: String) {
        val messenger = messenger ?: return
        val msg = Message.obtain(null, FLAG_POST_MESSAGE)

        try {
            msg.arg1 = Integer.parseInt(num)
            messenger.send(msg)
        } catch (e: NumberFormatException) {
            Log.e(javaClass.simpleName, "----------Couldn't parse integer from text: $num")
        } catch (e: RemoteException) {
            Log.e(javaClass.simpleName, "----------Remote exception encountered")
            e.printStackTrace()
        }
    }

    fun doBindService(context: Context) {
        val componentName = ComponentName("com.example.walkie", "com.example.walkie.WalkieService")

        val intent = Intent()
        intent.component = componentName

        try {
            context.startService(intent)
            val bindResult = if (context.bindService(intent, serviceConnection, 0))
                "Success!!!"
            else
                "Failure :("
            Log.i(javaClass.simpleName, "----------MainFragment  tried to bind: $bindResult")
        } catch (e: SecurityException) {
            Log.i(javaClass.simpleName, "----------SecurityException while attempting to bind to service!!! Do you have all required permissions?")
            e.printStackTrace()
        }
    }

    fun doUnbindService(context: Context) {
        if (serviceConnection.isBound)
            context.unbindService(serviceConnection)
    }


    inner class WalkieServiceConnection: ServiceConnection {

        var isBound = false

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            isBound = true
            messenger = Messenger(service)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
            messenger = null
        }
    }
}
package com.example.walkie.ui.main

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.walkie.FLAG_POST_MESSAGE
import com.example.walkie.FLAG_REGISTER_CLIENT
import com.example.walkie.FLAG_UNREGISTER_CLIENT

class WalkieViewModel : ViewModel() {

    private var serviceConnection = WalkieServiceConnection()
    private var outgoingMessenger: Messenger? = null
    private var incomingMessenger: Messenger? = Messenger(IncomingHandler())

    // You MUST set this to update the text field correctly
    var textUpdater: ((text: String) -> Unit)? = null

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
        } catch (e: RemoteException) {
            Log.e(javaClass.simpleName, "----------Remote exception encountered")
            e.printStackTrace()
        }
    }

    fun doUnbindService(context: Context) {
        if (serviceConnection.isBound) {
            val msg = Message.obtain(null, FLAG_UNREGISTER_CLIENT)
            msg.replyTo = incomingMessenger
            msg.arg1 = 31

            try {
                outgoingMessenger?.send(msg)
            } catch (e: RemoteException) {
                Log.e(javaClass.simpleName, "----------Remote exception encountered")
                e.printStackTrace()
            }

            try {
                context.unbindService(serviceConnection)
            } catch (e: SecurityException) {
                Log.i(javaClass.simpleName, "----------SecurityException while attempting to bind to service!!! Do you have all required permissions?")
                e.printStackTrace()
            }
        }
    }


    inner class IncomingHandler: Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                FLAG_POST_MESSAGE -> textUpdater?.invoke(msg.arg1.toString())
                else -> super.handleMessage(msg)
            }
        }
    }

    inner class WalkieServiceConnection: ServiceConnection {

        var isBound = false

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            isBound = true
            outgoingMessenger = Messenger(service)

            val msg = Message.obtain(null, FLAG_REGISTER_CLIENT)
            msg.replyTo = incomingMessenger
            msg.arg1 = 13

            try {
                outgoingMessenger?.send(msg)
            } catch (e: RemoteException) {
                Log.e(javaClass.simpleName, "----------Remote exception encountered")
                e.printStackTrace()
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
            outgoingMessenger = null
            incomingMessenger = null
        }
    }
}
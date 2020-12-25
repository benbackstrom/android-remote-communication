package com.example.walkie

import android.app.Service
import android.content.Intent
import android.os.IBinder

import android.os.*
import android.util.Log

const val FLAG_REGISTER_CLIENT = 1
const val FLAG_UNREGISTER_CLIENT = 2
const val FLAG_POST_MESSAGE = 3

class WalkieService: Service() {

    private val clients = mutableListOf<Messenger>()
    private var messenger: Messenger? = Messenger(IncomingHandler())

    override fun onCreate() {
        super.onCreate()
        Log.i(javaClass.simpleName, "----------TalkieService  onCreate")
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.i(javaClass.simpleName, "----------TalkieService  onBind")
        return messenger?.binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(javaClass.simpleName, "----------TalkieService  onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.i(javaClass.simpleName, "----------TalkieService  onDestroy")
        messenger = null
        super.onDestroy()
    }

    inner class IncomingHandler: Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                FLAG_REGISTER_CLIENT -> {
                    Log.i(javaClass.simpleName, "----------TalkieService  FLAG_REGISTER_CLIENT")
                    clients.add(msg.replyTo)
                }
                FLAG_UNREGISTER_CLIENT -> {
                    Log.i(javaClass.simpleName, "----------TalkieService  FLAG_UNREGISTER_CLIENT")
                    clients.remove(msg.replyTo)
                }
                FLAG_POST_MESSAGE -> {
                    Log.i(javaClass.simpleName, "----------TalkieService  FLAG_POST_MESSAGE ${msg.arg1}")
                    for (client in clients) {
                        try {
                            client.send(Message.obtain(null, FLAG_POST_MESSAGE, msg.arg1, 0))
                        } catch (e: RemoteException) {
                            clients.remove(client)
                        }
                    }
                }
                else -> super.handleMessage(msg)
            }
        }
    }
}

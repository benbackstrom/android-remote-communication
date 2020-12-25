package com.example.talkie

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log

const val FLAG_REGISTER_CLIENT = 1
const val FLAG_UNREGISTER_CLIENT = 2
const val FLAG_POST_MESSAGE = 3

class TalkieService: Service(), IncomingHandler.Callback {

    private val clients = mutableListOf<Messenger>()
    private val messenger = Messenger(IncomingHandler(this))

    override fun onCreate() {
        super.onCreate()
        Log.i(javaClass.simpleName, "----------TalkieService  onCreate")
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.i(javaClass.simpleName, "----------TalkieService  onBind")
        return messenger.binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(javaClass.simpleName, "----------TalkieService  onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.i(javaClass.simpleName, "----------TalkieService  onDestroy")
        super.onDestroy()
    }

    override fun addClient(client: Messenger) {
        clients.add(client)
    }

    override fun removeClient(client: Messenger) {
        clients.remove(client)
    }

    override fun getClients(): MutableList<Messenger> = clients
}

class IncomingHandler(private val callback: Callback): Handler() {
    override fun handleMessage(msg: Message) {
        when (msg.what) {
            FLAG_REGISTER_CLIENT -> callback.addClient(msg.replyTo)
            FLAG_UNREGISTER_CLIENT -> callback.removeClient(msg.replyTo)
            FLAG_POST_MESSAGE -> {
                val clients = callback.getClients()
                for (client in clients) {
                    try {
                        client.send(Message.obtain(null,
                            FLAG_POST_MESSAGE, msg.arg1, 0))
                    } catch (e: RemoteException) {
                        clients.remove(client)
                    }
                }
            }
            else -> super.handleMessage(msg)
        }
    }

    interface Callback {
        fun addClient(client: Messenger)
        fun removeClient(client: Messenger)
        fun getClients(): MutableList<Messenger>
    }
}

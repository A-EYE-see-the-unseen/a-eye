package com.dicoding.picodiploma.aeye.ui.detecting

import android.util.Log
import io.socket.client.IO.socket
import io.socket.client.Socket
import java.net.URISyntaxException

object SocketHandler {

    lateinit var mSocket: Socket

    @Synchronized
    fun setSocket() {
        try {
            mSocket = socket("http://10.0.2.2:3000")
            //mSocket = socket("https://backend-dot-adroit-sol-378614.et.r.appspot.com")
            //mSocket = socket("http://192.168.1.5:3000")
        } catch (e: URISyntaxException) {
            Log.e("SocketException", e.message.toString())
        }
    }

    @Synchronized
    fun getSocket(): Socket {
        return mSocket
    }

    @Synchronized
    fun establishConnection() {
        mSocket.connect()
    }

    @Synchronized
    fun closeConnection() {
        mSocket.disconnect()
    }

}
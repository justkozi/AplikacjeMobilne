package com.example.pomodorotimer

import android.app.Service
import android.content.Intent
import android.os.CountDownTimer
import android.os.IBinder
import android.util.Log

class CountDownService : Service() {

    var bi = Intent(COUNTDOWN_BR)

    private lateinit var timer: CountDownTimer

    override fun onCreate() {
        super.onCreate()
        return super.onCreate()
    }

    override fun onDestroy() {
        super.onDestroy()

        timer.cancel()

        bi.putExtra("forceStopped", true)
        sendBroadcast(bi)

        return super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        var secToCount: Long = intent?.getLongExtra("toCount", -1) ?: -1
        secToCount++  // +1s to avoid some phone's service finish before our own timer class reaches 0


        try {

            timer = object:CountDownTimer(secToCount*1000, 1000) {  // counting down 1s at a time
                override fun onTick(millisUntilFinished: Long) {

                    val msRemain:Long = millisUntilFinished

                    Log.i("timerapp", msRemain.toString())
                    bi.putExtra("toCount", msRemain)
                    sendBroadcast(bi)
                }

                override fun onFinish() {

                    Log.i("timerapp", "timer finish")
                    bi.putExtra("toCount", (-1).toLong())
                    sendBroadcast(bi)
                }
            }

            timer.start()

        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }


        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(arg0: Intent?): IBinder? {
        return null
    }

    companion object {
        const val COUNTDOWN_BR = "CountDownService.countdown_br"
    }
}
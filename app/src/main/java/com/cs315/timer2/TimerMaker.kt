package com.cs315.timer2


import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.util.*

class TimerMaker : Service() {
    override fun onBind(p0: Intent?): IBinder? = null;

    private val timer = Timer()

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val time = intent.getDoubleExtra(timeE, 0.0)
        timer.scheduleAtFixedRate(TimeTask(time), 0, 1000)
        return START_NOT_STICKY
    }

    override fun onDestroy()
    {
        timer.cancel()
        super.onDestroy()
    }

    private inner class TimeTask(private var time: Double) : TimerTask()
    {
        override fun run()
        {
            val intent = Intent(timeUpdate)
            time++
            intent.putExtra(timeE, time)
            sendBroadcast(intent)
        }
    }

    companion object
    {
        const val timeUpdate = "timerUpdated"
        const val timeE = "timeExtra"
    }
}
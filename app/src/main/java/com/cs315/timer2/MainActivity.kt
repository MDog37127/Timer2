package com.cs315.timer2

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cs315.timer2.TimerMaker
import com.cs315.timer2.databinding.ActivityMainBinding
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var timerStart = false
    private lateinit var serviceIntent: Intent
    private var time = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.start.setOnClickListener { startTimer() }
        binding.restart.setOnClickListener { restartTimer() }

        serviceIntent = Intent(applicationContext, TimerMaker::class.java)
        registerReceiver(updateTime, IntentFilter(TimerMaker.timeUpdate))
    }

    private fun restartTimer() {
        stopTimer()
        time = 0.0
        binding.timeView.text = getTimeStringDouble(time)
    }

    private fun startTimer() {
        if (timerStart)
            stopTimer()
        else
            startTime()
    }

    private fun stopTimer() {
        stopService(serviceIntent)
        binding.start.text = "Start"
        timerStart = false
    }

    private fun startTime() {
        serviceIntent.putExtra(TimerMaker.timeE, time)
        startService(serviceIntent)
        binding.start.text = "Stop"
        timerStart = true
    }

    private val updateTime: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            time = intent.getDoubleExtra(TimerMaker.timeE, 0.0)
            binding.timeView.text = getTimeStringDouble(time)
        }
    }

    private fun getTimeStringDouble(time: Double): String {
        val resultInt = time.roundToInt()
        val hour = resultInt % 86400 / 3600
        val minute = resultInt % 86400 % 3600 / 60
        val second = resultInt % 86400 % 3600 % 60

        return makeTString(hour, minute, second)
    }

    private fun makeTString(ho: Int, min: Int, sec: Int): String =
        String.format("%02d:%02d:%02d", ho, min, sec)
}
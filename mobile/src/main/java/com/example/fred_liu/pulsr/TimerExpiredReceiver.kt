package com.example.fred_liu.pulsr

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.fred_liu.pulsr.Timer.TimerActivity
import com.example.fred_liu.pulsr.Timer.NotificationUtil
import com.example.fred_liu.pulsr.Timer.PrefUtil

class TimerExpiredReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        NotificationUtil.showTimerExpired(context)

        PrefUtil.setTimerState(TimerActivity.TimerState.Stopped, context)
        PrefUtil.setAlarmSetTime(0, context)
    }
}

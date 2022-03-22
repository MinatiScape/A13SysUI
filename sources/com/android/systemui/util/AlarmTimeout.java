package com.android.systemui.util;

import android.app.AlarmManager;
import android.os.Handler;
import android.os.SystemClock;
/* loaded from: classes.dex */
public final class AlarmTimeout implements AlarmManager.OnAlarmListener {
    public final AlarmManager mAlarmManager;
    public final Handler mHandler;
    public final AlarmManager.OnAlarmListener mListener;
    public boolean mScheduled;
    public final String mTag;

    public final void cancel() {
        if (this.mScheduled) {
            this.mAlarmManager.cancel(this);
            this.mScheduled = false;
        }
    }

    @Override // android.app.AlarmManager.OnAlarmListener
    public final void onAlarm() {
        if (this.mScheduled) {
            this.mScheduled = false;
            this.mListener.onAlarm();
        }
    }

    public final boolean schedule(long j) {
        if (this.mScheduled) {
            return false;
        }
        this.mAlarmManager.setExact(2, SystemClock.elapsedRealtime() + j, this.mTag, this, this.mHandler);
        this.mScheduled = true;
        return true;
    }

    public AlarmTimeout(AlarmManager alarmManager, AlarmManager.OnAlarmListener onAlarmListener, String str, Handler handler) {
        this.mAlarmManager = alarmManager;
        this.mListener = onAlarmListener;
        this.mTag = str;
        this.mHandler = handler;
    }
}

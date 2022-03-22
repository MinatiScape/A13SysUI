package com.android.systemui.doze;

import android.app.AlarmManager;
import android.os.Handler;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.util.AlarmTimeout;
import java.util.Objects;
/* loaded from: classes.dex */
public final class DozePauser implements DozeMachine.Part {
    public DozeMachine mMachine;
    public final AlarmTimeout mPauseTimeout;
    public final AlwaysOnDisplayPolicy mPolicy;

    public DozePauser(Handler handler, AlarmManager alarmManager, AlwaysOnDisplayPolicy alwaysOnDisplayPolicy) {
        this.mPauseTimeout = new AlarmTimeout(alarmManager, new AlarmManager.OnAlarmListener() { // from class: com.android.systemui.doze.DozePauser$$ExternalSyntheticLambda0
            @Override // android.app.AlarmManager.OnAlarmListener
            public final void onAlarm() {
                DozePauser dozePauser = DozePauser.this;
                Objects.requireNonNull(dozePauser);
                dozePauser.mMachine.requestState(DozeMachine.State.DOZE_AOD_PAUSED);
            }
        }, "DozePauser", handler);
        this.mPolicy = alwaysOnDisplayPolicy;
    }

    @Override // com.android.systemui.doze.DozeMachine.Part
    public final void transitionTo(DozeMachine.State state, DozeMachine.State state2) {
        if (state2.ordinal() != 10) {
            this.mPauseTimeout.cancel();
        } else {
            this.mPauseTimeout.schedule(this.mPolicy.proxScreenOffDelayMs);
        }
    }

    @Override // com.android.systemui.doze.DozeMachine.Part
    public final void setDozeMachine(DozeMachine dozeMachine) {
        this.mMachine = dozeMachine;
    }
}

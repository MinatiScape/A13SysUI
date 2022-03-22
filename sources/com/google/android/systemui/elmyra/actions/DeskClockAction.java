package com.google.android.systemui.elmyra.actions;

import android.app.ActivityOptions;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.UserHandle;
import android.provider.Settings;
import android.util.Log;
import com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda9;
import com.google.android.systemui.elmyra.UserContentObserver;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
/* loaded from: classes.dex */
public abstract class DeskClockAction extends Action {
    public boolean mAlertFiring;
    public final AnonymousClass1 mAlertReceiver = new BroadcastReceiver() { // from class: com.google.android.systemui.elmyra.actions.DeskClockAction.1
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DeskClockAction.this.getAlertAction())) {
                DeskClockAction.this.mAlertFiring = true;
            } else if (intent.getAction().equals(DeskClockAction.this.getDoneAction())) {
                DeskClockAction.this.mAlertFiring = false;
            }
            DeskClockAction.this.notifyListener();
        }
    };
    public boolean mReceiverRegistered;

    /* JADX WARN: Type inference failed for: r5v1, types: [com.google.android.systemui.elmyra.actions.DeskClockAction$1] */
    public DeskClockAction(Context context) {
        super(context, null);
        updateBroadcastReceiver();
        new UserContentObserver(this.mContext, Settings.Secure.getUriFor("assist_gesture_silence_alerts_enabled"), new BubbleController$$ExternalSyntheticLambda9(this, 4), true);
    }

    public abstract Intent createDismissIntent();

    public abstract String getAlertAction();

    public abstract String getDoneAction();

    public final void updateBroadcastReceiver() {
        boolean z = false;
        this.mAlertFiring = false;
        if (this.mReceiverRegistered) {
            this.mContext.unregisterReceiver(this.mAlertReceiver);
            this.mReceiverRegistered = false;
        }
        if (Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "assist_gesture_silence_alerts_enabled", 1, -2) != 0) {
            z = true;
        }
        if (z) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(getAlertAction());
            intentFilter.addAction(getDoneAction());
            this.mContext.registerReceiverAsUser(this.mAlertReceiver, UserHandle.CURRENT, intentFilter, "com.android.systemui.permission.SEND_ALERT_BROADCASTS", null, 2);
            this.mReceiverRegistered = true;
        }
        notifyListener();
    }

    @Override // com.google.android.systemui.elmyra.actions.Action
    public final String toString() {
        return super.toString() + " [mReceiverRegistered -> " + this.mReceiverRegistered + "]";
    }

    @Override // com.google.android.systemui.elmyra.actions.Action
    public final void onTrigger(GestureSensor.DetectionProperties detectionProperties) {
        try {
            Intent createDismissIntent = createDismissIntent();
            ActivityOptions makeBasic = ActivityOptions.makeBasic();
            makeBasic.setDisallowEnterPictureInPictureWhileLaunching(true);
            createDismissIntent.setFlags(268435456);
            createDismissIntent.putExtra("android.intent.extra.REFERRER", Uri.parse("android-app://" + this.mContext.getPackageName()));
            this.mContext.startActivityAsUser(createDismissIntent, makeBasic.toBundle(), UserHandle.CURRENT);
        } catch (ActivityNotFoundException e) {
            Log.e("Elmyra/DeskClockAction", "Failed to dismiss alert", e);
        }
        this.mAlertFiring = false;
        notifyListener();
    }

    @Override // com.google.android.systemui.elmyra.actions.Action
    public final boolean isAvailable() {
        return this.mAlertFiring;
    }
}

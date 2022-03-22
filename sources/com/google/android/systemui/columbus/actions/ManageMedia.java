package com.google.android.systemui.columbus.actions;

import android.content.Context;
import android.media.AudioManager;
import android.os.SystemClock;
import android.view.KeyEvent;
import com.android.internal.logging.UiEventLogger;
import com.google.android.systemui.columbus.ColumbusEvent;
import com.google.android.systemui.columbus.sensors.GestureSensor;
/* compiled from: ManageMedia.kt */
/* loaded from: classes.dex */
public final class ManageMedia extends UserAction {
    public final AudioManager audioManager;
    public final String tag = "Columbus/ManageMedia";
    public final UiEventLogger uiEventLogger;

    @Override // com.google.android.systemui.columbus.actions.UserAction
    public final boolean availableOnLockscreen() {
        return true;
    }

    @Override // com.google.android.systemui.columbus.actions.UserAction
    public final boolean availableOnScreenOff() {
        return true;
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public final void onTrigger(GestureSensor.DetectionProperties detectionProperties) {
        boolean z;
        if (this.audioManager.isMusicActive() || this.audioManager.isMusicActiveRemotely()) {
            z = true;
        } else {
            z = false;
        }
        long uptimeMillis = SystemClock.uptimeMillis();
        this.audioManager.dispatchMediaKeyEvent(new KeyEvent(uptimeMillis, uptimeMillis, 0, 85, 0));
        this.audioManager.dispatchMediaKeyEvent(new KeyEvent(uptimeMillis, uptimeMillis, 1, 85, 0));
        if (z) {
            this.uiEventLogger.log(ColumbusEvent.COLUMBUS_INVOKED_PAUSE_MEDIA);
        } else {
            this.uiEventLogger.log(ColumbusEvent.COLUMBUS_INVOKED_PLAY_MEDIA);
        }
    }

    public ManageMedia(Context context, AudioManager audioManager, UiEventLogger uiEventLogger) {
        super(context);
        this.audioManager = audioManager;
        this.uiEventLogger = uiEventLogger;
        setAvailable(true);
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public final String getTag$vendor__unbundled_google__packages__SystemUIGoogle__android_common__sysuig() {
        return this.tag;
    }
}

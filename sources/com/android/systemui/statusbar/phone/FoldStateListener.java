package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.hardware.devicestate.DeviceStateManager;
import android.os.Trace;
import java.util.Objects;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: FoldStateListener.kt */
/* loaded from: classes.dex */
public final class FoldStateListener implements DeviceStateManager.DeviceStateCallback {
    public final int[] foldedDeviceStates;
    public final int[] goToSleepDeviceStates;
    public final OnFoldStateChangeListener listener;
    public Boolean wasFolded;

    /* compiled from: FoldStateListener.kt */
    /* loaded from: classes.dex */
    public interface OnFoldStateChangeListener {
    }

    public final void onStateChanged(int i) {
        boolean z;
        boolean contains = ArraysKt___ArraysKt.contains(this.foldedDeviceStates, i);
        if (!Intrinsics.areEqual(this.wasFolded, Boolean.valueOf(contains))) {
            this.wasFolded = Boolean.valueOf(contains);
            boolean contains2 = ArraysKt___ArraysKt.contains(this.goToSleepDeviceStates, i);
            StatusBar$$ExternalSyntheticLambda8 statusBar$$ExternalSyntheticLambda8 = (StatusBar$$ExternalSyntheticLambda8) this.listener;
            Objects.requireNonNull(statusBar$$ExternalSyntheticLambda8);
            StatusBar statusBar = (StatusBar) statusBar$$ExternalSyntheticLambda8.f$0;
            long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
            Objects.requireNonNull(statusBar);
            Trace.beginSection("StatusBar#onFoldedStateChanged");
            if (!statusBar.mShadeController.isShadeOpen() || contains2) {
                z = false;
            } else {
                z = true;
            }
            if (z) {
                statusBar.mStatusBarStateController.setLeaveOpenOnKeyguardHide(true);
                if (statusBar.mIsKeyguard) {
                    statusBar.mCloseQsBeforeScreenOff = true;
                }
            }
            Trace.endSection();
        }
    }

    public FoldStateListener(Context context, StatusBar$$ExternalSyntheticLambda8 statusBar$$ExternalSyntheticLambda8) {
        this.listener = statusBar$$ExternalSyntheticLambda8;
        this.foldedDeviceStates = context.getResources().getIntArray(17236065);
        this.goToSleepDeviceStates = context.getResources().getIntArray(17236024);
    }
}

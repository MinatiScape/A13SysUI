package com.google.android.systemui.columbus.sensors;

import android.os.SystemClock;
import android.util.Log;
import com.google.android.systemui.columbus.ColumbusEvent;
import com.google.android.systemui.columbus.gates.Gate;
import com.google.android.systemui.columbus.sensors.GestureController;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import java.util.Iterator;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: GestureController.kt */
/* loaded from: classes.dex */
public final class GestureController$gestureSensorListener$1 implements GestureSensor.Listener {
    public final /* synthetic */ GestureController this$0;

    public GestureController$gestureSensorListener$1(GestureController gestureController) {
        this.this$0 = gestureController;
    }

    @Override // com.google.android.systemui.columbus.sensors.GestureSensor.Listener
    public final void onGestureDetected(GestureSensor gestureSensor, int i, GestureSensor.DetectionProperties detectionProperties) {
        boolean z;
        Object obj;
        GestureController gestureController = this.this$0;
        Objects.requireNonNull(gestureController);
        long uptimeMillis = SystemClock.uptimeMillis();
        long j = gestureController.lastTimestampMap.get(i);
        gestureController.lastTimestampMap.put(i, uptimeMillis);
        if (uptimeMillis - j <= 500) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            Log.w("Columbus/GestureControl", "Gesture " + i + " throttled");
            return;
        }
        Iterator<T> it = this.this$0.softGates.iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            if (((Gate) obj).isBlocking()) {
                break;
            }
        }
        Gate gate = (Gate) obj;
        if (gate != null) {
            this.this$0.softGateBlockCount++;
            Log.i("Columbus/GestureControl", Intrinsics.stringPlus("Gesture blocked by ", gate));
            return;
        }
        if (i == 1) {
            this.this$0.uiEventLogger.log(ColumbusEvent.COLUMBUS_DOUBLE_TAP_DETECTED);
        }
        GestureController.GestureListener gestureListener = this.this$0.gestureListener;
        if (gestureListener != null) {
            gestureListener.onGestureDetected(i, detectionProperties);
        }
    }
}

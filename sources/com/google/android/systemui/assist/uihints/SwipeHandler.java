package com.google.android.systemui.assist.uihints;

import android.hardware.input.InputManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.InputDevice;
import android.view.MotionEvent;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline0;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import java.util.Objects;
/* loaded from: classes.dex */
public final class SwipeHandler implements NgaMessageHandler.SwipeListener {
    public final Handler mUiHandler = new Handler(Looper.getMainLooper());

    public static void injectMotionEvent(int i, int i2, long j, float f, float f2, float f3) {
        int i3;
        int[] deviceIds = InputDevice.getDeviceIds();
        int length = deviceIds.length;
        int i4 = 0;
        while (true) {
            if (i4 >= length) {
                i3 = 0;
                break;
            }
            int i5 = deviceIds[i4];
            if (InputDevice.getDevice(i5).supportsSource(i)) {
                i3 = i5;
                break;
            }
            i4++;
        }
        MotionEvent obtain = MotionEvent.obtain(j, j, i2, f, f2, f3, 1.0f, 0, 1.0f, 1.0f, i3, 0);
        obtain.setSource(i);
        InputManager.getInstance().injectInputEvent(obtain, 0);
    }

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.SwipeListener
    public final void onSwipe(Bundle bundle) {
        final float f = bundle.getFloat("start_x", 0.0f);
        final float f2 = bundle.getFloat("start_y", 0.0f);
        final float f3 = bundle.getFloat("end_x", 0.0f);
        final float f4 = bundle.getFloat("end_y", 0.0f);
        final int i = bundle.getInt("duration_ms", 1000);
        int i2 = bundle.getInt("num_motion_events", (i * 60) / 1000);
        if (i2 < 1 || i2 > 600) {
            Log.e("SwipeHandler", "Invalid number of motion events requested");
        } else if (i < 0 || i > 10000) {
            Log.e("SwipeHandler", "Invalid swipe duration requested");
        } else {
            final long uptimeMillis = SystemClock.uptimeMillis();
            injectMotionEvent(4098, 0, uptimeMillis, f, f2, 1.0f);
            final long j = uptimeMillis + i;
            final int i3 = i / i2;
            this.mUiHandler.postDelayed(new Runnable() { // from class: com.google.android.systemui.assist.uihints.SwipeHandler.1
                public final /* synthetic */ int val$inputSource = 4098;

                @Override // java.lang.Runnable
                public final void run() {
                    long uptimeMillis2 = SystemClock.uptimeMillis();
                    if (uptimeMillis2 < j) {
                        float f5 = ((float) (uptimeMillis2 - uptimeMillis)) / i;
                        SwipeHandler swipeHandler = SwipeHandler.this;
                        int i4 = this.val$inputSource;
                        float f6 = f;
                        float m = MotionController$$ExternalSyntheticOutline0.m(f3, f6, f5, f6);
                        float f7 = f2;
                        Objects.requireNonNull(swipeHandler);
                        SwipeHandler.injectMotionEvent(i4, 2, uptimeMillis2, m, f7 + ((f4 - f7) * f5), 1.0f);
                        SwipeHandler.this.mUiHandler.postDelayed(this, i3);
                        return;
                    }
                    SwipeHandler swipeHandler2 = SwipeHandler.this;
                    int i5 = this.val$inputSource;
                    float f8 = f3;
                    float f9 = f4;
                    Objects.requireNonNull(swipeHandler2);
                    SwipeHandler.injectMotionEvent(i5, 1, uptimeMillis2, f8, f9, 0.0f);
                }
            }, i3);
        }
    }
}

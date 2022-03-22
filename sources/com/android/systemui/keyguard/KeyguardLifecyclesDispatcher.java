package com.android.systemui.keyguard;

import android.app.IWallpaperManager;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.os.Trace;
import android.util.DisplayMetrics;
import android.util.Log;
import com.android.internal.policy.IKeyguardDrawnCallback;
import com.android.systemui.dagger.SysUIComponent$$ExternalSyntheticLambda0;
import com.android.systemui.dagger.SysUIComponent$$ExternalSyntheticLambda2;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.QSTileHost$$ExternalSyntheticLambda3;
import com.android.systemui.recents.OverviewProxyService$$ExternalSyntheticLambda6;
import java.util.Objects;
/* loaded from: classes.dex */
public final class KeyguardLifecyclesDispatcher {
    public AnonymousClass1 mHandler = new Handler() { // from class: com.android.systemui.keyguard.KeyguardLifecyclesDispatcher.1
        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            final Object obj = message.obj;
            switch (message.what) {
                case 0:
                    ScreenLifecycle screenLifecycle = KeyguardLifecyclesDispatcher.this.mScreenLifecycle;
                    Runnable runnable = new Runnable() { // from class: com.android.systemui.keyguard.KeyguardLifecyclesDispatcher.1.1
                        public boolean mInvoked;

                        @Override // java.lang.Runnable
                        public final void run() {
                            Object obj2 = obj;
                            if (obj2 != null) {
                                if (!this.mInvoked) {
                                    this.mInvoked = true;
                                    try {
                                        ((IKeyguardDrawnCallback) obj2).onDrawn();
                                    } catch (RemoteException e) {
                                        Log.w("KeyguardLifecyclesDispatcher", "Exception calling onDrawn():", e);
                                    }
                                } else {
                                    Log.w("KeyguardLifecyclesDispatcher", "KeyguardDrawnCallback#onDrawn() invoked > 1 times");
                                }
                            }
                        }
                    };
                    Objects.requireNonNull(screenLifecycle);
                    screenLifecycle.mScreenState = 1;
                    Trace.traceCounter(4096L, "screenState", 1);
                    for (int i = 0; i < screenLifecycle.mObservers.size(); i++) {
                        ((ScreenLifecycle.Observer) screenLifecycle.mObservers.get(i)).onScreenTurningOn(runnable);
                    }
                    return;
                case 1:
                    ScreenLifecycle screenLifecycle2 = KeyguardLifecyclesDispatcher.this.mScreenLifecycle;
                    Objects.requireNonNull(screenLifecycle2);
                    screenLifecycle2.mScreenState = 2;
                    Trace.traceCounter(4096L, "screenState", 2);
                    screenLifecycle2.dispatch(ScreenLifecycle$$ExternalSyntheticLambda0.INSTANCE);
                    return;
                case 2:
                    ScreenLifecycle screenLifecycle3 = KeyguardLifecyclesDispatcher.this.mScreenLifecycle;
                    Objects.requireNonNull(screenLifecycle3);
                    screenLifecycle3.mScreenState = 3;
                    Trace.traceCounter(4096L, "screenState", 3);
                    screenLifecycle3.dispatch(SysUIComponent$$ExternalSyntheticLambda2.INSTANCE$1);
                    return;
                case 3:
                    ScreenLifecycle screenLifecycle4 = KeyguardLifecyclesDispatcher.this.mScreenLifecycle;
                    Objects.requireNonNull(screenLifecycle4);
                    screenLifecycle4.mScreenState = 0;
                    Trace.traceCounter(4096L, "screenState", 0);
                    screenLifecycle4.dispatch(SysUIComponent$$ExternalSyntheticLambda0.INSTANCE$1);
                    return;
                case 4:
                    WakefulnessLifecycle wakefulnessLifecycle = KeyguardLifecyclesDispatcher.this.mWakefulnessLifecycle;
                    int i2 = message.arg1;
                    Objects.requireNonNull(wakefulnessLifecycle);
                    if (wakefulnessLifecycle.mWakefulness != 1) {
                        wakefulnessLifecycle.mWakefulness = 1;
                        Trace.traceCounter(4096L, "wakefulness", 1);
                        wakefulnessLifecycle.mLastWakeReason = i2;
                        wakefulnessLifecycle.mLastWakeOriginLocation = null;
                        if (i2 != 1) {
                            DisplayMetrics displayMetrics = wakefulnessLifecycle.mDisplayMetrics;
                            wakefulnessLifecycle.mLastWakeOriginLocation = new Point(displayMetrics.widthPixels / 2, displayMetrics.heightPixels);
                        } else {
                            wakefulnessLifecycle.mLastWakeOriginLocation = wakefulnessLifecycle.getPowerButtonOrigin();
                        }
                        IWallpaperManager iWallpaperManager = wakefulnessLifecycle.mWallpaperManagerService;
                        if (iWallpaperManager != null) {
                            try {
                                Point point = wakefulnessLifecycle.mLastWakeOriginLocation;
                                iWallpaperManager.notifyWakingUp(point.x, point.y, new Bundle());
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                        wakefulnessLifecycle.dispatch(QSTileHost$$ExternalSyntheticLambda3.INSTANCE$1);
                        return;
                    }
                    return;
                case 5:
                    WakefulnessLifecycle wakefulnessLifecycle2 = KeyguardLifecyclesDispatcher.this.mWakefulnessLifecycle;
                    Objects.requireNonNull(wakefulnessLifecycle2);
                    if (wakefulnessLifecycle2.mWakefulness != 2) {
                        wakefulnessLifecycle2.mWakefulness = 2;
                        Trace.traceCounter(4096L, "wakefulness", 2);
                        wakefulnessLifecycle2.dispatch(WakefulnessLifecycle$$ExternalSyntheticLambda1.INSTANCE);
                        wakefulnessLifecycle2.dispatch(OverviewProxyService$$ExternalSyntheticLambda6.INSTANCE$1);
                        return;
                    }
                    return;
                case FalsingManager.VERSION /* 6 */:
                    WakefulnessLifecycle wakefulnessLifecycle3 = KeyguardLifecyclesDispatcher.this.mWakefulnessLifecycle;
                    int i3 = message.arg1;
                    Objects.requireNonNull(wakefulnessLifecycle3);
                    if (wakefulnessLifecycle3.mWakefulness != 3) {
                        wakefulnessLifecycle3.mWakefulness = 3;
                        Trace.traceCounter(4096L, "wakefulness", 3);
                        wakefulnessLifecycle3.mLastSleepReason = i3;
                        wakefulnessLifecycle3.mLastSleepOriginLocation = null;
                        if (i3 != 4) {
                            DisplayMetrics displayMetrics2 = wakefulnessLifecycle3.mDisplayMetrics;
                            wakefulnessLifecycle3.mLastSleepOriginLocation = new Point(displayMetrics2.widthPixels / 2, displayMetrics2.heightPixels);
                        } else {
                            wakefulnessLifecycle3.mLastSleepOriginLocation = wakefulnessLifecycle3.getPowerButtonOrigin();
                        }
                        IWallpaperManager iWallpaperManager2 = wakefulnessLifecycle3.mWallpaperManagerService;
                        if (iWallpaperManager2 != null) {
                            try {
                                Point point2 = wakefulnessLifecycle3.mLastSleepOriginLocation;
                                iWallpaperManager2.notifyGoingToSleep(point2.x, point2.y, new Bundle());
                            } catch (RemoteException e2) {
                                e2.printStackTrace();
                            }
                        }
                        wakefulnessLifecycle3.dispatch(WakefulnessLifecycle$$ExternalSyntheticLambda2.INSTANCE);
                        return;
                    }
                    return;
                case 7:
                    WakefulnessLifecycle wakefulnessLifecycle4 = KeyguardLifecyclesDispatcher.this.mWakefulnessLifecycle;
                    Objects.requireNonNull(wakefulnessLifecycle4);
                    if (wakefulnessLifecycle4.mWakefulness != 0) {
                        wakefulnessLifecycle4.mWakefulness = 0;
                        Trace.traceCounter(4096L, "wakefulness", 0);
                        wakefulnessLifecycle4.dispatch(WakefulnessLifecycle$$ExternalSyntheticLambda0.INSTANCE);
                        return;
                    }
                    return;
                default:
                    throw new IllegalArgumentException("Unknown message: " + message);
            }
        }
    };
    public final ScreenLifecycle mScreenLifecycle;
    public final WakefulnessLifecycle mWakefulnessLifecycle;

    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.systemui.keyguard.KeyguardLifecyclesDispatcher$1] */
    public KeyguardLifecyclesDispatcher(ScreenLifecycle screenLifecycle, WakefulnessLifecycle wakefulnessLifecycle) {
        this.mScreenLifecycle = screenLifecycle;
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
    }
}

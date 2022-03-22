package com.android.systemui.unfold;

import android.content.Context;
import android.hardware.devicestate.DeviceStateManager;
import com.android.internal.util.LatencyTracker;
import com.android.systemui.keyguard.ScreenLifecycle;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: UnfoldLatencyTracker.kt */
/* loaded from: classes.dex */
public final class UnfoldLatencyTracker implements ScreenLifecycle.Observer {
    public final Context context;
    public final DeviceStateManager deviceStateManager;
    public final FoldStateListener foldStateListener;
    public Boolean folded;
    public final LatencyTracker latencyTracker;
    public final ScreenLifecycle screenLifecycle;
    public final Executor uiBgExecutor;

    /* compiled from: UnfoldLatencyTracker.kt */
    /* loaded from: classes.dex */
    public final class FoldStateListener extends DeviceStateManager.FoldStateListener {
        public FoldStateListener(final UnfoldLatencyTracker unfoldLatencyTracker, Context context) {
            super(context, new Consumer() { // from class: com.android.systemui.unfold.UnfoldLatencyTracker.FoldStateListener.1
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    UnfoldLatencyTracker unfoldLatencyTracker2 = UnfoldLatencyTracker.this;
                    boolean booleanValue = ((Boolean) obj).booleanValue();
                    Objects.requireNonNull(unfoldLatencyTracker2);
                    if (!Intrinsics.areEqual(unfoldLatencyTracker2.folded, Boolean.valueOf(booleanValue))) {
                        unfoldLatencyTracker2.folded = Boolean.valueOf(booleanValue);
                        if (!booleanValue) {
                            unfoldLatencyTracker2.latencyTracker.onActionStart(13);
                        }
                    }
                }
            });
        }
    }

    @Override // com.android.systemui.keyguard.ScreenLifecycle.Observer
    public final void onScreenTurnedOn() {
        if (Intrinsics.areEqual(this.folded, Boolean.FALSE)) {
            this.latencyTracker.onActionEnd(13);
        }
    }

    public UnfoldLatencyTracker(LatencyTracker latencyTracker, DeviceStateManager deviceStateManager, Executor executor, Context context, ScreenLifecycle screenLifecycle) {
        this.latencyTracker = latencyTracker;
        this.deviceStateManager = deviceStateManager;
        this.uiBgExecutor = executor;
        this.context = context;
        this.screenLifecycle = screenLifecycle;
        this.foldStateListener = new FoldStateListener(this, context);
    }
}

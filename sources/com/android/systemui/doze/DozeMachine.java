package com.android.systemui.doze;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.hardware.display.AmbientDisplayConfiguration;
import android.util.Log;
import com.android.internal.util.Preconditions;
import com.android.systemui.dock.DockManager;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.util.Assert;
import com.android.systemui.util.wakelock.WakeLock;
import java.io.PrintWriter;
import java.util.ArrayList;
/* loaded from: classes.dex */
public final class DozeMachine {
    public static final boolean DEBUG = DozeService.DEBUG;
    public final BatteryController mBatteryController;
    public final AmbientDisplayConfiguration mConfig;
    public DockManager mDockManager;
    public final DozeHost mDozeHost;
    public final DozeLog mDozeLog;
    public final Service mDozeService;
    public Part[] mParts;
    public int mPulseReason;
    public final WakeLock mWakeLock;
    public final WakefulnessLifecycle mWakefulnessLifecycle;
    public final ArrayList<State> mQueuedRequests = new ArrayList<>();
    public State mState = State.UNINITIALIZED;
    public boolean mWakeLockHeldForCurrentState = false;

    /* loaded from: classes.dex */
    public interface Part {
        default void destroy() {
        }

        default void dump(PrintWriter printWriter) {
        }

        default void onScreenState(int i) {
        }

        default void setDozeMachine(DozeMachine dozeMachine) {
        }

        void transitionTo(State state, State state2);
    }

    /* loaded from: classes.dex */
    public interface Service {

        /* loaded from: classes.dex */
        public static class Delegate implements Service {
            public final Service mDelegate;

            @Override // com.android.systemui.doze.DozeMachine.Service
            public final void finish() {
                this.mDelegate.finish();
            }

            @Override // com.android.systemui.doze.DozeMachine.Service
            public final void requestWakeUp() {
                this.mDelegate.requestWakeUp();
            }

            @Override // com.android.systemui.doze.DozeMachine.Service
            public void setDozeScreenBrightness(int i) {
                this.mDelegate.setDozeScreenBrightness(i);
            }

            @Override // com.android.systemui.doze.DozeMachine.Service
            public void setDozeScreenState(int i) {
                this.mDelegate.setDozeScreenState(i);
            }

            public Delegate(Service service) {
                this.mDelegate = service;
            }
        }

        void finish();

        void requestWakeUp();

        void setDozeScreenBrightness(int i);

        void setDozeScreenState(int i);
    }

    /* loaded from: classes.dex */
    public enum State {
        UNINITIALIZED,
        INITIALIZED,
        DOZE,
        DOZE_AOD,
        DOZE_REQUEST_PULSE,
        DOZE_PULSING,
        DOZE_PULSING_BRIGHT,
        DOZE_PULSE_DONE,
        FINISH,
        DOZE_AOD_PAUSED,
        DOZE_AOD_PAUSING,
        DOZE_AOD_DOCKED;

        public final boolean isAlwaysOn() {
            if (this == DOZE_AOD || this == DOZE_AOD_DOCKED) {
                return true;
            }
            return false;
        }
    }

    public final void requestState(State state) {
        Preconditions.checkArgument(state != State.DOZE_REQUEST_PULSE);
        requestState(state, -1);
    }

    public final boolean isExecutingTransition() {
        return !this.mQueuedRequests.isEmpty();
    }

    /* JADX WARN: Code restructure failed: missing block: B:126:0x0212, code lost:
        if (r16.mDockManager.isHidden() != false) goto L_0x0220;
     */
    /* JADX WARN: Removed duplicated region for block: B:125:0x020c  */
    /* JADX WARN: Removed duplicated region for block: B:127:0x0215  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00a3  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void transitionTo(com.android.systemui.doze.DozeMachine.State r17, int r18) {
        /*
            Method dump skipped, instructions count: 598
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.doze.DozeMachine.transitionTo(com.android.systemui.doze.DozeMachine$State, int):void");
    }

    public DozeMachine(Service service, AmbientDisplayConfiguration ambientDisplayConfiguration, WakeLock wakeLock, WakefulnessLifecycle wakefulnessLifecycle, BatteryController batteryController, DozeLog dozeLog, DockManager dockManager, DozeHost dozeHost, Part[] partArr) {
        this.mDozeService = service;
        this.mConfig = ambientDisplayConfiguration;
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
        this.mWakeLock = wakeLock;
        this.mBatteryController = batteryController;
        this.mDozeLog = dozeLog;
        this.mDockManager = dockManager;
        this.mDozeHost = dozeHost;
        this.mParts = partArr;
        for (Part part : partArr) {
            part.setDozeMachine(this);
        }
    }

    public final State getState() {
        Assert.isMainThread();
        if (!isExecutingTransition()) {
            return this.mState;
        }
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Cannot get state because there were pending transitions: ");
        m.append(this.mQueuedRequests.toString());
        throw new IllegalStateException(m.toString());
    }

    public final void requestState(State state, int i) {
        Assert.isMainThread();
        if (DEBUG) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("request: current=");
            m.append(this.mState);
            m.append(" req=");
            m.append(state);
            Log.i("DozeMachine", m.toString(), new Throwable("here"));
        }
        boolean z = !isExecutingTransition();
        this.mQueuedRequests.add(state);
        if (z) {
            this.mWakeLock.acquire("DozeMachine#requestState");
            for (int i2 = 0; i2 < this.mQueuedRequests.size(); i2++) {
                transitionTo(this.mQueuedRequests.get(i2), i);
            }
            this.mQueuedRequests.clear();
            this.mWakeLock.release("DozeMachine#requestState");
        }
    }
}

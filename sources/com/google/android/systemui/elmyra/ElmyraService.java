package com.google.android.systemui.elmyra;

import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.metrics.LogMaker;
import android.os.PowerManager;
import android.os.SystemClock;
import android.util.Log;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.Dumpable;
import com.android.systemui.globalactions.GlobalActionsDialogLite;
import com.android.systemui.settings.CurrentUserTracker$$ExternalSyntheticLambda0;
import com.android.wm.shell.ShellCommandHandlerImpl$$ExternalSyntheticLambda2;
import com.google.android.systemui.elmyra.actions.Action;
import com.google.android.systemui.elmyra.feedback.FeedbackEffect;
import com.google.android.systemui.elmyra.gates.Gate;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ElmyraService implements Dumpable {
    public final ArrayList mActions;
    public final ArrayList mFeedbackEffects;
    public final ArrayList mGates;
    public final GestureListener mGestureListener;
    public final GestureSensor mGestureSensor;
    public Action mLastActiveAction;
    public long mLastPrimedGesture;
    public int mLastStage;
    public final PowerManager mPowerManager;
    public final UiEventLogger mUiEventLogger;
    public final PowerManager.WakeLock mWakeLock;
    public final AnonymousClass1 mActionListener = new AnonymousClass1();
    public final AnonymousClass2 mGateListener = new Gate.Listener() { // from class: com.google.android.systemui.elmyra.ElmyraService.2
        @Override // com.google.android.systemui.elmyra.gates.Gate.Listener
        public final void onGateChanged(Gate gate) {
            ElmyraService.this.updateSensorListener();
        }
    };
    public final MetricsLogger mLogger = new MetricsLogger();

    /* renamed from: com.google.android.systemui.elmyra.ElmyraService$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements Action.Listener {
        public AnonymousClass1() {
        }
    }

    /* loaded from: classes.dex */
    public class GestureListener implements GestureSensor.Listener {
        public GestureListener() {
        }

        @Override // com.google.android.systemui.elmyra.sensors.GestureSensor.Listener
        public final void onGestureDetected(GestureSensor.DetectionProperties detectionProperties) {
            int i;
            long j;
            ElmyraEvent elmyraEvent;
            ElmyraService.this.mWakeLock.acquire(2000L);
            boolean isInteractive = ElmyraService.this.mPowerManager.isInteractive();
            if (detectionProperties != null && detectionProperties.mHostSuspended) {
                i = 3;
            } else if (!isInteractive) {
                i = 2;
            } else {
                i = 1;
            }
            LogMaker subtype = new LogMaker(999).setType(4).setSubtype(i);
            if (isInteractive) {
                j = SystemClock.uptimeMillis() - ElmyraService.this.mLastPrimedGesture;
            } else {
                j = 0;
            }
            LogMaker latency = subtype.setLatency(j);
            ElmyraService elmyraService = ElmyraService.this;
            elmyraService.mLastPrimedGesture = 0L;
            Action updateActiveAction = elmyraService.updateActiveAction();
            if (updateActiveAction != null) {
                Log.i("Elmyra/ElmyraService", "Triggering " + updateActiveAction);
                updateActiveAction.onTrigger(detectionProperties);
                for (int i2 = 0; i2 < ElmyraService.this.mFeedbackEffects.size(); i2++) {
                    ((FeedbackEffect) ElmyraService.this.mFeedbackEffects.get(i2)).onResolve(detectionProperties);
                }
                latency.setPackageName(updateActiveAction.getClass().getName());
            }
            UiEventLogger uiEventLogger = ElmyraService.this.mUiEventLogger;
            if (detectionProperties != null && detectionProperties.mHostSuspended) {
                elmyraEvent = ElmyraEvent.ELMYRA_TRIGGERED_AP_SUSPENDED;
            } else if (!isInteractive) {
                elmyraEvent = ElmyraEvent.ELMYRA_TRIGGERED_SCREEN_OFF;
            } else {
                elmyraEvent = ElmyraEvent.ELMYRA_TRIGGERED_SCREEN_ON;
            }
            uiEventLogger.log(elmyraEvent);
            ElmyraService.this.mLogger.write(latency);
        }

        @Override // com.google.android.systemui.elmyra.sensors.GestureSensor.Listener
        public final void onGestureProgress(float f, int i) {
            Action updateActiveAction = ElmyraService.this.updateActiveAction();
            if (updateActiveAction != null) {
                updateActiveAction.onProgress(f, i);
                for (int i2 = 0; i2 < ElmyraService.this.mFeedbackEffects.size(); i2++) {
                    ((FeedbackEffect) ElmyraService.this.mFeedbackEffects.get(i2)).onProgress(f, i);
                }
            }
            if (i != ElmyraService.this.mLastStage) {
                long uptimeMillis = SystemClock.uptimeMillis();
                if (i == 2) {
                    ElmyraService.this.mUiEventLogger.log(ElmyraEvent.ELMYRA_PRIMED);
                    ElmyraService.this.mLogger.action(998);
                    ElmyraService.this.mLastPrimedGesture = uptimeMillis;
                } else if (i == 0) {
                    ElmyraService elmyraService = ElmyraService.this;
                    if (elmyraService.mLastPrimedGesture != 0) {
                        elmyraService.mUiEventLogger.log(ElmyraEvent.ELMYRA_RELEASED);
                        ElmyraService.this.mLogger.write(new LogMaker(997).setType(4).setLatency(uptimeMillis - ElmyraService.this.mLastPrimedGesture));
                    }
                }
                ElmyraService.this.mLastStage = i;
            }
        }
    }

    public final Action updateActiveAction() {
        Action action;
        int i = 0;
        while (true) {
            if (i >= this.mActions.size()) {
                action = null;
                break;
            } else if (((Action) this.mActions.get(i)).isAvailable()) {
                action = (Action) this.mActions.get(i);
                break;
            } else {
                i++;
            }
        }
        Action action2 = this.mLastActiveAction;
        if (!(action2 == null || action == action2)) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Switching action from ");
            m.append(this.mLastActiveAction);
            m.append(" to ");
            m.append(action);
            Log.i("Elmyra/ElmyraService", m.toString());
            this.mLastActiveAction.onProgress(0.0f, 0);
        }
        this.mLastActiveAction = action;
        return action;
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        String str;
        String str2;
        printWriter.println("ElmyraService state:");
        printWriter.println("  Gates:");
        int i = 0;
        while (true) {
            str = "X ";
            if (i >= this.mGates.size()) {
                break;
            }
            printWriter.print("    ");
            Gate gate = (Gate) this.mGates.get(i);
            Objects.requireNonNull(gate);
            if (gate.mActive) {
                if (!((Gate) this.mGates.get(i)).isBlocking()) {
                    str = "O ";
                }
                printWriter.print(str);
            } else {
                printWriter.print("- ");
            }
            printWriter.println(((Gate) this.mGates.get(i)).toString());
            i++;
        }
        printWriter.println("  Actions:");
        for (int i2 = 0; i2 < this.mActions.size(); i2++) {
            printWriter.print("    ");
            if (((Action) this.mActions.get(i2)).isAvailable()) {
                str2 = "O ";
            } else {
                str2 = str;
            }
            printWriter.print(str2);
            printWriter.println(((Action) this.mActions.get(i2)).toString());
        }
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("  Active: ");
        m.append(this.mLastActiveAction);
        printWriter.println(m.toString());
        printWriter.println("  Feedback Effects:");
        for (int i3 = 0; i3 < this.mFeedbackEffects.size(); i3++) {
            printWriter.print("    ");
            printWriter.println(((FeedbackEffect) this.mFeedbackEffects.get(i3)).toString());
        }
        StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("  Gesture Sensor: ");
        m2.append(this.mGestureSensor.toString());
        printWriter.println(m2.toString());
        GestureSensor gestureSensor = this.mGestureSensor;
        if (gestureSensor instanceof Dumpable) {
            ((Dumpable) gestureSensor).dump(fileDescriptor, printWriter, strArr);
        }
    }

    public final void stopListening() {
        GestureSensor gestureSensor = this.mGestureSensor;
        if (gestureSensor != null && gestureSensor.isListening()) {
            this.mGestureSensor.stopListening();
            for (int i = 0; i < this.mFeedbackEffects.size(); i++) {
                ((FeedbackEffect) this.mFeedbackEffects.get(i)).onRelease();
            }
            Action updateActiveAction = updateActiveAction();
            if (updateActiveAction != null) {
                updateActiveAction.onProgress(0.0f, 0);
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.google.android.systemui.elmyra.ElmyraService$2] */
    public ElmyraService(Context context, ServiceConfiguration serviceConfiguration, UiEventLogger uiEventLogger) {
        GestureListener gestureListener = new GestureListener();
        this.mGestureListener = gestureListener;
        this.mUiEventLogger = uiEventLogger;
        PowerManager powerManager = (PowerManager) context.getSystemService(GlobalActionsDialogLite.GLOBAL_ACTION_KEY_POWER);
        this.mPowerManager = powerManager;
        this.mWakeLock = powerManager.newWakeLock(1, "Elmyra/ElmyraService");
        ArrayList arrayList = new ArrayList(serviceConfiguration.getActions());
        this.mActions = arrayList;
        arrayList.forEach(new CurrentUserTracker$$ExternalSyntheticLambda0(this, 3));
        this.mFeedbackEffects = new ArrayList(serviceConfiguration.getFeedbackEffects());
        ArrayList arrayList2 = new ArrayList(serviceConfiguration.getGates());
        this.mGates = arrayList2;
        arrayList2.forEach(new ShellCommandHandlerImpl$$ExternalSyntheticLambda2(this, 5));
        GestureSensor gestureSensor = serviceConfiguration.getGestureSensor();
        this.mGestureSensor = gestureSensor;
        if (gestureSensor != null) {
            gestureSensor.setGestureListener(gestureListener);
        }
        updateSensorListener();
    }

    public final void updateSensorListener() {
        Gate gate;
        Action updateActiveAction = updateActiveAction();
        int i = 0;
        if (updateActiveAction == null) {
            Log.i("Elmyra/ElmyraService", "No available actions");
            while (i < this.mGates.size()) {
                ((Gate) this.mGates.get(i)).deactivate();
                i++;
            }
            stopListening();
            return;
        }
        for (int i2 = 0; i2 < this.mGates.size(); i2++) {
            ((Gate) this.mGates.get(i2)).activate();
        }
        while (true) {
            if (i >= this.mGates.size()) {
                gate = null;
                break;
            } else if (((Gate) this.mGates.get(i)).isBlocking()) {
                gate = (Gate) this.mGates.get(i);
                break;
            } else {
                i++;
            }
        }
        if (gate != null) {
            Log.i("Elmyra/ElmyraService", "Gated by " + gate);
            stopListening();
            return;
        }
        Log.i("Elmyra/ElmyraService", "Unblocked; current action: " + updateActiveAction);
        GestureSensor gestureSensor = this.mGestureSensor;
        if (!(gestureSensor == null || gestureSensor.isListening())) {
            this.mGestureSensor.startListening();
        }
    }
}

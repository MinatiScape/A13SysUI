package com.google.android.systemui.columbus;

import android.os.PowerManager;
import android.util.Log;
import com.android.systemui.Dumpable;
import com.google.android.systemui.columbus.PowerManagerWrapper;
import com.google.android.systemui.columbus.actions.Action;
import com.google.android.systemui.columbus.feedback.FeedbackEffect;
import com.google.android.systemui.columbus.gates.Gate;
import com.google.android.systemui.columbus.sensors.GestureController;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ColumbusService.kt */
/* loaded from: classes.dex */
public final class ColumbusService implements Dumpable {
    public final ColumbusService$actionListener$1 actionListener;
    public final List<Action> actions;
    public final Set<FeedbackEffect> effects;
    public final ColumbusService$gateListener$1 gateListener;
    public final Set<Gate> gates;
    public final GestureController gestureController;
    public final ColumbusService$gestureListener$1 gestureListener;
    public Action lastActiveAction;
    public final PowerManagerWrapper.WakeLockWrapper wakeLock;

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        String str;
        String str2;
        printWriter.println(Intrinsics.stringPlus("ColumbusService", " state:"));
        printWriter.println("  Gates:");
        Iterator<T> it = this.gates.iterator();
        while (true) {
            str = "X ";
            if (!it.hasNext()) {
                break;
            }
            Gate gate = (Gate) it.next();
            printWriter.print("    ");
            Objects.requireNonNull(gate);
            if (gate.active) {
                if (!gate.isBlocking()) {
                    str = "O ";
                }
                printWriter.print(str);
            } else {
                printWriter.print("- ");
            }
            printWriter.println(gate.toString());
        }
        printWriter.println("  Actions:");
        for (Action action : this.actions) {
            printWriter.print("    ");
            Objects.requireNonNull(action);
            if (action.isAvailable) {
                str2 = "O ";
            } else {
                str2 = str;
            }
            printWriter.print(str2);
            printWriter.println(action.toString());
        }
        printWriter.println(Intrinsics.stringPlus("  Active: ", this.lastActiveAction));
        printWriter.println("  Feedback Effects:");
        for (FeedbackEffect feedbackEffect : this.effects) {
            printWriter.print("    ");
            printWriter.println(feedbackEffect.toString());
        }
        GestureController gestureController = this.gestureController;
        Objects.requireNonNull(gestureController);
        printWriter.println(Intrinsics.stringPlus("  Soft Blocks: ", Long.valueOf(gestureController.softGateBlockCount)));
        printWriter.println(Intrinsics.stringPlus("  Gesture Sensor: ", gestureController.gestureSensor));
        GestureSensor gestureSensor = gestureController.gestureSensor;
        if (gestureSensor instanceof Dumpable) {
            ((Dumpable) gestureSensor).dump(fileDescriptor, printWriter, strArr);
        }
    }

    public final void stopListening() {
        boolean z;
        GestureController gestureController = this.gestureController;
        Objects.requireNonNull(gestureController);
        if (gestureController.gestureSensor.isListening()) {
            gestureController.gestureSensor.stopListening();
            for (Gate gate : gestureController.softGates) {
                gate.unregisterListener(gestureController.softGateListener);
            }
            z = true;
        } else {
            z = false;
        }
        if (z) {
            for (FeedbackEffect feedbackEffect : this.effects) {
                feedbackEffect.onGestureDetected(0, null);
            }
            Action updateActiveAction = updateActiveAction();
            if (updateActiveAction != null) {
                updateActiveAction.onGestureDetected(0, null);
            }
        }
    }

    public final Action updateActiveAction() {
        Object obj;
        Iterator<T> it = this.actions.iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            Action action = (Action) obj;
            Objects.requireNonNull(action);
            if (action.isAvailable) {
                break;
            }
        }
        Action action2 = (Action) obj;
        Action action3 = this.lastActiveAction;
        if (!(action3 == null || action2 == action3)) {
            Log.i("Columbus/Service", "Switching action from " + action3 + " to " + action2);
            action3.onGestureDetected(0, null);
        }
        this.lastActiveAction = action2;
        return action2;
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [com.google.android.systemui.columbus.ColumbusService$actionListener$1] */
    /* JADX WARN: Type inference failed for: r2v3, types: [com.google.android.systemui.columbus.ColumbusService$gateListener$1] */
    /* JADX WARN: Type inference failed for: r2v4, types: [com.google.android.systemui.columbus.ColumbusService$gestureListener$1] */
    public ColumbusService(List<Action> list, Set<FeedbackEffect> set, Set<Gate> set2, GestureController gestureController, PowerManagerWrapper powerManagerWrapper) {
        PowerManager.WakeLock wakeLock;
        this.actions = list;
        this.effects = set;
        this.gates = set2;
        this.gestureController = gestureController;
        PowerManager powerManager = powerManagerWrapper.powerManager;
        if (powerManager == null) {
            wakeLock = null;
        } else {
            wakeLock = powerManager.newWakeLock(1, "Columbus/Service");
        }
        this.wakeLock = new PowerManagerWrapper.WakeLockWrapper(wakeLock);
        this.actionListener = new Action.Listener() { // from class: com.google.android.systemui.columbus.ColumbusService$actionListener$1
            @Override // com.google.android.systemui.columbus.actions.Action.Listener
            public final void onActionAvailabilityChanged(Action action) {
                ColumbusService.this.updateSensorListener();
            }
        };
        this.gateListener = new Gate.Listener() { // from class: com.google.android.systemui.columbus.ColumbusService$gateListener$1
            @Override // com.google.android.systemui.columbus.gates.Gate.Listener
            public final void onGateChanged(Gate gate) {
                ColumbusService.this.updateSensorListener();
            }
        };
        this.gestureListener = new GestureController.GestureListener() { // from class: com.google.android.systemui.columbus.ColumbusService$gestureListener$1
            @Override // com.google.android.systemui.columbus.sensors.GestureController.GestureListener
            public final void onGestureDetected(int i, GestureSensor.DetectionProperties detectionProperties) {
                if (i != 0) {
                    PowerManagerWrapper.WakeLockWrapper wakeLockWrapper = ColumbusService.this.wakeLock;
                    Objects.requireNonNull(wakeLockWrapper);
                    PowerManager.WakeLock wakeLock2 = wakeLockWrapper.wakeLock;
                    if (wakeLock2 != null) {
                        wakeLock2.acquire(2000L);
                    }
                }
                Action updateActiveAction = ColumbusService.this.updateActiveAction();
                if (updateActiveAction != null) {
                    ColumbusService columbusService = ColumbusService.this;
                    updateActiveAction.onGestureDetected(i, detectionProperties);
                    for (FeedbackEffect feedbackEffect : columbusService.effects) {
                        feedbackEffect.onGestureDetected(i, detectionProperties);
                    }
                }
            }
        };
        for (Action action : list) {
            ColumbusService$actionListener$1 columbusService$actionListener$1 = this.actionListener;
            Objects.requireNonNull(action);
            action.listeners.add(columbusService$actionListener$1);
        }
        GestureController gestureController2 = this.gestureController;
        ColumbusService$gestureListener$1 columbusService$gestureListener$1 = this.gestureListener;
        Objects.requireNonNull(gestureController2);
        gestureController2.gestureListener = columbusService$gestureListener$1;
        updateSensorListener();
    }

    public final void updateSensorListener() {
        Object obj;
        Action updateActiveAction = updateActiveAction();
        if (updateActiveAction == null) {
            Log.i("Columbus/Service", "No available actions");
            for (Gate gate : this.gates) {
                gate.unregisterListener(this.gateListener);
            }
            stopListening();
            return;
        }
        for (Gate gate2 : this.gates) {
            gate2.registerListener(this.gateListener);
        }
        Iterator<T> it = this.gates.iterator();
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
        Gate gate3 = (Gate) obj;
        if (gate3 != null) {
            Log.i("Columbus/Service", Intrinsics.stringPlus("Gated by ", gate3));
            stopListening();
            return;
        }
        Log.i("Columbus/Service", Intrinsics.stringPlus("Unblocked; current action: ", updateActiveAction));
        GestureController gestureController = this.gestureController;
        Objects.requireNonNull(gestureController);
        if (!gestureController.gestureSensor.isListening()) {
            for (Gate gate4 : gestureController.softGates) {
                gate4.registerListener(gestureController.softGateListener);
            }
            gestureController.gestureSensor.startListening();
        }
    }
}

package com.google.android.systemui.columbus.sensors;

import android.util.SparseLongArray;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.Dumpable;
import com.android.systemui.statusbar.commandline.Command;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.google.android.systemui.columbus.gates.Gate;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: GestureController.kt */
/* loaded from: classes.dex */
public final class GestureController implements Dumpable {
    public GestureListener gestureListener;
    public final GestureSensor gestureSensor;
    public final GestureController$gestureSensorListener$1 gestureSensorListener;
    public long softGateBlockCount;
    public final Set<Gate> softGates;
    public final UiEventLogger uiEventLogger;
    public final SparseLongArray lastTimestampMap = new SparseLongArray();
    public final GestureController$softGateListener$1 softGateListener = new Gate.Listener() { // from class: com.google.android.systemui.columbus.sensors.GestureController$softGateListener$1
        @Override // com.google.android.systemui.columbus.gates.Gate.Listener
        public final void onGateChanged(Gate gate) {
        }
    };

    /* compiled from: GestureController.kt */
    /* renamed from: com.google.android.systemui.columbus.sensors.GestureController$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static final class AnonymousClass1 extends Lambda implements Function0<Command> {
        public AnonymousClass1() {
            super(0);
        }

        @Override // kotlin.jvm.functions.Function0
        public final Command invoke() {
            return new ColumbusCommand();
        }
    }

    /* compiled from: GestureController.kt */
    /* loaded from: classes.dex */
    public final class ColumbusCommand implements Command {
        public ColumbusCommand() {
        }

        @Override // com.android.systemui.statusbar.commandline.Command
        public final void execute(PrintWriter printWriter, List<String> list) {
            if (list.isEmpty()) {
                printWriter.println("usage: quick-tap <command>");
                printWriter.println("Available commands:");
                printWriter.println("  trigger");
            } else if (Intrinsics.areEqual(list.get(0), "trigger")) {
                GestureController gestureController = GestureController.this;
                gestureController.gestureSensorListener.onGestureDetected(gestureController.gestureSensor, 1, null);
            } else {
                printWriter.println("usage: quick-tap <command>");
                printWriter.println("Available commands:");
                printWriter.println("  trigger");
            }
        }
    }

    /* compiled from: GestureController.kt */
    /* loaded from: classes.dex */
    public interface GestureListener {
        void onGestureDetected(int i, GestureSensor.DetectionProperties detectionProperties);
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println(Intrinsics.stringPlus("  Soft Blocks: ", Long.valueOf(this.softGateBlockCount)));
        printWriter.println(Intrinsics.stringPlus("  Gesture Sensor: ", this.gestureSensor));
        GestureSensor gestureSensor = this.gestureSensor;
        if (gestureSensor instanceof Dumpable) {
            ((Dumpable) gestureSensor).dump(fileDescriptor, printWriter, strArr);
        }
    }

    /* JADX WARN: Type inference failed for: r4v1, types: [com.google.android.systemui.columbus.sensors.GestureController$softGateListener$1] */
    public GestureController(GestureSensor gestureSensor, Set<Gate> set, CommandRegistry commandRegistry, UiEventLogger uiEventLogger) {
        this.gestureSensor = gestureSensor;
        this.softGates = set;
        this.uiEventLogger = uiEventLogger;
        GestureController$gestureSensorListener$1 gestureController$gestureSensorListener$1 = new GestureController$gestureSensorListener$1(this);
        this.gestureSensorListener = gestureController$gestureSensorListener$1;
        Objects.requireNonNull(gestureSensor);
        gestureSensor.listener = gestureController$gestureSensorListener$1;
        commandRegistry.registerCommand("quick-tap", new AnonymousClass1());
    }
}

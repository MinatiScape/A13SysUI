package com.google.android.systemui.columbus;

import com.android.systemui.Dumpable;
import com.google.android.systemui.columbus.actions.Action;
import com.google.android.systemui.columbus.actions.SettingsAction;
import com.google.android.systemui.columbus.feedback.FeedbackEffect;
import com.google.android.systemui.columbus.gates.Gate;
import com.google.android.systemui.columbus.sensors.GestureController;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import dagger.Lazy;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ColumbusServiceWrapper.kt */
/* loaded from: classes.dex */
public final class ColumbusServiceWrapper implements Dumpable {
    public final Lazy<ColumbusService> columbusService;
    public final ColumbusSettings columbusSettings;
    public final Lazy<ColumbusStructuredDataManager> columbusStructuredDataManager;
    public final Lazy<SettingsAction> settingsAction;
    public final ColumbusServiceWrapper$settingsChangeListener$1 settingsChangeListener;
    public boolean started;

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        String str;
        String str2;
        if (this.started) {
            ColumbusService columbusService = this.columbusService.get();
            Objects.requireNonNull(columbusService);
            printWriter.println(Intrinsics.stringPlus("ColumbusService", " state:"));
            printWriter.println("  Gates:");
            Iterator<T> it = columbusService.gates.iterator();
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
            for (Action action : columbusService.actions) {
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
            printWriter.println(Intrinsics.stringPlus("  Active: ", columbusService.lastActiveAction));
            printWriter.println("  Feedback Effects:");
            for (FeedbackEffect feedbackEffect : columbusService.effects) {
                printWriter.print("    ");
                printWriter.println(feedbackEffect.toString());
            }
            GestureController gestureController = columbusService.gestureController;
            Objects.requireNonNull(gestureController);
            printWriter.println(Intrinsics.stringPlus("  Soft Blocks: ", Long.valueOf(gestureController.softGateBlockCount)));
            printWriter.println(Intrinsics.stringPlus("  Gesture Sensor: ", gestureController.gestureSensor));
            GestureSensor gestureSensor = gestureController.gestureSensor;
            if (gestureSensor instanceof Dumpable) {
                ((Dumpable) gestureSensor).dump(fileDescriptor, printWriter, strArr);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [java.lang.Object, com.google.android.systemui.columbus.ColumbusServiceWrapper$settingsChangeListener$1, com.google.android.systemui.columbus.ColumbusSettings$ColumbusSettingsChangeListener] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public ColumbusServiceWrapper(com.google.android.systemui.columbus.ColumbusSettings r3, dagger.Lazy<com.google.android.systemui.columbus.ColumbusService> r4, dagger.Lazy<com.google.android.systemui.columbus.actions.SettingsAction> r5, dagger.Lazy<com.google.android.systemui.columbus.ColumbusStructuredDataManager> r6) {
        /*
            r2 = this;
            r2.<init>()
            r2.columbusSettings = r3
            r2.columbusService = r4
            r2.settingsAction = r5
            r2.columbusStructuredDataManager = r6
            com.google.android.systemui.columbus.ColumbusServiceWrapper$settingsChangeListener$1 r0 = new com.google.android.systemui.columbus.ColumbusServiceWrapper$settingsChangeListener$1
            r0.<init>()
            r2.settingsChangeListener = r0
            boolean r1 = r3.isColumbusEnabled()
            if (r1 == 0) goto L_0x0027
            java.util.Objects.requireNonNull(r3)
            java.util.LinkedHashSet r3 = r3.listeners
            r3.remove(r0)
            r3 = 1
            r2.started = r3
            r4.get()
            goto L_0x002d
        L_0x0027:
            r3.registerColumbusSettingsChangeListener(r0)
            r5.get()
        L_0x002d:
            r6.get()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.columbus.ColumbusServiceWrapper.<init>(com.google.android.systemui.columbus.ColumbusSettings, dagger.Lazy, dagger.Lazy, dagger.Lazy):void");
    }
}

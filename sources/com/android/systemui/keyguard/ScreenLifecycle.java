package com.android.systemui.keyguard;

import com.android.keyguard.LockIconView$$ExternalSyntheticOutline0;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: classes.dex */
public final class ScreenLifecycle extends Lifecycle<Observer> implements Dumpable {
    public int mScreenState = 0;

    /* loaded from: classes.dex */
    public interface Observer {
        default void onScreenTurnedOff() {
        }

        default void onScreenTurnedOn() {
        }

        default void onScreenTurningOff() {
        }

        default void onScreenTurningOn(Runnable runnable) {
        }
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        StringBuilder m = LockIconView$$ExternalSyntheticOutline0.m(printWriter, "ScreenLifecycle:", "  mScreenState=");
        m.append(this.mScreenState);
        printWriter.println(m.toString());
    }

    public ScreenLifecycle(DumpManager dumpManager) {
        dumpManager.registerDumpable("ScreenLifecycle", this);
    }
}

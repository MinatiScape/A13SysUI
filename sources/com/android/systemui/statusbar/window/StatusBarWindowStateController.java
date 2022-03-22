package com.android.systemui.statusbar.window;

import com.android.systemui.statusbar.CommandQueue;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
/* compiled from: StatusBarWindowStateController.kt */
/* loaded from: classes.dex */
public final class StatusBarWindowStateController {
    public final HashSet listeners = new HashSet();
    public final int thisDisplayId;
    public int windowState;

    public StatusBarWindowStateController(int i, CommandQueue commandQueue) {
        this.thisDisplayId = i;
        CommandQueue.Callbacks statusBarWindowStateController$commandQueueCallback$1 = new CommandQueue.Callbacks() { // from class: com.android.systemui.statusbar.window.StatusBarWindowStateController$commandQueueCallback$1
            @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
            public final void setWindowState(int i2, int i3, int i4) {
                StatusBarWindowStateController statusBarWindowStateController = StatusBarWindowStateController.this;
                Objects.requireNonNull(statusBarWindowStateController);
                if (i2 == statusBarWindowStateController.thisDisplayId && i3 == 1 && statusBarWindowStateController.windowState != i4) {
                    statusBarWindowStateController.windowState = i4;
                    Iterator it = statusBarWindowStateController.listeners.iterator();
                    while (it.hasNext()) {
                        ((StatusBarWindowStateListener) it.next()).onStatusBarWindowStateChanged(i4);
                    }
                }
            }
        };
        commandQueue.addCallback(statusBarWindowStateController$commandQueueCallback$1);
    }
}

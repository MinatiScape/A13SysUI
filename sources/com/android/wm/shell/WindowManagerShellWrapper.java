package com.android.wm.shell;

import android.os.RemoteException;
import android.view.WindowManagerGlobal;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.pip.PinnedStackListenerForwarder;
import java.util.Objects;
/* loaded from: classes.dex */
public class WindowManagerShellWrapper {
    public final PinnedStackListenerForwarder mPinnedStackListenerForwarder;

    public final void addPinnedStackListener(PinnedStackListenerForwarder.PinnedTaskListener pinnedTaskListener) throws RemoteException {
        PinnedStackListenerForwarder pinnedStackListenerForwarder = this.mPinnedStackListenerForwarder;
        Objects.requireNonNull(pinnedStackListenerForwarder);
        pinnedStackListenerForwarder.mListeners.add(pinnedTaskListener);
        PinnedStackListenerForwarder pinnedStackListenerForwarder2 = this.mPinnedStackListenerForwarder;
        Objects.requireNonNull(pinnedStackListenerForwarder2);
        WindowManagerGlobal.getWindowManagerService().registerPinnedTaskListener(0, pinnedStackListenerForwarder2.mListenerImpl);
    }

    public WindowManagerShellWrapper(ShellExecutor shellExecutor) {
        this.mPinnedStackListenerForwarder = new PinnedStackListenerForwarder(shellExecutor);
    }
}

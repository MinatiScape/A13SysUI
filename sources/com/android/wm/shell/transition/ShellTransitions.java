package com.android.wm.shell.transition;

import android.window.RemoteTransition;
import android.window.TransitionFilter;
/* loaded from: classes.dex */
public interface ShellTransitions {
    default IShellTransitions createExternalInterface() {
        return null;
    }

    default void registerRemote(TransitionFilter transitionFilter, RemoteTransition remoteTransition) {
    }
}

package com.android.systemui.dock;

import com.android.systemui.statusbar.KeyguardIndicationController$$ExternalSyntheticLambda0;
/* loaded from: classes.dex */
public interface DockManager {

    /* loaded from: classes.dex */
    public interface AlignmentStateListener {
        void onAlignmentStateChanged(int i);
    }

    /* loaded from: classes.dex */
    public interface DockEventListener {
        void onEvent(int i);
    }

    void addAlignmentStateListener(KeyguardIndicationController$$ExternalSyntheticLambda0 keyguardIndicationController$$ExternalSyntheticLambda0);

    void addListener(DockEventListener dockEventListener);

    boolean isDocked();

    boolean isHidden();

    void removeListener(DockEventListener dockEventListener);
}

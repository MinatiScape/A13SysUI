package com.android.systemui.statusbar.core;

import com.android.systemui.statusbar.window.StatusBarWindowController;
/* compiled from: StatusBarInitializer.kt */
/* loaded from: classes.dex */
public final class StatusBarInitializer {
    public OnStatusBarViewUpdatedListener statusBarViewUpdatedListener;
    public final StatusBarWindowController windowController;

    /* compiled from: StatusBarInitializer.kt */
    /* loaded from: classes.dex */
    public interface OnStatusBarViewUpdatedListener {
    }

    public StatusBarInitializer(StatusBarWindowController statusBarWindowController) {
        this.windowController = statusBarWindowController;
    }
}

package com.android.systemui.statusbar.phone.ongoingcall;

import com.android.systemui.statusbar.window.StatusBarWindowController;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: OngoingCallController.kt */
/* loaded from: classes.dex */
public final class OngoingCallController$removeChip$1<T> implements Consumer {
    public static final OngoingCallController$removeChip$1<T> INSTANCE = new OngoingCallController$removeChip$1<>();

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        StatusBarWindowController statusBarWindowController = (StatusBarWindowController) obj;
        Objects.requireNonNull(statusBarWindowController);
        StatusBarWindowController.State state = statusBarWindowController.mCurrentState;
        state.mOngoingProcessRequiresStatusBarVisible = false;
        statusBarWindowController.apply(state);
    }
}

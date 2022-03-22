package com.android.systemui.statusbar.phone.ongoingcall;

import com.android.systemui.statusbar.gesture.SwipeStatusBarAwayGestureHandler;
import java.util.function.Consumer;
/* compiled from: OngoingCallController.kt */
/* loaded from: classes.dex */
public final class OngoingCallController$removeChip$2<T> implements Consumer {
    public static final OngoingCallController$removeChip$2<T> INSTANCE = new OngoingCallController$removeChip$2<>();

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((SwipeStatusBarAwayGestureHandler) obj).removeOnGestureDetectedCallback();
    }
}

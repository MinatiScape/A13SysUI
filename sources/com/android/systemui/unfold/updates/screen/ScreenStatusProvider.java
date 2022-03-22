package com.android.systemui.unfold.updates.screen;

import com.android.systemui.statusbar.policy.CallbackController;
/* compiled from: ScreenStatusProvider.kt */
/* loaded from: classes.dex */
public interface ScreenStatusProvider extends CallbackController<ScreenListener> {

    /* compiled from: ScreenStatusProvider.kt */
    /* loaded from: classes.dex */
    public interface ScreenListener {
        void onScreenTurnedOn();
    }
}

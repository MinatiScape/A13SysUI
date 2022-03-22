package com.android.systemui.dreams.touch;

import com.android.systemui.dreams.touch.DreamOverlayTouchMonitor;
/* loaded from: classes.dex */
public interface DreamTouchHandler {

    /* loaded from: classes.dex */
    public interface TouchSession {

        /* loaded from: classes.dex */
        public interface Callback {
            void onRemoved();
        }
    }

    void onSessionStart(DreamOverlayTouchMonitor.TouchSessionImpl touchSessionImpl);
}

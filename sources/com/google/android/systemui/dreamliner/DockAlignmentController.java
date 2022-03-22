package com.google.android.systemui.dreamliner;

import android.util.Log;
import com.google.android.systemui.dreamliner.WirelessCharger;
/* loaded from: classes.dex */
public final class DockAlignmentController {
    public static final boolean DEBUG = Log.isLoggable("DockAlignmentController", 3);
    public int mAlignmentState = 0;
    public final DockObserver mDockObserver;
    public final WirelessCharger mWirelessCharger;

    /* loaded from: classes.dex */
    public final class RegisterAlignInfoListener implements WirelessCharger.AlignInfoListener {
        public RegisterAlignInfoListener() {
        }
    }

    public DockAlignmentController(WirelessCharger wirelessCharger, DockObserver dockObserver) {
        this.mWirelessCharger = wirelessCharger;
        this.mDockObserver = dockObserver;
    }
}

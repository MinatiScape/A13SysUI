package com.android.systemui.qs;

import android.hardware.display.ColorDisplayManager;
import android.provider.Settings;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.util.settings.SecureSettings;
import java.util.ArrayList;
/* loaded from: classes.dex */
public final class ReduceBrightColorsController implements CallbackController<Listener> {
    public final AnonymousClass1 mContentObserver;
    public AnonymousClass2 mCurrentUserTrackerCallback;
    public final ArrayList<Listener> mListeners = new ArrayList<>();
    public final ColorDisplayManager mManager;
    public final SecureSettings mSecureSettings;
    public final UserTracker mUserTracker;

    /* loaded from: classes.dex */
    public interface Listener {
        default void onActivated(boolean z) {
        }
    }

    public final void addCallback(Listener listener) {
        synchronized (this.mListeners) {
            if (!this.mListeners.contains(listener)) {
                this.mListeners.add(listener);
                if (this.mListeners.size() == 1) {
                    this.mSecureSettings.registerContentObserverForUser(Settings.Secure.getUriFor("reduce_bright_colors_activated"), false, this.mContentObserver, this.mUserTracker.getUserId());
                }
            }
        }
    }

    public final void removeCallback(Listener listener) {
        synchronized (this.mListeners) {
            if (this.mListeners.remove(listener) && this.mListeners.size() == 0) {
                this.mSecureSettings.unregisterContentObserver(this.mContentObserver);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r4v1, types: [com.android.systemui.qs.ReduceBrightColorsController$1] */
    /* JADX WARN: Type inference failed for: r4v2, types: [com.android.systemui.qs.ReduceBrightColorsController$2, com.android.systemui.settings.UserTracker$Callback] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public ReduceBrightColorsController(com.android.systemui.settings.UserTracker r2, android.os.Handler r3, android.hardware.display.ColorDisplayManager r4, com.android.systemui.util.settings.SecureSettings r5) {
        /*
            r1 = this;
            r1.<init>()
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r1.mListeners = r0
            r1.mManager = r4
            r1.mUserTracker = r2
            r1.mSecureSettings = r5
            com.android.systemui.qs.ReduceBrightColorsController$1 r4 = new com.android.systemui.qs.ReduceBrightColorsController$1
            r4.<init>(r3)
            r1.mContentObserver = r4
            com.android.systemui.qs.ReduceBrightColorsController$2 r4 = new com.android.systemui.qs.ReduceBrightColorsController$2
            r4.<init>()
            r1.mCurrentUserTrackerCallback = r4
            android.os.HandlerExecutor r1 = new android.os.HandlerExecutor
            r1.<init>(r3)
            r2.addCallback(r4, r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.ReduceBrightColorsController.<init>(com.android.systemui.settings.UserTracker, android.os.Handler, android.hardware.display.ColorDisplayManager, com.android.systemui.util.settings.SecureSettings):void");
    }
}

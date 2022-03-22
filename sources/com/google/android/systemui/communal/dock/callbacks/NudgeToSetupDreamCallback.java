package com.google.android.systemui.communal.dock.callbacks;

import android.app.Notification;
import android.app.NotificationManager;
import android.util.Log;
import androidx.core.view.ViewCompat$$ExternalSyntheticLambda0;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.dreams.DreamOverlayStateController$$ExternalSyntheticLambda0;
import com.android.systemui.util.condition.Monitor;
import com.google.android.systemui.communal.dreams.SetupDreamComplication;
import dagger.Lazy;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NudgeToSetupDreamCallback implements Monitor.Callback {
    public static final boolean DEBUG = Log.isLoggable("NudgeToSetupDream", 3);
    public final SetupDreamComplication mComplication;
    public boolean mDocked;
    public final DreamOverlayStateController mDreamOverlayStateController;
    public final Provider<Boolean> mDreamSelectedProvider;
    public boolean mDreamSet;
    public final int mNotificationId;
    public final Lazy<Notification> mNotificationLazy;
    public final NotificationManager mNotificationManager;
    public final AnonymousClass1 mSettingObserver;

    @Override // com.android.systemui.util.condition.Monitor.Callback
    public final void onConditionsChanged(boolean z) {
        if (DEBUG) {
            ViewCompat$$ExternalSyntheticLambda0.m("onConditionsChanged:", z, "NudgeToSetupDream");
        }
        this.mDocked = z;
        this.mDreamSet = this.mDreamSelectedProvider.mo144get().booleanValue();
        updatePresentation();
    }

    public final void updatePresentation() {
        if (this.mDreamSet || !this.mDocked) {
            this.mNotificationManager.cancel("NudgeToSetupDream", this.mNotificationId);
            DreamOverlayStateController dreamOverlayStateController = this.mDreamOverlayStateController;
            SetupDreamComplication setupDreamComplication = this.mComplication;
            Objects.requireNonNull(dreamOverlayStateController);
            dreamOverlayStateController.mExecutor.execute(new DreamOverlayStateController$$ExternalSyntheticLambda0(dreamOverlayStateController, setupDreamComplication, 0));
            return;
        }
        this.mNotificationManager.notify("NudgeToSetupDream", this.mNotificationId, this.mNotificationLazy.get());
        this.mDreamOverlayStateController.addComplication(this.mComplication);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [com.google.android.systemui.communal.dock.callbacks.NudgeToSetupDreamCallback$1, android.database.ContentObserver] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public NudgeToSetupDreamCallback(com.google.android.systemui.communal.dreams.SetupDreamComplication r2, com.android.systemui.dreams.DreamOverlayStateController r3, javax.inject.Provider<java.lang.Boolean> r4, android.app.NotificationManager r5, dagger.Lazy<android.app.Notification> r6, android.content.ContentResolver r7, android.net.Uri r8, int r9) {
        /*
            r1 = this;
            r1.<init>()
            com.google.android.systemui.communal.dock.callbacks.NudgeToSetupDreamCallback$1 r0 = new com.google.android.systemui.communal.dock.callbacks.NudgeToSetupDreamCallback$1
            r0.<init>()
            r1.mSettingObserver = r0
            r1.mNotificationManager = r5
            r1.mComplication = r2
            r1.mDreamOverlayStateController = r3
            r1.mDreamSelectedProvider = r4
            r1.mNotificationLazy = r6
            r1.mNotificationId = r9
            r1 = 1
            r7.registerContentObserver(r8, r1, r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.communal.dock.callbacks.NudgeToSetupDreamCallback.<init>(com.google.android.systemui.communal.dreams.SetupDreamComplication, com.android.systemui.dreams.DreamOverlayStateController, javax.inject.Provider, android.app.NotificationManager, dagger.Lazy, android.content.ContentResolver, android.net.Uri, int):void");
    }
}

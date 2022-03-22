package com.google.android.systemui;

import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.os.Handler;
import android.os.UserManager;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationClickNotifier;
import com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.collection.render.NotificationVisibilityProvider;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.google.android.systemui.smartspace.SmartSpaceController;
import dagger.Lazy;
/* loaded from: classes.dex */
public final class NotificationLockscreenUserManagerGoogle extends NotificationLockscreenUserManagerImpl {
    public final Lazy<KeyguardBypassController> mKeyguardBypassControllerLazy;
    public final KeyguardStateController mKeyguardStateController;
    public final KeyguardStateController.Callback mKeyguardVisibilityCallback;
    public final SmartSpaceController mSmartSpaceController;

    public NotificationLockscreenUserManagerGoogle(Context context, BroadcastDispatcher broadcastDispatcher, DevicePolicyManager devicePolicyManager, UserManager userManager, Lazy<NotificationVisibilityProvider> lazy, Lazy<CommonNotifCollection> lazy2, NotificationClickNotifier notificationClickNotifier, KeyguardManager keyguardManager, StatusBarStateController statusBarStateController, Handler handler, DeviceProvisionedController deviceProvisionedController, KeyguardStateController keyguardStateController, Lazy<KeyguardBypassController> lazy3, SmartSpaceController smartSpaceController, DumpManager dumpManager) {
        super(context, broadcastDispatcher, devicePolicyManager, userManager, lazy, lazy2, notificationClickNotifier, keyguardManager, statusBarStateController, handler, deviceProvisionedController, keyguardStateController, dumpManager);
        KeyguardStateController.Callback callback = new KeyguardStateController.Callback() { // from class: com.google.android.systemui.NotificationLockscreenUserManagerGoogle.1
            @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
            public final void onKeyguardShowingChanged() {
                NotificationLockscreenUserManagerGoogle.this.updateSmartSpaceVisibilitySettings();
            }
        };
        this.mKeyguardVisibilityCallback = callback;
        this.mKeyguardBypassControllerLazy = lazy3;
        this.mSmartSpaceController = smartSpaceController;
        this.mKeyguardStateController = keyguardStateController;
        keyguardStateController.addCallback(callback);
    }

    /* JADX WARN: Code restructure failed: missing block: B:62:0x00e4, code lost:
        if (r3.mCard.isWorkProfile != false) goto L_0x00e8;
     */
    /* JADX WARN: Removed duplicated region for block: B:61:0x00d6  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateSmartSpaceVisibilitySettings() {
        /*
            Method dump skipped, instructions count: 290
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.NotificationLockscreenUserManagerGoogle.updateSmartSpaceVisibilitySettings():void");
    }

    @Override // com.android.systemui.statusbar.NotificationLockscreenUserManagerImpl, com.android.systemui.statusbar.NotificationLockscreenUserManager
    public final void updatePublicMode() {
        super.updatePublicMode();
        updateSmartSpaceVisibilitySettings();
    }
}

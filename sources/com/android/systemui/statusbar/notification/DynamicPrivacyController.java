package com.android.systemui.statusbar.notification;

import android.util.ArraySet;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.util.Iterator;
/* loaded from: classes.dex */
public final class DynamicPrivacyController implements KeyguardStateController.Callback {
    public boolean mCacheInvalid;
    public final KeyguardStateController mKeyguardStateController;
    public final NotificationLockscreenUserManager mLockscreenUserManager;
    public final StatusBarStateController mStateController;
    public StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    public final ArraySet<Listener> mListeners = new ArraySet<>();
    public boolean mLastDynamicUnlocked = isDynamicallyUnlocked();

    /* loaded from: classes.dex */
    public interface Listener {
        void onDynamicPrivacyChanged();
    }

    public boolean isDynamicPrivacyEnabled() {
        NotificationLockscreenUserManager notificationLockscreenUserManager = this.mLockscreenUserManager;
        return !notificationLockscreenUserManager.userAllowsPrivateNotificationsInPublic(notificationLockscreenUserManager.getCurrentUserId());
    }

    public final boolean isDynamicallyUnlocked() {
        if ((this.mKeyguardStateController.canDismissLockScreen() || this.mKeyguardStateController.isKeyguardGoingAway() || this.mKeyguardStateController.isKeyguardFadingAway()) && isDynamicPrivacyEnabled()) {
            return true;
        }
        return false;
    }

    public final boolean isInLockedDownShade() {
        boolean z;
        int state;
        StatusBarKeyguardViewManager statusBarKeyguardViewManager = this.mStatusBarKeyguardViewManager;
        if (statusBarKeyguardViewManager == null || !statusBarKeyguardViewManager.mShowing) {
            z = false;
        } else {
            z = true;
        }
        if (!z || !this.mKeyguardStateController.isMethodSecure() || (((state = this.mStateController.getState()) != 0 && state != 2) || !isDynamicPrivacyEnabled() || isDynamicallyUnlocked())) {
            return false;
        }
        return true;
    }

    public DynamicPrivacyController(NotificationLockscreenUserManager notificationLockscreenUserManager, KeyguardStateController keyguardStateController, StatusBarStateController statusBarStateController) {
        this.mLockscreenUserManager = notificationLockscreenUserManager;
        this.mStateController = statusBarStateController;
        this.mKeyguardStateController = keyguardStateController;
        keyguardStateController.addCallback(this);
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
    public final void onUnlockedChanged() {
        if (isDynamicPrivacyEnabled()) {
            boolean isDynamicallyUnlocked = isDynamicallyUnlocked();
            if (isDynamicallyUnlocked != this.mLastDynamicUnlocked || this.mCacheInvalid) {
                this.mLastDynamicUnlocked = isDynamicallyUnlocked;
                Iterator<Listener> it = this.mListeners.iterator();
                while (it.hasNext()) {
                    it.next().onDynamicPrivacyChanged();
                }
            }
            this.mCacheInvalid = false;
            return;
        }
        this.mCacheInvalid = true;
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
    public final void onKeyguardFadingAwayChanged() {
        onUnlockedChanged();
    }
}

package com.android.systemui.navigationbar;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityManager;
import androidx.leanback.R$color;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline0;
import com.android.keyguard.LockIconView$$ExternalSyntheticOutline0;
import com.android.systemui.Dumpable;
import com.android.systemui.accessibility.AccessibilityButtonModeObserver;
import com.android.systemui.accessibility.AccessibilityButtonTargetsObserver;
import com.android.systemui.accessibility.SystemActions;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.KeyguardSliceProvider;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.phone.NotificationShadeWindowView;
import com.android.systemui.statusbar.phone.StatusBar;
import dagger.Lazy;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
/* loaded from: classes.dex */
public final class NavBarHelper implements AccessibilityButtonModeObserver.ModeChangedListener, AccessibilityButtonTargetsObserver.TargetsChangedListener, OverviewProxyService.OverviewProxyListener, NavigationModeController.ModeChangedListener, Dumpable {
    public int mA11yButtonState;
    public final AccessibilityButtonModeObserver mAccessibilityButtonModeObserver;
    public final AccessibilityManager mAccessibilityManager;
    public final Lazy<AssistManager> mAssistManagerLazy;
    public boolean mAssistantAvailable;
    public boolean mAssistantTouchGestureEnabled;
    public ContentResolver mContentResolver;
    public final Context mContext;
    public boolean mLongPressHomeEnabled;
    public int mNavBarMode;
    public final Lazy<Optional<StatusBar>> mStatusBarOptionalLazy;
    public final SystemActions mSystemActions;
    public final UserTracker mUserTracker;
    public final ArrayList mA11yEventListeners = new ArrayList();
    public final AnonymousClass1 mAssistContentObserver = new ContentObserver(new Handler(Looper.getMainLooper())) { // from class: com.android.systemui.navigationbar.NavBarHelper.1
        @Override // android.database.ContentObserver
        public final void onChange(boolean z, Uri uri) {
            NavBarHelper.this.updateAssistantAvailability();
        }
    };

    /* loaded from: classes.dex */
    public interface NavbarTaskbarStateUpdater {
        void updateAccessibilityServicesState();

        void updateAssistantAvailable(boolean z);
    }

    public final void dispatchA11yEventUpdate() {
        Iterator it = this.mA11yEventListeners.iterator();
        while (it.hasNext()) {
            ((NavbarTaskbarStateUpdater) it.next()).updateAccessibilityServicesState();
        }
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        StringBuilder m = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(LockIconView$$ExternalSyntheticOutline0.m(printWriter, "NavbarTaskbarFriendster", "  longPressHomeEnabled="), this.mLongPressHomeEnabled, printWriter, "  mAssistantTouchGestureEnabled="), this.mAssistantTouchGestureEnabled, printWriter, "  mAssistantAvailable="), this.mAssistantAvailable, printWriter, "  mNavBarMode=");
        m.append(this.mNavBarMode);
        printWriter.println(m.toString());
    }

    public final void init() {
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor("assistant"), false, this.mAssistContentObserver, -1);
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor("assist_long_press_home_enabled"), false, this.mAssistContentObserver, -1);
        this.mContentResolver.registerContentObserver(Settings.Secure.getUriFor("assist_touch_gesture_enabled"), false, this.mAssistContentObserver, -1);
        updateAssistantAvailability();
    }

    public final boolean isImeShown(int i) {
        boolean z;
        StatusBar statusBar = this.mStatusBarOptionalLazy.get().get();
        Objects.requireNonNull(statusBar);
        NotificationShadeWindowView notificationShadeWindowView = statusBar.mNotificationShadeWindowView;
        boolean isKeyguardShowing = this.mStatusBarOptionalLazy.get().get().isKeyguardShowing();
        if (notificationShadeWindowView == null || !notificationShadeWindowView.isAttachedToWindow() || !notificationShadeWindowView.getRootWindowInsets().isVisible(WindowInsets.Type.ime())) {
            z = false;
        } else {
            z = true;
        }
        if (z) {
            return true;
        }
        if (isKeyguardShowing || (i & 2) == 0) {
            return false;
        }
        return true;
    }

    @Override // com.android.systemui.recents.OverviewProxyService.OverviewProxyListener
    public final void onConnectionChanged(boolean z) {
        if (z) {
            updateAssistantAvailability();
        }
    }

    @Override // com.android.systemui.navigationbar.NavigationModeController.ModeChangedListener
    public final void onNavigationModeChanged(int i) {
        this.mNavBarMode = i;
        updateAssistantAvailability();
    }

    @Override // com.android.systemui.recents.OverviewProxyService.OverviewProxyListener
    public final void startAssistant(Bundle bundle) {
        this.mAssistManagerLazy.get().startAssist(bundle);
    }

    public final void updateA11yState() {
        boolean z;
        int i;
        int i2 = this.mA11yButtonState;
        AccessibilityButtonModeObserver accessibilityButtonModeObserver = this.mAccessibilityButtonModeObserver;
        Objects.requireNonNull(accessibilityButtonModeObserver);
        boolean z2 = true;
        int i3 = 0;
        boolean z3 = false;
        if (AccessibilityButtonModeObserver.parseAccessibilityButtonMode(Settings.Secure.getStringForUser(accessibilityButtonModeObserver.mContentResolver, accessibilityButtonModeObserver.mKey, -2)) == 1) {
            this.mA11yButtonState = 0;
            z2 = false;
        } else {
            int size = this.mAccessibilityManager.getAccessibilityShortcutTargets(0).size();
            if (size >= 1) {
                z = true;
            } else {
                z = false;
            }
            if (size < 2) {
                z2 = false;
            }
            if (z) {
                i = 16;
            } else {
                i = 0;
            }
            if (z2) {
                i3 = 32;
            }
            this.mA11yButtonState = i | i3;
            z3 = z;
        }
        if (i2 != this.mA11yButtonState) {
            updateSystemAction(z3, 11);
            updateSystemAction(z2, 12);
        }
    }

    public final void updateAssistantAvailability() {
        Object[] objArr;
        boolean z;
        boolean z2;
        AssistManager assistManager = this.mAssistManagerLazy.get();
        Objects.requireNonNull(assistManager);
        boolean z3 = true;
        if (assistManager.mAssistUtils.getAssistComponentForUser(-2) != null) {
            objArr = 1;
        } else {
            objArr = null;
        }
        if (Settings.Secure.getIntForUser(this.mContentResolver, "assist_long_press_home_enabled", this.mContext.getResources().getBoolean(17891368) ? 1 : 0, this.mUserTracker.getUserId()) != 0) {
            z = true;
        } else {
            z = false;
        }
        this.mLongPressHomeEnabled = z;
        if (Settings.Secure.getIntForUser(this.mContentResolver, "assist_touch_gesture_enabled", this.mContext.getResources().getBoolean(17891369) ? 1 : 0, this.mUserTracker.getUserId()) != 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.mAssistantTouchGestureEnabled = z2;
        if (objArr == null || !z2 || !R$color.isGesturalMode(this.mNavBarMode)) {
            z3 = false;
        }
        this.mAssistantAvailable = z3;
        Iterator it = this.mA11yEventListeners.iterator();
        while (it.hasNext()) {
            ((NavbarTaskbarStateUpdater) it.next()).updateAssistantAvailable(z3);
        }
    }

    public final void updateSystemAction(boolean z, int i) {
        int i2;
        String str;
        if (z) {
            SystemActions systemActions = this.mSystemActions;
            switch (i) {
                case 1:
                    i2 = 17039597;
                    str = "SYSTEM_ACTION_BACK";
                    break;
                case 2:
                    i2 = 17039606;
                    str = "SYSTEM_ACTION_HOME";
                    break;
                case 3:
                    i2 = 17039613;
                    str = "SYSTEM_ACTION_RECENTS";
                    break;
                case 4:
                    i2 = 17039608;
                    str = "SYSTEM_ACTION_NOTIFICATIONS";
                    break;
                case 5:
                    i2 = 17039612;
                    str = "SYSTEM_ACTION_QUICK_SETTINGS";
                    break;
                case FalsingManager.VERSION /* 6 */:
                    i2 = 17039611;
                    str = "SYSTEM_ACTION_POWER_DIALOG";
                    break;
                case 7:
                case 14:
                default:
                    Objects.requireNonNull(systemActions);
                    return;
                case 8:
                    i2 = 17039607;
                    str = "SYSTEM_ACTION_LOCK_SCREEN";
                    break;
                case 9:
                    i2 = 17039614;
                    str = "SYSTEM_ACTION_TAKE_SCREENSHOT";
                    break;
                case 10:
                    i2 = 17039605;
                    str = "SYSTEM_ACTION_HEADSET_HOOK";
                    break;
                case QSTileImpl.H.STALE /* 11 */:
                    i2 = 17039610;
                    str = "SYSTEM_ACTION_ACCESSIBILITY_BUTTON";
                    break;
                case KeyguardSliceProvider.ALARM_VISIBILITY_HOURS /* 12 */:
                    i2 = 17039609;
                    str = "SYSTEM_ACTION_ACCESSIBILITY_BUTTON_MENU";
                    break;
                case QS.VERSION /* 13 */:
                    i2 = 17039604;
                    str = "SYSTEM_ACTION_ACCESSIBILITY_SHORTCUT";
                    break;
                case 15:
                    i2 = 17039598;
                    str = "SYSTEM_ACTION_ACCESSIBILITY_DISMISS_NOTIFICATION_SHADE";
                    break;
                case 16:
                    i2 = 17039603;
                    str = "SYSTEM_ACTION_DPAD_UP";
                    break;
                case 17:
                    i2 = 17039600;
                    str = "SYSTEM_ACTION_DPAD_DOWN";
                    break;
                case 18:
                    i2 = 17039601;
                    str = "SYSTEM_ACTION_DPAD_LEFT";
                    break;
                case 19:
                    i2 = 17039602;
                    str = "SYSTEM_ACTION_DPAD_RIGHT";
                    break;
                case 20:
                    i2 = 17039599;
                    str = "SYSTEM_ACTION_DPAD_CENTER";
                    break;
            }
            systemActions.mA11yManager.registerSystemAction(systemActions.createRemoteAction(i2, str), i);
            return;
        }
        SystemActions systemActions2 = this.mSystemActions;
        Objects.requireNonNull(systemActions2);
        systemActions2.mA11yManager.unregisterSystemAction(i);
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.systemui.navigationbar.NavBarHelper$1] */
    public NavBarHelper(Context context, AccessibilityManager accessibilityManager, AccessibilityButtonModeObserver accessibilityButtonModeObserver, AccessibilityButtonTargetsObserver accessibilityButtonTargetsObserver, SystemActions systemActions, OverviewProxyService overviewProxyService, Lazy<AssistManager> lazy, Lazy<Optional<StatusBar>> lazy2, NavigationModeController navigationModeController, UserTracker userTracker, DumpManager dumpManager) {
        this.mContext = context;
        this.mContentResolver = context.getContentResolver();
        this.mAccessibilityManager = accessibilityManager;
        this.mAssistManagerLazy = lazy;
        this.mStatusBarOptionalLazy = lazy2;
        this.mUserTracker = userTracker;
        this.mSystemActions = systemActions;
        accessibilityManager.addAccessibilityServicesStateChangeListener(new AccessibilityManager.AccessibilityServicesStateChangeListener() { // from class: com.android.systemui.navigationbar.NavBarHelper$$ExternalSyntheticLambda0
            public final void onAccessibilityServicesStateChanged(AccessibilityManager accessibilityManager2) {
                NavBarHelper navBarHelper = NavBarHelper.this;
                Objects.requireNonNull(navBarHelper);
                navBarHelper.dispatchA11yEventUpdate();
            }
        });
        this.mAccessibilityButtonModeObserver = accessibilityButtonModeObserver;
        accessibilityButtonModeObserver.addListener(this);
        accessibilityButtonTargetsObserver.addListener(this);
        this.mNavBarMode = navigationModeController.addListener(this);
        overviewProxyService.addCallback((OverviewProxyService.OverviewProxyListener) this);
        dumpManager.registerDumpable(this);
    }

    @Override // com.android.systemui.accessibility.AccessibilityButtonModeObserver.ModeChangedListener
    public final void onAccessibilityButtonModeChanged(int i) {
        updateA11yState();
        dispatchA11yEventUpdate();
    }

    @Override // com.android.systemui.accessibility.AccessibilityButtonTargetsObserver.TargetsChangedListener
    public final void onAccessibilityButtonTargetsChanged(String str) {
        updateA11yState();
        dispatchA11yEventUpdate();
    }
}

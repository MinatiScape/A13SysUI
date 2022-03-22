package com.android.systemui.accessibility;

import android.app.PendingIntent;
import android.app.RemoteAction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.drawable.Icon;
import android.hardware.input.InputManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.Log;
import android.view.IWindowManager;
import android.view.KeyEvent;
import android.view.WindowManagerGlobal;
import android.view.accessibility.AccessibilityManager;
import com.android.internal.accessibility.dialog.AccessibilityButtonChooserActivity;
import com.android.internal.util.ScreenshotHelper;
import com.android.systemui.CoreStartable;
import com.android.systemui.assist.PhoneStateMonitor$$ExternalSyntheticLambda1;
import com.android.systemui.keyguard.KeyguardSliceProvider;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.qs.QSTileHost$$ExternalSyntheticLambda4;
import com.android.systemui.qs.QSTileHost$$ExternalSyntheticLambda5;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.recents.Recents;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarWindowCallback;
import com.android.systemui.theme.ThemeOverlayApplier;
import com.android.systemui.util.Assert;
import dagger.Lazy;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class SystemActions extends CoreStartable {
    public boolean mDismissNotificationShadeActionRegistered;
    public final NotificationShadeWindowController mNotificationShadeController;
    public final Optional<Recents> mRecentsOptional;
    public final Lazy<Optional<StatusBar>> mStatusBarOptionalLazy;
    public final SystemActionsBroadcastReceiver mReceiver = new SystemActionsBroadcastReceiver();
    public Locale mLocale = this.mContext.getResources().getConfiguration().getLocales().get(0);
    public final AccessibilityManager mA11yManager = (AccessibilityManager) this.mContext.getSystemService("accessibility");
    public final SystemActions$$ExternalSyntheticLambda0 mNotificationShadeCallback = new StatusBarWindowCallback() { // from class: com.android.systemui.accessibility.SystemActions$$ExternalSyntheticLambda0
        @Override // com.android.systemui.statusbar.phone.StatusBarWindowCallback
        public final void onStateChanged(boolean z, boolean z2, boolean z3, boolean z4) {
            SystemActions systemActions = SystemActions.this;
            Objects.requireNonNull(systemActions);
            systemActions.registerOrUnregisterDismissNotificationShadeAction();
        }
    };

    /* loaded from: classes.dex */
    public class SystemActionsBroadcastReceiver extends BroadcastReceiver {
        public static final /* synthetic */ int $r8$clinit = 0;

        public SystemActionsBroadcastReceiver() {
        }

        /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            char c;
            String action = intent.getAction();
            Objects.requireNonNull(action);
            switch (action.hashCode()) {
                case -1103811776:
                    if (action.equals("SYSTEM_ACTION_BACK")) {
                        c = 0;
                        break;
                    }
                    c = 65535;
                    break;
                case -1103619272:
                    if (action.equals("SYSTEM_ACTION_HOME")) {
                        c = 1;
                        break;
                    }
                    c = 65535;
                    break;
                case -720484549:
                    if (action.equals("SYSTEM_ACTION_POWER_DIALOG")) {
                        c = 2;
                        break;
                    }
                    c = 65535;
                    break;
                case -535129457:
                    if (action.equals("SYSTEM_ACTION_NOTIFICATIONS")) {
                        c = 3;
                        break;
                    }
                    c = 65535;
                    break;
                case -181386672:
                    if (action.equals("SYSTEM_ACTION_ACCESSIBILITY_SHORTCUT")) {
                        c = 4;
                        break;
                    }
                    c = 65535;
                    break;
                case -153384569:
                    if (action.equals("SYSTEM_ACTION_LOCK_SCREEN")) {
                        c = 5;
                        break;
                    }
                    c = 65535;
                    break;
                case 42571871:
                    if (action.equals("SYSTEM_ACTION_RECENTS")) {
                        c = 6;
                        break;
                    }
                    c = 65535;
                    break;
                case 526987266:
                    if (action.equals("SYSTEM_ACTION_ACCESSIBILITY_BUTTON_MENU")) {
                        c = 7;
                        break;
                    }
                    c = 65535;
                    break;
                case 689657964:
                    if (action.equals("SYSTEM_ACTION_DPAD_CENTER")) {
                        c = '\b';
                        break;
                    }
                    c = 65535;
                    break;
                case 815482418:
                    if (action.equals("SYSTEM_ACTION_DPAD_UP")) {
                        c = '\t';
                        break;
                    }
                    c = 65535;
                    break;
                case 1245940668:
                    if (action.equals("SYSTEM_ACTION_ACCESSIBILITY_BUTTON")) {
                        c = '\n';
                        break;
                    }
                    c = 65535;
                    break;
                case 1493428793:
                    if (action.equals("SYSTEM_ACTION_HEADSET_HOOK")) {
                        c = 11;
                        break;
                    }
                    c = 65535;
                    break;
                case 1579999269:
                    if (action.equals("SYSTEM_ACTION_TAKE_SCREENSHOT")) {
                        c = '\f';
                        break;
                    }
                    c = 65535;
                    break;
                case 1668921710:
                    if (action.equals("SYSTEM_ACTION_QUICK_SETTINGS")) {
                        c = '\r';
                        break;
                    }
                    c = 65535;
                    break;
                case 1698779909:
                    if (action.equals("SYSTEM_ACTION_DPAD_RIGHT")) {
                        c = 14;
                        break;
                    }
                    c = 65535;
                    break;
                case 1894867256:
                    if (action.equals("SYSTEM_ACTION_ACCESSIBILITY_DISMISS_NOTIFICATION_SHADE")) {
                        c = 15;
                        break;
                    }
                    c = 65535;
                    break;
                case 1994051193:
                    if (action.equals("SYSTEM_ACTION_DPAD_DOWN")) {
                        c = 16;
                        break;
                    }
                    c = 65535;
                    break;
                case 1994279390:
                    if (action.equals("SYSTEM_ACTION_DPAD_LEFT")) {
                        c = 17;
                        break;
                    }
                    c = 65535;
                    break;
                default:
                    c = 65535;
                    break;
            }
            switch (c) {
                case 0:
                    Objects.requireNonNull(SystemActions.this);
                    SystemActions.sendDownAndUpKeyEvents(4);
                    return;
                case 1:
                    Objects.requireNonNull(SystemActions.this);
                    SystemActions.sendDownAndUpKeyEvents(3);
                    return;
                case 2:
                    Objects.requireNonNull(SystemActions.this);
                    try {
                        WindowManagerGlobal.getWindowManagerService().showGlobalActions();
                        return;
                    } catch (RemoteException unused) {
                        Log.e("SystemActions", "failed to display power dialog.");
                        return;
                    }
                case 3:
                    SystemActions systemActions = SystemActions.this;
                    Objects.requireNonNull(systemActions);
                    systemActions.mStatusBarOptionalLazy.get().ifPresent(SystemActions$$ExternalSyntheticLambda2.INSTANCE);
                    return;
                case 4:
                    SystemActions systemActions2 = SystemActions.this;
                    Objects.requireNonNull(systemActions2);
                    systemActions2.mA11yManager.performAccessibilityShortcut();
                    return;
                case 5:
                    SystemActions systemActions3 = SystemActions.this;
                    Objects.requireNonNull(systemActions3);
                    IWindowManager windowManagerService = WindowManagerGlobal.getWindowManagerService();
                    ((PowerManager) systemActions3.mContext.getSystemService(PowerManager.class)).goToSleep(SystemClock.uptimeMillis(), 7, 0);
                    try {
                        windowManagerService.lockNow((Bundle) null);
                        return;
                    } catch (RemoteException unused2) {
                        Log.e("SystemActions", "failed to lock screen.");
                        return;
                    }
                case FalsingManager.VERSION /* 6 */:
                    SystemActions systemActions4 = SystemActions.this;
                    Objects.requireNonNull(systemActions4);
                    systemActions4.mRecentsOptional.ifPresent(SystemActions$$ExternalSyntheticLambda1.INSTANCE);
                    return;
                case 7:
                    SystemActions systemActions5 = SystemActions.this;
                    Objects.requireNonNull(systemActions5);
                    Intent intent2 = new Intent("com.android.internal.intent.action.CHOOSE_ACCESSIBILITY_BUTTON");
                    intent2.addFlags(268468224);
                    intent2.setClassName(ThemeOverlayApplier.ANDROID_PACKAGE, AccessibilityButtonChooserActivity.class.getName());
                    systemActions5.mContext.startActivityAsUser(intent2, UserHandle.CURRENT);
                    return;
                case '\b':
                    Objects.requireNonNull(SystemActions.this);
                    SystemActions.sendDownAndUpKeyEvents(23);
                    return;
                case '\t':
                    Objects.requireNonNull(SystemActions.this);
                    SystemActions.sendDownAndUpKeyEvents(19);
                    return;
                case '\n':
                    SystemActions systemActions6 = SystemActions.this;
                    Objects.requireNonNull(systemActions6);
                    AccessibilityManager.getInstance(systemActions6.mContext).notifyAccessibilityButtonClicked(0);
                    return;
                case QSTileImpl.H.STALE /* 11 */:
                    Objects.requireNonNull(SystemActions.this);
                    SystemActions.sendDownAndUpKeyEvents(79);
                    return;
                case KeyguardSliceProvider.ALARM_VISIBILITY_HOURS /* 12 */:
                    SystemActions systemActions7 = SystemActions.this;
                    Objects.requireNonNull(systemActions7);
                    new ScreenshotHelper(systemActions7.mContext).takeScreenshot(1, true, true, 4, new Handler(Looper.getMainLooper()), (Consumer) null);
                    return;
                case QS.VERSION /* 13 */:
                    SystemActions systemActions8 = SystemActions.this;
                    Objects.requireNonNull(systemActions8);
                    systemActions8.mStatusBarOptionalLazy.get().ifPresent(QSTileHost$$ExternalSyntheticLambda5.INSTANCE$1);
                    return;
                case 14:
                    Objects.requireNonNull(SystemActions.this);
                    SystemActions.sendDownAndUpKeyEvents(22);
                    return;
                case 15:
                    SystemActions systemActions9 = SystemActions.this;
                    Objects.requireNonNull(systemActions9);
                    systemActions9.mStatusBarOptionalLazy.get().ifPresent(QSTileHost$$ExternalSyntheticLambda4.INSTANCE$1);
                    return;
                case 16:
                    Objects.requireNonNull(SystemActions.this);
                    SystemActions.sendDownAndUpKeyEvents(20);
                    return;
                case 17:
                    Objects.requireNonNull(SystemActions.this);
                    SystemActions.sendDownAndUpKeyEvents(21);
                    return;
                default:
                    return;
            }
        }
    }

    public static void sendKeyEventIdentityCleared(int i, int i2, long j, long j2) {
        KeyEvent obtain = KeyEvent.obtain(j, j2, i2, i, 0, 0, -1, 0, 8, 257, null);
        InputManager.getInstance().injectInputEvent(obtain, 0);
        obtain.recycle();
    }

    public final RemoteAction createRemoteAction(int i, String str) {
        PendingIntent pendingIntent;
        Icon createWithResource = Icon.createWithResource(this.mContext, 17301684);
        String string = this.mContext.getString(i);
        String string2 = this.mContext.getString(i);
        SystemActionsBroadcastReceiver systemActionsBroadcastReceiver = this.mReceiver;
        Context context = this.mContext;
        int i2 = SystemActionsBroadcastReceiver.$r8$clinit;
        Objects.requireNonNull(systemActionsBroadcastReceiver);
        char c = 65535;
        switch (str.hashCode()) {
            case -1103811776:
                if (str.equals("SYSTEM_ACTION_BACK")) {
                    c = 0;
                    break;
                }
                break;
            case -1103619272:
                if (str.equals("SYSTEM_ACTION_HOME")) {
                    c = 1;
                    break;
                }
                break;
            case -720484549:
                if (str.equals("SYSTEM_ACTION_POWER_DIALOG")) {
                    c = 2;
                    break;
                }
                break;
            case -535129457:
                if (str.equals("SYSTEM_ACTION_NOTIFICATIONS")) {
                    c = 3;
                    break;
                }
                break;
            case -181386672:
                if (str.equals("SYSTEM_ACTION_ACCESSIBILITY_SHORTCUT")) {
                    c = 4;
                    break;
                }
                break;
            case -153384569:
                if (str.equals("SYSTEM_ACTION_LOCK_SCREEN")) {
                    c = 5;
                    break;
                }
                break;
            case 42571871:
                if (str.equals("SYSTEM_ACTION_RECENTS")) {
                    c = 6;
                    break;
                }
                break;
            case 526987266:
                if (str.equals("SYSTEM_ACTION_ACCESSIBILITY_BUTTON_MENU")) {
                    c = 7;
                    break;
                }
                break;
            case 689657964:
                if (str.equals("SYSTEM_ACTION_DPAD_CENTER")) {
                    c = '\b';
                    break;
                }
                break;
            case 815482418:
                if (str.equals("SYSTEM_ACTION_DPAD_UP")) {
                    c = '\t';
                    break;
                }
                break;
            case 1245940668:
                if (str.equals("SYSTEM_ACTION_ACCESSIBILITY_BUTTON")) {
                    c = '\n';
                    break;
                }
                break;
            case 1493428793:
                if (str.equals("SYSTEM_ACTION_HEADSET_HOOK")) {
                    c = 11;
                    break;
                }
                break;
            case 1579999269:
                if (str.equals("SYSTEM_ACTION_TAKE_SCREENSHOT")) {
                    c = '\f';
                    break;
                }
                break;
            case 1668921710:
                if (str.equals("SYSTEM_ACTION_QUICK_SETTINGS")) {
                    c = '\r';
                    break;
                }
                break;
            case 1698779909:
                if (str.equals("SYSTEM_ACTION_DPAD_RIGHT")) {
                    c = 14;
                    break;
                }
                break;
            case 1894867256:
                if (str.equals("SYSTEM_ACTION_ACCESSIBILITY_DISMISS_NOTIFICATION_SHADE")) {
                    c = 15;
                    break;
                }
                break;
            case 1994051193:
                if (str.equals("SYSTEM_ACTION_DPAD_DOWN")) {
                    c = 16;
                    break;
                }
                break;
            case 1994279390:
                if (str.equals("SYSTEM_ACTION_DPAD_LEFT")) {
                    c = 17;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case FalsingManager.VERSION /* 6 */:
            case 7:
            case '\b':
            case '\t':
            case '\n':
            case QSTileImpl.H.STALE /* 11 */:
            case KeyguardSliceProvider.ALARM_VISIBILITY_HOURS /* 12 */:
            case QS.VERSION /* 13 */:
            case 14:
            case 15:
            case 16:
            case 17:
                Intent intent = new Intent(str);
                intent.setPackage(context.getPackageName());
                pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 67108864);
                break;
            default:
                pendingIntent = null;
                break;
        }
        return new RemoteAction(createWithResource, string, string2, pendingIntent);
    }

    @Override // com.android.systemui.CoreStartable
    public final void onConfigurationChanged(Configuration configuration) {
        Locale locale = this.mContext.getResources().getConfiguration().getLocales().get(0);
        if (!locale.equals(this.mLocale)) {
            this.mLocale = locale;
            registerActions();
        }
    }

    public final void registerActions() {
        RemoteAction createRemoteAction = createRemoteAction(17039597, "SYSTEM_ACTION_BACK");
        RemoteAction createRemoteAction2 = createRemoteAction(17039606, "SYSTEM_ACTION_HOME");
        RemoteAction createRemoteAction3 = createRemoteAction(17039613, "SYSTEM_ACTION_RECENTS");
        RemoteAction createRemoteAction4 = createRemoteAction(17039608, "SYSTEM_ACTION_NOTIFICATIONS");
        RemoteAction createRemoteAction5 = createRemoteAction(17039612, "SYSTEM_ACTION_QUICK_SETTINGS");
        RemoteAction createRemoteAction6 = createRemoteAction(17039611, "SYSTEM_ACTION_POWER_DIALOG");
        RemoteAction createRemoteAction7 = createRemoteAction(17039607, "SYSTEM_ACTION_LOCK_SCREEN");
        RemoteAction createRemoteAction8 = createRemoteAction(17039614, "SYSTEM_ACTION_TAKE_SCREENSHOT");
        RemoteAction createRemoteAction9 = createRemoteAction(17039605, "SYSTEM_ACTION_HEADSET_HOOK");
        RemoteAction createRemoteAction10 = createRemoteAction(17039604, "SYSTEM_ACTION_ACCESSIBILITY_SHORTCUT");
        RemoteAction createRemoteAction11 = createRemoteAction(17039603, "SYSTEM_ACTION_DPAD_UP");
        RemoteAction createRemoteAction12 = createRemoteAction(17039600, "SYSTEM_ACTION_DPAD_DOWN");
        RemoteAction createRemoteAction13 = createRemoteAction(17039601, "SYSTEM_ACTION_DPAD_LEFT");
        RemoteAction createRemoteAction14 = createRemoteAction(17039602, "SYSTEM_ACTION_DPAD_RIGHT");
        RemoteAction createRemoteAction15 = createRemoteAction(17039599, "SYSTEM_ACTION_DPAD_CENTER");
        this.mA11yManager.registerSystemAction(createRemoteAction, 1);
        this.mA11yManager.registerSystemAction(createRemoteAction2, 2);
        this.mA11yManager.registerSystemAction(createRemoteAction3, 3);
        this.mA11yManager.registerSystemAction(createRemoteAction4, 4);
        this.mA11yManager.registerSystemAction(createRemoteAction5, 5);
        this.mA11yManager.registerSystemAction(createRemoteAction6, 6);
        this.mA11yManager.registerSystemAction(createRemoteAction7, 8);
        this.mA11yManager.registerSystemAction(createRemoteAction8, 9);
        this.mA11yManager.registerSystemAction(createRemoteAction9, 10);
        this.mA11yManager.registerSystemAction(createRemoteAction10, 13);
        this.mA11yManager.registerSystemAction(createRemoteAction11, 16);
        this.mA11yManager.registerSystemAction(createRemoteAction12, 17);
        this.mA11yManager.registerSystemAction(createRemoteAction13, 18);
        this.mA11yManager.registerSystemAction(createRemoteAction14, 19);
        this.mA11yManager.registerSystemAction(createRemoteAction15, 20);
        registerOrUnregisterDismissNotificationShadeAction();
    }

    @Override // com.android.systemui.CoreStartable
    public final void start() {
        this.mNotificationShadeController.registerCallback(this.mNotificationShadeCallback);
        Context context = this.mContext;
        SystemActionsBroadcastReceiver systemActionsBroadcastReceiver = this.mReceiver;
        int i = SystemActionsBroadcastReceiver.$r8$clinit;
        Objects.requireNonNull(systemActionsBroadcastReceiver);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("SYSTEM_ACTION_BACK");
        intentFilter.addAction("SYSTEM_ACTION_HOME");
        intentFilter.addAction("SYSTEM_ACTION_RECENTS");
        intentFilter.addAction("SYSTEM_ACTION_NOTIFICATIONS");
        intentFilter.addAction("SYSTEM_ACTION_QUICK_SETTINGS");
        intentFilter.addAction("SYSTEM_ACTION_POWER_DIALOG");
        intentFilter.addAction("SYSTEM_ACTION_LOCK_SCREEN");
        intentFilter.addAction("SYSTEM_ACTION_TAKE_SCREENSHOT");
        intentFilter.addAction("SYSTEM_ACTION_HEADSET_HOOK");
        intentFilter.addAction("SYSTEM_ACTION_ACCESSIBILITY_BUTTON");
        intentFilter.addAction("SYSTEM_ACTION_ACCESSIBILITY_BUTTON_MENU");
        intentFilter.addAction("SYSTEM_ACTION_ACCESSIBILITY_SHORTCUT");
        intentFilter.addAction("SYSTEM_ACTION_ACCESSIBILITY_DISMISS_NOTIFICATION_SHADE");
        intentFilter.addAction("SYSTEM_ACTION_DPAD_UP");
        intentFilter.addAction("SYSTEM_ACTION_DPAD_DOWN");
        intentFilter.addAction("SYSTEM_ACTION_DPAD_LEFT");
        intentFilter.addAction("SYSTEM_ACTION_DPAD_RIGHT");
        intentFilter.addAction("SYSTEM_ACTION_DPAD_CENTER");
        context.registerReceiverForAllUsers(systemActionsBroadcastReceiver, intentFilter, "com.android.systemui.permission.SELF", null, 2);
        registerActions();
    }

    /* JADX WARN: Type inference failed for: r1v10, types: [com.android.systemui.accessibility.SystemActions$$ExternalSyntheticLambda0] */
    public SystemActions(Context context, NotificationShadeWindowController notificationShadeWindowController, Lazy<Optional<StatusBar>> lazy, Optional<Recents> optional) {
        super(context);
        this.mRecentsOptional = optional;
        this.mNotificationShadeController = notificationShadeWindowController;
        this.mStatusBarOptionalLazy = lazy;
    }

    public static void sendDownAndUpKeyEvents(int i) {
        long uptimeMillis = SystemClock.uptimeMillis();
        sendKeyEventIdentityCleared(i, 0, uptimeMillis, uptimeMillis);
        sendKeyEventIdentityCleared(i, 1, uptimeMillis, SystemClock.uptimeMillis());
    }

    public final void registerOrUnregisterDismissNotificationShadeAction() {
        Assert.isMainThread();
        Optional<StatusBar> optional = this.mStatusBarOptionalLazy.get();
        if (!((Boolean) optional.map(PhoneStateMonitor$$ExternalSyntheticLambda1.INSTANCE$1).orElse(Boolean.FALSE)).booleanValue() || optional.get().isKeyguardShowing()) {
            if (this.mDismissNotificationShadeActionRegistered) {
                this.mA11yManager.unregisterSystemAction(15);
                this.mDismissNotificationShadeActionRegistered = false;
            }
        } else if (!this.mDismissNotificationShadeActionRegistered) {
            this.mA11yManager.registerSystemAction(createRemoteAction(17039598, "SYSTEM_ACTION_ACCESSIBILITY_DISMISS_NOTIFICATION_SHADE"), 15);
            this.mDismissNotificationShadeActionRegistered = true;
        }
    }
}

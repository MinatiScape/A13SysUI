package com.android.wm.shell.onehanded;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Handler;
import android.os.UserHandle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Slog;
import android.view.accessibility.AccessibilityManager;
import android.window.WindowContainerTransaction;
import com.android.systemui.ScreenDecorations$2$$ExternalSyntheticLambda0;
import com.android.systemui.doze.DozeScreenState$$ExternalSyntheticLambda0;
import com.android.systemui.keyguard.KeyguardViewMediator$$ExternalSyntheticLambda2;
import com.android.systemui.qs.tileimpl.QSTileImpl$$ExternalSyntheticLambda0;
import com.android.systemui.qs.tiles.ScreenRecordTile$$ExternalSyntheticLambda1;
import com.android.systemui.screenshot.ScreenshotController$$ExternalSyntheticLambda6;
import com.android.systemui.wmshell.WMShell;
import com.android.wifitrackerlib.StandardWifiEntry$$ExternalSyntheticLambda0;
import com.android.wm.shell.common.DisplayChangeController;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.RemoteCallable;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.onehanded.IOneHanded;
import com.android.wm.shell.onehanded.OneHandedController;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public final class OneHandedController implements RemoteCallable<OneHandedController>, DisplayChangeController.OnDisplayChangingListener {
    public final AccessibilityManager mAccessibilityManager;
    public final AnonymousClass5 mActivatedObserver;
    public Context mContext;
    public OneHandedDisplayAreaOrganizer mDisplayAreaOrganizer;
    public final DisplayController mDisplayController;
    public final AnonymousClass1 mDisplaysChangedListener;
    public final AnonymousClass5 mEnabledObserver;
    public OneHandedEventCallback mEventCallback;
    public volatile boolean mIsOneHandedEnabled;
    public boolean mIsShortcutEnabled;
    public volatile boolean mIsSwipeToNotificationEnabled;
    public boolean mKeyguardShowing;
    public boolean mLockedDisabled;
    public final ShellExecutor mMainExecutor;
    public final Handler mMainHandler;
    public float mOffSetFraction;
    public final OneHandedAccessibilityUtil mOneHandedAccessibilityUtil;
    public final OneHandedSettingsUtil mOneHandedSettingsUtil;
    public OneHandedUiEventLogger mOneHandedUiEventLogger;
    public final AnonymousClass5 mShortcutEnabledObserver;
    public final OneHandedState mState;
    public final AnonymousClass5 mSwipeToNotificationEnabledObserver;
    public boolean mTaskChangeToExit;
    public final TaskStackListenerImpl mTaskStackListener;
    public final AnonymousClass4 mTaskStackListenerCallback;
    public final OneHandedTimeoutHandler mTimeoutHandler;
    public final OneHandedTouchHandler mTouchHandler;
    public final AnonymousClass3 mTransitionCallBack;
    public final OneHandedTutorialHandler mTutorialHandler;
    public final OneHandedImpl mImpl = new OneHandedImpl();
    public AnonymousClass2 mAccessibilityStateChangeListener = new AccessibilityManager.AccessibilityStateChangeListener() { // from class: com.android.wm.shell.onehanded.OneHandedController.2
        @Override // android.view.accessibility.AccessibilityManager.AccessibilityStateChangeListener
        public final void onAccessibilityStateChanged(boolean z) {
            if (OneHandedController.this.isInitialized()) {
                if (z) {
                    OneHandedController oneHandedController = OneHandedController.this;
                    OneHandedSettingsUtil oneHandedSettingsUtil = oneHandedController.mOneHandedSettingsUtil;
                    ContentResolver contentResolver = oneHandedController.mContext.getContentResolver();
                    int i = OneHandedController.this.mUserId;
                    Objects.requireNonNull(oneHandedSettingsUtil);
                    int recommendedTimeoutMillis = OneHandedController.this.mAccessibilityManager.getRecommendedTimeoutMillis(Settings.Secure.getIntForUser(contentResolver, "one_handed_mode_timeout", 8, i) * 1000, 4);
                    OneHandedTimeoutHandler oneHandedTimeoutHandler = OneHandedController.this.mTimeoutHandler;
                    int i2 = recommendedTimeoutMillis / 1000;
                    Objects.requireNonNull(oneHandedTimeoutHandler);
                    oneHandedTimeoutHandler.mTimeout = i2;
                    oneHandedTimeoutHandler.mTimeoutMs = TimeUnit.SECONDS.toMillis(i2);
                    oneHandedTimeoutHandler.resetTimer();
                    return;
                }
                OneHandedController oneHandedController2 = OneHandedController.this;
                OneHandedTimeoutHandler oneHandedTimeoutHandler2 = oneHandedController2.mTimeoutHandler;
                OneHandedSettingsUtil oneHandedSettingsUtil2 = oneHandedController2.mOneHandedSettingsUtil;
                ContentResolver contentResolver2 = oneHandedController2.mContext.getContentResolver();
                int i3 = OneHandedController.this.mUserId;
                Objects.requireNonNull(oneHandedSettingsUtil2);
                int intForUser = Settings.Secure.getIntForUser(contentResolver2, "one_handed_mode_timeout", 8, i3);
                Objects.requireNonNull(oneHandedTimeoutHandler2);
                oneHandedTimeoutHandler2.mTimeout = intForUser;
                oneHandedTimeoutHandler2.mTimeoutMs = TimeUnit.SECONDS.toMillis(intForUser);
                oneHandedTimeoutHandler2.resetTimer();
            }
        }
    };
    public int mUserId = UserHandle.myUserId();

    /* loaded from: classes.dex */
    public class OneHandedImpl implements OneHanded {
        public static final /* synthetic */ int $r8$clinit = 0;
        public IOneHandedImpl mIOneHanded;

        @Override // com.android.wm.shell.onehanded.OneHanded
        public final void stopOneHanded() {
            OneHandedController.this.mMainExecutor.execute(new StandardWifiEntry$$ExternalSyntheticLambda0(this, 10));
        }

        public OneHandedImpl() {
        }

        @Override // com.android.wm.shell.onehanded.OneHanded
        public final IOneHanded createExternalInterface() {
            IOneHandedImpl iOneHandedImpl = this.mIOneHanded;
            if (iOneHandedImpl != null) {
                Objects.requireNonNull(iOneHandedImpl);
                iOneHandedImpl.mController = null;
            }
            IOneHandedImpl iOneHandedImpl2 = new IOneHandedImpl(OneHandedController.this);
            this.mIOneHanded = iOneHandedImpl2;
            return iOneHandedImpl2;
        }

        @Override // com.android.wm.shell.onehanded.OneHanded
        public final void onConfigChanged(Configuration configuration) {
            OneHandedController.this.mMainExecutor.execute(new ScreenDecorations$2$$ExternalSyntheticLambda0(this, configuration, 3));
        }

        @Override // com.android.wm.shell.onehanded.OneHanded
        public final void onKeyguardVisibilityChanged(final boolean z) {
            OneHandedController.this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.onehanded.OneHandedController$OneHandedImpl$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    OneHandedController.OneHandedImpl oneHandedImpl = OneHandedController.OneHandedImpl.this;
                    boolean z2 = z;
                    Objects.requireNonNull(oneHandedImpl);
                    OneHandedController oneHandedController = OneHandedController.this;
                    Objects.requireNonNull(oneHandedController);
                    oneHandedController.mKeyguardShowing = z2;
                }
            });
        }

        @Override // com.android.wm.shell.onehanded.OneHanded
        public final void onUserSwitch(int i) {
            OneHandedController.this.mMainExecutor.execute(new KeyguardViewMediator$$ExternalSyntheticLambda2(this, i, 1));
        }

        @Override // com.android.wm.shell.onehanded.OneHanded
        public final void registerEventCallback(WMShell.AnonymousClass8 r4) {
            OneHandedController.this.mMainExecutor.execute(new ScreenshotController$$ExternalSyntheticLambda6(this, r4, 3));
        }

        @Override // com.android.wm.shell.onehanded.OneHanded
        public final void registerTransitionCallback(OneHandedTransitionCallback oneHandedTransitionCallback) {
            OneHandedController.this.mMainExecutor.execute(new ScreenRecordTile$$ExternalSyntheticLambda1(this, oneHandedTransitionCallback, 4));
        }

        @Override // com.android.wm.shell.onehanded.OneHanded
        public final void setLockedDisabled(final boolean z) {
            OneHandedController.this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.onehanded.OneHandedController$OneHandedImpl$$ExternalSyntheticLambda2
                public final /* synthetic */ boolean f$2 = false;

                @Override // java.lang.Runnable
                public final void run() {
                    OneHandedController.OneHandedImpl oneHandedImpl = OneHandedController.OneHandedImpl.this;
                    boolean z2 = z;
                    boolean z3 = this.f$2;
                    Objects.requireNonNull(oneHandedImpl);
                    OneHandedController.this.setLockedDisabled(z2, z3);
                }
            });
        }

        @Override // com.android.wm.shell.onehanded.OneHanded
        public final void stopOneHanded(final int i) {
            OneHandedController.this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.onehanded.OneHandedController$OneHandedImpl$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    OneHandedController.OneHandedImpl oneHandedImpl = OneHandedController.OneHandedImpl.this;
                    int i2 = i;
                    Objects.requireNonNull(oneHandedImpl);
                    OneHandedController.this.stopOneHanded(i2);
                }
            });
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r10v0, types: [com.android.wm.shell.common.TaskStackListenerCallback, com.android.wm.shell.onehanded.OneHandedController$4] */
    /* JADX WARN: Type inference failed for: r12v3, types: [com.android.wm.shell.onehanded.OneHandedController$5] */
    /* JADX WARN: Type inference failed for: r12v5, types: [com.android.wm.shell.onehanded.OneHandedController$5] */
    /* JADX WARN: Type inference failed for: r12v7, types: [com.android.wm.shell.onehanded.OneHandedController$5] */
    /* JADX WARN: Type inference failed for: r13v7, types: [com.android.wm.shell.onehanded.OneHandedController$5] */
    /* JADX WARN: Type inference failed for: r8v1, types: [com.android.wm.shell.common.DisplayController$OnDisplaysChangedListener, com.android.wm.shell.onehanded.OneHandedController$1] */
    /* JADX WARN: Type inference failed for: r9v0, types: [com.android.wm.shell.onehanded.OneHandedController$2] */
    /* JADX WARN: Type inference failed for: r9v1, types: [com.android.wm.shell.onehanded.OneHandedController$3, java.lang.Object] */
    /* JADX WARN: Unknown variable types count: 3 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public OneHandedController(android.content.Context r16, com.android.wm.shell.common.DisplayController r17, com.android.wm.shell.onehanded.OneHandedDisplayAreaOrganizer r18, com.android.wm.shell.onehanded.OneHandedTouchHandler r19, com.android.wm.shell.onehanded.OneHandedTutorialHandler r20, com.android.wm.shell.onehanded.OneHandedSettingsUtil r21, com.android.wm.shell.onehanded.OneHandedAccessibilityUtil r22, com.android.wm.shell.onehanded.OneHandedTimeoutHandler r23, com.android.wm.shell.onehanded.OneHandedState r24, com.android.internal.jank.InteractionJankMonitor r25, com.android.wm.shell.onehanded.OneHandedUiEventLogger r26, android.content.om.IOverlayManager r27, com.android.wm.shell.common.TaskStackListenerImpl r28, com.android.wm.shell.common.ShellExecutor r29, android.os.Handler r30) {
        /*
            Method dump skipped, instructions count: 308
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.onehanded.OneHandedController.<init>(android.content.Context, com.android.wm.shell.common.DisplayController, com.android.wm.shell.onehanded.OneHandedDisplayAreaOrganizer, com.android.wm.shell.onehanded.OneHandedTouchHandler, com.android.wm.shell.onehanded.OneHandedTutorialHandler, com.android.wm.shell.onehanded.OneHandedSettingsUtil, com.android.wm.shell.onehanded.OneHandedAccessibilityUtil, com.android.wm.shell.onehanded.OneHandedTimeoutHandler, com.android.wm.shell.onehanded.OneHandedState, com.android.internal.jank.InteractionJankMonitor, com.android.wm.shell.onehanded.OneHandedUiEventLogger, android.content.om.IOverlayManager, com.android.wm.shell.common.TaskStackListenerImpl, com.android.wm.shell.common.ShellExecutor, android.os.Handler):void");
    }

    public void stopOneHanded() {
        stopOneHanded(1);
    }

    /* loaded from: classes.dex */
    public static class IOneHandedImpl extends IOneHanded.Stub {
        public static final /* synthetic */ int $r8$clinit = 0;
        public OneHandedController mController;

        public IOneHandedImpl(OneHandedController oneHandedController) {
            this.mController = oneHandedController;
        }
    }

    public final boolean isInitialized() {
        if (this.mDisplayAreaOrganizer != null && this.mDisplayController != null && this.mOneHandedSettingsUtil != null) {
            return true;
        }
        Slog.w("OneHandedController", "Components may not initialized yet!");
        return false;
    }

    public void notifyExpandNotification() {
        if (this.mEventCallback != null) {
            this.mMainExecutor.execute(new QSTileImpl$$ExternalSyntheticLambda0(this, 7));
        }
    }

    public void onEnabledSettingChanged() {
        int i;
        OneHandedSettingsUtil oneHandedSettingsUtil = this.mOneHandedSettingsUtil;
        ContentResolver contentResolver = this.mContext.getContentResolver();
        int i2 = this.mUserId;
        Objects.requireNonNull(oneHandedSettingsUtil);
        boolean settingsOneHandedModeEnabled = OneHandedSettingsUtil.getSettingsOneHandedModeEnabled(contentResolver, i2);
        OneHandedUiEventLogger oneHandedUiEventLogger = this.mOneHandedUiEventLogger;
        if (settingsOneHandedModeEnabled) {
            i = 8;
        } else {
            i = 9;
        }
        oneHandedUiEventLogger.writeEvent(i);
        this.mIsOneHandedEnabled = settingsOneHandedModeEnabled;
        updateOneHandedEnabled();
    }

    public final void onShortcutEnabledChanged() {
        int i;
        OneHandedSettingsUtil oneHandedSettingsUtil = this.mOneHandedSettingsUtil;
        ContentResolver contentResolver = this.mContext.getContentResolver();
        int i2 = this.mUserId;
        Objects.requireNonNull(oneHandedSettingsUtil);
        String stringForUser = Settings.Secure.getStringForUser(contentResolver, "accessibility_button_targets", i2);
        boolean z = true;
        if (TextUtils.isEmpty(stringForUser) || !stringForUser.contains(OneHandedSettingsUtil.ONE_HANDED_MODE_TARGET_NAME)) {
            String stringForUser2 = Settings.Secure.getStringForUser(contentResolver, "accessibility_shortcut_target_service", i2);
            if (TextUtils.isEmpty(stringForUser2) || !stringForUser2.contains(OneHandedSettingsUtil.ONE_HANDED_MODE_TARGET_NAME)) {
                z = false;
            }
        }
        this.mIsShortcutEnabled = z;
        OneHandedUiEventLogger oneHandedUiEventLogger = this.mOneHandedUiEventLogger;
        if (z) {
            i = 20;
        } else {
            i = 21;
        }
        oneHandedUiEventLogger.writeEvent(i);
    }

    public void onSwipeToNotificationEnabledChanged() {
        int i;
        OneHandedSettingsUtil oneHandedSettingsUtil = this.mOneHandedSettingsUtil;
        ContentResolver contentResolver = this.mContext.getContentResolver();
        int i2 = this.mUserId;
        Objects.requireNonNull(oneHandedSettingsUtil);
        boolean settingsSwipeToNotificationEnabled = OneHandedSettingsUtil.getSettingsSwipeToNotificationEnabled(contentResolver, i2);
        this.mIsSwipeToNotificationEnabled = settingsSwipeToNotificationEnabled;
        Objects.requireNonNull(this.mState);
        notifyShortcutStateChanged(OneHandedState.sCurrentState);
        OneHandedUiEventLogger oneHandedUiEventLogger = this.mOneHandedUiEventLogger;
        if (settingsSwipeToNotificationEnabled) {
            i = 18;
        } else {
            i = 19;
        }
        oneHandedUiEventLogger.writeEvent(i);
    }

    public final void registerSettingObservers(int i) {
        OneHandedSettingsUtil oneHandedSettingsUtil = this.mOneHandedSettingsUtil;
        ContentResolver contentResolver = this.mContext.getContentResolver();
        AnonymousClass5 r2 = this.mActivatedObserver;
        Objects.requireNonNull(oneHandedSettingsUtil);
        OneHandedSettingsUtil.registerSettingsKeyObserver("one_handed_mode_activated", contentResolver, r2, i);
        OneHandedSettingsUtil oneHandedSettingsUtil2 = this.mOneHandedSettingsUtil;
        ContentResolver contentResolver2 = this.mContext.getContentResolver();
        AnonymousClass5 r22 = this.mEnabledObserver;
        Objects.requireNonNull(oneHandedSettingsUtil2);
        OneHandedSettingsUtil.registerSettingsKeyObserver("one_handed_mode_enabled", contentResolver2, r22, i);
        OneHandedSettingsUtil oneHandedSettingsUtil3 = this.mOneHandedSettingsUtil;
        ContentResolver contentResolver3 = this.mContext.getContentResolver();
        AnonymousClass5 r23 = this.mSwipeToNotificationEnabledObserver;
        Objects.requireNonNull(oneHandedSettingsUtil3);
        OneHandedSettingsUtil.registerSettingsKeyObserver("swipe_bottom_to_notification_enabled", contentResolver3, r23, i);
        OneHandedSettingsUtil oneHandedSettingsUtil4 = this.mOneHandedSettingsUtil;
        ContentResolver contentResolver4 = this.mContext.getContentResolver();
        AnonymousClass5 r24 = this.mShortcutEnabledObserver;
        Objects.requireNonNull(oneHandedSettingsUtil4);
        OneHandedSettingsUtil.registerSettingsKeyObserver("accessibility_button_targets", contentResolver4, r24, i);
        OneHandedSettingsUtil oneHandedSettingsUtil5 = this.mOneHandedSettingsUtil;
        ContentResolver contentResolver5 = this.mContext.getContentResolver();
        AnonymousClass5 r3 = this.mShortcutEnabledObserver;
        Objects.requireNonNull(oneHandedSettingsUtil5);
        OneHandedSettingsUtil.registerSettingsKeyObserver("accessibility_shortcut_target_service", contentResolver5, r3, i);
    }

    public void setLockedDisabled(boolean z, boolean z2) {
        boolean z3;
        boolean z4 = false;
        if (this.mIsOneHandedEnabled || this.mIsSwipeToNotificationEnabled) {
            z3 = true;
        } else {
            z3 = false;
        }
        if (z2 != z3) {
            if (z && !z2) {
                z4 = true;
            }
            this.mLockedDisabled = z4;
        }
    }

    public final void stopOneHanded(int i) {
        Objects.requireNonNull(this.mState);
        int i2 = OneHandedState.sCurrentState;
        boolean z = true;
        if (!(i2 == 1 || i2 == 3)) {
            z = false;
        }
        if (!z) {
            Objects.requireNonNull(this.mState);
            if (OneHandedState.sCurrentState != 0) {
                this.mState.setState(3);
                OneHandedAccessibilityUtil oneHandedAccessibilityUtil = this.mOneHandedAccessibilityUtil;
                Objects.requireNonNull(oneHandedAccessibilityUtil);
                oneHandedAccessibilityUtil.announcementForScreenReader(oneHandedAccessibilityUtil.mStopOneHandedDescription);
                this.mDisplayAreaOrganizer.scheduleOffset(0);
                OneHandedTimeoutHandler oneHandedTimeoutHandler = this.mTimeoutHandler;
                Objects.requireNonNull(oneHandedTimeoutHandler);
                oneHandedTimeoutHandler.mMainExecutor.removeCallbacks(oneHandedTimeoutHandler.mTimeoutRunnable);
                this.mOneHandedUiEventLogger.writeEvent(i);
            }
        }
    }

    public void updateDisplayLayout(int i) {
        DisplayLayout displayLayout = this.mDisplayController.getDisplayLayout(i);
        if (displayLayout == null) {
            Slog.w("OneHandedController", "Failed to get new DisplayLayout.");
            return;
        }
        this.mDisplayAreaOrganizer.setDisplayLayout(displayLayout);
        OneHandedTutorialHandler oneHandedTutorialHandler = this.mTutorialHandler;
        Objects.requireNonNull(oneHandedTutorialHandler);
        if (displayLayout.mHeight > displayLayout.mWidth) {
            oneHandedTutorialHandler.mDisplayBounds = new Rect(0, 0, displayLayout.mWidth, displayLayout.mHeight);
        } else {
            oneHandedTutorialHandler.mDisplayBounds = new Rect(0, 0, displayLayout.mHeight, displayLayout.mWidth);
        }
        int round = Math.round(oneHandedTutorialHandler.mDisplayBounds.height() * oneHandedTutorialHandler.mTutorialHeightRatio);
        oneHandedTutorialHandler.mTutorialAreaHeight = round;
        oneHandedTutorialHandler.mAlphaTransitionStart = round * 0.6f;
        BackgroundWindowManager backgroundWindowManager = oneHandedTutorialHandler.mBackgroundWindowManager;
        Objects.requireNonNull(backgroundWindowManager);
        if (displayLayout.mHeight > displayLayout.mWidth) {
            backgroundWindowManager.mDisplayBounds = new Rect(0, 0, displayLayout.mWidth, displayLayout.mHeight);
        } else {
            backgroundWindowManager.mDisplayBounds = new Rect(0, 0, displayLayout.mHeight, displayLayout.mWidth);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:5:0x0012, code lost:
        if (com.android.wm.shell.onehanded.OneHandedState.sCurrentState == 2) goto L_0x0014;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateOneHandedEnabled() {
        /*
            r3 = this;
            com.android.wm.shell.onehanded.OneHandedState r0 = r3.mState
            java.util.Objects.requireNonNull(r0)
            int r0 = com.android.wm.shell.onehanded.OneHandedState.sCurrentState
            r1 = 1
            if (r0 == r1) goto L_0x0014
            com.android.wm.shell.onehanded.OneHandedState r0 = r3.mState
            java.util.Objects.requireNonNull(r0)
            int r0 = com.android.wm.shell.onehanded.OneHandedState.sCurrentState
            r1 = 2
            if (r0 != r1) goto L_0x001f
        L_0x0014:
            com.android.wm.shell.common.ShellExecutor r0 = r3.mMainExecutor
            com.android.systemui.qs.tileimpl.QSTileImpl$$ExternalSyntheticLambda1 r1 = new com.android.systemui.qs.tileimpl.QSTileImpl$$ExternalSyntheticLambda1
            r2 = 4
            r1.<init>(r3, r2)
            r0.execute(r1)
        L_0x001f:
            boolean r0 = r3.isOneHandedEnabled()
            if (r0 == 0) goto L_0x0035
            boolean r0 = r3.isSwipeToNotificationEnabled()
            if (r0 != 0) goto L_0x0035
            com.android.wm.shell.onehanded.OneHandedState r0 = r3.mState
            java.util.Objects.requireNonNull(r0)
            int r0 = com.android.wm.shell.onehanded.OneHandedState.sCurrentState
            r3.notifyShortcutStateChanged(r0)
        L_0x0035:
            com.android.wm.shell.onehanded.OneHandedTouchHandler r0 = r3.mTouchHandler
            boolean r1 = r3.mIsOneHandedEnabled
            java.util.Objects.requireNonNull(r0)
            r0.mIsEnabled = r1
            r0.updateIsEnabled()
            boolean r0 = r3.mIsOneHandedEnabled
            if (r0 != 0) goto L_0x004b
            com.android.wm.shell.onehanded.OneHandedDisplayAreaOrganizer r3 = r3.mDisplayAreaOrganizer
            r3.unregisterOrganizer()
            return
        L_0x004b:
            com.android.wm.shell.onehanded.OneHandedDisplayAreaOrganizer r0 = r3.mDisplayAreaOrganizer
            android.util.ArrayMap r0 = r0.getDisplayAreaTokenMap()
            boolean r0 = r0.isEmpty()
            if (r0 == 0) goto L_0x005d
            com.android.wm.shell.onehanded.OneHandedDisplayAreaOrganizer r3 = r3.mDisplayAreaOrganizer
            r0 = 3
            r3.registerOrganizer(r0)
        L_0x005d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.onehanded.OneHandedController.updateOneHandedEnabled():void");
    }

    public final void updateSettings() {
        boolean isEmpty;
        boolean isEmpty2;
        OneHandedSettingsUtil oneHandedSettingsUtil = this.mOneHandedSettingsUtil;
        ContentResolver contentResolver = this.mContext.getContentResolver();
        int i = this.mUserId;
        Objects.requireNonNull(oneHandedSettingsUtil);
        this.mIsOneHandedEnabled = OneHandedSettingsUtil.getSettingsOneHandedModeEnabled(contentResolver, i);
        updateOneHandedEnabled();
        OneHandedTimeoutHandler oneHandedTimeoutHandler = this.mTimeoutHandler;
        OneHandedSettingsUtil oneHandedSettingsUtil2 = this.mOneHandedSettingsUtil;
        ContentResolver contentResolver2 = this.mContext.getContentResolver();
        int i2 = this.mUserId;
        Objects.requireNonNull(oneHandedSettingsUtil2);
        int intForUser = Settings.Secure.getIntForUser(contentResolver2, "one_handed_mode_timeout", 8, i2);
        Objects.requireNonNull(oneHandedTimeoutHandler);
        oneHandedTimeoutHandler.mTimeout = intForUser;
        oneHandedTimeoutHandler.mTimeoutMs = TimeUnit.SECONDS.toMillis(intForUser);
        oneHandedTimeoutHandler.resetTimer();
        OneHandedSettingsUtil oneHandedSettingsUtil3 = this.mOneHandedSettingsUtil;
        ContentResolver contentResolver3 = this.mContext.getContentResolver();
        int i3 = this.mUserId;
        Objects.requireNonNull(oneHandedSettingsUtil3);
        boolean z = true;
        if (Settings.Secure.getIntForUser(contentResolver3, "taps_app_to_exit", 1, i3) != 1) {
            z = false;
        }
        if (z) {
            this.mTaskStackListener.addListener(this.mTaskStackListenerCallback);
        } else {
            TaskStackListenerImpl taskStackListenerImpl = this.mTaskStackListener;
            AnonymousClass4 r1 = this.mTaskStackListenerCallback;
            Objects.requireNonNull(taskStackListenerImpl);
            synchronized (taskStackListenerImpl.mTaskStackListeners) {
                isEmpty = taskStackListenerImpl.mTaskStackListeners.isEmpty();
                taskStackListenerImpl.mTaskStackListeners.remove(r1);
                isEmpty2 = taskStackListenerImpl.mTaskStackListeners.isEmpty();
            }
            if (!isEmpty && isEmpty2) {
                try {
                    taskStackListenerImpl.mActivityTaskManager.unregisterTaskStackListener(taskStackListenerImpl);
                } catch (Exception e) {
                    Log.w("TaskStackListenerImpl", "Failed to call unregisterTaskStackListener", e);
                }
            }
        }
        this.mTaskChangeToExit = z;
        OneHandedSettingsUtil oneHandedSettingsUtil4 = this.mOneHandedSettingsUtil;
        ContentResolver contentResolver4 = this.mContext.getContentResolver();
        int i4 = this.mUserId;
        Objects.requireNonNull(oneHandedSettingsUtil4);
        this.mIsSwipeToNotificationEnabled = OneHandedSettingsUtil.getSettingsSwipeToNotificationEnabled(contentResolver4, i4);
        onShortcutEnabledChanged();
    }

    public void notifyShortcutStateChanged(int i) {
        int i2;
        if (isShortcutEnabled()) {
            OneHandedSettingsUtil oneHandedSettingsUtil = this.mOneHandedSettingsUtil;
            ContentResolver contentResolver = this.mContext.getContentResolver();
            if (i == 2) {
                i2 = 1;
            } else {
                i2 = 0;
            }
            int i3 = this.mUserId;
            Objects.requireNonNull(oneHandedSettingsUtil);
            Settings.Secure.putIntForUser(contentResolver, "one_handed_mode_activated", i2, i3);
        }
    }

    public void onActivatedActionChanged() {
        boolean z;
        if (!isShortcutEnabled()) {
            Slog.w("OneHandedController", "Shortcut not enabled, skip onActivatedActionChanged()");
            return;
        }
        boolean z2 = true;
        if (!isOneHandedEnabled()) {
            OneHandedSettingsUtil oneHandedSettingsUtil = this.mOneHandedSettingsUtil;
            ContentResolver contentResolver = this.mContext.getContentResolver();
            int i = this.mUserId;
            Objects.requireNonNull(oneHandedSettingsUtil);
            Slog.d("OneHandedController", "Auto enabled One-handed mode by shortcut trigger, success=" + Settings.Secure.putIntForUser(contentResolver, "one_handed_mode_enabled", 1, i));
        }
        if (isSwipeToNotificationEnabled()) {
            notifyExpandNotification();
            return;
        }
        Objects.requireNonNull(this.mState);
        if (OneHandedState.sCurrentState == 2) {
            z = true;
        } else {
            z = false;
        }
        OneHandedSettingsUtil oneHandedSettingsUtil2 = this.mOneHandedSettingsUtil;
        ContentResolver contentResolver2 = this.mContext.getContentResolver();
        int i2 = this.mUserId;
        Objects.requireNonNull(oneHandedSettingsUtil2);
        if (Settings.Secure.getIntForUser(contentResolver2, "one_handed_mode_activated", 0, i2) != 1) {
            z2 = false;
        }
        if (!(z ^ z2)) {
            return;
        }
        if (z2) {
            startOneHanded();
        } else {
            stopOneHanded();
        }
    }

    @Override // com.android.wm.shell.common.DisplayChangeController.OnDisplayChangingListener
    public final void onRotateDisplay(int i, int i2, int i3, WindowContainerTransaction windowContainerTransaction) {
        if (isInitialized()) {
            OneHandedSettingsUtil oneHandedSettingsUtil = this.mOneHandedSettingsUtil;
            ContentResolver contentResolver = this.mContext.getContentResolver();
            int i4 = this.mUserId;
            Objects.requireNonNull(oneHandedSettingsUtil);
            if (OneHandedSettingsUtil.getSettingsOneHandedModeEnabled(contentResolver, i4)) {
                OneHandedSettingsUtil oneHandedSettingsUtil2 = this.mOneHandedSettingsUtil;
                ContentResolver contentResolver2 = this.mContext.getContentResolver();
                int i5 = this.mUserId;
                Objects.requireNonNull(oneHandedSettingsUtil2);
                if (!OneHandedSettingsUtil.getSettingsSwipeToNotificationEnabled(contentResolver2, i5)) {
                    OneHandedDisplayAreaOrganizer oneHandedDisplayAreaOrganizer = this.mDisplayAreaOrganizer;
                    Context context = this.mContext;
                    Objects.requireNonNull(oneHandedDisplayAreaOrganizer);
                    DisplayLayout displayLayout = oneHandedDisplayAreaOrganizer.mDisplayLayout;
                    Objects.requireNonNull(displayLayout);
                    if (displayLayout.mRotation != i3) {
                        oneHandedDisplayAreaOrganizer.mDisplayLayout.rotateTo(context.getResources(), i3);
                        oneHandedDisplayAreaOrganizer.updateDisplayBounds();
                        oneHandedDisplayAreaOrganizer.finishOffset(0, 2);
                    }
                    this.mOneHandedUiEventLogger.writeEvent(4);
                }
            }
        }
    }

    public void startOneHanded() {
        boolean z;
        boolean z2;
        DisplayLayout displayLayout;
        if (isLockedDisabled() || this.mKeyguardShowing) {
            Slog.d("OneHandedController", "Temporary lock disabled");
            return;
        }
        OneHandedDisplayAreaOrganizer oneHandedDisplayAreaOrganizer = this.mDisplayAreaOrganizer;
        Objects.requireNonNull(oneHandedDisplayAreaOrganizer);
        if (!oneHandedDisplayAreaOrganizer.mIsReady) {
            this.mMainExecutor.executeDelayed(new DozeScreenState$$ExternalSyntheticLambda0(this, 7), 10L);
            return;
        }
        Objects.requireNonNull(this.mState);
        int i = OneHandedState.sCurrentState;
        if (i == 1 || i == 3) {
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            Objects.requireNonNull(this.mState);
            if (OneHandedState.sCurrentState == 2) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (!z2) {
                OneHandedDisplayAreaOrganizer oneHandedDisplayAreaOrganizer2 = this.mDisplayAreaOrganizer;
                Objects.requireNonNull(oneHandedDisplayAreaOrganizer2);
                DisplayLayout displayLayout2 = oneHandedDisplayAreaOrganizer2.mDisplayLayout;
                Objects.requireNonNull(displayLayout2);
                int i2 = displayLayout2.mRotation;
                if (i2 == 0 || i2 == 2) {
                    this.mState.setState(1);
                    OneHandedDisplayAreaOrganizer oneHandedDisplayAreaOrganizer3 = this.mDisplayAreaOrganizer;
                    Objects.requireNonNull(oneHandedDisplayAreaOrganizer3);
                    Objects.requireNonNull(oneHandedDisplayAreaOrganizer3.mDisplayLayout);
                    int round = Math.round(displayLayout.mHeight * this.mOffSetFraction);
                    OneHandedAccessibilityUtil oneHandedAccessibilityUtil = this.mOneHandedAccessibilityUtil;
                    Objects.requireNonNull(oneHandedAccessibilityUtil);
                    oneHandedAccessibilityUtil.announcementForScreenReader(oneHandedAccessibilityUtil.mStartOneHandedDescription);
                    this.mDisplayAreaOrganizer.scheduleOffset(round);
                    this.mTimeoutHandler.resetTimer();
                    this.mOneHandedUiEventLogger.writeEvent(0);
                    return;
                }
                Slog.w("OneHandedController", "One handed mode only support portrait mode");
            }
        }
    }

    @Override // com.android.wm.shell.common.RemoteCallable
    public final Context getContext() {
        return this.mContext;
    }

    @Override // com.android.wm.shell.common.RemoteCallable
    public final ShellExecutor getRemoteCallExecutor() {
        return this.mMainExecutor;
    }

    public boolean isLockedDisabled() {
        return this.mLockedDisabled;
    }

    public boolean isOneHandedEnabled() {
        return this.mIsOneHandedEnabled;
    }

    public boolean isShortcutEnabled() {
        return this.mIsShortcutEnabled;
    }

    public boolean isSwipeToNotificationEnabled() {
        return this.mIsSwipeToNotificationEnabled;
    }
}

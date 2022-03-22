package com.android.systemui.statusbar.lockscreen;

import android.app.PendingIntent;
import android.app.smartspace.SmartspaceConfig;
import android.app.smartspace.SmartspaceManager;
import android.app.smartspace.SmartspaceSession;
import android.app.smartspace.SmartspaceTarget;
import android.app.smartspace.SmartspaceTargetEvent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.UserInfo;
import android.os.UserHandle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.android.settingslib.Utils;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.util.concurrency.Execution;
import com.android.systemui.util.settings.SecureSettings;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import kotlin.collections.EmptyList;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: LockscreenSmartspaceController.kt */
/* loaded from: classes.dex */
public final class LockscreenSmartspaceController {
    public final ActivityStarter activityStarter;
    public final ConfigurationController configurationController;
    public final ContentResolver contentResolver;
    public final Context context;
    public final DeviceProvisionedController deviceProvisionedController;
    public final LockscreenSmartspaceController$deviceProvisionedListener$1 deviceProvisionedListener;
    public final Execution execution;
    public final FalsingManager falsingManager;
    public final FeatureFlags featureFlags;
    public UserHandle managedUserHandle;
    public final BcSmartspaceDataPlugin plugin;
    public final SecureSettings secureSettings;
    public SmartspaceSession session;
    public final LockscreenSmartspaceController$settingsObserver$1 settingsObserver;
    public boolean showSensitiveContentForCurrentUser;
    public boolean showSensitiveContentForManagedUser;
    public final SmartspaceManager smartspaceManager;
    public final StatusBarStateController statusBarStateController;
    public final Executor uiExecutor;
    public final UserTracker userTracker;
    public LinkedHashSet smartspaceViews = new LinkedHashSet();
    public LockscreenSmartspaceController$stateChangeListener$1 stateChangeListener = new View.OnAttachStateChangeListener() { // from class: com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$stateChangeListener$1
        @Override // android.view.View.OnAttachStateChangeListener
        public final void onViewAttachedToWindow(View view) {
            LockscreenSmartspaceController.this.smartspaceViews.add((BcSmartspaceDataPlugin.SmartspaceView) view);
            LockscreenSmartspaceController.this.connectSession();
            LockscreenSmartspaceController.access$updateTextColorFromWallpaper(LockscreenSmartspaceController.this);
            LockscreenSmartspaceController lockscreenSmartspaceController = LockscreenSmartspaceController.this;
            lockscreenSmartspaceController.statusBarStateListener.onDozeAmountChanged(0.0f, lockscreenSmartspaceController.statusBarStateController.getDozeAmount());
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public final void onViewDetachedFromWindow(View view) {
            LockscreenSmartspaceController.this.smartspaceViews.remove((BcSmartspaceDataPlugin.SmartspaceView) view);
            if (LockscreenSmartspaceController.this.smartspaceViews.isEmpty()) {
                LockscreenSmartspaceController lockscreenSmartspaceController = LockscreenSmartspaceController.this;
                Objects.requireNonNull(lockscreenSmartspaceController);
                if (lockscreenSmartspaceController.smartspaceViews.isEmpty()) {
                    lockscreenSmartspaceController.execution.assertIsMainThread();
                    SmartspaceSession smartspaceSession = lockscreenSmartspaceController.session;
                    if (smartspaceSession != null) {
                        smartspaceSession.removeOnTargetsAvailableListener(lockscreenSmartspaceController.sessionListener);
                        smartspaceSession.close();
                        lockscreenSmartspaceController.userTracker.removeCallback(lockscreenSmartspaceController.userTrackerCallback);
                        lockscreenSmartspaceController.contentResolver.unregisterContentObserver(lockscreenSmartspaceController.settingsObserver);
                        lockscreenSmartspaceController.configurationController.removeCallback(lockscreenSmartspaceController.configChangeListener);
                        lockscreenSmartspaceController.statusBarStateController.removeCallback(lockscreenSmartspaceController.statusBarStateListener);
                        lockscreenSmartspaceController.session = null;
                        BcSmartspaceDataPlugin bcSmartspaceDataPlugin = lockscreenSmartspaceController.plugin;
                        if (bcSmartspaceDataPlugin != null) {
                            bcSmartspaceDataPlugin.registerSmartspaceEventNotifier(null);
                        }
                        BcSmartspaceDataPlugin bcSmartspaceDataPlugin2 = lockscreenSmartspaceController.plugin;
                        if (bcSmartspaceDataPlugin2 != null) {
                            bcSmartspaceDataPlugin2.onTargetsAvailable(EmptyList.INSTANCE);
                        }
                        Log.d("LockscreenSmartspaceController", "Ending smartspace session for lockscreen");
                    }
                }
            }
        }
    };
    public final LockscreenSmartspaceController$sessionListener$1 sessionListener = new SmartspaceSession.OnTargetsAvailableListener() { // from class: com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$sessionListener$1
        public final void onTargetsAvailable(List<SmartspaceTarget> list) {
            LockscreenSmartspaceController.this.execution.assertIsMainThread();
            LockscreenSmartspaceController lockscreenSmartspaceController = LockscreenSmartspaceController.this;
            ArrayList arrayList = new ArrayList();
            for (Object obj : list) {
                SmartspaceTarget smartspaceTarget = (SmartspaceTarget) obj;
                Objects.requireNonNull(lockscreenSmartspaceController);
                UserHandle userHandle = smartspaceTarget.getUserHandle();
                boolean z = true;
                if (!Intrinsics.areEqual(userHandle, lockscreenSmartspaceController.userTracker.getUserHandle()) ? !Intrinsics.areEqual(userHandle, lockscreenSmartspaceController.managedUserHandle) || lockscreenSmartspaceController.userTracker.getUserHandle().getIdentifier() != 0 || (smartspaceTarget.isSensitive() && !lockscreenSmartspaceController.showSensitiveContentForManagedUser) : smartspaceTarget.isSensitive() && !lockscreenSmartspaceController.showSensitiveContentForCurrentUser) {
                    z = false;
                }
                if (z) {
                    arrayList.add(obj);
                }
            }
            BcSmartspaceDataPlugin bcSmartspaceDataPlugin = LockscreenSmartspaceController.this.plugin;
            if (bcSmartspaceDataPlugin != null) {
                bcSmartspaceDataPlugin.onTargetsAvailable(arrayList);
            }
        }
    };
    public final LockscreenSmartspaceController$userTrackerCallback$1 userTrackerCallback = new UserTracker.Callback() { // from class: com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$userTrackerCallback$1
        @Override // com.android.systemui.settings.UserTracker.Callback
        public final void onUserChanged(int i) {
            LockscreenSmartspaceController.this.execution.assertIsMainThread();
            LockscreenSmartspaceController.this.reloadSmartspace();
        }
    };
    public final LockscreenSmartspaceController$configChangeListener$1 configChangeListener = new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$configChangeListener$1
        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public final void onThemeChanged() {
            LockscreenSmartspaceController.this.execution.assertIsMainThread();
            LockscreenSmartspaceController.access$updateTextColorFromWallpaper(LockscreenSmartspaceController.this);
        }
    };
    public final LockscreenSmartspaceController$statusBarStateListener$1 statusBarStateListener = new LockscreenSmartspaceController$statusBarStateListener$1(this);

    public final View buildAndConnectView(ViewGroup viewGroup) {
        View view;
        this.execution.assertIsMainThread();
        if (isEnabled()) {
            BcSmartspaceDataPlugin bcSmartspaceDataPlugin = this.plugin;
            if (bcSmartspaceDataPlugin == null) {
                view = null;
            } else {
                BcSmartspaceDataPlugin.SmartspaceView view2 = bcSmartspaceDataPlugin.getView(viewGroup);
                view2.registerDataProvider(this.plugin);
                view2.setIntentStarter(new BcSmartspaceDataPlugin.IntentStarter() { // from class: com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$buildView$1
                    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.IntentStarter
                    public final void startIntent(View view3, Intent intent, boolean z) {
                        LockscreenSmartspaceController.this.activityStarter.startActivity(intent, true, (ActivityLaunchAnimator.Controller) null, z);
                    }

                    @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.IntentStarter
                    public final void startPendingIntent(PendingIntent pendingIntent, boolean z) {
                        if (z) {
                            pendingIntent.send();
                        } else {
                            LockscreenSmartspaceController.this.activityStarter.startPendingIntentDismissingKeyguard(pendingIntent);
                        }
                    }
                });
                view2.setFalsingManager(this.falsingManager);
                view = (View) view2;
                view.addOnAttachStateChangeListener(this.stateChangeListener);
            }
            connectSession();
            return view;
        }
        throw new RuntimeException("Cannot build view when not enabled");
    }

    public final void connectSession() {
        if (this.plugin != null && this.session == null && !this.smartspaceViews.isEmpty() && this.deviceProvisionedController.isDeviceProvisioned() && this.deviceProvisionedController.isCurrentUserSetup()) {
            SmartspaceSession createSmartspaceSession = this.smartspaceManager.createSmartspaceSession(new SmartspaceConfig.Builder(this.context, "lockscreen").build());
            Log.d("LockscreenSmartspaceController", "Starting smartspace session for lockscreen");
            createSmartspaceSession.addOnTargetsAvailableListener(this.uiExecutor, this.sessionListener);
            this.session = createSmartspaceSession;
            this.deviceProvisionedController.removeCallback(this.deviceProvisionedListener);
            this.userTracker.addCallback(this.userTrackerCallback, this.uiExecutor);
            this.contentResolver.registerContentObserver(this.secureSettings.getUriFor("lock_screen_allow_private_notifications"), true, this.settingsObserver, -1);
            this.configurationController.addCallback(this.configChangeListener);
            this.statusBarStateController.addCallback(this.statusBarStateListener);
            this.plugin.registerSmartspaceEventNotifier(new BcSmartspaceDataPlugin.SmartspaceEventNotifier() { // from class: com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$connectSession$1
                @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.SmartspaceEventNotifier
                public final void notifySmartspaceEvent(SmartspaceTargetEvent smartspaceTargetEvent) {
                    SmartspaceSession smartspaceSession = LockscreenSmartspaceController.this.session;
                    if (smartspaceSession != null) {
                        smartspaceSession.notifySmartspaceEvent(smartspaceTargetEvent);
                    }
                }
            });
            reloadSmartspace();
        }
    }

    public final boolean isEnabled() {
        this.execution.assertIsMainThread();
        if (!this.featureFlags.isEnabled(Flags.SMARTSPACE) || this.plugin == null) {
            return false;
        }
        return true;
    }

    public final void reloadSmartspace() {
        boolean z;
        Integer num;
        UserHandle userHandle;
        boolean z2 = false;
        if (this.secureSettings.getIntForUser("lock_screen_allow_private_notifications", 0, this.userTracker.getUserId()) == 1) {
            z = true;
        } else {
            z = false;
        }
        this.showSensitiveContentForCurrentUser = z;
        Iterator<UserInfo> it = this.userTracker.getUserProfiles().iterator();
        while (true) {
            num = null;
            if (!it.hasNext()) {
                userHandle = null;
                break;
            }
            UserInfo next = it.next();
            if (next.isManagedProfile()) {
                userHandle = next.getUserHandle();
                break;
            }
        }
        this.managedUserHandle = userHandle;
        if (userHandle != null) {
            num = Integer.valueOf(userHandle.getIdentifier());
        }
        if (num != null) {
            if (this.secureSettings.getIntForUser("lock_screen_allow_private_notifications", 0, num.intValue()) == 1) {
                z2 = true;
            }
            this.showSensitiveContentForManagedUser = z2;
        }
        SmartspaceSession smartspaceSession = this.session;
        if (smartspaceSession != null) {
            smartspaceSession.requestSmartspaceUpdate();
        }
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [java.lang.Object, com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$deviceProvisionedListener$1] */
    /* JADX WARN: Type inference failed for: r1v5, types: [com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$stateChangeListener$1] */
    /* JADX WARN: Type inference failed for: r1v7, types: [com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$userTrackerCallback$1] */
    /* JADX WARN: Type inference failed for: r1v8, types: [com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$settingsObserver$1] */
    /* JADX WARN: Type inference failed for: r1v9, types: [com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$configChangeListener$1] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public LockscreenSmartspaceController(android.content.Context r1, com.android.systemui.flags.FeatureFlags r2, android.app.smartspace.SmartspaceManager r3, com.android.systemui.plugins.ActivityStarter r4, com.android.systemui.plugins.FalsingManager r5, com.android.systemui.util.settings.SecureSettings r6, com.android.systemui.settings.UserTracker r7, android.content.ContentResolver r8, com.android.systemui.statusbar.policy.ConfigurationController r9, com.android.systemui.plugins.statusbar.StatusBarStateController r10, com.android.systemui.statusbar.policy.DeviceProvisionedController r11, com.android.systemui.util.concurrency.Execution r12, java.util.concurrent.Executor r13, final android.os.Handler r14, java.util.Optional<com.android.systemui.plugins.BcSmartspaceDataPlugin> r15) {
        /*
            r0 = this;
            r0.<init>()
            r0.context = r1
            r0.featureFlags = r2
            r0.smartspaceManager = r3
            r0.activityStarter = r4
            r0.falsingManager = r5
            r0.secureSettings = r6
            r0.userTracker = r7
            r0.contentResolver = r8
            r0.configurationController = r9
            r0.statusBarStateController = r10
            r0.deviceProvisionedController = r11
            r0.execution = r12
            r0.uiExecutor = r13
            r1 = 0
            java.lang.Object r1 = r15.orElse(r1)
            com.android.systemui.plugins.BcSmartspaceDataPlugin r1 = (com.android.systemui.plugins.BcSmartspaceDataPlugin) r1
            r0.plugin = r1
            java.util.LinkedHashSet r1 = new java.util.LinkedHashSet
            r1.<init>()
            r0.smartspaceViews = r1
            com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$stateChangeListener$1 r1 = new com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$stateChangeListener$1
            r1.<init>()
            r0.stateChangeListener = r1
            com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$sessionListener$1 r1 = new com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$sessionListener$1
            r1.<init>()
            r0.sessionListener = r1
            com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$userTrackerCallback$1 r1 = new com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$userTrackerCallback$1
            r1.<init>()
            r0.userTrackerCallback = r1
            com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$settingsObserver$1 r1 = new com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$settingsObserver$1
            r1.<init>(r14)
            r0.settingsObserver = r1
            com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$configChangeListener$1 r1 = new com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$configChangeListener$1
            r1.<init>()
            r0.configChangeListener = r1
            com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$statusBarStateListener$1 r1 = new com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$statusBarStateListener$1
            r1.<init>(r0)
            r0.statusBarStateListener = r1
            com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$deviceProvisionedListener$1 r1 = new com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$deviceProvisionedListener$1
            r1.<init>()
            r0.deviceProvisionedListener = r1
            r11.addCallback(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController.<init>(android.content.Context, com.android.systemui.flags.FeatureFlags, android.app.smartspace.SmartspaceManager, com.android.systemui.plugins.ActivityStarter, com.android.systemui.plugins.FalsingManager, com.android.systemui.util.settings.SecureSettings, com.android.systemui.settings.UserTracker, android.content.ContentResolver, com.android.systemui.statusbar.policy.ConfigurationController, com.android.systemui.plugins.statusbar.StatusBarStateController, com.android.systemui.statusbar.policy.DeviceProvisionedController, com.android.systemui.util.concurrency.Execution, java.util.concurrent.Executor, android.os.Handler, java.util.Optional):void");
    }

    public static final void access$updateTextColorFromWallpaper(LockscreenSmartspaceController lockscreenSmartspaceController) {
        Objects.requireNonNull(lockscreenSmartspaceController);
        int colorAttrDefaultColor = Utils.getColorAttrDefaultColor(lockscreenSmartspaceController.context, 2130970103);
        for (BcSmartspaceDataPlugin.SmartspaceView smartspaceView : lockscreenSmartspaceController.smartspaceViews) {
            smartspaceView.setPrimaryTextColor(colorAttrDefaultColor);
        }
    }
}

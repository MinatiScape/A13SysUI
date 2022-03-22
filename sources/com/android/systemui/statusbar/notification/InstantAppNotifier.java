package com.android.systemui.statusbar.notification;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.AppGlobals;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.SynchronousUserSwitchObserver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageManager;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.UserHandle;
import android.service.notification.StatusBarNotification;
import android.util.ArraySet;
import android.util.Pair;
import com.android.systemui.CoreStartable;
import com.android.systemui.Dependency;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.wmshell.WMShell$$ExternalSyntheticLambda2;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda18;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class InstantAppNotifier extends CoreStartable implements CommandQueue.Callbacks, KeyguardStateController.Callback {
    public final CommandQueue mCommandQueue;
    public boolean mDockedStackExists;
    public KeyguardStateController mKeyguardStateController;
    public final Optional<LegacySplitScreen> mSplitScreenOptional;
    public final Executor mUiBgExecutor;
    public final Handler mHandler = new Handler();
    public final ArraySet<Pair<String, Integer>> mCurrentNotifs = new ArraySet<>();
    public final AnonymousClass1 mUserSwitchListener = new AnonymousClass1();

    /* renamed from: com.android.systemui.statusbar.notification.InstantAppNotifier$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends SynchronousUserSwitchObserver {
        public static final /* synthetic */ int $r8$clinit = 0;

        public final void onUserSwitching(int i) throws RemoteException {
        }

        public AnonymousClass1() {
        }

        public final void onUserSwitchComplete(int i) throws RemoteException {
            InstantAppNotifier.this.mHandler.post(new BubbleStackView$$ExternalSyntheticLambda18(this, 4));
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void appTransitionStarting(int i, long j, long j2, boolean z) {
        if (this.mContext.getDisplayId() == i) {
            updateForegroundInstantApps();
        }
    }

    public final void checkAndPostForStack(ActivityTaskManager.RootTaskInfo rootTaskInfo, ArraySet<Pair<String, Integer>> arraySet, NotificationManager notificationManager, IPackageManager iPackageManager) {
        ApplicationInfo applicationInfo;
        if (rootTaskInfo != null) {
            try {
                ComponentName componentName = rootTaskInfo.topActivity;
                if (componentName != null) {
                    String packageName = componentName.getPackageName();
                    if (!arraySet.remove(new Pair(packageName, Integer.valueOf(rootTaskInfo.userId))) && (applicationInfo = iPackageManager.getApplicationInfo(packageName, 8192L, rootTaskInfo.userId)) != null && applicationInfo.isInstantApp()) {
                        int i = rootTaskInfo.userId;
                        int[] iArr = rootTaskInfo.childTaskIds;
                        postInstantAppNotif(packageName, i, applicationInfo, notificationManager, iArr[iArr.length - 1]);
                    }
                }
            } catch (RemoteException e) {
                e.rethrowFromSystemServer();
            }
        }
    }

    public final void postInstantAppNotif(String str, int i, ApplicationInfo applicationInfo, NotificationManager notificationManager, int i2) {
        int i3;
        Notification.Action action;
        PendingIntent pendingIntent;
        String str2;
        Intent intent;
        PendingIntent pendingIntent2;
        char c;
        Notification.Builder builder;
        ComponentName componentName;
        Bundle bundle = new Bundle();
        bundle.putString("android.substName", this.mContext.getString(2131952466));
        this.mCurrentNotifs.add(new Pair<>(str, Integer.valueOf(i)));
        String string = this.mContext.getString(2131952467);
        boolean z = !string.isEmpty();
        Context context = this.mContext;
        if (z) {
            i3 = 2131952469;
        } else {
            i3 = 2131952468;
        }
        String string2 = context.getString(i3);
        UserHandle of = UserHandle.of(i);
        Notification.Action build = new Notification.Action.Builder((Icon) null, this.mContext.getString(2131951890), PendingIntent.getActivityAsUser(this.mContext, 0, new Intent("android.settings.APPLICATION_DETAILS_SETTINGS").setData(Uri.fromParts("package", str, null)), 67108864, null, of)).build();
        if (z) {
            str2 = "android.intent.action.VIEW";
            action = build;
            pendingIntent = PendingIntent.getActivityAsUser(this.mContext, 0, new Intent("android.intent.action.VIEW").setData(Uri.parse(string)), 67108864, null, of);
        } else {
            str2 = "android.intent.action.VIEW";
            action = build;
            pendingIntent = null;
        }
        List recentTasks = ActivityTaskManager.getInstance().getRecentTasks(5, 0, i);
        int i4 = 0;
        while (true) {
            if (i4 >= recentTasks.size()) {
                intent = null;
                break;
            } else if (((ActivityManager.RecentTaskInfo) recentTasks.get(i4)).id == i2) {
                intent = ((ActivityManager.RecentTaskInfo) recentTasks.get(i4)).baseIntent;
                break;
            } else {
                i4++;
            }
        }
        Notification.Builder builder2 = new Notification.Builder(this.mContext, "GEN");
        if (intent == null || !intent.isWebIntent()) {
            builder = builder2;
            c = 0;
            pendingIntent2 = pendingIntent;
        } else {
            intent.setComponent(null).setPackage(null).addFlags(512).addFlags(268435456);
            c = 0;
            pendingIntent2 = pendingIntent;
            PendingIntent activityAsUser = PendingIntent.getActivityAsUser(this.mContext, 0, intent, 67108864, null, of);
            try {
                componentName = AppGlobals.getPackageManager().getInstantAppInstallerComponent();
            } catch (RemoteException e) {
                e.rethrowFromSystemServer();
                componentName = null;
            }
            Intent addCategory = new Intent().setComponent(componentName).setAction(str2).addCategory("android.intent.category.BROWSABLE");
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("unique:");
            m.append(System.currentTimeMillis());
            builder = builder2;
            builder.addAction(new Notification.Action.Builder((Icon) null, this.mContext.getString(2131952408), PendingIntent.getActivityAsUser(this.mContext, 0, addCategory.addCategory(m.toString()).putExtra("android.intent.extra.PACKAGE_NAME", applicationInfo.packageName).putExtra("android.intent.extra.VERSION_CODE", applicationInfo.versionCode & Integer.MAX_VALUE).putExtra("android.intent.extra.LONG_VERSION_CODE", applicationInfo.longVersionCode).putExtra("android.intent.extra.INSTANT_APP_FAILURE", activityAsUser), 67108864, null, of)).build());
        }
        Notification.Builder color = builder.addExtras(bundle).addAction(action).setContentIntent(pendingIntent2).setColor(this.mContext.getColor(2131099886));
        Context context2 = this.mContext;
        Object[] objArr = new Object[1];
        objArr[c] = applicationInfo.loadLabel(context2.getPackageManager());
        notificationManager.notifyAsUser(str, 7, color.setContentTitle(context2.getString(2131952470, objArr)).setLargeIcon(Icon.createWithResource(str, applicationInfo.icon)).setSmallIcon(Icon.createWithResource(this.mContext.getPackageName(), 2131232347)).setContentText(string2).setStyle(new Notification.BigTextStyle().bigText(string2)).setOngoing(true).build(), new UserHandle(i));
    }

    @Override // com.android.systemui.CoreStartable
    public final void start() {
        StatusBarNotification[] activeNotifications;
        this.mKeyguardStateController = (KeyguardStateController) Dependency.get(KeyguardStateController.class);
        try {
            ActivityManager.getService().registerUserSwitchObserver(this.mUserSwitchListener, "InstantAppNotifier");
        } catch (RemoteException unused) {
        }
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) this);
        this.mKeyguardStateController.addCallback(this);
        this.mSplitScreenOptional.ifPresent(new WMShell$$ExternalSyntheticLambda2(this, 2));
        NotificationManager notificationManager = (NotificationManager) this.mContext.getSystemService(NotificationManager.class);
        for (StatusBarNotification statusBarNotification : notificationManager.getActiveNotifications()) {
            if (statusBarNotification.getId() == 7) {
                notificationManager.cancel(statusBarNotification.getTag(), statusBarNotification.getId());
            }
        }
    }

    public final void updateForegroundInstantApps() {
        final NotificationManager notificationManager = (NotificationManager) this.mContext.getSystemService(NotificationManager.class);
        final IPackageManager packageManager = AppGlobals.getPackageManager();
        this.mUiBgExecutor.execute(new Runnable() { // from class: com.android.systemui.statusbar.notification.InstantAppNotifier$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                int windowingMode;
                InstantAppNotifier instantAppNotifier = InstantAppNotifier.this;
                NotificationManager notificationManager2 = notificationManager;
                IPackageManager iPackageManager = packageManager;
                Objects.requireNonNull(instantAppNotifier);
                ArraySet<Pair<String, Integer>> arraySet = new ArraySet<>(instantAppNotifier.mCurrentNotifs);
                try {
                    ActivityTaskManager.RootTaskInfo focusedRootTaskInfo = ActivityTaskManager.getService().getFocusedRootTaskInfo();
                    if (focusedRootTaskInfo != null && ((windowingMode = focusedRootTaskInfo.configuration.windowConfiguration.getWindowingMode()) == 1 || windowingMode == 4 || windowingMode == 5)) {
                        instantAppNotifier.checkAndPostForStack(focusedRootTaskInfo, arraySet, notificationManager2, iPackageManager);
                    }
                    if (instantAppNotifier.mDockedStackExists) {
                        try {
                            instantAppNotifier.checkAndPostForStack(ActivityTaskManager.getService().getRootTaskInfo(3, 0), arraySet, notificationManager2, iPackageManager);
                        } catch (RemoteException e) {
                            e.rethrowFromSystemServer();
                        }
                    }
                } catch (RemoteException e2) {
                    e2.rethrowFromSystemServer();
                }
                arraySet.forEach(new InstantAppNotifier$$ExternalSyntheticLambda1(instantAppNotifier, notificationManager2, 0));
            }
        });
    }

    public InstantAppNotifier(Context context, CommandQueue commandQueue, Executor executor, Optional<LegacySplitScreen> optional) {
        super(context);
        this.mSplitScreenOptional = optional;
        this.mCommandQueue = commandQueue;
        this.mUiBgExecutor = executor;
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
    public final void onKeyguardShowingChanged() {
        updateForegroundInstantApps();
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void preloadRecentApps() {
        updateForegroundInstantApps();
    }
}

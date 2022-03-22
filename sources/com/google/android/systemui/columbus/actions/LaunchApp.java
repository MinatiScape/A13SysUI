package com.google.android.systemui.columbus.actions;

import android.app.ActivityManager;
import android.app.IActivityManager;
import android.app.PendingIntent;
import android.app.SynchronousUserSwitchObserver;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.LauncherActivityInfo;
import android.content.pm.LauncherApps;
import android.content.pm.ShortcutInfo;
import android.content.res.ColorStateList;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Handler;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.DeviceConfig;
import android.provider.Settings;
import android.util.Log;
import com.android.internal.logging.UiEventLogger;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.google.android.systemui.columbus.ColumbusEvent;
import com.google.android.systemui.columbus.ColumbusSettings;
import com.google.android.systemui.columbus.gates.Gate;
import com.google.android.systemui.columbus.gates.KeyguardVisibility;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import kotlin.collections.CollectionsKt__IteratorsJVMKt;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__StringsKt;
/* compiled from: LaunchApp.kt */
/* loaded from: classes.dex */
public final class LaunchApp extends UserAction {
    public final ActivityStarter activityStarter;
    public final Set<String> allowCertList;
    public final Set<String> allowPackageList;
    public final Executor bgExecutor;
    public final Handler bgHandler;
    public ComponentName currentApp;
    public String currentShortcut;
    public final LaunchApp$deviceConfigPropertiesChangedListener$1 deviceConfigPropertiesChangedListener;
    public final KeyguardUpdateMonitor keyguardUpdateMonitor;
    public final LaunchApp$keyguardUpdateMonitorCallback$1 keyguardUpdateMonitorCallback;
    public final KeyguardVisibility keyguardVisibility;
    public final LauncherApps launcherApps;
    public final Handler mainHandler;
    public final StatusBarKeyguardViewManager statusBarKeyguardViewManager;
    public final UiEventLogger uiEventLogger;
    public final UserManager userManager;
    public final UserTracker userTracker;
    public final String tag = "Columbus/LaunchApp";
    public final LaunchApp$settingsListener$1 settingsListener = new ColumbusSettings.ColumbusSettingsChangeListener() { // from class: com.google.android.systemui.columbus.actions.LaunchApp$settingsListener$1
        @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
        public final void onAlertSilenceEnabledChange(boolean z) {
        }

        @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
        public final void onColumbusEnabledChange(boolean z) {
        }

        @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
        public final void onLowSensitivityChange(boolean z) {
        }

        @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
        public final void onSelectedActionChange(String str) {
        }

        @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
        public final void onUseApSensorChange() {
        }

        @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
        public final void onSelectedAppChange(String str) {
            LaunchApp.this.currentApp = ComponentName.unflattenFromString(str);
            LaunchApp.this.updateAvailable();
        }

        @Override // com.google.android.systemui.columbus.ColumbusSettings.ColumbusSettingsChangeListener
        public final void onSelectedAppShortcutChange(String str) {
            LaunchApp launchApp = LaunchApp.this;
            launchApp.currentShortcut = str;
            launchApp.updateAvailable();
        }
    };
    public final LaunchApp$broadcastReceiver$1 broadcastReceiver = new BroadcastReceiver() { // from class: com.google.android.systemui.columbus.actions.LaunchApp$broadcastReceiver$1
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            LaunchApp.this.updateAvailableAppsAndShortcutsAsync();
        }
    };
    public final LaunchApp$gateListener$1 gateListener = new Gate.Listener() { // from class: com.google.android.systemui.columbus.actions.LaunchApp$gateListener$1
        @Override // com.google.android.systemui.columbus.gates.Gate.Listener
        public final void onGateChanged(Gate gate) {
            if (!gate.isBlocking()) {
                LaunchApp.this.updateAvailableAppsAndShortcutsAsync();
            }
        }
    };
    public final LaunchApp$onDismissKeyguardAction$1 onDismissKeyguardAction = new ActivityStarter.OnDismissAction() { // from class: com.google.android.systemui.columbus.actions.LaunchApp$onDismissKeyguardAction$1
        @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
        public final boolean onDismiss() {
            String str;
            ShortcutInfo shortcutInfo;
            String str2;
            LaunchApp launchApp = LaunchApp.this;
            Objects.requireNonNull(launchApp);
            String str3 = null;
            if (launchApp.usingShortcut()) {
                LinkedHashMap linkedHashMap = launchApp.availableShortcuts;
                ComponentName componentName = launchApp.currentApp;
                if (componentName == null) {
                    str = null;
                } else {
                    str = componentName.getPackageName();
                }
                Map map = (Map) linkedHashMap.get(str);
                if (!(map == null || (shortcutInfo = (ShortcutInfo) map.get(launchApp.currentShortcut)) == null)) {
                    UiEventLogger uiEventLogger = launchApp.uiEventLogger;
                    ColumbusEvent columbusEvent = ColumbusEvent.COLUMBUS_INVOKED_LAUNCH_SHORTCUT;
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("");
                    ComponentName componentName2 = launchApp.currentApp;
                    if (componentName2 == null) {
                        str2 = null;
                    } else {
                        str2 = componentName2.getPackageName();
                    }
                    m.append((Object) str2);
                    m.append('/');
                    m.append((Object) shortcutInfo.getId());
                    uiEventLogger.log(columbusEvent, 0, m.toString());
                    launchApp.launcherApps.startShortcut(shortcutInfo, null, null);
                }
            } else {
                PendingIntent pendingIntent = (PendingIntent) launchApp.availableApps.get(launchApp.currentApp);
                if (pendingIntent != null) {
                    UiEventLogger uiEventLogger2 = launchApp.uiEventLogger;
                    ColumbusEvent columbusEvent2 = ColumbusEvent.COLUMBUS_INVOKED_LAUNCH_APP;
                    ComponentName componentName3 = launchApp.currentApp;
                    if (componentName3 != null) {
                        str3 = componentName3.flattenToString();
                    }
                    uiEventLogger2.log(columbusEvent2, 0, str3);
                    pendingIntent.send();
                }
            }
            return false;
        }
    };
    public final MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
    public final LinkedHashSet denyPackageList = new LinkedHashSet();
    public final LinkedHashMap availableApps = new LinkedHashMap();
    public final LinkedHashMap availableShortcuts = new LinkedHashMap();

    /* JADX WARN: Type inference failed for: r4v1, types: [com.google.android.systemui.columbus.actions.LaunchApp$settingsListener$1] */
    /* JADX WARN: Type inference failed for: r5v1, types: [com.google.android.systemui.columbus.actions.LaunchApp$broadcastReceiver$1] */
    /* JADX WARN: Type inference failed for: r5v2, types: [com.google.android.systemui.columbus.actions.LaunchApp$gateListener$1] */
    /* JADX WARN: Type inference failed for: r5v3, types: [com.google.android.systemui.columbus.actions.LaunchApp$keyguardUpdateMonitorCallback$1] */
    /* JADX WARN: Type inference failed for: r7v1, types: [com.google.android.systemui.columbus.actions.LaunchApp$onDismissKeyguardAction$1] */
    public LaunchApp(final Context context, LauncherApps launcherApps, ActivityStarter activityStarter, StatusBarKeyguardViewManager statusBarKeyguardViewManager, IActivityManager iActivityManager, UserManager userManager, ColumbusSettings columbusSettings, KeyguardVisibility keyguardVisibility, KeyguardUpdateMonitor keyguardUpdateMonitor, Handler handler, Handler handler2, Executor executor, UiEventLogger uiEventLogger, UserTracker userTracker) {
        super(context, null);
        this.launcherApps = launcherApps;
        this.activityStarter = activityStarter;
        this.statusBarKeyguardViewManager = statusBarKeyguardViewManager;
        this.userManager = userManager;
        this.keyguardVisibility = keyguardVisibility;
        this.keyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mainHandler = handler;
        this.bgHandler = handler2;
        this.bgExecutor = executor;
        this.uiEventLogger = uiEventLogger;
        this.userTracker = userTracker;
        SynchronousUserSwitchObserver launchApp$userSwitchCallback$1 = new SynchronousUserSwitchObserver() { // from class: com.google.android.systemui.columbus.actions.LaunchApp$userSwitchCallback$1
            public final void onUserSwitching(int i) throws RemoteException {
                LaunchApp.this.updateAvailableAppsAndShortcutsAsync();
            }
        };
        this.keyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() { // from class: com.google.android.systemui.columbus.actions.LaunchApp$keyguardUpdateMonitorCallback$1
            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public final void onKeyguardBouncerChanged(boolean z) {
                if (z) {
                    LaunchApp.this.keyguardUpdateMonitor.removeCallback(this);
                    final LaunchApp launchApp = LaunchApp.this;
                    Handler handler3 = launchApp.mainHandler;
                    final Context context2 = context;
                    handler3.post(new Runnable() { // from class: com.google.android.systemui.columbus.actions.LaunchApp$keyguardUpdateMonitorCallback$1$onKeyguardBouncerChanged$1
                        @Override // java.lang.Runnable
                        public final void run() {
                            LaunchApp.this.statusBarKeyguardViewManager.showBouncerMessage(context2.getString(2131952125), ColorStateList.valueOf(-1));
                        }
                    });
                }
            }
        };
        DeviceConfig.OnPropertiesChangedListener launchApp$deviceConfigPropertiesChangedListener$1 = new DeviceConfig.OnPropertiesChangedListener() { // from class: com.google.android.systemui.columbus.actions.LaunchApp$deviceConfigPropertiesChangedListener$1
            public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                if (properties.getKeyset().contains("systemui_google_columbus_secure_deny_list")) {
                    LaunchApp.this.updateDenyList(properties.getString("systemui_google_columbus_secure_deny_list", (String) null));
                }
            }
        };
        this.deviceConfigPropertiesChangedListener = launchApp$deviceConfigPropertiesChangedListener$1;
        String[] stringArray = context.getResources().getStringArray(2130903087);
        this.allowPackageList = SetsKt__SetsKt.setOf(Arrays.copyOf(stringArray, stringArray.length));
        String[] stringArray2 = context.getResources().getStringArray(2130903086);
        this.allowCertList = SetsKt__SetsKt.setOf(Arrays.copyOf(stringArray2, stringArray2.length));
        String str = "";
        this.currentShortcut = str;
        DeviceConfig.addOnPropertiesChangedListener("systemui", executor, launchApp$deviceConfigPropertiesChangedListener$1);
        updateDenyList(DeviceConfig.getString("systemui", "systemui_google_columbus_secure_deny_list", (String) null));
        try {
            iActivityManager.registerUserSwitchObserver(launchApp$userSwitchCallback$1, "Columbus/LaunchApp");
        } catch (RemoteException e) {
            Log.e("Columbus/LaunchApp", "Failed to register user switch observer", e);
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        intentFilter.addDataScheme("package");
        context.registerReceiver(this.broadcastReceiver, intentFilter);
        context.registerReceiver(this.broadcastReceiver, new IntentFilter("android.intent.action.BOOT_COMPLETED"));
        updateAvailableAppsAndShortcutsAsync();
        columbusSettings.registerColumbusSettingsChangeListener(this.settingsListener);
        this.currentApp = ComponentName.unflattenFromString(columbusSettings.selectedApp());
        String stringForUser = Settings.Secure.getStringForUser(columbusSettings.contentResolver, "columbus_launch_app_shortcut", columbusSettings.userTracker.getUserId());
        this.currentShortcut = stringForUser != null ? stringForUser : str;
        this.keyguardVisibility.registerListener(this.gateListener);
        updateAvailable();
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0025  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00b0  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00df  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x0136  */
    /* JADX WARN: Removed duplicated region for block: B:70:? A[RETURN, SYNTHETIC] */
    @Override // com.google.android.systemui.columbus.actions.Action
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onTrigger(com.google.android.systemui.columbus.sensors.GestureSensor.DetectionProperties r20) {
        /*
            Method dump skipped, instructions count: 363
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.columbus.actions.LaunchApp.onTrigger(com.google.android.systemui.columbus.sensors.GestureSensor$DetectionProperties):void");
    }

    public final void updateAvailableAppsAndShortcutsAsync() {
        this.bgHandler.post(new Runnable() { // from class: com.google.android.systemui.columbus.actions.LaunchApp$updateAvailableAppsAndShortcutsAsync$1
            @Override // java.lang.Runnable
            public final void run() {
                List<ShortcutInfo> list;
                int currentUser = ActivityManager.getCurrentUser();
                if (LaunchApp.this.userManager.isUserUnlocked(currentUser)) {
                    LaunchApp.this.availableApps.clear();
                    LaunchApp.this.availableShortcuts.clear();
                    List<LauncherActivityInfo> activityList = LaunchApp.this.launcherApps.getActivityList(null, UserHandle.of(currentUser));
                    LaunchApp launchApp = LaunchApp.this;
                    Objects.requireNonNull(launchApp);
                    LauncherApps.ShortcutQuery shortcutQuery = new LauncherApps.ShortcutQuery();
                    shortcutQuery.setQueryFlags(9);
                    try {
                        list = launchApp.launcherApps.getShortcuts(shortcutQuery, UserHandle.of(currentUser));
                    } catch (Exception e) {
                        if ((e instanceof SecurityException) || (e instanceof IllegalStateException)) {
                            Log.e("Columbus/LaunchApp", "Failed to query for shortcuts", e);
                            list = null;
                        } else {
                            throw e;
                        }
                    }
                    for (LauncherActivityInfo launcherActivityInfo : activityList) {
                        try {
                            PendingIntent mainActivityLaunchIntent = LaunchApp.this.launcherApps.getMainActivityLaunchIntent(launcherActivityInfo.getComponentName(), null, UserHandle.of(currentUser));
                            if (mainActivityLaunchIntent != null) {
                                Intent intent = new Intent(mainActivityLaunchIntent.getIntent());
                                intent.putExtra("systemui_google_quick_tap_is_source", true);
                                LinkedHashMap linkedHashMap = LaunchApp.this.availableApps;
                                ComponentName componentName = launcherActivityInfo.getComponentName();
                                LaunchApp launchApp2 = LaunchApp.this;
                                Objects.requireNonNull(launchApp2);
                                linkedHashMap.put(componentName, PendingIntent.getActivityAsUser(launchApp2.context, 0, intent, 67108864, null, LaunchApp.this.userTracker.getUserHandle()));
                                LaunchApp.access$addShortcutsForApp(LaunchApp.this, launcherActivityInfo, list);
                            }
                        } catch (RuntimeException unused) {
                        }
                    }
                    final LaunchApp launchApp3 = LaunchApp.this;
                    launchApp3.mainHandler.post(new Runnable() { // from class: com.google.android.systemui.columbus.actions.LaunchApp$updateAvailableAppsAndShortcutsAsync$1.1
                        @Override // java.lang.Runnable
                        public final void run() {
                            LaunchApp.this.updateAvailable();
                        }
                    });
                    return;
                }
                Log.d("Columbus/LaunchApp", "Did not update apps and shortcuts, user " + currentUser + " not unlocked");
            }
        });
    }

    public final void updateDenyList(String str) {
        this.denyPackageList.clear();
        if (str != null) {
            LinkedHashSet linkedHashSet = this.denyPackageList;
            List<String> split$default = StringsKt__StringsKt.split$default(str, new String[]{","});
            ArrayList arrayList = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(split$default, 10));
            for (String str2 : split$default) {
                arrayList.add(StringsKt__StringsKt.trim(str2).toString());
            }
            linkedHashSet.addAll(arrayList);
        }
    }

    public final boolean usingShortcut() {
        boolean z;
        String str;
        if (this.currentShortcut.length() > 0) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            String str2 = this.currentShortcut;
            ComponentName componentName = this.currentApp;
            if (componentName == null) {
                str = null;
            } else {
                str = componentName.flattenToString();
            }
            if (!Intrinsics.areEqual(str2, str)) {
                return true;
            }
        }
        return false;
    }

    public static final void access$addShortcutsForApp(LaunchApp launchApp, LauncherActivityInfo launcherActivityInfo, List list) {
        Objects.requireNonNull(launchApp);
        if (list != null) {
            ArrayList arrayList = new ArrayList();
            for (Object obj : list) {
                if (Intrinsics.areEqual(((ShortcutInfo) obj).getPackage(), launcherActivityInfo.getComponentName().getPackageName())) {
                    arrayList.add(obj);
                }
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                ShortcutInfo shortcutInfo = (ShortcutInfo) it.next();
                launchApp.availableShortcuts.putIfAbsent(shortcutInfo.getPackage(), new LinkedHashMap());
                Object obj2 = launchApp.availableShortcuts.get(shortcutInfo.getPackage());
                Intrinsics.checkNotNull(obj2);
                ((Map) obj2).put(shortcutInfo.getId(), shortcutInfo);
            }
        }
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public final String toString() {
        if (!usingShortcut()) {
            return Intrinsics.stringPlus("Launch ", this.currentApp);
        }
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Launch ");
        m.append(this.currentApp);
        m.append(" shortcut ");
        m.append(this.currentShortcut);
        return m.toString();
    }

    public final void updateAvailable() {
        String str;
        if (usingShortcut()) {
            LinkedHashMap linkedHashMap = this.availableShortcuts;
            ComponentName componentName = this.currentApp;
            if (componentName == null) {
                str = null;
            } else {
                str = componentName.getPackageName();
            }
            Map map = (Map) linkedHashMap.get(str);
            boolean z = false;
            if (map != null && map.containsKey(this.currentShortcut)) {
                z = true;
            }
            setAvailable(z);
            return;
        }
        setAvailable(this.availableApps.containsKey(this.currentApp));
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public final String getTag$vendor__unbundled_google__packages__SystemUIGoogle__android_common__sysuig() {
        return this.tag;
    }
}

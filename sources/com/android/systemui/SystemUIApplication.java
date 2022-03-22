package com.android.systemui;

import android.app.ActivityManager;
import android.app.ActivityThread;
import android.app.Application;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Bundle;
import android.os.Process;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.Dumpable;
import android.util.DumpableContainer;
import android.util.Log;
import android.util.TimingsTraceLog;
import android.view.SurfaceControl;
import android.view.ThreadedRenderer;
import com.android.systemui.SystemUIAppComponentFactory;
import com.android.systemui.dagger.ContextComponentHelper;
import com.android.systemui.dagger.GlobalRootComponent;
import com.android.systemui.dagger.SysUIComponent;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.util.NotificationChannels;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import javax.inject.Provider;
/* loaded from: classes.dex */
public class SystemUIApplication extends Application implements SystemUIAppComponentFactory.ContextInitializer, DumpableContainer {
    public static final /* synthetic */ int $r8$clinit = 0;
    public BootCompleteCacheImpl mBootCompleteCache;
    public ContextComponentHelper mComponentHelper;
    public SystemUIAppComponentFactory.ContextAvailableCallback mContextAvailableCallback;
    public DumpManager mDumpManager;
    public final ArrayMap<String, Dumpable> mDumpables = new ArrayMap<>();
    public GlobalRootComponent mRootComponent;
    public CoreStartable[] mServices;
    public boolean mServicesStarted;
    public SysUIComponent mSysUIComponent;

    public final void startServicesIfNeeded() {
        SystemUIFactory systemUIFactory = SystemUIFactory.mFactory;
        Resources resources = getResources();
        Objects.requireNonNull(systemUIFactory);
        String string = resources.getString(2131952148);
        TreeMap treeMap = new TreeMap(Comparator.comparing(SystemUIApplication$$ExternalSyntheticLambda4.INSTANCE));
        SystemUIFactory systemUIFactory2 = SystemUIFactory.mFactory;
        Objects.requireNonNull(systemUIFactory2);
        treeMap.putAll(systemUIFactory2.mSysUIComponent.getStartables());
        SystemUIFactory systemUIFactory3 = SystemUIFactory.mFactory;
        Objects.requireNonNull(systemUIFactory3);
        treeMap.putAll(systemUIFactory3.mSysUIComponent.getPerUserStartables());
        startServicesIfNeeded(treeMap, "StartServices", string);
    }

    public static void overrideNotificationAppName(Context context, Notification.Builder builder, boolean z) {
        String str;
        Bundle bundle = new Bundle();
        if (z) {
            str = context.getString(17040853);
        } else {
            str = context.getString(17040852);
        }
        bundle.putString("android.substName", str);
        builder.addExtras(bundle);
    }

    @Override // android.app.Application, android.content.ComponentCallbacks
    public final void onConfigurationChanged(Configuration configuration) {
        if (this.mServicesStarted) {
            this.mSysUIComponent.getConfigurationController().onConfigurationChanged(configuration);
            int length = this.mServices.length;
            for (int i = 0; i < length; i++) {
                CoreStartable[] coreStartableArr = this.mServices;
                if (coreStartableArr[i] != null) {
                    coreStartableArr[i].onConfigurationChanged(configuration);
                }
            }
        }
    }

    public final boolean removeDumpable(Dumpable dumpable) {
        Log.w("SystemUIService", "removeDumpable(" + dumpable + "): not implemented");
        return false;
    }

    public SystemUIApplication() {
        Log.v("SystemUIService", "SystemUIApplication constructed.");
    }

    public static void timeInitialization(String str, Runnable runnable, TimingsTraceLog timingsTraceLog, String str2) {
        long currentTimeMillis = System.currentTimeMillis();
        timingsTraceLog.traceBegin(str2 + " " + str);
        runnable.run();
        timingsTraceLog.traceEnd();
        long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
        if (currentTimeMillis2 > 1000) {
            Log.w("SystemUIService", "Initialization of " + str + " took " + currentTimeMillis2 + " ms");
        }
    }

    public final boolean addDumpable(final Dumpable dumpable) {
        String dumpableName = dumpable.getDumpableName();
        if (this.mDumpables.containsKey(dumpableName)) {
            return false;
        }
        this.mDumpables.put(dumpableName, dumpable);
        this.mDumpManager.registerDumpable(dumpable.getDumpableName(), new Dumpable() { // from class: com.android.systemui.SystemUIApplication$$ExternalSyntheticLambda0
            @Override // com.android.systemui.Dumpable
            public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
                Dumpable dumpable2 = dumpable;
                int i = SystemUIApplication.$r8$clinit;
                dumpable2.dump(printWriter, strArr);
            }
        });
        return true;
    }

    @Override // android.app.Application
    public final void onCreate() {
        super.onCreate();
        Log.v("SystemUIService", "SystemUIApplication created.");
        TimingsTraceLog timingsTraceLog = new TimingsTraceLog("SystemUIBootTiming", 4096L);
        timingsTraceLog.traceBegin("DependencyInjection");
        this.mContextAvailableCallback.onContextAvailable(this);
        SystemUIFactory systemUIFactory = SystemUIFactory.mFactory;
        Objects.requireNonNull(systemUIFactory);
        this.mRootComponent = systemUIFactory.mRootComponent;
        SystemUIFactory systemUIFactory2 = SystemUIFactory.mFactory;
        Objects.requireNonNull(systemUIFactory2);
        SysUIComponent sysUIComponent = systemUIFactory2.mSysUIComponent;
        this.mSysUIComponent = sysUIComponent;
        this.mComponentHelper = sysUIComponent.getContextComponentHelper();
        this.mBootCompleteCache = this.mSysUIComponent.provideBootCacheImpl();
        timingsTraceLog.traceEnd();
        setTheme(2132018181);
        if (Process.myUserHandle().equals(UserHandle.SYSTEM)) {
            IntentFilter intentFilter = new IntentFilter("android.intent.action.BOOT_COMPLETED");
            intentFilter.setPriority(1000);
            int gPUContextPriority = SurfaceControl.getGPUContextPriority();
            Log.i("SystemUIService", "Found SurfaceFlinger's GPU Priority: " + gPUContextPriority);
            if (gPUContextPriority == 13143) {
                Log.i("SystemUIService", "Setting SysUI's GPU Context priority to: 12545");
                ThreadedRenderer.setContextPriority(12545);
            }
            try {
                ActivityManager.getService().enableBinderTracing();
            } catch (RemoteException e) {
                Log.e("SystemUIService", "Unable to enable binder tracing", e);
            }
            registerReceiver(new BroadcastReceiver() { // from class: com.android.systemui.SystemUIApplication.1
                @Override // android.content.BroadcastReceiver
                public final void onReceive(Context context, Intent intent) {
                    if (!SystemUIApplication.this.mBootCompleteCache.isBootComplete()) {
                        SystemUIApplication.this.unregisterReceiver(this);
                        SystemUIApplication.this.mBootCompleteCache.setBootComplete();
                        SystemUIApplication systemUIApplication = SystemUIApplication.this;
                        if (systemUIApplication.mServicesStarted) {
                            int length = systemUIApplication.mServices.length;
                            for (int i = 0; i < length; i++) {
                                SystemUIApplication.this.mServices[i].onBootCompleted();
                            }
                        }
                    }
                }
            }, intentFilter);
            registerReceiver(new BroadcastReceiver() { // from class: com.android.systemui.SystemUIApplication.2
                @Override // android.content.BroadcastReceiver
                public final void onReceive(Context context, Intent intent) {
                    if ("android.intent.action.LOCALE_CHANGED".equals(intent.getAction()) && SystemUIApplication.this.mBootCompleteCache.isBootComplete()) {
                        NotificationChannels.createAll(context);
                    }
                }
            }, new IntentFilter("android.intent.action.LOCALE_CHANGED"));
            return;
        }
        String currentProcessName = ActivityThread.currentProcessName();
        ApplicationInfo applicationInfo = getApplicationInfo();
        if (currentProcessName != null) {
            if (currentProcessName.startsWith(applicationInfo.processName + ":")) {
                return;
            }
        }
        TreeMap treeMap = new TreeMap(Comparator.comparing(SystemUIApplication$$ExternalSyntheticLambda3.INSTANCE));
        SystemUIFactory systemUIFactory3 = SystemUIFactory.mFactory;
        Objects.requireNonNull(systemUIFactory3);
        treeMap.putAll(systemUIFactory3.mSysUIComponent.getPerUserStartables());
        startServicesIfNeeded(treeMap, "StartSecondaryServices", null);
    }

    public final void startServicesIfNeeded(TreeMap treeMap, String str, String str2) {
        if (!this.mServicesStarted) {
            this.mServices = new CoreStartable[treeMap.size() + (str2 == null ? 0 : 1)];
            if (!this.mBootCompleteCache.isBootComplete() && "1".equals(SystemProperties.get("sys.boot_completed"))) {
                this.mBootCompleteCache.setBootComplete();
            }
            this.mDumpManager = this.mSysUIComponent.createDumpManager();
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Starting SystemUI services for user ");
            m.append(Process.myUserHandle().getIdentifier());
            m.append(".");
            Log.v("SystemUIService", m.toString());
            TimingsTraceLog timingsTraceLog = new TimingsTraceLog("SystemUIBootTiming", 4096L);
            timingsTraceLog.traceBegin(str);
            final int i = 0;
            for (final Map.Entry entry : treeMap.entrySet()) {
                final String name = ((Class) entry.getKey()).getName();
                timeInitialization(name, new Runnable(i, name, entry) { // from class: com.android.systemui.SystemUIApplication$$ExternalSyntheticLambda2
                    public final /* synthetic */ int f$1;
                    public final /* synthetic */ Map.Entry f$3;

                    {
                        this.f$3 = entry;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        SystemUIApplication systemUIApplication = SystemUIApplication.this;
                        int i2 = this.f$1;
                        Map.Entry entry2 = this.f$3;
                        int i3 = SystemUIApplication.$r8$clinit;
                        Objects.requireNonNull(systemUIApplication);
                        CoreStartable[] coreStartableArr = systemUIApplication.mServices;
                        CoreStartable coreStartable = (CoreStartable) ((Provider) entry2.getValue()).mo144get();
                        coreStartable.start();
                        coreStartableArr[i2] = coreStartable;
                    }
                }, timingsTraceLog, str);
                i++;
            }
            if (str2 != null) {
                timeInitialization(str2, new SystemUIApplication$$ExternalSyntheticLambda1(this, str2, 0), timingsTraceLog, str);
            }
            for (int i2 = 0; i2 < this.mServices.length; i2++) {
                if (this.mBootCompleteCache.isBootComplete()) {
                    this.mServices[i2].onBootCompleted();
                }
                this.mDumpManager.registerDumpable(this.mServices[i2].getClass().getName(), this.mServices[i2]);
            }
            InitController initController = this.mSysUIComponent.getInitController();
            Objects.requireNonNull(initController);
            while (!initController.mTasks.isEmpty()) {
                initController.mTasks.remove(0).run();
            }
            initController.mTasksExecuted = true;
            timingsTraceLog.traceEnd();
            this.mServicesStarted = true;
        }
    }

    @Override // com.android.systemui.SystemUIAppComponentFactory.ContextInitializer
    public final void setContextAvailableCallback(SystemUIAppComponentFactory.ContextAvailableCallback contextAvailableCallback) {
        this.mContextAvailableCallback = contextAvailableCallback;
    }
}

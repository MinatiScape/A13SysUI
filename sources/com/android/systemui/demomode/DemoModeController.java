package com.android.systemui.demomode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.android.keyguard.KeyguardBiometricLockoutLogger$$ExternalSyntheticOutline0;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.policy.CallbackController;
import com.android.systemui.util.Assert;
import com.android.systemui.util.settings.GlobalSettings;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import kotlin.collections.CollectionsKt__IteratorsJVMKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__StringsKt;
/* compiled from: DemoModeController.kt */
/* loaded from: classes.dex */
public final class DemoModeController implements CallbackController<DemoMode>, Dumpable {
    public final DemoModeController$broadcastReceiver$1 broadcastReceiver;
    public final Context context;
    public final GlobalSettings globalSettings;
    public boolean initialized;
    public boolean isInDemoMode;
    public final LinkedHashMap receiverMap;
    public final ArrayList receivers = new ArrayList();
    public final DemoModeController$tracker$1 tracker;

    public final void enterDemoMode() {
        List<DemoModeCommandReceiver> list;
        this.isInDemoMode = true;
        Assert.isMainThread();
        synchronized (this) {
            list = CollectionsKt___CollectionsKt.toList(this.receivers);
        }
        for (DemoModeCommandReceiver demoModeCommandReceiver : list) {
            demoModeCommandReceiver.onDemoModeStarted();
        }
    }

    public final void exitDemoMode() {
        List<DemoModeCommandReceiver> list;
        this.isInDemoMode = false;
        Assert.isMainThread();
        synchronized (this) {
            list = CollectionsKt___CollectionsKt.toList(this.receivers);
        }
        for (DemoModeCommandReceiver demoModeCommandReceiver : list) {
            demoModeCommandReceiver.onDemoModeFinished();
        }
    }

    public final void addCallback(DemoMode demoMode) {
        for (String str : demoMode.demoCommands()) {
            if (this.receiverMap.containsKey(str)) {
                Object obj = this.receiverMap.get(str);
                Intrinsics.checkNotNull(obj);
                ((List) obj).add(demoMode);
            } else {
                throw new IllegalStateException("Command (" + ((Object) str) + ") not recognized. See DemoMode.java for valid commands");
            }
        }
        synchronized (this) {
            this.receivers.add(demoMode);
        }
        if (this.isInDemoMode) {
            demoMode.onDemoModeStarted();
        }
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        List<DemoModeCommandReceiver> list;
        printWriter.println("DemoModeController state -");
        KeyguardBiometricLockoutLogger$$ExternalSyntheticOutline0.m(this.isInDemoMode, "  isInDemoMode=", printWriter);
        DemoModeController$tracker$1 demoModeController$tracker$1 = this.tracker;
        Objects.requireNonNull(demoModeController$tracker$1);
        printWriter.println(Intrinsics.stringPlus("  isDemoModeAllowed=", Boolean.valueOf(demoModeController$tracker$1.isDemoModeAvailable)));
        printWriter.print("  receivers=[");
        synchronized (this) {
            list = CollectionsKt___CollectionsKt.toList(this.receivers);
        }
        for (DemoModeCommandReceiver demoModeCommandReceiver : list) {
            printWriter.print(Intrinsics.stringPlus(" ", demoModeCommandReceiver.getClass().getSimpleName()));
        }
        printWriter.println(" ]");
        printWriter.println("  receiverMap= [");
        for (String str : this.receiverMap.keySet()) {
            printWriter.print("    " + str + " : [");
            Object obj = this.receiverMap.get(str);
            Intrinsics.checkNotNull(obj);
            Iterable<DemoMode> iterable = (Iterable) obj;
            ArrayList arrayList = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(iterable, 10));
            for (DemoMode demoMode : iterable) {
                arrayList.add(demoMode.getClass().getSimpleName());
            }
            printWriter.println(Intrinsics.stringPlus(CollectionsKt___CollectionsKt.joinToString$default(arrayList, ",", null, null, null, 62), " ]"));
        }
    }

    public final void removeCallback(DemoMode demoMode) {
        synchronized (this) {
            for (String str : demoMode.demoCommands()) {
                Object obj = this.receiverMap.get(str);
                Intrinsics.checkNotNull(obj);
                ((List) obj).remove(demoMode);
            }
            this.receivers.remove(demoMode);
        }
    }

    /* JADX WARN: Type inference failed for: r3v4, types: [com.android.systemui.demomode.DemoModeController$broadcastReceiver$1] */
    /* JADX WARN: Type inference failed for: r4v3, types: [com.android.systemui.demomode.DemoModeController$tracker$1] */
    public DemoModeController(Context context, DumpManager dumpManager, GlobalSettings globalSettings) {
        this.context = context;
        this.globalSettings = globalSettings;
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        ArrayList<String> arrayList = DemoMode.COMMANDS;
        ArrayList arrayList2 = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(arrayList, 10));
        for (String str : arrayList) {
            arrayList2.add((List) linkedHashMap.put(str, new ArrayList()));
        }
        this.receiverMap = linkedHashMap;
        final Context context2 = this.context;
        this.tracker = new DemoModeAvailabilityTracker(context2) { // from class: com.android.systemui.demomode.DemoModeController$tracker$1
            @Override // com.android.systemui.demomode.DemoModeAvailabilityTracker
            public final void onDemoModeAvailabilityChanged() {
                DemoModeController demoModeController = DemoModeController.this;
                boolean z = this.isDemoModeAvailable;
                Objects.requireNonNull(demoModeController);
                if (demoModeController.isInDemoMode && !z) {
                    demoModeController.globalSettings.putInt("sysui_tuner_demo_on", 0);
                }
            }

            @Override // com.android.systemui.demomode.DemoModeAvailabilityTracker
            public final void onDemoModeFinished() {
                DemoModeController demoModeController = DemoModeController.this;
                Objects.requireNonNull(demoModeController);
                if (demoModeController.isInDemoMode != this.isInDemoMode) {
                    DemoModeController.this.exitDemoMode();
                }
            }

            @Override // com.android.systemui.demomode.DemoModeAvailabilityTracker
            public final void onDemoModeStarted() {
                DemoModeController demoModeController = DemoModeController.this;
                Objects.requireNonNull(demoModeController);
                if (demoModeController.isInDemoMode != this.isInDemoMode) {
                    DemoModeController.this.enterDemoMode();
                }
            }
        };
        this.broadcastReceiver = new BroadcastReceiver() { // from class: com.android.systemui.demomode.DemoModeController$broadcastReceiver$1
            @Override // android.content.BroadcastReceiver
            public final void onReceive(Context context3, Intent intent) {
                Bundle extras;
                if ("com.android.systemui.demo".equals(intent.getAction()) && (extras = intent.getExtras()) != null) {
                    String lowerCase = StringsKt__StringsKt.trim(extras.getString("command", "")).toString().toLowerCase();
                    if (lowerCase.length() != 0) {
                        try {
                            DemoModeController.this.dispatchDemoCommand(lowerCase, extras);
                        } catch (Throwable th) {
                            Log.w("DemoModeController", "Error running demo command, intent=" + intent + ' ' + th);
                        }
                    }
                }
            }
        };
    }

    public final void dispatchDemoCommand(String str, Bundle bundle) {
        Assert.isMainThread();
        DemoModeController$tracker$1 demoModeController$tracker$1 = this.tracker;
        Objects.requireNonNull(demoModeController$tracker$1);
        if (demoModeController$tracker$1.isDemoModeAvailable) {
            if (Intrinsics.areEqual(str, "enter")) {
                enterDemoMode();
            } else if (Intrinsics.areEqual(str, "exit")) {
                exitDemoMode();
            } else if (!this.isInDemoMode) {
                enterDemoMode();
            }
            Object obj = this.receiverMap.get(str);
            Intrinsics.checkNotNull(obj);
            for (DemoMode demoMode : (Iterable) obj) {
                demoMode.dispatchDemoCommand(str, bundle);
            }
        }
    }
}

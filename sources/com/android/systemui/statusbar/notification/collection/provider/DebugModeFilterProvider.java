package com.android.systemui.statusbar.notification.collection.provider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import com.android.keyguard.KeyguardBiometricLockoutLogger$$ExternalSyntheticOutline0;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.util.Assert;
import com.android.systemui.util.ListenerSet;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import kotlin.collections.EmptyList;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: DebugModeFilterProvider.kt */
/* loaded from: classes.dex */
public final class DebugModeFilterProvider implements Dumpable {
    public final Context context;
    public List<String> allowedPackages = EmptyList.INSTANCE;
    public final ListenerSet<Runnable> listeners = new ListenerSet<>();
    public final DebugModeFilterProvider$mReceiver$1 mReceiver = new BroadcastReceiver() { // from class: com.android.systemui.statusbar.notification.collection.provider.DebugModeFilterProvider$mReceiver$1
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            String str;
            List<String> list = null;
            if (intent == null) {
                str = null;
            } else {
                str = intent.getAction();
            }
            if (Intrinsics.areEqual("com.android.systemui.action.SET_NOTIF_DEBUG_MODE", str)) {
                DebugModeFilterProvider debugModeFilterProvider = DebugModeFilterProvider.this;
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    list = extras.getStringArrayList("allowed_packages");
                }
                if (list == null) {
                    list = EmptyList.INSTANCE;
                }
                debugModeFilterProvider.allowedPackages = list;
                Log.d("DebugModeFilterProvider", Intrinsics.stringPlus("Updated allowedPackages: ", DebugModeFilterProvider.this.allowedPackages));
                for (Runnable runnable : DebugModeFilterProvider.this.listeners) {
                    runnable.run();
                }
                return;
            }
            Log.d("DebugModeFilterProvider", Intrinsics.stringPlus("Malformed intent: ", intent));
        }
    };

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        ListenerSet<Runnable> listenerSet = this.listeners;
        Objects.requireNonNull(listenerSet);
        KeyguardBiometricLockoutLogger$$ExternalSyntheticOutline0.m(!listenerSet.listeners.isEmpty(), "initialized: ", printWriter);
        printWriter.println(Intrinsics.stringPlus("allowedPackages: ", Integer.valueOf(this.allowedPackages.size())));
        int i = 0;
        for (Object obj : this.allowedPackages) {
            int i2 = i + 1;
            if (i >= 0) {
                printWriter.println("  [" + i + "]: " + ((String) obj));
                i = i2;
            } else {
                SetsKt__SetsKt.throwIndexOverflow();
                throw null;
            }
        }
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [com.android.systemui.statusbar.notification.collection.provider.DebugModeFilterProvider$mReceiver$1] */
    public DebugModeFilterProvider(Context context, DumpManager dumpManager) {
        this.context = context;
        dumpManager.registerDumpable(this);
    }

    public final void registerInvalidationListener(Runnable runnable) {
        Assert.isMainThread();
        if (Build.isDebuggable()) {
            ListenerSet<Runnable> listenerSet = this.listeners;
            Objects.requireNonNull(listenerSet);
            boolean isEmpty = listenerSet.listeners.isEmpty();
            this.listeners.addIfAbsent(runnable);
            if (isEmpty) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("com.android.systemui.action.SET_NOTIF_DEBUG_MODE");
                this.context.registerReceiver(this.mReceiver, intentFilter, "com.android.systemui.permission.NOTIF_DEBUG_MODE", null, 2);
                Log.d("DebugModeFilterProvider", Intrinsics.stringPlus("Registered: ", this.mReceiver));
            }
        }
    }
}

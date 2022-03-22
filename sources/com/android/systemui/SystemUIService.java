package com.android.systemui;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Process;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.util.Log;
import android.util.Slog;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline0;
import com.android.internal.os.BinderInternal;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpHandler;
import com.android.systemui.dump.LogBufferFreezer;
import com.android.systemui.dump.SystemUIAuxiliaryDumpService;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.statusbar.policy.BatteryStateNotifier;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Objects;
/* loaded from: classes.dex */
public class SystemUIService extends Service {
    public final BatteryStateNotifier mBatteryStateNotifier;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public final DumpHandler mDumpHandler;
    public final LogBufferFreezer mLogBufferFreezer;
    public final Handler mMainHandler;

    @Override // android.app.Service
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        if (strArr.length == 0) {
            strArr = new String[]{"--dump-priority", "CRITICAL"};
        }
        this.mDumpHandler.dump(fileDescriptor, printWriter, strArr);
    }

    @Override // android.app.Service
    public final IBinder onBind(Intent intent) {
        return null;
    }

    public SystemUIService(Handler handler, DumpHandler dumpHandler, BroadcastDispatcher broadcastDispatcher, LogBufferFreezer logBufferFreezer, BatteryStateNotifier batteryStateNotifier) {
        this.mMainHandler = handler;
        this.mDumpHandler = dumpHandler;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mLogBufferFreezer = logBufferFreezer;
        this.mBatteryStateNotifier = batteryStateNotifier;
    }

    @Override // android.app.Service
    public final void onCreate() {
        super.onCreate();
        ((SystemUIApplication) getApplication()).startServicesIfNeeded();
        final LogBufferFreezer logBufferFreezer = this.mLogBufferFreezer;
        BroadcastDispatcher broadcastDispatcher = this.mBroadcastDispatcher;
        Objects.requireNonNull(logBufferFreezer);
        BroadcastDispatcher.registerReceiver$default(broadcastDispatcher, new BroadcastReceiver() { // from class: com.android.systemui.dump.LogBufferFreezer$attach$1
            @Override // android.content.BroadcastReceiver
            public final void onReceive(Context context, Intent intent) {
                final LogBufferFreezer logBufferFreezer2 = LogBufferFreezer.this;
                Objects.requireNonNull(logBufferFreezer2);
                Runnable runnable = logBufferFreezer2.pendingToken;
                if (runnable != null) {
                    runnable.run();
                }
                Log.i("LogBufferFreezer", "Freezing log buffers");
                DumpManager dumpManager = logBufferFreezer2.dumpManager;
                Objects.requireNonNull(dumpManager);
                synchronized (dumpManager) {
                    for (RegisteredDumpable registeredDumpable : dumpManager.buffers.values()) {
                        Objects.requireNonNull(registeredDumpable);
                        ((LogBuffer) registeredDumpable.dumpable).freeze();
                    }
                }
                logBufferFreezer2.pendingToken = logBufferFreezer2.executor.executeDelayed(new Runnable() { // from class: com.android.systemui.dump.LogBufferFreezer$onBugreportStarted$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        Log.i("LogBufferFreezer", "Unfreezing log buffers");
                        LogBufferFreezer logBufferFreezer3 = LogBufferFreezer.this;
                        logBufferFreezer3.pendingToken = null;
                        DumpManager dumpManager2 = logBufferFreezer3.dumpManager;
                        Objects.requireNonNull(dumpManager2);
                        synchronized (dumpManager2) {
                            for (RegisteredDumpable registeredDumpable2 : dumpManager2.buffers.values()) {
                                Objects.requireNonNull(registeredDumpable2);
                                ((LogBuffer) registeredDumpable2.dumpable).unfreeze();
                            }
                        }
                    }
                }, logBufferFreezer2.freezeDuration);
            }
        }, new IntentFilter("com.android.internal.intent.action.BUGREPORT_STARTED"), logBufferFreezer.executor, UserHandle.ALL, 16);
        if (getResources().getBoolean(2131034165)) {
            BatteryStateNotifier batteryStateNotifier = this.mBatteryStateNotifier;
            Objects.requireNonNull(batteryStateNotifier);
            batteryStateNotifier.controller.addCallback(batteryStateNotifier);
        }
        if (!Build.IS_DEBUGGABLE || !SystemProperties.getBoolean("debug.crash_sysui", false)) {
            if (Build.IS_DEBUGGABLE) {
                BinderInternal.nSetBinderProxyCountEnabled(true);
                BinderInternal.nSetBinderProxyCountWatermarks(1000, 900);
                BinderInternal.setBinderProxyCountCallback(new BinderInternal.BinderProxyLimitListener() { // from class: com.android.systemui.SystemUIService.1
                    public final void onLimitReached(int i) {
                        StringBuilder m = ExifInterface$$ExternalSyntheticOutline0.m("uid ", i, " sent too many Binder proxies to uid ");
                        m.append(Process.myUid());
                        Slog.w("SystemUIService", m.toString());
                    }
                }, this.mMainHandler);
            }
            startServiceAsUser(new Intent(getApplicationContext(), SystemUIAuxiliaryDumpService.class), UserHandle.SYSTEM);
            return;
        }
        throw new RuntimeException();
    }
}

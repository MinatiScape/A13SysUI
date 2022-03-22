package com.android.systemui.doze;

import android.content.Context;
import android.os.PowerManager;
import android.os.SystemClock;
import android.service.dreams.DreamService;
import android.util.Log;
import android.view.Display;
import com.android.systemui.doze.DozeMachine;
import com.android.systemui.doze.dagger.DozeComponent;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.plugins.DozeServicePlugin;
import com.android.systemui.plugins.PluginListener;
import com.android.systemui.shared.plugins.PluginManager;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Objects;
/* loaded from: classes.dex */
public class DozeService extends DreamService implements DozeMachine.Service, DozeServicePlugin.RequestDoze, PluginListener<DozeServicePlugin> {
    public static final boolean DEBUG = Log.isLoggable("DozeService", 3);
    public final DozeComponent.Builder mDozeComponentBuilder;
    public DozeMachine mDozeMachine;
    public DozeServicePlugin mDozePlugin;
    public PluginManager mPluginManager;

    @Override // android.service.dreams.DreamService, android.app.Service
    public final void onDestroy() {
        PluginManager pluginManager = this.mPluginManager;
        if (pluginManager != null) {
            pluginManager.removePluginListener(this);
        }
        super.onDestroy();
        DozeMachine dozeMachine = this.mDozeMachine;
        Objects.requireNonNull(dozeMachine);
        for (DozeMachine.Part part : dozeMachine.mParts) {
            part.destroy();
        }
        this.mDozeMachine = null;
    }

    @Override // com.android.systemui.plugins.PluginListener
    public final void onPluginConnected(DozeServicePlugin dozeServicePlugin, Context context) {
        DozeServicePlugin dozeServicePlugin2 = dozeServicePlugin;
        this.mDozePlugin = dozeServicePlugin2;
        dozeServicePlugin2.setDozeRequester(this);
    }

    @Override // com.android.systemui.plugins.PluginListener
    public final void onPluginDisconnected(DozeServicePlugin dozeServicePlugin) {
        DozeServicePlugin dozeServicePlugin2 = this.mDozePlugin;
        if (dozeServicePlugin2 != null) {
            dozeServicePlugin2.onDreamingStopped();
            this.mDozePlugin = null;
        }
    }

    @Override // com.android.systemui.plugins.DozeServicePlugin.RequestDoze
    public final void onRequestHideDoze() {
        DozeMachine dozeMachine = this.mDozeMachine;
        if (dozeMachine != null) {
            dozeMachine.requestState(DozeMachine.State.DOZE);
        }
    }

    @Override // com.android.systemui.plugins.DozeServicePlugin.RequestDoze
    public final void onRequestShowDoze() {
        DozeMachine dozeMachine = this.mDozeMachine;
        if (dozeMachine != null) {
            dozeMachine.requestState(DozeMachine.State.DOZE_AOD);
        }
    }

    @Override // com.android.systemui.doze.DozeMachine.Service
    public final void requestWakeUp() {
        ((PowerManager) getSystemService(PowerManager.class)).wakeUp(SystemClock.uptimeMillis(), 4, "com.android.systemui:NODOZE");
    }

    public DozeService(DozeComponent.Builder builder, PluginManager pluginManager) {
        this.mDozeComponentBuilder = builder;
        setDebug(DEBUG);
        this.mPluginManager = pluginManager;
    }

    public final void dumpOnHandler(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        super.dumpOnHandler(fileDescriptor, printWriter, strArr);
        DozeMachine dozeMachine = this.mDozeMachine;
        if (dozeMachine != null) {
            Objects.requireNonNull(dozeMachine);
            printWriter.print(" state=");
            printWriter.println(dozeMachine.mState);
            printWriter.print(" wakeLockHeldForCurrentState=");
            printWriter.println(dozeMachine.mWakeLockHeldForCurrentState);
            printWriter.print(" wakeLock=");
            printWriter.println(dozeMachine.mWakeLock);
            printWriter.print(" isDozeSuppressed=");
            printWriter.println(dozeMachine.mDozeHost.isDozeSuppressed());
            printWriter.println("Parts:");
            for (DozeMachine.Part part : dozeMachine.mParts) {
                part.dump(printWriter);
            }
        }
    }

    @Override // android.service.dreams.DreamService, android.app.Service
    public final void onCreate() {
        super.onCreate();
        setWindowless(true);
        this.mPluginManager.addPluginListener((PluginListener) this, DozeServicePlugin.class, false);
        this.mDozeMachine = this.mDozeComponentBuilder.build(this).getDozeMachine();
    }

    @Override // android.service.dreams.DreamService
    public final void onDreamingStarted() {
        super.onDreamingStarted();
        this.mDozeMachine.requestState(DozeMachine.State.INITIALIZED);
        startDozing();
        DozeServicePlugin dozeServicePlugin = this.mDozePlugin;
        if (dozeServicePlugin != null) {
            dozeServicePlugin.onDreamingStarted();
        }
    }

    @Override // android.service.dreams.DreamService
    public final void onDreamingStopped() {
        super.onDreamingStopped();
        this.mDozeMachine.requestState(DozeMachine.State.FINISH);
        DozeServicePlugin dozeServicePlugin = this.mDozePlugin;
        if (dozeServicePlugin != null) {
            dozeServicePlugin.onDreamingStopped();
        }
    }

    @Override // com.android.systemui.doze.DozeMachine.Service
    public final void setDozeScreenState(int i) {
        super.setDozeScreenState(i);
        DozeMachine dozeMachine = this.mDozeMachine;
        Objects.requireNonNull(dozeMachine);
        DozeLog dozeLog = dozeMachine.mDozeLog;
        Objects.requireNonNull(dozeLog);
        DozeLogger dozeLogger = dozeLog.mLogger;
        Objects.requireNonNull(dozeLogger);
        LogBuffer logBuffer = dozeLogger.buffer;
        LogLevel logLevel = LogLevel.INFO;
        DozeLogger$logDisplayStateChanged$2 dozeLogger$logDisplayStateChanged$2 = DozeLogger$logDisplayStateChanged$2.INSTANCE;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("DozeLog", logLevel, dozeLogger$logDisplayStateChanged$2);
            obtain.str1 = Display.stateToString(i);
            logBuffer.push(obtain);
        }
        for (DozeMachine.Part part : dozeMachine.mParts) {
            part.onScreenState(i);
        }
    }
}

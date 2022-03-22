package com.android.systemui.classifier;

import android.net.Uri;
import android.provider.DeviceConfig;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.util.DeviceConfigProxy;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class FalsingManagerProxy implements FalsingManager, Dumpable {
    public final Provider<BrightLineFalsingManager> mBrightLineFalsingManagerProvider;
    public final DeviceConfigProxy mDeviceConfig;
    public final FalsingManagerProxy$$ExternalSyntheticLambda0 mDeviceConfigListener;
    public final DumpManager mDumpManager;
    public FalsingManager mInternalFalsingManager;
    public final AnonymousClass1 mPluginListener;
    public final PluginManager mPluginManager;

    @Override // com.android.systemui.plugins.FalsingManager
    public final void addFalsingBeliefListener(FalsingManager.FalsingBeliefListener falsingBeliefListener) {
        this.mInternalFalsingManager.addFalsingBeliefListener(falsingBeliefListener);
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public final void addTapListener(FalsingManager.FalsingTapListener falsingTapListener) {
        this.mInternalFalsingManager.addTapListener(falsingTapListener);
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public final void cleanupInternal() {
        DeviceConfigProxy deviceConfigProxy = this.mDeviceConfig;
        FalsingManagerProxy$$ExternalSyntheticLambda0 falsingManagerProxy$$ExternalSyntheticLambda0 = this.mDeviceConfigListener;
        Objects.requireNonNull(deviceConfigProxy);
        DeviceConfig.removeOnPropertiesChangedListener(falsingManagerProxy$$ExternalSyntheticLambda0);
        this.mPluginManager.removePluginListener(this.mPluginListener);
        this.mDumpManager.unregisterDumpable("FalsingManager");
        this.mInternalFalsingManager.cleanupInternal();
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        this.mInternalFalsingManager.dump(fileDescriptor, printWriter, strArr);
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public final boolean isClassifierEnabled() {
        return this.mInternalFalsingManager.isClassifierEnabled();
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public final boolean isFalseDoubleTap() {
        return this.mInternalFalsingManager.isFalseDoubleTap();
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public final boolean isFalseTap(int i) {
        return this.mInternalFalsingManager.isFalseTap(i);
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public final boolean isFalseTouch(int i) {
        return this.mInternalFalsingManager.isFalseTouch(i);
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public final boolean isReportingEnabled() {
        return this.mInternalFalsingManager.isReportingEnabled();
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public final boolean isSimpleTap() {
        return this.mInternalFalsingManager.isSimpleTap();
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public final boolean isUnlockingDisabled() {
        return this.mInternalFalsingManager.isUnlockingDisabled();
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public final void onProximityEvent(FalsingManager.ProximityEvent proximityEvent) {
        this.mInternalFalsingManager.onProximityEvent(proximityEvent);
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public final void onSuccessfulUnlock() {
        this.mInternalFalsingManager.onSuccessfulUnlock();
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public final void removeFalsingBeliefListener(FalsingManager.FalsingBeliefListener falsingBeliefListener) {
        this.mInternalFalsingManager.removeFalsingBeliefListener(falsingBeliefListener);
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public final void removeTapListener(FalsingManager.FalsingTapListener falsingTapListener) {
        this.mInternalFalsingManager.removeTapListener(falsingTapListener);
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public final Uri reportRejectedTouch() {
        return this.mInternalFalsingManager.reportRejectedTouch();
    }

    public final void setupFalsingManager() {
        FalsingManager falsingManager = this.mInternalFalsingManager;
        if (falsingManager != null) {
            falsingManager.cleanupInternal();
        }
        this.mInternalFalsingManager = this.mBrightLineFalsingManagerProvider.mo144get();
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public final boolean shouldEnforceBouncer() {
        return this.mInternalFalsingManager.shouldEnforceBouncer();
    }

    /* JADX WARN: Type inference failed for: r3v1, types: [com.android.systemui.plugins.PluginListener, com.android.systemui.classifier.FalsingManagerProxy$1] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public FalsingManagerProxy(com.android.systemui.shared.plugins.PluginManager r2, java.util.concurrent.Executor r3, com.android.systemui.util.DeviceConfigProxy r4, com.android.systemui.dump.DumpManager r5, javax.inject.Provider<com.android.systemui.classifier.BrightLineFalsingManager> r6) {
        /*
            r1 = this;
            r1.<init>()
            com.android.systemui.classifier.FalsingManagerProxy$$ExternalSyntheticLambda0 r0 = new com.android.systemui.classifier.FalsingManagerProxy$$ExternalSyntheticLambda0
            r0.<init>()
            r1.mDeviceConfigListener = r0
            r1.mPluginManager = r2
            r1.mDumpManager = r5
            r1.mDeviceConfig = r4
            r1.mBrightLineFalsingManagerProvider = r6
            r1.setupFalsingManager()
            java.util.Objects.requireNonNull(r4)
            java.lang.String r4 = "systemui"
            android.provider.DeviceConfig.addOnPropertiesChangedListener(r4, r3, r0)
            com.android.systemui.classifier.FalsingManagerProxy$1 r3 = new com.android.systemui.classifier.FalsingManagerProxy$1
            r3.<init>()
            r1.mPluginListener = r3
            java.lang.Class<com.android.systemui.plugins.FalsingPlugin> r4 = com.android.systemui.plugins.FalsingPlugin.class
            r2.addPluginListener(r3, r4)
            java.lang.String r2 = "FalsingManager"
            r5.registerDumpable(r2, r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.classifier.FalsingManagerProxy.<init>(com.android.systemui.shared.plugins.PluginManager, java.util.concurrent.Executor, com.android.systemui.util.DeviceConfigProxy, com.android.systemui.dump.DumpManager, javax.inject.Provider):void");
    }
}

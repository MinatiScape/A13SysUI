package com.google.android.systemui.communal.dock.callbacks.mediashell;

import android.util.Log;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda22;
import com.android.systemui.util.condition.Monitor;
import com.android.systemui.util.service.ObservableServiceConnection;
import com.android.systemui.util.service.PersistentConnectionManager;
import java.util.Objects;
/* loaded from: classes.dex */
public final class MediaShellCallback implements Monitor.Callback {
    public AnonymousClass1 mCallback;
    public final PersistentConnectionManager mConnectionManager;
    public final DreamOverlayStateController mDreamOverlayStateController;
    public final MediaShellComplication mMediaShellComplication;

    @Override // com.android.systemui.util.condition.Monitor.Callback
    public final void onConditionsChanged(boolean z) {
        if (z) {
            PersistentConnectionManager persistentConnectionManager = this.mConnectionManager;
            Objects.requireNonNull(persistentConnectionManager);
            persistentConnectionManager.mConnection.addCallback(persistentConnectionManager.mConnectionCallback);
            persistentConnectionManager.mObserver.addCallback(persistentConnectionManager.mObserverCallback);
            persistentConnectionManager.mReconnectAttempts = 0;
            persistentConnectionManager.mConnection.bind();
            return;
        }
        PersistentConnectionManager persistentConnectionManager2 = this.mConnectionManager;
        Objects.requireNonNull(persistentConnectionManager2);
        ObservableServiceConnection<T> observableServiceConnection = persistentConnectionManager2.mConnection;
        PersistentConnectionManager.AnonymousClass2 r0 = persistentConnectionManager2.mConnectionCallback;
        if (ObservableServiceConnection.DEBUG) {
            Objects.requireNonNull(observableServiceConnection);
            Log.d("ObservableSvcConn", "removeCallback:" + r0);
        }
        observableServiceConnection.mExecutor.execute(new StatusBar$$ExternalSyntheticLambda22(observableServiceConnection, r0, 3));
        persistentConnectionManager2.mObserver.removeCallback(persistentConnectionManager2.mObserverCallback);
        persistentConnectionManager2.mConnection.unbind();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [com.google.android.systemui.communal.dock.callbacks.mediashell.MediaShellCallback$1] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public MediaShellCallback(com.google.android.systemui.communal.dock.callbacks.mediashell.dagger.MediaShellComponent$Factory r12, com.android.systemui.dreams.DreamOverlayStateController r13, com.google.android.systemui.communal.dock.callbacks.mediashell.MediaShellComplication r14) {
        /*
            r11 = this;
            r11.<init>()
            com.google.android.systemui.communal.dock.callbacks.mediashell.MediaShellCallback$1 r0 = new com.google.android.systemui.communal.dock.callbacks.mediashell.MediaShellCallback$1
            r0.<init>()
            r11.mCallback = r0
            com.google.android.systemui.titan.DaggerTitanGlobalRootComponent$TitanSysUIComponentImpl$MediaShellComponentImpl r12 = r12.create(r0)
            java.util.Objects.requireNonNull(r12)
            com.android.systemui.util.service.PersistentConnectionManager r8 = new com.android.systemui.util.service.PersistentConnectionManager
            com.google.android.systemui.titan.DaggerTitanGlobalRootComponent$TitanSysUIComponentImpl r0 = com.google.android.systemui.titan.DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.this
            javax.inject.Provider<com.android.systemui.util.time.SystemClock> r0 = r0.bindSystemClockProvider
            java.lang.Object r0 = r0.mo144get()
            r1 = r0
            com.android.systemui.util.time.SystemClock r1 = (com.android.systemui.util.time.SystemClock) r1
            com.google.android.systemui.titan.DaggerTitanGlobalRootComponent$TitanSysUIComponentImpl r0 = com.google.android.systemui.titan.DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.this
            javax.inject.Provider<com.android.systemui.util.concurrency.DelayableExecutor> r0 = r0.provideDelayableExecutorProvider
            java.lang.Object r0 = r0.mo144get()
            r2 = r0
            com.android.systemui.util.concurrency.DelayableExecutor r2 = (com.android.systemui.util.concurrency.DelayableExecutor) r2
            com.android.systemui.util.service.ObservableServiceConnection r3 = new com.android.systemui.util.service.ObservableServiceConnection
            com.google.android.systemui.titan.DaggerTitanGlobalRootComponent$TitanSysUIComponentImpl r0 = com.google.android.systemui.titan.DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.this
            com.google.android.systemui.titan.DaggerTitanGlobalRootComponent r0 = com.google.android.systemui.titan.DaggerTitanGlobalRootComponent.this
            android.content.Context r4 = r0.context
            javax.inject.Provider r5 = com.google.android.systemui.titan.DaggerTitanGlobalRootComponent.ABSENT_JDK_OPTIONAL_PROVIDER
            android.content.res.Resources r0 = r0.mainResources()
            r5 = 2131952138(0x7f13020a, float:1.954071E38)
            java.lang.String r0 = r0.getString(r5)
            android.content.ComponentName r0 = android.content.ComponentName.unflattenFromString(r0)
            java.lang.String r6 = "Cannot return null from a non-@Nullable @Provides method"
            java.util.Objects.requireNonNull(r0, r6)
            android.content.Intent r7 = new android.content.Intent
            java.lang.String r9 = "android.intent.action.DOCK_EVENT"
            r7.<init>(r9)
            r7.setComponent(r0)
            com.google.android.systemui.titan.DaggerTitanGlobalRootComponent$TitanSysUIComponentImpl r0 = com.google.android.systemui.titan.DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.this
            com.google.android.systemui.titan.DaggerTitanGlobalRootComponent r0 = com.google.android.systemui.titan.DaggerTitanGlobalRootComponent.this
            javax.inject.Provider<java.util.concurrent.Executor> r0 = r0.provideMainExecutorProvider
            java.lang.Object r0 = r0.mo144get()
            java.util.concurrent.Executor r0 = (java.util.concurrent.Executor) r0
            r3.<init>(r4, r7, r0)
            com.android.systemui.util.service.ObservableServiceConnection$Callback<android.os.IBinder> r0 = r12.callback
            r3.addCallback(r0)
            com.google.android.systemui.titan.DaggerTitanGlobalRootComponent$TitanSysUIComponentImpl r0 = com.google.android.systemui.titan.DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.this
            com.google.android.systemui.titan.DaggerTitanGlobalRootComponent r0 = com.google.android.systemui.titan.DaggerTitanGlobalRootComponent.this
            android.content.res.Resources r0 = r0.mainResources()
            r4 = 2131492887(0x7f0c0017, float:1.8609239E38)
            int r4 = r0.getInteger(r4)
            com.google.android.systemui.titan.DaggerTitanGlobalRootComponent$TitanSysUIComponentImpl r0 = com.google.android.systemui.titan.DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.this
            com.google.android.systemui.titan.DaggerTitanGlobalRootComponent r0 = com.google.android.systemui.titan.DaggerTitanGlobalRootComponent.this
            android.content.res.Resources r0 = r0.mainResources()
            r7 = 2131492888(0x7f0c0018, float:1.860924E38)
            int r7 = r0.getInteger(r7)
            com.google.android.systemui.titan.DaggerTitanGlobalRootComponent$TitanSysUIComponentImpl r0 = com.google.android.systemui.titan.DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.this
            com.google.android.systemui.titan.DaggerTitanGlobalRootComponent r0 = com.google.android.systemui.titan.DaggerTitanGlobalRootComponent.this
            android.content.res.Resources r0 = r0.mainResources()
            r9 = 2131492889(0x7f0c0019, float:1.8609243E38)
            int r9 = r0.getInteger(r9)
            com.google.android.systemui.titan.DaggerTitanGlobalRootComponent$TitanSysUIComponentImpl r0 = com.google.android.systemui.titan.DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.this
            com.google.android.systemui.titan.DaggerTitanGlobalRootComponent r0 = com.google.android.systemui.titan.DaggerTitanGlobalRootComponent.this
            android.content.res.Resources r0 = r0.mainResources()
            java.lang.String r0 = r0.getString(r5)
            android.content.ComponentName r0 = android.content.ComponentName.unflattenFromString(r0)
            java.util.Objects.requireNonNull(r0, r6)
            com.android.systemui.util.service.PackageObserver r10 = new com.android.systemui.util.service.PackageObserver
            com.google.android.systemui.titan.DaggerTitanGlobalRootComponent$TitanSysUIComponentImpl r12 = com.google.android.systemui.titan.DaggerTitanGlobalRootComponent.TitanSysUIComponentImpl.this
            com.google.android.systemui.titan.DaggerTitanGlobalRootComponent r12 = com.google.android.systemui.titan.DaggerTitanGlobalRootComponent.this
            android.content.Context r12 = r12.context
            r10.<init>(r12, r0)
            r0 = r8
            r5 = r7
            r6 = r9
            r7 = r10
            r0.<init>(r1, r2, r3, r4, r5, r6, r7)
            r11.mConnectionManager = r8
            r11.mDreamOverlayStateController = r13
            r11.mMediaShellComplication = r14
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.communal.dock.callbacks.mediashell.MediaShellCallback.<init>(com.google.android.systemui.communal.dock.callbacks.mediashell.dagger.MediaShellComponent$Factory, com.android.systemui.dreams.DreamOverlayStateController, com.google.android.systemui.communal.dock.callbacks.mediashell.MediaShellComplication):void");
    }
}

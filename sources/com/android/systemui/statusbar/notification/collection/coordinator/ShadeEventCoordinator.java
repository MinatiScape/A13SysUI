package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeRenderListListener;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.collection.render.NotifShadeEventSource;
import com.android.systemui.wmshell.WMShell$7$$ExternalSyntheticLambda2;
import com.android.systemui.wmshell.WMShell$8$$ExternalSyntheticLambda0;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
/* compiled from: ShadeEventCoordinator.kt */
/* loaded from: classes.dex */
public final class ShadeEventCoordinator implements Coordinator, NotifShadeEventSource {
    public boolean mEntryRemoved;
    public boolean mEntryRemovedByUser;
    public final ShadeEventCoordinatorLogger mLogger;
    public final Executor mMainExecutor;
    public final ShadeEventCoordinator$mNotifCollectionListener$1 mNotifCollectionListener = new NotifCollectionListener() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.ShadeEventCoordinator$mNotifCollectionListener$1
        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
        public final void onEntryRemoved(NotificationEntry notificationEntry, int i) {
            ShadeEventCoordinator shadeEventCoordinator = ShadeEventCoordinator.this;
            boolean z = true;
            shadeEventCoordinator.mEntryRemoved = true;
            if (!(i == 1 || i == 3 || i == 2)) {
                z = false;
            }
            shadeEventCoordinator.mEntryRemovedByUser = z;
        }
    };
    public Runnable mNotifRemovedByUserCallback;
    public Runnable mShadeEmptiedCallback;

    @Override // com.android.systemui.statusbar.notification.collection.coordinator.Coordinator
    public final void attach(NotifPipeline notifPipeline) {
        notifPipeline.addCollectionListener(this.mNotifCollectionListener);
        notifPipeline.addOnBeforeRenderListListener(new OnBeforeRenderListListener() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.ShadeEventCoordinator$attach$1
            @Override // com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeRenderListListener
            public final void onBeforeRenderList(List<? extends ListEntry> list) {
                ShadeEventCoordinator shadeEventCoordinator = ShadeEventCoordinator.this;
                Objects.requireNonNull(shadeEventCoordinator);
                LogLevel logLevel = LogLevel.DEBUG;
                if (shadeEventCoordinator.mEntryRemoved && list.isEmpty()) {
                    ShadeEventCoordinatorLogger shadeEventCoordinatorLogger = shadeEventCoordinator.mLogger;
                    Objects.requireNonNull(shadeEventCoordinatorLogger);
                    LogBuffer logBuffer = shadeEventCoordinatorLogger.buffer;
                    ShadeEventCoordinatorLogger$logShadeEmptied$2 shadeEventCoordinatorLogger$logShadeEmptied$2 = ShadeEventCoordinatorLogger$logShadeEmptied$2.INSTANCE;
                    Objects.requireNonNull(logBuffer);
                    if (!logBuffer.frozen) {
                        logBuffer.push(logBuffer.obtain("ShadeEventCoordinator", logLevel, shadeEventCoordinatorLogger$logShadeEmptied$2));
                    }
                    Runnable runnable = shadeEventCoordinator.mShadeEmptiedCallback;
                    if (runnable != null) {
                        shadeEventCoordinator.mMainExecutor.execute(runnable);
                    }
                }
                if (shadeEventCoordinator.mEntryRemoved && shadeEventCoordinator.mEntryRemovedByUser) {
                    ShadeEventCoordinatorLogger shadeEventCoordinatorLogger2 = shadeEventCoordinator.mLogger;
                    Objects.requireNonNull(shadeEventCoordinatorLogger2);
                    LogBuffer logBuffer2 = shadeEventCoordinatorLogger2.buffer;
                    ShadeEventCoordinatorLogger$logNotifRemovedByUser$2 shadeEventCoordinatorLogger$logNotifRemovedByUser$2 = ShadeEventCoordinatorLogger$logNotifRemovedByUser$2.INSTANCE;
                    Objects.requireNonNull(logBuffer2);
                    if (!logBuffer2.frozen) {
                        logBuffer2.push(logBuffer2.obtain("ShadeEventCoordinator", logLevel, shadeEventCoordinatorLogger$logNotifRemovedByUser$2));
                    }
                    Runnable runnable2 = shadeEventCoordinator.mNotifRemovedByUserCallback;
                    if (runnable2 != null) {
                        shadeEventCoordinator.mMainExecutor.execute(runnable2);
                    }
                }
                shadeEventCoordinator.mEntryRemoved = false;
                shadeEventCoordinator.mEntryRemovedByUser = false;
            }
        });
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NotifShadeEventSource
    public final void setNotifRemovedByUserCallback(WMShell$8$$ExternalSyntheticLambda0 wMShell$8$$ExternalSyntheticLambda0) {
        boolean z;
        if (this.mNotifRemovedByUserCallback == null) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            this.mNotifRemovedByUserCallback = wMShell$8$$ExternalSyntheticLambda0;
            return;
        }
        throw new IllegalStateException("mNotifRemovedByUserCallback already set".toString());
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NotifShadeEventSource
    public final void setShadeEmptiedCallback(WMShell$7$$ExternalSyntheticLambda2 wMShell$7$$ExternalSyntheticLambda2) {
        boolean z;
        if (this.mShadeEmptiedCallback == null) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            this.mShadeEmptiedCallback = wMShell$7$$ExternalSyntheticLambda2;
            return;
        }
        throw new IllegalStateException("mShadeEmptiedCallback already set".toString());
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.statusbar.notification.collection.coordinator.ShadeEventCoordinator$mNotifCollectionListener$1] */
    public ShadeEventCoordinator(Executor executor, ShadeEventCoordinatorLogger shadeEventCoordinatorLogger) {
        this.mMainExecutor = executor;
        this.mLogger = shadeEventCoordinatorLogger;
    }
}

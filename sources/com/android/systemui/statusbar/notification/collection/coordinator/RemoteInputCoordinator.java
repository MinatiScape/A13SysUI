package com.android.systemui.statusbar.notification.collection.coordinator;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Handler;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.RemoteInputController;
import com.android.systemui.statusbar.RemoteInputNotificationRebuilder;
import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.notification.collection.NotifCollection;
import com.android.systemui.statusbar.notification.collection.NotifCollection$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.notification.collection.NotifCollection$$ExternalSyntheticLambda2;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener;
import com.android.systemui.statusbar.notification.collection.notifcollection.NotifLifetimeExtender;
import com.android.systemui.statusbar.notification.collection.notifcollection.SelfTrackingLifetimeExtender;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.SetsKt__SetsKt;
/* compiled from: RemoteInputCoordinator.kt */
/* loaded from: classes.dex */
public final class RemoteInputCoordinator implements Coordinator, NotificationRemoteInputManager.RemoteInputListener, Dumpable {
    public final RemoteInputCoordinator$mCollectionListener$1 mCollectionListener = new NotifCollectionListener() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.RemoteInputCoordinator$mCollectionListener$1
        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
        public final void onEntryRemoved(NotificationEntry notificationEntry, int i) {
            boolean booleanValue;
            booleanValue = ((Boolean) RemoteInputCoordinatorKt.DEBUG$delegate.getValue()).booleanValue();
            if (booleanValue) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("mCollectionListener.onEntryRemoved(entry=");
                m.append(notificationEntry.mKey);
                m.append(')');
                Log.d("RemoteInputCoordinator", m.toString());
            }
            RemoteInputCoordinator.this.mSmartReplyController.stopSending(notificationEntry);
            if (i == 1 || i == 2) {
                NotificationRemoteInputManager notificationRemoteInputManager = RemoteInputCoordinator.this.mNotificationRemoteInputManager;
                Objects.requireNonNull(notificationRemoteInputManager);
                if (notificationRemoteInputManager.isRemoteInputActive(notificationEntry)) {
                    notificationEntry.mRemoteEditImeVisible = false;
                    notificationRemoteInputManager.mRemoteInputController.removeRemoteInput(notificationEntry, null);
                }
            }
        }

        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.NotifCollectionListener
        public final void onEntryUpdated(NotificationEntry notificationEntry, boolean z) {
            boolean booleanValue;
            booleanValue = ((Boolean) RemoteInputCoordinatorKt.DEBUG$delegate.getValue()).booleanValue();
            if (booleanValue) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("mCollectionListener.onEntryUpdated(entry=");
                m.append(notificationEntry.mKey);
                m.append(", fromSystem=");
                m.append(z);
                m.append(')');
                Log.d("RemoteInputCoordinator", m.toString());
            }
            if (z) {
                RemoteInputCoordinator.this.mSmartReplyController.stopSending(notificationEntry);
            }
        }
    };
    public final Handler mMainHandler;
    public NotifCollection$$ExternalSyntheticLambda0 mNotifUpdater;
    public final NotificationRemoteInputManager mNotificationRemoteInputManager;
    public final RemoteInputNotificationRebuilder mRebuilder;
    public final RemoteInputActiveExtender mRemoteInputActiveExtender;
    public final RemoteInputHistoryExtender mRemoteInputHistoryExtender;
    public final List<SelfTrackingLifetimeExtender> mRemoteInputLifetimeExtenders;
    public final SmartReplyController mSmartReplyController;
    public final SmartReplyHistoryExtender mSmartReplyHistoryExtender;

    /* compiled from: RemoteInputCoordinator.kt */
    /* loaded from: classes.dex */
    public final class RemoteInputActiveExtender extends SelfTrackingLifetimeExtender {
        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public RemoteInputActiveExtender() {
            /*
                r2 = this;
                com.android.systemui.statusbar.notification.collection.coordinator.RemoteInputCoordinator.this = r3
                boolean r0 = com.android.systemui.statusbar.notification.collection.coordinator.RemoteInputCoordinatorKt.access$getDEBUG()
                android.os.Handler r3 = r3.mMainHandler
                java.lang.String r1 = "RemoteInputActive"
                r2.<init>(r1, r0, r3)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.collection.coordinator.RemoteInputCoordinator.RemoteInputActiveExtender.<init>(com.android.systemui.statusbar.notification.collection.coordinator.RemoteInputCoordinator):void");
        }

        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.SelfTrackingLifetimeExtender
        public final boolean queryShouldExtendLifetime(NotificationEntry notificationEntry) {
            return RemoteInputCoordinator.this.mNotificationRemoteInputManager.isRemoteInputActive(notificationEntry);
        }
    }

    /* compiled from: RemoteInputCoordinator.kt */
    /* loaded from: classes.dex */
    public final class RemoteInputHistoryExtender extends SelfTrackingLifetimeExtender {
        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public RemoteInputHistoryExtender() {
            /*
                r2 = this;
                com.android.systemui.statusbar.notification.collection.coordinator.RemoteInputCoordinator.this = r3
                boolean r0 = com.android.systemui.statusbar.notification.collection.coordinator.RemoteInputCoordinatorKt.access$getDEBUG()
                android.os.Handler r3 = r3.mMainHandler
                java.lang.String r1 = "RemoteInputHistory"
                r2.<init>(r1, r0, r3)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.collection.coordinator.RemoteInputCoordinator.RemoteInputHistoryExtender.<init>(com.android.systemui.statusbar.notification.collection.coordinator.RemoteInputCoordinator):void");
        }

        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.SelfTrackingLifetimeExtender
        public final void onStartedLifetimeExtension(NotificationEntry notificationEntry) {
            RemoteInputNotificationRebuilder remoteInputNotificationRebuilder = RemoteInputCoordinator.this.mRebuilder;
            Objects.requireNonNull(remoteInputNotificationRebuilder);
            CharSequence charSequence = notificationEntry.remoteInputText;
            if (TextUtils.isEmpty(charSequence)) {
                charSequence = notificationEntry.remoteInputTextWhenReset;
            }
            StatusBarNotification rebuildWithRemoteInputInserted = remoteInputNotificationRebuilder.rebuildWithRemoteInputInserted(notificationEntry, charSequence, false, notificationEntry.remoteInputMimeType, notificationEntry.remoteInputUri);
            notificationEntry.lastRemoteInputSent = -2000L;
            NotifCollection$$ExternalSyntheticLambda0 notifCollection$$ExternalSyntheticLambda0 = null;
            notificationEntry.remoteInputTextWhenReset = null;
            NotifCollection$$ExternalSyntheticLambda0 notifCollection$$ExternalSyntheticLambda02 = RemoteInputCoordinator.this.mNotifUpdater;
            if (notifCollection$$ExternalSyntheticLambda02 != null) {
                notifCollection$$ExternalSyntheticLambda0 = notifCollection$$ExternalSyntheticLambda02;
            }
            notifCollection$$ExternalSyntheticLambda0.onInternalNotificationUpdate(rebuildWithRemoteInputInserted, "Extending lifetime of notification with remote input");
        }

        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.SelfTrackingLifetimeExtender
        public final boolean queryShouldExtendLifetime(NotificationEntry notificationEntry) {
            return RemoteInputCoordinator.this.mNotificationRemoteInputManager.shouldKeepForRemoteInputHistory(notificationEntry);
        }
    }

    /* compiled from: RemoteInputCoordinator.kt */
    /* loaded from: classes.dex */
    public final class SmartReplyHistoryExtender extends SelfTrackingLifetimeExtender {
        /* JADX WARN: Illegal instructions before constructor call */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public SmartReplyHistoryExtender() {
            /*
                r2 = this;
                com.android.systemui.statusbar.notification.collection.coordinator.RemoteInputCoordinator.this = r3
                boolean r0 = com.android.systemui.statusbar.notification.collection.coordinator.RemoteInputCoordinatorKt.access$getDEBUG()
                android.os.Handler r3 = r3.mMainHandler
                java.lang.String r1 = "SmartReplyHistory"
                r2.<init>(r1, r0, r3)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.collection.coordinator.RemoteInputCoordinator.SmartReplyHistoryExtender.<init>(com.android.systemui.statusbar.notification.collection.coordinator.RemoteInputCoordinator):void");
        }

        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.SelfTrackingLifetimeExtender
        public final void onCanceledLifetimeExtension(NotificationEntry notificationEntry) {
            RemoteInputCoordinator.this.mSmartReplyController.stopSending(notificationEntry);
        }

        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.SelfTrackingLifetimeExtender
        public final void onStartedLifetimeExtension(NotificationEntry notificationEntry) {
            RemoteInputNotificationRebuilder remoteInputNotificationRebuilder = RemoteInputCoordinator.this.mRebuilder;
            Objects.requireNonNull(remoteInputNotificationRebuilder);
            StatusBarNotification rebuildWithRemoteInputInserted = remoteInputNotificationRebuilder.rebuildWithRemoteInputInserted(notificationEntry, null, false, null, null);
            RemoteInputCoordinator.this.mSmartReplyController.stopSending(notificationEntry);
            NotifCollection$$ExternalSyntheticLambda0 notifCollection$$ExternalSyntheticLambda0 = RemoteInputCoordinator.this.mNotifUpdater;
            if (notifCollection$$ExternalSyntheticLambda0 == null) {
                notifCollection$$ExternalSyntheticLambda0 = null;
            }
            notifCollection$$ExternalSyntheticLambda0.onInternalNotificationUpdate(rebuildWithRemoteInputInserted, "Extending lifetime of notification with smart reply");
        }

        @Override // com.android.systemui.statusbar.notification.collection.notifcollection.SelfTrackingLifetimeExtender
        public final boolean queryShouldExtendLifetime(NotificationEntry notificationEntry) {
            return RemoteInputCoordinator.this.mNotificationRemoteInputManager.shouldKeepForSmartReplyHistory(notificationEntry);
        }
    }

    public static /* synthetic */ void getMRemoteInputActiveExtender$annotations() {
    }

    public static /* synthetic */ void getMRemoteInputHistoryExtender$annotations() {
    }

    public static /* synthetic */ void getMSmartReplyHistoryExtender$annotations() {
    }

    @Override // com.android.systemui.statusbar.notification.collection.coordinator.Coordinator
    public final void attach(NotifPipeline notifPipeline) {
        NotificationRemoteInputManager notificationRemoteInputManager = this.mNotificationRemoteInputManager;
        Objects.requireNonNull(notificationRemoteInputManager);
        if (notificationRemoteInputManager.mNotifPipelineFlags.isNewPipelineEnabled()) {
            if (notificationRemoteInputManager.mRemoteInputListener == null) {
                notificationRemoteInputManager.mRemoteInputListener = this;
                RemoteInputController remoteInputController = notificationRemoteInputManager.mRemoteInputController;
                if (remoteInputController != null) {
                    setRemoteInputController(remoteInputController);
                }
            } else {
                throw new IllegalStateException("mRemoteInputListener is already set");
            }
        }
        for (SelfTrackingLifetimeExtender selfTrackingLifetimeExtender : this.mRemoteInputLifetimeExtenders) {
            notifPipeline.addNotificationLifetimeExtender(selfTrackingLifetimeExtender);
        }
        NotifCollection notifCollection = notifPipeline.mNotifCollection;
        Objects.requireNonNull(notifCollection);
        this.mNotifUpdater = new NotifCollection$$ExternalSyntheticLambda0(notifCollection);
        notifPipeline.addCollectionListener(this.mCollectionListener);
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        for (SelfTrackingLifetimeExtender selfTrackingLifetimeExtender : this.mRemoteInputLifetimeExtenders) {
            selfTrackingLifetimeExtender.dump(fileDescriptor, printWriter, strArr);
        }
    }

    @Override // com.android.systemui.statusbar.NotificationRemoteInputManager.RemoteInputListener
    public final boolean isNotificationKeptForRemoteInputHistory(String str) {
        if (this.mRemoteInputHistoryExtender.isExtending(str) || this.mSmartReplyHistoryExtender.isExtending(str)) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.statusbar.NotificationRemoteInputManager.RemoteInputListener
    public final void onPanelCollapsed() {
        RemoteInputActiveExtender remoteInputActiveExtender = this.mRemoteInputActiveExtender;
        Objects.requireNonNull(remoteInputActiveExtender);
        List<NotificationEntry> list = CollectionsKt___CollectionsKt.toList(remoteInputActiveExtender.mEntriesExtended.values());
        if (remoteInputActiveExtender.debug) {
            String str = remoteInputActiveExtender.tag;
            Log.d(str, remoteInputActiveExtender.name + ".endAllLifetimeExtensions() entries=" + list);
        }
        remoteInputActiveExtender.mEntriesExtended.clear();
        remoteInputActiveExtender.warnIfEnding();
        remoteInputActiveExtender.mEnding = true;
        for (NotificationEntry notificationEntry : list) {
            NotifLifetimeExtender.OnEndLifetimeExtensionCallback onEndLifetimeExtensionCallback = remoteInputActiveExtender.mCallback;
            if (onEndLifetimeExtensionCallback == null) {
                onEndLifetimeExtensionCallback = null;
            }
            ((NotifCollection$$ExternalSyntheticLambda2) onEndLifetimeExtensionCallback).onEndLifetimeExtension(remoteInputActiveExtender, notificationEntry);
        }
        remoteInputActiveExtender.mEnding = false;
    }

    @Override // com.android.systemui.statusbar.NotificationRemoteInputManager.RemoteInputListener
    public final void setRemoteInputController(RemoteInputController remoteInputController) {
        SmartReplyController smartReplyController = this.mSmartReplyController;
        SmartReplyController.Callback remoteInputCoordinator$setRemoteInputController$1 = new SmartReplyController.Callback() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.RemoteInputCoordinator$setRemoteInputController$1
            @Override // com.android.systemui.statusbar.SmartReplyController.Callback
            public final void onSmartReplySent(NotificationEntry notificationEntry, CharSequence charSequence) {
                boolean booleanValue;
                RemoteInputCoordinator remoteInputCoordinator = RemoteInputCoordinator.this;
                Objects.requireNonNull(remoteInputCoordinator);
                booleanValue = ((Boolean) RemoteInputCoordinatorKt.DEBUG$delegate.getValue()).booleanValue();
                if (booleanValue) {
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("onSmartReplySent(entry=");
                    m.append(notificationEntry.mKey);
                    m.append(')');
                    Log.d("RemoteInputCoordinator", m.toString());
                }
                RemoteInputNotificationRebuilder remoteInputNotificationRebuilder = remoteInputCoordinator.mRebuilder;
                Objects.requireNonNull(remoteInputNotificationRebuilder);
                StatusBarNotification rebuildWithRemoteInputInserted = remoteInputNotificationRebuilder.rebuildWithRemoteInputInserted(notificationEntry, charSequence, true, null, null);
                NotifCollection$$ExternalSyntheticLambda0 notifCollection$$ExternalSyntheticLambda0 = remoteInputCoordinator.mNotifUpdater;
                if (notifCollection$$ExternalSyntheticLambda0 == null) {
                    notifCollection$$ExternalSyntheticLambda0 = null;
                }
                notifCollection$$ExternalSyntheticLambda0.onInternalNotificationUpdate(rebuildWithRemoteInputInserted, "Adding smart reply spinner for sent");
                remoteInputCoordinator.mRemoteInputActiveExtender.endLifetimeExtensionAfterDelay(notificationEntry.mKey, 500L);
            }
        };
        Objects.requireNonNull(smartReplyController);
        smartReplyController.mCallback = remoteInputCoordinator$setRemoteInputController$1;
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [com.android.systemui.statusbar.notification.collection.coordinator.RemoteInputCoordinator$mCollectionListener$1] */
    public RemoteInputCoordinator(DumpManager dumpManager, RemoteInputNotificationRebuilder remoteInputNotificationRebuilder, NotificationRemoteInputManager notificationRemoteInputManager, Handler handler, SmartReplyController smartReplyController) {
        this.mRebuilder = remoteInputNotificationRebuilder;
        this.mNotificationRemoteInputManager = notificationRemoteInputManager;
        this.mMainHandler = handler;
        this.mSmartReplyController = smartReplyController;
        RemoteInputHistoryExtender remoteInputHistoryExtender = new RemoteInputHistoryExtender();
        this.mRemoteInputHistoryExtender = remoteInputHistoryExtender;
        SmartReplyHistoryExtender smartReplyHistoryExtender = new SmartReplyHistoryExtender();
        this.mSmartReplyHistoryExtender = smartReplyHistoryExtender;
        RemoteInputActiveExtender remoteInputActiveExtender = new RemoteInputActiveExtender();
        this.mRemoteInputActiveExtender = remoteInputActiveExtender;
        this.mRemoteInputLifetimeExtenders = SetsKt__SetsKt.listOf(remoteInputHistoryExtender, smartReplyHistoryExtender, remoteInputActiveExtender);
        dumpManager.registerDumpable(this);
    }

    @Override // com.android.systemui.statusbar.NotificationRemoteInputManager.RemoteInputListener
    public final void onRemoteInputSent(NotificationEntry notificationEntry) {
        boolean booleanValue;
        booleanValue = ((Boolean) RemoteInputCoordinatorKt.DEBUG$delegate.getValue()).booleanValue();
        if (booleanValue) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("onRemoteInputSent(entry=");
            m.append(notificationEntry.mKey);
            m.append(')');
            Log.d("RemoteInputCoordinator", m.toString());
        }
        this.mRemoteInputHistoryExtender.endLifetimeExtension(notificationEntry.mKey);
        this.mSmartReplyHistoryExtender.endLifetimeExtension(notificationEntry.mKey);
        this.mRemoteInputActiveExtender.endLifetimeExtensionAfterDelay(notificationEntry.mKey, 500L);
    }

    @Override // com.android.systemui.statusbar.NotificationRemoteInputManager.RemoteInputListener
    public final void releaseNotificationIfKeptForRemoteInputHistory(NotificationEntry notificationEntry) {
        boolean booleanValue;
        booleanValue = ((Boolean) RemoteInputCoordinatorKt.DEBUG$delegate.getValue()).booleanValue();
        if (booleanValue) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("releaseNotificationIfKeptForRemoteInputHistory(entry=");
            m.append(notificationEntry.mKey);
            m.append(')');
            Log.d("RemoteInputCoordinator", m.toString());
        }
        this.mRemoteInputHistoryExtender.endLifetimeExtensionAfterDelay(notificationEntry.mKey, 200L);
        this.mSmartReplyHistoryExtender.endLifetimeExtensionAfterDelay(notificationEntry.mKey, 200L);
        this.mRemoteInputActiveExtender.endLifetimeExtensionAfterDelay(notificationEntry.mKey, 200L);
    }
}

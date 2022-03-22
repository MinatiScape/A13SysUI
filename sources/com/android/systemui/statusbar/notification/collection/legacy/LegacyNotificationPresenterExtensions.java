package com.android.systemui.statusbar.notification.collection.legacy;

import com.android.systemui.statusbar.notification.NotificationEntryListener;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.render.NotifShadeEventSource;
import com.android.systemui.wmshell.WMShell$7$$ExternalSyntheticLambda2;
import com.android.systemui.wmshell.WMShell$8$$ExternalSyntheticLambda0;
import java.util.Objects;
/* loaded from: classes.dex */
public final class LegacyNotificationPresenterExtensions implements NotifShadeEventSource {
    public boolean mEntryListenerAdded;
    public final NotificationEntryManager mEntryManager;
    public Runnable mNotifRemovedByUserCallback;
    public Runnable mShadeEmptiedCallback;

    /* renamed from: com.android.systemui.statusbar.notification.collection.legacy.LegacyNotificationPresenterExtensions$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass1 implements NotificationEntryListener {
        public AnonymousClass1() {
        }

        @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
        public final void onEntryRemoved(NotificationEntry notificationEntry, boolean z) {
            Runnable runnable;
            boolean z2;
            Runnable runnable2;
            if (notificationEntry.mSbn != null) {
                NotificationEntryManager notificationEntryManager = LegacyNotificationPresenterExtensions.this.mEntryManager;
                Objects.requireNonNull(notificationEntryManager);
                notificationEntryManager.mNotifPipelineFlags.checkLegacyPipelineEnabled();
                if (notificationEntryManager.mReadOnlyNotifications.size() != 0) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (!z2 && (runnable2 = LegacyNotificationPresenterExtensions.this.mShadeEmptiedCallback) != null) {
                    runnable2.run();
                }
            }
            if (z && (runnable = LegacyNotificationPresenterExtensions.this.mNotifRemovedByUserCallback) != null) {
                runnable.run();
            }
        }
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NotifShadeEventSource
    public final void setNotifRemovedByUserCallback(WMShell$8$$ExternalSyntheticLambda0 wMShell$8$$ExternalSyntheticLambda0) {
        if (this.mNotifRemovedByUserCallback == null) {
            this.mNotifRemovedByUserCallback = wMShell$8$$ExternalSyntheticLambda0;
            if (!this.mEntryListenerAdded) {
                this.mEntryListenerAdded = true;
                this.mEntryManager.addNotificationEntryListener(new AnonymousClass1());
                return;
            }
            return;
        }
        throw new IllegalStateException("mNotifRemovedByUserCallback already set");
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NotifShadeEventSource
    public final void setShadeEmptiedCallback(WMShell$7$$ExternalSyntheticLambda2 wMShell$7$$ExternalSyntheticLambda2) {
        if (this.mShadeEmptiedCallback == null) {
            this.mShadeEmptiedCallback = wMShell$7$$ExternalSyntheticLambda2;
            if (!this.mEntryListenerAdded) {
                this.mEntryListenerAdded = true;
                this.mEntryManager.addNotificationEntryListener(new AnonymousClass1());
                return;
            }
            return;
        }
        throw new IllegalStateException("mShadeEmptiedCallback already set");
    }

    public LegacyNotificationPresenterExtensions(NotificationEntryManager notificationEntryManager) {
        this.mEntryManager = notificationEntryManager;
    }
}

package com.android.systemui.statusbar.notification.collection.coordinator;

import android.os.Trace;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.NotifSection;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnAfterRenderListListener;
import com.android.systemui.statusbar.notification.collection.render.NotifStackController;
import com.android.systemui.statusbar.notification.collection.render.NotifStats;
import com.android.systemui.statusbar.notification.collection.render.RenderStageManager;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.NotificationIconAreaController;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: StackCoordinator.kt */
/* loaded from: classes.dex */
public final class StackCoordinator implements Coordinator {
    public final NotificationIconAreaController notificationIconAreaController;

    @Override // com.android.systemui.statusbar.notification.collection.coordinator.Coordinator
    public final void attach(NotifPipeline notifPipeline) {
        OnAfterRenderListListener stackCoordinator$attach$1 = new OnAfterRenderListListener() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.StackCoordinator$attach$1
            @Override // com.android.systemui.statusbar.notification.collection.listbuilder.OnAfterRenderListListener
            public final void onAfterRenderList(List<? extends ListEntry> list, NotifStackController notifStackController) {
                StackCoordinator stackCoordinator = StackCoordinator.this;
                Objects.requireNonNull(stackCoordinator);
                Trace.beginSection("StackCoordinator.onAfterRenderList");
                try {
                    NotifStats calculateNotifStats = StackCoordinator.calculateNotifStats(list);
                    NotificationStackScrollLayoutController.NotifStackControllerImpl notifStackControllerImpl = (NotificationStackScrollLayoutController.NotifStackControllerImpl) notifStackController;
                    Objects.requireNonNull(notifStackControllerImpl);
                    NotificationStackScrollLayoutController notificationStackScrollLayoutController = NotificationStackScrollLayoutController.this;
                    notificationStackScrollLayoutController.mNotifStats = calculateNotifStats;
                    notificationStackScrollLayoutController.updateFooter();
                    NotificationStackScrollLayoutController.this.updateShowEmptyShadeView();
                    stackCoordinator.notificationIconAreaController.updateNotificationIcons(list);
                } finally {
                    Trace.endSection();
                }
            }
        };
        Objects.requireNonNull(notifPipeline);
        RenderStageManager renderStageManager = notifPipeline.mRenderStageManager;
        Objects.requireNonNull(renderStageManager);
        renderStageManager.onAfterRenderListListeners.add(stackCoordinator$attach$1);
    }

    public StackCoordinator(NotificationIconAreaController notificationIconAreaController) {
        this.notificationIconAreaController = notificationIconAreaController;
    }

    public static NotifStats calculateNotifStats(List list) {
        boolean z;
        Iterator it = list.iterator();
        boolean z2 = false;
        boolean z3 = false;
        boolean z4 = false;
        boolean z5 = false;
        while (it.hasNext()) {
            ListEntry listEntry = (ListEntry) it.next();
            NotifSection section = listEntry.getSection();
            if (section != null) {
                NotificationEntry representativeEntry = listEntry.getRepresentativeEntry();
                if (representativeEntry != null) {
                    if (section.bucket == 6) {
                        z = true;
                    } else {
                        z = false;
                    }
                    boolean isClearable = representativeEntry.isClearable();
                    if (z && isClearable) {
                        z5 = true;
                    } else if (z && !isClearable) {
                        z4 = true;
                    } else if (!z && isClearable) {
                        z3 = true;
                    } else if (!z && !isClearable) {
                        z2 = true;
                    }
                } else {
                    throw new IllegalStateException(Intrinsics.stringPlus("Null notif entry for ", listEntry.getKey()).toString());
                }
            } else {
                throw new IllegalStateException(Intrinsics.stringPlus("Null section for ", listEntry.getKey()).toString());
            }
        }
        return new NotifStats(list.size(), z2, z3, z4, z5);
    }
}

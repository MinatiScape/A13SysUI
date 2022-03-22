package com.android.systemui.statusbar.notification.collection.render;

import android.content.Context;
import android.os.Trace;
import android.view.View;
import com.android.systemui.statusbar.notification.NotificationSectionsFeatureManager;
import com.android.systemui.statusbar.notification.SectionHeaderVisibilityProvider;
import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import java.util.List;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ShadeViewManager.kt */
/* loaded from: classes.dex */
public final class ShadeViewManager {
    public final RootNodeController rootController;
    public final NodeSpecBuilder specBuilder;
    public final NotifStackController stackController;
    public final NotifViewBarn viewBarn;
    public final ShadeViewDiffer viewDiffer;
    public final ShadeViewManager$viewRenderer$1 viewRenderer = new NotifViewRenderer() { // from class: com.android.systemui.statusbar.notification.collection.render.ShadeViewManager$viewRenderer$1
        @Override // com.android.systemui.statusbar.notification.collection.render.NotifViewRenderer
        public final void onDispatchComplete() {
        }

        @Override // com.android.systemui.statusbar.notification.collection.render.NotifViewRenderer
        public final NotifViewController getGroupController(GroupEntry groupEntry) {
            NotifViewBarn notifViewBarn = ShadeViewManager.this.viewBarn;
            NotificationEntry notificationEntry = groupEntry.mSummary;
            if (notificationEntry != null) {
                Objects.requireNonNull(notifViewBarn);
                NotifViewController notifViewController = (NotifViewController) notifViewBarn.rowMap.get(notificationEntry.mKey);
                if (notifViewController != null) {
                    return notifViewController;
                }
                throw new IllegalStateException(Intrinsics.stringPlus("No view has been registered for entry: ", notificationEntry.mKey).toString());
            }
            throw new IllegalStateException(Intrinsics.stringPlus("No Summary: ", groupEntry).toString());
        }

        @Override // com.android.systemui.statusbar.notification.collection.render.NotifViewRenderer
        public final NotifViewController getRowController(NotificationEntry notificationEntry) {
            NotifViewBarn notifViewBarn = ShadeViewManager.this.viewBarn;
            Objects.requireNonNull(notifViewBarn);
            NotifViewController notifViewController = (NotifViewController) notifViewBarn.rowMap.get(notificationEntry.mKey);
            if (notifViewController != null) {
                return notifViewController;
            }
            throw new IllegalStateException(Intrinsics.stringPlus("No view has been registered for entry: ", notificationEntry.mKey).toString());
        }

        @Override // com.android.systemui.statusbar.notification.collection.render.NotifViewRenderer
        public final NotifStackController getStackController() {
            return ShadeViewManager.this.stackController;
        }

        @Override // com.android.systemui.statusbar.notification.collection.render.NotifViewRenderer
        public final void onRenderList(List<? extends ListEntry> list) {
            ShadeViewManager shadeViewManager = ShadeViewManager.this;
            Trace.beginSection("ShadeViewManager.onRenderList");
            try {
                shadeViewManager.viewDiffer.applySpec(shadeViewManager.specBuilder.buildNodeSpec(shadeViewManager.rootController, list));
            } finally {
                Trace.endSection();
            }
        }
    };

    /* JADX WARN: Type inference failed for: r2v3, types: [com.android.systemui.statusbar.notification.collection.render.ShadeViewManager$viewRenderer$1] */
    public ShadeViewManager(Context context, NotificationStackScrollLayoutController.NotificationListContainerImpl notificationListContainerImpl, NotificationStackScrollLayoutController.NotifStackControllerImpl notifStackControllerImpl, MediaContainerController mediaContainerController, NotificationSectionsFeatureManager notificationSectionsFeatureManager, SectionHeaderVisibilityProvider sectionHeaderVisibilityProvider, ShadeViewDifferLogger shadeViewDifferLogger, NotifViewBarn notifViewBarn) {
        this.stackController = notifStackControllerImpl;
        this.viewBarn = notifViewBarn;
        RootNodeController rootNodeController = new RootNodeController(notificationListContainerImpl, new View(context));
        this.rootController = rootNodeController;
        this.specBuilder = new NodeSpecBuilder(mediaContainerController, notificationSectionsFeatureManager, sectionHeaderVisibilityProvider, notifViewBarn);
        this.viewDiffer = new ShadeViewDiffer(rootNodeController, shadeViewDifferLogger);
    }
}

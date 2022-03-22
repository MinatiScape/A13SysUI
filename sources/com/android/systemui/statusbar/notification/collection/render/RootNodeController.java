package com.android.systemui.statusbar.notification.collection.render;

import android.view.View;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import java.util.Objects;
/* compiled from: RootNodeController.kt */
/* loaded from: classes.dex */
public final class RootNodeController implements NodeController {
    public final NotificationListContainer listContainer;
    public final View view;

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public final String getNodeLabel() {
        return "<root>";
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public final void onViewAdded() {
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public final void onViewMoved() {
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public final void onViewRemoved() {
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public final void addChildAt(NodeController nodeController, int i) {
        ExpandableNotificationRow expandableNotificationRow;
        this.listContainer.addContainerViewAt(nodeController.getView(), i);
        Objects.requireNonNull(this.listContainer);
        View view = nodeController.getView();
        if (view instanceof ExpandableNotificationRow) {
            expandableNotificationRow = (ExpandableNotificationRow) view;
        } else {
            expandableNotificationRow = null;
        }
        if (expandableNotificationRow != null) {
            expandableNotificationRow.mChangingPosition = false;
        }
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public final View getChildAt(int i) {
        return this.listContainer.getContainerChildAt(i);
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public final int getChildCount() {
        return this.listContainer.getContainerChildCount();
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public final void moveChildTo(NodeController nodeController, int i) {
        this.listContainer.changeViewPosition((ExpandableView) nodeController.getView(), i);
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public final void removeChild(NodeController nodeController, boolean z) {
        ExpandableNotificationRow expandableNotificationRow;
        if (z) {
            this.listContainer.setChildTransferInProgress(true);
            View view = nodeController.getView();
            if (view instanceof ExpandableNotificationRow) {
                expandableNotificationRow = (ExpandableNotificationRow) view;
            } else {
                expandableNotificationRow = null;
            }
            if (expandableNotificationRow != null) {
                expandableNotificationRow.mChangingPosition = true;
            }
        }
        this.listContainer.removeContainerView(nodeController.getView());
        if (z) {
            this.listContainer.setChildTransferInProgress(false);
        }
    }

    public RootNodeController(NotificationStackScrollLayoutController.NotificationListContainerImpl notificationListContainerImpl, View view) {
        this.listContainer = notificationListContainerImpl;
        this.view = view;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public final View getView() {
        return this.view;
    }
}

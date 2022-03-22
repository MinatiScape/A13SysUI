package com.android.systemui.statusbar.notification;

import android.view.View;
import android.view.ViewGroup;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
/* loaded from: classes.dex */
public final class AboveShelfObserver implements AboveShelfChangedListener {
    public boolean mHasViewsAboveShelf = false;
    public final ViewGroup mHostLayout;
    public HasViewAboveShelfChangedListener mListener;

    /* loaded from: classes.dex */
    public interface HasViewAboveShelfChangedListener {
        void onHasViewsAboveShelfChanged();
    }

    public final void onAboveShelfStateChanged(boolean z) {
        ViewGroup viewGroup;
        if (!z && (viewGroup = this.mHostLayout) != null) {
            int childCount = viewGroup.getChildCount();
            int i = 0;
            while (true) {
                if (i >= childCount) {
                    break;
                }
                View childAt = this.mHostLayout.getChildAt(i);
                if ((childAt instanceof ExpandableNotificationRow) && ((ExpandableNotificationRow) childAt).isAboveShelf()) {
                    z = true;
                    break;
                }
                i++;
            }
        }
        if (this.mHasViewsAboveShelf != z) {
            this.mHasViewsAboveShelf = z;
            HasViewAboveShelfChangedListener hasViewAboveShelfChangedListener = this.mListener;
            if (hasViewAboveShelfChangedListener != null) {
                hasViewAboveShelfChangedListener.onHasViewsAboveShelfChanged();
            }
        }
    }

    public AboveShelfObserver(NotificationStackScrollLayout notificationStackScrollLayout) {
        this.mHostLayout = notificationStackScrollLayout;
    }

    @VisibleForTesting
    public boolean hasViewsAboveShelf() {
        return this.mHasViewsAboveShelf;
    }
}

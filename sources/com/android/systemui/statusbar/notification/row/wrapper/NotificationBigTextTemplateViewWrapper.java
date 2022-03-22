package com.android.systemui.statusbar.notification.row.wrapper;

import android.content.Context;
import android.view.View;
import com.android.internal.widget.ImageFloatingTextView;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import java.util.Objects;
/* loaded from: classes.dex */
public final class NotificationBigTextTemplateViewWrapper extends NotificationTemplateViewWrapper {
    public ImageFloatingTextView mBigtext;

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationTemplateViewWrapper, com.android.systemui.statusbar.notification.row.wrapper.NotificationHeaderViewWrapper, com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper
    public final void onContentUpdated(ExpandableNotificationRow expandableNotificationRow) {
        Objects.requireNonNull(expandableNotificationRow);
        Objects.requireNonNull(expandableNotificationRow.mEntry);
        this.mBigtext = this.mView.findViewById(16908820);
        super.onContentUpdated(expandableNotificationRow);
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationTemplateViewWrapper, com.android.systemui.statusbar.notification.row.wrapper.NotificationHeaderViewWrapper
    public final void updateTransformedTypes() {
        super.updateTransformedTypes();
        View view = this.mBigtext;
        if (view != null) {
            this.mTransformationHelper.addTransformedView(2, view);
        }
    }

    public NotificationBigTextTemplateViewWrapper(Context context, View view, ExpandableNotificationRow expandableNotificationRow) {
        super(context, view, expandableNotificationRow);
    }
}

package com.android.systemui.statusbar.notification.row.wrapper;

import android.content.Context;
import android.view.View;
import com.android.internal.widget.CachingIconView;
import com.android.internal.widget.CallLayout;
import com.android.systemui.R$array;
import com.android.systemui.statusbar.notification.NotificationFadeAware;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
/* compiled from: NotificationCallTemplateViewWrapper.kt */
/* loaded from: classes.dex */
public final class NotificationCallTemplateViewWrapper extends NotificationTemplateViewWrapper {
    public View appName;
    public final CallLayout callLayout;
    public View conversationBadgeBg;
    public View conversationIconContainer;
    public CachingIconView conversationIconView;
    public View conversationTitleView;
    public View expandBtn;
    public final int minHeightWithActions;

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationTemplateViewWrapper, com.android.systemui.statusbar.notification.row.wrapper.NotificationHeaderViewWrapper, com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper
    public final void onContentUpdated(ExpandableNotificationRow expandableNotificationRow) {
        CallLayout callLayout = this.callLayout;
        this.conversationIconContainer = callLayout.requireViewById(16908925);
        this.conversationIconView = callLayout.requireViewById(16908921);
        this.conversationBadgeBg = callLayout.requireViewById(16908923);
        this.expandBtn = callLayout.requireViewById(16908980);
        this.appName = callLayout.requireViewById(16908782);
        this.conversationTitleView = callLayout.requireViewById(16908927);
        super.onContentUpdated(expandableNotificationRow);
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper
    public final void setNotificationFaded(boolean z) {
        View view = this.expandBtn;
        View view2 = null;
        if (view == null) {
            view = null;
        }
        NotificationFadeAware.setLayerTypeForFaded(view, z);
        View view3 = this.conversationIconContainer;
        if (view3 != null) {
            view2 = view3;
        }
        NotificationFadeAware.setLayerTypeForFaded(view2, z);
    }

    public NotificationCallTemplateViewWrapper(Context context, View view, ExpandableNotificationRow expandableNotificationRow) {
        super(context, view, expandableNotificationRow);
        this.minHeightWithActions = R$array.getFontScaledHeight(context, 2131166652);
        this.callLayout = (CallLayout) view;
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationTemplateViewWrapper, com.android.systemui.statusbar.notification.row.wrapper.NotificationHeaderViewWrapper
    public final void updateTransformedTypes() {
        super.updateTransformedTypes();
        View[] viewArr = new View[2];
        View view = this.appName;
        View view2 = null;
        if (view == null) {
            view = null;
        }
        viewArr[0] = view;
        View view3 = this.conversationTitleView;
        if (view3 == null) {
            view3 = null;
        }
        viewArr[1] = view3;
        addTransformedViews(viewArr);
        View[] viewArr2 = new View[3];
        CachingIconView cachingIconView = this.conversationIconView;
        if (cachingIconView == null) {
            cachingIconView = null;
        }
        viewArr2[0] = cachingIconView;
        View view4 = this.conversationBadgeBg;
        if (view4 == null) {
            view4 = null;
        }
        viewArr2[1] = view4;
        View view5 = this.expandBtn;
        if (view5 != null) {
            view2 = view5;
        }
        viewArr2[2] = view2;
        addViewsTransformingToSimilar(viewArr2);
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper
    public final int getMinLayoutHeight() {
        return this.minHeightWithActions;
    }
}

package com.android.systemui.statusbar.notification.row.wrapper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.widget.CachingIconView;
import com.android.internal.widget.ConversationLayout;
import com.android.internal.widget.MessagingLinearLayout;
import com.android.systemui.R$array;
import com.android.systemui.statusbar.ViewTransformationHelper;
import com.android.systemui.statusbar.notification.NotificationFadeAware;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.wrapper.NotificationMessagingTemplateViewWrapper;
import java.util.Objects;
/* compiled from: NotificationConversationTemplateViewWrapper.kt */
/* loaded from: classes.dex */
public final class NotificationConversationTemplateViewWrapper extends NotificationTemplateViewWrapper {
    public View appName;
    public View conversationBadgeBg;
    public View conversationIconContainer;
    public CachingIconView conversationIconView;
    public final ConversationLayout conversationLayout;
    public View conversationTitleView;
    public View expandBtn;
    public View expandBtnContainer;
    public View facePileBottom;
    public View facePileBottomBg;
    public View facePileTop;
    public ViewGroup imageMessageContainer;
    public View importanceRing;
    public MessagingLinearLayout messagingLinearLayout;
    public final int minHeightWithActions;

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper
    public final int getMinLayoutHeight() {
        View view = this.mActionsContainer;
        if (view == null || view.getVisibility() == 8) {
            return 0;
        }
        return this.minHeightWithActions;
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationHeaderViewWrapper, com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper
    public final View getShelfTransformationTarget() {
        if (!this.conversationLayout.isImportantConversation()) {
            return this.mIcon;
        }
        View view = this.conversationIconView;
        if (view == null) {
            view = null;
        }
        if (view.getVisibility() == 8) {
            return this.mIcon;
        }
        CachingIconView cachingIconView = this.conversationIconView;
        if (cachingIconView == null) {
            return null;
        }
        return cachingIconView;
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationTemplateViewWrapper, com.android.systemui.statusbar.notification.row.wrapper.NotificationHeaderViewWrapper, com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper
    public final void onContentUpdated(ExpandableNotificationRow expandableNotificationRow) {
        this.messagingLinearLayout = this.conversationLayout.getMessagingLinearLayout();
        this.imageMessageContainer = this.conversationLayout.getImageMessageContainer();
        ConversationLayout conversationLayout = this.conversationLayout;
        this.conversationIconContainer = conversationLayout.requireViewById(16908925);
        this.conversationIconView = conversationLayout.requireViewById(16908921);
        this.conversationBadgeBg = conversationLayout.requireViewById(16908923);
        this.expandBtn = conversationLayout.requireViewById(16908980);
        this.expandBtnContainer = conversationLayout.requireViewById(16908982);
        this.importanceRing = conversationLayout.requireViewById(16908924);
        this.appName = conversationLayout.requireViewById(16908782);
        this.conversationTitleView = conversationLayout.requireViewById(16908927);
        this.facePileTop = conversationLayout.findViewById(16908919);
        this.facePileBottom = conversationLayout.findViewById(16908917);
        this.facePileBottomBg = conversationLayout.findViewById(16908918);
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

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper
    public final void setRemoteInputVisible(boolean z) {
        this.conversationLayout.showHistoricMessages(z);
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationHeaderViewWrapper, com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper
    public final void updateExpandability(boolean z, View.OnClickListener onClickListener, boolean z2) {
        this.conversationLayout.updateExpandability(z, onClickListener);
    }

    public NotificationConversationTemplateViewWrapper(Context context, View view, ExpandableNotificationRow expandableNotificationRow) {
        super(context, view, expandableNotificationRow);
        this.minHeightWithActions = R$array.getFontScaledHeight(context, 2131166656);
        this.conversationLayout = (ConversationLayout) view;
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationTemplateViewWrapper, com.android.systemui.statusbar.notification.row.wrapper.NotificationHeaderViewWrapper
    public final void updateTransformedTypes() {
        super.updateTransformedTypes();
        ViewTransformationHelper viewTransformationHelper = this.mTransformationHelper;
        View view = this.conversationTitleView;
        View view2 = null;
        if (view == null) {
            view = null;
        }
        viewTransformationHelper.addTransformedView(1, view);
        View[] viewArr = new View[2];
        MessagingLinearLayout messagingLinearLayout = this.messagingLinearLayout;
        if (messagingLinearLayout == null) {
            messagingLinearLayout = null;
        }
        viewArr[0] = messagingLinearLayout;
        View view3 = this.appName;
        if (view3 == null) {
            view3 = null;
        }
        viewArr[1] = view3;
        addTransformedViews(viewArr);
        ViewTransformationHelper viewTransformationHelper2 = this.mTransformationHelper;
        ViewGroup viewGroup = this.imageMessageContainer;
        if (viewGroup == null) {
            viewGroup = null;
        }
        if (viewGroup != null) {
            NotificationMessagingTemplateViewWrapper.AnonymousClass1 r6 = new NotificationMessagingTemplateViewWrapper.AnonymousClass1();
            int id = viewGroup.getId();
            Objects.requireNonNull(viewTransformationHelper2);
            viewTransformationHelper2.mCustomTransformations.put(Integer.valueOf(id), r6);
        }
        View[] viewArr2 = new View[7];
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
        if (view5 == null) {
            view5 = null;
        }
        viewArr2[2] = view5;
        View view6 = this.importanceRing;
        if (view6 != null) {
            view2 = view6;
        }
        viewArr2[3] = view2;
        viewArr2[4] = this.facePileTop;
        viewArr2[5] = this.facePileBottom;
        viewArr2[6] = this.facePileBottomBg;
        addViewsTransformingToSimilar(viewArr2);
    }
}

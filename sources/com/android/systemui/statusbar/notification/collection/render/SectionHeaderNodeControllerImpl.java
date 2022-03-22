package com.android.systemui.statusbar.notification.collection.render;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.notification.stack.SectionHeaderView;
import com.google.android.systemui.assist.uihints.IconController$$ExternalSyntheticLambda0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SectionHeaderController.kt */
/* loaded from: classes.dex */
public final class SectionHeaderNodeControllerImpl implements NodeController, SectionHeaderController {
    public SectionHeaderView _view;
    public final ActivityStarter activityStarter;
    public boolean clearAllButtonEnabled;
    public View.OnClickListener clearAllClickListener;
    public final String clickIntentAction;
    public final int headerTextResId;
    public final LayoutInflater layoutInflater;
    public final String nodeLabel;
    public final SectionHeaderNodeControllerImpl$onHeaderClickListener$1 onHeaderClickListener = new View.OnClickListener() { // from class: com.android.systemui.statusbar.notification.collection.render.SectionHeaderNodeControllerImpl$onHeaderClickListener$1
        @Override // android.view.View.OnClickListener
        public final void onClick(View view) {
            SectionHeaderNodeControllerImpl.this.activityStarter.startActivity(new Intent(SectionHeaderNodeControllerImpl.this.clickIntentAction), true, true, 536870912);
        }
    };

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public final int getChildCount() {
        return 0;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public final void onViewMoved() {
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public final void onViewRemoved() {
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public final void addChildAt(NodeController nodeController, int i) {
        throw new RuntimeException("Not supported");
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public final View getChildAt(int i) {
        throw new RuntimeException("Not supported");
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public final View getView() {
        SectionHeaderView sectionHeaderView = this._view;
        Intrinsics.checkNotNull(sectionHeaderView);
        return sectionHeaderView;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public final void moveChildTo(NodeController nodeController, int i) {
        throw new RuntimeException("Not supported");
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public final void onViewAdded() {
        SectionHeaderView sectionHeaderView = this._view;
        if (sectionHeaderView != null) {
            sectionHeaderView.setContentVisible(true);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0044  */
    /* JADX WARN: Removed duplicated region for block: B:12:0x004d  */
    /* JADX WARN: Removed duplicated region for block: B:15:0x0059  */
    @Override // com.android.systemui.statusbar.notification.collection.render.SectionHeaderController
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void reinflateView(android.view.ViewGroup r7) {
        /*
            r6 = this;
            com.android.systemui.statusbar.notification.stack.SectionHeaderView r0 = r6._view
            r1 = -1
            if (r0 != 0) goto L_0x0006
            goto L_0x0017
        L_0x0006:
            r0.removeFromTransientContainer()
            android.view.ViewParent r2 = r0.getParent()
            if (r2 != r7) goto L_0x0017
            int r2 = r7.indexOfChild(r0)
            r7.removeView(r0)
            goto L_0x0018
        L_0x0017:
            r2 = r1
        L_0x0018:
            android.view.LayoutInflater r0 = r6.layoutInflater
            r3 = 2131624526(0x7f0e024e, float:1.8876234E38)
            r4 = 0
            android.view.View r0 = r0.inflate(r3, r7, r4)
            java.lang.String r3 = "null cannot be cast to non-null type com.android.systemui.statusbar.notification.stack.SectionHeaderView"
            java.util.Objects.requireNonNull(r0, r3)
            com.android.systemui.statusbar.notification.stack.SectionHeaderView r0 = (com.android.systemui.statusbar.notification.stack.SectionHeaderView) r0
            int r3 = r6.headerTextResId
            java.lang.Integer r5 = java.lang.Integer.valueOf(r3)
            r0.mLabelTextId = r5
            android.widget.TextView r5 = r0.mLabelView
            r5.setText(r3)
            com.android.systemui.statusbar.notification.collection.render.SectionHeaderNodeControllerImpl$onHeaderClickListener$1 r3 = r6.onHeaderClickListener
            r0.mLabelClickListener = r3
            android.widget.TextView r5 = r0.mLabelView
            r5.setOnClickListener(r3)
            android.view.View$OnClickListener r3 = r6.clearAllClickListener
            if (r3 != 0) goto L_0x0044
            goto L_0x004b
        L_0x0044:
            r0.mOnClearClickListener = r3
            android.widget.ImageView r5 = r0.mClearAllButton
            r5.setOnClickListener(r3)
        L_0x004b:
            if (r2 == r1) goto L_0x0050
            r7.addView(r0, r2)
        L_0x0050:
            r6._view = r0
            boolean r6 = r6.clearAllButtonEnabled
            android.widget.ImageView r7 = r0.mClearAllButton
            if (r6 == 0) goto L_0x0059
            goto L_0x005b
        L_0x0059:
            r4 = 8
        L_0x005b:
            r7.setVisibility(r4)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.collection.render.SectionHeaderNodeControllerImpl.reinflateView(android.view.ViewGroup):void");
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public final void removeChild(NodeController nodeController, boolean z) {
        throw new RuntimeException("Not supported");
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.SectionHeaderController
    public final void setClearSectionEnabled(boolean z) {
        int i;
        this.clearAllButtonEnabled = z;
        SectionHeaderView sectionHeaderView = this._view;
        if (sectionHeaderView != null) {
            ImageView imageView = sectionHeaderView.mClearAllButton;
            if (z) {
                i = 0;
            } else {
                i = 8;
            }
            imageView.setVisibility(i);
        }
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.SectionHeaderController
    public final void setOnClearSectionClickListener(IconController$$ExternalSyntheticLambda0 iconController$$ExternalSyntheticLambda0) {
        this.clearAllClickListener = iconController$$ExternalSyntheticLambda0;
        SectionHeaderView sectionHeaderView = this._view;
        if (sectionHeaderView != null) {
            sectionHeaderView.mOnClearClickListener = iconController$$ExternalSyntheticLambda0;
            sectionHeaderView.mClearAllButton.setOnClickListener(iconController$$ExternalSyntheticLambda0);
        }
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.statusbar.notification.collection.render.SectionHeaderNodeControllerImpl$onHeaderClickListener$1] */
    public SectionHeaderNodeControllerImpl(String str, LayoutInflater layoutInflater, int i, ActivityStarter activityStarter, String str2) {
        this.nodeLabel = str;
        this.layoutInflater = layoutInflater;
        this.headerTextResId = i;
        this.activityStarter = activityStarter;
        this.clickIntentAction = str2;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.SectionHeaderController
    public final SectionHeaderView getHeaderView() {
        return this._view;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public final String getNodeLabel() {
        return this.nodeLabel;
    }
}

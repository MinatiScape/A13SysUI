package com.android.systemui.statusbar.notification;

import androidx.leanback.R$raw;
import com.android.systemui.statusbar.TransformableView;
import com.android.systemui.statusbar.ViewTransformationHelper;
import com.android.systemui.statusbar.notification.row.wrapper.NotificationHeaderViewWrapper;
/* loaded from: classes.dex */
public abstract class CustomInterpolatorTransformation extends ViewTransformationHelper.CustomTransformation {
    public final int mViewType = 1;

    @Override // com.android.systemui.statusbar.ViewTransformationHelper.CustomTransformation
    public final boolean transformFrom(TransformState transformState, TransformableView transformableView, float f) {
        boolean z;
        TransformState currentState;
        NotificationHeaderViewWrapper notificationHeaderViewWrapper = NotificationHeaderViewWrapper.this;
        if (!notificationHeaderViewWrapper.mIsLowPriority || !notificationHeaderViewWrapper.mTransformLowPriorityTitle) {
            z = false;
        } else {
            z = true;
        }
        if (!z || (currentState = transformableView.getCurrentState(this.mViewType)) == null) {
            return false;
        }
        R$raw.fadeIn(transformState.mTransformedView, f, true);
        transformState.transformViewFrom(currentState, 17, this, f);
        currentState.recycle();
        return true;
    }

    @Override // com.android.systemui.statusbar.ViewTransformationHelper.CustomTransformation
    public final boolean transformTo(TransformState transformState, TransformableView transformableView, float f) {
        boolean z;
        TransformState currentState;
        NotificationHeaderViewWrapper notificationHeaderViewWrapper = NotificationHeaderViewWrapper.this;
        if (!notificationHeaderViewWrapper.mIsLowPriority || !notificationHeaderViewWrapper.mTransformLowPriorityTitle) {
            z = false;
        } else {
            z = true;
        }
        if (!z || (currentState = transformableView.getCurrentState(this.mViewType)) == null) {
            return false;
        }
        R$raw.fadeOut(transformState.mTransformedView, f, true);
        transformState.transformViewTo(currentState, 17, this, f);
        currentState.recycle();
        return true;
    }
}

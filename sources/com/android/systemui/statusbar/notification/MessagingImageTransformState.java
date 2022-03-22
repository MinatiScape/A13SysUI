package com.android.systemui.statusbar.notification;

import android.util.Pools;
import android.view.View;
import com.android.internal.widget.MessagingImageMessage;
import com.android.systemui.R$array;
import com.android.systemui.statusbar.ViewTransformationHelper;
import com.android.systemui.statusbar.notification.TransformState;
/* loaded from: classes.dex */
public final class MessagingImageTransformState extends ImageTransformState {
    public MessagingImageMessage mImageMessage;
    public static Pools.SimplePool<MessagingImageTransformState> sInstancePool = new Pools.SimplePool<>(40);
    public static final int START_ACTUAL_WIDTH = 2131429086;
    public static final int START_ACTUAL_HEIGHT = 2131429085;

    @Override // com.android.systemui.statusbar.notification.TransformState
    public final boolean transformScale(TransformState transformState) {
        return false;
    }

    @Override // com.android.systemui.statusbar.notification.ImageTransformState, com.android.systemui.statusbar.notification.TransformState
    public final void initFrom(View view, TransformState.TransformInfo transformInfo) {
        super.initFrom(view, transformInfo);
        this.mImageMessage = (MessagingImageMessage) view;
    }

    @Override // com.android.systemui.statusbar.notification.ImageTransformState, com.android.systemui.statusbar.notification.TransformState
    public final void recycle() {
        super.recycle();
        sInstancePool.release(this);
    }

    @Override // com.android.systemui.statusbar.notification.ImageTransformState, com.android.systemui.statusbar.notification.TransformState
    public final void reset() {
        super.reset();
        this.mImageMessage = null;
    }

    @Override // com.android.systemui.statusbar.notification.TransformState
    public final void resetTransformedView() {
        super.resetTransformedView();
        MessagingImageMessage messagingImageMessage = this.mImageMessage;
        messagingImageMessage.setActualWidth(messagingImageMessage.getWidth());
        MessagingImageMessage messagingImageMessage2 = this.mImageMessage;
        messagingImageMessage2.setActualHeight(messagingImageMessage2.getHeight());
    }

    @Override // com.android.systemui.statusbar.notification.ImageTransformState, com.android.systemui.statusbar.notification.TransformState
    public final boolean sameAs(TransformState transformState) {
        if (super.sameAs(transformState)) {
            return true;
        }
        if (transformState instanceof MessagingImageTransformState) {
            return this.mImageMessage.sameAs(((MessagingImageTransformState) transformState).mImageMessage);
        }
        return false;
    }

    @Override // com.android.systemui.statusbar.notification.TransformState
    public final void transformViewFrom(TransformState transformState, int i, ViewTransformationHelper.CustomTransformation customTransformation, float f) {
        int i2;
        MessagingImageMessage messagingImageMessage;
        MessagingImageMessage messagingImageMessage2;
        super.transformViewFrom(transformState, i, customTransformation, f);
        float interpolation = this.mDefaultInterpolator.getInterpolation(f);
        if ((transformState instanceof MessagingImageTransformState) && sameAs(transformState)) {
            MessagingImageMessage messagingImageMessage3 = ((MessagingImageTransformState) transformState).mImageMessage;
            if (f == 0.0f) {
                this.mTransformedView.setTag(START_ACTUAL_WIDTH, Integer.valueOf(messagingImageMessage3.getActualWidth()));
                this.mTransformedView.setTag(START_ACTUAL_HEIGHT, Integer.valueOf(messagingImageMessage3.getActualHeight()));
            }
            Object tag = this.mTransformedView.getTag(START_ACTUAL_WIDTH);
            int i3 = -1;
            if (tag == null) {
                i2 = -1;
            } else {
                i2 = ((Integer) tag).intValue();
            }
            this.mImageMessage.setActualWidth((int) R$array.interpolate(i2, messagingImageMessage.getWidth(), interpolation));
            Object tag2 = this.mTransformedView.getTag(START_ACTUAL_HEIGHT);
            if (tag2 != null) {
                i3 = ((Integer) tag2).intValue();
            }
            this.mImageMessage.setActualHeight((int) R$array.interpolate(i3, messagingImageMessage2.getHeight(), interpolation));
        }
    }
}

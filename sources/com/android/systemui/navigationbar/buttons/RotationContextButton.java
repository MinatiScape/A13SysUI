package com.android.systemui.navigationbar.buttons;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import com.android.systemui.shared.rotation.RotationButton;
import com.android.systemui.shared.rotation.RotationButtonController;
import java.util.Objects;
/* loaded from: classes.dex */
public final class RotationContextButton extends ContextualButton implements RotationButton {
    public RotationButtonController mRotationButtonController;

    @Override // com.android.systemui.shared.rotation.RotationButton
    public final boolean acceptRotationProposal() {
        View view = this.mCurrentView;
        if (view == null || !view.isAttachedToWindow()) {
            return false;
        }
        return true;
    }

    @Override // com.android.systemui.navigationbar.buttons.ContextualButton
    public final KeyButtonDrawable getNewDrawable(int i, int i2) {
        RotationButtonController rotationButtonController = this.mRotationButtonController;
        Objects.requireNonNull(rotationButtonController);
        Context context = rotationButtonController.mContext;
        RotationButtonController rotationButtonController2 = this.mRotationButtonController;
        Objects.requireNonNull(rotationButtonController2);
        return KeyButtonDrawable.create(context, i, i2, rotationButtonController2.mIconResId, false);
    }

    @Override // com.android.systemui.shared.rotation.RotationButton
    public final void setUpdatesCallback(RotationButton.RotationButtonUpdatesCallback rotationButtonUpdatesCallback) {
        this.mListener = new RotationContextButton$$ExternalSyntheticLambda0(rotationButtonUpdatesCallback);
    }

    public RotationContextButton(Context context) {
        super(2131428715, context, 2131232290);
    }

    @Override // com.android.systemui.navigationbar.buttons.ContextualButton, com.android.systemui.navigationbar.buttons.ButtonDispatcher
    public final void setVisibility(int i) {
        super.setVisibility(i);
        KeyButtonDrawable keyButtonDrawable = this.mImageDrawable;
        if (i == 0 && keyButtonDrawable != null) {
            AnimatedVectorDrawable animatedVectorDrawable = keyButtonDrawable.mAnimatedDrawable;
            if (animatedVectorDrawable != null) {
                animatedVectorDrawable.reset();
            }
            AnimatedVectorDrawable animatedVectorDrawable2 = keyButtonDrawable.mAnimatedDrawable;
            if (animatedVectorDrawable2 != null) {
                animatedVectorDrawable2.start();
            }
        }
    }

    @Override // com.android.systemui.shared.rotation.RotationButton
    public final void setRotationButtonController(RotationButtonController rotationButtonController) {
        this.mRotationButtonController = rotationButtonController;
    }

    @Override // com.android.systemui.shared.rotation.RotationButton
    public final Drawable getImageDrawable() {
        return this.mImageDrawable;
    }
}

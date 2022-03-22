package com.android.systemui.navigationbar.buttons;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import com.android.systemui.navigationbar.NavigationBarView;
import com.android.systemui.shared.rotation.RotationButton;
/* loaded from: classes.dex */
public class ContextualButton extends ButtonDispatcher {
    public ContextualButtonGroup mGroup;
    public final int mIconResId;
    public final Context mLightContext;
    public ContextButtonListener mListener;

    /* loaded from: classes.dex */
    public interface ContextButtonListener {
    }

    public KeyButtonDrawable getNewDrawable(int i, int i2) {
        return KeyButtonDrawable.create(this.mLightContext, i, i2, this.mIconResId, false);
    }

    public final boolean hide() {
        ContextualButtonGroup contextualButtonGroup = this.mGroup;
        if (contextualButtonGroup == null) {
            setVisibility(4);
            return false;
        } else if (contextualButtonGroup.setButtonVisibility(this.mId, false) != 0) {
            return true;
        } else {
            return false;
        }
    }

    public final boolean show() {
        ContextualButtonGroup contextualButtonGroup = this.mGroup;
        if (contextualButtonGroup == null) {
            setVisibility(0);
            return true;
        } else if (contextualButtonGroup.setButtonVisibility(this.mId, true) == 0) {
            return true;
        } else {
            return false;
        }
    }

    public final void updateIcon(int i, int i2) {
        if (this.mIconResId != 0) {
            KeyButtonDrawable keyButtonDrawable = this.mImageDrawable;
            KeyButtonDrawable newDrawable = getNewDrawable(i, i2);
            if (keyButtonDrawable != null) {
                newDrawable.setDarkIntensity(keyButtonDrawable.mState.mDarkIntensity);
            }
            setImageDrawable(newDrawable);
        }
    }

    public ContextualButton(int i, Context context, int i2) {
        super(i);
        this.mLightContext = context;
        this.mIconResId = i2;
    }

    @Override // com.android.systemui.navigationbar.buttons.ButtonDispatcher
    public void setVisibility(int i) {
        boolean z;
        super.setVisibility(i);
        KeyButtonDrawable keyButtonDrawable = this.mImageDrawable;
        if (!(i == 0 || keyButtonDrawable == null || !keyButtonDrawable.mState.mSupportsAnimation)) {
            AnimatedVectorDrawable animatedVectorDrawable = keyButtonDrawable.mAnimatedDrawable;
            if (animatedVectorDrawable != null) {
                animatedVectorDrawable.clearAnimationCallbacks();
            }
            AnimatedVectorDrawable animatedVectorDrawable2 = keyButtonDrawable.mAnimatedDrawable;
            if (animatedVectorDrawable2 != null) {
                animatedVectorDrawable2.reset();
            }
        }
        ContextButtonListener contextButtonListener = this.mListener;
        if (contextButtonListener != null) {
            if (i == 0) {
                z = true;
            } else {
                z = false;
            }
            RotationButton.RotationButtonUpdatesCallback rotationButtonUpdatesCallback = ((RotationContextButton$$ExternalSyntheticLambda0) contextButtonListener).f$0;
            if (rotationButtonUpdatesCallback != null) {
                ((NavigationBarView.AnonymousClass2) rotationButtonUpdatesCallback).onVisibilityChanged(z);
            }
        }
    }
}

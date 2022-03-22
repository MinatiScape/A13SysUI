package com.android.systemui.biometrics;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class UdfpsAnimationView extends FrameLayout {
    public int mAlpha;
    public boolean mPauseAuth;

    public boolean dozeTimeTick() {
        return false;
    }

    public abstract UdfpsDrawable getDrawable();

    public int calculateAlpha() {
        if (this.mPauseAuth) {
            return this.mAlpha;
        }
        return 255;
    }

    public void onIlluminationStarting() {
        UdfpsDrawable drawable = getDrawable();
        Objects.requireNonNull(drawable);
        if (!drawable.isIlluminationShowing) {
            drawable.isIlluminationShowing = true;
            drawable.invalidateSelf();
        }
        getDrawable().invalidateSelf();
    }

    public void onIlluminationStopped() {
        UdfpsDrawable drawable = getDrawable();
        Objects.requireNonNull(drawable);
        if (drawable.isIlluminationShowing) {
            drawable.isIlluminationShowing = false;
            drawable.invalidateSelf();
        }
        getDrawable().invalidateSelf();
    }

    public int updateAlpha() {
        int calculateAlpha = calculateAlpha();
        getDrawable().setAlpha(calculateAlpha);
        if (!this.mPauseAuth || calculateAlpha != 0 || getParent() == null) {
            ((ViewGroup) getParent()).setVisibility(0);
        } else {
            ((ViewGroup) getParent()).setVisibility(4);
        }
        return calculateAlpha;
    }

    public UdfpsAnimationView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}

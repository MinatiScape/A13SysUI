package com.android.systemui.shared.rotation;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import androidx.core.view.OneShotPreDrawListener;
import com.android.settingslib.wifi.AccessPoint$$ExternalSyntheticLambda0;
import com.android.settingslib.wifi.AccessPoint$$ExternalSyntheticLambda1;
import com.android.systemui.navigationbar.NavigationBarView;
import com.android.systemui.navigationbar.buttons.KeyButtonRipple;
import com.android.systemui.shared.rotation.FloatingRotationButtonPositionCalculator;
import com.android.systemui.shared.rotation.RotationButton;
import java.util.Objects;
/* loaded from: classes.dex */
public final class FloatingRotationButton implements RotationButton {
    public AnimatedVectorDrawable mAnimatedDrawable;
    public int mContainerSize;
    public final Context mContext;
    public int mDisplayRotation;
    public boolean mIsShowing;
    public final ViewGroup mKeyButtonContainer;
    public final FloatingRotationButtonView mKeyButtonView;
    public FloatingRotationButtonPositionCalculator.Position mPosition;
    public FloatingRotationButtonPositionCalculator mPositionCalculator;
    public RotationButtonController mRotationButtonController;
    public RotationButton.RotationButtonUpdatesCallback mUpdatesCallback;
    public final WindowManager mWindowManager;
    public boolean mCanShow = true;
    public boolean mIsTaskbarVisible = false;
    public boolean mIsTaskbarStashed = false;
    public final int mContentDescriptionResource = 2131951804;
    public final int mMinMarginResource = 2131165729;
    public final int mRoundedContentPaddingResource = 2131166936;
    public final int mTaskbarLeftMarginResource = 2131165731;
    public final int mTaskbarBottomMarginResource = 2131165730;
    public final int mButtonDiameterResource = 2131165728;

    public final WindowManager.LayoutParams adjustViewPositionAndCreateLayoutParams() {
        int i = this.mContainerSize;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(i, i, 0, 0, 2024, 8, -3);
        layoutParams.privateFlags |= 16;
        layoutParams.setTitle("FloatingRotationButton");
        layoutParams.setFitInsetsTypes(0);
        int rotation = this.mWindowManager.getDefaultDisplay().getRotation();
        this.mDisplayRotation = rotation;
        FloatingRotationButtonPositionCalculator.Position calculatePosition = this.mPositionCalculator.calculatePosition(rotation, this.mIsTaskbarVisible, this.mIsTaskbarStashed);
        this.mPosition = calculatePosition;
        Objects.requireNonNull(calculatePosition);
        layoutParams.gravity = calculatePosition.gravity;
        FloatingRotationButtonPositionCalculator.Position position = this.mPosition;
        Objects.requireNonNull(position);
        ((FrameLayout.LayoutParams) this.mKeyButtonView.getLayoutParams()).gravity = position.gravity;
        updateTranslation(this.mPosition, false);
        return layoutParams;
    }

    @Override // com.android.systemui.shared.rotation.RotationButton
    public final boolean hide() {
        if (!this.mIsShowing) {
            return false;
        }
        this.mWindowManager.removeViewImmediate(this.mKeyButtonContainer);
        this.mIsShowing = false;
        RotationButton.RotationButtonUpdatesCallback rotationButtonUpdatesCallback = this.mUpdatesCallback;
        if (rotationButtonUpdatesCallback == null) {
            return true;
        }
        ((NavigationBarView.AnonymousClass2) rotationButtonUpdatesCallback).onVisibilityChanged(false);
        return true;
    }

    @Override // com.android.systemui.shared.rotation.RotationButton
    public final void setCanShowRotationButton(boolean z) {
        this.mCanShow = z;
        if (!z) {
            hide();
        }
    }

    @Override // com.android.systemui.shared.rotation.RotationButton
    public final void setDarkIntensity(float f) {
        boolean z;
        FloatingRotationButtonView floatingRotationButtonView = this.mKeyButtonView;
        Objects.requireNonNull(floatingRotationButtonView);
        KeyButtonRipple keyButtonRipple = floatingRotationButtonView.mRipple;
        Objects.requireNonNull(keyButtonRipple);
        if (f >= 0.5f) {
            z = true;
        } else {
            z = false;
        }
        keyButtonRipple.mDark = z;
    }

    @Override // com.android.systemui.shared.rotation.RotationButton
    public final void setOnClickListener(View.OnClickListener onClickListener) {
        this.mKeyButtonView.setOnClickListener(onClickListener);
    }

    @Override // com.android.systemui.shared.rotation.RotationButton
    public final void setOnHoverListener(RotationButtonController$$ExternalSyntheticLambda0 rotationButtonController$$ExternalSyntheticLambda0) {
        this.mKeyButtonView.setOnHoverListener(rotationButtonController$$ExternalSyntheticLambda0);
    }

    @Override // com.android.systemui.shared.rotation.RotationButton
    public final void setRotationButtonController(RotationButtonController rotationButtonController) {
        this.mRotationButtonController = rotationButtonController;
        Objects.requireNonNull(rotationButtonController);
        int i = rotationButtonController.mLightIconColor;
        RotationButtonController rotationButtonController2 = this.mRotationButtonController;
        Objects.requireNonNull(rotationButtonController2);
        updateIcon(i, rotationButtonController2.mDarkIconColor);
    }

    @Override // com.android.systemui.shared.rotation.RotationButton
    public final boolean show() {
        if (!this.mCanShow || this.mIsShowing) {
            return false;
        }
        this.mIsShowing = true;
        this.mWindowManager.addView(this.mKeyButtonContainer, adjustViewPositionAndCreateLayoutParams());
        AnimatedVectorDrawable animatedVectorDrawable = this.mAnimatedDrawable;
        if (animatedVectorDrawable != null) {
            animatedVectorDrawable.reset();
            this.mAnimatedDrawable.start();
        }
        OneShotPreDrawListener.add(this.mKeyButtonView, new AccessPoint$$ExternalSyntheticLambda0(this, 6));
        return true;
    }

    public final void updateDimensionResources() {
        Resources resources = this.mContext.getResources();
        int max = Math.max(resources.getDimensionPixelSize(this.mMinMarginResource), resources.getDimensionPixelSize(this.mRoundedContentPaddingResource));
        int dimensionPixelSize = resources.getDimensionPixelSize(this.mTaskbarLeftMarginResource);
        int dimensionPixelSize2 = resources.getDimensionPixelSize(this.mTaskbarBottomMarginResource);
        this.mPositionCalculator = new FloatingRotationButtonPositionCalculator(max, dimensionPixelSize, dimensionPixelSize2);
        this.mContainerSize = Math.max(max, Math.max(dimensionPixelSize, dimensionPixelSize2)) + resources.getDimensionPixelSize(this.mButtonDiameterResource);
    }

    @Override // com.android.systemui.shared.rotation.RotationButton
    public final void updateIcon(int i, int i2) {
        Context context = this.mKeyButtonView.getContext();
        RotationButtonController rotationButtonController = this.mRotationButtonController;
        Objects.requireNonNull(rotationButtonController);
        AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) context.getDrawable(rotationButtonController.mIconResId);
        this.mAnimatedDrawable = animatedVectorDrawable;
        this.mKeyButtonView.setImageDrawable(animatedVectorDrawable);
        FloatingRotationButtonView floatingRotationButtonView = this.mKeyButtonView;
        Objects.requireNonNull(floatingRotationButtonView);
        floatingRotationButtonView.getDrawable().setColorFilter(new PorterDuffColorFilter(i, PorterDuff.Mode.SRC_IN));
        floatingRotationButtonView.mOvalBgPaint.setColor(Color.valueOf(Color.red(i2), Color.green(i2), Color.blue(i2), 0.92f).toArgb());
        KeyButtonRipple keyButtonRipple = floatingRotationButtonView.mRipple;
        KeyButtonRipple.Type type = KeyButtonRipple.Type.OVAL;
        Objects.requireNonNull(keyButtonRipple);
        keyButtonRipple.mType = type;
    }

    public FloatingRotationButton(Context context) {
        this.mWindowManager = (WindowManager) context.getSystemService(WindowManager.class);
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(2131624449, (ViewGroup) null);
        this.mKeyButtonContainer = viewGroup;
        FloatingRotationButtonView floatingRotationButtonView = (FloatingRotationButtonView) viewGroup.findViewById(2131428715);
        this.mKeyButtonView = floatingRotationButtonView;
        floatingRotationButtonView.setVisibility(0);
        floatingRotationButtonView.setContentDescription(context.getString(2131951804));
        KeyButtonRipple keyButtonRipple = new KeyButtonRipple(floatingRotationButtonView.getContext(), floatingRotationButtonView);
        floatingRotationButtonView.mRipple = keyButtonRipple;
        floatingRotationButtonView.setBackground(keyButtonRipple);
        this.mContext = context;
        updateDimensionResources();
    }

    public final void updateTranslation(FloatingRotationButtonPositionCalculator.Position position, boolean z) {
        Objects.requireNonNull(position);
        int i = position.translationX;
        int i2 = position.translationY;
        if (z) {
            this.mKeyButtonView.animate().translationX(i).translationY(i2).setDuration(300L).setInterpolator(new AccelerateDecelerateInterpolator()).withEndAction(new AccessPoint$$ExternalSyntheticLambda1(this, 3)).start();
            return;
        }
        this.mKeyButtonView.setTranslationX(i);
        this.mKeyButtonView.setTranslationY(i2);
    }

    @Override // com.android.systemui.shared.rotation.RotationButton
    public final void setUpdatesCallback(RotationButton.RotationButtonUpdatesCallback rotationButtonUpdatesCallback) {
        this.mUpdatesCallback = rotationButtonUpdatesCallback;
    }

    @Override // com.android.systemui.shared.rotation.RotationButton
    public final View getCurrentView() {
        return this.mKeyButtonView;
    }

    @Override // com.android.systemui.shared.rotation.RotationButton
    public final Drawable getImageDrawable() {
        return this.mAnimatedDrawable;
    }

    @Override // com.android.systemui.shared.rotation.RotationButton
    public final boolean isVisible() {
        return this.mIsShowing;
    }
}

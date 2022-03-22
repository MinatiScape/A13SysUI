package com.android.systemui.settings.brightness;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.FrameLayout;
import androidx.annotation.Keep;
import com.android.systemui.Gefingerpoken;
import com.android.systemui.screenshot.ScreenshotController$$ExternalSyntheticLambda4;
import java.util.Objects;
/* loaded from: classes.dex */
public class BrightnessSliderView extends FrameLayout {
    public DispatchTouchEventListener mListener;
    public Gefingerpoken mOnInterceptListener;
    public Drawable mProgressDrawable;
    public float mScale;
    public ToggleSeekBar mSlider;

    @FunctionalInterface
    /* loaded from: classes.dex */
    public interface DispatchTouchEventListener {
    }

    public BrightnessSliderView(Context context) {
        this(context, null);
    }

    public BrightnessSliderView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mScale = 1.0f;
    }

    public final void applySliderScale() {
        Drawable drawable = this.mProgressDrawable;
        if (drawable != null) {
            Rect bounds = drawable.getBounds();
            int intrinsicHeight = (int) (this.mProgressDrawable.getIntrinsicHeight() * this.mScale);
            int intrinsicHeight2 = (this.mProgressDrawable.getIntrinsicHeight() - intrinsicHeight) / 2;
            this.mProgressDrawable.setBounds(bounds.left, intrinsicHeight2, bounds.right, intrinsicHeight + intrinsicHeight2);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public final boolean dispatchTouchEvent(MotionEvent motionEvent) {
        DispatchTouchEventListener dispatchTouchEventListener = this.mListener;
        if (dispatchTouchEventListener != null) {
            ((ScreenshotController$$ExternalSyntheticLambda4) dispatchTouchEventListener).onDispatchTouchEvent(motionEvent);
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override // android.view.ViewGroup
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        Gefingerpoken gefingerpoken = this.mOnInterceptListener;
        if (gefingerpoken != null) {
            return gefingerpoken.onInterceptTouchEvent(motionEvent);
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public final void requestDisallowInterceptTouchEvent(boolean z) {
        ViewParent viewParent = ((FrameLayout) this).mParent;
        if (viewParent != null) {
            viewParent.requestDisallowInterceptTouchEvent(z);
        }
    }

    @Keep
    public void setSliderScaleY(float f) {
        if (f != this.mScale) {
            this.mScale = f;
            applySliderScale();
        }
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        setLayerType(2, null);
        ToggleSeekBar toggleSeekBar = (ToggleSeekBar) requireViewById(2131428874);
        this.mSlider = toggleSeekBar;
        String charSequence = getContentDescription().toString();
        Objects.requireNonNull(toggleSeekBar);
        toggleSeekBar.mAccessibilityLabel = charSequence;
        try {
            this.mProgressDrawable = ((LayerDrawable) ((DrawableWrapper) ((LayerDrawable) this.mSlider.getProgressDrawable()).findDrawableByLayerId(16908301)).getDrawable()).findDrawableByLayerId(2131428875);
        } catch (Exception unused) {
        }
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        applySliderScale();
    }

    @Keep
    public float getSliderScaleY() {
        return this.mScale;
    }
}

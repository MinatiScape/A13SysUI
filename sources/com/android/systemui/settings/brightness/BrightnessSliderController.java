package com.android.systemui.settings.brightness;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.SeekBar;
import com.android.systemui.Gefingerpoken;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.settings.brightness.ToggleSlider;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.policy.BrightnessMirrorController;
import com.android.systemui.util.ViewController;
import java.util.Objects;
/* loaded from: classes.dex */
public final class BrightnessSliderController extends ViewController<BrightnessSliderView> implements ToggleSlider {
    public final FalsingManager mFalsingManager;
    public ToggleSlider.Listener mListener;
    public ToggleSlider mMirror;
    public BrightnessMirrorController mMirrorController;
    public final AnonymousClass1 mOnInterceptListener = new Gefingerpoken() { // from class: com.android.systemui.settings.brightness.BrightnessSliderController.1
        @Override // com.android.systemui.Gefingerpoken
        public final boolean onTouchEvent(MotionEvent motionEvent) {
            return false;
        }

        @Override // com.android.systemui.Gefingerpoken
        public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
            int actionMasked = motionEvent.getActionMasked();
            if (actionMasked != 1 && actionMasked != 3) {
                return false;
            }
            BrightnessSliderController.this.mFalsingManager.isFalseTouch(10);
            return false;
        }
    };
    public final AnonymousClass2 mSeekListener = new SeekBar.OnSeekBarChangeListener() { // from class: com.android.systemui.settings.brightness.BrightnessSliderController.2
        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public final void onProgressChanged(SeekBar seekBar, int i, boolean z) {
            BrightnessSliderController brightnessSliderController = BrightnessSliderController.this;
            ToggleSlider.Listener listener = brightnessSliderController.mListener;
            if (listener != null) {
                ((BrightnessController) listener).onChanged(brightnessSliderController.mTracking, i, false);
            }
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public final void onStartTrackingTouch(SeekBar seekBar) {
            BrightnessSliderController brightnessSliderController = BrightnessSliderController.this;
            brightnessSliderController.mTracking = true;
            ToggleSlider.Listener listener = brightnessSliderController.mListener;
            if (listener != null) {
                BrightnessSliderView brightnessSliderView = (BrightnessSliderView) brightnessSliderController.mView;
                Objects.requireNonNull(brightnessSliderView);
                ((BrightnessController) listener).onChanged(true, brightnessSliderView.mSlider.getProgress(), false);
            }
            BrightnessMirrorController brightnessMirrorController = BrightnessSliderController.this.mMirrorController;
            if (brightnessMirrorController != null) {
                brightnessMirrorController.mBrightnessMirror.setVisibility(0);
                brightnessMirrorController.mVisibilityCallback.accept(Boolean.TRUE);
                brightnessMirrorController.mNotificationPanel.setPanelAlpha(0, true);
                NotificationShadeDepthController notificationShadeDepthController = brightnessMirrorController.mDepthController;
                Objects.requireNonNull(notificationShadeDepthController);
                NotificationShadeDepthController.DepthAnimation.animateTo$default(notificationShadeDepthController.brightnessMirrorSpring, (int) notificationShadeDepthController.blurUtils.blurRadiusOfRatio(1.0f));
                BrightnessSliderController brightnessSliderController2 = BrightnessSliderController.this;
                BrightnessMirrorController brightnessMirrorController2 = brightnessSliderController2.mMirrorController;
                T t = brightnessSliderController2.mView;
                Objects.requireNonNull(brightnessMirrorController2);
                t.getLocationInWindow(brightnessMirrorController2.mInt2Cache);
                int[] iArr = brightnessMirrorController2.mInt2Cache;
                int i = iArr[0];
                int i2 = brightnessMirrorController2.mBrightnessMirrorBackgroundPadding;
                int i3 = i - i2;
                int i4 = iArr[1] - i2;
                brightnessMirrorController2.mBrightnessMirror.setTranslationX(0.0f);
                brightnessMirrorController2.mBrightnessMirror.setTranslationY(0.0f);
                brightnessMirrorController2.mBrightnessMirror.getLocationInWindow(brightnessMirrorController2.mInt2Cache);
                int[] iArr2 = brightnessMirrorController2.mInt2Cache;
                int i5 = iArr2[0];
                int i6 = iArr2[1];
                brightnessMirrorController2.mBrightnessMirror.setTranslationX(i3 - i5);
                brightnessMirrorController2.mBrightnessMirror.setTranslationY(i4 - i6);
                int measuredWidth = (brightnessMirrorController2.mBrightnessMirrorBackgroundPadding * 2) + t.getMeasuredWidth();
                if (measuredWidth != brightnessMirrorController2.mLastBrightnessSliderWidth) {
                    ViewGroup.LayoutParams layoutParams = brightnessMirrorController2.mBrightnessMirror.getLayoutParams();
                    layoutParams.width = measuredWidth;
                    brightnessMirrorController2.mBrightnessMirror.setLayoutParams(layoutParams);
                }
            }
        }

        @Override // android.widget.SeekBar.OnSeekBarChangeListener
        public final void onStopTrackingTouch(SeekBar seekBar) {
            BrightnessSliderController brightnessSliderController = BrightnessSliderController.this;
            brightnessSliderController.mTracking = false;
            ToggleSlider.Listener listener = brightnessSliderController.mListener;
            if (listener != null) {
                BrightnessSliderView brightnessSliderView = (BrightnessSliderView) brightnessSliderController.mView;
                Objects.requireNonNull(brightnessSliderView);
                ((BrightnessController) listener).onChanged(false, brightnessSliderView.mSlider.getProgress(), true);
            }
            BrightnessMirrorController brightnessMirrorController = BrightnessSliderController.this.mMirrorController;
            if (brightnessMirrorController != null) {
                brightnessMirrorController.mVisibilityCallback.accept(Boolean.FALSE);
                brightnessMirrorController.mNotificationPanel.setPanelAlpha(255, true);
                NotificationShadeDepthController notificationShadeDepthController = brightnessMirrorController.mDepthController;
                Objects.requireNonNull(notificationShadeDepthController);
                NotificationShadeDepthController.DepthAnimation.animateTo$default(notificationShadeDepthController.brightnessMirrorSpring, 0);
            }
        }
    };
    public boolean mTracking;

    /* loaded from: classes.dex */
    public static class Factory {
        public final FalsingManager mFalsingManager;

        public Factory(FalsingManager falsingManager) {
            this.mFalsingManager = falsingManager;
        }

        public final BrightnessSliderController create(Context context, ViewGroup viewGroup) {
            return new BrightnessSliderController((BrightnessSliderView) LayoutInflater.from(context).inflate(2131624438, viewGroup, false), this.mFalsingManager);
        }
    }

    public final boolean mirrorTouchEvent(MotionEvent motionEvent) {
        if (this.mMirror == null) {
            return ((BrightnessSliderView) this.mView).dispatchTouchEvent(motionEvent);
        }
        MotionEvent copy = motionEvent.copy();
        boolean mirrorTouchEvent = ((BrightnessSliderController) this.mMirror).mirrorTouchEvent(copy);
        copy.recycle();
        return mirrorTouchEvent;
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewAttached() {
        BrightnessSliderView brightnessSliderView = (BrightnessSliderView) this.mView;
        AnonymousClass2 r1 = this.mSeekListener;
        Objects.requireNonNull(brightnessSliderView);
        brightnessSliderView.mSlider.setOnSeekBarChangeListener(r1);
        BrightnessSliderView brightnessSliderView2 = (BrightnessSliderView) this.mView;
        AnonymousClass1 r2 = this.mOnInterceptListener;
        Objects.requireNonNull(brightnessSliderView2);
        brightnessSliderView2.mOnInterceptListener = r2;
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewDetached() {
        BrightnessSliderView brightnessSliderView = (BrightnessSliderView) this.mView;
        Objects.requireNonNull(brightnessSliderView);
        brightnessSliderView.mSlider.setOnSeekBarChangeListener(null);
        BrightnessSliderView brightnessSliderView2 = (BrightnessSliderView) this.mView;
        Objects.requireNonNull(brightnessSliderView2);
        brightnessSliderView2.mListener = null;
        BrightnessSliderView brightnessSliderView3 = (BrightnessSliderView) this.mView;
        Objects.requireNonNull(brightnessSliderView3);
        brightnessSliderView3.mOnInterceptListener = null;
    }

    public final void setMax(int i) {
        BrightnessSliderView brightnessSliderView = (BrightnessSliderView) this.mView;
        Objects.requireNonNull(brightnessSliderView);
        brightnessSliderView.mSlider.setMax(i);
        ToggleSlider toggleSlider = this.mMirror;
        if (toggleSlider != null) {
            ((BrightnessSliderController) toggleSlider).setMax(i);
        }
    }

    public final void setValue(int i) {
        BrightnessSliderView brightnessSliderView = (BrightnessSliderView) this.mView;
        Objects.requireNonNull(brightnessSliderView);
        brightnessSliderView.mSlider.setProgress(i);
        ToggleSlider toggleSlider = this.mMirror;
        if (toggleSlider != null) {
            ((BrightnessSliderController) toggleSlider).setValue(i);
        }
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.settings.brightness.BrightnessSliderController$1] */
    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.systemui.settings.brightness.BrightnessSliderController$2] */
    public BrightnessSliderController(BrightnessSliderView brightnessSliderView, FalsingManager falsingManager) {
        super(brightnessSliderView);
        this.mFalsingManager = falsingManager;
    }
}

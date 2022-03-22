package com.android.systemui.biometrics;

import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.drawable.Animatable2;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.GridLayoutManager$$ExternalSyntheticOutline1;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.KeyguardUpdateMonitor$18$$ExternalSyntheticOutline0;
import com.android.systemui.biometrics.AuthBiometricView;
import java.util.Objects;
/* loaded from: classes.dex */
public class AuthBiometricFaceView extends AuthBiometricView {
    @VisibleForTesting
    public IconController mFaceIconController;
    public final AnonymousClass1 mOnAttachStateChangeListener;

    /* loaded from: classes.dex */
    public static class IconController extends Animatable2.AnimationCallback {
        public Context mContext;
        public boolean mDeactivated;
        public ImageView mIconView;
        public boolean mLastPulseLightToDark;
        public int mState;

        public final void animateIcon(int i, boolean z) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("animateIcon, state: ");
            m.append(this.mState);
            m.append(", deactivated: ");
            KeyguardUpdateMonitor$18$$ExternalSyntheticOutline0.m(m, this.mDeactivated, "BiometricPrompt/AuthBiometricFaceView");
            if (!this.mDeactivated) {
                AnimatedVectorDrawable animatedVectorDrawable = (AnimatedVectorDrawable) this.mContext.getDrawable(i);
                this.mIconView.setImageDrawable(animatedVectorDrawable);
                animatedVectorDrawable.forceAnimationOnUI();
                if (z) {
                    animatedVectorDrawable.registerAnimationCallback(this);
                }
                animatedVectorDrawable.start();
            }
        }

        public final void showStaticDrawable(int i) {
            this.mIconView.setImageDrawable(this.mContext.getDrawable(i));
        }

        public void updateState(int i, int i2) {
            boolean z;
            if (this.mDeactivated) {
                GridLayoutManager$$ExternalSyntheticOutline1.m("Ignoring updateState when deactivated: ", i2, "BiometricPrompt/AuthBiometricFaceView");
                return;
            }
            if (i == 4 || i == 3) {
                z = true;
            } else {
                z = false;
            }
            if (i2 == 1) {
                showStaticDrawable(2131231704);
                this.mIconView.setContentDescription(this.mContext.getString(2131951930));
            } else if (i2 == 2) {
                this.mLastPulseLightToDark = false;
                animateIcon(2131231704, true);
                this.mIconView.setContentDescription(this.mContext.getString(2131951930));
            } else if (i == 5 && i2 == 6) {
                animateIcon(2131231700, false);
                this.mIconView.setContentDescription(this.mContext.getString(2131951931));
            } else if (z && i2 == 0) {
                animateIcon(2131231702, false);
                this.mIconView.setContentDescription(this.mContext.getString(2131951932));
            } else if (z && i2 == 6) {
                animateIcon(2131231700, false);
                this.mIconView.setContentDescription(this.mContext.getString(2131951929));
            } else if (i2 == 4 && i != 4) {
                animateIcon(2131231701, false);
            } else if (i == 2 && i2 == 6) {
                animateIcon(2131231700, false);
                this.mIconView.setContentDescription(this.mContext.getString(2131951929));
            } else if (i2 == 5) {
                animateIcon(2131231706, false);
                this.mIconView.setContentDescription(this.mContext.getString(2131951929));
            } else if (i2 == 0) {
                showStaticDrawable(2131231703);
                this.mIconView.setContentDescription(this.mContext.getString(2131951932));
            } else {
                GridLayoutManager$$ExternalSyntheticOutline1.m("Unhandled state: ", i2, "BiometricPrompt/AuthBiometricFaceView");
            }
            this.mState = i2;
        }

        public IconController(Context context, ImageView imageView) {
            this.mContext = context;
            this.mIconView = imageView;
            new Handler(Looper.getMainLooper());
            showStaticDrawable(2131231704);
        }

        @Override // android.graphics.drawable.Animatable2.AnimationCallback
        public final void onAnimationEnd(Drawable drawable) {
            int i;
            super.onAnimationEnd(drawable);
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("onAnimationEnd, mState: ");
            m.append(this.mState);
            m.append(", deactivated: ");
            KeyguardUpdateMonitor$18$$ExternalSyntheticOutline0.m(m, this.mDeactivated, "BiometricPrompt/AuthBiometricFaceView");
            if (!this.mDeactivated) {
                int i2 = this.mState;
                if (i2 == 2 || i2 == 3) {
                    if (this.mLastPulseLightToDark) {
                        i = 2131231704;
                    } else {
                        i = 2131231705;
                    }
                    animateIcon(i, true);
                    this.mLastPulseLightToDark = !this.mLastPulseLightToDark;
                }
            }
        }
    }

    public AuthBiometricFaceView(Context context) {
        this(context, null);
    }

    @Override // com.android.systemui.biometrics.AuthBiometricView
    public int getDelayAfterAuthenticatedDurationMs() {
        return 500;
    }

    @Override // com.android.systemui.biometrics.AuthBiometricView
    public int getStateForAfterError() {
        return 0;
    }

    @Override // com.android.systemui.biometrics.AuthBiometricView
    public boolean supportsManualRetry() {
        return !(this instanceof AuthBiometricFaceToFingerprintView);
    }

    @Override // com.android.systemui.biometrics.AuthBiometricView
    public final boolean supportsSmallDialog() {
        return true;
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.biometrics.AuthBiometricFaceView$1] */
    public AuthBiometricFaceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mOnAttachStateChangeListener = new View.OnAttachStateChangeListener() { // from class: com.android.systemui.biometrics.AuthBiometricFaceView.1
            @Override // android.view.View.OnAttachStateChangeListener
            public final void onViewAttachedToWindow(View view) {
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public final void onViewDetachedFromWindow(View view) {
                IconController iconController = AuthBiometricFaceView.this.mFaceIconController;
                Objects.requireNonNull(iconController);
                iconController.mDeactivated = true;
            }
        };
    }

    @Override // com.android.systemui.biometrics.AuthBiometricView
    public final void handleResetAfterError() {
        this.mIndicatorView.setTextColor(this.mTextColorHint);
        this.mIndicatorView.setVisibility(4);
    }

    @Override // com.android.systemui.biometrics.AuthBiometricView
    public final void handleResetAfterHelp() {
        this.mIndicatorView.setTextColor(this.mTextColorHint);
        this.mIndicatorView.setVisibility(4);
    }

    @Override // com.android.systemui.biometrics.AuthBiometricView
    public void onAuthenticationFailed(int i, String str) {
        if (this.mSize == 2 && (!(this instanceof AuthBiometricFaceToFingerprintView))) {
            this.mTryAgainButton.setVisibility(0);
            this.mConfirmButton.setVisibility(8);
        }
        super.onAuthenticationFailed(i, str);
    }

    @Override // com.android.systemui.biometrics.AuthBiometricView
    public void updateState(int i) {
        this.mFaceIconController.updateState(this.mState, i);
        if (i == 1 || (i == 2 && this.mSize == 2)) {
            this.mIndicatorView.setTextColor(this.mTextColorHint);
            this.mIndicatorView.setVisibility(4);
        }
        super.updateState(i);
    }

    @Override // com.android.systemui.biometrics.AuthBiometricView, android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mFaceIconController = new IconController(((LinearLayout) this).mContext, this.mIconView);
        addOnAttachStateChangeListener(this.mOnAttachStateChangeListener);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.biometrics.AuthBiometricFaceView$1] */
    @VisibleForTesting
    public AuthBiometricFaceView(Context context, AttributeSet attributeSet, AuthBiometricView.Injector injector) {
        super(context, attributeSet, injector);
        this.mOnAttachStateChangeListener = new View.OnAttachStateChangeListener() { // from class: com.android.systemui.biometrics.AuthBiometricFaceView.1
            @Override // android.view.View.OnAttachStateChangeListener
            public final void onViewAttachedToWindow(View view) {
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public final void onViewDetachedFromWindow(View view) {
                IconController iconController = AuthBiometricFaceView.this.mFaceIconController;
                Objects.requireNonNull(iconController);
                iconController.mDeactivated = true;
            }
        };
    }
}

package com.android.keyguard;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.android.keyguard.KeyguardInputView;
import com.android.settingslib.animation.AppearAnimationUtils;
import com.android.settingslib.animation.DisappearAnimationUtils;
import java.util.Objects;
/* loaded from: classes.dex */
public class KeyguardPINView extends KeyguardPinBasedInputView {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final AppearAnimationUtils mAppearAnimationUtils;
    public ConstraintLayout mContainer;
    public final DisappearAnimationUtils mDisappearAnimationUtils;
    public final DisappearAnimationUtils mDisappearAnimationUtilsLocked;
    public int mDisappearYTranslation;
    public int mLastDevicePosture;
    public View[][] mViews;

    public KeyguardPINView(Context context) {
        this(context, null);
    }

    @Override // com.android.keyguard.KeyguardAbsKeyInputView
    public final int getPasswordTextViewId() {
        return 2131428589;
    }

    @Override // com.android.keyguard.KeyguardAbsKeyInputView
    public final int getWrongPasswordStringId() {
        return 2131952607;
    }

    @Override // android.view.View
    public final boolean hasOverlappingRendering() {
        return false;
    }

    @Override // com.android.keyguard.KeyguardInputView
    public final void startAppearAnimation() {
        enableClipping(false);
        setAlpha(1.0f);
        AppearAnimationUtils appearAnimationUtils = this.mAppearAnimationUtils;
        Objects.requireNonNull(appearAnimationUtils);
        setTranslationY(appearAnimationUtils.mStartTranslation);
        AppearAnimationUtils appearAnimationUtils2 = this.mAppearAnimationUtils;
        Objects.requireNonNull(appearAnimationUtils2);
        AppearAnimationUtils.startTranslationYAnimation(this, 0L, 500L, 0.0f, appearAnimationUtils2.mInterpolator, new KeyguardInputView.AnonymousClass1(19));
        AppearAnimationUtils appearAnimationUtils3 = this.mAppearAnimationUtils;
        View[][] viewArr = this.mViews;
        Runnable runnable = new Runnable() { // from class: com.android.keyguard.KeyguardPINView.1
            @Override // java.lang.Runnable
            public final void run() {
                KeyguardPINView keyguardPINView = KeyguardPINView.this;
                int i = KeyguardPINView.$r8$clinit;
                keyguardPINView.enableClipping(true);
            }
        };
        Objects.requireNonNull(appearAnimationUtils3);
        appearAnimationUtils3.startAnimation2d(viewArr, runnable, appearAnimationUtils3);
    }

    public KeyguardPINView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mLastDevicePosture = 0;
        this.mAppearAnimationUtils = new AppearAnimationUtils(context, 220L, 1.0f, 1.0f, AnimationUtils.loadInterpolator(context, 17563662));
        this.mDisappearAnimationUtils = new DisappearAnimationUtils(context, 125L, 0.6f, 0.45f, AnimationUtils.loadInterpolator(((LinearLayout) this).mContext, 17563663));
        this.mDisappearAnimationUtilsLocked = new DisappearAnimationUtils(context, 187L, 0.6f, 0.45f, AnimationUtils.loadInterpolator(((LinearLayout) this).mContext, 17563663));
        this.mDisappearYTranslation = getResources().getDimensionPixelSize(2131165665);
    }

    public final void enableClipping(boolean z) {
        this.mContainer.setClipToPadding(z);
        this.mContainer.setClipChildren(z);
        setClipChildren(z);
    }

    public final void updateMargins() {
        int dimensionPixelSize = ((LinearLayout) this).mContext.getResources().getDimensionPixelSize(2131166685);
        int dimensionPixelSize2 = ((LinearLayout) this).mContext.getResources().getDimensionPixelSize(2131166686);
        String string = ((LinearLayout) this).mContext.getResources().getString(2131952930);
        for (int i = 1; i < 5; i++) {
            for (int i2 = 0; i2 < 3; i2++) {
                View view = this.mViews[i][i2];
                ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) view.getLayoutParams();
                layoutParams.dimensionRatio = string;
                if (i != 4) {
                    ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin = dimensionPixelSize;
                }
                if (i2 != 2) {
                    ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin = dimensionPixelSize2;
                }
                view.setLayoutParams(layoutParams);
            }
        }
        float f = ((LinearLayout) this).mContext.getResources().getFloat(2131165793);
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.mContainer);
        if (this.mLastDevicePosture != 2) {
            f = 0.0f;
        }
        constraintSet.get(2131428591).layout.guidePercent = f;
        constraintSet.get(2131428591).layout.guideEnd = -1;
        constraintSet.get(2131428591).layout.guideBegin = -1;
        constraintSet.applyTo(this.mContainer);
    }

    @Override // com.android.keyguard.KeyguardPinBasedInputView, com.android.keyguard.KeyguardAbsKeyInputView, android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mContainer = (ConstraintLayout) findViewById(2131428590);
        this.mViews = new View[][]{new View[]{findViewById(2131428720), null, null}, new View[]{findViewById(2131428149), findViewById(2131428150), findViewById(2131428151)}, new View[]{findViewById(2131428152), findViewById(2131428153), findViewById(2131428154)}, new View[]{findViewById(2131428155), findViewById(2131428156), findViewById(2131428157)}, new View[]{findViewById(2131427813), findViewById(2131428148), findViewById(2131428158)}, new View[]{null, this.mEcaView, null}};
    }

    @Override // android.view.View
    public final void onConfigurationChanged(Configuration configuration) {
        updateMargins();
    }
}

package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.util.AttributeSet;
import com.android.keyguard.AlphaOptimizedLinearLayout;
import com.android.keyguard.KeyguardConstants;
import com.android.settingslib.animation.AppearAnimationUtils;
import com.android.settingslib.animation.DisappearAnimationUtils;
import com.android.systemui.animation.Interpolators;
/* loaded from: classes.dex */
public class KeyguardUserSwitcherListView extends AlphaOptimizedLinearLayout {
    public static final boolean DEBUG = KeyguardConstants.DEBUG;
    public boolean mAnimating;
    public final AppearAnimationUtils mAppearAnimationUtils;
    public final DisappearAnimationUtils mDisappearAnimationUtils;

    public KeyguardUserSwitcherListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mAppearAnimationUtils = new AppearAnimationUtils(context, 220L, -0.5f, 0.5f, Interpolators.FAST_OUT_SLOW_IN);
        this.mDisappearAnimationUtils = new DisappearAnimationUtils(context, 220L, 0.2f, 0.2f, Interpolators.FAST_OUT_SLOW_IN_REVERSE);
    }
}

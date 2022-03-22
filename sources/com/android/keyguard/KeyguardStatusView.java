package com.android.keyguard;

import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import androidx.core.graphics.ColorUtils;
import com.android.internal.widget.LockPatternUtils;
import com.android.keyguard.KeyguardSliceView;
import com.android.systemui.plugins.ClockPlugin;
import com.android.systemui.util.wakelock.KeepAwakeAnimationListener;
import java.util.Objects;
import java.util.Set;
/* loaded from: classes.dex */
public class KeyguardStatusView extends GridLayout {
    public static final /* synthetic */ int $r8$clinit = 0;
    public float mChildrenAlphaExcludingSmartSpace;
    public KeyguardClockSwitch mClockView;
    public float mDarkAmount;
    public KeyguardSliceView mKeyguardSlice;
    public View mMediaHostContainer;
    public boolean mShowingHeader;
    public ViewGroup mStatusViewContainer;
    public int mTextColor;

    public KeyguardStatusView(Context context) {
        this(context, null, 0);
    }

    public KeyguardStatusView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public final void setChildrenAlphaExcludingClockView(float f) {
        Set of = Set.of(this.mClockView);
        this.mChildrenAlphaExcludingSmartSpace = f;
        for (int i = 0; i < this.mStatusViewContainer.getChildCount(); i++) {
            View childAt = this.mStatusViewContainer.getChildAt(i);
            if (!of.contains(childAt)) {
                childAt.setAlpha(f);
            }
        }
    }

    public final void updateDark() {
        boolean z;
        KeepAwakeAnimationListener keepAwakeAnimationListener;
        int blendARGB = ColorUtils.blendARGB(this.mTextColor, -1, this.mDarkAmount);
        KeyguardSliceView keyguardSliceView = this.mKeyguardSlice;
        float f = this.mDarkAmount;
        Objects.requireNonNull(keyguardSliceView);
        keyguardSliceView.mDarkAmount = f;
        KeyguardSliceView.Row row = keyguardSliceView.mRow;
        Objects.requireNonNull(row);
        boolean z2 = true;
        if (f != 0.0f) {
            z = true;
        } else {
            z = false;
        }
        if (row.mDarkAmount == 0.0f) {
            z2 = false;
        }
        if (z != z2) {
            row.mDarkAmount = f;
            if (z) {
                keepAwakeAnimationListener = null;
            } else {
                keepAwakeAnimationListener = row.mKeepAwakeListener;
            }
            row.setLayoutAnimationListener(keepAwakeAnimationListener);
        }
        keyguardSliceView.updateTextColors();
        KeyguardClockSwitch keyguardClockSwitch = this.mClockView;
        Objects.requireNonNull(keyguardClockSwitch);
        ClockPlugin clockPlugin = keyguardClockSwitch.mClockPlugin;
        if (clockPlugin != null) {
            clockPlugin.setTextColor(blendARGB);
        }
    }

    public KeyguardStatusView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mDarkAmount = 0.0f;
        this.mChildrenAlphaExcludingSmartSpace = 1.0f;
        ActivityManager.getService();
        new LockPatternUtils(getContext());
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mStatusViewContainer = (ViewGroup) findViewById(2131428934);
        this.mClockView = (KeyguardClockSwitch) findViewById(2131428170);
        Context context = ((GridLayout) this).mContext;
        int i = KeyguardClockAccessibilityDelegate.$r8$clinit;
        if (!TextUtils.isEmpty(context.getString(2131952536))) {
            this.mClockView.setAccessibilityDelegate(new KeyguardClockAccessibilityDelegate(((GridLayout) this).mContext));
        }
        this.mKeyguardSlice = (KeyguardSliceView) findViewById(2131428193);
        KeyguardClockSwitch keyguardClockSwitch = this.mClockView;
        Objects.requireNonNull(keyguardClockSwitch);
        this.mTextColor = keyguardClockSwitch.mClockView.getCurrentTextColor();
        KeyguardSliceView keyguardSliceView = this.mKeyguardSlice;
        KeyguardStatusView$$ExternalSyntheticLambda0 keyguardStatusView$$ExternalSyntheticLambda0 = new KeyguardStatusView$$ExternalSyntheticLambda0(this, 0);
        Objects.requireNonNull(keyguardSliceView);
        keyguardSliceView.mContentChangeListener = keyguardStatusView$$ExternalSyntheticLambda0;
        KeyguardSliceView keyguardSliceView2 = this.mKeyguardSlice;
        Objects.requireNonNull(keyguardSliceView2);
        boolean z = keyguardSliceView2.mHasHeader;
        if (this.mShowingHeader != z) {
            this.mShowingHeader = z;
        }
        this.mMediaHostContainer = findViewById(2131428935);
        updateDark();
    }

    static {
        boolean z = KeyguardConstants.DEBUG;
    }
}

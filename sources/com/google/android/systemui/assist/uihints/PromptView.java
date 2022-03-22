package com.google.android.systemui.assist.uihints;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import com.android.systemui.Dependency;
import com.android.systemui.statusbar.policy.ConfigurationController;
/* loaded from: classes.dex */
public class PromptView extends TextView implements ConfigurationController.ConfigurationListener {
    public final DecelerateInterpolator mDecelerateInterpolator;
    public boolean mEnabled;
    public String mHandleString;
    public boolean mHasDarkBackground;
    public int mLastInvocationType;
    public final float mRiseDistance;
    public String mSqueezeString;
    public final int mTextColorDark;
    public final int mTextColorLight;

    public PromptView(Context context) {
        this(context, null);
    }

    public final void disable$1() {
        this.mEnabled = false;
        setVisibility(8);
    }

    public PromptView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public final void onDensityOrFontScaleChanged() {
        setTextSize(0, ((TextView) this).mContext.getResources().getDimension(2131167244));
        updateViewHeight();
    }

    public PromptView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public final void onConfigChanged(Configuration configuration) {
        this.mHandleString = getResources().getString(2131952425);
        this.mSqueezeString = getResources().getString(2131953309);
    }

    public final void updateViewHeight() {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams != null) {
            layoutParams.height = (int) (((TextView) this).mContext.getResources().getDimension(2131167244) + getResources().getDimension(2131165345) + this.mRiseDistance);
        }
        requestLayout();
    }

    public PromptView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mDecelerateInterpolator = new DecelerateInterpolator(2.0f);
        this.mHasDarkBackground = false;
        this.mEnabled = false;
        this.mLastInvocationType = 0;
        int color = getContext().getColor(2131100753);
        this.mTextColorDark = color;
        int color2 = getContext().getColor(2131100754);
        this.mTextColorLight = color2;
        this.mRiseDistance = getResources().getDimension(2131165344);
        this.mHandleString = getResources().getString(2131952425);
        this.mSqueezeString = getResources().getString(2131953309);
        ((ConfigurationController) Dependency.get(ConfigurationController.class)).addCallback(this);
        boolean z = this.mHasDarkBackground;
        boolean z2 = !z;
        if (z2 != z) {
            setTextColor(!z2 ? color2 : color);
            this.mHasDarkBackground = z2;
        }
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        updateViewHeight();
    }
}

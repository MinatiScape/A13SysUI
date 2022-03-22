package com.android.keyguard.clock;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.MathUtils;
import android.view.View;
import android.widget.FrameLayout;
import com.android.systemui.R$anim;
/* loaded from: classes.dex */
public class ClockLayout extends FrameLayout {
    public View mAnalogClock;
    public int mBurnInPreventionOffsetX;
    public int mBurnInPreventionOffsetY;

    public ClockLayout(Context context) {
        this(context, null);
    }

    public ClockLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ClockLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mAnalogClock = findViewById(2131427487);
        Resources resources = getResources();
        this.mBurnInPreventionOffsetX = resources.getDimensionPixelSize(2131165464);
        this.mBurnInPreventionOffsetY = resources.getDimensionPixelSize(2131165465);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        float lerp = MathUtils.lerp(0.0f, R$anim.getBurnInOffset(this.mBurnInPreventionOffsetX * 2, true) - this.mBurnInPreventionOffsetX, 0.0f);
        float lerp2 = MathUtils.lerp(0.0f, R$anim.getBurnInOffset(this.mBurnInPreventionOffsetY * 2, false) - (this.mBurnInPreventionOffsetY * 0.5f), 0.0f);
        View view = this.mAnalogClock;
        if (view != null) {
            view.setX((lerp * 3.0f) + Math.max(0.0f, (getWidth() - this.mAnalogClock.getWidth()) * 0.5f));
            this.mAnalogClock.setY((lerp2 * 3.0f) + Math.max(0.0f, (getHeight() - this.mAnalogClock.getHeight()) * 0.5f));
        }
    }
}

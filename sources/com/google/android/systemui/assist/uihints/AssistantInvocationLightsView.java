package com.google.android.systemui.assist.uihints;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.MathUtils;
import com.android.systemui.assist.ui.CornerPathRenderer;
import com.android.systemui.assist.ui.InvocationLightsView;
import com.android.systemui.assist.ui.PathSpecCornerPathRenderer;
import com.android.systemui.assist.ui.PerimeterPathGuide;
/* loaded from: classes.dex */
public class AssistantInvocationLightsView extends InvocationLightsView {
    public final int mColorBlue;
    public final int mColorGreen;
    public final int mColorRed;
    public final int mColorYellow;

    public AssistantInvocationLightsView(Context context) {
        this(context, null);
    }

    public AssistantInvocationLightsView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    @Override // com.android.systemui.assist.ui.InvocationLightsView
    public final CornerPathRenderer createCornerPathRenderer(Context context) {
        return new PathSpecCornerPathRenderer(context);
    }

    @Override // com.android.systemui.assist.ui.InvocationLightsView
    public final void onInvocationProgress(float f) {
        if (f <= 1.0f) {
            super.onInvocationProgress(f);
        } else {
            float regionWidth = this.mGuide.getRegionWidth(PerimeterPathGuide.Region.BOTTOM) / 4.0f;
            float lerp = MathUtils.lerp((this.mGuide.getRegionWidth(PerimeterPathGuide.Region.BOTTOM_LEFT) * 0.6f) / 2.0f, regionWidth, 1.0f - (f - 1.0f));
            setLight(0, regionWidth - lerp, regionWidth);
            float f2 = 2.0f * regionWidth;
            setLight(1, regionWidth, f2);
            float f3 = regionWidth * 3.0f;
            setLight(2, f2, f3);
            setLight(3, f3, lerp + f3);
            setVisibility(0);
        }
        invalidate();
    }

    public AssistantInvocationLightsView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public AssistantInvocationLightsView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        Resources resources = context.getResources();
        this.mColorRed = resources.getColor(2131099855);
        this.mColorYellow = resources.getColor(2131099856);
        this.mColorBlue = resources.getColor(2131099853);
        this.mColorGreen = resources.getColor(2131099854);
    }
}

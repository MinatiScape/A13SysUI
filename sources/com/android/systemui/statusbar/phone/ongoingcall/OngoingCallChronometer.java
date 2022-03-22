package com.android.systemui.statusbar.phone.ongoingcall;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Chronometer;
import kotlin.jvm.internal.DefaultConstructorMarker;
/* compiled from: OngoingCallChronometer.kt */
/* loaded from: classes.dex */
public final class OngoingCallChronometer extends Chronometer {
    public int minimumTextWidth;
    public boolean shouldHideText;

    public OngoingCallChronometer(Context context) {
        this(context, null, 0, 6, null);
    }

    public OngoingCallChronometer(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 4, null);
    }

    public /* synthetic */ OngoingCallChronometer(Context context, AttributeSet attributeSet, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i2 & 2) != 0 ? null : attributeSet, (i2 & 4) != 0 ? 0 : i);
    }

    @Override // android.widget.Chronometer
    public final void setBase(long j) {
        this.minimumTextWidth = 0;
        this.shouldHideText = false;
        setVisibility(0);
        super.setBase(j);
    }

    public OngoingCallChronometer(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override // android.widget.TextView, android.view.View
    public final void onMeasure(int i, int i2) {
        if (this.shouldHideText) {
            setMeasuredDimension(0, 0);
            return;
        }
        super.onMeasure(View.MeasureSpec.makeMeasureSpec(0, 0), i2);
        int measuredWidth = getMeasuredWidth();
        if (measuredWidth > View.resolveSize(measuredWidth, i)) {
            this.shouldHideText = true;
            setVisibility(8);
            setMeasuredDimension(0, 0);
            return;
        }
        int i3 = this.minimumTextWidth;
        if (measuredWidth < i3) {
            measuredWidth = i3;
        }
        this.minimumTextWidth = measuredWidth;
        setMeasuredDimension(measuredWidth, View.MeasureSpec.getSize(i2));
    }
}

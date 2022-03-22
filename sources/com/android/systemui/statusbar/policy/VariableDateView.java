package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import com.android.systemui.R$styleable;
/* compiled from: VariableDateView.kt */
/* loaded from: classes.dex */
public final class VariableDateView extends TextView {
    public boolean freezeSwitching;
    public final String longerPattern;
    public OnMeasureListener onMeasureListener;
    public final String shorterPattern;

    /* compiled from: VariableDateView.kt */
    /* loaded from: classes.dex */
    public interface OnMeasureListener {
        void onMeasureAction(int i);
    }

    public VariableDateView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R$styleable.VariableDateView, 0, 0);
        String string = obtainStyledAttributes.getString(0);
        this.longerPattern = string == null ? context.getString(2131953348) : string;
        String string2 = obtainStyledAttributes.getString(1);
        this.shorterPattern = string2 == null ? context.getString(2131951616) : string2;
        obtainStyledAttributes.recycle();
    }

    @Override // android.widget.TextView, android.view.View
    public final void onMeasure(int i, int i2) {
        OnMeasureListener onMeasureListener;
        int size = (View.MeasureSpec.getSize(i) - getPaddingStart()) - getPaddingEnd();
        if (!(View.MeasureSpec.getMode(i) == 0 || this.freezeSwitching || (onMeasureListener = this.onMeasureListener) == null)) {
            onMeasureListener.onMeasureAction(size);
        }
        super.onMeasure(i, i2);
    }
}

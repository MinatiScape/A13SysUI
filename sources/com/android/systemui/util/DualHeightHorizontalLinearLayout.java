package com.android.systemui.util;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.systemui.R$styleable;
import kotlin.jvm.internal.DefaultConstructorMarker;
/* compiled from: DualHeightHorizontalLinearLayout.kt */
/* loaded from: classes.dex */
public final class DualHeightHorizontalLinearLayout extends LinearLayout {
    public boolean alwaysSingleLine;
    public int initialPadding;
    public int singleLineHeightPx;
    public final TypedValue singleLineHeightValue;
    public int singleLineVerticalPaddingPx;
    public final TypedValue singleLineVerticalPaddingValue;
    public TextView textView;
    public final int textViewId;

    public DualHeightHorizontalLinearLayout(Context context) {
        this(context, null, 0, 0, 14, null);
    }

    public DualHeightHorizontalLinearLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0, 12, null);
    }

    public DualHeightHorizontalLinearLayout(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0, 8, null);
    }

    public /* synthetic */ DualHeightHorizontalLinearLayout(Context context, AttributeSet attributeSet, int i, int i2, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i3 & 2) != 0 ? null : attributeSet, (i3 & 4) != 0 ? 0 : i, (i3 & 8) != 0 ? 0 : i2);
    }

    @Override // android.widget.LinearLayout
    public final void setOrientation(int i) {
        if (i != 1) {
            super.setOrientation(i);
            return;
        }
        throw new IllegalStateException("This view should always have horizontal orientation");
    }

    public DualHeightHorizontalLinearLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.initialPadding = ((LinearLayout) this).mPaddingTop;
        if (getOrientation() == 0) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.DualHeightHorizontalLinearLayout, i, i2);
            TypedValue typedValue = new TypedValue();
            TypedValue typedValue2 = null;
            if (obtainStyledAttributes.hasValue(0)) {
                obtainStyledAttributes.getValue(0, typedValue);
            } else {
                typedValue = null;
            }
            this.singleLineHeightValue = typedValue;
            TypedValue typedValue3 = new TypedValue();
            if (obtainStyledAttributes.hasValue(1)) {
                obtainStyledAttributes.getValue(1, typedValue3);
                typedValue2 = typedValue3;
            }
            this.singleLineVerticalPaddingValue = typedValue2;
            this.textViewId = obtainStyledAttributes.getResourceId(2, 0);
            obtainStyledAttributes.recycle();
            updateResources();
            return;
        }
        throw new IllegalStateException("This view should always have horizontal orientation");
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [com.android.systemui.util.DualHeightHorizontalLinearLayout$updateResources$2] */
    /* JADX WARN: Type inference failed for: r2v1, types: [com.android.systemui.util.DualHeightHorizontalLinearLayout$updateResources$4] */
    /* JADX WARN: Unknown variable types count: 2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateResources() {
        /*
            r3 = this;
            android.util.TypedValue r0 = r3.singleLineHeightValue
            int r1 = r3.getMinimumHeight()
            com.android.systemui.util.DualHeightHorizontalLinearLayout$updateResources$2 r2 = new com.android.systemui.util.DualHeightHorizontalLinearLayout$updateResources$2
            r2.<init>(r3)
            if (r0 != 0) goto L_0x000e
            goto L_0x0033
        L_0x000e:
            int r1 = r0.resourceId
            if (r1 == 0) goto L_0x0021
            android.content.Context r1 = r3.getContext()
            android.content.res.Resources r1 = r1.getResources()
            int r0 = r0.resourceId
            int r0 = r1.getDimensionPixelSize(r0)
            goto L_0x0032
        L_0x0021:
            android.content.Context r1 = r3.getContext()
            android.content.res.Resources r1 = r1.getResources()
            android.util.DisplayMetrics r1 = r1.getDisplayMetrics()
            float r0 = r0.getDimension(r1)
            int r0 = (int) r0
        L_0x0032:
            r1 = r0
        L_0x0033:
            java.lang.Integer r0 = java.lang.Integer.valueOf(r1)
            r2.set(r0)
            android.util.TypedValue r0 = r3.singleLineVerticalPaddingValue
            r1 = 0
            com.android.systemui.util.DualHeightHorizontalLinearLayout$updateResources$4 r2 = new com.android.systemui.util.DualHeightHorizontalLinearLayout$updateResources$4
            r2.<init>(r3)
            if (r0 != 0) goto L_0x0045
            goto L_0x006a
        L_0x0045:
            int r1 = r0.resourceId
            if (r1 == 0) goto L_0x0058
            android.content.Context r3 = r3.getContext()
            android.content.res.Resources r3 = r3.getResources()
            int r0 = r0.resourceId
            int r3 = r3.getDimensionPixelSize(r0)
            goto L_0x0069
        L_0x0058:
            android.content.Context r3 = r3.getContext()
            android.content.res.Resources r3 = r3.getResources()
            android.util.DisplayMetrics r3 = r3.getDisplayMetrics()
            float r3 = r0.getDimension(r3)
            int r3 = (int) r3
        L_0x0069:
            r1 = r3
        L_0x006a:
            java.lang.Integer r3 = java.lang.Integer.valueOf(r1)
            r2.set(r3)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.util.DualHeightHorizontalLinearLayout.updateResources():void");
    }

    @Override // android.view.View
    public final void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateResources();
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        TextView textView = (TextView) findViewById(this.textViewId);
        if (textView == null) {
            textView = null;
        } else {
            textView.getMaxLines();
        }
        this.textView = textView;
    }

    @Override // android.widget.LinearLayout, android.view.View
    public final void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        TextView textView = this.textView;
        if (textView != null) {
            if (textView.getLineCount() < 2 || this.alwaysSingleLine) {
                setMeasuredDimension(getMeasuredWidth(), this.singleLineHeightPx);
                ((LinearLayout) this).mPaddingBottom = 0;
                ((LinearLayout) this).mPaddingTop = 0;
                return;
            }
            int i3 = this.initialPadding;
            ((LinearLayout) this).mPaddingBottom = i3;
            ((LinearLayout) this).mPaddingTop = i3;
        }
    }

    @Override // android.view.View
    public final void setPadding(int i, int i2, int i3, int i4) {
        super.setPadding(i, i2, i3, i4);
        this.initialPadding = i2;
    }

    @Override // android.view.View
    public final void setPaddingRelative(int i, int i2, int i3, int i4) {
        super.setPaddingRelative(i, i2, i3, i4);
        this.initialPadding = i2;
    }
}

package com.android.systemui.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;
import kotlin.jvm.internal.DefaultConstructorMarker;
/* compiled from: SafeMarqueeTextView.kt */
@SuppressLint({"AppCompatCustomView"})
/* loaded from: classes.dex */
public class SafeMarqueeTextView extends TextView {
    public boolean safelyIgnoreLayout;

    public SafeMarqueeTextView(Context context) {
        this(context, null, 0, 0, 14, null);
    }

    public SafeMarqueeTextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0, 12, null);
    }

    public SafeMarqueeTextView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0, 8, null);
    }

    public /* synthetic */ SafeMarqueeTextView(Context context, AttributeSet attributeSet, int i, int i2, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i3 & 2) != 0 ? null : attributeSet, (i3 & 4) != 0 ? 0 : i, (i3 & 8) != 0 ? 0 : i2);
    }

    public SafeMarqueeTextView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    @Override // android.view.View
    public final void requestLayout() {
        if (!this.safelyIgnoreLayout) {
            super.requestLayout();
        }
    }

    public final void startMarquee() {
        boolean z;
        boolean z2 = this.safelyIgnoreLayout;
        if (getLayoutParams().width != -2) {
            z = true;
        } else {
            z = false;
        }
        this.safelyIgnoreLayout = z;
        super.startMarquee();
        this.safelyIgnoreLayout = z2;
    }

    public final void stopMarquee() {
        boolean z;
        boolean z2 = this.safelyIgnoreLayout;
        if (getLayoutParams().width != -2) {
            z = true;
        } else {
            z = false;
        }
        this.safelyIgnoreLayout = z;
        super.stopMarquee();
        this.safelyIgnoreLayout = z2;
    }
}

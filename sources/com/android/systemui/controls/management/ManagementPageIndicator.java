package com.android.systemui.controls.management;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.android.systemui.qs.PageIndicator;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ManagementPageIndicator.kt */
/* loaded from: classes.dex */
public final class ManagementPageIndicator extends PageIndicator {
    public Function1<? super Integer, Unit> visibilityListener = ManagementPageIndicator$visibilityListener$1.INSTANCE;

    public ManagementPageIndicator(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    public final void onVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
        if (Intrinsics.areEqual(view, this)) {
            this.visibilityListener.invoke(Integer.valueOf(i));
        }
    }

    @Override // com.android.systemui.qs.PageIndicator
    public final void setLocation(float f) {
        if (getLayoutDirection() == 1) {
            super.setLocation((getChildCount() - 1) - f);
        } else {
            super.setLocation(f);
        }
    }
}

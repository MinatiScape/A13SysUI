package com.google.android.material.progressindicator;

import android.content.Context;
import android.util.AttributeSet;
/* loaded from: classes.dex */
public final class CircularProgressIndicator extends BaseProgressIndicator<CircularProgressIndicatorSpec> {
    public static final /* synthetic */ int $r8$clinit = 0;

    @Override // com.google.android.material.progressindicator.BaseProgressIndicator
    public final CircularProgressIndicatorSpec createSpec(Context context, AttributeSet attributeSet) {
        return new CircularProgressIndicatorSpec(context, attributeSet);
    }

    public CircularProgressIndicator(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 2130968786, 2132018652);
        Context context2 = getContext();
        CircularProgressIndicatorSpec circularProgressIndicatorSpec = (CircularProgressIndicatorSpec) this.spec;
        setIndeterminateDrawable(new IndeterminateDrawable(context2, circularProgressIndicatorSpec, new CircularDrawingDelegate(circularProgressIndicatorSpec), new CircularIndeterminateAnimatorDelegate(circularProgressIndicatorSpec)));
        Context context3 = getContext();
        CircularProgressIndicatorSpec circularProgressIndicatorSpec2 = (CircularProgressIndicatorSpec) this.spec;
        setProgressDrawable(new DeterminateDrawable(context3, circularProgressIndicatorSpec2, new CircularDrawingDelegate(circularProgressIndicatorSpec2)));
    }
}

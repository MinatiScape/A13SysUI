package com.android.systemui.statusbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
/* loaded from: classes.dex */
public class BackDropView extends FrameLayout {
    public BackDropView(Context context) {
        super(context);
    }

    @Override // android.view.View
    public final boolean hasOverlappingRendering() {
        return false;
    }

    public BackDropView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public BackDropView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public BackDropView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    @Override // android.view.View
    public final void onVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
    }
}

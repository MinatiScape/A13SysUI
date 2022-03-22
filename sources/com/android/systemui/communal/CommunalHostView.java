package com.android.systemui.communal;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
/* loaded from: classes.dex */
public class CommunalHostView extends FrameLayout {
    public CommunalHostView(Context context) {
        this(context, null, 0);
    }

    public CommunalHostView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CommunalHostView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }
}

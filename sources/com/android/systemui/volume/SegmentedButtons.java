package com.android.systemui.volume;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
/* loaded from: classes.dex */
public class SegmentedButtons extends LinearLayout {
    static {
        Typeface.create("sans-serif", 0);
        Typeface.create("sans-serif-medium", 0);
    }

    public SegmentedButtons(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(context);
        setOrientation(0);
        new ConfigurableTexts(context);
    }
}

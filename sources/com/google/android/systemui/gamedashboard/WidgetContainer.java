package com.google.android.systemui.gamedashboard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
/* loaded from: classes.dex */
public class WidgetContainer extends LinearLayout {
    public int mWidgetCount = 0;

    public final int addWidget(WidgetView widgetView) {
        if (this.mWidgetCount > 0) {
            LayoutInflater.from(getContext()).inflate(2131624113, this);
        }
        super.addView(widgetView);
        int i = this.mWidgetCount + 1;
        this.mWidgetCount = i;
        return i;
    }

    public WidgetContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}

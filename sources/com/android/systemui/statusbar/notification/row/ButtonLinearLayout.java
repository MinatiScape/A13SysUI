package com.android.systemui.statusbar.notification.row;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
/* loaded from: classes.dex */
public class ButtonLinearLayout extends LinearLayout {
    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    public final CharSequence getAccessibilityClassName() {
        return Button.class.getName();
    }

    public ButtonLinearLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}

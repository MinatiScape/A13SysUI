package com.google.android.apps.miphone.aiai.matchmaker.overview.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
/* loaded from: classes.dex */
public class UrlIndicatorContainerView extends LinearLayout {
    public UrlIndicatorContainerView(Context context, AttributeSet attrs) {
        super(context, attrs, 0, 0);
        LinearLayout linearLayout = (LinearLayout) View.inflate(context, 2131624642, this);
        TextView textView = (TextView) linearLayout.findViewById(2131429157);
        ImageButton imageButton = (ImageButton) linearLayout.findViewById(2131429155);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}

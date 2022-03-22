package com.google.android.setupdesign.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import com.android.systemui.qs.tileimpl.QSTileImpl;
/* loaded from: classes.dex */
public class NavigationBar extends LinearLayout implements View.OnClickListener {
    public NavigationBar(Context context) {
        super(getThemedContext(context));
        init();
    }

    public static ContextThemeWrapper getThemedContext(Context context) {
        int i;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{2130969892, 16842800, 16842801});
        boolean z = false;
        int resourceId = obtainStyledAttributes.getResourceId(0, 0);
        if (resourceId == 0) {
            float[] fArr = new float[3];
            float[] fArr2 = new float[3];
            Color.colorToHSV(obtainStyledAttributes.getColor(1, 0), fArr);
            Color.colorToHSV(obtainStyledAttributes.getColor(2, 0), fArr2);
            if (fArr[2] > fArr2[2]) {
                z = true;
            }
            if (z) {
                i = 2132017774;
            } else {
                i = 2132017775;
            }
            resourceId = i;
        }
        obtainStyledAttributes.recycle();
        return new ContextThemeWrapper(context, resourceId);
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
    }

    public NavigationBar(Context context, AttributeSet attributeSet) {
        super(getThemedContext(context), attributeSet);
        init();
    }

    public final void init() {
        if (!isInEditMode()) {
            View.inflate(getContext(), 2131624572, this);
            Button button = (Button) findViewById(2131428989);
            Button button2 = (Button) findViewById(2131428987);
            Button button3 = (Button) findViewById(2131428988);
        }
    }

    @TargetApi(QSTileImpl.H.STALE)
    public NavigationBar(Context context, AttributeSet attributeSet, int i) {
        super(getThemedContext(context), attributeSet, i);
        init();
    }
}

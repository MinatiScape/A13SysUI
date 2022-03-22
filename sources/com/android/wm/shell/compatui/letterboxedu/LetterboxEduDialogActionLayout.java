package com.android.wm.shell.compatui.letterboxedu;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.wm.shell.R;
/* loaded from: classes.dex */
class LetterboxEduDialogActionLayout extends FrameLayout {
    public LetterboxEduDialogActionLayout(Context context) {
        this(context, null);
    }

    public LetterboxEduDialogActionLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public LetterboxEduDialogActionLayout(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public LetterboxEduDialogActionLayout(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.LetterboxEduDialogActionLayout, i, i2);
        int resourceId = obtainStyledAttributes.getResourceId(0, 0);
        String string = obtainStyledAttributes.getString(1);
        obtainStyledAttributes.recycle();
        View inflate = View.inflate(getContext(), 2131624236, this);
        ((ImageView) inflate.findViewById(2131428249)).setImageResource(resourceId);
        ((TextView) inflate.findViewById(2131428250)).setText(string);
    }
}

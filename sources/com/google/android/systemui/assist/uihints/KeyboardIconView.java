package com.google.android.systemui.assist.uihints;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
/* loaded from: classes.dex */
public class KeyboardIconView extends FrameLayout {
    public final int COLOR_DARK_BACKGROUND;
    public final int COLOR_LIGHT_BACKGROUND;
    public ImageView mKeyboardIcon;

    public KeyboardIconView(Context context) {
        this(context, null);
    }

    public KeyboardIconView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public KeyboardIconView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        this.mKeyboardIcon = (ImageView) findViewById(2131428160);
    }

    public KeyboardIconView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.COLOR_DARK_BACKGROUND = getResources().getColor(2131100751);
        this.COLOR_LIGHT_BACKGROUND = getResources().getColor(2131100752);
    }
}

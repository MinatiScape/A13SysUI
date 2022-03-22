package com.google.android.systemui.gamedashboard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
@SuppressLint({"AppCompatCustomView"})
/* loaded from: classes.dex */
public class ShortcutBarButton extends ImageView {
    public ShortcutBarButton(Context context) {
        super(context);
    }

    public ShortcutBarButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    public final boolean performClick() {
        super.performClick();
        return true;
    }
}

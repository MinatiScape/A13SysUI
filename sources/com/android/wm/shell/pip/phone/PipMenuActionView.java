package com.android.wm.shell.pip.phone;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
/* loaded from: classes.dex */
public class PipMenuActionView extends FrameLayout {
    public ImageView mImageView;

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mImageView = (ImageView) findViewById(2131428115);
    }

    public PipMenuActionView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}

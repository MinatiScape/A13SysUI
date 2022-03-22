package com.android.wm.shell.common;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.widget.FrameLayout;
import android.widget.ImageView;
/* loaded from: classes.dex */
public final class DismissCircleView extends FrameLayout {
    public final ImageView mIconView;

    public DismissCircleView(Context context) {
        super(context);
        ImageView imageView = new ImageView(getContext());
        this.mIconView = imageView;
        Resources resources = getResources();
        setBackground(resources.getDrawable(2131231697));
        imageView.setImageDrawable(resources.getDrawable(2131232552));
        addView(imageView);
        int dimensionPixelSize = getResources().getDimensionPixelSize(2131165668);
        imageView.setLayoutParams(new FrameLayout.LayoutParams(dimensionPixelSize, dimensionPixelSize, 17));
    }

    @Override // android.view.View
    public final void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        int dimensionPixelSize = getResources().getDimensionPixelSize(2131165668);
        this.mIconView.setLayoutParams(new FrameLayout.LayoutParams(dimensionPixelSize, dimensionPixelSize, 17));
    }
}

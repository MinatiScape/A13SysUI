package com.google.android.setupdesign.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import androidx.core.content.ContextCompat;
/* loaded from: classes.dex */
public class IconUniformityAppImageView extends ImageView {
    public static final boolean ON_L_PLUS = true;
    public int backdropColorResId = 0;
    public final GradientDrawable backdropDrawable = new GradientDrawable();

    static {
        Float.valueOf(0.75f).floatValue();
    }

    public IconUniformityAppImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.widget.ImageView, android.view.View
    public final void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        boolean z = ON_L_PLUS;
        super.onDraw(canvas);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.backdropColorResId = 2131100733;
        GradientDrawable gradientDrawable = this.backdropDrawable;
        Context context = getContext();
        int i = this.backdropColorResId;
        Object obj = ContextCompat.sLock;
        gradientDrawable.setColor(context.getColor(i));
    }
}

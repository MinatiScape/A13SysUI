package com.google.android.material.animation;

import android.graphics.drawable.Drawable;
import android.util.Property;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public final class DrawableAlphaProperty extends Property<Drawable, Integer> {
    public static final DrawableAlphaProperty DRAWABLE_ALPHA_COMPAT = new DrawableAlphaProperty();

    public DrawableAlphaProperty() {
        super(Integer.class, "drawableAlphaCompat");
        new WeakHashMap();
    }

    @Override // android.util.Property
    public final Integer get(Drawable drawable) {
        return Integer.valueOf(drawable.getAlpha());
    }

    @Override // android.util.Property
    public final void set(Drawable drawable, Integer num) {
        drawable.setAlpha(num.intValue());
    }
}

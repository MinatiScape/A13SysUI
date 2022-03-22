package com.google.android.setupdesign.util;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import com.google.android.setupcompat.internal.TemplateLayout;
/* loaded from: classes.dex */
public final class DrawableLayoutDirectionHelper {
    @SuppressLint({"InlinedApi"})
    public static InsetDrawable createRelativeInsetDrawable(Drawable drawable, int i, int i2, TemplateLayout templateLayout) {
        boolean z = true;
        if (templateLayout.getLayoutDirection() != 1) {
            z = false;
        }
        if (z) {
            return new InsetDrawable(drawable, i2, 0, i, 0);
        }
        return new InsetDrawable(drawable, i, 0, i2, 0);
    }
}

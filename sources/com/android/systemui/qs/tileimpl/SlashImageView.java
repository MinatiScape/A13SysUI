package com.android.systemui.qs.tileimpl;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.qs.SlashDrawable;
import java.util.Objects;
/* loaded from: classes.dex */
public class SlashImageView extends ImageView {
    public boolean mAnimationEnabled = true;
    @VisibleForTesting
    public SlashDrawable mSlash;

    @Override // android.widget.ImageView
    public final void setImageDrawable(Drawable drawable) {
        if (drawable == null) {
            this.mSlash = null;
            super.setImageDrawable(null);
            return;
        }
        SlashDrawable slashDrawable = this.mSlash;
        if (slashDrawable == null) {
            setImageLevel(drawable.getLevel());
            super.setImageDrawable(drawable);
            return;
        }
        Objects.requireNonNull(slashDrawable);
        slashDrawable.mDrawable = drawable;
        drawable.setCallback(slashDrawable.getCallback());
        slashDrawable.mDrawable.setBounds(slashDrawable.getBounds());
        PorterDuff.Mode mode = slashDrawable.mTintMode;
        if (mode != null) {
            slashDrawable.mDrawable.setTintMode(mode);
        }
        ColorStateList colorStateList = slashDrawable.mTintList;
        if (colorStateList != null) {
            slashDrawable.mDrawable.setTintList(colorStateList);
        }
        slashDrawable.invalidateSelf();
    }

    public SlashImageView(Context context) {
        super(context);
    }
}

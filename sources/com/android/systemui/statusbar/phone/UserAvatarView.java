package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import com.android.settingslib.drawable.UserIconDrawable;
import com.android.systemui.R$styleable;
import java.util.Objects;
/* loaded from: classes.dex */
public class UserAvatarView extends View {
    public final UserIconDrawable mDrawable;

    public UserAvatarView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mDrawable = new UserIconDrawable(0);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.UserAvatarView, i, i2);
        int indexCount = obtainStyledAttributes.getIndexCount();
        for (int i3 = 0; i3 < indexCount; i3++) {
            int index = obtainStyledAttributes.getIndex(i3);
            if (index == 1) {
                float dimension = obtainStyledAttributes.getDimension(index, 0.0f);
                UserIconDrawable userIconDrawable = this.mDrawable;
                Objects.requireNonNull(userIconDrawable);
                userIconDrawable.mPadding = dimension;
                userIconDrawable.onBoundsChange(userIconDrawable.getBounds());
            } else if (index == 6) {
                float dimension2 = obtainStyledAttributes.getDimension(index, 0.0f);
                UserIconDrawable userIconDrawable2 = this.mDrawable;
                Objects.requireNonNull(userIconDrawable2);
                userIconDrawable2.initFramePaint();
                userIconDrawable2.mFrameWidth = dimension2;
                userIconDrawable2.mFramePaint.setStrokeWidth(dimension2);
                userIconDrawable2.onBoundsChange(userIconDrawable2.getBounds());
            } else if (index == 5) {
                float dimension3 = obtainStyledAttributes.getDimension(index, 0.0f);
                UserIconDrawable userIconDrawable3 = this.mDrawable;
                Objects.requireNonNull(userIconDrawable3);
                userIconDrawable3.initFramePaint();
                userIconDrawable3.mFramePadding = dimension3;
                userIconDrawable3.onBoundsChange(userIconDrawable3.getBounds());
            } else if (index == 4) {
                ColorStateList colorStateList = obtainStyledAttributes.getColorStateList(index);
                UserIconDrawable userIconDrawable4 = this.mDrawable;
                Objects.requireNonNull(userIconDrawable4);
                userIconDrawable4.initFramePaint();
                userIconDrawable4.mFrameColor = colorStateList;
                userIconDrawable4.invalidateSelf();
            } else if (index == 2) {
                float dimension4 = obtainStyledAttributes.getDimension(index, 0.0f);
                UserIconDrawable userIconDrawable5 = this.mDrawable;
                Objects.requireNonNull(userIconDrawable5);
                userIconDrawable5.mBadgeRadius = dimension4 * 0.5f;
                userIconDrawable5.onBoundsChange(userIconDrawable5.getBounds());
            } else if (index == 3) {
                float dimension5 = obtainStyledAttributes.getDimension(index, 0.0f);
                UserIconDrawable userIconDrawable6 = this.mDrawable;
                Objects.requireNonNull(userIconDrawable6);
                userIconDrawable6.mBadgeMargin = dimension5;
                userIconDrawable6.onBoundsChange(userIconDrawable6.getBounds());
            }
        }
        obtainStyledAttributes.recycle();
        setBackground(this.mDrawable);
    }

    public final void setDrawableWithBadge(Drawable drawable, int i) {
        if (!(drawable instanceof UserIconDrawable)) {
            UserIconDrawable userIconDrawable = this.mDrawable;
            Objects.requireNonNull(userIconDrawable);
            Drawable drawable2 = userIconDrawable.mUserDrawable;
            if (drawable2 != null) {
                drawable2.setCallback(null);
            }
            userIconDrawable.mUserIcon = null;
            userIconDrawable.mUserDrawable = drawable;
            if (drawable == null) {
                userIconDrawable.mBitmap = null;
            } else {
                drawable.setCallback(userIconDrawable);
            }
            userIconDrawable.onBoundsChange(userIconDrawable.getBounds());
            this.mDrawable.setBadgeIfManagedUser(getContext(), i);
            return;
        }
        throw new RuntimeException("Recursively adding UserIconDrawable");
    }

    @Override // android.view.View
    public final void setActivated(boolean z) {
        super.setActivated(z);
        this.mDrawable.invalidateSelf();
    }

    public UserAvatarView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public UserAvatarView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public UserAvatarView(Context context) {
        this(context, null);
    }
}

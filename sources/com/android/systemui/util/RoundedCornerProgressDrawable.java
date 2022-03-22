package com.android.systemui.util;

import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.graphics.drawable.InsetDrawable;
import java.util.Objects;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: RoundedCornerProgressDrawable.kt */
/* loaded from: classes.dex */
public final class RoundedCornerProgressDrawable extends InsetDrawable {

    /* compiled from: RoundedCornerProgressDrawable.kt */
    /* loaded from: classes.dex */
    public static final class RoundedCornerState extends Drawable.ConstantState {
        public final Drawable.ConstantState wrappedState;

        @Override // android.graphics.drawable.Drawable.ConstantState
        public final boolean canApplyTheme() {
            return true;
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public final Drawable newDrawable() {
            return newDrawable(null, null);
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public final int getChangingConfigurations() {
            return this.wrappedState.getChangingConfigurations();
        }

        @Override // android.graphics.drawable.Drawable.ConstantState
        public final Drawable newDrawable(Resources resources, Resources.Theme theme) {
            Drawable newDrawable = this.wrappedState.newDrawable(resources, theme);
            Objects.requireNonNull(newDrawable, "null cannot be cast to non-null type android.graphics.drawable.DrawableWrapper");
            return new RoundedCornerProgressDrawable(((DrawableWrapper) newDrawable).getDrawable());
        }

        public RoundedCornerState(Drawable.ConstantState constantState) {
            this.wrappedState = constantState;
        }
    }

    public RoundedCornerProgressDrawable() {
        this(null, 1, null);
    }

    public /* synthetic */ RoundedCornerProgressDrawable(Drawable drawable, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this((i & 1) != 0 ? null : drawable);
    }

    public RoundedCornerProgressDrawable(Drawable drawable) {
        super(drawable, 0);
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public final Drawable.ConstantState getConstantState() {
        Drawable.ConstantState constantState = super.getConstantState();
        Intrinsics.checkNotNull(constantState);
        return new RoundedCornerState(constantState);
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public final boolean canApplyTheme() {
        boolean z;
        Drawable drawable = getDrawable();
        if (drawable == null) {
            z = false;
        } else {
            z = drawable.canApplyTheme();
        }
        if (z || super.canApplyTheme()) {
            return true;
        }
        return false;
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public final int getChangingConfigurations() {
        return super.getChangingConfigurations() | 4096;
    }

    @Override // android.graphics.drawable.InsetDrawable, android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public final void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        onLevelChange(getLevel());
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public final boolean onLayoutDirectionChanged(int i) {
        onLevelChange(getLevel());
        return super.onLayoutDirectionChanged(i);
    }

    @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
    public final boolean onLevelChange(int i) {
        Rect rect;
        Drawable drawable = getDrawable();
        if (drawable == null) {
            rect = null;
        } else {
            rect = drawable.getBounds();
        }
        Intrinsics.checkNotNull(rect);
        int width = (((getBounds().width() - getBounds().height()) * i) / 10000) + getBounds().height();
        Drawable drawable2 = getDrawable();
        if (drawable2 != null) {
            drawable2.setBounds(getBounds().left, rect.top, getBounds().left + width, rect.bottom);
        }
        return super.onLevelChange(i);
    }
}

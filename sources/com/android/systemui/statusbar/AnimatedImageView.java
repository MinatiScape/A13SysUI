package com.android.systemui.statusbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.RemotableViewMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;
import com.android.systemui.R$styleable;
@RemoteViews.RemoteView
/* loaded from: classes.dex */
public class AnimatedImageView extends ImageView {
    public boolean mAllowAnimation;
    public AnimationDrawable mAnim;
    public boolean mAttached;
    public int mDrawableId;
    public final boolean mHasOverlappingRendering;

    public AnimatedImageView(Context context) {
        this(context, null);
    }

    public AnimatedImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mAllowAnimation = true;
        TypedArray obtainStyledAttributes = context.getTheme().obtainStyledAttributes(attributeSet, R$styleable.AnimatedImageView, 0, 0);
        try {
            this.mHasOverlappingRendering = obtainStyledAttributes.getBoolean(0, true);
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    @Override // android.widget.ImageView
    public final void setImageDrawable(Drawable drawable) {
        if (drawable == null) {
            this.mDrawableId = 0;
        } else if (this.mDrawableId != drawable.hashCode()) {
            this.mDrawableId = drawable.hashCode();
        } else {
            return;
        }
        super.setImageDrawable(drawable);
        updateAnim();
    }

    @Override // android.widget.ImageView
    @RemotableViewMethod
    public final void setImageResource(int i) {
        if (this.mDrawableId != i) {
            this.mDrawableId = i;
            super.setImageResource(i);
            updateAnim();
        }
    }

    @Override // android.widget.ImageView, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mAttached = true;
        updateAnim();
    }

    @Override // android.widget.ImageView, android.view.View
    public final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        AnimationDrawable animationDrawable = this.mAnim;
        if (animationDrawable != null) {
            animationDrawable.stop();
        }
        this.mAttached = false;
    }

    @Override // android.view.View
    public final void onVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
        if (this.mAnim == null) {
            return;
        }
        if (!isShown() || !this.mAllowAnimation) {
            this.mAnim.stop();
        } else {
            this.mAnim.start();
        }
    }

    public final void updateAnim() {
        AnimationDrawable animationDrawable;
        Drawable drawable = getDrawable();
        if (this.mAttached && (animationDrawable = this.mAnim) != null) {
            animationDrawable.stop();
        }
        if (drawable instanceof AnimationDrawable) {
            this.mAnim = (AnimationDrawable) drawable;
            if (isShown() && this.mAllowAnimation) {
                this.mAnim.start();
                return;
            }
            return;
        }
        this.mAnim = null;
    }

    @Override // android.widget.ImageView, android.view.View
    public boolean hasOverlappingRendering() {
        return this.mHasOverlappingRendering;
    }
}

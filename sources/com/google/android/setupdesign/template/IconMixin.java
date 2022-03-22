package com.google.android.setupdesign.template;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.google.android.setupcompat.internal.TemplateLayout;
import com.google.android.setupcompat.template.Mixin;
import com.google.android.setupdesign.R$styleable;
/* loaded from: classes.dex */
public final class IconMixin implements Mixin {
    public final int originalHeight;
    public final ImageView.ScaleType originalScaleType;
    public final TemplateLayout templateLayout;

    public final ImageView getView() {
        return (ImageView) this.templateLayout.findManagedViewById(2131428977);
    }

    public IconMixin(TemplateLayout templateLayout, AttributeSet attributeSet, int i) {
        ImageView view;
        ImageView.ScaleType scaleType;
        ImageView view2;
        int i2;
        this.templateLayout = templateLayout;
        Context context = templateLayout.getContext();
        ImageView view3 = getView();
        if (view3 != null) {
            this.originalHeight = view3.getLayoutParams().height;
            this.originalScaleType = view3.getScaleType();
        } else {
            this.originalHeight = 0;
            this.originalScaleType = null;
        }
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.SudIconMixin, i, 0);
        int resourceId = obtainStyledAttributes.getResourceId(0, 0);
        if (!(resourceId == 0 || (view2 = getView()) == null)) {
            view2.setImageResource(resourceId);
            if (resourceId != 0) {
                i2 = 0;
            } else {
                i2 = 8;
            }
            view2.setVisibility(i2);
            int visibility = view2.getVisibility();
            if (((FrameLayout) templateLayout.findManagedViewById(2131428978)) != null) {
                ((FrameLayout) templateLayout.findManagedViewById(2131428978)).setVisibility(visibility);
            }
        }
        boolean z = obtainStyledAttributes.getBoolean(2, false);
        ImageView view4 = getView();
        if (view4 != null) {
            ViewGroup.LayoutParams layoutParams = view4.getLayoutParams();
            layoutParams.height = !z ? this.originalHeight : view4.getMaxHeight();
            view4.setLayoutParams(layoutParams);
            if (z) {
                scaleType = ImageView.ScaleType.FIT_CENTER;
            } else {
                scaleType = this.originalScaleType;
            }
            view4.setScaleType(scaleType);
        }
        int color = obtainStyledAttributes.getColor(1, 0);
        if (!(color == 0 || (view = getView()) == null)) {
            view.setColorFilter(color);
        }
        obtainStyledAttributes.recycle();
    }
}

package com.google.android.systemui.smartspace;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.content.res.AppCompatResources;
/* loaded from: classes.dex */
public class PageIndicator extends LinearLayout {
    public int mPrimaryColor = getAttrColor(getContext());
    public int mCurrentPageIndex = -1;
    public int mNumPages = -1;

    public PageIndicator(Context context) {
        super(context);
    }

    public static int getAttrColor(Context context) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{16842806});
        int color = obtainStyledAttributes.getColor(0, 0);
        obtainStyledAttributes.recycle();
        return color;
    }

    public final void setNumPages(int i) {
        ImageView imageView;
        LinearLayout.LayoutParams layoutParams;
        float f;
        if (i <= 0) {
            Log.w("PageIndicator", "Total number of pages invalid: " + i + ". Assuming 1 page.");
            i = 1;
        }
        if (i < 2) {
            setVisibility(8);
            return;
        }
        setVisibility(0);
        if (i != this.mNumPages) {
            this.mNumPages = i;
            int childCount = getChildCount() - this.mNumPages;
            for (int i2 = 0; i2 < childCount; i2++) {
                removeViewAt(0);
            }
            int dimensionPixelSize = getContext().getResources().getDimensionPixelSize(2131166753);
            for (int i3 = 0; i3 < this.mNumPages; i3++) {
                if (i3 < getChildCount()) {
                    imageView = (ImageView) getChildAt(i3);
                } else {
                    imageView = new ImageView(getContext());
                }
                if (i3 < getChildCount()) {
                    layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                } else {
                    layoutParams = new LinearLayout.LayoutParams(-2, -2);
                }
                if (i3 == 0) {
                    layoutParams.setMarginStart(0);
                } else {
                    layoutParams.setMarginStart(dimensionPixelSize);
                }
                if (i3 == this.mNumPages - 1) {
                    layoutParams.setMarginEnd(0);
                } else {
                    layoutParams.setMarginEnd(dimensionPixelSize);
                }
                if (i3 < getChildCount()) {
                    imageView.setLayoutParams(layoutParams);
                } else {
                    Drawable drawable = AppCompatResources.getDrawable(getContext(), 2131232540);
                    drawable.setTint(this.mPrimaryColor);
                    imageView.setImageDrawable(drawable);
                    addView(imageView, layoutParams);
                }
                int i4 = this.mCurrentPageIndex;
                if (i4 < 0) {
                    this.mCurrentPageIndex = 0;
                } else {
                    int i5 = this.mNumPages;
                    if (i4 >= i5) {
                        this.mCurrentPageIndex = i5 - 1;
                    }
                }
                if (i3 == this.mCurrentPageIndex) {
                    f = 1.0f;
                } else {
                    f = 0.4f;
                }
                imageView.setAlpha(f);
            }
            setContentDescription(getContext().getString(2131951811, 1, Integer.valueOf(this.mNumPages)));
        }
    }

    public PageIndicator(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public PageIndicator(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public PageIndicator(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }
}

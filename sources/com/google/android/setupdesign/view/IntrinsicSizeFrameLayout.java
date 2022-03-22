package com.google.android.setupdesign.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.template.FooterBarMixin$$ExternalSyntheticOutline0;
import com.google.android.setupdesign.R$styleable;
/* loaded from: classes.dex */
public class IntrinsicSizeFrameLayout extends FrameLayout {
    public WindowInsets lastInsets;
    public int intrinsicHeight = 0;
    public int intrinsicWidth = 0;
    public final Rect windowVisibleDisplayRect = new Rect();

    public IntrinsicSizeFrameLayout(Context context) {
        super(context);
        init(context, null, 0);
    }

    public final int getIntrinsicMeasureSpec(int i, int i2) {
        if (i2 <= 0) {
            return i;
        }
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        if (mode == 0) {
            return View.MeasureSpec.makeMeasureSpec(this.intrinsicHeight, 1073741824);
        }
        if (mode == Integer.MIN_VALUE) {
            return View.MeasureSpec.makeMeasureSpec(Math.min(size, this.intrinsicHeight), 1073741824);
        }
        return i;
    }

    public boolean isWindowSizeSmallerThanDisplaySize() {
        getWindowVisibleDisplayFrame(this.windowVisibleDisplayRect);
        Display display = getDisplay();
        if (display != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            display.getRealMetrics(displayMetrics);
            if (this.windowVisibleDisplayRect.width() > 0 && this.windowVisibleDisplayRect.width() < displayMetrics.widthPixels) {
                return true;
            }
        }
        return false;
    }

    @Override // android.view.View
    public final WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        this.lastInsets = windowInsets;
        return super.onApplyWindowInsets(windowInsets);
    }

    @Override // android.view.View
    public final void setLayoutParams(ViewGroup.LayoutParams layoutParams) {
        if (this.intrinsicHeight == 0 && this.intrinsicWidth == 0) {
            layoutParams.width = -1;
            layoutParams.height = -1;
        }
        super.setLayoutParams(layoutParams);
    }

    public final void init(Context context, AttributeSet attributeSet, int i) {
        if (!isInEditMode()) {
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.SudIntrinsicSizeFrameLayout, i, 0);
            this.intrinsicHeight = obtainStyledAttributes.getDimensionPixelSize(0, 0);
            this.intrinsicWidth = obtainStyledAttributes.getDimensionPixelSize(1, 0);
            obtainStyledAttributes.recycle();
            PartnerConfigHelper partnerConfigHelper = PartnerConfigHelper.get(context);
            PartnerConfig partnerConfig = PartnerConfig.CONFIG_CARD_VIEW_INTRINSIC_HEIGHT;
            if (partnerConfigHelper.isPartnerConfigAvailable(partnerConfig)) {
                this.intrinsicHeight = (int) FooterBarMixin$$ExternalSyntheticOutline0.m(context, context, partnerConfig, 0.0f);
            }
            PartnerConfigHelper partnerConfigHelper2 = PartnerConfigHelper.get(context);
            PartnerConfig partnerConfig2 = PartnerConfig.CONFIG_CARD_VIEW_INTRINSIC_WIDTH;
            if (partnerConfigHelper2.isPartnerConfigAvailable(partnerConfig2)) {
                this.intrinsicWidth = (int) FooterBarMixin$$ExternalSyntheticOutline0.m(context, context, partnerConfig2, 0.0f);
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.lastInsets == null) {
            requestApplyInsets();
        }
    }

    @Override // android.widget.FrameLayout, android.view.View
    public final void onMeasure(int i, int i2) {
        int i3;
        if (isWindowSizeSmallerThanDisplaySize()) {
            getWindowVisibleDisplayFrame(this.windowVisibleDisplayRect);
            i3 = View.MeasureSpec.makeMeasureSpec(this.windowVisibleDisplayRect.width(), 1073741824);
        } else {
            i3 = getIntrinsicMeasureSpec(i, this.intrinsicWidth);
        }
        super.onMeasure(i3, getIntrinsicMeasureSpec(i2, this.intrinsicHeight));
    }

    public IntrinsicSizeFrameLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context, attributeSet, 0);
    }

    @TargetApi(QSTileImpl.H.STALE)
    public IntrinsicSizeFrameLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(context, attributeSet, i);
    }
}

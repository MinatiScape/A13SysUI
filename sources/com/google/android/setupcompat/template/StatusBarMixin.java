package com.google.android.setupcompat.template;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import com.android.systemui.R$id;
import com.google.android.setupcompat.PartnerCustomizationLayout;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.view.StatusBarBackgroundLayout;
import java.util.Objects;
/* loaded from: classes.dex */
public final class StatusBarMixin implements Mixin {
    public final View decorView;
    public LinearLayout linearLayout;
    public final PartnerCustomizationLayout partnerCustomizationLayout;
    public StatusBarBackgroundLayout statusBarLayout;

    public final void setStatusBarBackground(Drawable drawable) {
        boolean z;
        if (this.partnerCustomizationLayout.shouldApplyPartnerResource() && !this.partnerCustomizationLayout.useFullDynamicColor()) {
            Context context = this.partnerCustomizationLayout.getContext();
            drawable = PartnerConfigHelper.get(context).getDrawable(context, PartnerConfig.CONFIG_STATUS_BAR_BACKGROUND);
        }
        StatusBarBackgroundLayout statusBarBackgroundLayout = this.statusBarLayout;
        if (statusBarBackgroundLayout == null) {
            this.linearLayout.setBackgroundDrawable(drawable);
            return;
        }
        Objects.requireNonNull(statusBarBackgroundLayout);
        statusBarBackgroundLayout.statusBarBackground = drawable;
        boolean z2 = true;
        if (drawable == null) {
            z = true;
        } else {
            z = false;
        }
        statusBarBackgroundLayout.setWillNotDraw(z);
        if (drawable == null) {
            z2 = false;
        }
        statusBarBackgroundLayout.setFitsSystemWindows(z2);
        statusBarBackgroundLayout.invalidate();
    }

    public StatusBarMixin(PartnerCustomizationLayout partnerCustomizationLayout, Window window, AttributeSet attributeSet, int i) {
        boolean z;
        this.partnerCustomizationLayout = partnerCustomizationLayout;
        View findManagedViewById = partnerCustomizationLayout.findManagedViewById(2131428953);
        Objects.requireNonNull(findManagedViewById, "sucLayoutStatus cannot be null in StatusBarMixin");
        if (findManagedViewById instanceof StatusBarBackgroundLayout) {
            this.statusBarLayout = (StatusBarBackgroundLayout) findManagedViewById;
        } else {
            this.linearLayout = (LinearLayout) findManagedViewById;
        }
        View decorView = window.getDecorView();
        this.decorView = decorView;
        window.setStatusBarColor(0);
        TypedArray obtainStyledAttributes = partnerCustomizationLayout.getContext().obtainStyledAttributes(attributeSet, R$id.SucStatusBarMixin, i, 0);
        if ((decorView.getSystemUiVisibility() & 8192) == 8192) {
            z = true;
        } else {
            z = false;
        }
        boolean z2 = obtainStyledAttributes.getBoolean(0, z);
        if (partnerCustomizationLayout.shouldApplyPartnerResource()) {
            Context context = partnerCustomizationLayout.getContext();
            z2 = PartnerConfigHelper.get(context).getBoolean(context, PartnerConfig.CONFIG_LIGHT_STATUS_BAR, false);
        }
        if (z2) {
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | 8192);
        } else {
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & (-8193));
        }
        setStatusBarBackground(obtainStyledAttributes.getDrawable(1));
        obtainStyledAttributes.recycle();
    }
}

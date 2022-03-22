package com.google.android.setupcompat.template;

import android.view.Window;
import com.google.android.setupcompat.PartnerCustomizationLayout;
import com.google.android.setupcompat.internal.TemplateLayout;
/* loaded from: classes.dex */
public final class SystemNavBarMixin implements Mixin {
    public final boolean applyPartnerResources;
    public final TemplateLayout templateLayout;
    public final boolean useFullDynamicColor;
    public final Window windowOfActivity;

    public SystemNavBarMixin(TemplateLayout templateLayout, Window window) {
        boolean z;
        this.templateLayout = templateLayout;
        this.windowOfActivity = window;
        boolean z2 = templateLayout instanceof PartnerCustomizationLayout;
        boolean z3 = false;
        if (!z2 || !((PartnerCustomizationLayout) templateLayout).shouldApplyPartnerResource()) {
            z = false;
        } else {
            z = true;
        }
        this.applyPartnerResources = z;
        if (z2 && ((PartnerCustomizationLayout) templateLayout).useFullDynamicColor()) {
            z3 = true;
        }
        this.useFullDynamicColor = z3;
    }
}

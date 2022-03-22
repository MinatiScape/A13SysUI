package com.google.android.setupdesign;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.setupdesign.template.ListMixin;
import com.google.android.setupdesign.template.RequireScrollMixin;
import java.util.Objects;
/* loaded from: classes.dex */
public class GlifListLayout extends GlifLayout {
    public ListMixin listMixin;

    @Override // com.google.android.setupdesign.GlifLayout, com.google.android.setupcompat.PartnerCustomizationLayout, com.google.android.setupcompat.internal.TemplateLayout
    public final ViewGroup findContainer(int i) {
        if (i == 0) {
            i = 16908298;
        }
        return super.findContainer(i);
    }

    @Override // com.google.android.setupdesign.GlifLayout, com.google.android.setupcompat.PartnerCustomizationLayout, com.google.android.setupcompat.internal.TemplateLayout
    public final View onInflateTemplate(LayoutInflater layoutInflater, int i) {
        if (i == 0) {
            i = 2131624539;
        }
        return super.onInflateTemplate(layoutInflater, i);
    }

    public GlifListLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        if (!isInEditMode()) {
            ListMixin listMixin = new ListMixin(this, attributeSet);
            this.listMixin = listMixin;
            registerMixin(ListMixin.class, listMixin);
            ListMixin listMixin2 = this.listMixin;
            Objects.requireNonNull(listMixin2);
            listMixin2.getListViewInternal();
            Objects.requireNonNull((RequireScrollMixin) getMixin(RequireScrollMixin.class));
            View findViewById = findViewById(2131428970);
            if (findViewById != null) {
                tryApplyPartnerCustomizationContentPaddingTopStyle(findViewById);
            }
            updateLandscapeMiddleHorizontalSpacing();
        }
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        ListMixin listMixin = this.listMixin;
        Objects.requireNonNull(listMixin);
        if (listMixin.divider == null) {
            listMixin.updateDivider();
        }
    }
}

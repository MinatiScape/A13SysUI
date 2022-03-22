package com.google.android.setupdesign.template;

import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.google.android.setupcompat.internal.TemplateLayout;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.template.FooterBarMixin$$ExternalSyntheticOutline0;
import com.google.android.setupcompat.template.Mixin;
import com.google.android.setupdesign.R$styleable;
import com.google.android.setupdesign.items.ItemAdapter;
import com.google.android.setupdesign.items.ItemGroup;
import com.google.android.setupdesign.items.ItemInflater;
import com.google.android.setupdesign.util.DrawableLayoutDirectionHelper;
import com.google.android.setupdesign.util.PartnerStyleHelper;
/* loaded from: classes.dex */
public final class ListMixin implements Mixin {
    public Drawable defaultDivider;
    public InsetDrawable divider;
    public int dividerInsetEnd;
    public int dividerInsetStart;
    public ListView listView;
    public final TemplateLayout templateLayout;

    public final ListView getListViewInternal() {
        if (this.listView == null) {
            View findManagedViewById = this.templateLayout.findManagedViewById(16908298);
            if (findManagedViewById instanceof ListView) {
                this.listView = (ListView) findManagedViewById;
            }
        }
        return this.listView;
    }

    /* JADX WARN: Finally extract failed */
    public ListMixin(TemplateLayout templateLayout, AttributeSet attributeSet) {
        this.templateLayout = templateLayout;
        Context context = templateLayout.getContext();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.SudListMixin, 0, 0);
        int resourceId = obtainStyledAttributes.getResourceId(0, 0);
        if (resourceId != 0) {
            ItemInflater itemInflater = new ItemInflater(context);
            XmlResourceParser xml = itemInflater.resources.getXml(resourceId);
            try {
                Object inflate = itemInflater.inflate(xml);
                xml.close();
                ItemAdapter itemAdapter = new ItemAdapter((ItemGroup) inflate);
                ListView listViewInternal = getListViewInternal();
                if (listViewInternal != null) {
                    listViewInternal.setAdapter((ListAdapter) itemAdapter);
                }
            } catch (Throwable th) {
                xml.close();
                throw th;
            }
        }
        boolean z = obtainStyledAttributes.getBoolean(4, true);
        if (PartnerStyleHelper.shouldApplyPartnerResource(templateLayout)) {
            PartnerConfigHelper partnerConfigHelper = PartnerConfigHelper.get(context);
            PartnerConfig partnerConfig = PartnerConfig.CONFIG_ITEMS_DIVIDER_SHOWN;
            if (partnerConfigHelper.isPartnerConfigAvailable(partnerConfig)) {
                z = PartnerConfigHelper.get(context).getBoolean(context, partnerConfig, true);
            }
        }
        if (z) {
            int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(1, -1);
            if (dimensionPixelSize != -1) {
                this.dividerInsetStart = dimensionPixelSize;
                this.dividerInsetEnd = 0;
                updateDivider();
            } else {
                int dimensionPixelSize2 = obtainStyledAttributes.getDimensionPixelSize(3, 0);
                int dimensionPixelSize3 = obtainStyledAttributes.getDimensionPixelSize(2, 0);
                if (PartnerStyleHelper.shouldApplyPartnerResource(templateLayout)) {
                    PartnerConfigHelper partnerConfigHelper2 = PartnerConfigHelper.get(context);
                    PartnerConfig partnerConfig2 = PartnerConfig.CONFIG_LAYOUT_MARGIN_START;
                    dimensionPixelSize2 = partnerConfigHelper2.isPartnerConfigAvailable(partnerConfig2) ? (int) FooterBarMixin$$ExternalSyntheticOutline0.m(context, context, partnerConfig2, 0.0f) : dimensionPixelSize2;
                    PartnerConfigHelper partnerConfigHelper3 = PartnerConfigHelper.get(context);
                    PartnerConfig partnerConfig3 = PartnerConfig.CONFIG_LAYOUT_MARGIN_END;
                    if (partnerConfigHelper3.isPartnerConfigAvailable(partnerConfig3)) {
                        dimensionPixelSize3 = (int) FooterBarMixin$$ExternalSyntheticOutline0.m(context, context, partnerConfig3, 0.0f);
                    }
                }
                this.dividerInsetStart = dimensionPixelSize2;
                this.dividerInsetEnd = dimensionPixelSize3;
                updateDivider();
            }
        } else {
            getListViewInternal().setDivider(null);
        }
        obtainStyledAttributes.recycle();
    }

    public final void updateDivider() {
        ListView listViewInternal = getListViewInternal();
        if (listViewInternal != null && this.templateLayout.isLayoutDirectionResolved()) {
            if (this.defaultDivider == null) {
                this.defaultDivider = listViewInternal.getDivider();
            }
            Drawable drawable = this.defaultDivider;
            if (drawable != null) {
                InsetDrawable createRelativeInsetDrawable = DrawableLayoutDirectionHelper.createRelativeInsetDrawable(drawable, this.dividerInsetStart, this.dividerInsetEnd, this.templateLayout);
                this.divider = createRelativeInsetDrawable;
                listViewInternal.setDivider(createRelativeInsetDrawable);
            }
        }
    }
}

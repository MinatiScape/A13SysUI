package com.google.android.setupdesign.items;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.template.FooterBarMixin$$ExternalSyntheticOutline0;
import com.google.android.setupdesign.R$styleable;
import com.google.android.setupdesign.util.LayoutStyler;
import com.google.android.setupdesign.util.PartnerStyleHelper;
import com.google.android.setupdesign.util.TextViewPartnerStyler;
/* loaded from: classes.dex */
public class Item extends AbstractItem {
    public CharSequence contentDescription;
    public boolean enabled;
    public Drawable icon;
    public int iconGravity;
    public int iconTint;
    public int layoutRes;
    public CharSequence summary;
    public CharSequence title;
    public boolean visible;

    public Item() {
        this.enabled = true;
        this.visible = true;
        this.iconTint = 0;
        this.iconGravity = 16;
        this.layoutRes = getDefaultLayoutResource();
    }

    public int getDefaultLayoutResource() {
        return 2131624558;
    }

    @Override // com.google.android.setupdesign.items.AbstractItem
    public void onBindView(View view) {
        boolean z;
        float f;
        float f2;
        ((TextView) view.findViewById(2131428969)).setText(this.title);
        TextView textView = (TextView) view.findViewById(2131428966);
        CharSequence summary = getSummary();
        if (summary == null || summary.length() <= 0) {
            z = false;
        } else {
            z = true;
        }
        if (z) {
            textView.setText(summary);
            textView.setVisibility(0);
        } else {
            textView.setVisibility(8);
        }
        view.setContentDescription(this.contentDescription);
        View findViewById = view.findViewById(2131428965);
        Drawable drawable = this.icon;
        if (drawable != null) {
            ImageView imageView = (ImageView) view.findViewById(2131428964);
            imageView.setImageDrawable(null);
            imageView.setImageState(drawable.getState(), false);
            imageView.setImageLevel(drawable.getLevel());
            imageView.setImageDrawable(drawable);
            int i = this.iconTint;
            if (i != 0) {
                imageView.setColorFilter(i);
            } else {
                imageView.clearColorFilter();
            }
            ViewGroup.LayoutParams layoutParams = findViewById.getLayoutParams();
            if (layoutParams instanceof LinearLayout.LayoutParams) {
                ((LinearLayout.LayoutParams) layoutParams).gravity = this.iconGravity;
            }
            findViewById.setVisibility(0);
        } else {
            findViewById.setVisibility(8);
        }
        view.setId(this.id);
        if (!(this instanceof ExpandableSwitchItem) && view.getId() != 2131428976) {
            LayoutStyler.applyPartnerCustomizationLayoutPaddingStyle(view);
        }
        if (PartnerStyleHelper.shouldApplyPartnerHeavyThemeResource(view)) {
            TextView textView2 = (TextView) view.findViewById(2131428969);
            if (PartnerStyleHelper.shouldApplyPartnerHeavyThemeResource(textView2)) {
                TextViewPartnerStyler.applyPartnerCustomizationStyle(textView2, new TextViewPartnerStyler.TextPartnerConfigs(null, null, PartnerConfig.CONFIG_ITEMS_TITLE_TEXT_SIZE, PartnerConfig.CONFIG_ITEMS_TITLE_FONT_FAMILY, null, null, null, PartnerStyleHelper.getLayoutGravity(textView2.getContext())));
            }
            TextView textView3 = (TextView) view.findViewById(2131428966);
            if (textView3.getVisibility() == 8 && (view instanceof LinearLayout)) {
                ((LinearLayout) view).setGravity(16);
            }
            if (PartnerStyleHelper.shouldApplyPartnerHeavyThemeResource(textView3)) {
                TextViewPartnerStyler.applyPartnerCustomizationStyle(textView3, new TextViewPartnerStyler.TextPartnerConfigs(null, null, PartnerConfig.CONFIG_ITEMS_SUMMARY_TEXT_SIZE, PartnerConfig.CONFIG_ITEMS_SUMMARY_FONT_FAMILY, null, PartnerConfig.CONFIG_ITEMS_SUMMARY_MARGIN_TOP, null, PartnerStyleHelper.getLayoutGravity(textView3.getContext())));
            }
            Context context = view.getContext();
            PartnerConfigHelper partnerConfigHelper = PartnerConfigHelper.get(context);
            PartnerConfig partnerConfig = PartnerConfig.CONFIG_ITEMS_PADDING_TOP;
            if (partnerConfigHelper.isPartnerConfigAvailable(partnerConfig)) {
                f = FooterBarMixin$$ExternalSyntheticOutline0.m(context, context, partnerConfig, 0.0f);
            } else {
                f = view.getPaddingTop();
            }
            PartnerConfigHelper partnerConfigHelper2 = PartnerConfigHelper.get(context);
            PartnerConfig partnerConfig2 = PartnerConfig.CONFIG_ITEMS_PADDING_BOTTOM;
            if (partnerConfigHelper2.isPartnerConfigAvailable(partnerConfig2)) {
                f2 = FooterBarMixin$$ExternalSyntheticOutline0.m(context, context, partnerConfig2, 0.0f);
            } else {
                f2 = view.getPaddingBottom();
            }
            if (!(f == view.getPaddingTop() && f2 == view.getPaddingBottom())) {
                view.setPadding(view.getPaddingStart(), (int) f, view.getPaddingEnd(), (int) f2);
            }
            PartnerConfigHelper partnerConfigHelper3 = PartnerConfigHelper.get(context);
            PartnerConfig partnerConfig3 = PartnerConfig.CONFIG_ITEMS_MIN_HEIGHT;
            if (partnerConfigHelper3.isPartnerConfigAvailable(partnerConfig3)) {
                view.setMinimumHeight((int) FooterBarMixin$$ExternalSyntheticOutline0.m(context, context, partnerConfig3, 0.0f));
            }
        }
    }

    public Item(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.enabled = true;
        this.visible = true;
        this.iconTint = 0;
        this.iconGravity = 16;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.SudItem);
        this.enabled = obtainStyledAttributes.getBoolean(1, true);
        this.icon = obtainStyledAttributes.getDrawable(0);
        this.title = obtainStyledAttributes.getText(4);
        this.summary = obtainStyledAttributes.getText(5);
        this.contentDescription = obtainStyledAttributes.getText(6);
        this.layoutRes = obtainStyledAttributes.getResourceId(2, getDefaultLayoutResource());
        this.visible = obtainStyledAttributes.getBoolean(3, true);
        this.iconTint = obtainStyledAttributes.getColor(8, 0);
        this.iconGravity = obtainStyledAttributes.getInt(7, 16);
        obtainStyledAttributes.recycle();
    }

    @Override // com.google.android.setupdesign.items.AbstractItem, com.google.android.setupdesign.items.ItemHierarchy
    public final int getCount() {
        return this.visible ? 1 : 0;
    }

    @Override // com.google.android.setupdesign.items.AbstractItem
    public final int getLayoutResource() {
        return this.layoutRes;
    }

    public CharSequence getSummary() {
        return this.summary;
    }

    @Override // com.google.android.setupdesign.items.AbstractItem
    public boolean isEnabled() {
        return this.enabled;
    }
}

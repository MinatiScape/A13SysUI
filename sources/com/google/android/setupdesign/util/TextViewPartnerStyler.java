package com.google.android.setupdesign.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.android.setupcompat.PartnerCustomizationLayout;
import com.google.android.setupcompat.internal.TemplateLayout;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupdesign.GlifLayout;
import com.google.android.setupdesign.view.RichTextView;
import java.util.Objects;
/* loaded from: classes.dex */
public final class TextViewPartnerStyler {

    /* loaded from: classes.dex */
    public static class TextPartnerConfigs {
        public final PartnerConfig textColorConfig;
        public final PartnerConfig textFontFamilyConfig;
        public final int textGravity;
        public final PartnerConfig textLinkFontFamilyConfig;
        public final PartnerConfig textLinkedColorConfig;
        public final PartnerConfig textMarginBottomConfig;
        public final PartnerConfig textMarginTopConfig;
        public final PartnerConfig textSizeConfig;

        public TextPartnerConfigs(PartnerConfig partnerConfig, PartnerConfig partnerConfig2, PartnerConfig partnerConfig3, PartnerConfig partnerConfig4, PartnerConfig partnerConfig5, PartnerConfig partnerConfig6, PartnerConfig partnerConfig7, int i) {
            this.textColorConfig = partnerConfig;
            this.textLinkedColorConfig = partnerConfig2;
            this.textSizeConfig = partnerConfig3;
            this.textFontFamilyConfig = partnerConfig4;
            this.textLinkFontFamilyConfig = partnerConfig5;
            this.textMarginTopConfig = partnerConfig6;
            this.textMarginBottomConfig = partnerConfig7;
            this.textGravity = i;
        }
    }

    public static void applyPartnerCustomizationVerticalMargins(TextView textView, TextPartnerConfigs textPartnerConfigs) {
        int i;
        int i2;
        if (textPartnerConfigs.textMarginTopConfig != null || textPartnerConfigs.textMarginBottomConfig != null) {
            Context context = textView.getContext();
            ViewGroup.LayoutParams layoutParams = textView.getLayoutParams();
            if (layoutParams instanceof LinearLayout.LayoutParams) {
                LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) layoutParams;
                if (textPartnerConfigs.textMarginTopConfig == null || !PartnerConfigHelper.get(context).isPartnerConfigAvailable(textPartnerConfigs.textMarginTopConfig)) {
                    i = layoutParams2.topMargin;
                } else {
                    PartnerConfigHelper partnerConfigHelper = PartnerConfigHelper.get(context);
                    PartnerConfig partnerConfig = textPartnerConfigs.textMarginTopConfig;
                    Objects.requireNonNull(partnerConfigHelper);
                    i = (int) partnerConfigHelper.getDimension(context, partnerConfig, 0.0f);
                }
                if (textPartnerConfigs.textMarginBottomConfig == null || !PartnerConfigHelper.get(context).isPartnerConfigAvailable(textPartnerConfigs.textMarginBottomConfig)) {
                    i2 = layoutParams2.bottomMargin;
                } else {
                    PartnerConfigHelper partnerConfigHelper2 = PartnerConfigHelper.get(context);
                    PartnerConfig partnerConfig2 = textPartnerConfigs.textMarginBottomConfig;
                    Objects.requireNonNull(partnerConfigHelper2);
                    i2 = (int) partnerConfigHelper2.getDimension(context, partnerConfig2, 0.0f);
                }
                layoutParams2.setMargins(layoutParams2.leftMargin, i, layoutParams2.rightMargin, i2);
                textView.setLayoutParams(layoutParams);
            }
        }
    }

    public static void applyPartnerCustomizationStyle(TextView textView, TextPartnerConfigs textPartnerConfigs) {
        Typeface create;
        Typeface create2;
        boolean z;
        int color;
        TemplateLayout findLayoutFromActivity;
        int color2;
        Context context = textView.getContext();
        if (!(textPartnerConfigs.textColorConfig == null || !PartnerConfigHelper.get(context).isPartnerConfigAvailable(textPartnerConfigs.textColorConfig) || (color2 = PartnerConfigHelper.get(context).getColor(context, textPartnerConfigs.textColorConfig)) == 0)) {
            textView.setTextColor(color2);
        }
        if (textPartnerConfigs.textLinkedColorConfig != null && PartnerConfigHelper.get(context).isPartnerConfigAvailable(textPartnerConfigs.textLinkedColorConfig)) {
            Context context2 = textView.getContext();
            try {
                findLayoutFromActivity = PartnerStyleHelper.findLayoutFromActivity(PartnerCustomizationLayout.lookupActivityFromContext(context2));
            } catch (ClassCastException | IllegalArgumentException unused) {
            }
            if (findLayoutFromActivity instanceof GlifLayout) {
                z = ((GlifLayout) findLayoutFromActivity).shouldApplyDynamicColor();
                if (!z && (color = PartnerConfigHelper.get(context).getColor(context, textPartnerConfigs.textLinkedColorConfig)) != 0) {
                    textView.setLinkTextColor(color);
                }
            }
            TypedArray obtainStyledAttributes = context2.obtainStyledAttributes(new int[]{2130969811});
            boolean hasValue = obtainStyledAttributes.hasValue(0);
            obtainStyledAttributes.recycle();
            z = hasValue;
            if (!z) {
                textView.setLinkTextColor(color);
            }
        }
        if (textPartnerConfigs.textSizeConfig != null && PartnerConfigHelper.get(context).isPartnerConfigAvailable(textPartnerConfigs.textSizeConfig)) {
            float dimension = PartnerConfigHelper.get(context).getDimension(context, textPartnerConfigs.textSizeConfig, 0.0f);
            if (dimension > 0.0f) {
                textView.setTextSize(0, dimension);
            }
        }
        if (!(textPartnerConfigs.textFontFamilyConfig == null || !PartnerConfigHelper.get(context).isPartnerConfigAvailable(textPartnerConfigs.textFontFamilyConfig) || (create2 = Typeface.create(PartnerConfigHelper.get(context).getString(context, textPartnerConfigs.textFontFamilyConfig), 0)) == null)) {
            textView.setTypeface(create2);
        }
        if ((textView instanceof RichTextView) && textPartnerConfigs.textLinkFontFamilyConfig != null && PartnerConfigHelper.get(context).isPartnerConfigAvailable(textPartnerConfigs.textLinkFontFamilyConfig) && (create = Typeface.create(PartnerConfigHelper.get(context).getString(context, textPartnerConfigs.textLinkFontFamilyConfig), 0)) != null) {
            RichTextView richTextView = (RichTextView) textView;
            RichTextView.spanTypeface = create;
        }
        applyPartnerCustomizationVerticalMargins(textView, textPartnerConfigs);
        textView.setGravity(textPartnerConfigs.textGravity);
    }
}

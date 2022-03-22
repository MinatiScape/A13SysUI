package com.google.android.setupdesign.template;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import com.google.android.setupcompat.internal.TemplateLayout;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.template.FooterBarMixin$$ExternalSyntheticOutline0;
import com.google.android.setupcompat.template.Mixin;
import com.google.android.setupdesign.R$styleable;
import com.google.android.setupdesign.util.PartnerStyleHelper;
/* loaded from: classes.dex */
public final class HeaderMixin implements Mixin {
    public boolean autoTextSizeEnabled = false;
    public float headerAutoSizeLineExtraSpacingInPx;
    public int headerAutoSizeMaxLineOfMaxSize;
    public float headerAutoSizeMaxTextSizeInPx;
    public float headerAutoSizeMinTextSizeInPx;
    public final TemplateLayout templateLayout;

    public final void autoAdjustTextSize(final TextView textView) {
        if (textView != null) {
            textView.setTextSize(0, this.headerAutoSizeMaxTextSizeInPx);
            textView.setLineHeight(Math.round(this.headerAutoSizeLineExtraSpacingInPx + this.headerAutoSizeMaxTextSizeInPx));
            textView.setMaxLines(6);
            textView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: com.google.android.setupdesign.template.HeaderMixin.1
                @Override // android.view.ViewTreeObserver.OnPreDrawListener
                public final boolean onPreDraw() {
                    textView.getViewTreeObserver().removeOnPreDrawListener(this);
                    int lineCount = textView.getLineCount();
                    HeaderMixin headerMixin = HeaderMixin.this;
                    if (lineCount <= headerMixin.headerAutoSizeMaxLineOfMaxSize) {
                        return true;
                    }
                    textView.setTextSize(0, headerMixin.headerAutoSizeMinTextSizeInPx);
                    TextView textView2 = textView;
                    HeaderMixin headerMixin2 = HeaderMixin.this;
                    textView2.setLineHeight(Math.round(headerMixin2.headerAutoSizeLineExtraSpacingInPx + headerMixin2.headerAutoSizeMinTextSizeInPx));
                    textView.invalidate();
                    return false;
                }
            });
        }
    }

    public final void tryUpdateAutoTextSizeFlagWithPartnerConfig() {
        Context context = this.templateLayout.getContext();
        if (!PartnerStyleHelper.shouldApplyPartnerResource(this.templateLayout)) {
            this.autoTextSizeEnabled = false;
            return;
        }
        PartnerConfigHelper partnerConfigHelper = PartnerConfigHelper.get(context);
        PartnerConfig partnerConfig = PartnerConfig.CONFIG_HEADER_AUTO_SIZE_ENABLED;
        if (partnerConfigHelper.isPartnerConfigAvailable(partnerConfig)) {
            this.autoTextSizeEnabled = PartnerConfigHelper.get(context).getBoolean(context, partnerConfig, this.autoTextSizeEnabled);
        }
        if (this.autoTextSizeEnabled) {
            PartnerConfigHelper partnerConfigHelper2 = PartnerConfigHelper.get(context);
            PartnerConfig partnerConfig2 = PartnerConfig.CONFIG_HEADER_AUTO_SIZE_MAX_TEXT_SIZE;
            if (partnerConfigHelper2.isPartnerConfigAvailable(partnerConfig2)) {
                this.headerAutoSizeMaxTextSizeInPx = FooterBarMixin$$ExternalSyntheticOutline0.m(context, context, partnerConfig2, 0.0f);
            }
            PartnerConfigHelper partnerConfigHelper3 = PartnerConfigHelper.get(context);
            PartnerConfig partnerConfig3 = PartnerConfig.CONFIG_HEADER_AUTO_SIZE_MIN_TEXT_SIZE;
            if (partnerConfigHelper3.isPartnerConfigAvailable(partnerConfig3)) {
                this.headerAutoSizeMinTextSizeInPx = FooterBarMixin$$ExternalSyntheticOutline0.m(context, context, partnerConfig3, 0.0f);
            }
            PartnerConfigHelper partnerConfigHelper4 = PartnerConfigHelper.get(context);
            PartnerConfig partnerConfig4 = PartnerConfig.CONFIG_HEADER_AUTO_SIZE_LINE_SPACING_EXTRA;
            if (partnerConfigHelper4.isPartnerConfigAvailable(partnerConfig4)) {
                this.headerAutoSizeLineExtraSpacingInPx = FooterBarMixin$$ExternalSyntheticOutline0.m(context, context, partnerConfig4, 0.0f);
            }
            PartnerConfigHelper partnerConfigHelper5 = PartnerConfigHelper.get(context);
            PartnerConfig partnerConfig5 = PartnerConfig.CONFIG_HEADER_AUTO_SIZE_MAX_LINE_OF_MAX_SIZE;
            if (partnerConfigHelper5.isPartnerConfigAvailable(partnerConfig5)) {
                this.headerAutoSizeMaxLineOfMaxSize = PartnerConfigHelper.get(context).getInteger(context, partnerConfig5);
            }
            if (this.headerAutoSizeMaxLineOfMaxSize >= 1) {
                float f = this.headerAutoSizeMinTextSizeInPx;
                if (f > 0.0f && this.headerAutoSizeMaxTextSizeInPx >= f) {
                    return;
                }
            }
            Log.w("HeaderMixin", "Invalid configs, disable auto text size.");
            this.autoTextSizeEnabled = false;
        }
    }

    public HeaderMixin(TemplateLayout templateLayout, AttributeSet attributeSet, int i) {
        TextView textView;
        TextView textView2;
        this.templateLayout = templateLayout;
        TypedArray obtainStyledAttributes = templateLayout.getContext().obtainStyledAttributes(attributeSet, R$styleable.SucHeaderMixin, i, 0);
        CharSequence text = obtainStyledAttributes.getText(4);
        ColorStateList colorStateList = obtainStyledAttributes.getColorStateList(5);
        obtainStyledAttributes.recycle();
        tryUpdateAutoTextSizeFlagWithPartnerConfig();
        if (!(text == null || (textView2 = (TextView) templateLayout.findManagedViewById(2131428954)) == null)) {
            if (this.autoTextSizeEnabled) {
                autoAdjustTextSize(textView2);
            }
            textView2.setText(text);
        }
        if (colorStateList != null && (textView = (TextView) templateLayout.findManagedViewById(2131428954)) != null) {
            textView.setTextColor(colorStateList);
        }
    }
}

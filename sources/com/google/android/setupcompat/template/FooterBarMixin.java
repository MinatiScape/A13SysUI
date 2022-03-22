package com.google.android.setupcompat.template;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.util.AttributeSet;
import android.util.StateSet;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.LinearLayout;
import com.android.systemui.R$array;
import com.android.systemui.R$id;
import com.android.systemui.plugins.FalsingManager;
import com.google.android.setupcompat.PartnerCustomizationLayout;
import com.google.android.setupcompat.internal.FooterButtonPartnerConfig;
import com.google.android.setupcompat.internal.TemplateLayout;
import com.google.android.setupcompat.logging.internal.FooterBarMixinMetrics;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.template.FooterButton;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: classes.dex */
public final class FooterBarMixin implements Mixin {
    public static final AtomicInteger nextGeneratedId = new AtomicInteger(1);
    public final boolean applyDynamicColor;
    public final boolean applyPartnerResources;
    public LinearLayout buttonContainer;
    public final Context context;
    public int defaultPadding;
    public int footerBarPaddingBottom;
    public int footerBarPaddingEnd;
    public int footerBarPaddingStart;
    public int footerBarPaddingTop;
    public final int footerBarPrimaryBackgroundColor;
    public final int footerBarSecondaryBackgroundColor;
    public final ViewStub footerStub;
    public boolean isSecondaryButtonInPrimaryStyle = false;
    public final FooterBarMixinMetrics metrics;
    public FooterButton primaryButton;
    public int primaryButtonId;
    public FooterButtonPartnerConfig primaryButtonPartnerConfigForTesting;
    public FooterButton secondaryButton;
    public int secondaryButtonId;
    public FooterButtonPartnerConfig secondaryButtonPartnerConfigForTesting;
    public final boolean useFullDynamicColor;

    /* renamed from: com.google.android.setupcompat.template.FooterBarMixin$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass1 implements FooterButton.OnButtonEventListener {
        public final /* synthetic */ int val$id;

        public AnonymousClass1(int i) {
            this.val$id = i;
        }
    }

    public final LinearLayout ensureFooterInflated() {
        if (this.buttonContainer == null) {
            if (this.footerStub != null) {
                this.footerStub.setLayoutInflater(LayoutInflater.from(new ContextThemeWrapper(this.context, 2132017682)));
                this.footerStub.setLayoutResource(2131624530);
                LinearLayout linearLayout = (LinearLayout) this.footerStub.inflate();
                this.buttonContainer = linearLayout;
                if (linearLayout != null) {
                    linearLayout.setId(View.generateViewId());
                    linearLayout.setPadding(this.footerBarPaddingStart, this.footerBarPaddingTop, this.footerBarPaddingEnd, this.footerBarPaddingBottom);
                    if (isFooterButtonAlignedEnd()) {
                        linearLayout.setGravity(8388629);
                    }
                }
                LinearLayout linearLayout2 = this.buttonContainer;
                if (linearLayout2 != null && this.applyPartnerResources) {
                    if (!this.useFullDynamicColor) {
                        linearLayout2.setBackgroundColor(PartnerConfigHelper.get(this.context).getColor(this.context, PartnerConfig.CONFIG_FOOTER_BAR_BG_COLOR));
                    }
                    PartnerConfigHelper partnerConfigHelper = PartnerConfigHelper.get(this.context);
                    PartnerConfig partnerConfig = PartnerConfig.CONFIG_FOOTER_BUTTON_PADDING_TOP;
                    if (partnerConfigHelper.isPartnerConfigAvailable(partnerConfig)) {
                        PartnerConfigHelper partnerConfigHelper2 = PartnerConfigHelper.get(this.context);
                        Context context = this.context;
                        Objects.requireNonNull(partnerConfigHelper2);
                        this.footerBarPaddingTop = (int) partnerConfigHelper2.getDimension(context, partnerConfig, 0.0f);
                    }
                    PartnerConfigHelper partnerConfigHelper3 = PartnerConfigHelper.get(this.context);
                    PartnerConfig partnerConfig2 = PartnerConfig.CONFIG_FOOTER_BUTTON_PADDING_BOTTOM;
                    if (partnerConfigHelper3.isPartnerConfigAvailable(partnerConfig2)) {
                        PartnerConfigHelper partnerConfigHelper4 = PartnerConfigHelper.get(this.context);
                        Context context2 = this.context;
                        Objects.requireNonNull(partnerConfigHelper4);
                        this.footerBarPaddingBottom = (int) partnerConfigHelper4.getDimension(context2, partnerConfig2, 0.0f);
                    }
                    PartnerConfigHelper partnerConfigHelper5 = PartnerConfigHelper.get(this.context);
                    PartnerConfig partnerConfig3 = PartnerConfig.CONFIG_FOOTER_BAR_PADDING_START;
                    if (partnerConfigHelper5.isPartnerConfigAvailable(partnerConfig3)) {
                        PartnerConfigHelper partnerConfigHelper6 = PartnerConfigHelper.get(this.context);
                        Context context3 = this.context;
                        Objects.requireNonNull(partnerConfigHelper6);
                        this.footerBarPaddingStart = (int) partnerConfigHelper6.getDimension(context3, partnerConfig3, 0.0f);
                    }
                    PartnerConfigHelper partnerConfigHelper7 = PartnerConfigHelper.get(this.context);
                    PartnerConfig partnerConfig4 = PartnerConfig.CONFIG_FOOTER_BAR_PADDING_END;
                    if (partnerConfigHelper7.isPartnerConfigAvailable(partnerConfig4)) {
                        PartnerConfigHelper partnerConfigHelper8 = PartnerConfigHelper.get(this.context);
                        Context context4 = this.context;
                        Objects.requireNonNull(partnerConfigHelper8);
                        this.footerBarPaddingEnd = (int) partnerConfigHelper8.getDimension(context4, partnerConfig4, 0.0f);
                    }
                    linearLayout2.setPadding(this.footerBarPaddingStart, this.footerBarPaddingTop, this.footerBarPaddingEnd, this.footerBarPaddingBottom);
                    PartnerConfigHelper partnerConfigHelper9 = PartnerConfigHelper.get(this.context);
                    PartnerConfig partnerConfig5 = PartnerConfig.CONFIG_FOOTER_BAR_MIN_HEIGHT;
                    if (partnerConfigHelper9.isPartnerConfigAvailable(partnerConfig5)) {
                        PartnerConfigHelper partnerConfigHelper10 = PartnerConfigHelper.get(this.context);
                        Context context5 = this.context;
                        Objects.requireNonNull(partnerConfigHelper10);
                        int dimension = (int) partnerConfigHelper10.getDimension(context5, partnerConfig5, 0.0f);
                        if (dimension > 0) {
                            linearLayout2.setMinimumHeight(dimension);
                        }
                    }
                }
            } else {
                throw new IllegalStateException("Footer stub is not found in this template");
            }
        }
        return this.buttonContainer;
    }

    public int getPaddingBottom() {
        LinearLayout linearLayout = this.buttonContainer;
        if (linearLayout != null) {
            return linearLayout.getPaddingBottom();
        }
        return this.footerStub.getPaddingBottom();
    }

    public int getPaddingTop() {
        LinearLayout linearLayout = this.buttonContainer;
        if (linearLayout != null) {
            return linearLayout.getPaddingTop();
        }
        return this.footerStub.getPaddingTop();
    }

    public Button getPrimaryButtonView() {
        LinearLayout linearLayout = this.buttonContainer;
        if (linearLayout == null) {
            return null;
        }
        return (Button) linearLayout.findViewById(this.primaryButtonId);
    }

    public Button getSecondaryButtonView() {
        LinearLayout linearLayout = this.buttonContainer;
        if (linearLayout == null) {
            return null;
        }
        return (Button) linearLayout.findViewById(this.secondaryButtonId);
    }

    public int getVisibility() {
        return this.buttonContainer.getVisibility();
    }

    public final FooterActionButton inflateButton(FooterButton footerButton, FooterButtonPartnerConfig footerButtonPartnerConfig) {
        FooterActionButton footerActionButton = (FooterActionButton) LayoutInflater.from(new ContextThemeWrapper(this.context, footerButtonPartnerConfig.partnerTheme)).inflate(2131624529, (ViewGroup) null, false);
        footerActionButton.setId(View.generateViewId());
        Objects.requireNonNull(footerButton);
        footerActionButton.setText(footerButton.text);
        footerActionButton.setOnClickListener(footerButton);
        footerActionButton.setVisibility(0);
        footerActionButton.setEnabled(footerButton.enabled);
        footerActionButton.footerButton = footerButton;
        footerButton.buttonListener = new AnonymousClass1(footerActionButton.getId());
        return footerActionButton;
    }

    public final boolean isFooterButtonAlignedEnd() {
        PartnerConfigHelper partnerConfigHelper = PartnerConfigHelper.get(this.context);
        PartnerConfig partnerConfig = PartnerConfig.CONFIG_FOOTER_BUTTON_ALIGNED_END;
        if (partnerConfigHelper.isPartnerConfigAvailable(partnerConfig)) {
            return PartnerConfigHelper.get(this.context).getBoolean(this.context, partnerConfig, false);
        }
        return false;
    }

    @TargetApi(29)
    public final void onFooterButtonApplyPartnerResource(FooterActionButton footerActionButton, FooterButtonPartnerConfig footerButtonPartnerConfig) {
        boolean z;
        int i;
        RippleDrawable rippleDrawable;
        int i2;
        GradientDrawable gradientDrawable;
        Drawable drawable;
        Drawable drawable2;
        if (this.applyPartnerResources) {
            Context context = this.context;
            boolean z2 = this.applyDynamicColor;
            if (footerActionButton.getId() == this.primaryButtonId) {
                z = true;
            } else {
                z = false;
            }
            FooterButtonStyleUtils.defaultTextColor.put(Integer.valueOf(footerActionButton.getId()), footerActionButton.getTextColors());
            if (!z2) {
                if (footerActionButton.isEnabled()) {
                    int color = PartnerConfigHelper.get(context).getColor(context, footerButtonPartnerConfig.buttonTextColorConfig);
                    if (color != 0) {
                        footerActionButton.setTextColor(ColorStateList.valueOf(color));
                    }
                } else {
                    FooterButtonStyleUtils.updateButtonTextDisabledColorWithPartnerConfig(context, footerActionButton, footerButtonPartnerConfig.buttonDisableTextColorConfig);
                }
                PartnerConfig partnerConfig = footerButtonPartnerConfig.buttonBackgroundConfig;
                PartnerConfig partnerConfig2 = footerButtonPartnerConfig.buttonDisableAlphaConfig;
                PartnerConfig partnerConfig3 = footerButtonPartnerConfig.buttonDisableBackgroundConfig;
                int color2 = PartnerConfigHelper.get(context).getColor(context, partnerConfig);
                float fraction = PartnerConfigHelper.get(context).getFraction(context, partnerConfig2);
                int color3 = PartnerConfigHelper.get(context).getColor(context, partnerConfig3);
                int[] iArr = {-16842910};
                int[] iArr2 = new int[0];
                if (color2 != 0) {
                    if (fraction <= 0.0f) {
                        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{16842803});
                        fraction = obtainStyledAttributes.getFloat(0, 0.26f);
                        obtainStyledAttributes.recycle();
                    }
                    if (color3 == 0) {
                        color3 = color2;
                    }
                    ColorStateList colorStateList = new ColorStateList(new int[][]{iArr, iArr2}, new int[]{Color.argb((int) (fraction * 255.0f), Color.red(color3), Color.green(color3), Color.blue(color3)), color2});
                    footerActionButton.getBackground().mutate().setState(new int[0]);
                    footerActionButton.refreshDrawableState();
                    footerActionButton.setBackgroundTintList(colorStateList);
                }
            }
            PartnerConfig partnerConfig4 = footerButtonPartnerConfig.buttonTextColorConfig;
            PartnerConfig partnerConfig5 = footerButtonPartnerConfig.buttonRippleColorAlphaConfig;
            if (z2) {
                i = footerActionButton.getTextColors().getDefaultColor();
            } else {
                i = PartnerConfigHelper.get(context).getColor(context, partnerConfig4);
            }
            PartnerConfigHelper partnerConfigHelper = PartnerConfigHelper.get(context);
            Objects.requireNonNull(partnerConfigHelper);
            float fraction2 = partnerConfigHelper.getFraction(context, partnerConfig5);
            Drawable background = footerActionButton.getBackground();
            if (background instanceof InsetDrawable) {
                rippleDrawable = (RippleDrawable) ((InsetDrawable) background).getDrawable();
            } else if (background instanceof RippleDrawable) {
                rippleDrawable = (RippleDrawable) background;
            } else {
                rippleDrawable = null;
            }
            if (rippleDrawable != null) {
                int argb = Color.argb((int) (fraction2 * 255.0f), Color.red(i), Color.green(i), Color.blue(i));
                rippleDrawable.setColor(new ColorStateList(new int[][]{new int[]{16842919}, new int[]{16842908}, StateSet.NOTHING}, new int[]{argb, argb, 0}));
            }
            PartnerConfig partnerConfig6 = footerButtonPartnerConfig.buttonMarginStartConfig;
            ViewGroup.LayoutParams layoutParams = footerActionButton.getLayoutParams();
            if (PartnerConfigHelper.get(context).isPartnerConfigAvailable(partnerConfig6) && (layoutParams instanceof ViewGroup.MarginLayoutParams)) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                marginLayoutParams.setMargins((int) FooterBarMixin$$ExternalSyntheticOutline0.m(context, context, partnerConfig6, 0.0f), marginLayoutParams.topMargin, marginLayoutParams.rightMargin, marginLayoutParams.bottomMargin);
            }
            float m = FooterBarMixin$$ExternalSyntheticOutline0.m(context, context, footerButtonPartnerConfig.buttonTextSizeConfig, 0.0f);
            if (m > 0.0f) {
                footerActionButton.setTextSize(0, m);
            }
            PartnerConfig partnerConfig7 = footerButtonPartnerConfig.buttonMinHeightConfig;
            if (PartnerConfigHelper.get(context).isPartnerConfigAvailable(partnerConfig7)) {
                float m2 = FooterBarMixin$$ExternalSyntheticOutline0.m(context, context, partnerConfig7, 0.0f);
                if (m2 > 0.0f) {
                    footerActionButton.setMinHeight((int) m2);
                }
            }
            PartnerConfig partnerConfig8 = footerButtonPartnerConfig.buttonTextTypeFaceConfig;
            PartnerConfig partnerConfig9 = footerButtonPartnerConfig.buttonTextStyleConfig;
            String string = PartnerConfigHelper.get(context).getString(context, partnerConfig8);
            if (PartnerConfigHelper.get(context).isPartnerConfigAvailable(partnerConfig9)) {
                i2 = PartnerConfigHelper.get(context).getInteger(context, partnerConfig9);
            } else {
                i2 = 0;
            }
            Typeface create = Typeface.create(string, i2);
            if (create != null) {
                footerActionButton.setTypeface(create);
            }
            float m3 = FooterBarMixin$$ExternalSyntheticOutline0.m(context, context, footerButtonPartnerConfig.buttonRadiusConfig, 0.0f);
            Drawable background2 = footerActionButton.getBackground();
            if (background2 instanceof InsetDrawable) {
                gradientDrawable = (GradientDrawable) ((LayerDrawable) ((InsetDrawable) background2).getDrawable()).getDrawable(0);
            } else if (background2 instanceof RippleDrawable) {
                RippleDrawable rippleDrawable2 = (RippleDrawable) background2;
                if (rippleDrawable2.getDrawable(0) instanceof GradientDrawable) {
                    gradientDrawable = (GradientDrawable) rippleDrawable2.getDrawable(0);
                } else {
                    gradientDrawable = (GradientDrawable) ((InsetDrawable) rippleDrawable2.getDrawable(0)).getDrawable();
                }
            } else {
                gradientDrawable = null;
            }
            if (gradientDrawable != null) {
                gradientDrawable.setCornerRadius(m3);
            }
            PartnerConfig partnerConfig10 = footerButtonPartnerConfig.buttonIconConfig;
            if (partnerConfig10 != null) {
                drawable = PartnerConfigHelper.get(context).getDrawable(context, partnerConfig10);
            } else {
                drawable = null;
            }
            if (drawable != null) {
                drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            }
            if (z) {
                drawable2 = drawable;
                drawable = null;
            } else {
                drawable2 = null;
            }
            footerActionButton.setCompoundDrawablesRelative(drawable, null, drawable2, null);
            if (!this.applyDynamicColor) {
                PartnerConfig partnerConfig11 = footerButtonPartnerConfig.buttonTextColorConfig;
                PartnerConfig partnerConfig12 = footerButtonPartnerConfig.buttonDisableTextColorConfig;
                if (footerActionButton.isEnabled()) {
                    Context context2 = this.context;
                    int color4 = PartnerConfigHelper.get(context2).getColor(context2, partnerConfig11);
                    if (color4 != 0) {
                        footerActionButton.setTextColor(ColorStateList.valueOf(color4));
                        return;
                    }
                    return;
                }
                FooterButtonStyleUtils.updateButtonTextDisabledColorWithPartnerConfig(this.context, footerActionButton, partnerConfig12);
            }
        }
    }

    public final void onFooterButtonInflated(FooterActionButton footerActionButton, int i) {
        boolean z;
        if (i != 0) {
            HashMap<Integer, ColorStateList> hashMap = FooterButtonStyleUtils.defaultTextColor;
            footerActionButton.getBackground().mutate().setColorFilter(i, PorterDuff.Mode.SRC_ATOP);
        }
        this.buttonContainer.addView(footerActionButton);
        Button primaryButtonView = getPrimaryButtonView();
        Button secondaryButtonView = getSecondaryButtonView();
        boolean z2 = true;
        int i2 = 0;
        if (primaryButtonView == null || primaryButtonView.getVisibility() != 0) {
            z = false;
        } else {
            z = true;
        }
        if (secondaryButtonView == null || secondaryButtonView.getVisibility() != 0) {
            z2 = false;
        }
        LinearLayout linearLayout = this.buttonContainer;
        if (linearLayout != null) {
            if (!z && !z2) {
                i2 = 8;
            }
            linearLayout.setVisibility(i2);
        }
    }

    public final void setPrimaryButton(FooterButton footerButton) {
        R$array.ensureOnMainThread("setPrimaryButton");
        ensureFooterInflated();
        PartnerConfig partnerConfig = PartnerConfig.CONFIG_FOOTER_PRIMARY_BUTTON_BG_COLOR;
        FooterButtonPartnerConfig footerButtonPartnerConfig = new FooterButtonPartnerConfig(getPartnerTheme(footerButton, 2132017679, partnerConfig), partnerConfig, PartnerConfig.CONFIG_FOOTER_BUTTON_DISABLED_ALPHA, PartnerConfig.CONFIG_FOOTER_BUTTON_DISABLED_BG_COLOR, PartnerConfig.CONFIG_FOOTER_PRIMARY_BUTTON_DISABLED_TEXT_COLOR, getDrawablePartnerConfig(footerButton.buttonType), PartnerConfig.CONFIG_FOOTER_PRIMARY_BUTTON_TEXT_COLOR, PartnerConfig.CONFIG_FOOTER_PRIMARY_BUTTON_MARGIN_START, PartnerConfig.CONFIG_FOOTER_BUTTON_TEXT_SIZE, PartnerConfig.CONFIG_FOOTER_BUTTON_MIN_HEIGHT, PartnerConfig.CONFIG_FOOTER_BUTTON_FONT_FAMILY, PartnerConfig.CONFIG_FOOTER_BUTTON_TEXT_STYLE, PartnerConfig.CONFIG_FOOTER_BUTTON_RADIUS, PartnerConfig.CONFIG_FOOTER_BUTTON_RIPPLE_COLOR_ALPHA);
        FooterActionButton inflateButton = inflateButton(footerButton, footerButtonPartnerConfig);
        this.primaryButtonId = inflateButton.getId();
        inflateButton.isPrimaryButtonStyle = true;
        this.primaryButton = footerButton;
        this.primaryButtonPartnerConfigForTesting = footerButtonPartnerConfig;
        onFooterButtonInflated(inflateButton, this.footerBarPrimaryBackgroundColor);
        onFooterButtonApplyPartnerResource(inflateButton, footerButtonPartnerConfig);
        repopulateButtons();
    }

    public final void setSecondaryButton(FooterButton footerButton) {
        PartnerConfig partnerConfig = PartnerConfig.CONFIG_FOOTER_SECONDARY_BUTTON_BG_COLOR;
        R$array.ensureOnMainThread("setSecondaryButton");
        this.isSecondaryButtonInPrimaryStyle = false;
        ensureFooterInflated();
        FooterButtonPartnerConfig footerButtonPartnerConfig = new FooterButtonPartnerConfig(getPartnerTheme(footerButton, 2132017680, partnerConfig), partnerConfig, PartnerConfig.CONFIG_FOOTER_BUTTON_DISABLED_ALPHA, PartnerConfig.CONFIG_FOOTER_BUTTON_DISABLED_BG_COLOR, PartnerConfig.CONFIG_FOOTER_SECONDARY_BUTTON_DISABLED_TEXT_COLOR, getDrawablePartnerConfig(footerButton.buttonType), PartnerConfig.CONFIG_FOOTER_SECONDARY_BUTTON_TEXT_COLOR, PartnerConfig.CONFIG_FOOTER_SECONDARY_BUTTON_MARGIN_START, PartnerConfig.CONFIG_FOOTER_BUTTON_TEXT_SIZE, PartnerConfig.CONFIG_FOOTER_BUTTON_MIN_HEIGHT, PartnerConfig.CONFIG_FOOTER_BUTTON_FONT_FAMILY, PartnerConfig.CONFIG_FOOTER_BUTTON_TEXT_STYLE, PartnerConfig.CONFIG_FOOTER_BUTTON_RADIUS, PartnerConfig.CONFIG_FOOTER_BUTTON_RIPPLE_COLOR_ALPHA);
        FooterActionButton inflateButton = inflateButton(footerButton, footerButtonPartnerConfig);
        this.secondaryButtonId = inflateButton.getId();
        inflateButton.isPrimaryButtonStyle = false;
        this.secondaryButton = footerButton;
        this.secondaryButtonPartnerConfigForTesting = footerButtonPartnerConfig;
        onFooterButtonInflated(inflateButton, this.footerBarSecondaryBackgroundColor);
        onFooterButtonApplyPartnerResource(inflateButton, footerButtonPartnerConfig);
        repopulateButtons();
    }

    public FooterBarMixin(TemplateLayout templateLayout, AttributeSet attributeSet, int i) {
        boolean z;
        boolean z2;
        XmlResourceParser xml;
        String str;
        FooterBarMixinMetrics footerBarMixinMetrics = new FooterBarMixinMetrics();
        this.metrics = footerBarMixinMetrics;
        Context context = templateLayout.getContext();
        this.context = context;
        this.footerStub = (ViewStub) templateLayout.findManagedViewById(2131428952);
        boolean z3 = templateLayout instanceof PartnerCustomizationLayout;
        boolean z4 = true;
        if (!z3 || !((PartnerCustomizationLayout) templateLayout).shouldApplyPartnerResource()) {
            z = false;
        } else {
            z = true;
        }
        this.applyPartnerResources = z;
        if (!z3 || !((PartnerCustomizationLayout) templateLayout).shouldApplyDynamicColor()) {
            z2 = false;
        } else {
            z2 = true;
        }
        this.applyDynamicColor = z2;
        this.useFullDynamicColor = (!z3 || !((PartnerCustomizationLayout) templateLayout).useFullDynamicColor()) ? false : z4;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$id.SucFooterBarMixin, i, 0);
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(11, 0);
        this.defaultPadding = dimensionPixelSize;
        this.footerBarPaddingTop = obtainStyledAttributes.getDimensionPixelSize(10, dimensionPixelSize);
        this.footerBarPaddingBottom = obtainStyledAttributes.getDimensionPixelSize(7, this.defaultPadding);
        this.footerBarPaddingStart = obtainStyledAttributes.getDimensionPixelSize(9, 0);
        this.footerBarPaddingEnd = obtainStyledAttributes.getDimensionPixelSize(8, 0);
        this.footerBarPrimaryBackgroundColor = obtainStyledAttributes.getColor(12, 0);
        this.footerBarSecondaryBackgroundColor = obtainStyledAttributes.getColor(14, 0);
        int resourceId = obtainStyledAttributes.getResourceId(13, 0);
        int resourceId2 = obtainStyledAttributes.getResourceId(15, 0);
        obtainStyledAttributes.recycle();
        FooterButtonInflater footerButtonInflater = new FooterButtonInflater(context);
        String str2 = "VisibleUsingXml";
        if (resourceId2 != 0) {
            xml = context.getResources().getXml(resourceId2);
            try {
                FooterButton inflate = footerButtonInflater.inflate(xml);
                xml.close();
                setSecondaryButton(inflate);
                if (footerBarMixinMetrics.primaryButtonVisibility.equals("Unknown")) {
                    str = str2;
                } else {
                    str = footerBarMixinMetrics.primaryButtonVisibility;
                }
                footerBarMixinMetrics.primaryButtonVisibility = str;
            } finally {
            }
        }
        if (resourceId != 0) {
            xml = context.getResources().getXml(resourceId);
            try {
                FooterButton inflate2 = footerButtonInflater.inflate(xml);
                xml.close();
                setPrimaryButton(inflate2);
                footerBarMixinMetrics.secondaryButtonVisibility = !footerBarMixinMetrics.secondaryButtonVisibility.equals("Unknown") ? footerBarMixinMetrics.secondaryButtonVisibility : str2;
            } finally {
            }
        }
        FooterButtonStyleUtils.defaultTextColor.clear();
    }

    public static PartnerConfig getDrawablePartnerConfig(int i) {
        switch (i) {
            case 1:
                return PartnerConfig.CONFIG_FOOTER_BUTTON_ICON_ADD_ANOTHER;
            case 2:
                return PartnerConfig.CONFIG_FOOTER_BUTTON_ICON_CANCEL;
            case 3:
                return PartnerConfig.CONFIG_FOOTER_BUTTON_ICON_CLEAR;
            case 4:
                return PartnerConfig.CONFIG_FOOTER_BUTTON_ICON_DONE;
            case 5:
                return PartnerConfig.CONFIG_FOOTER_BUTTON_ICON_NEXT;
            case FalsingManager.VERSION /* 6 */:
                return PartnerConfig.CONFIG_FOOTER_BUTTON_ICON_OPT_IN;
            case 7:
                return PartnerConfig.CONFIG_FOOTER_BUTTON_ICON_SKIP;
            case 8:
                return PartnerConfig.CONFIG_FOOTER_BUTTON_ICON_STOP;
            default:
                return null;
        }
    }

    public final View addSpace() {
        LinearLayout ensureFooterInflated = ensureFooterInflated();
        View view = new View(this.context);
        view.setLayoutParams(new LinearLayout.LayoutParams(0, 0, 1.0f));
        view.setVisibility(4);
        ensureFooterInflated.addView(view);
        return view;
    }

    public final int getPartnerTheme(FooterButton footerButton, int i, PartnerConfig partnerConfig) {
        Objects.requireNonNull(footerButton);
        int i2 = footerButton.theme;
        if (i2 != 0 && !this.applyPartnerResources) {
            i = i2;
        }
        if (!this.applyPartnerResources) {
            return i;
        }
        int color = PartnerConfigHelper.get(this.context).getColor(this.context, partnerConfig);
        if (color == 0) {
            return 2132017680;
        }
        if (color != 0) {
            return 2132017679;
        }
        return i;
    }

    public boolean isPrimaryButtonVisible() {
        if (getPrimaryButtonView() == null || getPrimaryButtonView().getVisibility() != 0) {
            return false;
        }
        return true;
    }

    public boolean isSecondaryButtonVisible() {
        if (getSecondaryButtonView() == null || getSecondaryButtonView().getVisibility() != 0) {
            return false;
        }
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x005d  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x006e  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x007e  */
    /* JADX WARN: Removed duplicated region for block: B:40:0x00a9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void repopulateButtons() {
        /*
            Method dump skipped, instructions count: 248
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.setupcompat.template.FooterBarMixin.repopulateButtons():void");
    }

    public LinearLayout getButtonContainer() {
        return this.buttonContainer;
    }
}

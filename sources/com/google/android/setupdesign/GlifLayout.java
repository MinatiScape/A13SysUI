package com.google.android.setupdesign;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.google.android.setupcompat.PartnerCustomizationLayout;
import com.google.android.setupcompat.internal.TemplateLayout;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.template.FooterBarMixin$$ExternalSyntheticOutline0;
import com.google.android.setupcompat.template.StatusBarMixin;
import com.google.android.setupdesign.template.DescriptionMixin;
import com.google.android.setupdesign.template.HeaderMixin;
import com.google.android.setupdesign.template.IconMixin;
import com.google.android.setupdesign.template.IllustrationProgressMixin;
import com.google.android.setupdesign.template.ProgressBarMixin;
import com.google.android.setupdesign.template.RequireScrollMixin;
import com.google.android.setupdesign.util.LayoutStyler;
import com.google.android.setupdesign.util.PartnerStyleHelper;
import com.google.android.setupdesign.util.TextViewPartnerStyler;
import com.google.android.setupdesign.view.BottomScrollView;
import java.util.Objects;
/* loaded from: classes.dex */
public class GlifLayout extends PartnerCustomizationLayout {
    public boolean applyPartnerHeavyThemeResource;
    public ColorStateList backgroundBaseColor;
    public boolean backgroundPatterned;
    public ColorStateList primaryColor;

    public GlifLayout(Context context) {
        this(context, 0, 0);
    }

    public GlifLayout(Context context, int i) {
        this(context, i, 0);
    }

    private void init(AttributeSet attributeSet, int i) {
        boolean z;
        ScrollView scrollView;
        if (!isInEditMode()) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.SudGlifLayout, i, 0);
            boolean z2 = obtainStyledAttributes.getBoolean(4, false);
            if (!shouldApplyPartnerResource() || !z2) {
                z = false;
            } else {
                z = true;
            }
            this.applyPartnerHeavyThemeResource = z;
            registerMixin(HeaderMixin.class, new HeaderMixin(this, attributeSet, i));
            registerMixin(DescriptionMixin.class, new DescriptionMixin(this, attributeSet, i));
            registerMixin(IconMixin.class, new IconMixin(this, attributeSet, i));
            registerMixin(ProgressBarMixin.class, new ProgressBarMixin(this, attributeSet, i));
            registerMixin(IllustrationProgressMixin.class, new IllustrationProgressMixin(this));
            registerMixin(RequireScrollMixin.class, new RequireScrollMixin());
            View findManagedViewById = findManagedViewById(2131428993);
            if (findManagedViewById instanceof ScrollView) {
                scrollView = (ScrollView) findManagedViewById;
            } else {
                scrollView = null;
            }
            if (scrollView != null) {
                if (scrollView instanceof BottomScrollView) {
                    BottomScrollView bottomScrollView = (BottomScrollView) scrollView;
                } else {
                    Log.w("ScrollViewDelegate", "Cannot set non-BottomScrollView. Found=" + scrollView);
                }
            }
            ColorStateList colorStateList = obtainStyledAttributes.getColorStateList(2);
            if (colorStateList != null) {
                this.primaryColor = colorStateList;
                updateBackground();
                ProgressBarMixin progressBarMixin = (ProgressBarMixin) getMixin(ProgressBarMixin.class);
                Objects.requireNonNull(progressBarMixin);
                progressBarMixin.color = colorStateList;
                ProgressBar peekProgressBar = progressBarMixin.peekProgressBar();
                if (peekProgressBar != null) {
                    peekProgressBar.setIndeterminateTintList(colorStateList);
                    peekProgressBar.setProgressBackgroundTintList(colorStateList);
                }
            }
            if (shouldApplyPartnerHeavyThemeResource() && !useFullDynamicColor()) {
                getRootView().setBackgroundColor(PartnerConfigHelper.get(getContext()).getColor(getContext(), PartnerConfig.CONFIG_LAYOUT_BACKGROUND_COLOR));
            }
            View findManagedViewById2 = findManagedViewById(2131428973);
            if (findManagedViewById2 != null) {
                if (shouldApplyPartnerResource()) {
                    LayoutStyler.applyPartnerCustomizationExtraPaddingStyle(findManagedViewById2);
                }
                if (!(this instanceof GlifPreferenceLayout)) {
                    tryApplyPartnerCustomizationContentPaddingTopStyle(findManagedViewById2);
                }
            }
            updateLandscapeMiddleHorizontalSpacing();
            this.backgroundBaseColor = obtainStyledAttributes.getColorStateList(0);
            updateBackground();
            this.backgroundPatterned = obtainStyledAttributes.getBoolean(1, true);
            updateBackground();
            int resourceId = obtainStyledAttributes.getResourceId(3, 0);
            if (resourceId != 0) {
                ViewStub viewStub = (ViewStub) findManagedViewById(2131428984);
                viewStub.setLayoutResource(resourceId);
                viewStub.inflate();
            }
            obtainStyledAttributes.recycle();
        }
    }

    @Override // com.google.android.setupcompat.PartnerCustomizationLayout, com.google.android.setupcompat.internal.TemplateLayout
    public ViewGroup findContainer(int i) {
        if (i == 0) {
            i = 2131428973;
        }
        return super.findContainer(i);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        PartnerConfig partnerConfig;
        boolean z;
        int i;
        ViewGroup.LayoutParams layoutParams;
        PartnerConfigHelper partnerConfigHelper;
        PartnerConfig partnerConfig2;
        int dimension;
        int i2;
        PartnerConfig partnerConfig3 = PartnerConfig.CONFIG_DESCRIPTION_LINK_FONT_FAMILY;
        PartnerConfig partnerConfig4 = PartnerConfig.CONFIG_DESCRIPTION_FONT_FAMILY;
        PartnerConfig partnerConfig5 = PartnerConfig.CONFIG_DESCRIPTION_TEXT_SIZE;
        PartnerConfig partnerConfig6 = PartnerConfig.CONFIG_DESCRIPTION_LINK_TEXT_COLOR;
        PartnerConfig partnerConfig7 = PartnerConfig.CONFIG_DESCRIPTION_TEXT_COLOR;
        super.onFinishInflate();
        IconMixin iconMixin = (IconMixin) getMixin(IconMixin.class);
        Objects.requireNonNull(iconMixin);
        if (PartnerStyleHelper.shouldApplyPartnerResource(iconMixin.templateLayout)) {
            final ImageView view = iconMixin.getView();
            FrameLayout frameLayout = (FrameLayout) iconMixin.templateLayout.findManagedViewById(2131428978);
            if (!(view == null || frameLayout == null)) {
                Context context = view.getContext();
                int layoutGravity = PartnerStyleHelper.getLayoutGravity(context);
                if (layoutGravity != 0 && (view.getLayoutParams() instanceof FrameLayout.LayoutParams)) {
                    FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) view.getLayoutParams();
                    layoutParams2.gravity = layoutGravity;
                    view.setLayoutParams(layoutParams2);
                }
                PartnerConfigHelper partnerConfigHelper2 = PartnerConfigHelper.get(context);
                PartnerConfig partnerConfig8 = PartnerConfig.CONFIG_ICON_SIZE;
                if (partnerConfigHelper2.isPartnerConfigAvailable(partnerConfig8)) {
                    view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: com.google.android.setupdesign.util.HeaderAreaStyler$1
                        @Override // android.view.ViewTreeObserver.OnPreDrawListener
                        public final boolean onPreDraw() {
                            view.getViewTreeObserver().removeOnPreDrawListener(this);
                            if (view.getDrawable() == null || (view.getDrawable() instanceof VectorDrawable) || (view.getDrawable() instanceof VectorDrawableCompat)) {
                                return true;
                            }
                            String str = Build.TYPE;
                            if (!str.equals("userdebug") && !str.equals("eng")) {
                                return true;
                            }
                            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("To achieve scaling icon in SetupDesign lib, should use vector drawable icon from ");
                            m.append(view.getContext().getPackageName());
                            Log.w("HeaderAreaStyler", m.toString());
                            return true;
                        }
                    });
                    ViewGroup.LayoutParams layoutParams3 = view.getLayoutParams();
                    layoutParams3.height = (int) FooterBarMixin$$ExternalSyntheticOutline0.m(context, context, partnerConfig8, 0.0f);
                    layoutParams3.width = -2;
                    view.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    Drawable drawable = view.getDrawable();
                    if (drawable != null && drawable.getIntrinsicWidth() > drawable.getIntrinsicHeight() * 2 && (i2 = layoutParams3.height) > (dimension = (int) context.getResources().getDimension(2131167163))) {
                        i = i2 - dimension;
                        layoutParams3.height = dimension;
                        layoutParams = frameLayout.getLayoutParams();
                        partnerConfigHelper = PartnerConfigHelper.get(context);
                        partnerConfig2 = PartnerConfig.CONFIG_ICON_MARGIN_TOP;
                        if (partnerConfigHelper.isPartnerConfigAvailable(partnerConfig2) && (layoutParams instanceof ViewGroup.MarginLayoutParams)) {
                            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                            marginLayoutParams.setMargins(marginLayoutParams.leftMargin, ((int) FooterBarMixin$$ExternalSyntheticOutline0.m(context, context, partnerConfig2, 0.0f)) + i, marginLayoutParams.rightMargin, marginLayoutParams.bottomMargin);
                        }
                    }
                }
                i = 0;
                layoutParams = frameLayout.getLayoutParams();
                partnerConfigHelper = PartnerConfigHelper.get(context);
                partnerConfig2 = PartnerConfig.CONFIG_ICON_MARGIN_TOP;
                if (partnerConfigHelper.isPartnerConfigAvailable(partnerConfig2)) {
                    ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) layoutParams;
                    marginLayoutParams2.setMargins(marginLayoutParams2.leftMargin, ((int) FooterBarMixin$$ExternalSyntheticOutline0.m(context, context, partnerConfig2, 0.0f)) + i, marginLayoutParams2.rightMargin, marginLayoutParams2.bottomMargin);
                }
            }
        }
        HeaderMixin headerMixin = (HeaderMixin) getMixin(HeaderMixin.class);
        Objects.requireNonNull(headerMixin);
        TextView textView = (TextView) headerMixin.templateLayout.findManagedViewById(2131428954);
        if (PartnerStyleHelper.shouldApplyPartnerResource(headerMixin.templateLayout)) {
            View findManagedViewById = headerMixin.templateLayout.findManagedViewById(2131428976);
            LayoutStyler.applyPartnerCustomizationExtraPaddingStyle(findManagedViewById);
            if (textView != null) {
                TextViewPartnerStyler.applyPartnerCustomizationStyle(textView, new TextViewPartnerStyler.TextPartnerConfigs(PartnerConfig.CONFIG_HEADER_TEXT_COLOR, null, PartnerConfig.CONFIG_HEADER_TEXT_SIZE, PartnerConfig.CONFIG_HEADER_FONT_FAMILY, null, PartnerConfig.CONFIG_HEADER_TEXT_MARGIN_TOP, PartnerConfig.CONFIG_HEADER_TEXT_MARGIN_BOTTOM, PartnerStyleHelper.getLayoutGravity(textView.getContext())));
            }
            ViewGroup viewGroup = (ViewGroup) findManagedViewById;
            if (viewGroup != null) {
                Context context2 = viewGroup.getContext();
                viewGroup.setBackgroundColor(PartnerConfigHelper.get(context2).getColor(context2, PartnerConfig.CONFIG_HEADER_AREA_BACKGROUND_COLOR));
                PartnerConfigHelper partnerConfigHelper3 = PartnerConfigHelper.get(context2);
                PartnerConfig partnerConfig9 = PartnerConfig.CONFIG_HEADER_CONTAINER_MARGIN_BOTTOM;
                if (partnerConfigHelper3.isPartnerConfigAvailable(partnerConfig9)) {
                    ViewGroup.LayoutParams layoutParams4 = viewGroup.getLayoutParams();
                    if (layoutParams4 instanceof ViewGroup.MarginLayoutParams) {
                        ViewGroup.MarginLayoutParams marginLayoutParams3 = (ViewGroup.MarginLayoutParams) layoutParams4;
                        marginLayoutParams3.setMargins(marginLayoutParams3.leftMargin, marginLayoutParams3.topMargin, marginLayoutParams3.rightMargin, (int) FooterBarMixin$$ExternalSyntheticOutline0.m(context2, context2, partnerConfig9, 0.0f));
                        viewGroup.setLayoutParams(layoutParams4);
                    }
                }
            }
        }
        headerMixin.tryUpdateAutoTextSizeFlagWithPartnerConfig();
        if (headerMixin.autoTextSizeEnabled) {
            headerMixin.autoAdjustTextSize(textView);
        }
        DescriptionMixin descriptionMixin = (DescriptionMixin) getMixin(DescriptionMixin.class);
        Objects.requireNonNull(descriptionMixin);
        TextView textView2 = (TextView) descriptionMixin.templateLayout.findManagedViewById(2131428985);
        if (textView2 == null || !PartnerStyleHelper.shouldApplyPartnerResource(descriptionMixin.templateLayout)) {
            partnerConfig = partnerConfig3;
        } else {
            partnerConfig = partnerConfig3;
            TextViewPartnerStyler.applyPartnerCustomizationStyle(textView2, new TextViewPartnerStyler.TextPartnerConfigs(partnerConfig7, partnerConfig6, partnerConfig5, partnerConfig4, partnerConfig3, PartnerConfig.CONFIG_DESCRIPTION_TEXT_MARGIN_TOP, PartnerConfig.CONFIG_DESCRIPTION_TEXT_MARGIN_BOTTOM, PartnerStyleHelper.getLayoutGravity(textView2.getContext())));
        }
        ProgressBarMixin progressBarMixin = (ProgressBarMixin) getMixin(ProgressBarMixin.class);
        Objects.requireNonNull(progressBarMixin);
        ProgressBar peekProgressBar = progressBarMixin.peekProgressBar();
        if (progressBarMixin.useBottomProgressBar && peekProgressBar != null) {
            TemplateLayout templateLayout = progressBarMixin.templateLayout;
            if (!(templateLayout instanceof GlifLayout)) {
                z = false;
            } else {
                z = ((GlifLayout) templateLayout).shouldApplyPartnerHeavyThemeResource();
            }
            if (z) {
                Context context3 = peekProgressBar.getContext();
                ViewGroup.LayoutParams layoutParams5 = peekProgressBar.getLayoutParams();
                if (layoutParams5 instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams marginLayoutParams4 = (ViewGroup.MarginLayoutParams) layoutParams5;
                    int i3 = marginLayoutParams4.topMargin;
                    PartnerConfigHelper partnerConfigHelper4 = PartnerConfigHelper.get(context3);
                    PartnerConfig partnerConfig10 = PartnerConfig.CONFIG_PROGRESS_BAR_MARGIN_TOP;
                    if (partnerConfigHelper4.isPartnerConfigAvailable(partnerConfig10)) {
                        i3 = (int) PartnerConfigHelper.get(context3).getDimension(context3, partnerConfig10, context3.getResources().getDimension(2131167196));
                    }
                    int i4 = marginLayoutParams4.bottomMargin;
                    PartnerConfigHelper partnerConfigHelper5 = PartnerConfigHelper.get(context3);
                    PartnerConfig partnerConfig11 = PartnerConfig.CONFIG_PROGRESS_BAR_MARGIN_BOTTOM;
                    if (partnerConfigHelper5.isPartnerConfigAvailable(partnerConfig11)) {
                        i4 = (int) PartnerConfigHelper.get(context3).getDimension(context3, partnerConfig11, context3.getResources().getDimension(2131167195));
                    }
                    if (!(i3 == marginLayoutParams4.topMargin && i4 == marginLayoutParams4.bottomMargin)) {
                        marginLayoutParams4.setMargins(marginLayoutParams4.leftMargin, i3, marginLayoutParams4.rightMargin, i4);
                    }
                }
            } else {
                Context context4 = peekProgressBar.getContext();
                ViewGroup.LayoutParams layoutParams6 = peekProgressBar.getLayoutParams();
                if (layoutParams6 instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams marginLayoutParams5 = (ViewGroup.MarginLayoutParams) layoutParams6;
                    marginLayoutParams5.setMargins(marginLayoutParams5.leftMargin, (int) context4.getResources().getDimension(2131167196), marginLayoutParams5.rightMargin, (int) context4.getResources().getDimension(2131167195));
                }
            }
        }
        TextView textView3 = (TextView) findManagedViewById(2131428975);
        if (textView3 == null) {
            return;
        }
        if (this.applyPartnerHeavyThemeResource) {
            TextViewPartnerStyler.applyPartnerCustomizationStyle(textView3, new TextViewPartnerStyler.TextPartnerConfigs(partnerConfig7, partnerConfig6, partnerConfig5, partnerConfig4, partnerConfig, null, null, PartnerStyleHelper.getLayoutGravity(textView3.getContext())));
        } else if (shouldApplyPartnerResource()) {
            int layoutGravity2 = PartnerStyleHelper.getLayoutGravity(textView3.getContext());
            TextViewPartnerStyler.applyPartnerCustomizationVerticalMargins(textView3, new TextViewPartnerStyler.TextPartnerConfigs(null, null, null, null, null, null, null, layoutGravity2));
            textView3.setGravity(layoutGravity2);
        }
    }

    @Override // com.google.android.setupcompat.PartnerCustomizationLayout, com.google.android.setupcompat.internal.TemplateLayout
    public View onInflateTemplate(LayoutInflater layoutInflater, int i) {
        if (i == 0) {
            i = 2131624553;
        }
        return inflateTemplate(layoutInflater, 2132017784, i);
    }

    public final boolean shouldApplyPartnerHeavyThemeResource() {
        if (this.applyPartnerHeavyThemeResource || (shouldApplyPartnerResource() && PartnerConfigHelper.shouldApplyExtendedPartnerConfig(getContext()))) {
            return true;
        }
        return false;
    }

    public GlifLayout(Context context, int i, int i2) {
        super(context, i, i2);
        this.backgroundPatterned = true;
        this.applyPartnerHeavyThemeResource = false;
        init(null, 2130969880);
    }

    @TargetApi(17)
    public final void tryApplyPartnerCustomizationContentPaddingTopStyle(View view) {
        int m;
        Context context = view.getContext();
        PartnerConfigHelper partnerConfigHelper = PartnerConfigHelper.get(context);
        PartnerConfig partnerConfig = PartnerConfig.CONFIG_CONTENT_PADDING_TOP;
        boolean isPartnerConfigAvailable = partnerConfigHelper.isPartnerConfigAvailable(partnerConfig);
        if (shouldApplyPartnerResource() && isPartnerConfigAvailable && (m = (int) FooterBarMixin$$ExternalSyntheticOutline0.m(context, context, partnerConfig, 0.0f)) != view.getPaddingTop()) {
            view.setPadding(view.getPaddingStart(), m, view.getPaddingEnd(), view.getPaddingBottom());
        }
    }

    public final void updateBackground() {
        Drawable drawable;
        if (findManagedViewById(2131428953) != null) {
            int i = 0;
            ColorStateList colorStateList = this.backgroundBaseColor;
            if (colorStateList != null) {
                i = colorStateList.getDefaultColor();
            } else {
                ColorStateList colorStateList2 = this.primaryColor;
                if (colorStateList2 != null) {
                    i = colorStateList2.getDefaultColor();
                }
            }
            if (this.backgroundPatterned) {
                drawable = new GlifPatternDrawable(i);
            } else {
                drawable = new ColorDrawable(i);
            }
            ((StatusBarMixin) getMixin(StatusBarMixin.class)).setStatusBarBackground(drawable);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:25:0x00e2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateLandscapeMiddleHorizontalSpacing() {
        /*
            r9 = this;
            android.content.res.Resources r0 = r9.getResources()
            r1 = 2131167149(0x7f0707ad, float:1.7948563E38)
            int r0 = r0.getDimensionPixelSize(r1)
            boolean r1 = r9.shouldApplyPartnerResource()
            r2 = 0
            if (r1 == 0) goto L_0x0036
            android.content.Context r1 = r9.getContext()
            com.google.android.setupcompat.partnerconfig.PartnerConfigHelper r1 = com.google.android.setupcompat.partnerconfig.PartnerConfigHelper.get(r1)
            com.google.android.setupcompat.partnerconfig.PartnerConfig r3 = com.google.android.setupcompat.partnerconfig.PartnerConfig.CONFIG_LAND_MIDDLE_HORIZONTAL_SPACING
            boolean r1 = r1.isPartnerConfigAvailable(r3)
            if (r1 == 0) goto L_0x0036
            android.content.Context r0 = r9.getContext()
            com.google.android.setupcompat.partnerconfig.PartnerConfigHelper r0 = com.google.android.setupcompat.partnerconfig.PartnerConfigHelper.get(r0)
            android.content.Context r1 = r9.getContext()
            java.util.Objects.requireNonNull(r0)
            float r0 = r0.getDimension(r1, r3, r2)
            int r0 = (int) r0
        L_0x0036:
            r1 = 2131428971(0x7f0b066b, float:1.8479602E38)
            android.view.View r1 = r9.findManagedViewById(r1)
            r3 = 1
            r4 = 0
            if (r1 == 0) goto L_0x0095
            boolean r5 = r9.shouldApplyPartnerResource()
            if (r5 == 0) goto L_0x006c
            android.content.Context r5 = r9.getContext()
            com.google.android.setupcompat.partnerconfig.PartnerConfigHelper r5 = com.google.android.setupcompat.partnerconfig.PartnerConfigHelper.get(r5)
            com.google.android.setupcompat.partnerconfig.PartnerConfig r6 = com.google.android.setupcompat.partnerconfig.PartnerConfig.CONFIG_LAYOUT_MARGIN_END
            boolean r5 = r5.isPartnerConfigAvailable(r6)
            if (r5 == 0) goto L_0x006c
            android.content.Context r5 = r9.getContext()
            com.google.android.setupcompat.partnerconfig.PartnerConfigHelper r5 = com.google.android.setupcompat.partnerconfig.PartnerConfigHelper.get(r5)
            android.content.Context r7 = r9.getContext()
            java.util.Objects.requireNonNull(r5)
            float r5 = r5.getDimension(r7, r6, r2)
            int r5 = (int) r5
            goto L_0x0083
        L_0x006c:
            android.content.Context r5 = r9.getContext()
            int[] r6 = new int[r3]
            r7 = 2130969887(0x7f04051f, float:1.7548469E38)
            r6[r4] = r7
            android.content.res.TypedArray r5 = r5.obtainStyledAttributes(r6)
            int r6 = r5.getDimensionPixelSize(r4, r4)
            r5.recycle()
            r5 = r6
        L_0x0083:
            int r6 = r0 / 2
            int r6 = r6 - r5
            int r5 = r1.getPaddingStart()
            int r7 = r1.getPaddingTop()
            int r8 = r1.getPaddingBottom()
            r1.setPadding(r5, r7, r6, r8)
        L_0x0095:
            r5 = 2131428970(0x7f0b066a, float:1.84796E38)
            android.view.View r5 = r9.findManagedViewById(r5)
            if (r5 == 0) goto L_0x00f5
            boolean r6 = r9.shouldApplyPartnerResource()
            if (r6 == 0) goto L_0x00c9
            android.content.Context r6 = r9.getContext()
            com.google.android.setupcompat.partnerconfig.PartnerConfigHelper r6 = com.google.android.setupcompat.partnerconfig.PartnerConfigHelper.get(r6)
            com.google.android.setupcompat.partnerconfig.PartnerConfig r7 = com.google.android.setupcompat.partnerconfig.PartnerConfig.CONFIG_LAYOUT_MARGIN_START
            boolean r6 = r6.isPartnerConfigAvailable(r7)
            if (r6 == 0) goto L_0x00c9
            android.content.Context r3 = r9.getContext()
            com.google.android.setupcompat.partnerconfig.PartnerConfigHelper r3 = com.google.android.setupcompat.partnerconfig.PartnerConfigHelper.get(r3)
            android.content.Context r9 = r9.getContext()
            java.util.Objects.requireNonNull(r3)
            float r9 = r3.getDimension(r9, r7, r2)
            int r9 = (int) r9
            goto L_0x00e0
        L_0x00c9:
            android.content.Context r9 = r9.getContext()
            int[] r2 = new int[r3]
            r3 = 2130969888(0x7f040520, float:1.754847E38)
            r2[r4] = r3
            android.content.res.TypedArray r9 = r9.obtainStyledAttributes(r2)
            int r2 = r9.getDimensionPixelSize(r4, r4)
            r9.recycle()
            r9 = r2
        L_0x00e0:
            if (r1 == 0) goto L_0x00e6
            int r0 = r0 / 2
            int r4 = r0 - r9
        L_0x00e6:
            int r9 = r5.getPaddingTop()
            int r0 = r5.getPaddingEnd()
            int r1 = r5.getPaddingBottom()
            r5.setPadding(r4, r9, r0, r1)
        L_0x00f5:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.setupdesign.GlifLayout.updateLandscapeMiddleHorizontalSpacing():void");
    }

    public GlifLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.backgroundPatterned = true;
        this.applyPartnerHeavyThemeResource = false;
        init(attributeSet, 2130969880);
    }

    @TargetApi(QSTileImpl.H.STALE)
    public GlifLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.backgroundPatterned = true;
        this.applyPartnerHeavyThemeResource = false;
        init(attributeSet, i);
    }
}

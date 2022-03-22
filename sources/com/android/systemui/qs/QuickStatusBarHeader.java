package com.android.systemui.qs;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.DisplayCutout;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Space;
import com.android.internal.policy.SystemBarUtils;
import com.android.keyguard.LockIconViewController$$ExternalSyntheticLambda2;
import com.android.settingslib.Utils;
import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.qs.PathInterpolatorBuilder;
import com.android.systemui.qs.TouchAnimator;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.phone.StatusIconContainer;
import com.android.systemui.statusbar.policy.Clock;
import com.android.systemui.statusbar.policy.VariableDateView;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class QuickStatusBarHeader extends FrameLayout {
    public static final /* synthetic */ int $r8$clinit = 0;
    public TouchAnimator mAlphaAnimator;
    public BatteryMeterView mBatteryRemainingIcon;
    public ViewGroup mClockContainer;
    public VariableDateView mClockDateView;
    public View mClockIconsSeparator;
    public Clock mClockView;
    public boolean mConfigShowBatteryEstimate;
    public View mContainer;
    public int mCutOutPaddingLeft;
    public int mCutOutPaddingRight;
    public View mDateContainer;
    public Space mDatePrivacySeparator;
    public View mDatePrivacyView;
    public View mDateView;
    public boolean mExpanded;
    public boolean mHasCenterCutout;
    public QuickQSPanel mHeaderQsPanel;
    public StatusIconContainer mIconContainer;
    public TouchAnimator mIconsAlphaAnimator;
    public TouchAnimator mIconsAlphaAnimatorFixed;
    public StatusBarContentInsetsProvider mInsetsProvider;
    public boolean mIsSingleCarrier;
    public float mKeyguardExpansionFraction;
    public View mPrivacyChip;
    public View mPrivacyContainer;
    public View mQSCarriers;
    public QSExpansionPathInterpolator mQSExpansionPathInterpolator;
    public boolean mQsDisabled;
    public View mRightLayout;
    public List<String> mRssiIgnoredSlots;
    public View mSecurityHeaderView;
    public boolean mShowClockIconsSeparator;
    public View mStatusIconsView;
    public StatusBarIconController.TintedIconManager mTintedIconManager;
    public int mTopViewMeasureHeight;
    public TouchAnimator mTranslationAnimator;
    public boolean mUseCombinedQSHeader;
    public int mWaterfallTopInset;
    public int mRoundedCornerPadding = 0;
    public int mTextColorPrimary = 0;

    public final void setDatePrivacyContainersWidth(boolean z) {
        int i;
        float f;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mDateContainer.getLayoutParams();
        int i2 = -2;
        if (z) {
            i = -2;
        } else {
            i = 0;
        }
        layoutParams.width = i;
        float f2 = 0.0f;
        if (z) {
            f = 0.0f;
        } else {
            f = 1.0f;
        }
        layoutParams.weight = f;
        this.mDateContainer.setLayoutParams(layoutParams);
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.mPrivacyContainer.getLayoutParams();
        if (!z) {
            i2 = 0;
        }
        layoutParams2.width = i2;
        if (!z) {
            f2 = 1.0f;
        }
        layoutParams2.weight = f2;
        this.mPrivacyContainer.setLayoutParams(layoutParams2);
    }

    public final void setSeparatorVisibility(boolean z) {
        int i;
        int i2;
        int i3;
        float f;
        int i4 = 8;
        int i5 = 0;
        if (z) {
            i = 0;
        } else {
            i = 8;
        }
        if (this.mClockIconsSeparator.getVisibility() != i) {
            View view = this.mClockIconsSeparator;
            if (z) {
                i2 = 0;
            } else {
                i2 = 8;
            }
            view.setVisibility(i2);
            View view2 = this.mQSCarriers;
            if (!z) {
                i4 = 0;
            }
            view2.setVisibility(i4);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mClockContainer.getLayoutParams();
            if (z) {
                i3 = 0;
            } else {
                i3 = -2;
            }
            layoutParams.width = i3;
            float f2 = 1.0f;
            if (z) {
                f = 1.0f;
            } else {
                f = 0.0f;
            }
            layoutParams.weight = f;
            this.mClockContainer.setLayoutParams(layoutParams);
            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.mRightLayout.getLayoutParams();
            if (!z) {
                i5 = -2;
            }
            layoutParams2.width = i5;
            if (!z) {
                f2 = 0.0f;
            }
            layoutParams2.weight = f2;
            this.mRightLayout.setLayoutParams(layoutParams2);
        }
    }

    public final void updateAlphaAnimator() {
        if (this.mUseCombinedQSHeader) {
            this.mAlphaAnimator = null;
            return;
        }
        TouchAnimator.Builder builder = new TouchAnimator.Builder();
        builder.addFloat(this.mSecurityHeaderView, "alpha", 0.0f, 1.0f);
        builder.addFloat(this.mDateView, "alpha", 0.0f, 0.0f, 1.0f);
        builder.addFloat(this.mClockDateView, "alpha", 1.0f, 0.0f, 0.0f);
        builder.addFloat(this.mQSCarriers, "alpha", 0.0f, 1.0f);
        builder.mListener = new TouchAnimator.ListenerAdapter() { // from class: com.android.systemui.qs.QuickStatusBarHeader.1
            @Override // com.android.systemui.qs.TouchAnimator.Listener
            public final void onAnimationAtEnd() {
                QuickStatusBarHeader quickStatusBarHeader = QuickStatusBarHeader.this;
                if (!quickStatusBarHeader.mIsSingleCarrier) {
                    quickStatusBarHeader.mIconContainer.addIgnoredSlots(quickStatusBarHeader.mRssiIgnoredSlots);
                }
                QuickStatusBarHeader.this.mClockDateView.setVisibility(8);
            }

            @Override // com.android.systemui.qs.TouchAnimator.ListenerAdapter, com.android.systemui.qs.TouchAnimator.Listener
            public final void onAnimationAtStart() {
                VariableDateView variableDateView = QuickStatusBarHeader.this.mClockDateView;
                Objects.requireNonNull(variableDateView);
                variableDateView.freezeSwitching = false;
                QuickStatusBarHeader.this.mClockDateView.setVisibility(0);
                QuickStatusBarHeader quickStatusBarHeader = QuickStatusBarHeader.this;
                quickStatusBarHeader.setSeparatorVisibility(quickStatusBarHeader.mShowClockIconsSeparator);
                QuickStatusBarHeader quickStatusBarHeader2 = QuickStatusBarHeader.this;
                quickStatusBarHeader2.mIconContainer.removeIgnoredSlots(quickStatusBarHeader2.mRssiIgnoredSlots);
            }

            @Override // com.android.systemui.qs.TouchAnimator.Listener
            public final void onAnimationStarted() {
                QuickStatusBarHeader.this.mClockDateView.setVisibility(0);
                VariableDateView variableDateView = QuickStatusBarHeader.this.mClockDateView;
                Objects.requireNonNull(variableDateView);
                variableDateView.freezeSwitching = true;
                QuickStatusBarHeader.this.setSeparatorVisibility(false);
                QuickStatusBarHeader quickStatusBarHeader = QuickStatusBarHeader.this;
                if (!quickStatusBarHeader.mIsSingleCarrier) {
                    quickStatusBarHeader.mIconContainer.addIgnoredSlots(quickStatusBarHeader.mRssiIgnoredSlots);
                }
            }
        };
        this.mAlphaAnimator = builder.build();
    }

    public final void updateAnimators() {
        PathInterpolatorBuilder.PathInterpolator pathInterpolator = null;
        if (this.mUseCombinedQSHeader) {
            this.mTranslationAnimator = null;
            return;
        }
        updateAlphaAnimator();
        int i = this.mTopViewMeasureHeight;
        TouchAnimator.Builder builder = new TouchAnimator.Builder();
        builder.addFloat(this.mContainer, "translationY", 0.0f, i);
        QSExpansionPathInterpolator qSExpansionPathInterpolator = this.mQSExpansionPathInterpolator;
        if (qSExpansionPathInterpolator != null) {
            pathInterpolator = qSExpansionPathInterpolator.getYInterpolator();
        }
        builder.mInterpolator = pathInterpolator;
        this.mTranslationAnimator = builder.build();
    }

    public final void updateHeadersPadding() {
        int i;
        int i2;
        View view = this.mDatePrivacyView;
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        marginLayoutParams.setMarginStart(0);
        marginLayoutParams.setMarginEnd(0);
        view.setLayoutParams(marginLayoutParams);
        View view2 = this.mStatusIconsView;
        ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) view2.getLayoutParams();
        marginLayoutParams2.setMarginStart(0);
        marginLayoutParams2.setMarginEnd(0);
        view2.setLayoutParams(marginLayoutParams2);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
        int i3 = layoutParams.leftMargin;
        int i4 = layoutParams.rightMargin;
        int i5 = this.mCutOutPaddingLeft;
        if (i5 > 0) {
            i = Math.max(Math.max(i5, this.mRoundedCornerPadding) - i3, 0);
        } else {
            i = 0;
        }
        int i6 = this.mCutOutPaddingRight;
        if (i6 > 0) {
            i2 = Math.max(Math.max(i6, this.mRoundedCornerPadding) - i4, 0);
        } else {
            i2 = 0;
        }
        this.mDatePrivacyView.setPadding(i, this.mWaterfallTopInset, i2, 0);
        this.mStatusIconsView.setPadding(i, this.mWaterfallTopInset, i2, 0);
    }

    public final void updateResources() {
        boolean z;
        int i;
        Resources resources = ((FrameLayout) this).mContext.getResources();
        boolean z2 = resources.getBoolean(2131034179);
        int i2 = 0;
        if (z2 || this.mUseCombinedQSHeader || this.mQsDisabled) {
            z = true;
        } else {
            z = false;
        }
        View view = this.mStatusIconsView;
        if (z) {
            i = 8;
        } else {
            i = 0;
        }
        view.setVisibility(i);
        View view2 = this.mDatePrivacyView;
        if (z) {
            i2 = 8;
        }
        view2.setVisibility(i2);
        this.mConfigShowBatteryEstimate = resources.getBoolean(2131034160);
        this.mRoundedCornerPadding = resources.getDimensionPixelSize(2131166936);
        int quickQsOffsetHeight = SystemBarUtils.getQuickQsOffsetHeight(((FrameLayout) this).mContext);
        this.mDatePrivacyView.getLayoutParams().height = Math.max(quickQsOffsetHeight, this.mDatePrivacyView.getMinimumHeight());
        View view3 = this.mDatePrivacyView;
        view3.setLayoutParams(view3.getLayoutParams());
        this.mStatusIconsView.getLayoutParams().height = Math.max(quickQsOffsetHeight, this.mStatusIconsView.getMinimumHeight());
        View view4 = this.mStatusIconsView;
        view4.setLayoutParams(view4.getLayoutParams());
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (this.mQsDisabled) {
            layoutParams.height = this.mStatusIconsView.getLayoutParams().height;
        } else {
            layoutParams.height = -2;
        }
        setLayoutParams(layoutParams);
        int colorAttrDefaultColor = Utils.getColorAttrDefaultColor(((FrameLayout) this).mContext, 16842806);
        if (colorAttrDefaultColor != this.mTextColorPrimary) {
            int colorAttrDefaultColor2 = Utils.getColorAttrDefaultColor(((FrameLayout) this).mContext, 16842808);
            this.mTextColorPrimary = colorAttrDefaultColor;
            this.mClockView.setTextColor(colorAttrDefaultColor);
            StatusBarIconController.TintedIconManager tintedIconManager = this.mTintedIconManager;
            if (tintedIconManager != null) {
                tintedIconManager.setTint(colorAttrDefaultColor);
            }
            BatteryMeterView batteryMeterView = this.mBatteryRemainingIcon;
            int i3 = this.mTextColorPrimary;
            batteryMeterView.updateColors(i3, colorAttrDefaultColor2, i3);
        }
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mHeaderQsPanel.getLayoutParams();
        if (z2 || !this.mUseCombinedQSHeader) {
            quickQsOffsetHeight = ((FrameLayout) this).mContext.getResources().getDimensionPixelSize(2131166838);
        }
        marginLayoutParams.topMargin = quickQsOffsetHeight;
        this.mHeaderQsPanel.setLayoutParams(marginLayoutParams);
        if (!this.mConfigShowBatteryEstimate || this.mHasCenterCutout) {
            this.mBatteryRemainingIcon.setPercentShowMode(1);
        } else {
            this.mBatteryRemainingIcon.setPercentShowMode(3);
        }
        updateHeadersPadding();
        updateAnimators();
        int dimensionPixelSize = ((FrameLayout) this).mContext.getResources().getDimensionPixelSize(2131167069);
        int dimensionPixelSize2 = ((FrameLayout) this).mContext.getResources().getDimensionPixelSize(2131167068);
        Clock clock = this.mClockView;
        clock.setPaddingRelative(dimensionPixelSize, clock.getPaddingTop(), dimensionPixelSize2, this.mClockView.getPaddingBottom());
        ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) this.mClockDateView.getLayoutParams();
        marginLayoutParams2.setMarginStart(dimensionPixelSize2);
        this.mClockDateView.setLayoutParams(marginLayoutParams2);
    }

    public QuickStatusBarHeader(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    public final WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        boolean z;
        DisplayCutout displayCutout = windowInsets.getDisplayCutout();
        Pair<Integer, Integer> statusBarContentInsetsForCurrentRotation = this.mInsetsProvider.getStatusBarContentInsetsForCurrentRotation();
        boolean currentRotationHasCornerCutout = this.mInsetsProvider.currentRotationHasCornerCutout();
        int i = 0;
        this.mDatePrivacyView.setPadding(((Integer) statusBarContentInsetsForCurrentRotation.first).intValue(), 0, ((Integer) statusBarContentInsetsForCurrentRotation.second).intValue(), 0);
        this.mStatusIconsView.setPadding(((Integer) statusBarContentInsetsForCurrentRotation.first).intValue(), 0, ((Integer) statusBarContentInsetsForCurrentRotation.second).intValue(), 0);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mDatePrivacySeparator.getLayoutParams();
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.mClockIconsSeparator.getLayoutParams();
        if (displayCutout != null) {
            Rect boundingRectTop = displayCutout.getBoundingRectTop();
            if (boundingRectTop.isEmpty() || currentRotationHasCornerCutout) {
                layoutParams.width = 0;
                this.mDatePrivacySeparator.setVisibility(8);
                layoutParams2.width = 0;
                setSeparatorVisibility(false);
                this.mShowClockIconsSeparator = false;
                this.mHasCenterCutout = false;
            } else {
                layoutParams.width = boundingRectTop.width();
                this.mDatePrivacySeparator.setVisibility(0);
                layoutParams2.width = boundingRectTop.width();
                this.mShowClockIconsSeparator = true;
                if (this.mKeyguardExpansionFraction == 0.0f) {
                    z = true;
                } else {
                    z = false;
                }
                setSeparatorVisibility(z);
                this.mHasCenterCutout = true;
            }
        }
        this.mDatePrivacySeparator.setLayoutParams(layoutParams);
        this.mClockIconsSeparator.setLayoutParams(layoutParams2);
        this.mCutOutPaddingLeft = ((Integer) statusBarContentInsetsForCurrentRotation.first).intValue();
        this.mCutOutPaddingRight = ((Integer) statusBarContentInsetsForCurrentRotation.second).intValue();
        if (displayCutout != null) {
            i = displayCutout.getWaterfallInsets().top;
        }
        this.mWaterfallTopInset = i;
        if (!this.mConfigShowBatteryEstimate || this.mHasCenterCutout) {
            this.mBatteryRemainingIcon.setPercentShowMode(1);
        } else {
            this.mBatteryRemainingIcon.setPercentShowMode(3);
        }
        updateHeadersPadding();
        return super.onApplyWindowInsets(windowInsets);
    }

    @Override // android.view.View
    public final void onConfigurationChanged(Configuration configuration) {
        boolean z;
        super.onConfigurationChanged(configuration);
        updateResources();
        int i = 0;
        boolean z2 = true;
        if (configuration.orientation == 2) {
            z = true;
        } else {
            z = false;
        }
        setDatePrivacyContainersWidth(z);
        if (configuration.orientation != 2) {
            z2 = false;
        }
        View view = this.mSecurityHeaderView;
        if (!z2) {
            i = 8;
        }
        view.setVisibility(i);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        boolean z;
        super.onFinishInflate();
        this.mHeaderQsPanel = (QuickQSPanel) findViewById(2131428659);
        this.mDatePrivacyView = findViewById(2131428663);
        this.mStatusIconsView = findViewById(2131428660);
        this.mQSCarriers = findViewById(2131427673);
        this.mContainer = findViewById(2131428646);
        this.mIconContainer = (StatusIconContainer) findViewById(2131428922);
        this.mPrivacyChip = findViewById(2131428617);
        this.mDateView = findViewById(2131427797);
        this.mClockDateView = (VariableDateView) findViewById(2131427798);
        this.mSecurityHeaderView = findViewById(2131428085);
        this.mClockIconsSeparator = findViewById(2131428836);
        this.mRightLayout = findViewById(2131428704);
        this.mDateContainer = findViewById(2131427799);
        this.mPrivacyContainer = findViewById(2131428618);
        this.mClockContainer = (ViewGroup) findViewById(2131427718);
        this.mClockView = (Clock) findViewById(2131427717);
        this.mDatePrivacySeparator = (Space) findViewById(2131428890);
        this.mBatteryRemainingIcon = (BatteryMeterView) findViewById(2131427572);
        updateResources();
        Configuration configuration = ((FrameLayout) this).mContext.getResources().getConfiguration();
        int i = 0;
        boolean z2 = true;
        if (configuration.orientation == 2) {
            z = true;
        } else {
            z = false;
        }
        setDatePrivacyContainersWidth(z);
        if (configuration.orientation != 2) {
            z2 = false;
        }
        View view = this.mSecurityHeaderView;
        if (!z2) {
            i = 8;
        }
        view.setVisibility(i);
        this.mBatteryRemainingIcon.setPercentShowMode(3);
        TouchAnimator.Builder builder = new TouchAnimator.Builder();
        builder.addFloat(this.mIconContainer, "alpha", 0.0f, 1.0f);
        builder.addFloat(this.mBatteryRemainingIcon, "alpha", 0.0f, 1.0f);
        this.mIconsAlphaAnimatorFixed = builder.build();
    }

    @Override // android.widget.FrameLayout, android.view.View
    public final void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.mDatePrivacyView.getMeasuredHeight() != this.mTopViewMeasureHeight) {
            this.mTopViewMeasureHeight = this.mDatePrivacyView.getMeasuredHeight();
            post(new LockIconViewController$$ExternalSyntheticLambda2(this, 2));
        }
    }

    @Override // android.view.View
    public final void onRtlPropertiesChanged(int i) {
        super.onRtlPropertiesChanged(i);
        updateResources();
    }
}

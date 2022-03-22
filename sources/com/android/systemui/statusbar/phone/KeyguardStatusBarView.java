package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.DisplayCutout;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowInsets;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.systemui.ScreenDecorations;
import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.util.Utils;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public class KeyguardStatusBarView extends RelativeLayout {
    public boolean mBatteryCharging;
    public BatteryMeterView mBatteryView;
    public TextView mCarrierLabel;
    public View mCutoutSpace;
    public DisplayCutout mDisplayCutout;
    public boolean mIsPrivacyDotEnabled;
    public boolean mIsUserSwitcherEnabled;
    public boolean mKeyguardUserAvatarEnabled;
    public boolean mKeyguardUserSwitcherEnabled;
    public int mMinDotWidth;
    public ImageView mMultiUserAvatar;
    public boolean mShowPercentAvailable;
    public int mStatusBarPaddingEnd;
    public ViewGroup mStatusIconArea;
    public StatusIconContainer mStatusIconContainer;
    public View mSystemIconsContainer;
    public int mSystemIconsSwitcherHiddenExpandedMargin;
    public int mTopClipping;
    public ViewGroup mUserSwitcherContainer;
    public final ArrayList<Rect> mEmptyTintRect = new ArrayList<>();
    public int mLayoutState = 0;
    public int mCutoutSideNudge = 0;
    public Pair<Integer, Integer> mPadding = new Pair<>(0, 0);
    public final Rect mClipRect = new Rect(0, 0, 0, 0);

    @Override // android.view.View
    public final boolean hasOverlappingRendering() {
        return false;
    }

    public final WindowInsets updateWindowInsets(WindowInsets windowInsets, StatusBarContentInsetsProvider statusBarContentInsetsProvider) {
        int i;
        int i2;
        int i3;
        boolean z = false;
        this.mLayoutState = 0;
        this.mDisplayCutout = getRootWindowInsets().getDisplayCutout();
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
        marginLayoutParams.height = Utils.getStatusBarHeaderHeightKeyguard(((RelativeLayout) this).mContext);
        setLayoutParams(marginLayoutParams);
        DisplayCutout displayCutout = this.mDisplayCutout;
        if (displayCutout == null) {
            i = 0;
        } else {
            i = displayCutout.getWaterfallInsets().top;
        }
        this.mPadding = statusBarContentInsetsProvider.getStatusBarContentInsetsForCurrentRotation();
        if (!isLayoutRtl() || !this.mIsPrivacyDotEnabled) {
            i2 = ((Integer) this.mPadding.first).intValue();
        } else {
            i2 = Math.max(this.mMinDotWidth, ((Integer) this.mPadding.first).intValue());
        }
        if (isLayoutRtl() || !this.mIsPrivacyDotEnabled) {
            i3 = ((Integer) this.mPadding.second).intValue();
        } else {
            i3 = Math.max(this.mMinDotWidth, ((Integer) this.mPadding.second).intValue());
        }
        setPadding(i2, i, i3, 0);
        if (this.mDisplayCutout == null || statusBarContentInsetsProvider.currentRotationHasCornerCutout()) {
            z = updateLayoutParamsNoCutout();
        } else if (this.mLayoutState != 1) {
            this.mLayoutState = 1;
            if (this.mCutoutSpace == null) {
                updateLayoutParamsNoCutout();
            }
            Rect rect = new Rect();
            ScreenDecorations.DisplayCutoutView.boundsFromDirection(this.mDisplayCutout, 48, rect);
            this.mCutoutSpace.setVisibility(0);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mCutoutSpace.getLayoutParams();
            int i4 = rect.left;
            int i5 = this.mCutoutSideNudge;
            rect.left = i4 + i5;
            rect.right -= i5;
            layoutParams.width = rect.width();
            layoutParams.height = rect.height();
            layoutParams.addRule(13);
            ((RelativeLayout.LayoutParams) this.mCarrierLabel.getLayoutParams()).addRule(16, 2131427796);
            RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) this.mStatusIconArea.getLayoutParams();
            layoutParams2.addRule(1, 2131427796);
            layoutParams2.width = -1;
            ((LinearLayout.LayoutParams) this.mSystemIconsContainer.getLayoutParams()).setMarginStart(0);
            z = true;
        }
        if (z) {
            requestLayout();
        }
        return super.onApplyWindowInsets(windowInsets);
    }

    public final void onOverlayChanged() {
        int themeAttr = com.android.settingslib.Utils.getThemeAttr(((RelativeLayout) this).mContext, 16842818);
        this.mCarrierLabel.setTextAppearance(themeAttr);
        BatteryMeterView batteryMeterView = this.mBatteryView;
        Objects.requireNonNull(batteryMeterView);
        TextView textView = batteryMeterView.mBatteryPercentView;
        if (textView != null) {
            batteryMeterView.removeView(textView);
            batteryMeterView.mBatteryPercentView = null;
        }
        batteryMeterView.updateShowPercent();
        TextView textView2 = (TextView) this.mUserSwitcherContainer.findViewById(2131427788);
        if (textView2 != null) {
            textView2.setTextAppearance(themeAttr);
        }
    }

    public final void onThemeChanged(StatusBarIconController.TintedIconManager tintedIconManager) {
        int i;
        float f;
        BatteryMeterView batteryMeterView = this.mBatteryView;
        Context context = ((RelativeLayout) this).mContext;
        if (context == null) {
            Objects.requireNonNull(batteryMeterView);
        } else {
            batteryMeterView.mDualToneHandler.setColorsFromContext(context);
        }
        int colorAttrDefaultColor = com.android.settingslib.Utils.getColorAttrDefaultColor(((RelativeLayout) this).mContext, 2130970103);
        Context context2 = ((RelativeLayout) this).mContext;
        if (Color.luminance(colorAttrDefaultColor) < 0.5d) {
            i = 2131099796;
        } else {
            i = 2131099958;
        }
        int colorStateListDefaultColor = com.android.settingslib.Utils.getColorStateListDefaultColor(context2, i);
        if (colorAttrDefaultColor == -1) {
            f = 0.0f;
        } else {
            f = 1.0f;
        }
        this.mCarrierLabel.setTextColor(colorStateListDefaultColor);
        TextView textView = (TextView) this.mUserSwitcherContainer.findViewById(2131427788);
        if (textView != null) {
            textView.setTextColor(com.android.settingslib.Utils.getColorStateListDefaultColor(((RelativeLayout) this).mContext, 2131099958));
        }
        if (tintedIconManager != null) {
            tintedIconManager.setTint(colorStateListDefaultColor);
        }
        ArrayList<Rect> arrayList = this.mEmptyTintRect;
        View findViewById = findViewById(2131427571);
        if (findViewById instanceof DarkIconDispatcher.DarkReceiver) {
            ((DarkIconDispatcher.DarkReceiver) findViewById).onDarkChanged(arrayList, f, colorStateListDefaultColor);
        }
        ArrayList<Rect> arrayList2 = this.mEmptyTintRect;
        View findViewById2 = findViewById(2131427717);
        if (findViewById2 instanceof DarkIconDispatcher.DarkReceiver) {
            ((DarkIconDispatcher.DarkReceiver) findViewById2).onDarkChanged(arrayList2, f, colorStateListDefaultColor);
        }
    }

    public final boolean updateLayoutParamsNoCutout() {
        if (this.mLayoutState == 2) {
            return false;
        }
        this.mLayoutState = 2;
        View view = this.mCutoutSpace;
        if (view != null) {
            view.setVisibility(8);
        }
        ((RelativeLayout.LayoutParams) this.mCarrierLabel.getLayoutParams()).addRule(16, 2131428932);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mStatusIconArea.getLayoutParams();
        layoutParams.removeRule(1);
        layoutParams.width = -2;
        ((LinearLayout.LayoutParams) this.mSystemIconsContainer.getLayoutParams()).setMarginStart(getResources().getDimensionPixelSize(2131167220));
        return true;
    }

    public final void updateSystemIconsLayoutParams() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mSystemIconsContainer.getLayoutParams();
        int dimensionPixelSize = getResources().getDimensionPixelSize(2131167220);
        int i = this.mStatusBarPaddingEnd;
        if (this.mKeyguardUserSwitcherEnabled) {
            i = this.mSystemIconsSwitcherHiddenExpandedMargin;
        }
        if (i != layoutParams.getMarginEnd() || dimensionPixelSize != layoutParams.getMarginStart()) {
            layoutParams.setMarginStart(dimensionPixelSize);
            layoutParams.setMarginEnd(i);
            this.mSystemIconsContainer.setLayoutParams(layoutParams);
        }
    }

    public final void updateVisibilities() {
        if (!this.mKeyguardUserAvatarEnabled) {
            ViewParent parent = this.mMultiUserAvatar.getParent();
            ViewGroup viewGroup = this.mStatusIconArea;
            if (parent == viewGroup) {
                viewGroup.removeView(this.mMultiUserAvatar);
            } else if (this.mMultiUserAvatar.getParent() != null) {
                getOverlay().remove(this.mMultiUserAvatar);
            }
        } else {
            int i = 0;
            if (this.mMultiUserAvatar.getParent() == this.mStatusIconArea || this.mKeyguardUserSwitcherEnabled) {
                ViewParent parent2 = this.mMultiUserAvatar.getParent();
                ViewGroup viewGroup2 = this.mStatusIconArea;
                if (parent2 == viewGroup2 && this.mKeyguardUserSwitcherEnabled) {
                    viewGroup2.removeView(this.mMultiUserAvatar);
                }
            } else {
                if (this.mMultiUserAvatar.getParent() != null) {
                    getOverlay().remove(this.mMultiUserAvatar);
                }
                this.mStatusIconArea.addView(this.mMultiUserAvatar, 0);
            }
            if (!this.mKeyguardUserSwitcherEnabled) {
                if (this.mIsUserSwitcherEnabled) {
                    this.mMultiUserAvatar.setVisibility(0);
                } else {
                    this.mMultiUserAvatar.setVisibility(8);
                }
            }
            BatteryMeterView batteryMeterView = this.mBatteryView;
            if (this.mBatteryCharging && this.mShowPercentAvailable) {
                i = 1;
            }
            Objects.requireNonNull(batteryMeterView);
            batteryMeterView.setPercentShowMode(i);
        }
    }

    public KeyguardStatusBarView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public final void loadDimens() {
        Resources resources = getResources();
        this.mSystemIconsSwitcherHiddenExpandedMargin = resources.getDimensionPixelSize(2131167221);
        this.mStatusBarPaddingEnd = resources.getDimensionPixelSize(2131167070);
        this.mMinDotWidth = resources.getDimensionPixelSize(2131166708);
        this.mCutoutSideNudge = getResources().getDimensionPixelSize(2131165669);
        this.mShowPercentAvailable = getContext().getResources().getBoolean(17891383);
        resources.getDimensionPixelSize(2131166936);
    }

    @Override // android.view.View
    public final void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        loadDimens();
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mMultiUserAvatar.getLayoutParams();
        int dimensionPixelSize = getResources().getDimensionPixelSize(2131166581);
        marginLayoutParams.height = dimensionPixelSize;
        marginLayoutParams.width = dimensionPixelSize;
        this.mMultiUserAvatar.setLayoutParams(marginLayoutParams);
        updateSystemIconsLayoutParams();
        ViewGroup viewGroup = this.mStatusIconArea;
        viewGroup.setPaddingRelative(viewGroup.getPaddingStart(), getResources().getDimensionPixelSize(2131167072), this.mStatusIconArea.getPaddingEnd(), this.mStatusIconArea.getPaddingBottom());
        StatusIconContainer statusIconContainer = this.mStatusIconContainer;
        statusIconContainer.setPaddingRelative(statusIconContainer.getPaddingStart(), this.mStatusIconContainer.getPaddingTop(), getResources().getDimensionPixelSize(2131167015), this.mStatusIconContainer.getPaddingBottom());
        int i = 0;
        this.mCarrierLabel.setTextSize(0, getResources().getDimensionPixelSize(17105581));
        ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) this.mCarrierLabel.getLayoutParams();
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(2131165840);
        int intValue = ((Integer) this.mPadding.first).intValue();
        if (intValue < dimensionPixelSize2) {
            i = dimensionPixelSize2 - intValue;
        }
        marginLayoutParams2.setMarginStart(i);
        this.mCarrierLabel.setLayoutParams(marginLayoutParams2);
        ViewGroup.MarginLayoutParams marginLayoutParams3 = (ViewGroup.MarginLayoutParams) getLayoutParams();
        marginLayoutParams3.height = Utils.getStatusBarHeaderHeightKeyguard(((RelativeLayout) this).mContext);
        setLayoutParams(marginLayoutParams3);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mSystemIconsContainer = findViewById(2131429003);
        this.mMultiUserAvatar = (ImageView) findViewById(2131428472);
        this.mCarrierLabel = (TextView) findViewById(2131428169);
        this.mBatteryView = (BatteryMeterView) this.mSystemIconsContainer.findViewById(2131427571);
        this.mCutoutSpace = findViewById(2131427796);
        this.mStatusIconArea = (ViewGroup) findViewById(2131428932);
        this.mStatusIconContainer = (StatusIconContainer) findViewById(2131428922);
        this.mUserSwitcherContainer = (ViewGroup) findViewById(2131429173);
        this.mIsPrivacyDotEnabled = ((RelativeLayout) this).mContext.getResources().getBoolean(2131034133);
        loadDimens();
    }

    @Override // android.widget.RelativeLayout, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.mClipRect.set(0, this.mTopClipping, getWidth(), getHeight());
        setClipBounds(this.mClipRect);
    }

    @Override // android.view.View
    public final void setVisibility(int i) {
        super.setVisibility(i);
        if (i != 0) {
            this.mSystemIconsContainer.animate().cancel();
            this.mSystemIconsContainer.setTranslationX(0.0f);
            this.mMultiUserAvatar.animate().cancel();
            this.mMultiUserAvatar.setAlpha(1.0f);
            return;
        }
        updateVisibilities();
        updateSystemIconsLayoutParams();
    }

    public boolean isKeyguardUserAvatarEnabled() {
        return this.mKeyguardUserAvatarEnabled;
    }
}

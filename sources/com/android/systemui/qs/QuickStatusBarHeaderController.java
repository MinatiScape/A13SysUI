package com.android.systemui.qs;

import android.os.Bundle;
import com.android.internal.colorextraction.ColorExtractor;
import com.android.settingslib.Utils;
import com.android.systemui.battery.BatteryMeterViewController;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.demomode.DemoMode;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.qs.QSPanelControllerBase;
import com.android.systemui.qs.carrier.QSCarrierGroup;
import com.android.systemui.qs.carrier.QSCarrierGroupController;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.phone.StatusIconContainer;
import com.android.systemui.statusbar.policy.Clock;
import com.android.systemui.statusbar.policy.VariableDateView;
import com.android.systemui.statusbar.policy.VariableDateViewController;
import com.android.systemui.util.ViewController;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class QuickStatusBarHeaderController extends ViewController<QuickStatusBarHeader> implements ChipVisibilityListener {
    public final BatteryMeterViewController mBatteryMeterViewController;
    public final Clock mClockView;
    public SysuiColorExtractor mColorExtractor;
    public final DemoModeController mDemoModeController;
    public final ClockDemoModeReceiver mDemoModeReceiver;
    public final FeatureFlags mFeatureFlags;
    public final StatusIconContainer mIconContainer;
    public final StatusBarIconController.TintedIconManager mIconManager;
    public final StatusBarContentInsetsProvider mInsetsProvider;
    public boolean mListening;
    public QuickStatusBarHeaderController$$ExternalSyntheticLambda0 mOnColorsChangedListener;
    public final HeaderPrivacyIconsController mPrivacyIconsController;
    public final QSCarrierGroupController mQSCarrierGroupController;
    public final QSExpansionPathInterpolator mQSExpansionPathInterpolator;
    public final QuickQSPanelController mQuickQSPanelController;
    public final StatusBarIconController mStatusBarIconController;
    public final VariableDateViewController mVariableDateViewControllerClockDateView;
    public final VariableDateViewController mVariableDateViewControllerDateView;

    /* loaded from: classes.dex */
    public static class ClockDemoModeReceiver implements DemoMode {
        public Clock mClockView;

        @Override // com.android.systemui.demomode.DemoMode
        public final List<String> demoCommands() {
            return List.of("clock");
        }

        @Override // com.android.systemui.demomode.DemoModeCommandReceiver
        public final void dispatchDemoCommand(String str, Bundle bundle) {
            this.mClockView.dispatchDemoCommand(str, bundle);
        }

        @Override // com.android.systemui.demomode.DemoModeCommandReceiver
        public final void onDemoModeFinished() {
            this.mClockView.onDemoModeFinished();
        }

        @Override // com.android.systemui.demomode.DemoModeCommandReceiver
        public final void onDemoModeStarted() {
            Clock clock = this.mClockView;
            Objects.requireNonNull(clock);
            clock.mDemoMode = true;
        }

        public ClockDemoModeReceiver(Clock clock) {
            this.mClockView = clock;
        }
    }

    public QuickStatusBarHeaderController(QuickStatusBarHeader quickStatusBarHeader, HeaderPrivacyIconsController headerPrivacyIconsController, StatusBarIconController statusBarIconController, DemoModeController demoModeController, QuickQSPanelController quickQSPanelController, QSCarrierGroupController.Builder builder, SysuiColorExtractor sysuiColorExtractor, QSExpansionPathInterpolator qSExpansionPathInterpolator, FeatureFlags featureFlags, VariableDateViewController.Factory factory, BatteryMeterViewController batteryMeterViewController, StatusBarContentInsetsProvider statusBarContentInsetsProvider) {
        super(quickStatusBarHeader);
        this.mPrivacyIconsController = headerPrivacyIconsController;
        this.mStatusBarIconController = statusBarIconController;
        this.mDemoModeController = demoModeController;
        this.mQuickQSPanelController = quickQSPanelController;
        this.mQSExpansionPathInterpolator = qSExpansionPathInterpolator;
        this.mFeatureFlags = featureFlags;
        this.mBatteryMeterViewController = batteryMeterViewController;
        this.mInsetsProvider = statusBarContentInsetsProvider;
        QSCarrierGroup qSCarrierGroup = (QSCarrierGroup) quickStatusBarHeader.findViewById(2131427673);
        Objects.requireNonNull(builder);
        builder.mView = qSCarrierGroup;
        this.mQSCarrierGroupController = new QSCarrierGroupController(qSCarrierGroup, builder.mActivityStarter, builder.mHandler, builder.mLooper, builder.mNetworkController, builder.mCarrierTextControllerBuilder, builder.mContext, builder.mCarrierConfigTracker, builder.mFeatureFlags, builder.mSlotIndexResolver);
        Clock clock = (Clock) quickStatusBarHeader.findViewById(2131427717);
        this.mClockView = clock;
        StatusIconContainer statusIconContainer = (StatusIconContainer) quickStatusBarHeader.findViewById(2131428922);
        this.mIconContainer = statusIconContainer;
        Objects.requireNonNull(factory);
        this.mVariableDateViewControllerDateView = new VariableDateViewController(factory.systemClock, factory.broadcastDispatcher, factory.handler, (VariableDateView) quickStatusBarHeader.requireViewById(2131427797));
        this.mVariableDateViewControllerClockDateView = new VariableDateViewController(factory.systemClock, factory.broadcastDispatcher, factory.handler, (VariableDateView) quickStatusBarHeader.requireViewById(2131427798));
        this.mIconManager = new StatusBarIconController.TintedIconManager(statusIconContainer, featureFlags);
        this.mDemoModeReceiver = new ClockDemoModeReceiver(clock);
        this.mColorExtractor = sysuiColorExtractor;
        ColorExtractor.OnColorsChangedListener quickStatusBarHeaderController$$ExternalSyntheticLambda0 = new ColorExtractor.OnColorsChangedListener() { // from class: com.android.systemui.qs.QuickStatusBarHeaderController$$ExternalSyntheticLambda0
            public final void onColorsChanged(ColorExtractor colorExtractor, int i) {
                ColorExtractor.GradientColors gradientColors;
                QuickStatusBarHeaderController quickStatusBarHeaderController = QuickStatusBarHeaderController.this;
                Objects.requireNonNull(quickStatusBarHeaderController);
                SysuiColorExtractor sysuiColorExtractor2 = quickStatusBarHeaderController.mColorExtractor;
                Objects.requireNonNull(sysuiColorExtractor2);
                if (sysuiColorExtractor2.mHasMediaArtwork) {
                    gradientColors = sysuiColorExtractor2.mBackdropColors;
                } else {
                    gradientColors = sysuiColorExtractor2.mNeutralColorsLock;
                }
                quickStatusBarHeaderController.mClockView.onColorsChanged(gradientColors.supportsDarkText());
            }
        };
        this.mOnColorsChangedListener = quickStatusBarHeaderController$$ExternalSyntheticLambda0;
        sysuiColorExtractor.addOnColorsChangedListener(quickStatusBarHeaderController$$ExternalSyntheticLambda0);
        Objects.requireNonNull(batteryMeterViewController);
        batteryMeterViewController.mIgnoreTunerUpdates = true;
        if (batteryMeterViewController.mIsSubscribedForTunerUpdates) {
            batteryMeterViewController.mTunerService.removeTunable(batteryMeterViewController.mTunable);
            batteryMeterViewController.mIsSubscribedForTunerUpdates = false;
        }
    }

    @Override // com.android.systemui.qs.ChipVisibilityListener
    public final void onChipVisibilityRefreshed(boolean z) {
        QuickStatusBarHeader quickStatusBarHeader = (QuickStatusBarHeader) this.mView;
        Objects.requireNonNull(quickStatusBarHeader);
        if (z) {
            TouchAnimator touchAnimator = quickStatusBarHeader.mIconsAlphaAnimatorFixed;
            quickStatusBarHeader.mIconsAlphaAnimator = touchAnimator;
            touchAnimator.setPosition(quickStatusBarHeader.mKeyguardExpansionFraction);
            return;
        }
        quickStatusBarHeader.mIconsAlphaAnimator = null;
        quickStatusBarHeader.mIconContainer.setAlpha(1.0f);
        quickStatusBarHeader.mBatteryRemainingIcon.setAlpha(1.0f);
    }

    @Override // com.android.systemui.util.ViewController
    public final void onInit() {
        this.mBatteryMeterViewController.init();
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewAttached() {
        boolean z;
        List<String> list;
        this.mPrivacyIconsController.onParentVisible();
        HeaderPrivacyIconsController headerPrivacyIconsController = this.mPrivacyIconsController;
        Objects.requireNonNull(headerPrivacyIconsController);
        headerPrivacyIconsController.chipVisibilityListener = this;
        StatusIconContainer statusIconContainer = this.mIconContainer;
        String string = getResources().getString(17041549);
        Objects.requireNonNull(statusIconContainer);
        if (statusIconContainer.mIgnoredSlots.contains(string)) {
            z = false;
        } else {
            statusIconContainer.mIgnoredSlots.add(string);
            z = true;
        }
        if (z) {
            statusIconContainer.requestLayout();
        }
        StatusIconContainer statusIconContainer2 = this.mIconContainer;
        Objects.requireNonNull(statusIconContainer2);
        statusIconContainer2.mShouldRestrictIcons = false;
        this.mStatusBarIconController.addIconGroup(this.mIconManager);
        QuickStatusBarHeader quickStatusBarHeader = (QuickStatusBarHeader) this.mView;
        QSCarrierGroupController qSCarrierGroupController = this.mQSCarrierGroupController;
        Objects.requireNonNull(qSCarrierGroupController);
        boolean z2 = qSCarrierGroupController.mIsSingleCarrier;
        Objects.requireNonNull(quickStatusBarHeader);
        quickStatusBarHeader.mIsSingleCarrier = z2;
        quickStatusBarHeader.updateAlphaAnimator();
        QSCarrierGroupController qSCarrierGroupController2 = this.mQSCarrierGroupController;
        final QuickStatusBarHeader quickStatusBarHeader2 = (QuickStatusBarHeader) this.mView;
        Objects.requireNonNull(quickStatusBarHeader2);
        QSCarrierGroupController.OnSingleCarrierChangedListener quickStatusBarHeaderController$$ExternalSyntheticLambda1 = new QSCarrierGroupController.OnSingleCarrierChangedListener() { // from class: com.android.systemui.qs.QuickStatusBarHeaderController$$ExternalSyntheticLambda1
            @Override // com.android.systemui.qs.carrier.QSCarrierGroupController.OnSingleCarrierChangedListener
            public final void onSingleCarrierChanged(boolean z3) {
                QuickStatusBarHeader quickStatusBarHeader3 = QuickStatusBarHeader.this;
                Objects.requireNonNull(quickStatusBarHeader3);
                quickStatusBarHeader3.mIsSingleCarrier = z3;
                quickStatusBarHeader3.updateAlphaAnimator();
            }
        };
        Objects.requireNonNull(qSCarrierGroupController2);
        qSCarrierGroupController2.mOnSingleCarrierChangedListener = quickStatusBarHeaderController$$ExternalSyntheticLambda1;
        if (this.mFeatureFlags.isEnabled(Flags.COMBINED_STATUS_BAR_SIGNAL_ICONS)) {
            list = List.of(getResources().getString(17041554), getResources().getString(17041537));
        } else {
            list = List.of(getResources().getString(17041551));
        }
        QuickStatusBarHeader quickStatusBarHeader3 = (QuickStatusBarHeader) this.mView;
        StatusBarIconController.TintedIconManager tintedIconManager = this.mIconManager;
        QSExpansionPathInterpolator qSExpansionPathInterpolator = this.mQSExpansionPathInterpolator;
        StatusBarContentInsetsProvider statusBarContentInsetsProvider = this.mInsetsProvider;
        boolean isEnabled = this.mFeatureFlags.isEnabled(Flags.COMBINED_QS_HEADERS);
        Objects.requireNonNull(quickStatusBarHeader3);
        quickStatusBarHeader3.mUseCombinedQSHeader = isEnabled;
        quickStatusBarHeader3.mTintedIconManager = tintedIconManager;
        quickStatusBarHeader3.mRssiIgnoredSlots = list;
        quickStatusBarHeader3.mInsetsProvider = statusBarContentInsetsProvider;
        tintedIconManager.setTint(Utils.getColorAttrDefaultColor(quickStatusBarHeader3.getContext(), 16842806));
        quickStatusBarHeader3.mQSExpansionPathInterpolator = qSExpansionPathInterpolator;
        quickStatusBarHeader3.updateAnimators();
        this.mDemoModeController.addCallback((DemoMode) this.mDemoModeReceiver);
        this.mVariableDateViewControllerDateView.init();
        this.mVariableDateViewControllerClockDateView.init();
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewDetached() {
        this.mColorExtractor.removeOnColorsChangedListener(this.mOnColorsChangedListener);
        HeaderPrivacyIconsController headerPrivacyIconsController = this.mPrivacyIconsController;
        Objects.requireNonNull(headerPrivacyIconsController);
        headerPrivacyIconsController.chipVisibilityListener = null;
        headerPrivacyIconsController.privacyChip.setOnClickListener(null);
        this.mStatusBarIconController.removeIconGroup(this.mIconManager);
        QSCarrierGroupController qSCarrierGroupController = this.mQSCarrierGroupController;
        Objects.requireNonNull(qSCarrierGroupController);
        qSCarrierGroupController.mOnSingleCarrierChangedListener = null;
        this.mDemoModeController.removeCallback((DemoMode) this.mDemoModeReceiver);
        setListening(false);
    }

    public final void setListening(boolean z) {
        this.mQSCarrierGroupController.setListening(z);
        if (z != this.mListening) {
            this.mListening = z;
            this.mQuickQSPanelController.setListening(z);
            QuickQSPanelController quickQSPanelController = this.mQuickQSPanelController;
            Objects.requireNonNull(quickQSPanelController);
            QuickQSPanel quickQSPanel = (QuickQSPanel) quickQSPanelController.mView;
            Objects.requireNonNull(quickQSPanel);
            if (quickQSPanel.mListening) {
                QuickQSPanelController quickQSPanelController2 = this.mQuickQSPanelController;
                Objects.requireNonNull(quickQSPanelController2);
                Iterator<QSPanelControllerBase.TileRecord> it = quickQSPanelController2.mRecords.iterator();
                while (it.hasNext()) {
                    it.next().tile.refreshState();
                }
            }
            if (this.mQuickQSPanelController.switchTileLayout(false)) {
                ((QuickStatusBarHeader) this.mView).updateResources();
            }
            if (z) {
                this.mPrivacyIconsController.startListening();
                return;
            }
            HeaderPrivacyIconsController headerPrivacyIconsController = this.mPrivacyIconsController;
            Objects.requireNonNull(headerPrivacyIconsController);
            headerPrivacyIconsController.listening = false;
            headerPrivacyIconsController.privacyItemController.removeCallback(headerPrivacyIconsController.picCallback);
            headerPrivacyIconsController.privacyChipLogged = false;
        }
    }
}

package com.android.systemui.statusbar.phone.fragment;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.LinearLayout;
import com.android.keyguard.KeyguardClockSwitch;
import com.android.keyguard.KeyguardClockSwitchController;
import com.android.keyguard.KeyguardStatusViewController;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.OperatorNameView;
import com.android.systemui.statusbar.OperatorNameViewController;
import com.android.systemui.statusbar.connectivity.IconState;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.statusbar.connectivity.NetworkControllerImpl$$ExternalSyntheticLambda1;
import com.android.systemui.statusbar.connectivity.SignalCallback;
import com.android.systemui.statusbar.events.SystemStatusAnimationCallback;
import com.android.systemui.statusbar.events.SystemStatusAnimationScheduler;
import com.android.systemui.statusbar.phone.NotificationIconAreaController;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.PhoneStatusBarView;
import com.android.systemui.statusbar.phone.StatusBarHideIconsForBouncerManager;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.phone.StatusBarLocationPublisher;
import com.android.systemui.statusbar.phone.StatusBarMarginUpdatedListener;
import com.android.systemui.statusbar.phone.fragment.dagger.StatusBarFragmentComponent;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallListener;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallLogger;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.policy.EncryptionHelper;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import kotlin.collections.CollectionsKt___CollectionsKt;
@SuppressLint({"ValidFragment"})
/* loaded from: classes.dex */
public final class CollapsedStatusBarFragment extends Fragment implements CommandQueue.Callbacks, StatusBarStateController.StateListener, SystemStatusAnimationCallback {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final SystemStatusAnimationScheduler mAnimationScheduler;
    public View mClockView;
    public final CollapsedStatusBarFragmentLogger mCollapsedStatusBarFragmentLogger;
    public final CommandQueue mCommandQueue;
    public StatusBarIconController.DarkIconManager mDarkIconManager;
    public int mDisabled1;
    public int mDisabled2;
    public final FeatureFlags mFeatureFlags;
    public final KeyguardStateController mKeyguardStateController;
    public final StatusBarLocationPublisher mLocationPublisher;
    public final NetworkController mNetworkController;
    public final NotificationIconAreaController mNotificationIconAreaController;
    public View mNotificationIconAreaInner;
    public final NotificationPanelViewController mNotificationPanelViewController;
    public View mOngoingCallChip;
    public final OngoingCallController mOngoingCallController;
    public OperatorNameViewController mOperatorNameViewController;
    public final OperatorNameViewController.Factory mOperatorNameViewControllerFactory;
    public final PanelExpansionStateManager mPanelExpansionStateManager;
    public PhoneStatusBarView mStatusBar;
    public StatusBarFragmentComponent mStatusBarFragmentComponent;
    public final StatusBarFragmentComponent.Factory mStatusBarFragmentComponentFactory;
    public final StatusBarHideIconsForBouncerManager mStatusBarHideIconsForBouncerManager;
    public final StatusBarIconController mStatusBarIconController;
    public final StatusBarStateController mStatusBarStateController;
    public LinearLayout mSystemIconArea;
    public ArrayList mBlockedIcons = new ArrayList();
    public AnonymousClass1 mSignalCallback = new SignalCallback() { // from class: com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragment.1
        @Override // com.android.systemui.statusbar.connectivity.SignalCallback
        public final void setIsAirplaneMode(IconState iconState) {
            CollapsedStatusBarFragment collapsedStatusBarFragment = CollapsedStatusBarFragment.this;
            collapsedStatusBarFragment.mCommandQueue.recomputeDisableFlags(collapsedStatusBarFragment.getContext().getDisplayId(), true);
        }
    };
    public final AnonymousClass2 mOngoingCallListener = new OngoingCallListener() { // from class: com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragment.2
        @Override // com.android.systemui.statusbar.phone.ongoingcall.OngoingCallListener
        public final void onOngoingCallStateChanged() {
            CollapsedStatusBarFragment collapsedStatusBarFragment = CollapsedStatusBarFragment.this;
            int displayId = collapsedStatusBarFragment.getContext().getDisplayId();
            CollapsedStatusBarFragment collapsedStatusBarFragment2 = CollapsedStatusBarFragment.this;
            collapsedStatusBarFragment.disable(displayId, collapsedStatusBarFragment2.mDisabled1, collapsedStatusBarFragment2.mDisabled2, true);
        }
    };
    public CollapsedStatusBarFragment$$ExternalSyntheticLambda0 mStatusBarLayoutListener = new View.OnLayoutChangeListener() { // from class: com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragment$$ExternalSyntheticLambda0
        @Override // android.view.View.OnLayoutChangeListener
        public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            CollapsedStatusBarFragment collapsedStatusBarFragment = CollapsedStatusBarFragment.this;
            if (i == i5 && i3 == i7) {
                Objects.requireNonNull(collapsedStatusBarFragment);
            } else {
                collapsedStatusBarFragment.updateStatusBarLocation(i, i3);
            }
        }
    };

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragment$1] */
    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragment$2] */
    /* JADX WARN: Type inference failed for: r1v3, types: [com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragment$$ExternalSyntheticLambda0] */
    @SuppressLint({"ValidFragment"})
    public CollapsedStatusBarFragment(StatusBarFragmentComponent.Factory factory, OngoingCallController ongoingCallController, SystemStatusAnimationScheduler systemStatusAnimationScheduler, StatusBarLocationPublisher statusBarLocationPublisher, NotificationIconAreaController notificationIconAreaController, PanelExpansionStateManager panelExpansionStateManager, FeatureFlags featureFlags, StatusBarIconController statusBarIconController, StatusBarHideIconsForBouncerManager statusBarHideIconsForBouncerManager, KeyguardStateController keyguardStateController, NotificationPanelViewController notificationPanelViewController, NetworkController networkController, StatusBarStateController statusBarStateController, CommandQueue commandQueue, CollapsedStatusBarFragmentLogger collapsedStatusBarFragmentLogger, OperatorNameViewController.Factory factory2) {
        this.mStatusBarFragmentComponentFactory = factory;
        this.mOngoingCallController = ongoingCallController;
        this.mAnimationScheduler = systemStatusAnimationScheduler;
        this.mLocationPublisher = statusBarLocationPublisher;
        this.mNotificationIconAreaController = notificationIconAreaController;
        this.mPanelExpansionStateManager = panelExpansionStateManager;
        this.mFeatureFlags = featureFlags;
        this.mStatusBarIconController = statusBarIconController;
        this.mStatusBarHideIconsForBouncerManager = statusBarHideIconsForBouncerManager;
        this.mKeyguardStateController = keyguardStateController;
        this.mNotificationPanelViewController = notificationPanelViewController;
        this.mNetworkController = networkController;
        this.mStatusBarStateController = statusBarStateController;
        this.mCommandQueue = commandQueue;
        this.mCollapsedStatusBarFragmentLogger = collapsedStatusBarFragmentLogger;
        this.mOperatorNameViewControllerFactory = factory2;
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public final void onStateChanged(int i) {
    }

    public final int clockHiddenMode() {
        boolean z;
        PanelExpansionStateManager panelExpansionStateManager = this.mPanelExpansionStateManager;
        Objects.requireNonNull(panelExpansionStateManager);
        if (panelExpansionStateManager.state == 0) {
            z = true;
        } else {
            z = false;
        }
        if (z || this.mKeyguardStateController.isShowing() || this.mStatusBarStateController.isDozing()) {
            return 8;
        }
        return 4;
    }

    @Override // com.android.systemui.statusbar.events.SystemStatusAnimationCallback
    public final void onSystemChromeAnimationEnd() {
        SystemStatusAnimationScheduler systemStatusAnimationScheduler = this.mAnimationScheduler;
        Objects.requireNonNull(systemStatusAnimationScheduler);
        boolean z = true;
        if (systemStatusAnimationScheduler.animationState == 1) {
            this.mSystemIconArea.setVisibility(4);
            this.mSystemIconArea.setAlpha(0.0f);
            return;
        }
        if ((this.mDisabled1 & 1048576) == 0 && (this.mDisabled2 & 2) == 0) {
            z = false;
        }
        if (!z) {
            this.mSystemIconArea.setAlpha(1.0f);
            this.mSystemIconArea.setVisibility(0);
        }
    }

    @Override // com.android.systemui.statusbar.events.SystemStatusAnimationCallback
    public final void onSystemChromeAnimationStart() {
        boolean z;
        SystemStatusAnimationScheduler systemStatusAnimationScheduler = this.mAnimationScheduler;
        Objects.requireNonNull(systemStatusAnimationScheduler);
        if (systemStatusAnimationScheduler.animationState == 3) {
            if ((this.mDisabled1 & 1048576) == 0 && (this.mDisabled2 & 2) == 0) {
                z = false;
            } else {
                z = true;
            }
            if (!z) {
                this.mSystemIconArea.setVisibility(0);
                this.mSystemIconArea.setAlpha(0.0f);
            }
        }
    }

    @Override // com.android.systemui.statusbar.events.SystemStatusAnimationCallback
    public final void onSystemChromeAnimationUpdate(ValueAnimator valueAnimator) {
        this.mSystemIconArea.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    public final void updateNotificationIconAreaAndCallChip(int i, boolean z) {
        boolean z2;
        boolean z3;
        boolean z4 = true;
        if ((131072 & i) != 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        if ((i & 67108864) == 0) {
            z3 = true;
        } else {
            z3 = false;
        }
        if (z2 || z3) {
            animateHiddenState(this.mNotificationIconAreaInner, 4, z);
        } else {
            animateShow(this.mNotificationIconAreaInner, z);
        }
        if (!z3 || z2) {
            z4 = false;
        }
        if (z4) {
            animateShow(this.mOngoingCallChip, z);
        } else {
            animateHiddenState(this.mOngoingCallChip, 8, z);
        }
        OngoingCallController ongoingCallController = this.mOngoingCallController;
        Objects.requireNonNull(ongoingCallController);
        OngoingCallLogger ongoingCallLogger = ongoingCallController.logger;
        Objects.requireNonNull(ongoingCallLogger);
        if (z4 && z4 != ongoingCallLogger.chipIsVisible) {
            ongoingCallLogger.logger.log(OngoingCallLogger.OngoingCallEvents.ONGOING_CALL_VISIBLE);
        }
        ongoingCallLogger.chipIsVisible = z4;
    }

    public final void updateStatusBarLocation(int i, int i2) {
        List<WeakReference> list;
        int left = i - this.mStatusBar.getLeft();
        StatusBarLocationPublisher statusBarLocationPublisher = this.mLocationPublisher;
        Objects.requireNonNull(statusBarLocationPublisher);
        statusBarLocationPublisher.marginLeft = left;
        statusBarLocationPublisher.marginRight = this.mStatusBar.getRight() - i2;
        synchronized (statusBarLocationPublisher) {
            list = CollectionsKt___CollectionsKt.toList(statusBarLocationPublisher.listeners);
        }
        for (WeakReference weakReference : list) {
            if (weakReference.get() == null) {
                statusBarLocationPublisher.listeners.remove(weakReference);
            }
            StatusBarMarginUpdatedListener statusBarMarginUpdatedListener = (StatusBarMarginUpdatedListener) weakReference.get();
            if (statusBarMarginUpdatedListener != null) {
                statusBarMarginUpdatedListener.onStatusBarMarginUpdated();
            }
        }
    }

    public static void animateHiddenState(View view, int i, boolean z) {
        view.animate().cancel();
        if (!z) {
            view.setAlpha(0.0f);
            view.setVisibility(i);
            return;
        }
        view.animate().alpha(0.0f).setDuration(160L).setStartDelay(0L).setInterpolator(Interpolators.ALPHA_OUT).withEndAction(new NetworkControllerImpl$$ExternalSyntheticLambda1(view, i, 1));
    }

    public final void animateShow(View view, boolean z) {
        view.animate().cancel();
        view.setVisibility(0);
        if (!z) {
            view.setAlpha(1.0f);
            return;
        }
        view.animate().alpha(1.0f).setDuration(320L).setInterpolator(Interpolators.ALPHA_IN).setStartDelay(50L).withEndAction(null);
        if (this.mKeyguardStateController.isKeyguardFadingAway()) {
            view.animate().setDuration(this.mKeyguardStateController.getKeyguardFadingAwayDuration()).setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN).setStartDelay(this.mKeyguardStateController.getKeyguardFadingAwayDelay()).start();
        }
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public final void disable(int i, int i2, int i3, boolean z) {
        int i4;
        int i5;
        boolean z2;
        boolean z3;
        if (i == getContext().getDisplayId()) {
            boolean shouldBeVisible = this.mStatusBarFragmentComponent.getHeadsUpAppearanceController().shouldBeVisible();
            if (shouldBeVisible) {
                i4 = i2 | 8388608;
            } else {
                i4 = i2;
            }
            boolean z4 = true;
            if (!this.mKeyguardStateController.isLaunchTransitionFadingAway() && !this.mKeyguardStateController.isKeyguardFadingAway()) {
                PanelExpansionStateManager panelExpansionStateManager = this.mPanelExpansionStateManager;
                Objects.requireNonNull(panelExpansionStateManager);
                if (panelExpansionStateManager.state == 0) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (z2 || !this.mNotificationPanelViewController.hideStatusBarIconsWhenExpanded()) {
                    StatusBarHideIconsForBouncerManager statusBarHideIconsForBouncerManager = this.mStatusBarHideIconsForBouncerManager;
                    Objects.requireNonNull(statusBarHideIconsForBouncerManager);
                    if (!statusBarHideIconsForBouncerManager.hideIconsForBouncer && !statusBarHideIconsForBouncerManager.wereIconsJustHidden) {
                        z3 = false;
                        if (z3 && (this.mStatusBarStateController.getState() != 1 || !shouldBeVisible)) {
                            i4 = i4 | 131072 | 1048576 | 8388608;
                        }
                    }
                }
                z3 = true;
                if (z3) {
                    i4 = i4 | 131072 | 1048576 | 8388608;
                }
            }
            NetworkController networkController = this.mNetworkController;
            if (networkController != null && EncryptionHelper.IS_DATA_ENCRYPTED) {
                if (networkController.hasEmergencyCryptKeeperText()) {
                    i4 |= 131072;
                }
                if (!this.mNetworkController.isRadioOn()) {
                    i4 |= 1048576;
                }
            }
            if (this.mStatusBarStateController.isDozing()) {
                NotificationPanelViewController notificationPanelViewController = this.mNotificationPanelViewController;
                Objects.requireNonNull(notificationPanelViewController);
                KeyguardStatusViewController keyguardStatusViewController = notificationPanelViewController.mKeyguardStatusViewController;
                Objects.requireNonNull(keyguardStatusViewController);
                KeyguardClockSwitchController keyguardClockSwitchController = keyguardStatusViewController.mKeyguardClockSwitchController;
                Objects.requireNonNull(keyguardClockSwitchController);
                KeyguardClockSwitch keyguardClockSwitch = (KeyguardClockSwitch) keyguardClockSwitchController.mView;
                Objects.requireNonNull(keyguardClockSwitch);
                if (keyguardClockSwitch.mClockPlugin == null) {
                    z4 = false;
                }
                if (z4) {
                    i4 |= 9437184;
                }
            }
            if (this.mOngoingCallController.hasOngoingCall()) {
                i5 = (-67108865) & i4;
            } else {
                i5 = i4 | 67108864;
            }
            CollapsedStatusBarFragmentLogger collapsedStatusBarFragmentLogger = this.mCollapsedStatusBarFragmentLogger;
            Objects.requireNonNull(collapsedStatusBarFragmentLogger);
            LogBuffer logBuffer = collapsedStatusBarFragmentLogger.buffer;
            LogLevel logLevel = LogLevel.INFO;
            CollapsedStatusBarFragmentLogger$logDisableFlagChange$2 collapsedStatusBarFragmentLogger$logDisableFlagChange$2 = new CollapsedStatusBarFragmentLogger$logDisableFlagChange$2(collapsedStatusBarFragmentLogger);
            Objects.requireNonNull(logBuffer);
            if (!logBuffer.frozen) {
                LogMessageImpl obtain = logBuffer.obtain("CollapsedSbFragment", logLevel, collapsedStatusBarFragmentLogger$logDisableFlagChange$2);
                obtain.int1 = i2;
                obtain.int2 = i3;
                obtain.long1 = i5;
                obtain.long2 = i3;
                logBuffer.push(obtain);
            }
            int i6 = this.mDisabled1 ^ i5;
            int i7 = this.mDisabled2 ^ i3;
            this.mDisabled1 = i5;
            this.mDisabled2 = i3;
            if (!((i6 & 1048576) == 0 && (i7 & 2) == 0)) {
                if ((i5 & 1048576) == 0 && (i3 & 2) == 0) {
                    SystemStatusAnimationScheduler systemStatusAnimationScheduler = this.mAnimationScheduler;
                    Objects.requireNonNull(systemStatusAnimationScheduler);
                    int i8 = systemStatusAnimationScheduler.animationState;
                    if (i8 == 0 || i8 == 4) {
                        animateShow(this.mSystemIconArea, z);
                    }
                    OperatorNameViewController operatorNameViewController = this.mOperatorNameViewController;
                    if (operatorNameViewController != null) {
                        animateShow(operatorNameViewController.mView, z);
                    }
                } else {
                    animateHiddenState(this.mSystemIconArea, 4, z);
                    OperatorNameViewController operatorNameViewController2 = this.mOperatorNameViewController;
                    if (operatorNameViewController2 != null) {
                        animateHiddenState(operatorNameViewController2.mView, 4, z);
                    }
                }
            }
            if (!((i6 & 67108864) == 0 && (i6 & 131072) == 0)) {
                updateNotificationIconAreaAndCallChip(i5, z);
            }
            if ((i6 & 8388608) != 0 || this.mClockView.getVisibility() != clockHiddenMode()) {
                if ((i5 & 8388608) != 0) {
                    animateHiddenState(this.mClockView, clockHiddenMode(), z);
                } else {
                    animateShow(this.mClockView, z);
                }
            }
        }
    }

    @Override // android.app.Fragment
    public final View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(2131624519, viewGroup, false);
    }

    @Override // android.app.Fragment
    public final void onDestroyView() {
        super.onDestroyView();
        this.mStatusBarIconController.removeIconGroup(this.mDarkIconManager);
        this.mAnimationScheduler.removeCallback((SystemStatusAnimationCallback) this);
        if (this.mNetworkController.hasEmergencyCryptKeeperText()) {
            this.mNetworkController.removeCallback(this.mSignalCallback);
        }
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public final void onDozingChanged(boolean z) {
        disable(getContext().getDisplayId(), this.mDisabled1, this.mDisabled2, false);
    }

    @Override // android.app.Fragment
    public final void onPause() {
        super.onPause();
        this.mCommandQueue.removeCallback((CommandQueue.Callbacks) this);
        this.mStatusBarStateController.removeCallback(this);
        OngoingCallController ongoingCallController = this.mOngoingCallController;
        AnonymousClass2 r2 = this.mOngoingCallListener;
        Objects.requireNonNull(ongoingCallController);
        synchronized (ongoingCallController.mListeners) {
            ongoingCallController.mListeners.remove(r2);
        }
    }

    @Override // android.app.Fragment
    public final void onResume() {
        super.onResume();
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) this);
        this.mStatusBarStateController.addCallback(this);
        this.mOngoingCallController.addCallback((OngoingCallListener) this.mOngoingCallListener);
        OngoingCallController ongoingCallController = this.mOngoingCallController;
        View view = this.mOngoingCallChip;
        Objects.requireNonNull(ongoingCallController);
        ongoingCallController.tearDownChipView();
        ongoingCallController.chipView = view;
        if (ongoingCallController.hasOngoingCall()) {
            ongoingCallController.updateChip();
        }
    }

    @Override // android.app.Fragment
    public final void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        SparseArray<Parcelable> sparseArray = new SparseArray<>();
        this.mStatusBar.saveHierarchyState(sparseArray);
        bundle.putSparseParcelableArray("panel_state", sparseArray);
    }

    @Override // android.app.Fragment
    public final void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        StatusBarFragmentComponent create = this.mStatusBarFragmentComponentFactory.create(this);
        this.mStatusBarFragmentComponent = create;
        create.init();
        PhoneStatusBarView phoneStatusBarView = (PhoneStatusBarView) view;
        this.mStatusBar = phoneStatusBarView;
        View findViewById = phoneStatusBarView.findViewById(2131428925);
        findViewById.addOnLayoutChangeListener(this.mStatusBarLayoutListener);
        updateStatusBarLocation(findViewById.getLeft(), findViewById.getRight());
        if (bundle != null && bundle.containsKey("panel_state")) {
            this.mStatusBar.restoreHierarchyState(bundle.getSparseParcelableArray("panel_state"));
        }
        StatusBarIconController.DarkIconManager darkIconManager = new StatusBarIconController.DarkIconManager((LinearLayout) view.findViewById(2131428922), this.mFeatureFlags);
        this.mDarkIconManager = darkIconManager;
        darkIconManager.mShouldLog = true;
        this.mBlockedIcons.add(getString(17041565));
        this.mBlockedIcons.add(getString(17041534));
        this.mBlockedIcons.add(getString(17041537));
        StatusBarIconController.DarkIconManager darkIconManager2 = this.mDarkIconManager;
        ArrayList arrayList = this.mBlockedIcons;
        Objects.requireNonNull(darkIconManager2);
        darkIconManager2.mBlockList.clear();
        if (arrayList != null && !arrayList.isEmpty()) {
            darkIconManager2.mBlockList.addAll(arrayList);
        }
        this.mStatusBarIconController.addIconGroup(this.mDarkIconManager);
        this.mSystemIconArea = (LinearLayout) this.mStatusBar.findViewById(2131429001);
        this.mClockView = this.mStatusBar.findViewById(2131427717);
        this.mOngoingCallChip = this.mStatusBar.findViewById(2131428537);
        SystemStatusAnimationScheduler systemStatusAnimationScheduler = this.mAnimationScheduler;
        Objects.requireNonNull(systemStatusAnimationScheduler);
        int i = systemStatusAnimationScheduler.animationState;
        if (i == 0 || i == 4) {
            animateShow(this.mSystemIconArea, false);
        }
        animateShow(this.mClockView, false);
        View findViewById2 = this.mStatusBar.findViewById(2131427907);
        if (this.mNetworkController.hasEmergencyCryptKeeperText()) {
            if (findViewById2 != null) {
                ((ViewStub) findViewById2).inflate();
            }
            this.mNetworkController.addCallback(this.mSignalCallback);
        } else if (findViewById2 != null) {
            ((ViewGroup) findViewById2.getParent()).removeView(findViewById2);
        }
        if (getResources().getBoolean(2131034169)) {
            OperatorNameViewController.Factory factory = this.mOperatorNameViewControllerFactory;
            Objects.requireNonNull(factory);
            OperatorNameViewController operatorNameViewController = new OperatorNameViewController((OperatorNameView) ((ViewStub) this.mStatusBar.findViewById(2131428540)).inflate(), factory.mDarkIconDispatcher, factory.mNetworkController, factory.mTunerService, factory.mTelephonyManager, factory.mKeyguardUpdateMonitor);
            this.mOperatorNameViewController = operatorNameViewController;
            operatorNameViewController.init();
        }
        ViewGroup viewGroup = (ViewGroup) this.mStatusBar.findViewById(2131428512);
        NotificationIconAreaController notificationIconAreaController = this.mNotificationIconAreaController;
        Objects.requireNonNull(notificationIconAreaController);
        View view2 = notificationIconAreaController.mNotificationIconArea;
        this.mNotificationIconAreaInner = view2;
        if (view2.getParent() != null) {
            ((ViewGroup) this.mNotificationIconAreaInner.getParent()).removeView(this.mNotificationIconAreaInner);
        }
        viewGroup.addView(this.mNotificationIconAreaInner);
        updateNotificationIconAreaAndCallChip(this.mDisabled1, false);
        this.mAnimationScheduler.addCallback((SystemStatusAnimationCallback) this);
    }
}

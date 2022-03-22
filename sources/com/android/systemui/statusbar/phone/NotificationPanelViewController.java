package com.android.systemui.statusbar.phone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Insets;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.hardware.biometrics.SensorLocationInternal;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.RemoteException;
import android.os.Trace;
import android.os.UserManager;
import android.provider.Settings;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.util.Log;
import android.util.MathUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.ViewStub;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.leanback.R$raw;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.policy.ScreenDecorationsUtils;
import com.android.internal.policy.SystemBarUtils;
import com.android.keyguard.KeyguardClockSwitch;
import com.android.keyguard.KeyguardClockSwitchController;
import com.android.keyguard.KeyguardStatusView;
import com.android.keyguard.KeyguardStatusViewController;
import com.android.keyguard.KeyguardUnfoldTransition;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardVisibilityHelper;
import com.android.keyguard.LockIconView;
import com.android.keyguard.LockIconViewController;
import com.android.keyguard.dagger.KeyguardQsUserSwitchComponent;
import com.android.keyguard.dagger.KeyguardStatusBarViewComponent;
import com.android.keyguard.dagger.KeyguardStatusViewComponent;
import com.android.keyguard.dagger.KeyguardUserSwitcherComponent;
import com.android.systemui.ActivityStarterDelegate$$ExternalSyntheticLambda0;
import com.android.systemui.DejankUtils;
import com.android.systemui.R$anim;
import com.android.systemui.R$array;
import com.android.systemui.ScreenDecorations$$ExternalSyntheticLambda4;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.animation.LaunchAnimator;
import com.android.systemui.animation.ShadeInterpolation;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.biometrics.UdfpsKeyguardViewController;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.communal.CommunalHostView;
import com.android.systemui.communal.CommunalHostViewController;
import com.android.systemui.communal.CommunalHostViewPositionAlgorithm;
import com.android.systemui.communal.CommunalSource;
import com.android.systemui.communal.CommunalSourceMonitor;
import com.android.systemui.communal.CommunalStateController;
import com.android.systemui.communal.dagger.CommunalViewComponent;
import com.android.systemui.controls.dagger.ControlsComponent;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.fragments.FragmentHostManager;
import com.android.systemui.fragments.FragmentService;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.media.KeyguardMediaController;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.MediaHierarchyManager;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavigationBarView;
import com.android.systemui.plugins.ClockPlugin;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qrcodescanner.controller.QRCodeScannerController;
import com.android.systemui.qs.HeaderPrivacyIconsController;
import com.android.systemui.qs.tiles.CastTile$$ExternalSyntheticLambda1;
import com.android.systemui.recents.OverviewProxyService$$ExternalSyntheticLambda4;
import com.android.systemui.screenrecord.RecordingController;
import com.android.systemui.scrim.ScrimView;
import com.android.systemui.scrim.ScrimView$$ExternalSyntheticLambda1;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.KeyguardAffordanceView;
import com.android.systemui.statusbar.KeyguardIndicationController;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.NotificationShelfController;
import com.android.systemui.statusbar.PulseExpansionHandler;
import com.android.systemui.statusbar.QsFrameTranslateController;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.events.PrivacyDotViewController;
import com.android.systemui.statusbar.events.ViewState;
import com.android.systemui.statusbar.notification.AnimatableProperty;
import com.android.systemui.statusbar.notification.ConversationNotificationManager;
import com.android.systemui.statusbar.notification.DynamicPrivacyController;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.PropertyAnimator;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy;
import com.android.systemui.statusbar.notification.collection.render.NotifPanelEventSource;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.notification.stack.StackScrollAlgorithm;
import com.android.systemui.statusbar.notification.stack.StackStateAnimator;
import com.android.systemui.statusbar.phone.KeyguardAffordanceHelper;
import com.android.systemui.statusbar.phone.KeyguardClockPositionAlgorithm;
import com.android.systemui.statusbar.phone.LockscreenGestureLogger;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.PanelViewController;
import com.android.systemui.statusbar.phone.PhoneStatusBarView;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManagerKt;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardQsUserSwitchController;
import com.android.systemui.statusbar.policy.KeyguardUserSwitcherController;
import com.android.systemui.statusbar.policy.KeyguardUserSwitcherScrim;
import com.android.systemui.statusbar.policy.KeyguardUserSwitcherView;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import com.android.systemui.user.CreateUserActivity$$ExternalSyntheticLambda1;
import com.android.systemui.util.ListenerSet;
import com.android.systemui.util.Utils;
import com.android.systemui.util.settings.SecureSettings;
import com.android.systemui.wallet.controller.QuickAccessWalletController;
import com.android.systemui.wmshell.WMShell$$ExternalSyntheticLambda6;
import com.android.wm.shell.ShellCommandHandlerImpl$$ExternalSyntheticLambda1;
import com.android.wm.shell.ShellCommandHandlerImpl$$ExternalSyntheticLambda3;
import com.android.wm.shell.animation.FlingAnimationUtils;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import javax.inject.Provider;
import kotlin.jvm.internal.Intrinsics;
/* loaded from: classes.dex */
public final class NotificationPanelViewController extends PanelViewController implements NotifPanelEventSource {
    public final AccessibilityManager mAccessibilityManager;
    public final ActivityManager mActivityManager;
    public boolean mAffordanceHasPreview;
    public KeyguardAffordanceHelper mAffordanceHelper;
    public boolean mAllowExpandForSmallExpansion;
    public int mAmbientIndicationBottomPadding;
    public boolean mAnimateNextNotificationBounds;
    public boolean mAnimateNextPositionUpdate;
    public boolean mAnimatingQS;
    public final AuthController mAuthController;
    public int mBarState;
    public boolean mBlockTouches;
    public boolean mBlockingExpansionForCurrentTouch;
    public float mBottomAreaShadeAlpha;
    public final ValueAnimator mBottomAreaShadeAlphaAnimator;
    public boolean mBouncerShowing;
    public boolean mClosingWithAlphaFadeOut;
    public boolean mCollapsedOnDown;
    public final CommandQueue mCommandQueue;
    public WeakReference<CommunalSource> mCommunalSource;
    public final CommunalSourceMonitor mCommunalSourceMonitor;
    public final NotificationPanelViewController$$ExternalSyntheticLambda2 mCommunalSourceMonitorCallback;
    public final CommunalStateController mCommunalStateController;
    public CommunalHostView mCommunalView;
    public final CommunalViewComponent.Factory mCommunalViewComponentFactory;
    public CommunalHostViewController mCommunalViewController;
    public final ConfigurationController mConfigurationController;
    public boolean mConflictingQsExpansionGesture;
    public final ContentResolver mContentResolver;
    public final ControlsComponent mControlsComponent;
    public final ConversationNotificationManager mConversationNotificationManager;
    public NotificationShadeDepthController mDepthController;
    public int mDisplayId;
    public int mDistanceForQSFullShadeTransition;
    public float mDownX;
    public float mDownY;
    public final DozeParameters mDozeParameters;
    public boolean mDozing;
    public boolean mDozingOnDown;
    public final NotificationEntryManager mEntryManager;
    public Runnable mExpandAfterLayoutRunnable;
    public boolean mExpandingFromHeadsUp;
    public boolean mExpectingSynthesizedDown;
    public final FalsingCollector mFalsingCollector;
    public final FalsingManager mFalsingManager;
    public final FeatureFlags mFeatureFlags;
    public FlingAnimationUtils mFlingAnimationUtils;
    public final Provider<FlingAnimationUtils.Builder> mFlingAnimationUtilsBuilder;
    public final FragmentService mFragmentService;
    public NotificationGroupManagerLegacy mGroupManager;
    public boolean mHeadsUpAnimatingAway;
    public HeadsUpAppearanceController mHeadsUpAppearanceController;
    public int mHeadsUpInset;
    public boolean mHeadsUpPinnedMode;
    public HeadsUpTouchHelper mHeadsUpTouchHelper;
    public Runnable mHideExpandedRunnable;
    public int mIndicationBottomPadding;
    public float mInitialHeightOnTouch;
    public float mInitialTouchX;
    public float mInitialTouchY;
    public final InteractionJankMonitor mInteractionJankMonitor;
    public float mInterpolatedDarkAmount;
    public boolean mIsExpanding;
    public boolean mIsFullWidth;
    public boolean mIsGestureNavigation;
    public boolean mIsLaunchTransitionFinished;
    public boolean mIsLaunchTransitionRunning;
    public boolean mIsPanelCollapseOnQQS;
    public boolean mIsPulseExpansionResetAnimator;
    public boolean mIsQsTranslationResetAnimator;
    public final KeyguardBypassController mKeyguardBypassController;
    public KeyguardIndicationController mKeyguardIndicationController;
    public KeyguardMediaController mKeyguardMediaController;
    public float mKeyguardNotificationBottomPadding;
    public final KeyguardQsUserSwitchComponent.Factory mKeyguardQsUserSwitchComponentFactory;
    public KeyguardQsUserSwitchController mKeyguardQsUserSwitchController;
    public boolean mKeyguardQsUserSwitchEnabled;
    public boolean mKeyguardShowing;
    public KeyguardStatusBarView mKeyguardStatusBar;
    public final KeyguardStatusBarViewComponent.Factory mKeyguardStatusBarViewComponentFactory;
    public KeyguardStatusBarViewController mKeyguardStatusBarViewController;
    public final KeyguardStatusViewComponent.Factory mKeyguardStatusViewComponentFactory;
    public KeyguardStatusViewController mKeyguardStatusViewController;
    public Optional<KeyguardUnfoldTransition> mKeyguardUnfoldTransition;
    public final KeyguardUserSwitcherComponent.Factory mKeyguardUserSwitcherComponentFactory;
    public KeyguardUserSwitcherController mKeyguardUserSwitcherController;
    public boolean mKeyguardUserSwitcherEnabled;
    public boolean mLastEventSynthesizedDown;
    public float mLastOverscroll;
    public Runnable mLaunchAnimationEndRunnable;
    public boolean mLaunchingAffordance;
    public final LayoutInflater mLayoutInflater;
    public float mLinearDarkAmount;
    public boolean mListenForHeadsUp;
    public LockIconViewController mLockIconViewController;
    public int mLockscreenNotificationQSPadding;
    public final LockscreenShadeTransitionController mLockscreenShadeTransitionController;
    public final NotificationLockscreenUserManager mLockscreenUserManager;
    public int mMaxAllowedKeyguardNotifications;
    public final int mMaxKeyguardNotifications;
    public int mMaxOverscrollAmountForPulse;
    public final MediaDataManager mMediaDataManager;
    public final MediaHierarchyManager mMediaHierarchyManager;
    public final MetricsLogger mMetricsLogger;
    public float mMinFraction;
    public int mNavigationBarBottomHeight;
    public long mNotificationBoundsAnimationDelay;
    public long mNotificationBoundsAnimationDuration;
    public NotificationsQuickSettingsContainer mNotificationContainerParent;
    public final NotificationIconAreaController mNotificationIconAreaController;
    public Optional<NotificationPanelUnfoldAnimationController> mNotificationPanelUnfoldAnimationController;
    public final AnonymousClass19 mNotificationPanelViewStateProvider;
    public NotificationShelfController mNotificationShelfController;
    public final NotificationStackScrollLayoutController mNotificationStackScrollLayoutController;
    public NotificationsQSContainerController mNotificationsQSContainerController;
    public int mOldLayoutDirection;
    public boolean mOnlyAffordanceInThisMotion;
    public float mOverStretchAmount;
    public int mPanelAlpha;
    public final AnimatableProperty.AnonymousClass6 mPanelAlphaAnimator;
    public Runnable mPanelAlphaEndAction;
    public final AnimationProperties mPanelAlphaInPropertiesAnimator;
    public final AnimationProperties mPanelAlphaOutPropertiesAnimator;
    public boolean mPanelExpanded;
    public final PowerManager mPowerManager;
    public ViewGroup mPreviewContainer;
    public final PrivacyDotViewController mPrivacyDotViewController;
    public final PulseExpansionHandler mPulseExpansionHandler;
    public boolean mPulsing;
    public final QRCodeScannerController mQRCodeScannerController;
    public boolean mQSAnimatingHiddenFromCollapsed;
    @VisibleForTesting
    public QS mQs;
    public boolean mQsAnimatorExpand;
    public int mQsClipBottom;
    public int mQsClipTop;
    public boolean mQsExpandImmediate;
    public boolean mQsExpanded;
    public boolean mQsExpandedWhenExpandingStarted;
    public ValueAnimator mQsExpansionAnimator;
    public boolean mQsExpansionFromOverscroll;
    public float mQsExpansionHeight;
    public int mQsFalsingThreshold;
    public FrameLayout mQsFrame;
    public QsFrameTranslateController mQsFrameTranslateController;
    public boolean mQsFullyExpanded;
    public int mQsMaxExpansionHeight;
    public int mQsMinExpansionHeight;
    public int mQsPeekHeight;
    public ValueAnimator mQsSizeChangeAnimator;
    public boolean mQsTouchAboveFalsingThreshold;
    public boolean mQsTracking;
    public float mQsTranslationForFullShadeTransition;
    public VelocityTracker mQsVelocityTracker;
    public boolean mQsVisible;
    public final QuickAccessWalletController mQuickAccessWalletController;
    public float mQuickQsOffsetHeight;
    public final RecordingController mRecordingController;
    public final NotificationRemoteInputManager mRemoteInputManager;
    public int mScreenCornerRadius;
    public ScreenOffAnimationController mScreenOffAnimationController;
    public final ScrimController mScrimController;
    public int mScrimCornerRadius;
    public final SecureSettings mSecureSettings;
    public final SettingsChangeObserver mSettingsChangeObserver;
    public boolean mShowIconsWhenExpanded;
    public final SplitShadeHeaderController mSplitShadeHeaderController;
    public int mSplitShadeNotificationsScrimMarginBottom;
    public int mSplitShadeStatusBarHeight;
    public int mStackScrollerMeasuringPass;
    public boolean mStackScrollerOverscrolling;
    public int mStatusBarHeaderHeightKeyguard;
    public final StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    public int mStatusBarMinHeight;
    public final SysUiState mSysUiState;
    public final TapAgainViewController mTapAgainViewController;
    public ExpandableNotificationRow mTrackedHeadsUpNotification;
    public int mTrackingPointer;
    public int mTransitionToFullShadeQSPosition;
    public float mTransitioningToFullShadeProgress;
    public boolean mTwoFingerQsExpandPossible;
    public float mUdfpsMaxYBurnInOffset;
    public final Executor mUiExecutor;
    public final KeyguardUpdateMonitor mUpdateMonitor;
    public final boolean mUseCombinedQSHeaders;
    public final UserManager mUserManager;
    public boolean mUserSetupComplete;
    public final VibratorHelper mVibratorHelper;
    public final NotificationPanelView mView;
    public static final long ANIMATION_DELAY_ICON_FADE_IN = 82;
    public static final Rect M_DUMMY_DIRTY_RECT = new Rect(0, 0, 1, 1);
    public static final Rect EMPTY_RECT = new Rect();
    public final OnClickListener mOnClickListener = new OnClickListener();
    public final KeyguardAffordanceHelperCallback mKeyguardAffordanceHelperCallback = new KeyguardAffordanceHelperCallback();
    public final MyOnHeadsUpChangedListener mOnHeadsUpChangedListener = new MyOnHeadsUpChangedListener();
    public final HeightListener mHeightListener = new HeightListener();
    public final ConfigurationListener mConfigurationListener = new ConfigurationListener();
    @VisibleForTesting
    public final StatusBarStateListener mStatusBarStateListener = new StatusBarStateListener();
    public boolean mQsExpansionEnabledPolicy = true;
    public boolean mQsExpansionEnabledAmbient = true;
    public int mDisplayTopInset = 0;
    public int mDisplayRightInset = 0;
    public final KeyguardClockPositionAlgorithm mClockPositionAlgorithm = new KeyguardClockPositionAlgorithm();
    public final KeyguardClockPositionAlgorithm.Result mClockPositionResult = new KeyguardClockPositionAlgorithm.Result();
    public final CommunalHostViewPositionAlgorithm mCommunalPositionAlgorithm = new CommunalHostViewPositionAlgorithm();
    public final CommunalHostViewPositionAlgorithm.Result mCommunalPositionResult = new CommunalHostViewPositionAlgorithm.Result();
    public boolean mQsScrimEnabled = true;
    public String mLastCameraLaunchSource = "lockscreen_affordance";
    public ScreenDecorations$$ExternalSyntheticLambda4 mHeadsUpExistenceChangedRunnable = new ScreenDecorations$$ExternalSyntheticLambda4(this, 2);
    public boolean mHideIconsDuringLaunchAnimation = true;
    public final ArrayList<Consumer<ExpandableNotificationRow>> mTrackingHeadsUpListeners = new ArrayList<>();
    public final Rect mQsClippingAnimationEndBounds = new Rect();
    public ValueAnimator mQsClippingAnimation = null;
    public final Rect mKeyguardStatusAreaClipBounds = new Rect();
    public final Region mQsInterceptRegion = new Region();
    public float mKeyguardOnlyContentAlpha = 1.0f;
    public boolean mStatusViewCentered = true;
    public final ListenerSet<NotifPanelEventSource.Callbacks> mNotifEventSourceCallbacks = new ListenerSet<>();
    public AnonymousClass1 mAccessibilityDelegate = new View.AccessibilityDelegate() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController.1
        @Override // android.view.View.AccessibilityDelegate
        public final boolean performAccessibilityAction(View view, int i, Bundle bundle) {
            if (i != AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_FORWARD.getId() && i != AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_UP.getId()) {
                return super.performAccessibilityAction(view, i, bundle);
            }
            NotificationPanelViewController.this.mStatusBarKeyguardViewManager.showBouncer(true);
            return true;
        }

        @Override // android.view.View.AccessibilityDelegate
        public final void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
            accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_FORWARD);
            accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_UP);
        }
    };
    public final AnonymousClass2 mCommunalStateCallback = new CommunalStateController.Callback() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController.2
        @Override // com.android.systemui.communal.CommunalStateController.Callback
        public final void onCommunalViewShowingChanged() {
            NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
            KeyguardStatusViewController keyguardStatusViewController = notificationPanelViewController.mKeyguardStatusViewController;
            int i = notificationPanelViewController.mBarState;
            boolean isKeyguardFadingAway = notificationPanelViewController.mKeyguardStateController.isKeyguardFadingAway();
            boolean goingToFullShade = NotificationPanelViewController.this.mStatusBarStateController.goingToFullShade();
            int i2 = NotificationPanelViewController.this.mBarState;
            Objects.requireNonNull(keyguardStatusViewController);
            keyguardStatusViewController.mKeyguardVisibilityHelper.setViewVisibility(i, isKeyguardFadingAway, goingToFullShade, i2);
            NotificationPanelViewController notificationPanelViewController2 = NotificationPanelViewController.this;
            KeyguardUserSwitcherController keyguardUserSwitcherController = notificationPanelViewController2.mKeyguardUserSwitcherController;
            if (keyguardUserSwitcherController != null) {
                keyguardUserSwitcherController.mKeyguardVisibilityHelper.setViewVisibility(notificationPanelViewController2.mBarState, notificationPanelViewController2.mKeyguardStateController.isKeyguardFadingAway(), NotificationPanelViewController.this.mStatusBarStateController.goingToFullShade(), NotificationPanelViewController.this.mBarState);
            }
            NotificationPanelViewController notificationPanelViewController3 = NotificationPanelViewController.this;
            KeyguardQsUserSwitchController keyguardQsUserSwitchController = notificationPanelViewController3.mKeyguardQsUserSwitchController;
            if (keyguardQsUserSwitchController != null) {
                keyguardQsUserSwitchController.mKeyguardVisibilityHelper.setViewVisibility(notificationPanelViewController3.mBarState, notificationPanelViewController3.mKeyguardStateController.isKeyguardFadingAway(), NotificationPanelViewController.this.mStatusBarStateController.goingToFullShade(), NotificationPanelViewController.this.mBarState);
            }
        }
    };
    public final AnonymousClass3 mFalsingTapListener = new FalsingManager.FalsingTapListener() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController.3
        @Override // com.android.systemui.plugins.FalsingManager.FalsingTapListener
        public final void onDoubleTapRequired() {
            if (NotificationPanelViewController.this.mStatusBarStateController.getState() == 2) {
                TapAgainViewController tapAgainViewController = NotificationPanelViewController.this.mTapAgainViewController;
                Objects.requireNonNull(tapAgainViewController);
                Runnable runnable = tapAgainViewController.mHideCanceler;
                if (runnable != null) {
                    runnable.run();
                }
                ((TapAgainView) tapAgainViewController.mView).animateIn();
                tapAgainViewController.mHideCanceler = tapAgainViewController.mDelayableExecutor.executeDelayed(new CreateUserActivity$$ExternalSyntheticLambda1(tapAgainViewController, 5), tapAgainViewController.mDoubleTapTimeMs);
            } else {
                NotificationPanelViewController.this.mKeyguardIndicationController.showTransientIndication(2131952924);
            }
            NotificationPanelViewController.this.mVibratorHelper.vibrate(1);
        }
    };
    public final AnonymousClass8 mAnimateKeyguardBottomAreaInvisibleEndRunnable = new Runnable() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController.8
        @Override // java.lang.Runnable
        public final void run() {
            NotificationPanelViewController.this.mKeyguardBottomArea.setVisibility(8);
        }
    };
    public final AnonymousClass13 mScrollListener = new QS.ScrollListener() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController.13
        @Override // com.android.systemui.plugins.qs.QS.ScrollListener
        public final void onQsPanelScrollChanged(int i) {
            SplitShadeHeaderController splitShadeHeaderController = NotificationPanelViewController.this.mSplitShadeHeaderController;
            Objects.requireNonNull(splitShadeHeaderController);
            if (splitShadeHeaderController.qsScrollY != i) {
                splitShadeHeaderController.qsScrollY = i;
                if (!splitShadeHeaderController.splitShadeMode && splitShadeHeaderController.combinedHeaders) {
                    splitShadeHeaderController.statusBar.setScrollY(i);
                }
            }
            if (i > 0) {
                NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
                if (!notificationPanelViewController.mQsFullyExpanded) {
                    notificationPanelViewController.expandWithQs();
                }
            }
        }
    };
    public final AnonymousClass14 mFragmentListener = new AnonymousClass14();
    public final AnonymousClass16 mMaybeHideExpandedRunnable = new Runnable() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController.16
        @Override // java.lang.Runnable
        public final void run() {
            NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
            Objects.requireNonNull(notificationPanelViewController);
            if (notificationPanelViewController.mExpandedFraction == 0.0f) {
                NotificationPanelViewController notificationPanelViewController2 = NotificationPanelViewController.this;
                notificationPanelViewController2.mView.post(notificationPanelViewController2.mHideExpandedRunnable);
            }
        }
    };
    public final AnonymousClass18 mStatusBarViewTouchEventHandler = new AnonymousClass18();
    public int mCurrentPanelState = 0;
    public boolean mShouldUseSplitNotificationShade = Utils.shouldUseSplitNotificationShade(this.mResources);

    /* renamed from: com.android.systemui.statusbar.phone.NotificationPanelViewController$14  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass14 implements FragmentHostManager.FragmentListener {
        public AnonymousClass14() {
        }

        @Override // com.android.systemui.fragments.FragmentHostManager.FragmentListener
        public final void onFragmentViewCreated(Fragment fragment) {
            NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
            QS qs = (QS) fragment;
            notificationPanelViewController.mQs = qs;
            qs.setPanelView(notificationPanelViewController.mHeightListener);
            NotificationPanelViewController notificationPanelViewController2 = NotificationPanelViewController.this;
            notificationPanelViewController2.mQs.setExpandClickListener(notificationPanelViewController2.mOnClickListener);
            NotificationPanelViewController notificationPanelViewController3 = NotificationPanelViewController.this;
            notificationPanelViewController3.mQs.setHeaderClickable(notificationPanelViewController3.isQsExpansionEnabled());
            NotificationPanelViewController notificationPanelViewController4 = NotificationPanelViewController.this;
            notificationPanelViewController4.mQs.setOverscrolling(notificationPanelViewController4.mStackScrollerOverscrolling);
            NotificationPanelViewController notificationPanelViewController5 = NotificationPanelViewController.this;
            notificationPanelViewController5.mQs.setInSplitShade(notificationPanelViewController5.mShouldUseSplitNotificationShade);
            NotificationPanelViewController.this.mQs.getView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController$14$$ExternalSyntheticLambda0
                @Override // android.view.View.OnLayoutChangeListener
                public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                    NotificationPanelViewController.AnonymousClass14 r0 = NotificationPanelViewController.AnonymousClass14.this;
                    if (i4 - i2 != i8 - i6) {
                        NotificationPanelViewController.this.mHeightListener.onQsHeightChanged();
                    } else {
                        Objects.requireNonNull(r0);
                    }
                }
            });
            NotificationPanelViewController.this.mQs.setCollapsedMediaVisibilityChangedListener(new OverviewProxyService$$ExternalSyntheticLambda4(this, 1));
            NotificationPanelViewController notificationPanelViewController6 = NotificationPanelViewController.this;
            LockscreenShadeTransitionController lockscreenShadeTransitionController = notificationPanelViewController6.mLockscreenShadeTransitionController;
            QS qs2 = notificationPanelViewController6.mQs;
            Objects.requireNonNull(lockscreenShadeTransitionController);
            lockscreenShadeTransitionController.qS = qs2;
            NotificationPanelViewController notificationPanelViewController7 = NotificationPanelViewController.this;
            NotificationStackScrollLayoutController notificationStackScrollLayoutController = notificationPanelViewController7.mNotificationStackScrollLayoutController;
            Objects.requireNonNull(notificationStackScrollLayoutController);
            NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
            Objects.requireNonNull(notificationStackScrollLayout);
            notificationStackScrollLayout.mQsContainer = (ViewGroup) notificationPanelViewController7.mQs.getView();
            NotificationPanelViewController notificationPanelViewController8 = NotificationPanelViewController.this;
            notificationPanelViewController8.mQs.setScrollListener(notificationPanelViewController8.mScrollListener);
            NotificationPanelViewController.this.updateQsExpansion$1();
        }

        @Override // com.android.systemui.fragments.FragmentHostManager.FragmentListener
        public final void onFragmentViewDestroyed(Fragment fragment) {
            NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
            if (fragment == notificationPanelViewController.mQs) {
                notificationPanelViewController.mQs = null;
            }
        }
    }

    /* renamed from: com.android.systemui.statusbar.phone.NotificationPanelViewController$17  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass17 extends PanelViewController.TouchHandler {
        public long mLastTouchDownTime = -1;

        public AnonymousClass17() {
            super();
        }

        /* JADX WARN: Removed duplicated region for block: B:134:0x0242  */
        /* JADX WARN: Removed duplicated region for block: B:174:0x02b5 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:175:0x02b6  */
        /* JADX WARN: Removed duplicated region for block: B:329:0x0559 A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:34:0x0075  */
        /* JADX WARN: Removed duplicated region for block: B:58:0x00ce  */
        /* JADX WARN: Removed duplicated region for block: B:83:0x012e  */
        @Override // com.android.systemui.statusbar.phone.PanelViewController.TouchHandler, android.view.View.OnTouchListener
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final boolean onTouch(android.view.View r18, android.view.MotionEvent r19) {
            /*
                Method dump skipped, instructions count: 1475
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.NotificationPanelViewController.AnonymousClass17.onTouch(android.view.View, android.view.MotionEvent):boolean");
        }
    }

    /* renamed from: com.android.systemui.statusbar.phone.NotificationPanelViewController$18  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass18 implements PhoneStatusBarView.TouchEventHandler {
        public AnonymousClass18() {
        }
    }

    /* renamed from: com.android.systemui.statusbar.phone.NotificationPanelViewController$19  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass19 implements NotificationPanelViewStateProvider {
        public AnonymousClass19() {
        }
    }

    /* loaded from: classes.dex */
    public class ConfigurationListener implements ConfigurationController.ConfigurationListener {
        public ConfigurationListener() {
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public final void onDensityOrFontScaleChanged() {
            NotificationPanelViewController.m101$$Nest$mreInflateViews(NotificationPanelViewController.this);
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public final void onSmallestScreenWidthChanged() {
            Trace.beginSection("onSmallestScreenWidthChanged");
            NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
            boolean z = notificationPanelViewController.mKeyguardUserSwitcherEnabled;
            boolean z2 = notificationPanelViewController.mKeyguardQsUserSwitchEnabled;
            notificationPanelViewController.updateUserSwitcherFlags();
            NotificationPanelViewController notificationPanelViewController2 = NotificationPanelViewController.this;
            if (!(z == notificationPanelViewController2.mKeyguardUserSwitcherEnabled && z2 == notificationPanelViewController2.mKeyguardQsUserSwitchEnabled)) {
                NotificationPanelViewController.m101$$Nest$mreInflateViews(notificationPanelViewController2);
            }
            Trace.endSection();
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public final void onThemeChanged() {
            NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
            notificationPanelViewController.mView.getContext().getThemeResId();
            Objects.requireNonNull(notificationPanelViewController);
            NotificationPanelViewController.m101$$Nest$mreInflateViews(NotificationPanelViewController.this);
        }
    }

    /* loaded from: classes.dex */
    public class DynamicPrivacyControlListener implements DynamicPrivacyController.Listener {
        public DynamicPrivacyControlListener() {
        }

        @Override // com.android.systemui.statusbar.notification.DynamicPrivacyController.Listener
        public final void onDynamicPrivacyChanged() {
            NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
            if (notificationPanelViewController.mLinearDarkAmount == 0.0f) {
                notificationPanelViewController.mAnimateNextPositionUpdate = true;
            }
        }
    }

    /* loaded from: classes.dex */
    public class HeightListener implements QS.HeightListener {
        public HeightListener() {
        }

        @Override // com.android.systemui.plugins.qs.QS.HeightListener
        public final void onQsHeightChanged() {
            int i;
            NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
            QS qs = notificationPanelViewController.mQs;
            if (qs != null) {
                i = qs.getDesiredHeight();
            } else {
                i = 0;
            }
            notificationPanelViewController.mQsMaxExpansionHeight = i;
            NotificationPanelViewController notificationPanelViewController2 = NotificationPanelViewController.this;
            if (notificationPanelViewController2.mQsExpanded && notificationPanelViewController2.mQsFullyExpanded) {
                notificationPanelViewController2.mQsExpansionHeight = notificationPanelViewController2.mQsMaxExpansionHeight;
                notificationPanelViewController2.requestScrollerTopPaddingUpdate(false);
                NotificationPanelViewController.this.requestPanelHeightUpdate();
            }
            if (NotificationPanelViewController.this.mAccessibilityManager.isEnabled()) {
                NotificationPanelViewController notificationPanelViewController3 = NotificationPanelViewController.this;
                notificationPanelViewController3.mView.setAccessibilityPaneTitle(notificationPanelViewController3.determineAccessibilityPaneTitle());
            }
            NotificationPanelViewController notificationPanelViewController4 = NotificationPanelViewController.this;
            NotificationStackScrollLayoutController notificationStackScrollLayoutController = notificationPanelViewController4.mNotificationStackScrollLayoutController;
            int i2 = notificationPanelViewController4.mQsMaxExpansionHeight;
            Objects.requireNonNull(notificationStackScrollLayoutController);
            NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
            Objects.requireNonNull(notificationStackScrollLayout);
            notificationStackScrollLayout.mMaxTopPadding = i2;
        }
    }

    /* loaded from: classes.dex */
    public class KeyguardAffordanceHelperCallback implements KeyguardAffordanceHelper.Callback {
        public KeyguardAffordanceHelperCallback() {
        }

        public final KeyguardAffordanceView getLeftIcon() {
            if (NotificationPanelViewController.this.mView.getLayoutDirection() == 1) {
                KeyguardBottomAreaView keyguardBottomAreaView = NotificationPanelViewController.this.mKeyguardBottomArea;
                Objects.requireNonNull(keyguardBottomAreaView);
                return keyguardBottomAreaView.mRightAffordanceView;
            }
            KeyguardBottomAreaView keyguardBottomAreaView2 = NotificationPanelViewController.this.mKeyguardBottomArea;
            Objects.requireNonNull(keyguardBottomAreaView2);
            return keyguardBottomAreaView2.mLeftAffordanceView;
        }

        public final float getMaxTranslationDistance() {
            NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
            Objects.requireNonNull(notificationPanelViewController);
            return (float) Math.hypot(NotificationPanelViewController.this.mView.getWidth(), notificationPanelViewController.mView.getHeight());
        }

        public final KeyguardAffordanceView getRightIcon() {
            if (NotificationPanelViewController.this.mView.getLayoutDirection() == 1) {
                KeyguardBottomAreaView keyguardBottomAreaView = NotificationPanelViewController.this.mKeyguardBottomArea;
                Objects.requireNonNull(keyguardBottomAreaView);
                return keyguardBottomAreaView.mLeftAffordanceView;
            }
            KeyguardBottomAreaView keyguardBottomAreaView2 = NotificationPanelViewController.this.mKeyguardBottomArea;
            Objects.requireNonNull(keyguardBottomAreaView2);
            return keyguardBottomAreaView2.mRightAffordanceView;
        }

        public final void onAnimationToSideStarted(boolean z, float f, float f2) {
            if (NotificationPanelViewController.this.mView.getLayoutDirection() != 1) {
                if (!z) {
                    z = true;
                } else {
                    z = false;
                }
            }
            NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
            notificationPanelViewController.mIsLaunchTransitionRunning = true;
            notificationPanelViewController.mLaunchAnimationEndRunnable = null;
            StatusBar statusBar = notificationPanelViewController.mStatusBar;
            Objects.requireNonNull(statusBar);
            float f3 = statusBar.mDisplayMetrics.density;
            int abs = Math.abs((int) (f / f3));
            int abs2 = Math.abs((int) (f2 / f3));
            if (z) {
                NotificationPanelViewController.this.mLockscreenGestureLogger.write(190, abs, abs2);
                LockscreenGestureLogger lockscreenGestureLogger = NotificationPanelViewController.this.mLockscreenGestureLogger;
                LockscreenGestureLogger.LockscreenUiEvent lockscreenUiEvent = LockscreenGestureLogger.LockscreenUiEvent.LOCKSCREEN_DIALER;
                Objects.requireNonNull(lockscreenGestureLogger);
                LockscreenGestureLogger.log(lockscreenUiEvent);
                NotificationPanelViewController.this.mFalsingCollector.onLeftAffordanceOn();
                NotificationPanelViewController.this.mFalsingCollector.shouldEnforceBouncer();
                NotificationPanelViewController.this.mKeyguardBottomArea.launchLeftAffordance();
            } else {
                if ("lockscreen_affordance".equals(NotificationPanelViewController.this.mLastCameraLaunchSource)) {
                    NotificationPanelViewController.this.mLockscreenGestureLogger.write(189, abs, abs2);
                    LockscreenGestureLogger lockscreenGestureLogger2 = NotificationPanelViewController.this.mLockscreenGestureLogger;
                    LockscreenGestureLogger.LockscreenUiEvent lockscreenUiEvent2 = LockscreenGestureLogger.LockscreenUiEvent.LOCKSCREEN_CAMERA;
                    Objects.requireNonNull(lockscreenGestureLogger2);
                    LockscreenGestureLogger.log(lockscreenUiEvent2);
                }
                NotificationPanelViewController.this.mFalsingCollector.onCameraOn();
                NotificationPanelViewController.this.mFalsingCollector.shouldEnforceBouncer();
                NotificationPanelViewController notificationPanelViewController2 = NotificationPanelViewController.this;
                notificationPanelViewController2.mKeyguardBottomArea.launchCamera(notificationPanelViewController2.mLastCameraLaunchSource);
            }
            StatusBar statusBar2 = NotificationPanelViewController.this.mStatusBar;
            Objects.requireNonNull(statusBar2);
            statusBar2.mMessageRouter.sendMessageDelayed(1003, 5000L);
            NotificationPanelViewController.this.mBlockTouches = true;
        }
    }

    /* loaded from: classes.dex */
    public class MyOnHeadsUpChangedListener implements OnHeadsUpChangedListener {
        public MyOnHeadsUpChangedListener() {
        }

        @Override // com.android.systemui.statusbar.policy.OnHeadsUpChangedListener
        public final void onHeadsUpPinned(NotificationEntry notificationEntry) {
            if (!NotificationPanelViewController.this.isOnKeyguard()) {
                NotificationStackScrollLayoutController notificationStackScrollLayoutController = NotificationPanelViewController.this.mNotificationStackScrollLayoutController;
                ExpandableNotificationRow expandableNotificationRow = notificationEntry.row;
                Objects.requireNonNull(notificationStackScrollLayoutController);
                notificationStackScrollLayoutController.mView.generateHeadsUpAnimation(expandableNotificationRow, true);
            }
        }

        @Override // com.android.systemui.statusbar.policy.OnHeadsUpChangedListener
        public final void onHeadsUpPinnedModeChanged(boolean z) {
            if (z) {
                NotificationPanelViewController.this.mHeadsUpExistenceChangedRunnable.run();
                NotificationPanelViewController.this.updateNotificationTranslucency();
            } else {
                NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
                Objects.requireNonNull(notificationPanelViewController);
                notificationPanelViewController.mHeadsUpAnimatingAway = true;
                NotificationStackScrollLayoutController notificationStackScrollLayoutController = notificationPanelViewController.mNotificationStackScrollLayoutController;
                Objects.requireNonNull(notificationStackScrollLayoutController);
                NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
                Objects.requireNonNull(notificationStackScrollLayout);
                notificationStackScrollLayout.mHeadsUpAnimatingAway = true;
                notificationStackScrollLayout.updateClipping();
                notificationPanelViewController.updateVisibility();
                NotificationPanelViewController notificationPanelViewController2 = NotificationPanelViewController.this;
                NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = notificationPanelViewController2.mNotificationStackScrollLayoutController;
                ScreenDecorations$$ExternalSyntheticLambda4 screenDecorations$$ExternalSyntheticLambda4 = notificationPanelViewController2.mHeadsUpExistenceChangedRunnable;
                Objects.requireNonNull(notificationStackScrollLayoutController2);
                NotificationStackScrollLayout notificationStackScrollLayout2 = notificationStackScrollLayoutController2.mView;
                Objects.requireNonNull(notificationStackScrollLayout2);
                notificationStackScrollLayout2.mAnimationFinishedRunnables.add(screenDecorations$$ExternalSyntheticLambda4);
            }
            NotificationPanelViewController.this.updateGestureExclusionRect();
            NotificationPanelViewController notificationPanelViewController3 = NotificationPanelViewController.this;
            notificationPanelViewController3.mHeadsUpPinnedMode = z;
            notificationPanelViewController3.updateVisibility();
            NotificationPanelViewController.this.mKeyguardStatusBarViewController.updateForHeadsUp();
        }

        @Override // com.android.systemui.statusbar.policy.OnHeadsUpChangedListener
        public final void onHeadsUpUnPinned(NotificationEntry notificationEntry) {
            if (NotificationPanelViewController.this.isFullyCollapsed() && notificationEntry.isRowHeadsUp() && !NotificationPanelViewController.this.isOnKeyguard()) {
                NotificationStackScrollLayoutController notificationStackScrollLayoutController = NotificationPanelViewController.this.mNotificationStackScrollLayoutController;
                ExpandableNotificationRow expandableNotificationRow = notificationEntry.row;
                Objects.requireNonNull(notificationStackScrollLayoutController);
                notificationStackScrollLayoutController.mView.generateHeadsUpAnimation(expandableNotificationRow, false);
                ExpandableNotificationRow expandableNotificationRow2 = notificationEntry.row;
                if (expandableNotificationRow2 != null) {
                    expandableNotificationRow2.mMustStayOnScreen = false;
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public interface NotificationPanelViewStateProvider {
    }

    /* loaded from: classes.dex */
    public class OnApplyWindowInsetsListener implements View.OnApplyWindowInsetsListener {
        public OnApplyWindowInsetsListener() {
        }

        @Override // android.view.View.OnApplyWindowInsetsListener
        public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
            Insets insetsIgnoringVisibility = windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars() | WindowInsets.Type.displayCutout());
            NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
            notificationPanelViewController.mDisplayTopInset = insetsIgnoringVisibility.top;
            notificationPanelViewController.mDisplayRightInset = insetsIgnoringVisibility.right;
            notificationPanelViewController.mNavigationBarBottomHeight = windowInsets.getStableInsetBottom();
            NotificationPanelViewController.m102$$Nest$mupdateMaxHeadsUpTranslation(NotificationPanelViewController.this);
            return windowInsets;
        }
    }

    /* loaded from: classes.dex */
    public class OnAttachStateChangeListener implements View.OnAttachStateChangeListener {
        public OnAttachStateChangeListener() {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public final void onViewAttachedToWindow(View view) {
            NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
            notificationPanelViewController.mFragmentService.getFragmentHostManager(notificationPanelViewController.mView).addTagListener(QS.TAG, NotificationPanelViewController.this.mFragmentListener);
            NotificationPanelViewController notificationPanelViewController2 = NotificationPanelViewController.this;
            notificationPanelViewController2.mStatusBarStateController.addCallback(notificationPanelViewController2.mStatusBarStateListener);
            NotificationPanelViewController notificationPanelViewController3 = NotificationPanelViewController.this;
            notificationPanelViewController3.mConfigurationController.addCallback(notificationPanelViewController3.mConfigurationListener);
            NotificationPanelViewController notificationPanelViewController4 = NotificationPanelViewController.this;
            CommunalSourceMonitor communalSourceMonitor = notificationPanelViewController4.mCommunalSourceMonitor;
            NotificationPanelViewController$$ExternalSyntheticLambda2 notificationPanelViewController$$ExternalSyntheticLambda2 = notificationPanelViewController4.mCommunalSourceMonitorCallback;
            Objects.requireNonNull(communalSourceMonitor);
            communalSourceMonitor.mExecutor.execute(new CastTile$$ExternalSyntheticLambda1(communalSourceMonitor, notificationPanelViewController$$ExternalSyntheticLambda2, 2));
            NotificationPanelViewController.this.mConfigurationListener.onThemeChanged();
            NotificationPanelViewController notificationPanelViewController5 = NotificationPanelViewController.this;
            notificationPanelViewController5.mFalsingManager.addTapListener(notificationPanelViewController5.mFalsingTapListener);
            NotificationPanelViewController.this.mKeyguardIndicationController.init();
            NotificationPanelViewController notificationPanelViewController6 = NotificationPanelViewController.this;
            Objects.requireNonNull(notificationPanelViewController6);
            notificationPanelViewController6.mContentResolver.registerContentObserver(Settings.Global.getUriFor("user_switcher_enabled"), false, notificationPanelViewController6.mSettingsChangeObserver);
            NotificationPanelViewController notificationPanelViewController7 = NotificationPanelViewController.this;
            notificationPanelViewController7.mCommunalStateController.addCallback((CommunalStateController.Callback) notificationPanelViewController7.mCommunalStateCallback);
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public final void onViewDetachedFromWindow(View view) {
            NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
            Objects.requireNonNull(notificationPanelViewController);
            notificationPanelViewController.mContentResolver.unregisterContentObserver(notificationPanelViewController.mSettingsChangeObserver);
            NotificationPanelViewController notificationPanelViewController2 = NotificationPanelViewController.this;
            FragmentHostManager fragmentHostManager = notificationPanelViewController2.mFragmentService.getFragmentHostManager(notificationPanelViewController2.mView);
            AnonymousClass14 r0 = NotificationPanelViewController.this.mFragmentListener;
            Objects.requireNonNull(fragmentHostManager);
            ArrayList<FragmentHostManager.FragmentListener> arrayList = fragmentHostManager.mListeners.get(QS.TAG);
            if (arrayList != null && arrayList.remove(r0) && arrayList.size() == 0) {
                fragmentHostManager.mListeners.remove(QS.TAG);
            }
            NotificationPanelViewController notificationPanelViewController3 = NotificationPanelViewController.this;
            notificationPanelViewController3.mStatusBarStateController.removeCallback(notificationPanelViewController3.mStatusBarStateListener);
            NotificationPanelViewController notificationPanelViewController4 = NotificationPanelViewController.this;
            notificationPanelViewController4.mConfigurationController.removeCallback(notificationPanelViewController4.mConfigurationListener);
            NotificationPanelViewController notificationPanelViewController5 = NotificationPanelViewController.this;
            CommunalSourceMonitor communalSourceMonitor = notificationPanelViewController5.mCommunalSourceMonitor;
            NotificationPanelViewController$$ExternalSyntheticLambda2 notificationPanelViewController$$ExternalSyntheticLambda2 = notificationPanelViewController5.mCommunalSourceMonitorCallback;
            Objects.requireNonNull(communalSourceMonitor);
            communalSourceMonitor.mExecutor.execute(new ScrimView$$ExternalSyntheticLambda1(communalSourceMonitor, notificationPanelViewController$$ExternalSyntheticLambda2, 1));
            NotificationPanelViewController notificationPanelViewController6 = NotificationPanelViewController.this;
            notificationPanelViewController6.mFalsingManager.removeTapListener(notificationPanelViewController6.mFalsingTapListener);
            NotificationPanelViewController notificationPanelViewController7 = NotificationPanelViewController.this;
            CommunalStateController communalStateController = notificationPanelViewController7.mCommunalStateController;
            AnonymousClass2 r4 = notificationPanelViewController7.mCommunalStateCallback;
            Objects.requireNonNull(communalStateController);
            Objects.requireNonNull(r4, "Callback must not be null. b/128895449");
            communalStateController.mCallbacks.remove(r4);
        }
    }

    /* loaded from: classes.dex */
    public class OnClickListener implements View.OnClickListener {
        public OnClickListener() {
        }

        @Override // android.view.View.OnClickListener
        public final void onClick(View view) {
            NotificationPanelViewController.this.onQsExpansionStarted();
            NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
            if (notificationPanelViewController.mQsExpanded) {
                notificationPanelViewController.flingSettings(0.0f, 1, null, true);
            } else if (notificationPanelViewController.isQsExpansionEnabled()) {
                NotificationPanelViewController.this.mLockscreenGestureLogger.write(195, 0, 0);
                NotificationPanelViewController.this.flingSettings(0.0f, 0, null, true);
            }
        }
    }

    /* loaded from: classes.dex */
    public class OnConfigurationChangedListener extends PanelViewController.OnConfigurationChangedListener {
        public OnConfigurationChangedListener() {
            super();
        }
    }

    /* loaded from: classes.dex */
    public class OnEmptySpaceClickListener implements NotificationStackScrollLayout.OnEmptySpaceClickListener {
        public OnEmptySpaceClickListener() {
        }
    }

    /* loaded from: classes.dex */
    public class OnHeightChangedListener implements ExpandableView.OnHeightChangedListener {
        @Override // com.android.systemui.statusbar.notification.row.ExpandableView.OnHeightChangedListener
        public final void onReset(ExpandableNotificationRow expandableNotificationRow) {
        }

        public OnHeightChangedListener() {
        }

        @Override // com.android.systemui.statusbar.notification.row.ExpandableView.OnHeightChangedListener
        public final void onHeightChanged(ExpandableView expandableView, boolean z) {
            ExpandableNotificationRow expandableNotificationRow;
            if (expandableView != null || !NotificationPanelViewController.this.mQsExpanded) {
                if (z) {
                    NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
                    if (notificationPanelViewController.mInterpolatedDarkAmount == 0.0f) {
                        notificationPanelViewController.mAnimateNextPositionUpdate = true;
                    }
                }
                NotificationStackScrollLayoutController notificationStackScrollLayoutController = NotificationPanelViewController.this.mNotificationStackScrollLayoutController;
                Objects.requireNonNull(notificationStackScrollLayoutController);
                ExpandableView firstChildNotGone = notificationStackScrollLayoutController.mView.getFirstChildNotGone();
                if (firstChildNotGone instanceof ExpandableNotificationRow) {
                    expandableNotificationRow = (ExpandableNotificationRow) firstChildNotGone;
                } else {
                    expandableNotificationRow = null;
                }
                if (expandableNotificationRow != null && (expandableView == expandableNotificationRow || expandableNotificationRow.mNotificationParent == expandableNotificationRow)) {
                    NotificationPanelViewController.this.requestScrollerTopPaddingUpdate(false);
                }
                NotificationPanelViewController.this.requestPanelHeightUpdate();
            }
        }
    }

    /* loaded from: classes.dex */
    public class OnLayoutChangeListener extends PanelViewController.OnLayoutChangeListener {
        public OnLayoutChangeListener() {
            super();
        }

        @Override // com.android.systemui.statusbar.phone.PanelViewController.OnLayoutChangeListener, android.view.View.OnLayoutChangeListener
        public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            NotificationStackScrollLayoutController notificationStackScrollLayoutController;
            boolean z;
            NotificationPanelViewController notificationPanelViewController;
            QS qs;
            ScrimState[] values;
            DejankUtils.startDetectingBlockingIpcs("NVP#onLayout");
            super.onLayoutChange(view, i, i2, i3, i4, i5, i6, i7, i8);
            NotificationPanelViewController.this.updateMaxDisplayedNotifications(true);
            NotificationPanelViewController notificationPanelViewController2 = NotificationPanelViewController.this;
            Objects.requireNonNull(notificationPanelViewController2.mNotificationStackScrollLayoutController);
            if (notificationStackScrollLayoutController.mView.getWidth() == NotificationPanelViewController.this.mView.getWidth()) {
                z = true;
            } else {
                z = false;
            }
            notificationPanelViewController2.mIsFullWidth = z;
            ScrimController scrimController = notificationPanelViewController2.mScrimController;
            Objects.requireNonNull(scrimController);
            if (z != scrimController.mClipsQsScrim) {
                scrimController.mClipsQsScrim = z;
                for (ScrimState scrimState : ScrimState.values()) {
                    boolean z2 = scrimController.mClipsQsScrim;
                    Objects.requireNonNull(scrimState);
                    scrimState.mClipQsScrim = z2;
                }
                ScrimView scrimView = scrimController.mScrimBehind;
                if (scrimView != null) {
                    scrimView.enableBottomEdgeConcave(scrimController.mClipsQsScrim);
                }
                ScrimState scrimState2 = scrimController.mState;
                if (scrimState2 != ScrimState.UNINITIALIZED) {
                    scrimState2.prepare(scrimState2);
                    scrimController.applyAndDispatchState();
                }
            }
            NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = notificationPanelViewController2.mNotificationStackScrollLayoutController;
            Objects.requireNonNull(notificationStackScrollLayoutController2);
            NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController2.mView;
            Objects.requireNonNull(notificationStackScrollLayout);
            Objects.requireNonNull(notificationStackScrollLayout.mAmbientState);
            KeyguardStatusViewController keyguardStatusViewController = NotificationPanelViewController.this.mKeyguardStatusViewController;
            Objects.requireNonNull(keyguardStatusViewController);
            ((KeyguardStatusView) keyguardStatusViewController.mView).setPivotX(notificationPanelViewController.mView.getWidth() / 2);
            KeyguardStatusViewController keyguardStatusViewController2 = NotificationPanelViewController.this.mKeyguardStatusViewController;
            Objects.requireNonNull(keyguardStatusViewController2);
            KeyguardClockSwitchController keyguardClockSwitchController = keyguardStatusViewController2.mKeyguardClockSwitchController;
            Objects.requireNonNull(keyguardClockSwitchController);
            KeyguardClockSwitch keyguardClockSwitch = (KeyguardClockSwitch) keyguardClockSwitchController.mView;
            Objects.requireNonNull(keyguardClockSwitch);
            ((KeyguardStatusView) keyguardStatusViewController2.mView).setPivotY(keyguardClockSwitch.mClockView.getTextSize() * 0.34521484f);
            NotificationPanelViewController notificationPanelViewController3 = NotificationPanelViewController.this;
            int i9 = notificationPanelViewController3.mQsMaxExpansionHeight;
            if (notificationPanelViewController3.mQs != null) {
                NotificationPanelViewController.m103$$Nest$mupdateQSMinHeight(notificationPanelViewController3);
                NotificationPanelViewController notificationPanelViewController4 = NotificationPanelViewController.this;
                notificationPanelViewController4.mQsMaxExpansionHeight = notificationPanelViewController4.mQs.getDesiredHeight();
                NotificationPanelViewController notificationPanelViewController5 = NotificationPanelViewController.this;
                NotificationStackScrollLayoutController notificationStackScrollLayoutController3 = notificationPanelViewController5.mNotificationStackScrollLayoutController;
                int i10 = notificationPanelViewController5.mQsMaxExpansionHeight;
                Objects.requireNonNull(notificationStackScrollLayoutController3);
                NotificationStackScrollLayout notificationStackScrollLayout2 = notificationStackScrollLayoutController3.mView;
                Objects.requireNonNull(notificationStackScrollLayout2);
                notificationStackScrollLayout2.mMaxTopPadding = i10;
            }
            NotificationPanelViewController notificationPanelViewController6 = NotificationPanelViewController.this;
            Objects.requireNonNull(notificationPanelViewController6);
            notificationPanelViewController6.positionClockAndNotifications(false);
            NotificationPanelViewController notificationPanelViewController7 = NotificationPanelViewController.this;
            boolean z3 = notificationPanelViewController7.mQsExpanded;
            if (z3 && notificationPanelViewController7.mQsFullyExpanded) {
                notificationPanelViewController7.mQsExpansionHeight = notificationPanelViewController7.mQsMaxExpansionHeight;
                notificationPanelViewController7.requestScrollerTopPaddingUpdate(false);
                NotificationPanelViewController.this.requestPanelHeightUpdate();
                final NotificationPanelViewController notificationPanelViewController8 = NotificationPanelViewController.this;
                int i11 = notificationPanelViewController8.mQsMaxExpansionHeight;
                if (i11 != i9) {
                    ValueAnimator valueAnimator = notificationPanelViewController8.mQsSizeChangeAnimator;
                    if (valueAnimator != null) {
                        i9 = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                        notificationPanelViewController8.mQsSizeChangeAnimator.cancel();
                    }
                    ValueAnimator ofInt = ValueAnimator.ofInt(i9, i11);
                    notificationPanelViewController8.mQsSizeChangeAnimator = ofInt;
                    ofInt.setDuration(300L);
                    notificationPanelViewController8.mQsSizeChangeAnimator.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
                    notificationPanelViewController8.mQsSizeChangeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController.6
                        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                        public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                            NotificationPanelViewController.this.requestScrollerTopPaddingUpdate(false);
                            NotificationPanelViewController.this.requestPanelHeightUpdate();
                            NotificationPanelViewController.this.mQs.setHeightOverride(((Integer) NotificationPanelViewController.this.mQsSizeChangeAnimator.getAnimatedValue()).intValue());
                        }
                    });
                    notificationPanelViewController8.mQsSizeChangeAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController.7
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public final void onAnimationEnd(Animator animator) {
                            NotificationPanelViewController.this.mQsSizeChangeAnimator = null;
                        }
                    });
                    notificationPanelViewController8.mQsSizeChangeAnimator.start();
                }
            } else if (!z3 && notificationPanelViewController7.mQsExpansionAnimator == null) {
                notificationPanelViewController7.setQsExpansion(notificationPanelViewController7.mQsMinExpansionHeight + notificationPanelViewController7.mLastOverscroll);
            }
            NotificationPanelViewController notificationPanelViewController9 = NotificationPanelViewController.this;
            Objects.requireNonNull(notificationPanelViewController9);
            notificationPanelViewController9.updateExpandedHeight(notificationPanelViewController9.mExpandedHeight);
            NotificationPanelViewController notificationPanelViewController10 = NotificationPanelViewController.this;
            Objects.requireNonNull(notificationPanelViewController10);
            if (notificationPanelViewController10.mBarState == 1) {
                notificationPanelViewController10.mKeyguardStatusBarViewController.updateViewState();
            }
            notificationPanelViewController10.updateQsExpansion$1();
            NotificationPanelViewController notificationPanelViewController11 = NotificationPanelViewController.this;
            if (notificationPanelViewController11.mQsSizeChangeAnimator == null && (qs = notificationPanelViewController11.mQs) != null) {
                qs.setHeightOverride(qs.getDesiredHeight());
            }
            NotificationPanelViewController.m102$$Nest$mupdateMaxHeadsUpTranslation(NotificationPanelViewController.this);
            NotificationPanelViewController.this.updateGestureExclusionRect();
            Runnable runnable = NotificationPanelViewController.this.mExpandAfterLayoutRunnable;
            if (runnable != null) {
                runnable.run();
                NotificationPanelViewController.this.mExpandAfterLayoutRunnable = null;
            }
            DejankUtils.stopDetectingBlockingIpcs("NVP#onLayout");
        }
    }

    /* loaded from: classes.dex */
    public class OnOverscrollTopChangedListener implements NotificationStackScrollLayout.OnOverscrollTopChangedListener {
        public OnOverscrollTopChangedListener() {
        }
    }

    /* loaded from: classes.dex */
    public class SettingsChangeObserver extends ContentObserver {
        public SettingsChangeObserver(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public final void onChange(boolean z) {
            NotificationPanelViewController.m101$$Nest$mreInflateViews(NotificationPanelViewController.this);
        }
    }

    /* loaded from: classes.dex */
    public class StatusBarStateListener implements StatusBarStateController.StateListener {
        public StatusBarStateListener() {
        }

        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public final void onDozeAmountChanged(float f, float f2) {
            NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
            notificationPanelViewController.mInterpolatedDarkAmount = f2;
            notificationPanelViewController.mLinearDarkAmount = f;
            KeyguardStatusViewController keyguardStatusViewController = notificationPanelViewController.mKeyguardStatusViewController;
            Objects.requireNonNull(keyguardStatusViewController);
            KeyguardStatusView keyguardStatusView = (KeyguardStatusView) keyguardStatusViewController.mView;
            Objects.requireNonNull(keyguardStatusView);
            if (keyguardStatusView.mDarkAmount != f2) {
                keyguardStatusView.mDarkAmount = f2;
                KeyguardClockSwitch keyguardClockSwitch = keyguardStatusView.mClockView;
                Objects.requireNonNull(keyguardClockSwitch);
                keyguardClockSwitch.mDarkAmount = f2;
                ClockPlugin clockPlugin = keyguardClockSwitch.mClockPlugin;
                if (clockPlugin != null) {
                    clockPlugin.setDarkAmount(f2);
                }
                R$raw.fadeOut(keyguardStatusView.mMediaHostContainer, f2, true);
                keyguardStatusView.updateDark();
            }
            NotificationPanelViewController notificationPanelViewController2 = NotificationPanelViewController.this;
            KeyguardBottomAreaView keyguardBottomAreaView = notificationPanelViewController2.mKeyguardBottomArea;
            float f3 = notificationPanelViewController2.mInterpolatedDarkAmount;
            Objects.requireNonNull(keyguardBottomAreaView);
            if (f3 != keyguardBottomAreaView.mDarkAmount) {
                keyguardBottomAreaView.mDarkAmount = f3;
                keyguardBottomAreaView.dozeTimeTick();
            }
            NotificationPanelViewController notificationPanelViewController3 = NotificationPanelViewController.this;
            Objects.requireNonNull(notificationPanelViewController3);
            notificationPanelViewController3.positionClockAndNotifications(false);
        }

        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public final void onStateChanged(int i) {
            boolean z;
            boolean z2;
            QS qs;
            int i2;
            long j;
            long j2;
            boolean goingToFullShade = NotificationPanelViewController.this.mStatusBarStateController.goingToFullShade();
            boolean isKeyguardFadingAway = NotificationPanelViewController.this.mKeyguardStateController.isKeyguardFadingAway();
            NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
            int i3 = notificationPanelViewController.mBarState;
            if (i == 1) {
                z = true;
            } else {
                z = false;
            }
            if (notificationPanelViewController.mDozeParameters.shouldDelayKeyguardShow() && i3 == 0 && i == 1) {
                NotificationPanelViewController notificationPanelViewController2 = NotificationPanelViewController.this;
                KeyguardStatusViewController keyguardStatusViewController = notificationPanelViewController2.mKeyguardStatusViewController;
                KeyguardClockPositionAlgorithm.Result result = notificationPanelViewController2.mClockPositionResult;
                keyguardStatusViewController.updatePosition(result.clockX, result.clockYFullyDozing, result.clockScale, false);
            }
            NotificationPanelViewController notificationPanelViewController3 = NotificationPanelViewController.this;
            KeyguardStatusViewController keyguardStatusViewController2 = notificationPanelViewController3.mKeyguardStatusViewController;
            int i4 = notificationPanelViewController3.mBarState;
            Objects.requireNonNull(keyguardStatusViewController2);
            keyguardStatusViewController2.mKeyguardVisibilityHelper.setViewVisibility(i, isKeyguardFadingAway, goingToFullShade, i4);
            NotificationPanelViewController notificationPanelViewController4 = NotificationPanelViewController.this;
            CommunalHostViewController communalHostViewController = notificationPanelViewController4.mCommunalViewController;
            if (communalHostViewController != null) {
                communalHostViewController.mKeyguardVisibilityHelper.setViewVisibility(i, isKeyguardFadingAway, goingToFullShade, notificationPanelViewController4.mBarState);
            }
            NotificationPanelViewController.this.setKeyguardBottomAreaVisibility(i, goingToFullShade);
            NotificationPanelViewController notificationPanelViewController5 = NotificationPanelViewController.this;
            notificationPanelViewController5.mBarState = i;
            notificationPanelViewController5.mKeyguardShowing = z;
            if (i3 == 1 && (goingToFullShade || i == 2)) {
                if (notificationPanelViewController5.mKeyguardStateController.isKeyguardFadingAway()) {
                    j2 = NotificationPanelViewController.this.mKeyguardStateController.getKeyguardFadingAwayDelay();
                    j = NotificationPanelViewController.this.mKeyguardStateController.getShortenedFadingAwayDuration();
                } else {
                    j2 = 0;
                    j = 360;
                }
                final KeyguardStatusBarViewController keyguardStatusBarViewController = NotificationPanelViewController.this.mKeyguardStatusBarViewController;
                Objects.requireNonNull(keyguardStatusBarViewController);
                ValueAnimator ofFloat = ValueAnimator.ofFloat(((KeyguardStatusBarView) keyguardStatusBarViewController.mView).getAlpha(), 0.0f);
                ofFloat.addUpdateListener(keyguardStatusBarViewController.mAnimatorUpdateListener);
                ofFloat.setStartDelay(j2);
                ofFloat.setDuration(j);
                ofFloat.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
                ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.phone.KeyguardStatusBarViewController.7
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationEnd(Animator animator) {
                        ((KeyguardStatusBarView) KeyguardStatusBarViewController.this.mView).setVisibility(4);
                        ((KeyguardStatusBarView) KeyguardStatusBarViewController.this.mView).setAlpha(1.0f);
                        KeyguardStatusBarViewController.this.mKeyguardStatusBarAnimateAlpha = 1.0f;
                    }
                });
                ofFloat.start();
                NotificationPanelViewController.m103$$Nest$mupdateQSMinHeight(NotificationPanelViewController.this);
            } else if (i3 == 2 && i == 1) {
                notificationPanelViewController5.mKeyguardStatusBarViewController.animateKeyguardStatusBarIn();
                NotificationStackScrollLayoutController notificationStackScrollLayoutController = NotificationPanelViewController.this.mNotificationStackScrollLayoutController;
                Objects.requireNonNull(notificationStackScrollLayoutController);
                NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
                Objects.requireNonNull(notificationStackScrollLayout);
                notificationStackScrollLayout.mScroller.abortAnimation();
                notificationStackScrollLayout.setOwnScrollY(0, false);
                NotificationPanelViewController notificationPanelViewController6 = NotificationPanelViewController.this;
                if (!notificationPanelViewController6.mQsExpanded && notificationPanelViewController6.mShouldUseSplitNotificationShade) {
                    notificationPanelViewController6.mQs.animateHeaderSlidingOut();
                }
            } else {
                if (i3 == 0 && i == 1 && notificationPanelViewController5.mScreenOffAnimationController.isKeyguardShowDelayed()) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (!z2) {
                    KeyguardStatusBarViewController keyguardStatusBarViewController2 = NotificationPanelViewController.this.mKeyguardStatusBarViewController;
                    if (z) {
                        i2 = 0;
                    } else {
                        i2 = 4;
                    }
                    Objects.requireNonNull(keyguardStatusBarViewController2);
                    ((KeyguardStatusBarView) keyguardStatusBarViewController2.mView).setAlpha(1.0f);
                    ((KeyguardStatusBarView) keyguardStatusBarViewController2.mView).setVisibility(i2);
                }
                if (z) {
                    NotificationPanelViewController notificationPanelViewController7 = NotificationPanelViewController.this;
                    if (!(i3 == notificationPanelViewController7.mBarState || (qs = notificationPanelViewController7.mQs) == null)) {
                        qs.hideImmediately();
                    }
                }
            }
            NotificationPanelViewController.this.mKeyguardStatusBarViewController.updateForHeadsUp();
            if (z) {
                NotificationPanelViewController notificationPanelViewController8 = NotificationPanelViewController.this;
                Objects.requireNonNull(notificationPanelViewController8);
                notificationPanelViewController8.mKeyguardBottomArea.setDozing$2(notificationPanelViewController8.mDozing, false);
                boolean z3 = notificationPanelViewController8.mDozing;
            }
            NotificationPanelViewController.this.updateMaxDisplayedNotifications(false);
            NotificationPanelViewController notificationPanelViewController9 = NotificationPanelViewController.this;
            Objects.requireNonNull(notificationPanelViewController9);
            notificationPanelViewController9.mBottomAreaShadeAlphaAnimator.cancel();
            if (notificationPanelViewController9.mBarState == 2) {
                notificationPanelViewController9.mBottomAreaShadeAlphaAnimator.setFloatValues(notificationPanelViewController9.mBottomAreaShadeAlpha, 0.0f);
                notificationPanelViewController9.mBottomAreaShadeAlphaAnimator.start();
            } else {
                notificationPanelViewController9.mBottomAreaShadeAlpha = 1.0f;
            }
            NotificationPanelViewController.this.updateQsState();
        }
    }

    public final void collapsePanel(boolean z, boolean z2, float f) {
        boolean z3;
        if (!z || isFullyCollapsed()) {
            resetViews(false);
            setExpandedHeightInternal(getMaxPanelHeight() * 0.0f);
            z3 = false;
        } else {
            collapse(z2, f);
            z3 = true;
        }
        if (!z3) {
            PanelExpansionStateManager panelExpansionStateManager = this.mPanelExpansionStateManager;
            Objects.requireNonNull(panelExpansionStateManager);
            PanelExpansionStateManagerKt.access$stateToString(panelExpansionStateManager.state);
            PanelExpansionStateManagerKt.access$stateToString(0);
            if (panelExpansionStateManager.state != 0) {
                panelExpansionStateManager.updateStateInternal(0);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:14:0x001c  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x0029  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void flingSettings(float r9, int r10, final com.android.systemui.wmshell.WMShell$7$$ExternalSyntheticLambda1 r11, boolean r12) {
        /*
            r8 = this;
            r0 = 0
            r1 = 1
            if (r10 == 0) goto L_0x0012
            if (r10 == r1) goto L_0x000f
            com.android.systemui.plugins.qs.QS r2 = r8.mQs
            if (r2 == 0) goto L_0x000d
            r2.closeDetail()
        L_0x000d:
            r2 = r0
            goto L_0x0015
        L_0x000f:
            int r2 = r8.mQsMinExpansionHeight
            goto L_0x0014
        L_0x0012:
            int r2 = r8.mQsMaxExpansionHeight
        L_0x0014:
            float r2 = (float) r2
        L_0x0015:
            float r3 = r8.mQsExpansionHeight
            int r4 = (r2 > r3 ? 1 : (r2 == r3 ? 0 : -1))
            r5 = 0
            if (r4 != 0) goto L_0x0029
            if (r11 == 0) goto L_0x0021
            r11.run()
        L_0x0021:
            if (r10 == 0) goto L_0x0024
            goto L_0x0025
        L_0x0024:
            r1 = r5
        L_0x0025:
            r8.traceQsJank(r5, r1)
            return
        L_0x0029:
            if (r10 != 0) goto L_0x002d
            r10 = r1
            goto L_0x002e
        L_0x002d:
            r10 = r5
        L_0x002e:
            int r4 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1))
            if (r4 <= 0) goto L_0x0034
            if (r10 == 0) goto L_0x003a
        L_0x0034:
            int r4 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1))
            if (r4 >= 0) goto L_0x003d
            if (r10 == 0) goto L_0x003d
        L_0x003a:
            r9 = r0
            r4 = r1
            goto L_0x003e
        L_0x003d:
            r4 = r5
        L_0x003e:
            r6 = 2
            float[] r6 = new float[r6]
            r6[r5] = r3
            r6[r1] = r2
            android.animation.ValueAnimator r3 = android.animation.ValueAnimator.ofFloat(r6)
            if (r12 == 0) goto L_0x0056
            android.view.animation.PathInterpolator r9 = com.android.systemui.animation.Interpolators.TOUCH_RESPONSE
            r3.setInterpolator(r9)
            r6 = 368(0x170, double:1.82E-321)
            r3.setDuration(r6)
            goto L_0x005d
        L_0x0056:
            com.android.wm.shell.animation.FlingAnimationUtils r12 = r8.mFlingAnimationUtils
            float r6 = r8.mQsExpansionHeight
            r12.apply(r3, r6, r2, r9)
        L_0x005d:
            if (r4 == 0) goto L_0x0064
            r6 = 350(0x15e, double:1.73E-321)
            r3.setDuration(r6)
        L_0x0064:
            com.android.systemui.biometrics.AuthBiometricView$$ExternalSyntheticLambda1 r9 = new com.android.systemui.biometrics.AuthBiometricView$$ExternalSyntheticLambda1
            r9.<init>(r8, r1)
            r3.addUpdateListener(r9)
            com.android.systemui.statusbar.phone.NotificationPanelViewController$10 r9 = new com.android.systemui.statusbar.phone.NotificationPanelViewController$10
            r9.<init>()
            r3.addListener(r9)
            r8.mAnimatingQS = r1
            r3.start()
            r8.mQsExpansionAnimator = r3
            r8.mQsAnimatorExpand = r10
            float r9 = r8.computeQsExpansionFraction()
            int r9 = (r9 > r0 ? 1 : (r9 == r0 ? 0 : -1))
            if (r9 != 0) goto L_0x008a
            int r9 = (r2 > r0 ? 1 : (r2 == r0 ? 0 : -1))
            if (r9 != 0) goto L_0x008a
            goto L_0x008b
        L_0x008a:
            r1 = r5
        L_0x008b:
            r8.mQSAnimatingHiddenFromCollapsed = r1
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.NotificationPanelViewController.flingSettings(float, int, com.android.systemui.wmshell.WMShell$7$$ExternalSyntheticLambda1, boolean):void");
    }

    public final void launchCamera(boolean z, int i) {
        boolean z2;
        boolean z3;
        KeyguardAffordanceView keyguardAffordanceView;
        KeyguardAffordanceView keyguardAffordanceView2;
        float f;
        if (i == 1) {
            this.mLastCameraLaunchSource = "power_double_tap";
        } else if (i == 0) {
            this.mLastCameraLaunchSource = "wiggle_gesture";
        } else if (i == 2) {
            this.mLastCameraLaunchSource = "lift_to_launch_ml";
        } else {
            this.mLastCameraLaunchSource = "lockscreen_affordance";
        }
        if (!isFullyCollapsed()) {
            setLaunchingAffordance(true);
        } else {
            z = false;
        }
        KeyguardBottomAreaView keyguardBottomAreaView = this.mKeyguardBottomArea;
        Objects.requireNonNull(keyguardBottomAreaView);
        if (keyguardBottomAreaView.mCameraPreview != null) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.mAffordanceHasPreview = z2;
        KeyguardAffordanceHelper keyguardAffordanceHelper = this.mAffordanceHelper;
        if (this.mView.getLayoutDirection() == 1) {
            z3 = true;
        } else {
            z3 = false;
        }
        Objects.requireNonNull(keyguardAffordanceHelper);
        if (!keyguardAffordanceHelper.mSwipingInProgress) {
            if (z3) {
                keyguardAffordanceView = keyguardAffordanceHelper.mLeftIcon;
            } else {
                keyguardAffordanceView = keyguardAffordanceHelper.mRightIcon;
            }
            if (z3) {
                keyguardAffordanceView2 = keyguardAffordanceHelper.mRightIcon;
            } else {
                keyguardAffordanceView2 = keyguardAffordanceHelper.mLeftIcon;
            }
            keyguardAffordanceHelper.startSwiping(keyguardAffordanceView);
            if (keyguardAffordanceView.getVisibility() != 0) {
                z = false;
            }
            if (z) {
                keyguardAffordanceHelper.fling(0.0f, false, !z3);
                KeyguardAffordanceHelper.updateIcon(keyguardAffordanceView2, 0.0f, 0.0f, true, false, true, false);
                return;
            }
            ((KeyguardAffordanceHelperCallback) keyguardAffordanceHelper.mCallback).onAnimationToSideStarted(!z3, keyguardAffordanceHelper.mTranslation, 0.0f);
            if (z3) {
                f = ((KeyguardAffordanceHelperCallback) keyguardAffordanceHelper.mCallback).getMaxTranslationDistance();
            } else {
                f = ((KeyguardAffordanceHelperCallback) keyguardAffordanceHelper.mCallback).getMaxTranslationDistance();
            }
            keyguardAffordanceHelper.mTranslation = f;
            KeyguardAffordanceHelper.updateIcon(keyguardAffordanceView2, 0.0f, 0.0f, false, false, true, false);
            KeyguardAffordanceView.cancelAnimator(keyguardAffordanceView.mPreviewClipper);
            View view = keyguardAffordanceView.mPreviewView;
            if (view != null) {
                view.setClipBounds(null);
                keyguardAffordanceView.mPreviewView.setVisibility(0);
            }
            keyguardAffordanceView.mCircleRadius = keyguardAffordanceView.getMaxCircleSize();
            keyguardAffordanceView.setImageAlpha(0.0f, false);
            keyguardAffordanceView.invalidate();
            keyguardAffordanceHelper.mFlingEndListener.onAnimationEnd(null);
            keyguardAffordanceHelper.mAnimationEndRunnable.run();
        }
    }

    @Override // com.android.systemui.statusbar.phone.PanelViewController
    public final void onFlingEnd(boolean z) {
        setOverExpansionInternal(0.0f, false);
        setAnimator(null);
        this.mKeyguardStateController.notifyPanelFlingEnd();
        if (!z) {
            if (super.mInteractionJankMonitor != null) {
                InteractionJankMonitor.getInstance().end(0);
            }
            notifyExpandingFinished();
        } else if (super.mInteractionJankMonitor != null) {
            InteractionJankMonitor.getInstance().cancel(0);
        }
        updatePanelExpansionAndVisibility();
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mNotificationStackScrollLayoutController;
        Objects.requireNonNull(notificationStackScrollLayoutController);
        NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
        Objects.requireNonNull(notificationStackScrollLayout);
        notificationStackScrollLayout.mIsFlinging = false;
    }

    public final void resetViews(boolean z) {
        this.mIsLaunchTransitionFinished = false;
        this.mBlockTouches = false;
        if (!this.mLaunchingAffordance) {
            this.mAffordanceHelper.reset(false);
            this.mLastCameraLaunchSource = "lockscreen_affordance";
        }
        StatusBar statusBar = this.mStatusBar;
        Objects.requireNonNull(statusBar);
        statusBar.mGutsManager.closeAndSaveGuts(true, true, true, true);
        if (!z || isFullyCollapsed()) {
            ValueAnimator valueAnimator = this.mQsExpansionAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            setQsExpansion(this.mQsMinExpansionHeight);
        } else {
            ValueAnimator valueAnimator2 = this.mQsExpansionAnimator;
            if (valueAnimator2 != null) {
                if (this.mQsAnimatorExpand) {
                    float f = this.mQsExpansionHeight;
                    valueAnimator2.cancel();
                    setQsExpansion(f);
                }
            }
            flingSettings(0.0f, 2, null, false);
        }
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mNotificationStackScrollLayoutController;
        Objects.requireNonNull(notificationStackScrollLayoutController);
        notificationStackScrollLayoutController.mView.setOverScrollAmount(0.0f, true, z, !z);
        NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = this.mNotificationStackScrollLayoutController;
        Objects.requireNonNull(notificationStackScrollLayoutController2);
        NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController2.mView;
        Objects.requireNonNull(notificationStackScrollLayout);
        notificationStackScrollLayout.mScroller.abortAnimation();
        notificationStackScrollLayout.setOwnScrollY(0, false);
    }

    /* JADX WARN: Removed duplicated region for block: B:33:0x00ac  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00b9  */
    /* JADX WARN: Removed duplicated region for block: B:38:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void setTransitionToFullShadeAmount(float r6, boolean r7, long r8) {
        /*
            r5 = this;
            r0 = 1
            r1 = 0
            r2 = 0
            if (r7 == 0) goto L_0x001c
            boolean r7 = r5.mIsFullWidth
            if (r7 == 0) goto L_0x001c
            r3 = 448(0x1c0, double:2.213E-321)
            r5.mAnimateNextNotificationBounds = r0
            r5.mNotificationBoundsAnimationDuration = r3
            r5.mNotificationBoundsAnimationDelay = r8
            float r7 = r5.mQsTranslationForFullShadeTransition
            int r7 = (r7 > r2 ? 1 : (r7 == r2 ? 0 : -1))
            if (r7 <= 0) goto L_0x0019
            r7 = r0
            goto L_0x001a
        L_0x0019:
            r7 = r1
        L_0x001a:
            r5.mIsQsTranslationResetAnimator = r7
        L_0x001c:
            int r7 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1))
            if (r7 <= 0) goto L_0x0090
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController r7 = r5.mNotificationStackScrollLayoutController
            int r7 = r7.getVisibleNotificationCount()
            if (r7 != 0) goto L_0x0053
            com.android.systemui.media.MediaDataManager r7 = r5.mMediaDataManager
            boolean r7 = r7.hasActiveMedia()
            if (r7 != 0) goto L_0x0053
            com.android.systemui.plugins.qs.QS r7 = r5.mQs
            if (r7 == 0) goto L_0x0090
            float r7 = r5.getQSEdgePosition()
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController r8 = r5.mNotificationStackScrollLayoutController
            java.util.Objects.requireNonNull(r8)
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout r8 = r8.mView
            java.util.Objects.requireNonNull(r8)
            int r8 = r8.mTopPadding
            float r8 = (float) r8
            float r7 = r7 - r8
            com.android.systemui.plugins.qs.QS r8 = r5.mQs
            android.view.View r8 = r8.getHeader()
            int r8 = r8.getHeight()
            float r8 = (float) r8
            float r7 = r7 + r8
            goto L_0x0091
        L_0x0053:
            float r7 = r5.getQSEdgePosition()
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController r8 = r5.mNotificationStackScrollLayoutController
            java.util.Objects.requireNonNull(r8)
            com.android.systemui.media.KeyguardMediaController r9 = r8.mKeyguardMediaController
            java.util.Objects.requireNonNull(r9)
            com.android.systemui.statusbar.notification.stack.MediaContainerView r9 = r9.singlePaneContainer
            if (r9 == 0) goto L_0x0083
            int r3 = r9.getHeight()
            if (r3 == 0) goto L_0x0083
            com.android.systemui.statusbar.SysuiStatusBarStateController r3 = r8.mStatusBarStateController
            int r3 = r3.getState()
            if (r3 == r0) goto L_0x0074
            goto L_0x0083
        L_0x0074:
            int r9 = r9.getHeight()
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout r8 = r8.mView
            java.util.Objects.requireNonNull(r8)
            int r1 = r8.mGapHeight
            int r8 = r8.mPaddingBetweenElements
            int r1 = r1 + r8
            int r1 = r1 + r9
        L_0x0083:
            float r8 = (float) r1
            float r7 = r7 + r8
            boolean r8 = r5.isOnKeyguard()
            if (r8 == 0) goto L_0x0091
            int r8 = r5.mLockscreenNotificationQSPadding
            float r8 = (float) r8
            float r7 = r7 - r8
            goto L_0x0091
        L_0x0090:
            r7 = r2
        L_0x0091:
            android.view.animation.PathInterpolator r8 = com.android.systemui.animation.Interpolators.FAST_OUT_SLOW_IN
            int r9 = r5.mDistanceForQSFullShadeTransition
            float r9 = (float) r9
            float r6 = r6 / r9
            float r6 = android.util.MathUtils.saturate(r6)
            float r6 = r8.getInterpolation(r6)
            r5.mTransitioningToFullShadeProgress = r6
            float r6 = android.util.MathUtils.lerp(r2, r7, r6)
            int r6 = (int) r6
            float r7 = r5.mTransitioningToFullShadeProgress
            int r7 = (r7 > r2 ? 1 : (r7 == r2 ? 0 : -1))
            if (r7 <= 0) goto L_0x00b0
            int r6 = java.lang.Math.max(r0, r6)
        L_0x00b0:
            r5.mTransitionToFullShadeQSPosition = r6
            r5.updateQsExpansion$1()
            com.android.systemui.communal.CommunalHostViewController r6 = r5.mCommunalViewController
            if (r6 == 0) goto L_0x00c0
            float r5 = r5.mTransitioningToFullShadeProgress
            r6.mShadeExpansion = r5
            r6.updateCommunalViewOccluded()
        L_0x00c0:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.NotificationPanelViewController.setTransitionToFullShadeAmount(float, boolean, long):void");
    }

    public final void showAodUi() {
        setDozing$1(true, false);
        this.mStatusBarStateController.setUpcomingState();
        this.mEntryManager.updateNotifications("showAodUi");
        this.mStatusBarStateListener.onStateChanged(1);
        this.mStatusBarStateListener.onDozeAmountChanged(1.0f, 1.0f);
        setExpandedHeightInternal(getMaxPanelHeight() * 1.0f);
    }

    /* JADX WARN: Removed duplicated region for block: B:49:0x0121  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0123  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x013c  */
    /* JADX WARN: Removed duplicated region for block: B:54:0x0140  */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0146  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0155  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0150 A[ADDED_TO_REGION, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:93:0x017c A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateMaxDisplayedNotifications(boolean r15) {
        /*
            Method dump skipped, instructions count: 496
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.NotificationPanelViewController.updateMaxDisplayedNotifications(boolean):void");
    }

    static {
        LaunchAnimator.Timings timings = ActivityLaunchAnimator.TIMINGS;
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0381  */
    /* JADX WARN: Removed duplicated region for block: B:20:0x044b  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0469  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x04cd  */
    /* JADX WARN: Removed duplicated region for block: B:29:0x04d8  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x0516  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x052e  */
    /* JADX WARN: Removed duplicated region for block: B:38:0x056b  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x056e  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x060b  */
    /* JADX WARN: Removed duplicated region for block: B:45:0x06d8  */
    /* JADX WARN: Type inference failed for: r5v37, types: [com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda2] */
    /* JADX WARN: Type inference failed for: r9v10, types: [com.android.systemui.statusbar.phone.NotificationPanelViewController$3] */
    /* JADX WARN: Type inference failed for: r9v11, types: [com.android.systemui.statusbar.phone.NotificationPanelViewController$8] */
    /* JADX WARN: Type inference failed for: r9v12, types: [com.android.systemui.statusbar.phone.NotificationPanelViewController$13] */
    /* JADX WARN: Type inference failed for: r9v14, types: [com.android.systemui.statusbar.phone.NotificationPanelViewController$16] */
    /* JADX WARN: Type inference failed for: r9v8, types: [com.android.systemui.statusbar.phone.NotificationPanelViewController$1] */
    /* JADX WARN: Type inference failed for: r9v9, types: [com.android.systemui.statusbar.phone.NotificationPanelViewController$2] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public NotificationPanelViewController(com.android.systemui.statusbar.phone.NotificationPanelView r18, android.content.res.Resources r19, android.os.Handler r20, android.view.LayoutInflater r21, com.android.systemui.flags.FeatureFlags r22, final com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator r23, com.android.systemui.statusbar.PulseExpansionHandler r24, com.android.systemui.statusbar.notification.DynamicPrivacyController r25, com.android.systemui.statusbar.phone.KeyguardBypassController r26, com.android.systemui.plugins.FalsingManager r27, com.android.systemui.classifier.FalsingCollector r28, com.android.systemui.statusbar.NotificationLockscreenUserManager r29, com.android.systemui.statusbar.notification.NotificationEntryManager r30, com.android.systemui.communal.CommunalStateController r31, com.android.systemui.statusbar.policy.KeyguardStateController r32, com.android.systemui.plugins.statusbar.StatusBarStateController r33, com.android.systemui.statusbar.window.StatusBarWindowStateController r34, com.android.systemui.statusbar.NotificationShadeWindowController r35, com.android.systemui.doze.DozeLog r36, com.android.systemui.statusbar.phone.DozeParameters r37, com.android.systemui.statusbar.CommandQueue r38, com.android.systemui.statusbar.VibratorHelper r39, com.android.internal.util.LatencyTracker r40, android.os.PowerManager r41, android.view.accessibility.AccessibilityManager r42, int r43, com.android.keyguard.KeyguardUpdateMonitor r44, com.android.systemui.communal.CommunalSourceMonitor r45, com.android.internal.logging.MetricsLogger r46, android.app.ActivityManager r47, com.android.systemui.statusbar.policy.ConfigurationController r48, javax.inject.Provider<com.android.wm.shell.animation.FlingAnimationUtils.Builder> r49, com.android.systemui.statusbar.phone.StatusBarTouchableRegionManager r50, com.android.systemui.statusbar.notification.ConversationNotificationManager r51, com.android.systemui.media.MediaHierarchyManager r52, com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager r53, com.android.systemui.statusbar.phone.NotificationsQSContainerController r54, final com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController r55, com.android.keyguard.dagger.KeyguardStatusViewComponent.Factory r56, com.android.keyguard.dagger.KeyguardQsUserSwitchComponent.Factory r57, com.android.keyguard.dagger.KeyguardUserSwitcherComponent.Factory r58, com.android.keyguard.dagger.KeyguardStatusBarViewComponent.Factory r59, com.android.systemui.communal.dagger.CommunalViewComponent.Factory r60, com.android.systemui.statusbar.LockscreenShadeTransitionController r61, com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy r62, com.android.systemui.statusbar.phone.NotificationIconAreaController r63, com.android.systemui.biometrics.AuthController r64, com.android.systemui.statusbar.phone.ScrimController r65, android.os.UserManager r66, com.android.systemui.media.MediaDataManager r67, com.android.systemui.statusbar.NotificationShadeDepthController r68, com.android.systemui.statusbar.notification.stack.AmbientState r69, com.android.keyguard.LockIconViewController r70, com.android.systemui.media.KeyguardMediaController r71, com.android.systemui.statusbar.events.PrivacyDotViewController r72, com.android.systemui.statusbar.phone.TapAgainViewController r73, com.android.systemui.navigationbar.NavigationModeController r74, com.android.systemui.fragments.FragmentService r75, android.content.ContentResolver r76, com.android.systemui.wallet.controller.QuickAccessWalletController r77, com.android.systemui.qrcodescanner.controller.QRCodeScannerController r78, com.android.systemui.screenrecord.RecordingController r79, java.util.concurrent.Executor r80, com.android.systemui.util.settings.SecureSettings r81, com.android.systemui.statusbar.phone.SplitShadeHeaderController r82, com.android.systemui.statusbar.phone.ScreenOffAnimationController r83, com.android.systemui.statusbar.phone.LockscreenGestureLogger r84, com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager r85, com.android.systemui.statusbar.NotificationRemoteInputManager r86, java.util.Optional<com.android.systemui.unfold.SysUIUnfoldComponent> r87, com.android.systemui.controls.dagger.ControlsComponent r88, com.android.internal.jank.InteractionJankMonitor r89, com.android.systemui.statusbar.QsFrameTranslateController r90, com.android.systemui.model.SysUiState r91, com.android.systemui.keyguard.KeyguardUnlockAnimationController r92) {
        /*
            Method dump skipped, instructions count: 1812
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.NotificationPanelViewController.<init>(com.android.systemui.statusbar.phone.NotificationPanelView, android.content.res.Resources, android.os.Handler, android.view.LayoutInflater, com.android.systemui.flags.FeatureFlags, com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator, com.android.systemui.statusbar.PulseExpansionHandler, com.android.systemui.statusbar.notification.DynamicPrivacyController, com.android.systemui.statusbar.phone.KeyguardBypassController, com.android.systemui.plugins.FalsingManager, com.android.systemui.classifier.FalsingCollector, com.android.systemui.statusbar.NotificationLockscreenUserManager, com.android.systemui.statusbar.notification.NotificationEntryManager, com.android.systemui.communal.CommunalStateController, com.android.systemui.statusbar.policy.KeyguardStateController, com.android.systemui.plugins.statusbar.StatusBarStateController, com.android.systemui.statusbar.window.StatusBarWindowStateController, com.android.systemui.statusbar.NotificationShadeWindowController, com.android.systemui.doze.DozeLog, com.android.systemui.statusbar.phone.DozeParameters, com.android.systemui.statusbar.CommandQueue, com.android.systemui.statusbar.VibratorHelper, com.android.internal.util.LatencyTracker, android.os.PowerManager, android.view.accessibility.AccessibilityManager, int, com.android.keyguard.KeyguardUpdateMonitor, com.android.systemui.communal.CommunalSourceMonitor, com.android.internal.logging.MetricsLogger, android.app.ActivityManager, com.android.systemui.statusbar.policy.ConfigurationController, javax.inject.Provider, com.android.systemui.statusbar.phone.StatusBarTouchableRegionManager, com.android.systemui.statusbar.notification.ConversationNotificationManager, com.android.systemui.media.MediaHierarchyManager, com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager, com.android.systemui.statusbar.phone.NotificationsQSContainerController, com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController, com.android.keyguard.dagger.KeyguardStatusViewComponent$Factory, com.android.keyguard.dagger.KeyguardQsUserSwitchComponent$Factory, com.android.keyguard.dagger.KeyguardUserSwitcherComponent$Factory, com.android.keyguard.dagger.KeyguardStatusBarViewComponent$Factory, com.android.systemui.communal.dagger.CommunalViewComponent$Factory, com.android.systemui.statusbar.LockscreenShadeTransitionController, com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy, com.android.systemui.statusbar.phone.NotificationIconAreaController, com.android.systemui.biometrics.AuthController, com.android.systemui.statusbar.phone.ScrimController, android.os.UserManager, com.android.systemui.media.MediaDataManager, com.android.systemui.statusbar.NotificationShadeDepthController, com.android.systemui.statusbar.notification.stack.AmbientState, com.android.keyguard.LockIconViewController, com.android.systemui.media.KeyguardMediaController, com.android.systemui.statusbar.events.PrivacyDotViewController, com.android.systemui.statusbar.phone.TapAgainViewController, com.android.systemui.navigationbar.NavigationModeController, com.android.systemui.fragments.FragmentService, android.content.ContentResolver, com.android.systemui.wallet.controller.QuickAccessWalletController, com.android.systemui.qrcodescanner.controller.QRCodeScannerController, com.android.systemui.screenrecord.RecordingController, java.util.concurrent.Executor, com.android.systemui.util.settings.SecureSettings, com.android.systemui.statusbar.phone.SplitShadeHeaderController, com.android.systemui.statusbar.phone.ScreenOffAnimationController, com.android.systemui.statusbar.phone.LockscreenGestureLogger, com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager, com.android.systemui.statusbar.NotificationRemoteInputManager, java.util.Optional, com.android.systemui.controls.dagger.ControlsComponent, com.android.internal.jank.InteractionJankMonitor, com.android.systemui.statusbar.QsFrameTranslateController, com.android.systemui.model.SysUiState, com.android.systemui.keyguard.KeyguardUnlockAnimationController):void");
    }

    public final void animateToFullShade(long j) {
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mNotificationStackScrollLayoutController;
        Objects.requireNonNull(notificationStackScrollLayoutController);
        NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
        Objects.requireNonNull(notificationStackScrollLayout);
        notificationStackScrollLayout.mGoToFullShadeNeedsAnimation = true;
        notificationStackScrollLayout.mGoToFullShadeDelay = j;
        notificationStackScrollLayout.mNeedsAnimation = true;
        notificationStackScrollLayout.requestChildrenUpdate();
        this.mView.requestLayout();
        this.mAnimateNextPositionUpdate = true;
    }

    public final void applyQSClippingImmediately(int i, int i2, int i3, int i4, boolean z) {
        boolean z2;
        Rect rect;
        int i5;
        float f;
        boolean z3;
        float f2;
        int i6 = this.mScrimCornerRadius;
        if (this.mIsFullWidth) {
            this.mKeyguardStatusAreaClipBounds.set(i, i2, i3, i4);
            if (this.mRecordingController.isRecording()) {
                f2 = 0.0f;
            } else {
                f2 = this.mScreenCornerRadius;
            }
            float f3 = this.mScrimCornerRadius;
            i6 = (int) MathUtils.lerp(f2, f3, Math.min(i2 / f3, 1.0f));
            z2 = z;
        } else {
            z2 = false;
        }
        if (this.mQs != null) {
            PulseExpansionHandler pulseExpansionHandler = this.mPulseExpansionHandler;
            Objects.requireNonNull(pulseExpansionHandler);
            boolean z4 = pulseExpansionHandler.isExpanding;
            if (this.mTransitioningToFullShadeProgress > 0.0f || z4 || (this.mQsClippingAnimation != null && (this.mIsQsTranslationResetAnimator || this.mIsPulseExpansionResetAnimator))) {
                if (z4 || this.mIsPulseExpansionResetAnimator) {
                    f = Math.max(0.0f, (i2 - this.mQs.getHeader().getHeight()) / 2.0f);
                } else if (!this.mShouldUseSplitNotificationShade) {
                    f = (i2 - this.mQs.getHeader().getHeight()) * 0.175f;
                }
                this.mQsTranslationForFullShadeTransition = f;
                this.mQsFrameTranslateController.translateQsFrame();
                float translationY = this.mQsFrame.getTranslationY();
                int i7 = (int) (i2 - translationY);
                this.mQsClipTop = i7;
                int i8 = (int) (i4 - translationY);
                this.mQsClipBottom = i8;
                this.mQsVisible = z;
                QS qs = this.mQs;
                if (z || this.mShouldUseSplitNotificationShade) {
                    z3 = false;
                } else {
                    z3 = true;
                }
                qs.setFancyClipping(i7, i8, i6, z3);
            }
            f = 0.0f;
            this.mQsTranslationForFullShadeTransition = f;
            this.mQsFrameTranslateController.translateQsFrame();
            float translationY2 = this.mQsFrame.getTranslationY();
            int i72 = (int) (i2 - translationY2);
            this.mQsClipTop = i72;
            int i82 = (int) (i4 - translationY2);
            this.mQsClipBottom = i82;
            this.mQsVisible = z;
            QS qs2 = this.mQs;
            if (z) {
            }
            z3 = false;
            qs2.setFancyClipping(i72, i82, i6, z3);
        }
        KeyguardStatusViewController keyguardStatusViewController = this.mKeyguardStatusViewController;
        if (z2) {
            rect = this.mKeyguardStatusAreaClipBounds;
        } else {
            rect = null;
        }
        Objects.requireNonNull(keyguardStatusViewController);
        if (rect != null) {
            keyguardStatusViewController.mClipBounds.set(rect.left, (int) (rect.top - ((KeyguardStatusView) keyguardStatusViewController.mView).getY()), rect.right, (int) (rect.bottom - ((KeyguardStatusView) keyguardStatusViewController.mView).getY()));
            ((KeyguardStatusView) keyguardStatusViewController.mView).setClipBounds(keyguardStatusViewController.mClipBounds);
        } else {
            ((KeyguardStatusView) keyguardStatusViewController.mView).setClipBounds(null);
        }
        if (z || !this.mShouldUseSplitNotificationShade) {
            this.mScrimController.setNotificationsBounds(i, i2, i3, i4);
        } else {
            this.mScrimController.setNotificationsBounds(0.0f, 0.0f, 0.0f, 0.0f);
        }
        if (this.mShouldUseSplitNotificationShade) {
            KeyguardStatusBarViewController keyguardStatusBarViewController = this.mKeyguardStatusBarViewController;
            Objects.requireNonNull(keyguardStatusBarViewController);
            KeyguardStatusBarView keyguardStatusBarView = (KeyguardStatusBarView) keyguardStatusBarViewController.mView;
            Objects.requireNonNull(keyguardStatusBarView);
            if (keyguardStatusBarView.mTopClipping != 0) {
                keyguardStatusBarView.mTopClipping = 0;
                keyguardStatusBarView.mClipRect.set(0, 0, keyguardStatusBarView.getWidth(), keyguardStatusBarView.getHeight());
                keyguardStatusBarView.setClipBounds(keyguardStatusBarView.mClipRect);
            }
        } else {
            KeyguardStatusBarViewController keyguardStatusBarViewController2 = this.mKeyguardStatusBarViewController;
            Objects.requireNonNull(keyguardStatusBarViewController2);
            KeyguardStatusBarView keyguardStatusBarView2 = (KeyguardStatusBarView) keyguardStatusBarViewController2.mView;
            int top = i2 - keyguardStatusBarView2.getTop();
            if (top != keyguardStatusBarView2.mTopClipping) {
                keyguardStatusBarView2.mTopClipping = top;
                keyguardStatusBarView2.mClipRect.set(0, top, keyguardStatusBarView2.getWidth(), keyguardStatusBarView2.getHeight());
                keyguardStatusBarView2.setClipBounds(keyguardStatusBarView2.mClipRect);
            }
        }
        ScrimController scrimController = this.mScrimController;
        Objects.requireNonNull(scrimController);
        ScrimView scrimView = scrimController.mScrimBehind;
        if (!(scrimView == null || scrimController.mNotificationsScrim == null)) {
            scrimView.setCornerRadius(i6);
            scrimController.mNotificationsScrim.setCornerRadius(i6);
        }
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mNotificationStackScrollLayoutController;
        Objects.requireNonNull(notificationStackScrollLayoutController);
        int left = i - notificationStackScrollLayoutController.mView.getLeft();
        NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = this.mNotificationStackScrollLayoutController;
        Objects.requireNonNull(notificationStackScrollLayoutController2);
        int left2 = i3 - notificationStackScrollLayoutController2.mView.getLeft();
        NotificationStackScrollLayoutController notificationStackScrollLayoutController3 = this.mNotificationStackScrollLayoutController;
        Objects.requireNonNull(notificationStackScrollLayoutController3);
        int top2 = i2 - notificationStackScrollLayoutController3.mView.getTop();
        if (this.mShouldUseSplitNotificationShade) {
            i5 = i6;
        } else {
            i5 = 0;
        }
        NotificationStackScrollLayoutController notificationStackScrollLayoutController4 = this.mNotificationStackScrollLayoutController;
        Objects.requireNonNull(notificationStackScrollLayoutController4);
        NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController4.mView;
        Objects.requireNonNull(notificationStackScrollLayout);
        if (notificationStackScrollLayout.mRoundedRectClippingLeft == left && notificationStackScrollLayout.mRoundedRectClippingRight == left2 && notificationStackScrollLayout.mRoundedRectClippingBottom == i4 && notificationStackScrollLayout.mRoundedRectClippingTop == top2) {
            float[] fArr = notificationStackScrollLayout.mBgCornerRadii;
            if (fArr[0] == i6 && fArr[5] == i5) {
                return;
            }
        }
        notificationStackScrollLayout.mRoundedRectClippingLeft = left;
        notificationStackScrollLayout.mRoundedRectClippingTop = top2;
        notificationStackScrollLayout.mRoundedRectClippingBottom = i4;
        notificationStackScrollLayout.mRoundedRectClippingRight = left2;
        float[] fArr2 = notificationStackScrollLayout.mBgCornerRadii;
        float f4 = i6;
        fArr2[0] = f4;
        fArr2[1] = f4;
        fArr2[2] = f4;
        fArr2[3] = f4;
        float f5 = i5;
        fArr2[4] = f5;
        fArr2[5] = f5;
        fArr2[6] = f5;
        fArr2[7] = f5;
        notificationStackScrollLayout.mRoundedClipPath.reset();
        notificationStackScrollLayout.mRoundedClipPath.addRoundRect(left, top2, left2, i4, notificationStackScrollLayout.mBgCornerRadii, Path.Direction.CW);
        if (notificationStackScrollLayout.mShouldUseRoundedRectClipping) {
            notificationStackScrollLayout.invalidate();
        }
    }

    public final int calculatePanelHeightQsExpanded() {
        int i;
        NotificationStackScrollLayoutController notificationStackScrollLayoutController;
        int height = this.mNotificationStackScrollLayoutController.getHeight();
        NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = this.mNotificationStackScrollLayoutController;
        Objects.requireNonNull(notificationStackScrollLayoutController2);
        int emptyBottomMargin = height - notificationStackScrollLayoutController2.mView.getEmptyBottomMargin();
        NotificationStackScrollLayoutController notificationStackScrollLayoutController3 = this.mNotificationStackScrollLayoutController;
        Objects.requireNonNull(notificationStackScrollLayoutController3);
        NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController3.mView;
        Objects.requireNonNull(notificationStackScrollLayout);
        float f = emptyBottomMargin - notificationStackScrollLayout.mTopPadding;
        if (this.mNotificationStackScrollLayoutController.getNotGoneChildCount() == 0) {
            NotificationStackScrollLayoutController notificationStackScrollLayoutController4 = this.mNotificationStackScrollLayoutController;
            Objects.requireNonNull(notificationStackScrollLayoutController4);
            if (notificationStackScrollLayoutController4.mShowEmptyShadeView) {
                NotificationStackScrollLayoutController notificationStackScrollLayoutController5 = this.mNotificationStackScrollLayoutController;
                Objects.requireNonNull(notificationStackScrollLayoutController5);
                NotificationStackScrollLayout notificationStackScrollLayout2 = notificationStackScrollLayoutController5.mView;
                Objects.requireNonNull(notificationStackScrollLayout2);
                f = notificationStackScrollLayout2.mEmptyShadeView.getHeight();
            }
        }
        int i2 = this.mQsMaxExpansionHeight;
        ValueAnimator valueAnimator = this.mQsSizeChangeAnimator;
        if (valueAnimator != null) {
            i2 = ((Integer) valueAnimator.getAnimatedValue()).intValue();
        }
        if (this.mBarState == 1) {
            i = this.mClockPositionResult.stackScrollerPadding;
        } else {
            i = 0;
        }
        float max = Math.max(i2, i) + f;
        NotificationStackScrollLayoutController notificationStackScrollLayoutController6 = this.mNotificationStackScrollLayoutController;
        Objects.requireNonNull(notificationStackScrollLayoutController6);
        NotificationStackScrollLayout notificationStackScrollLayout3 = notificationStackScrollLayoutController6.mView;
        Objects.requireNonNull(notificationStackScrollLayout3);
        float f2 = max + notificationStackScrollLayout3.mTopPaddingOverflow;
        if (f2 > this.mNotificationStackScrollLayoutController.getHeight()) {
            float f3 = i2;
            Objects.requireNonNull(this.mNotificationStackScrollLayoutController);
            f2 = Math.max(notificationStackScrollLayoutController.mView.getLayoutMinHeight() + f3, this.mNotificationStackScrollLayoutController.getHeight());
        }
        return (int) f2;
    }

    public final int calculatePanelHeightShade() {
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mNotificationStackScrollLayoutController;
        Objects.requireNonNull(notificationStackScrollLayoutController);
        int height = this.mNotificationStackScrollLayoutController.getHeight() - notificationStackScrollLayoutController.mView.getEmptyBottomMargin();
        if (this.mBarState != 1) {
            return height;
        }
        KeyguardClockPositionAlgorithm keyguardClockPositionAlgorithm = this.mClockPositionAlgorithm;
        Objects.requireNonNull(keyguardClockPositionAlgorithm);
        int i = keyguardClockPositionAlgorithm.mKeyguardStatusHeight;
        NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = this.mNotificationStackScrollLayoutController;
        Objects.requireNonNull(notificationStackScrollLayoutController2);
        NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController2.mView;
        Objects.requireNonNull(notificationStackScrollLayout);
        return Math.max(height, i + notificationStackScrollLayout.mIntrinsicContentHeight);
    }

    public final int calculateQsBottomPosition(float f) {
        if (this.mTransitioningToFullShadeProgress > 0.0f) {
            return this.mTransitionToFullShadeQSPosition;
        }
        int qsMinExpansionHeight = this.mQs.getQsMinExpansionHeight() + ((int) getHeaderTranslation());
        if (f != 0.0d) {
            return (int) MathUtils.lerp(qsMinExpansionHeight, this.mQs.getDesiredHeight(), f);
        }
        return qsMinExpansionHeight;
    }

    public final float computeQsExpansionFraction() {
        if (this.mQSAnimatingHiddenFromCollapsed) {
            return 0.0f;
        }
        float f = this.mQsExpansionHeight;
        int i = this.mQsMinExpansionHeight;
        return Math.min(1.0f, (f - i) / (this.mQsMaxExpansionHeight - i));
    }

    public final String determineAccessibilityPaneTitle() {
        QS qs = this.mQs;
        if (qs != null && qs.isCustomizing()) {
            return this.mResources.getString(2131951712);
        }
        if (this.mQsExpansionHeight != 0.0f && this.mQsFullyExpanded) {
            return this.mResources.getString(2131951711);
        }
        if (this.mBarState == 1) {
            return this.mResources.getString(2131951708);
        }
        return this.mResources.getString(2131951710);
    }

    public final ViewPropertyAnimator fadeOut(long j, long j2, Runnable runnable) {
        return this.mView.animate().alpha(0.0f).setStartDelay(j).setDuration(j2).setInterpolator(Interpolators.ALPHA_OUT).withLayer().withEndAction(runnable);
    }

    public final float getFadeoutAlpha() {
        int i = this.mQsMinExpansionHeight;
        if (i == 0) {
            return 1.0f;
        }
        return (float) Math.pow(Math.max(0.0f, Math.min(this.mExpandedHeight / i, 1.0f)), 0.75d);
    }

    public final float getHeaderTranslation() {
        if (this.mBarState == 1 && !this.mKeyguardBypassController.getBypassEnabled()) {
            return -this.mQs.getQsMinExpansionHeight();
        }
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mNotificationStackScrollLayoutController;
        float f = this.mExpandedHeight;
        Objects.requireNonNull(notificationStackScrollLayoutController);
        NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
        Objects.requireNonNull(notificationStackScrollLayout);
        float appearEndPosition = notificationStackScrollLayout.getAppearEndPosition();
        float appearStartPosition = notificationStackScrollLayout.getAppearStartPosition();
        float f2 = (f - appearStartPosition) / (appearEndPosition - appearStartPosition);
        float f3 = -this.mQsExpansionHeight;
        if (!this.mShouldUseSplitNotificationShade && this.mBarState == 0) {
            f3 *= 0.175f;
        }
        if (this.mKeyguardBypassController.getBypassEnabled() && isOnKeyguard()) {
            NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = this.mNotificationStackScrollLayoutController;
            Objects.requireNonNull(notificationStackScrollLayoutController2);
            NotificationStackScrollLayout notificationStackScrollLayout2 = notificationStackScrollLayoutController2.mView;
            Objects.requireNonNull(notificationStackScrollLayout2);
            AmbientState ambientState = notificationStackScrollLayout2.mAmbientState;
            Objects.requireNonNull(ambientState);
            float f4 = ambientState.mPulseHeight;
            if (f4 == 100000.0f) {
                f4 = 0.0f;
            }
            f2 = MathUtils.smoothStep(0.0f, notificationStackScrollLayout2.mIntrinsicPadding, f4);
            f3 = -this.mQs.getQsMinExpansionHeight();
        }
        return Math.min(0.0f, MathUtils.lerp(f3, 0.0f, Math.min(1.0f, f2)));
    }

    public final int getKeyguardNotificationStaticPadding() {
        if (!this.mKeyguardShowing) {
            return 0;
        }
        if (!this.mKeyguardBypassController.getBypassEnabled()) {
            return this.mClockPositionResult.stackScrollerPadding;
        }
        int i = this.mHeadsUpInset;
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mNotificationStackScrollLayoutController;
        Objects.requireNonNull(notificationStackScrollLayoutController);
        NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
        Objects.requireNonNull(notificationStackScrollLayout);
        if (!notificationStackScrollLayout.mAmbientState.isPulseExpanding()) {
            return i;
        }
        int i2 = this.mClockPositionResult.stackScrollerPadding;
        NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = this.mNotificationStackScrollLayoutController;
        Objects.requireNonNull(notificationStackScrollLayoutController2);
        NotificationStackScrollLayout notificationStackScrollLayout2 = notificationStackScrollLayoutController2.mView;
        Objects.requireNonNull(notificationStackScrollLayout2);
        AmbientState ambientState = notificationStackScrollLayout2.mAmbientState;
        Objects.requireNonNull(ambientState);
        float f = ambientState.mPulseHeight;
        if (f == 100000.0f) {
            f = 0.0f;
        }
        return (int) MathUtils.lerp(i, i2, MathUtils.smoothStep(0.0f, notificationStackScrollLayout2.mIntrinsicPadding, f));
    }

    @Override // com.android.systemui.statusbar.phone.PanelViewController
    public final int getMaxPanelHeight() {
        int i;
        int i2 = this.mStatusBarMinHeight;
        if (this.mBarState != 1 && this.mNotificationStackScrollLayoutController.getNotGoneChildCount() == 0) {
            i2 = Math.max(i2, this.mQsMinExpansionHeight);
        }
        if (this.mQsExpandImmediate || this.mQsExpanded || ((this.mIsExpanding && this.mQsExpandedWhenExpandingStarted) || this.mPulsing)) {
            i = calculatePanelHeightQsExpanded();
        } else {
            i = calculatePanelHeightShade();
        }
        int max = Math.max(i2, i);
        if (max == 0 || Float.isNaN(max)) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("maxPanelHeight is invalid. mOverExpansion: ");
            m.append(this.mOverExpansion);
            m.append(", calculatePanelHeightQsExpanded: ");
            m.append(calculatePanelHeightQsExpanded());
            m.append(", calculatePanelHeightShade: ");
            m.append(calculatePanelHeightShade());
            m.append(", mStatusBarMinHeight = ");
            m.append(this.mStatusBarMinHeight);
            m.append(", mQsMinExpansionHeight = ");
            m.append(this.mQsMinExpansionHeight);
            Log.wtf("PanelView", m.toString());
        }
        return max;
    }

    public final float getQSEdgePosition() {
        float f = this.mQuickQsOffsetHeight;
        AmbientState ambientState = this.mAmbientState;
        Objects.requireNonNull(ambientState);
        float f2 = f * ambientState.mExpansionFraction;
        AmbientState ambientState2 = this.mAmbientState;
        Objects.requireNonNull(ambientState2);
        float f3 = ambientState2.mStackY;
        AmbientState ambientState3 = this.mAmbientState;
        Objects.requireNonNull(ambientState3);
        return Math.max(f2, f3 - ambientState3.mScrollY);
    }

    public final boolean hideStatusBarIconsWhenExpanded() {
        if (this.mIsLaunchAnimationRunning) {
            return this.mHideIconsDuringLaunchAnimation;
        }
        HeadsUpAppearanceController headsUpAppearanceController = this.mHeadsUpAppearanceController;
        if (headsUpAppearanceController != null && headsUpAppearanceController.shouldBeVisible()) {
            return false;
        }
        if (!this.mIsFullWidth || !this.mShowIconsWhenExpanded) {
            return true;
        }
        return false;
    }

    public final void initBottomArea() {
        KeyguardAffordanceHelper keyguardAffordanceHelper = new KeyguardAffordanceHelper(this.mKeyguardAffordanceHelperCallback, this.mView.getContext(), this.mFalsingManager);
        this.mAffordanceHelper = keyguardAffordanceHelper;
        KeyguardBottomAreaView keyguardBottomAreaView = this.mKeyguardBottomArea;
        Objects.requireNonNull(keyguardBottomAreaView);
        keyguardBottomAreaView.mAffordanceHelper = keyguardAffordanceHelper;
        KeyguardBottomAreaView keyguardBottomAreaView2 = this.mKeyguardBottomArea;
        StatusBar statusBar = this.mStatusBar;
        Objects.requireNonNull(keyguardBottomAreaView2);
        keyguardBottomAreaView2.mStatusBar = statusBar;
        keyguardBottomAreaView2.updateCameraVisibility();
        KeyguardBottomAreaView keyguardBottomAreaView3 = this.mKeyguardBottomArea;
        boolean z = this.mUserSetupComplete;
        Objects.requireNonNull(keyguardBottomAreaView3);
        keyguardBottomAreaView3.mUserSetupComplete = z;
        keyguardBottomAreaView3.updateCameraVisibility();
        keyguardBottomAreaView3.updateLeftAffordanceIcon();
        KeyguardBottomAreaView keyguardBottomAreaView4 = this.mKeyguardBottomArea;
        FalsingManager falsingManager = this.mFalsingManager;
        Objects.requireNonNull(keyguardBottomAreaView4);
        keyguardBottomAreaView4.mFalsingManager = falsingManager;
        KeyguardBottomAreaView keyguardBottomAreaView5 = this.mKeyguardBottomArea;
        QuickAccessWalletController quickAccessWalletController = this.mQuickAccessWalletController;
        Objects.requireNonNull(keyguardBottomAreaView5);
        keyguardBottomAreaView5.mQuickAccessWalletController = quickAccessWalletController;
        quickAccessWalletController.setupWalletChangeObservers(keyguardBottomAreaView5.mCardRetriever, QuickAccessWalletController.WalletChangeEvent.WALLET_PREFERENCE_CHANGE, QuickAccessWalletController.WalletChangeEvent.DEFAULT_PAYMENT_APP_CHANGE);
        keyguardBottomAreaView5.mQuickAccessWalletController.updateWalletPreference();
        keyguardBottomAreaView5.mQuickAccessWalletController.queryWalletCards(keyguardBottomAreaView5.mCardRetriever);
        keyguardBottomAreaView5.updateWalletVisibility();
        keyguardBottomAreaView5.updateAffordanceColors();
        KeyguardBottomAreaView keyguardBottomAreaView6 = this.mKeyguardBottomArea;
        ControlsComponent controlsComponent = this.mControlsComponent;
        Objects.requireNonNull(keyguardBottomAreaView6);
        keyguardBottomAreaView6.mControlsComponent = controlsComponent;
        controlsComponent.getControlsListingController().ifPresent(new WMShell$$ExternalSyntheticLambda6(keyguardBottomAreaView6, 2));
        keyguardBottomAreaView6.updateAffordanceColors();
        KeyguardBottomAreaView keyguardBottomAreaView7 = this.mKeyguardBottomArea;
        QRCodeScannerController qRCodeScannerController = this.mQRCodeScannerController;
        Objects.requireNonNull(keyguardBottomAreaView7);
        keyguardBottomAreaView7.mQRCodeScannerController = qRCodeScannerController;
        qRCodeScannerController.registerQRCodeScannerChangeObservers(0, 1);
        keyguardBottomAreaView7.updateQRCodeButtonVisibility();
        keyguardBottomAreaView7.updateAffordanceColors();
    }

    public final boolean isOnKeyguard() {
        if (this.mBarState == 1) {
            return true;
        }
        return false;
    }

    public final boolean isQsExpansionEnabled() {
        if (!this.mQsExpansionEnabledPolicy || !this.mQsExpansionEnabledAmbient || this.mRemoteInputManager.isRemoteInputActive()) {
            return false;
        }
        return true;
    }

    @Override // com.android.systemui.statusbar.phone.PanelViewController
    public final void loadDimens() {
        ViewConfiguration viewConfiguration = ViewConfiguration.get(super.mView.getContext());
        this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
        this.mSlopMultiplier = viewConfiguration.getScaledAmbiguousGestureMultiplier();
        this.mHintDistance = this.mResources.getDimension(2131165805);
        this.mPanelFlingOvershootAmount = this.mResources.getDimension(2131166758);
        this.mUnlockFalsingThreshold = this.mResources.getDimensionPixelSize(2131167261);
        FlingAnimationUtils.Builder builder = this.mFlingAnimationUtilsBuilder.mo144get();
        Objects.requireNonNull(builder);
        builder.mMaxLengthSeconds = 0.4f;
        this.mFlingAnimationUtils = builder.build();
        this.mStatusBarMinHeight = SystemBarUtils.getStatusBarHeight(this.mView.getContext());
        this.mStatusBarHeaderHeightKeyguard = Utils.getStatusBarHeaderHeightKeyguard(this.mView.getContext());
        this.mQsPeekHeight = this.mResources.getDimensionPixelSize(2131166902);
        KeyguardClockPositionAlgorithm keyguardClockPositionAlgorithm = this.mClockPositionAlgorithm;
        Resources resources = this.mResources;
        Objects.requireNonNull(keyguardClockPositionAlgorithm);
        keyguardClockPositionAlgorithm.mStatusViewBottomMargin = resources.getDimensionPixelSize(2131165864);
        keyguardClockPositionAlgorithm.mSplitShadeTopNotificationsMargin = resources.getDimensionPixelSize(2131167041);
        keyguardClockPositionAlgorithm.mSplitShadeTargetTopMargin = resources.getDimensionPixelSize(2131165863);
        keyguardClockPositionAlgorithm.mContainerTopPadding = resources.getDimensionPixelSize(2131165844);
        keyguardClockPositionAlgorithm.mBurnInPreventionOffsetX = resources.getDimensionPixelSize(2131165464);
        keyguardClockPositionAlgorithm.mBurnInPreventionOffsetYClock = resources.getDimensionPixelSize(2131165466);
        this.mQsFalsingThreshold = this.mResources.getDimensionPixelSize(2131166856);
        this.mResources.getDimensionPixelSize(2131166666);
        this.mIndicationBottomPadding = this.mResources.getDimensionPixelSize(2131165849);
        this.mResources.getDimensionPixelSize(2131166676);
        this.mResources.getDimensionPixelSize(2131167064);
        this.mHeadsUpInset = this.mResources.getDimensionPixelSize(2131165797) + SystemBarUtils.getStatusBarHeight(this.mView.getContext());
        this.mDistanceForQSFullShadeTransition = this.mResources.getDimensionPixelSize(2131166125);
        this.mMaxOverscrollAmountForPulse = this.mResources.getDimensionPixelSize(2131166835);
        this.mScrimCornerRadius = this.mResources.getDimensionPixelSize(2131166671);
        this.mScreenCornerRadius = (int) ScreenDecorationsUtils.getWindowCornerRadius(this.mView.getContext());
        this.mLockscreenNotificationQSPadding = this.mResources.getDimensionPixelSize(2131166677);
        this.mUdfpsMaxYBurnInOffset = this.mResources.getDimensionPixelSize(2131167260);
    }

    @Override // com.android.systemui.statusbar.phone.PanelViewController
    public final void onExpandingStarted() {
        boolean z;
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mNotificationStackScrollLayoutController;
        Objects.requireNonNull(notificationStackScrollLayoutController);
        NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
        Objects.requireNonNull(notificationStackScrollLayout);
        notificationStackScrollLayout.mIsExpansionChanging = true;
        AmbientState ambientState = notificationStackScrollLayout.mAmbientState;
        Objects.requireNonNull(ambientState);
        ambientState.mExpansionChanging = true;
        notificationStackScrollLayoutController.checkSnoozeLeavebehind();
        this.mIsExpanding = true;
        boolean z2 = this.mQsFullyExpanded;
        this.mQsExpandedWhenExpandingStarted = z2;
        MediaHierarchyManager mediaHierarchyManager = this.mMediaHierarchyManager;
        if (!z2 || this.mAnimatingQS) {
            z = false;
        } else {
            z = true;
        }
        Objects.requireNonNull(mediaHierarchyManager);
        if (mediaHierarchyManager.collapsingShadeFromQS != z) {
            mediaHierarchyManager.collapsingShadeFromQS = z;
            MediaHierarchyManager.updateDesiredLocation$default(mediaHierarchyManager, true, 2);
        }
        if (this.mQsExpanded) {
            onQsExpansionStarted();
        }
        QS qs = this.mQs;
        if (qs != null) {
            qs.setHeaderListening(true);
        }
    }

    public final void onQsExpansionStarted() {
        ValueAnimator valueAnimator = this.mQsExpansionAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        cancelHeightAnimator();
        float f = this.mQsExpansionHeight - 0;
        setQsExpansion(f);
        requestPanelHeightUpdate();
        this.mNotificationStackScrollLayoutController.checkSnoozeLeavebehind();
        if (f == 0.0f) {
            StatusBar statusBar = this.mStatusBar;
            Objects.requireNonNull(statusBar);
            if (!statusBar.mKeyguardStateController.canDismissLockScreen()) {
                statusBar.mKeyguardUpdateMonitor.requestFaceAuth(false);
            }
        }
    }

    @Override // com.android.systemui.statusbar.phone.PanelViewController
    public final void onTrackingStarted() {
        FalsingCollector falsingCollector = this.mFalsingCollector;
        this.mKeyguardStateController.canDismissLockScreen();
        falsingCollector.onTrackingStarted();
        endClosing();
        this.mTracking = true;
        StatusBar statusBar = this.mStatusBar;
        Objects.requireNonNull(statusBar);
        statusBar.mShadeController.runPostCollapseRunnables();
        notifyExpandingStarted();
        updatePanelExpansionAndVisibility();
        ScrimController scrimController = this.mScrimController;
        Objects.requireNonNull(scrimController);
        scrimController.mTracking = true;
        scrimController.mDarkenWhileDragging = !scrimController.mKeyguardStateController.canDismissLockScreen();
        if (this.mQsFullyExpanded) {
            this.mQsExpandImmediate = true;
            if (!this.mShouldUseSplitNotificationShade) {
                this.mNotificationStackScrollLayoutController.setShouldShowShelfOnly(true);
            }
        }
        int i = this.mBarState;
        if (i == 1 || i == 2) {
            KeyguardAffordanceHelper keyguardAffordanceHelper = this.mAffordanceHelper;
            Objects.requireNonNull(keyguardAffordanceHelper);
            Animator animator = keyguardAffordanceHelper.mSwipeAnimator;
            if (animator != null) {
                animator.cancel();
            }
            KeyguardAffordanceHelper.updateIcon(keyguardAffordanceHelper.mRightIcon, 0.0f, 0.0f, true, false, false, false);
            KeyguardAffordanceHelper.updateIcon(keyguardAffordanceHelper.mLeftIcon, 0.0f, 0.0f, true, false, false, false);
        }
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mNotificationStackScrollLayoutController;
        Objects.requireNonNull(notificationStackScrollLayoutController);
        NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
        Objects.requireNonNull(notificationStackScrollLayout);
        notificationStackScrollLayout.mPanelTracking = true;
        AmbientState ambientState = notificationStackScrollLayout.mAmbientState;
        Objects.requireNonNull(ambientState);
        ambientState.mPanelTracking = true;
        notificationStackScrollLayout.mSwipeHelper.resetExposedMenuView(true, true);
        this.mView.removeCallbacks(this.mMaybeHideExpandedRunnable);
    }

    @Override // com.android.systemui.statusbar.phone.PanelViewController
    public final void onTrackingStopped(boolean z) {
        int i;
        this.mFalsingCollector.onTrackingStopped();
        this.mTracking = false;
        StatusBar statusBar = this.mStatusBar;
        Objects.requireNonNull(statusBar);
        int i2 = statusBar.mState;
        if ((i2 == 1 || i2 == 2) && !z && !statusBar.mKeyguardStateController.canDismissLockScreen()) {
            statusBar.mStatusBarKeyguardViewManager.showBouncer(false);
        }
        updatePanelExpansionAndVisibility();
        if (z) {
            NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mNotificationStackScrollLayoutController;
            Objects.requireNonNull(notificationStackScrollLayoutController);
            NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
            Objects.requireNonNull(notificationStackScrollLayout);
            notificationStackScrollLayout.setOverScrollAmount(0.0f, true, true, true);
        }
        NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = this.mNotificationStackScrollLayoutController;
        Objects.requireNonNull(notificationStackScrollLayoutController2);
        NotificationStackScrollLayout notificationStackScrollLayout2 = notificationStackScrollLayoutController2.mView;
        Objects.requireNonNull(notificationStackScrollLayout2);
        notificationStackScrollLayout2.mPanelTracking = false;
        AmbientState ambientState = notificationStackScrollLayout2.mAmbientState;
        Objects.requireNonNull(ambientState);
        ambientState.mPanelTracking = false;
        if (z && (((i = this.mBarState) == 1 || i == 2) && !this.mHintAnimationRunning)) {
            this.mAffordanceHelper.reset(true);
        }
        NotificationShadeDepthController notificationShadeDepthController = this.mDepthController;
        Objects.requireNonNull(notificationShadeDepthController);
        if (notificationShadeDepthController.blursDisabledForUnlock) {
            notificationShadeDepthController.blursDisabledForUnlock = false;
            notificationShadeDepthController.scheduleUpdate(null);
        }
    }

    public final void positionClockAndNotifications(boolean z) {
        boolean z2;
        boolean z3;
        int i;
        int i2;
        boolean z4;
        boolean z5;
        boolean z6;
        int i3;
        float f;
        float f2;
        int i4;
        int i5;
        int i6;
        int i7;
        boolean z7;
        int i8;
        int i9;
        boolean z8;
        int i10;
        int i11;
        int i12;
        float f3;
        boolean z9;
        boolean isAddOrRemoveAnimationPending = this.mNotificationStackScrollLayoutController.isAddOrRemoveAnimationPending();
        boolean isOnKeyguard = isOnKeyguard();
        if (isOnKeyguard && this.mCommunalViewController != null) {
            if (this.mScreenOffAnimationController.shouldExpandNotifications()) {
                f3 = 1.0f;
            } else {
                f3 = this.mExpandedFraction;
            }
            CommunalHostViewPositionAlgorithm communalHostViewPositionAlgorithm = this.mCommunalPositionAlgorithm;
            int height = this.mCommunalView.getHeight();
            if (CommunalHostViewPositionAlgorithm.DEBUG) {
                Objects.requireNonNull(communalHostViewPositionAlgorithm);
                Log.d("CommunalPositionAlg", "setup. panelExpansion:" + f3);
            }
            communalHostViewPositionAlgorithm.mPanelExpansion = f3;
            communalHostViewPositionAlgorithm.mCommunalHeight = height;
            CommunalHostViewPositionAlgorithm communalHostViewPositionAlgorithm2 = this.mCommunalPositionAlgorithm;
            CommunalHostViewPositionAlgorithm.Result result = this.mCommunalPositionResult;
            Objects.requireNonNull(communalHostViewPositionAlgorithm2);
            result.communalY = (int) ((1.0f - communalHostViewPositionAlgorithm2.mPanelExpansion) * (-communalHostViewPositionAlgorithm2.mCommunalHeight));
            if (this.mNotificationStackScrollLayoutController.isAddOrRemoveAnimationPending() || this.mAnimateNextPositionUpdate) {
                z9 = true;
            } else {
                z9 = false;
            }
            CommunalHostViewController communalHostViewController = this.mCommunalViewController;
            int i13 = this.mCommunalPositionResult.communalY;
            Objects.requireNonNull(communalHostViewController);
            PropertyAnimator.setProperty((CommunalHostView) communalHostViewController.mView, AnimatableProperty.Y, i13, CommunalHostViewController.COMMUNAL_ANIMATION_PROPERTIES, z9);
        }
        if (isOnKeyguard || z) {
            int i14 = this.mStatusBarHeaderHeightKeyguard;
            boolean bypassEnabled = this.mKeyguardBypassController.getBypassEnabled();
            if (this.mNotificationStackScrollLayoutController.getVisibleNotificationCount() != 0 || this.mMediaDataManager.hasActiveMedia()) {
                z4 = true;
            } else {
                z4 = false;
            }
            if (!this.mShouldUseSplitNotificationShade || !this.mMediaDataManager.hasActiveMedia()) {
                z5 = false;
            } else {
                z5 = true;
            }
            ScreenOffAnimationController screenOffAnimationController = this.mScreenOffAnimationController;
            Objects.requireNonNull(screenOffAnimationController);
            ArrayList arrayList = screenOffAnimationController.animations;
            if (!(arrayList instanceof Collection) || !arrayList.isEmpty()) {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    if (!((ScreenOffAnimation) it.next()).shouldAnimateClockChange()) {
                        z6 = false;
                        break;
                    }
                }
            }
            z6 = true;
            if ((!z4 || this.mShouldUseSplitNotificationShade) && (!z5 || this.mDozing)) {
                KeyguardStatusViewController keyguardStatusViewController = this.mKeyguardStatusViewController;
                Objects.requireNonNull(keyguardStatusViewController);
                keyguardStatusViewController.mKeyguardClockSwitchController.displayClock(0, z6);
            } else {
                KeyguardStatusViewController keyguardStatusViewController2 = this.mKeyguardStatusViewController;
                Objects.requireNonNull(keyguardStatusViewController2);
                keyguardStatusViewController2.mKeyguardClockSwitchController.displayClock(1, z6);
            }
            updateKeyguardStatusViewAlignment(true);
            KeyguardQsUserSwitchController keyguardQsUserSwitchController = this.mKeyguardQsUserSwitchController;
            if (keyguardQsUserSwitchController != null) {
                i3 = keyguardQsUserSwitchController.mUserAvatarView.getHeight();
            } else {
                i3 = 0;
            }
            KeyguardUserSwitcherController keyguardUserSwitcherController = this.mKeyguardUserSwitcherController;
            if (keyguardUserSwitcherController != null) {
                i3 = keyguardUserSwitcherController.mListView.getHeight();
            }
            if (this.mScreenOffAnimationController.shouldExpandNotifications()) {
                f = 1.0f;
            } else {
                f = this.mExpandedFraction;
            }
            if (this.mScreenOffAnimationController.shouldExpandNotifications()) {
                f2 = 1.0f;
            } else {
                f2 = this.mInterpolatedDarkAmount;
            }
            float f4 = -1.0f;
            if (this.mUpdateMonitor.isUdfpsEnrolled()) {
                AuthController authController = this.mAuthController;
                Objects.requireNonNull(authController);
                if (authController.mUdfpsProps.size() > 0) {
                    AuthController authController2 = this.mAuthController;
                    Objects.requireNonNull(authController2);
                    SensorLocationInternal location = ((FingerprintSensorPropertiesInternal) authController2.mUdfpsProps.get(0)).getLocation();
                    f4 = (location.sensorLocationY - location.sensorRadius) - this.mUdfpsMaxYBurnInOffset;
                }
            }
            KeyguardClockPositionAlgorithm keyguardClockPositionAlgorithm = this.mClockPositionAlgorithm;
            int i15 = this.mStatusBarHeaderHeightKeyguard;
            KeyguardStatusViewController keyguardStatusViewController3 = this.mKeyguardStatusViewController;
            Objects.requireNonNull(keyguardStatusViewController3);
            int height2 = ((KeyguardStatusView) keyguardStatusViewController3.mView).getHeight();
            KeyguardClockSwitchController keyguardClockSwitchController = keyguardStatusViewController3.mKeyguardClockSwitchController;
            Objects.requireNonNull(keyguardClockSwitchController);
            NotificationIconAreaController notificationIconAreaController = keyguardClockSwitchController.mNotificationIconAreaController;
            Objects.requireNonNull(notificationIconAreaController);
            NotificationIconContainer notificationIconContainer = notificationIconAreaController.mAodIcons;
            if (notificationIconContainer == null) {
                i4 = 0;
            } else {
                i4 = notificationIconContainer.getHeight();
            }
            int i16 = height2 - i4;
            float f5 = this.mOverStretchAmount;
            QS qs = this.mQs;
            if (qs != null) {
                i5 = qs.getHeader().getHeight();
            } else {
                i5 = 0;
            }
            int i17 = i5 + this.mQsPeekHeight;
            float computeQsExpansionFraction = computeQsExpansionFraction();
            z3 = isAddOrRemoveAnimationPending;
            int i18 = this.mDisplayTopInset;
            z2 = isOnKeyguard;
            boolean z10 = this.mShouldUseSplitNotificationShade;
            KeyguardStatusViewController keyguardStatusViewController4 = this.mKeyguardStatusViewController;
            int i19 = this.mStatusBarHeaderHeightKeyguard;
            Objects.requireNonNull(keyguardStatusViewController4);
            KeyguardClockSwitchController keyguardClockSwitchController2 = keyguardStatusViewController4.mKeyguardClockSwitchController;
            Objects.requireNonNull(keyguardClockSwitchController2);
            if (keyguardClockSwitchController2.mLargeClockFrame.getVisibility() == 0) {
                i7 = (keyguardClockSwitchController2.mLargeClockFrame.findViewById(2131427489).getHeight() / 2) + (keyguardClockSwitchController2.mLargeClockFrame.getHeight() / 2);
                i6 = i18;
            } else {
                i6 = i18;
                i7 = keyguardClockSwitchController2.mKeyguardClockTopMargin + keyguardClockSwitchController2.mClockFrame.findViewById(2131427488).getHeight() + i19;
            }
            float f6 = i7;
            KeyguardStatusViewController keyguardStatusViewController5 = this.mKeyguardStatusViewController;
            Objects.requireNonNull(keyguardStatusViewController5);
            KeyguardClockSwitchController keyguardClockSwitchController3 = keyguardStatusViewController5.mKeyguardClockSwitchController;
            Objects.requireNonNull(keyguardClockSwitchController3);
            if (keyguardClockSwitchController3.mLargeClockFrame.getVisibility() != 0) {
                z7 = true;
            } else {
                z7 = false;
            }
            Objects.requireNonNull(keyguardClockPositionAlgorithm);
            keyguardClockPositionAlgorithm.mMinTopMargin = Math.max(keyguardClockPositionAlgorithm.mContainerTopPadding, i3) + i15;
            keyguardClockPositionAlgorithm.mPanelExpansion = f;
            keyguardClockPositionAlgorithm.mKeyguardStatusHeight = keyguardClockPositionAlgorithm.mStatusViewBottomMargin + i16;
            keyguardClockPositionAlgorithm.mUserSwitchHeight = i3;
            keyguardClockPositionAlgorithm.mUserSwitchPreferredY = i14;
            keyguardClockPositionAlgorithm.mDarkAmount = f2;
            keyguardClockPositionAlgorithm.mOverStretchAmount = f5;
            keyguardClockPositionAlgorithm.mBypassEnabled = bypassEnabled;
            keyguardClockPositionAlgorithm.mUnlockedStackScrollerPadding = i17;
            keyguardClockPositionAlgorithm.mQsExpansion = computeQsExpansionFraction;
            keyguardClockPositionAlgorithm.mCutoutTopInset = i6;
            keyguardClockPositionAlgorithm.mIsSplitShade = z10;
            keyguardClockPositionAlgorithm.mUdfpsTop = f4;
            keyguardClockPositionAlgorithm.mClockBottom = f6;
            keyguardClockPositionAlgorithm.mIsClockTopAligned = z7;
            KeyguardClockPositionAlgorithm keyguardClockPositionAlgorithm2 = this.mClockPositionAlgorithm;
            KeyguardClockPositionAlgorithm.Result result2 = this.mClockPositionResult;
            Objects.requireNonNull(keyguardClockPositionAlgorithm2);
            int clockY = keyguardClockPositionAlgorithm2.getClockY(keyguardClockPositionAlgorithm2.mPanelExpansion, keyguardClockPositionAlgorithm2.mDarkAmount);
            result2.clockY = clockY;
            result2.userSwitchY = (int) (MathUtils.lerp((-keyguardClockPositionAlgorithm2.mKeyguardStatusHeight) - keyguardClockPositionAlgorithm2.mUserSwitchHeight, keyguardClockPositionAlgorithm2.mUserSwitchPreferredY, Interpolators.FAST_OUT_LINEAR_IN.getInterpolation(keyguardClockPositionAlgorithm2.mPanelExpansion)) + keyguardClockPositionAlgorithm2.mOverStretchAmount);
            result2.clockYFullyDozing = keyguardClockPositionAlgorithm2.getClockY(1.0f, 1.0f);
            result2.clockAlpha = MathUtils.lerp(Interpolators.ACCELERATE.getInterpolation((1.0f - MathUtils.saturate(keyguardClockPositionAlgorithm2.mQsExpansion / 0.3f)) * Math.max(0.0f, clockY / Math.max(1.0f, keyguardClockPositionAlgorithm2.getClockY(1.0f, keyguardClockPositionAlgorithm2.mDarkAmount)))), 1.0f, keyguardClockPositionAlgorithm2.mDarkAmount);
            boolean z11 = keyguardClockPositionAlgorithm2.mBypassEnabled;
            if (z11) {
                i8 = (int) (keyguardClockPositionAlgorithm2.mUnlockedStackScrollerPadding + keyguardClockPositionAlgorithm2.mOverStretchAmount);
            } else {
                if (keyguardClockPositionAlgorithm2.mIsSplitShade) {
                    clockY -= keyguardClockPositionAlgorithm2.mSplitShadeTopNotificationsMargin;
                    i12 = keyguardClockPositionAlgorithm2.mUserSwitchHeight;
                } else {
                    i12 = keyguardClockPositionAlgorithm2.mKeyguardStatusHeight;
                }
                i8 = clockY + i12;
            }
            result2.stackScrollerPadding = i8;
            if (z11) {
                i9 = keyguardClockPositionAlgorithm2.mUnlockedStackScrollerPadding;
            } else {
                if (keyguardClockPositionAlgorithm2.mIsSplitShade) {
                    i11 = keyguardClockPositionAlgorithm2.getClockY(1.0f, keyguardClockPositionAlgorithm2.mDarkAmount);
                    i10 = keyguardClockPositionAlgorithm2.mUserSwitchHeight;
                } else {
                    i11 = keyguardClockPositionAlgorithm2.getClockY(1.0f, keyguardClockPositionAlgorithm2.mDarkAmount);
                    i10 = keyguardClockPositionAlgorithm2.mKeyguardStatusHeight;
                }
                i9 = i11 + i10;
            }
            result2.stackScrollerPaddingExpanded = i9;
            result2.clockX = (int) R$array.interpolate(0.0f, R$anim.getBurnInOffset(keyguardClockPositionAlgorithm2.mBurnInPreventionOffsetX, true), keyguardClockPositionAlgorithm2.mDarkAmount);
            result2.clockScale = R$array.interpolate(R$anim.zigzag(((float) System.currentTimeMillis()) / 60000.0f, 0.2f, 181.0f) + 0.8f, 1.0f, 1.0f - keyguardClockPositionAlgorithm2.mDarkAmount);
            if ((this.mNotificationStackScrollLayoutController.isAddOrRemoveAnimationPending() || this.mAnimateNextPositionUpdate) && z6) {
                z8 = true;
            } else {
                z8 = false;
            }
            KeyguardStatusViewController keyguardStatusViewController6 = this.mKeyguardStatusViewController;
            KeyguardClockPositionAlgorithm.Result result3 = this.mClockPositionResult;
            keyguardStatusViewController6.updatePosition(result3.clockX, result3.clockY, result3.clockScale, z8);
            KeyguardQsUserSwitchController keyguardQsUserSwitchController2 = this.mKeyguardQsUserSwitchController;
            if (keyguardQsUserSwitchController2 != null) {
                KeyguardClockPositionAlgorithm.Result result4 = this.mClockPositionResult;
                int i20 = result4.clockX;
                int i21 = result4.userSwitchY;
                AnimationProperties animationProperties = KeyguardQsUserSwitchController.ANIMATION_PROPERTIES;
                PropertyAnimator.setProperty((FrameLayout) keyguardQsUserSwitchController2.mView, AnimatableProperty.Y, i21, animationProperties, z8);
                PropertyAnimator.setProperty((FrameLayout) keyguardQsUserSwitchController2.mView, AnimatableProperty.TRANSLATION_X, -Math.abs(i20), animationProperties, z8);
            }
            KeyguardUserSwitcherController keyguardUserSwitcherController2 = this.mKeyguardUserSwitcherController;
            if (keyguardUserSwitcherController2 != null) {
                KeyguardClockPositionAlgorithm.Result result5 = this.mClockPositionResult;
                int i22 = result5.clockX;
                int i23 = result5.userSwitchY;
                AnimationProperties animationProperties2 = KeyguardUserSwitcherController.ANIMATION_PROPERTIES;
                PropertyAnimator.setProperty(keyguardUserSwitcherController2.mListView, AnimatableProperty.Y, i23, animationProperties2, z8);
                PropertyAnimator.setProperty(keyguardUserSwitcherController2.mListView, AnimatableProperty.TRANSLATION_X, -Math.abs(i22), animationProperties2, z8);
                Rect rect = new Rect();
                keyguardUserSwitcherController2.mListView.getDrawingRect(rect);
                ((KeyguardUserSwitcherView) keyguardUserSwitcherController2.mView).offsetDescendantRectToMyCoords(keyguardUserSwitcherController2.mListView, rect);
                KeyguardUserSwitcherScrim keyguardUserSwitcherScrim = keyguardUserSwitcherController2.mBackground;
                Objects.requireNonNull(keyguardUserSwitcherScrim);
                keyguardUserSwitcherScrim.mCircleX = (int) (keyguardUserSwitcherController2.mListView.getTranslationX() + rect.left + (rect.width() / 2));
                keyguardUserSwitcherScrim.mCircleY = (int) (keyguardUserSwitcherController2.mListView.getTranslationY() + rect.top + (rect.height() / 2));
                keyguardUserSwitcherScrim.updatePaint();
            }
            updateNotificationTranslucency();
            updateClock();
        } else {
            z3 = isAddOrRemoveAnimationPending;
            z2 = isOnKeyguard;
        }
        if (z2) {
            i = this.mClockPositionResult.stackScrollerPaddingExpanded;
        } else if (this.mShouldUseSplitNotificationShade) {
            i = 0;
        } else {
            QS qs2 = this.mQs;
            if (qs2 != null) {
                i2 = qs2.getHeader().getHeight();
            } else {
                i2 = 0;
            }
            i = i2 + this.mQsPeekHeight;
        }
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mNotificationStackScrollLayoutController;
        Objects.requireNonNull(notificationStackScrollLayoutController);
        NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
        Objects.requireNonNull(notificationStackScrollLayout);
        notificationStackScrollLayout.mIntrinsicPadding = i;
        KeyguardBottomAreaView keyguardBottomAreaView = this.mKeyguardBottomArea;
        int i24 = this.mClockPositionResult.clockX;
        Objects.requireNonNull(keyguardBottomAreaView);
        if (keyguardBottomAreaView.mBurnInXOffset != i24) {
            keyguardBottomAreaView.mBurnInXOffset = i24;
            float f7 = i24;
            keyguardBottomAreaView.mIndicationArea.setTranslationX(f7);
            View view = keyguardBottomAreaView.mAmbientIndicationArea;
            if (view != null) {
                view.setTranslationX(f7);
            }
        }
        this.mStackScrollerMeasuringPass++;
        requestScrollerTopPaddingUpdate(z3);
        this.mStackScrollerMeasuringPass = 0;
        this.mAnimateNextPositionUpdate = false;
    }

    public final View reInflateStub(int i, int i2, int i3, boolean z) {
        View findViewById = this.mView.findViewById(i);
        if (findViewById != null) {
            int indexOfChild = this.mView.indexOfChild(findViewById);
            this.mView.removeView(findViewById);
            if (z) {
                View inflate = this.mLayoutInflater.inflate(i3, (ViewGroup) this.mView, false);
                this.mView.addView(inflate, indexOfChild);
                return inflate;
            }
            ViewStub viewStub = new ViewStub(this.mView.getContext(), i3);
            viewStub.setId(i2);
            this.mView.addView(viewStub, indexOfChild);
            return null;
        } else if (z) {
            return ((ViewStub) this.mView.findViewById(i2)).inflate();
        } else {
            return findViewById;
        }
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NotifPanelEventSource
    public final void registerCallbacks(NotifPanelEventSource.Callbacks callbacks) {
        this.mNotifEventSourceCallbacks.addIfAbsent(callbacks);
    }

    public final void requestScrollerTopPaddingUpdate(boolean z) {
        float f;
        int layoutMinHeight;
        boolean z2;
        boolean z3;
        int i;
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mNotificationStackScrollLayoutController;
        if (!this.mShouldUseSplitNotificationShade || this.mKeyguardShowing) {
            boolean z4 = this.mKeyguardShowing;
            if (!z4 || (!this.mQsExpandImmediate && (!this.mIsExpanding || !this.mQsExpandedWhenExpandingStarted))) {
                ValueAnimator valueAnimator = this.mQsSizeChangeAnimator;
                if (valueAnimator != null) {
                    i = Math.max(((Integer) valueAnimator.getAnimatedValue()).intValue(), getKeyguardNotificationStaticPadding());
                } else if (z4) {
                    f = MathUtils.lerp(getKeyguardNotificationStaticPadding(), this.mQsMaxExpansionHeight, computeQsExpansionFraction());
                } else {
                    f = this.mQsFrameTranslateController.getNotificationsTopPadding(this.mQsExpansionHeight);
                }
            } else {
                int keyguardNotificationStaticPadding = getKeyguardNotificationStaticPadding();
                int i2 = this.mQsMaxExpansionHeight;
                if (this.mBarState == 1) {
                    i2 = Math.max(keyguardNotificationStaticPadding, i2);
                }
                i = (int) MathUtils.lerp(this.mQsMinExpansionHeight, i2, this.mExpandedFraction);
            }
            f = i;
        } else {
            f = 0.0f;
        }
        Objects.requireNonNull(notificationStackScrollLayoutController);
        NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
        Objects.requireNonNull(notificationStackScrollLayout);
        int i3 = (int) f;
        if (notificationStackScrollLayout.getLayoutMinHeight() + i3 > notificationStackScrollLayout.getHeight()) {
            notificationStackScrollLayout.mTopPaddingOverflow = layoutMinHeight - notificationStackScrollLayout.getHeight();
        } else {
            notificationStackScrollLayout.mTopPaddingOverflow = 0.0f;
        }
        if (!z || notificationStackScrollLayout.mKeyguardBypassEnabled) {
            z2 = false;
        } else {
            z2 = true;
        }
        if (notificationStackScrollLayout.mTopPadding != i3) {
            if (z2 || notificationStackScrollLayout.mAnimateNextTopPaddingChange) {
                z3 = true;
            } else {
                z3 = false;
            }
            notificationStackScrollLayout.mTopPadding = i3;
            notificationStackScrollLayout.updateAlgorithmHeightAndPadding();
            notificationStackScrollLayout.updateContentHeight();
            if (z3 && notificationStackScrollLayout.mAnimationsEnabled && notificationStackScrollLayout.mIsExpanded) {
                notificationStackScrollLayout.mTopPaddingNeedsAnimation = true;
                notificationStackScrollLayout.mNeedsAnimation = true;
            }
            notificationStackScrollLayout.updateStackPosition(false);
            notificationStackScrollLayout.requestChildrenUpdate();
            notificationStackScrollLayout.notifyHeightChangeListener(null, z3);
            notificationStackScrollLayout.mAnimateNextTopPaddingChange = false;
        }
        notificationStackScrollLayout.setExpandedHeight(notificationStackScrollLayout.mExpandedHeight);
        if (this.mKeyguardShowing && this.mKeyguardBypassController.getBypassEnabled()) {
            updateQsExpansion$1();
        }
    }

    public final void setDozing$1(boolean z, boolean z2) {
        float f;
        if (z != this.mDozing) {
            NotificationPanelView notificationPanelView = this.mView;
            Objects.requireNonNull(notificationPanelView);
            notificationPanelView.mDozing = z;
            this.mDozing = z;
            NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mNotificationStackScrollLayoutController;
            Objects.requireNonNull(notificationStackScrollLayoutController);
            NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
            Objects.requireNonNull(notificationStackScrollLayout);
            AmbientState ambientState = notificationStackScrollLayout.mAmbientState;
            Objects.requireNonNull(ambientState);
            if (ambientState.mDozing != z) {
                AmbientState ambientState2 = notificationStackScrollLayout.mAmbientState;
                Objects.requireNonNull(ambientState2);
                ambientState2.mDozing = z;
                notificationStackScrollLayout.requestChildrenUpdate();
                notificationStackScrollLayout.notifyHeightChangeListener(notificationStackScrollLayout.mShelf, false);
            }
            this.mKeyguardBottomArea.setDozing$2(this.mDozing, z2);
            KeyguardStatusBarViewController keyguardStatusBarViewController = this.mKeyguardStatusBarViewController;
            boolean z3 = this.mDozing;
            Objects.requireNonNull(keyguardStatusBarViewController);
            keyguardStatusBarViewController.mDozing = z3;
            if (z) {
                this.mBottomAreaShadeAlphaAnimator.cancel();
            }
            int i = this.mBarState;
            if (i == 1 || i == 2) {
                this.mKeyguardBottomArea.setDozing$2(this.mDozing, z2);
                if (!this.mDozing && z2) {
                    this.mKeyguardStatusBarViewController.animateKeyguardStatusBarIn();
                }
            }
            if (z) {
                f = 1.0f;
            } else {
                f = 0.0f;
            }
            this.mStatusBarStateController.setAndInstrumentDozeAmount(this.mView, f, z2);
        }
    }

    @Override // com.android.systemui.statusbar.phone.PanelViewController
    public final void setIsClosing(boolean z) {
        boolean z2 = this.mClosing;
        this.mClosing = z;
        if (z2 != z) {
            Iterator<NotifPanelEventSource.Callbacks> it = this.mNotifEventSourceCallbacks.iterator();
            while (it.hasNext()) {
                it.next().onPanelCollapsingChanged(z);
            }
        }
    }

    public final void setKeyguardBottomAreaVisibility(int i, boolean z) {
        this.mKeyguardBottomArea.animate().cancel();
        if (z) {
            this.mKeyguardBottomArea.animate().alpha(0.0f).setStartDelay(this.mKeyguardStateController.getKeyguardFadingAwayDelay()).setDuration(this.mKeyguardStateController.getShortenedFadingAwayDuration()).setInterpolator(Interpolators.ALPHA_OUT).withEndAction(this.mAnimateKeyguardBottomAreaInvisibleEndRunnable).start();
        } else if (i == 1 || i == 2) {
            this.mKeyguardBottomArea.setVisibility(0);
            this.mKeyguardBottomArea.setAlpha(1.0f);
        } else {
            this.mKeyguardBottomArea.setVisibility(8);
        }
    }

    public final void setLaunchingAffordance(boolean z) {
        this.mLaunchingAffordance = z;
        KeyguardAffordanceView leftIcon = this.mKeyguardAffordanceHelperCallback.getLeftIcon();
        Objects.requireNonNull(leftIcon);
        leftIcon.mLaunchingAffordance = z;
        KeyguardAffordanceView rightIcon = this.mKeyguardAffordanceHelperCallback.getRightIcon();
        Objects.requireNonNull(rightIcon);
        rightIcon.mLaunchingAffordance = z;
        KeyguardBypassController keyguardBypassController = this.mKeyguardBypassController;
        Objects.requireNonNull(keyguardBypassController);
        keyguardBypassController.launchingAffordance = z;
    }

    public final void setListening(boolean z) {
        KeyguardStatusBarViewController keyguardStatusBarViewController = this.mKeyguardStatusBarViewController;
        Objects.requireNonNull(keyguardStatusBarViewController);
        if (z != keyguardStatusBarViewController.mBatteryListening) {
            keyguardStatusBarViewController.mBatteryListening = z;
            if (z) {
                keyguardStatusBarViewController.mBatteryController.addCallback(keyguardStatusBarViewController.mBatteryStateChangeCallback);
            } else {
                keyguardStatusBarViewController.mBatteryController.removeCallback(keyguardStatusBarViewController.mBatteryStateChangeCallback);
            }
        }
        QS qs = this.mQs;
        if (qs != null) {
            qs.setListening(z);
        }
    }

    @Override // com.android.systemui.statusbar.phone.PanelViewController
    public final void setOverExpansion(float f) {
        if (f != this.mOverExpansion) {
            this.mOverExpansion = f;
            this.mQsFrameTranslateController.translateQsFrame();
            NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mNotificationStackScrollLayoutController;
            Objects.requireNonNull(notificationStackScrollLayoutController);
            NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
            Objects.requireNonNull(notificationStackScrollLayout);
            AmbientState ambientState = notificationStackScrollLayout.mAmbientState;
            Objects.requireNonNull(ambientState);
            ambientState.mOverExpansion = f;
            notificationStackScrollLayout.updateStackPosition(false);
            notificationStackScrollLayout.requestChildrenUpdate();
        }
    }

    public final void setPanelAlpha(int i, boolean z) {
        AnimationProperties animationProperties;
        if (this.mPanelAlpha != i) {
            this.mPanelAlpha = i;
            NotificationPanelView notificationPanelView = this.mView;
            AnimatableProperty.AnonymousClass6 r1 = this.mPanelAlphaAnimator;
            float f = i;
            if (i == 255) {
                animationProperties = this.mPanelAlphaInPropertiesAnimator;
            } else {
                animationProperties = this.mPanelAlphaOutPropertiesAnimator;
            }
            PropertyAnimator.setProperty(notificationPanelView, r1, f, animationProperties, z);
        }
    }

    public final void setPanelScrimMinFraction(float f) {
        this.mMinFraction = f;
        NotificationShadeDepthController notificationShadeDepthController = this.mDepthController;
        Objects.requireNonNull(notificationShadeDepthController);
        notificationShadeDepthController.panelPullDownMinFraction = f;
        ScrimController scrimController = this.mScrimController;
        float f2 = this.mMinFraction;
        Objects.requireNonNull(scrimController);
        if (!Float.isNaN(f2)) {
            scrimController.mPanelScrimMinFraction = f2;
            scrimController.calculateAndUpdatePanelExpansion();
            return;
        }
        throw new IllegalArgumentException("minFraction should not be NaN");
    }

    @VisibleForTesting
    public void setQsExpanded(boolean z) {
        boolean z2;
        int i;
        Object obj;
        Throwable th;
        boolean z3 = true;
        if (this.mQsExpanded != z) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (z2) {
            this.mQsExpanded = z;
            updateQsState();
            requestPanelHeightUpdate();
            this.mFalsingCollector.setQsExpanded(z);
            StatusBar statusBar = this.mStatusBar;
            Objects.requireNonNull(statusBar);
            statusBar.mNotificationShadeWindowController.setQsExpanded(z);
            NotificationPanelViewController notificationPanelViewController = statusBar.mNotificationPanelViewController;
            if (z) {
                i = 4;
            } else {
                i = 0;
            }
            Objects.requireNonNull(notificationPanelViewController);
            KeyguardStatusViewController keyguardStatusViewController = notificationPanelViewController.mKeyguardStatusViewController;
            Objects.requireNonNull(keyguardStatusViewController);
            ((KeyguardStatusView) keyguardStatusViewController.mView).setImportantForAccessibility(i);
            statusBar.mNotificationPanelViewController.updateSystemUiStateFlags();
            if (statusBar.getNavigationBarView() != null) {
                NavigationBarView navigationBarView = statusBar.getNavigationBarView();
                Objects.requireNonNull(navigationBarView);
                navigationBarView.updateSlippery();
            }
            NotificationsQSContainerController notificationsQSContainerController = this.mNotificationsQSContainerController;
            Objects.requireNonNull(notificationsQSContainerController);
            if (notificationsQSContainerController.qsExpanded != z) {
                notificationsQSContainerController.qsExpanded = z;
                ((NotificationsQuickSettingsContainer) notificationsQSContainerController.mView).invalidate();
            }
            PulseExpansionHandler pulseExpansionHandler = this.mPulseExpansionHandler;
            Objects.requireNonNull(pulseExpansionHandler);
            pulseExpansionHandler.qsExpanded = z;
            KeyguardBypassController keyguardBypassController = this.mKeyguardBypassController;
            Objects.requireNonNull(keyguardBypassController);
            if (keyguardBypassController.qSExpanded == z) {
                z3 = false;
            }
            keyguardBypassController.qSExpanded = z;
            if (z3 && !z) {
                keyguardBypassController.maybePerformPendingUnlock();
            }
            StatusBarKeyguardViewManager statusBarKeyguardViewManager = this.mStatusBarKeyguardViewManager;
            Objects.requireNonNull(statusBarKeyguardViewManager);
            statusBarKeyguardViewManager.mQsExpanded = z;
            StatusBarKeyguardViewManager.AlternateAuthInterceptor alternateAuthInterceptor = statusBarKeyguardViewManager.mAlternateAuthInterceptor;
            if (alternateAuthInterceptor != null) {
                UdfpsKeyguardViewController udfpsKeyguardViewController = UdfpsKeyguardViewController.this;
                udfpsKeyguardViewController.mQsExpanded = z;
                udfpsKeyguardViewController.updatePauseAuth();
            }
            LockIconViewController lockIconViewController = this.mLockIconViewController;
            Objects.requireNonNull(lockIconViewController);
            lockIconViewController.mQsExpanded = z;
            lockIconViewController.updateVisibility();
            PrivacyDotViewController privacyDotViewController = this.mPrivacyDotViewController;
            Objects.requireNonNull(privacyDotViewController);
            Intrinsics.stringPlus("setQsExpanded ", Boolean.valueOf(z));
            Object obj2 = privacyDotViewController.lock;
            synchronized (obj2) {
                try {
                    obj = obj2;
                    try {
                        privacyDotViewController.setNextViewState(ViewState.copy$default(privacyDotViewController.nextViewState, false, false, false, z, null, null, null, null, false, 0, 0, 0, null, null, 16375));
                    } catch (Throwable th2) {
                        th = th2;
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    obj = obj2;
                }
            }
        }
    }

    public final void setQsExpansion(float f) {
        boolean z;
        float min = Math.min(Math.max(f, this.mQsMinExpansionHeight), this.mQsMaxExpansionHeight);
        int i = this.mQsMaxExpansionHeight;
        if (min != i || i == 0) {
            z = false;
        } else {
            z = true;
        }
        this.mQsFullyExpanded = z;
        float f2 = this.mQsMinExpansionHeight;
        if (min > f2 && !this.mQsExpanded && !this.mStackScrollerOverscrolling && !this.mDozing) {
            setQsExpanded(true);
        } else if (min <= f2 && this.mQsExpanded) {
            setQsExpanded(false);
        }
        this.mQsExpansionHeight = min;
        updateQsExpansion$1();
        requestScrollerTopPaddingUpdate(false);
        this.mKeyguardStatusBarViewController.updateViewState();
        int i2 = this.mBarState;
        if (i2 == 2 || i2 == 1) {
            updateKeyguardBottomAreaAlpha();
            positionClockAndNotifications(false);
        }
        if (this.mAccessibilityManager.isEnabled()) {
            this.mView.setAccessibilityPaneTitle(determineAccessibilityPaneTitle());
        }
        if (!this.mFalsingManager.isUnlockingDisabled() && this.mQsFullyExpanded) {
            this.mFalsingCollector.shouldEnforceBouncer();
        }
    }

    public final boolean touchXOutsideOfQs(float f) {
        if (f < this.mQsFrame.getX() || f > this.mQsFrame.getX() + this.mQsFrame.getWidth()) {
            return true;
        }
        return false;
    }

    public final void traceQsJank(boolean z, boolean z2) {
        InteractionJankMonitor interactionJankMonitor = this.mInteractionJankMonitor;
        if (interactionJankMonitor != null) {
            if (z) {
                interactionJankMonitor.begin(this.mView, 5);
            } else if (z2) {
                interactionJankMonitor.cancel(5);
            } else {
                interactionJankMonitor.end(5);
            }
        }
    }

    public final void trackMovement(MotionEvent motionEvent) {
        VelocityTracker velocityTracker = this.mQsVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.addMovement(motionEvent);
        }
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NotifPanelEventSource
    public final void unregisterCallbacks(NotifPanelEventSource.Callbacks callbacks) {
        this.mNotifEventSourceCallbacks.remove(callbacks);
    }

    public final void updateClock() {
        float f = this.mClockPositionResult.clockAlpha * this.mKeyguardOnlyContentAlpha;
        KeyguardStatusViewController keyguardStatusViewController = this.mKeyguardStatusViewController;
        Objects.requireNonNull(keyguardStatusViewController);
        KeyguardVisibilityHelper keyguardVisibilityHelper = keyguardStatusViewController.mKeyguardVisibilityHelper;
        Objects.requireNonNull(keyguardVisibilityHelper);
        if (!keyguardVisibilityHelper.mKeyguardViewVisibilityAnimating) {
            KeyguardUnlockAnimationController keyguardUnlockAnimationController = keyguardStatusViewController.mKeyguardUnlockAnimationController;
            Objects.requireNonNull(keyguardUnlockAnimationController);
            if (keyguardUnlockAnimationController.unlockingWithSmartspaceTransition) {
                ((KeyguardStatusView) keyguardStatusViewController.mView).setChildrenAlphaExcludingClockView(f);
                keyguardStatusViewController.mKeyguardClockSwitchController.setChildrenAlphaExcludingSmartspace(f);
            } else {
                KeyguardVisibilityHelper keyguardVisibilityHelper2 = keyguardStatusViewController.mKeyguardVisibilityHelper;
                Objects.requireNonNull(keyguardVisibilityHelper2);
                if (!keyguardVisibilityHelper2.mKeyguardViewVisibilityAnimating) {
                    ((KeyguardStatusView) keyguardStatusViewController.mView).setAlpha(f);
                    KeyguardStatusView keyguardStatusView = (KeyguardStatusView) keyguardStatusViewController.mView;
                    Objects.requireNonNull(keyguardStatusView);
                    if (keyguardStatusView.mChildrenAlphaExcludingSmartSpace < 1.0f) {
                        ((KeyguardStatusView) keyguardStatusViewController.mView).setChildrenAlphaExcludingClockView(1.0f);
                        keyguardStatusViewController.mKeyguardClockSwitchController.setChildrenAlphaExcludingSmartspace(1.0f);
                    }
                }
            }
        }
        KeyguardQsUserSwitchController keyguardQsUserSwitchController = this.mKeyguardQsUserSwitchController;
        if (keyguardQsUserSwitchController != null) {
            Objects.requireNonNull(keyguardQsUserSwitchController);
            KeyguardVisibilityHelper keyguardVisibilityHelper3 = keyguardQsUserSwitchController.mKeyguardVisibilityHelper;
            Objects.requireNonNull(keyguardVisibilityHelper3);
            if (!keyguardVisibilityHelper3.mKeyguardViewVisibilityAnimating) {
                ((FrameLayout) keyguardQsUserSwitchController.mView).setAlpha(f);
            }
        }
        KeyguardUserSwitcherController keyguardUserSwitcherController = this.mKeyguardUserSwitcherController;
        if (keyguardUserSwitcherController != null) {
            Objects.requireNonNull(keyguardUserSwitcherController);
            KeyguardVisibilityHelper keyguardVisibilityHelper4 = keyguardUserSwitcherController.mKeyguardVisibilityHelper;
            Objects.requireNonNull(keyguardVisibilityHelper4);
            if (!keyguardVisibilityHelper4.mKeyguardViewVisibilityAnimating) {
                ((KeyguardUserSwitcherView) keyguardUserSwitcherController.mView).setAlpha(f);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:26:0x0080, code lost:
        if (r6 < r2) goto L_0x0084;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateExpandedHeight(float r6) {
        /*
            r5 = this;
            boolean r0 = r5.mTracking
            if (r0 == 0) goto L_0x0022
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController r0 = r5.mNotificationStackScrollLayoutController
            android.view.VelocityTracker r1 = r5.mVelocityTracker
            r2 = 1000(0x3e8, float:1.401E-42)
            r1.computeCurrentVelocity(r2)
            android.view.VelocityTracker r1 = r5.mVelocityTracker
            float r1 = r1.getYVelocity()
            java.util.Objects.requireNonNull(r0)
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout r0 = r0.mView
            java.util.Objects.requireNonNull(r0)
            com.android.systemui.statusbar.notification.stack.AmbientState r0 = r0.mAmbientState
            java.util.Objects.requireNonNull(r0)
            r0.mExpandingVelocity = r1
        L_0x0022:
            com.android.systemui.statusbar.phone.KeyguardBypassController r0 = r5.mKeyguardBypassController
            boolean r0 = r0.getBypassEnabled()
            if (r0 == 0) goto L_0x0035
            boolean r0 = r5.isOnKeyguard()
            if (r0 == 0) goto L_0x0035
            int r6 = r5.getMaxPanelHeight()
            float r6 = (float) r6
        L_0x0035:
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController r0 = r5.mNotificationStackScrollLayoutController
            java.util.Objects.requireNonNull(r0)
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout r0 = r0.mView
            r0.setExpandedHeight(r6)
            r5.updateKeyguardBottomAreaAlpha()
            com.android.systemui.statusbar.phone.HeadsUpManagerPhone r6 = r5.mHeadsUpManager
            java.util.Objects.requireNonNull(r6)
            boolean r6 = r6.mHasPinnedNotification
            r0 = 1
            r1 = 0
            if (r6 != 0) goto L_0x0051
            boolean r6 = r5.mHeadsUpAnimatingAway
            if (r6 == 0) goto L_0x0057
        L_0x0051:
            int r6 = r5.mBarState
            if (r6 != 0) goto L_0x0057
            r6 = r0
            goto L_0x0058
        L_0x0057:
            r6 = r1
        L_0x0058:
            if (r6 != 0) goto L_0x005e
            boolean r6 = r5.mIsFullWidth
            if (r6 == 0) goto L_0x0083
        L_0x005e:
            float r6 = r5.mExpandedHeight
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController r2 = r5.mNotificationStackScrollLayoutController
            java.util.Objects.requireNonNull(r2)
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout r2 = r2.mView
            java.util.Objects.requireNonNull(r2)
            com.android.systemui.statusbar.EmptyShadeView r3 = r2.mEmptyShadeView
            int r3 = r3.getVisibility()
            r4 = 8
            if (r3 != r4) goto L_0x007a
            int r2 = r2.getMinExpansionHeight()
            float r2 = (float) r2
            goto L_0x007e
        L_0x007a:
            float r2 = r2.getAppearEndPosition()
        L_0x007e:
            int r6 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1))
            if (r6 >= 0) goto L_0x0083
            goto L_0x0084
        L_0x0083:
            r0 = r1
        L_0x0084:
            if (r0 == 0) goto L_0x008d
            boolean r6 = r5.isOnKeyguard()
            if (r6 == 0) goto L_0x008d
            r0 = r1
        L_0x008d:
            boolean r6 = r5.mShowIconsWhenExpanded
            if (r0 == r6) goto L_0x009a
            r5.mShowIconsWhenExpanded = r0
            com.android.systemui.statusbar.CommandQueue r6 = r5.mCommandQueue
            int r5 = r5.mDisplayId
            r6.recomputeDisableFlags(r5, r1)
        L_0x009a:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.NotificationPanelViewController.updateExpandedHeight(float):void");
    }

    public final void updateGestureExclusionRect() {
        Rect rect;
        List<Rect> list;
        Region calculateTouchableRegion = this.mStatusBarTouchableRegionManager.calculateTouchableRegion();
        if (!isFullyCollapsed() || calculateTouchableRegion == null) {
            rect = null;
        } else {
            rect = calculateTouchableRegion.getBounds();
        }
        if (rect == null) {
            rect = EMPTY_RECT;
        }
        NotificationPanelView notificationPanelView = this.mView;
        if (rect.isEmpty()) {
            list = Collections.EMPTY_LIST;
        } else {
            list = Collections.singletonList(rect);
        }
        notificationPanelView.setSystemGestureExclusionRects(list);
    }

    public final void updateKeyguardBottomAreaAlpha() {
        float f;
        int i;
        if (this.mHintAnimationRunning) {
            f = 0.0f;
        } else {
            f = 0.95f;
        }
        float min = Math.min(MathUtils.map(f, 1.0f, 0.0f, 1.0f, this.mExpandedFraction), 1.0f - computeQsExpansionFraction()) * this.mBottomAreaShadeAlpha;
        this.mKeyguardBottomArea.setAffordanceAlpha(min);
        KeyguardBottomAreaView keyguardBottomAreaView = this.mKeyguardBottomArea;
        if (min == 0.0f) {
            i = 4;
        } else {
            i = 0;
        }
        keyguardBottomAreaView.setImportantForAccessibility(i);
        StatusBar statusBar = this.mStatusBar;
        Objects.requireNonNull(statusBar);
        View view = statusBar.mAmbientIndicationContainer;
        if (view != null) {
            view.setAlpha(min);
        }
        LockIconViewController lockIconViewController = this.mLockIconViewController;
        Objects.requireNonNull(lockIconViewController);
        ((LockIconView) lockIconViewController.mView).setAlpha(min);
    }

    public final void updateKeyguardStatusViewAlignment(boolean z) {
        boolean z2;
        boolean z3;
        boolean z4;
        int i = 0;
        if (this.mNotificationStackScrollLayoutController.getVisibleNotificationCount() != 0 || this.mMediaDataManager.hasActiveMedia()) {
            z2 = true;
        } else {
            z2 = false;
        }
        WeakReference<CommunalSource> weakReference = this.mCommunalSource;
        if (weakReference == null || weakReference.get() == null) {
            z3 = false;
        } else {
            z3 = true;
        }
        if (!this.mShouldUseSplitNotificationShade || ((!z2 && !z3) || this.mDozing)) {
            z4 = true;
        } else {
            z4 = false;
        }
        if (this.mStatusViewCentered != z4) {
            this.mStatusViewCentered = z4;
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(this.mNotificationContainerParent);
            if (!z4) {
                i = 2131428648;
            }
            constraintSet.connect(2131428195, 7, i, 7);
            if (z) {
                ChangeBounds changeBounds = new ChangeBounds();
                changeBounds.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
                changeBounds.setDuration(360L);
                TransitionManager.beginDelayedTransition(this.mNotificationContainerParent, changeBounds);
            }
            constraintSet.applyTo(this.mNotificationContainerParent);
        }
        this.mKeyguardUnfoldTransition.ifPresent(new ShellCommandHandlerImpl$$ExternalSyntheticLambda3(this, 1));
    }

    public final void updateNotificationTranslucency() {
        float f;
        if (this.mClosingWithAlphaFadeOut && !this.mExpandingFromHeadsUp) {
            HeadsUpManagerPhone headsUpManagerPhone = this.mHeadsUpManager;
            Objects.requireNonNull(headsUpManagerPhone);
            if (!headsUpManagerPhone.mHasPinnedNotification) {
                f = getFadeoutAlpha();
                if (this.mBarState == 1 && !this.mHintAnimationRunning && !this.mKeyguardBypassController.getBypassEnabled()) {
                    f *= this.mClockPositionResult.clockAlpha;
                }
                NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mNotificationStackScrollLayoutController;
                Objects.requireNonNull(notificationStackScrollLayoutController);
                notificationStackScrollLayoutController.mView.setAlpha(f);
            }
        }
        f = 1.0f;
        if (this.mBarState == 1) {
            f *= this.mClockPositionResult.clockAlpha;
        }
        NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = this.mNotificationStackScrollLayoutController;
        Objects.requireNonNull(notificationStackScrollLayoutController2);
        notificationStackScrollLayoutController2.mView.setAlpha(f);
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0026  */
    /* JADX WARN: Removed duplicated region for block: B:12:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateQSExpansionEnabledAmbient() {
        /*
            r2 = this;
            com.android.systemui.statusbar.notification.stack.AmbientState r0 = r2.mAmbientState
            java.util.Objects.requireNonNull(r0)
            int r0 = r0.mTopPadding
            float r0 = (float) r0
            float r1 = r2.mQuickQsOffsetHeight
            float r0 = r0 - r1
            boolean r1 = r2.mShouldUseSplitNotificationShade
            if (r1 != 0) goto L_0x001e
            com.android.systemui.statusbar.notification.stack.AmbientState r1 = r2.mAmbientState
            java.util.Objects.requireNonNull(r1)
            int r1 = r1.mScrollY
            float r1 = (float) r1
            int r0 = (r1 > r0 ? 1 : (r1 == r0 ? 0 : -1))
            if (r0 > 0) goto L_0x001c
            goto L_0x001e
        L_0x001c:
            r0 = 0
            goto L_0x001f
        L_0x001e:
            r0 = 1
        L_0x001f:
            r2.mQsExpansionEnabledAmbient = r0
            com.android.systemui.plugins.qs.QS r0 = r2.mQs
            if (r0 != 0) goto L_0x0026
            goto L_0x002d
        L_0x0026:
            boolean r2 = r2.isQsExpansionEnabled()
            r0.setHeaderClickable(r2)
        L_0x002d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.NotificationPanelViewController.updateQSExpansionEnabledAmbient():void");
    }

    public final void updateQsExpansion$1() {
        float f;
        float f2;
        boolean z;
        float f3;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        if (this.mQs != null) {
            if (this.mQsExpandImmediate || this.mQsExpanded) {
                f = 1.0f;
            } else {
                LockscreenShadeTransitionController lockscreenShadeTransitionController = this.mLockscreenShadeTransitionController;
                Objects.requireNonNull(lockscreenShadeTransitionController);
                if (lockscreenShadeTransitionController.qSDragProgress > 0.0f) {
                    LockscreenShadeTransitionController lockscreenShadeTransitionController2 = this.mLockscreenShadeTransitionController;
                    Objects.requireNonNull(lockscreenShadeTransitionController2);
                    f = lockscreenShadeTransitionController2.qSDragProgress;
                } else {
                    NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mNotificationStackScrollLayoutController;
                    Objects.requireNonNull(notificationStackScrollLayoutController);
                    NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
                    Objects.requireNonNull(notificationStackScrollLayout);
                    StackScrollAlgorithm stackScrollAlgorithm = notificationStackScrollLayout.mStackScrollAlgorithm;
                    AmbientState ambientState = notificationStackScrollLayout.mAmbientState;
                    Objects.requireNonNull(stackScrollAlgorithm);
                    f = stackScrollAlgorithm.getExpansionFractionWithoutShelf(stackScrollAlgorithm.mTempAlgorithmState, ambientState);
                }
            }
            float computeQsExpansionFraction = computeQsExpansionFraction();
            if (this.mShouldUseSplitNotificationShade) {
                f2 = 1.0f;
            } else {
                f2 = computeQsExpansionFraction();
            }
            this.mQs.setQsExpansion(f2, this.mExpandedFraction, getHeaderTranslation(), f);
            MediaHierarchyManager mediaHierarchyManager = this.mMediaHierarchyManager;
            Objects.requireNonNull(mediaHierarchyManager);
            boolean z7 = true;
            if (mediaHierarchyManager.qsExpansion == computeQsExpansionFraction) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                mediaHierarchyManager.qsExpansion = computeQsExpansionFraction;
                MediaHierarchyManager.updateDesiredLocation$default(mediaHierarchyManager, false, 3);
                if (mediaHierarchyManager.getQSTransformationProgress() >= 0.0f) {
                    mediaHierarchyManager.updateTargetState();
                    mediaHierarchyManager.applyTargetStateIfNotAnimating();
                }
            }
            int calculateQsBottomPosition = calculateQsBottomPosition(computeQsExpansionFraction);
            ScrimController scrimController = this.mScrimController;
            Objects.requireNonNull(scrimController);
            if (!Float.isNaN(computeQsExpansionFraction)) {
                float notificationScrimAlpha = ShadeInterpolation.getNotificationScrimAlpha(computeQsExpansionFraction);
                if (calculateQsBottomPosition > 0) {
                    z5 = true;
                } else {
                    z5 = false;
                }
                if (!(scrimController.mQsExpansion == notificationScrimAlpha && scrimController.mQsBottomVisible == z5)) {
                    scrimController.mQsExpansion = notificationScrimAlpha;
                    scrimController.mQsBottomVisible = z5;
                    ScrimState scrimState = scrimController.mState;
                    if (scrimState == ScrimState.SHADE_LOCKED || scrimState == ScrimState.KEYGUARD || scrimState == ScrimState.PULSING) {
                        z6 = true;
                    } else {
                        z6 = false;
                    }
                    if (z6 && scrimController.mExpansionAffectsAlpha) {
                        scrimController.applyAndDispatchState();
                    }
                }
            }
            setQSClippingBounds();
            if (!this.mShouldUseSplitNotificationShade) {
                NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = this.mNotificationStackScrollLayoutController;
                Objects.requireNonNull(notificationStackScrollLayoutController2);
                NotificationStackScrollLayout notificationStackScrollLayout2 = notificationStackScrollLayoutController2.mView;
                Objects.requireNonNull(notificationStackScrollLayout2);
                float f4 = notificationStackScrollLayout2.mQsExpansionFraction;
                if (f4 == computeQsExpansionFraction || !(f4 == 1.0f || computeQsExpansionFraction == 1.0f)) {
                    z4 = false;
                } else {
                    z4 = true;
                }
                notificationStackScrollLayout2.mQsExpansionFraction = computeQsExpansionFraction;
                notificationStackScrollLayout2.updateUseRoundedRectClipping();
                int i = notificationStackScrollLayout2.mOwnScrollY;
                if (i > 0) {
                    notificationStackScrollLayout2.setOwnScrollY((int) MathUtils.lerp(i, 0, notificationStackScrollLayout2.mQsExpansionFraction), false);
                }
                if (z4) {
                    notificationStackScrollLayout2.updateFooter();
                }
            }
            NotificationShadeDepthController notificationShadeDepthController = this.mDepthController;
            Objects.requireNonNull(notificationShadeDepthController);
            if (Float.isNaN(computeQsExpansionFraction)) {
                Log.w("DepthController", "Invalid qs expansion");
            } else {
                if (notificationShadeDepthController.qsPanelExpansion == computeQsExpansionFraction) {
                    z3 = true;
                } else {
                    z3 = false;
                }
                if (!z3) {
                    notificationShadeDepthController.qsPanelExpansion = computeQsExpansionFraction;
                    notificationShadeDepthController.scheduleUpdate(null);
                }
            }
            if (this.mTransitioningToFullShadeProgress > 0.0f) {
                LockscreenShadeTransitionController lockscreenShadeTransitionController3 = this.mLockscreenShadeTransitionController;
                Objects.requireNonNull(lockscreenShadeTransitionController3);
                f3 = lockscreenShadeTransitionController3.qSDragProgress;
            } else {
                f3 = this.mExpandedFraction;
            }
            SplitShadeHeaderController splitShadeHeaderController = this.mSplitShadeHeaderController;
            Objects.requireNonNull(splitShadeHeaderController);
            if (splitShadeHeaderController.visible) {
                if (splitShadeHeaderController.shadeExpandedFraction == f3) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (!z2) {
                    splitShadeHeaderController.statusBar.setAlpha(ShadeInterpolation.getContentAlpha(f3));
                    splitShadeHeaderController.shadeExpandedFraction = f3;
                }
            }
            SplitShadeHeaderController splitShadeHeaderController2 = this.mSplitShadeHeaderController;
            Objects.requireNonNull(splitShadeHeaderController2);
            if (splitShadeHeaderController2.visible) {
                if (splitShadeHeaderController2.qsExpandedFraction != computeQsExpansionFraction) {
                    z7 = false;
                }
                if (!z7) {
                    splitShadeHeaderController2.qsExpandedFraction = computeQsExpansionFraction;
                    splitShadeHeaderController2.updateVisibility();
                    splitShadeHeaderController2.updatePosition$3();
                }
            }
            SplitShadeHeaderController splitShadeHeaderController3 = this.mSplitShadeHeaderController;
            boolean z8 = this.mQsVisible;
            Objects.requireNonNull(splitShadeHeaderController3);
            if (splitShadeHeaderController3.shadeExpanded != z8) {
                splitShadeHeaderController3.shadeExpanded = z8;
                if (z8) {
                    splitShadeHeaderController3.privacyIconsController.startListening();
                } else {
                    HeaderPrivacyIconsController headerPrivacyIconsController = splitShadeHeaderController3.privacyIconsController;
                    Objects.requireNonNull(headerPrivacyIconsController);
                    headerPrivacyIconsController.listening = false;
                    headerPrivacyIconsController.privacyItemController.removeCallback(headerPrivacyIconsController.picCallback);
                    headerPrivacyIconsController.privacyChipLogged = false;
                }
                splitShadeHeaderController3.updateVisibility();
                splitShadeHeaderController3.updatePosition$3();
            }
            CommunalHostViewController communalHostViewController = this.mCommunalViewController;
            if (communalHostViewController != null) {
                communalHostViewController.mQsExpansion = computeQsExpansionFraction;
                communalHostViewController.updateCommunalViewOccluded();
            }
        }
    }

    public final void updateQsState() {
        boolean z;
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mNotificationStackScrollLayoutController;
        boolean z2 = this.mQsExpanded;
        Objects.requireNonNull(notificationStackScrollLayoutController);
        NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
        Objects.requireNonNull(notificationStackScrollLayout);
        notificationStackScrollLayout.mQsExpanded = z2;
        notificationStackScrollLayout.updateAlgorithmLayoutMinHeight();
        boolean z3 = false;
        if (notificationStackScrollLayout.mQsExpanded || notificationStackScrollLayout.getScrollRange() <= 0) {
            z = false;
        } else {
            z = true;
        }
        if (z != notificationStackScrollLayout.mScrollable) {
            notificationStackScrollLayout.mScrollable = z;
            notificationStackScrollLayout.setFocusable(z);
            notificationStackScrollLayout.updateForwardAndBackwardScrollability();
        }
        notificationStackScrollLayoutController.updateShowEmptyShadeView();
        NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = this.mNotificationStackScrollLayoutController;
        if (this.mBarState != 1 && (!this.mQsExpanded || this.mQsExpansionFromOverscroll || this.mShouldUseSplitNotificationShade)) {
            z3 = true;
        }
        Objects.requireNonNull(notificationStackScrollLayoutController2);
        NotificationStackScrollLayout notificationStackScrollLayout2 = notificationStackScrollLayoutController2.mView;
        Objects.requireNonNull(notificationStackScrollLayout2);
        notificationStackScrollLayout2.mScrollingEnabled = z3;
        KeyguardUserSwitcherController keyguardUserSwitcherController = this.mKeyguardUserSwitcherController;
        if (keyguardUserSwitcherController != null && this.mQsExpanded && !this.mStackScrollerOverscrolling) {
            keyguardUserSwitcherController.closeSwitcherIfOpenAndNotSimple(true);
        }
        QS qs = this.mQs;
        if (qs != null) {
            qs.setExpanded(this.mQsExpanded);
        }
    }

    public final void updateResources() {
        int i;
        this.mQuickQsOffsetHeight = SystemBarUtils.getQuickQsOffsetHeight(this.mView.getContext());
        this.mSplitShadeStatusBarHeight = SystemBarUtils.getQuickQsOffsetHeight(this.mView.getContext());
        this.mSplitShadeNotificationsScrimMarginBottom = this.mResources.getDimensionPixelSize(2131167043);
        int dimensionPixelSize = this.mResources.getDimensionPixelSize(2131166901);
        int dimensionPixelSize2 = this.mResources.getDimensionPixelSize(2131166668);
        boolean shouldUseSplitNotificationShade = Utils.shouldUseSplitNotificationShade(this.mResources);
        this.mShouldUseSplitNotificationShade = shouldUseSplitNotificationShade;
        QS qs = this.mQs;
        if (qs != null) {
            qs.setInSplitShade(shouldUseSplitNotificationShade);
        }
        int dimensionPixelSize3 = this.mResources.getDimensionPixelSize(2131166664);
        if (this.mShouldUseSplitNotificationShade) {
            i = this.mSplitShadeStatusBarHeight;
        } else {
            i = this.mResources.getDimensionPixelSize(2131166665);
        }
        SplitShadeHeaderController splitShadeHeaderController = this.mSplitShadeHeaderController;
        boolean z = this.mShouldUseSplitNotificationShade;
        Objects.requireNonNull(splitShadeHeaderController);
        if (splitShadeHeaderController.splitShadeMode != z) {
            splitShadeHeaderController.splitShadeMode = z;
            if (z || splitShadeHeaderController.combinedHeaders) {
                splitShadeHeaderController.privacyIconsController.onParentVisible();
            } else {
                HeaderPrivacyIconsController headerPrivacyIconsController = splitShadeHeaderController.privacyIconsController;
                Objects.requireNonNull(headerPrivacyIconsController);
                headerPrivacyIconsController.chipVisibilityListener = null;
                headerPrivacyIconsController.privacyChip.setOnClickListener(null);
            }
            splitShadeHeaderController.updateVisibility();
            splitShadeHeaderController.updateConstraints();
        }
        NotificationsQuickSettingsContainer notificationsQuickSettingsContainer = this.mNotificationContainerParent;
        for (int i2 = 0; i2 < notificationsQuickSettingsContainer.getChildCount(); i2++) {
            View childAt = notificationsQuickSettingsContainer.getChildAt(i2);
            if (childAt.getId() == -1) {
                childAt.setId(View.generateViewId());
            }
        }
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this.mNotificationContainerParent);
        if (this.mShouldUseSplitNotificationShade) {
            constraintSet.connect(2131428651, 7, 2131428648, 7);
            constraintSet.connect(2131428521, 6, 2131428648, 6);
            constraintSet.get(2131428902).layout.mHeight = this.mSplitShadeStatusBarHeight;
            dimensionPixelSize = 0;
            dimensionPixelSize2 = 0;
        } else {
            constraintSet.connect(2131428651, 7, 0, 7);
            constraintSet.connect(2131428521, 6, 0, 6);
            if (this.mUseCombinedQSHeaders) {
                constraintSet.get(2131428902).layout.mHeight = -2;
            }
        }
        constraintSet.getConstraint(2131428521).layout.mWidth = dimensionPixelSize2;
        constraintSet.getConstraint(2131428651).layout.mWidth = dimensionPixelSize;
        constraintSet.setMargin(2131428521, 3, i);
        constraintSet.setMargin(2131428521, 4, dimensionPixelSize3);
        constraintSet.setMargin(2131428651, 3, i);
        constraintSet.applyTo(this.mNotificationContainerParent);
        AmbientState ambientState = this.mAmbientState;
        Objects.requireNonNull(ambientState);
        ambientState.mStackTopMargin = i;
        this.mNotificationsQSContainerController.updateMargins$2();
        NotificationsQSContainerController notificationsQSContainerController = this.mNotificationsQSContainerController;
        boolean z2 = this.mShouldUseSplitNotificationShade;
        Objects.requireNonNull(notificationsQSContainerController);
        if (notificationsQSContainerController.splitShadeEnabled != z2) {
            notificationsQSContainerController.splitShadeEnabled = z2;
            notificationsQSContainerController.updateBottomSpacing();
        }
        updateKeyguardStatusViewAlignment(false);
        this.mKeyguardMediaController.refreshMediaPosition();
    }

    public final void updateSystemUiStateFlags() {
        boolean z;
        SysUiState sysUiState = this.mSysUiState;
        if (!isFullyExpanded() || this.mQsExpanded) {
            z = false;
        } else {
            z = true;
        }
        sysUiState.setFlag(4, z);
        sysUiState.setFlag(2048, this.mQsExpanded);
        sysUiState.commitUpdate(this.mDisplayId);
    }

    public final void updateUserSwitcherFlags() {
        boolean z;
        boolean z2 = this.mResources.getBoolean(17891680);
        this.mKeyguardUserSwitcherEnabled = z2;
        if (!z2 || !this.mFeatureFlags.isEnabled(Flags.QS_USER_DETAIL_SHORTCUT)) {
            z = false;
        } else {
            z = true;
        }
        this.mKeyguardQsUserSwitchEnabled = z;
    }

    public final void updateViewControllers(KeyguardStatusView keyguardStatusView, FrameLayout frameLayout, KeyguardUserSwitcherView keyguardUserSwitcherView) {
        KeyguardStatusViewController keyguardStatusViewController = this.mKeyguardStatusViewComponentFactory.build(keyguardStatusView).getKeyguardStatusViewController();
        this.mKeyguardStatusViewController = keyguardStatusViewController;
        keyguardStatusViewController.init();
        KeyguardUserSwitcherController keyguardUserSwitcherController = this.mKeyguardUserSwitcherController;
        if (keyguardUserSwitcherController != null) {
            keyguardUserSwitcherController.closeSwitcherIfOpenAndNotSimple(false);
        }
        this.mKeyguardQsUserSwitchController = null;
        this.mKeyguardUserSwitcherController = null;
        if (frameLayout != null) {
            KeyguardQsUserSwitchController keyguardQsUserSwitchController = this.mKeyguardQsUserSwitchComponentFactory.build(frameLayout).getKeyguardQsUserSwitchController();
            this.mKeyguardQsUserSwitchController = keyguardQsUserSwitchController;
            keyguardQsUserSwitchController.init();
            this.mKeyguardStatusBarViewController.setKeyguardUserSwitcherEnabled(true);
        } else if (keyguardUserSwitcherView != null) {
            KeyguardUserSwitcherController keyguardUserSwitcherController2 = this.mKeyguardUserSwitcherComponentFactory.build(keyguardUserSwitcherView).getKeyguardUserSwitcherController();
            this.mKeyguardUserSwitcherController = keyguardUserSwitcherController2;
            keyguardUserSwitcherController2.init();
            this.mKeyguardStatusBarViewController.setKeyguardUserSwitcherEnabled(true);
        } else {
            this.mKeyguardStatusBarViewController.setKeyguardUserSwitcherEnabled(false);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x006f  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0085  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0088  */
    /* renamed from: -$$Nest$minitDownStates  reason: not valid java name */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void m100$$Nest$minitDownStates(com.android.systemui.statusbar.phone.NotificationPanelViewController r6, android.view.MotionEvent r7) {
        /*
            java.util.Objects.requireNonNull(r6)
            int r0 = r7.getActionMasked()
            r1 = 0
            if (r0 != 0) goto L_0x008b
            r6.mOnlyAffordanceInThisMotion = r1
            boolean r0 = r6.mQsFullyExpanded
            r6.mQsTouchAboveFalsingThreshold = r0
            boolean r0 = r6.mDozing
            r6.mDozingOnDown = r0
            float r0 = r7.getX()
            r6.mDownX = r0
            float r7 = r7.getY()
            r6.mDownY = r7
            boolean r7 = r6.isFullyCollapsed()
            r6.mCollapsedOnDown = r7
            float r0 = r6.mDownX
            float r2 = r6.mDownY
            r3 = 1
            if (r7 != 0) goto L_0x0068
            boolean r7 = r6.mKeyguardShowing
            if (r7 != 0) goto L_0x0068
            boolean r7 = r6.mQsExpanded
            if (r7 == 0) goto L_0x0036
            goto L_0x0068
        L_0x0036:
            com.android.systemui.plugins.qs.QS r7 = r6.mQs
            if (r7 != 0) goto L_0x003d
            com.android.systemui.statusbar.phone.KeyguardStatusBarView r7 = r6.mKeyguardStatusBar
            goto L_0x0041
        L_0x003d:
            android.view.View r7 = r7.getHeader()
        L_0x0041:
            android.widget.FrameLayout r4 = r6.mQsFrame
            float r4 = r4.getX()
            int r4 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r4 < 0) goto L_0x0068
            android.widget.FrameLayout r4 = r6.mQsFrame
            float r4 = r4.getX()
            android.widget.FrameLayout r5 = r6.mQsFrame
            int r5 = r5.getWidth()
            float r5 = (float) r5
            float r4 = r4 + r5
            int r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r0 > 0) goto L_0x0068
            int r7 = r7.getBottom()
            float r7 = (float) r7
            int r7 = (r2 > r7 ? 1 : (r2 == r7 ? 0 : -1))
            if (r7 > 0) goto L_0x0068
            r7 = r3
            goto L_0x0069
        L_0x0068:
            r7 = r1
        L_0x0069:
            r6.mIsPanelCollapseOnQQS = r7
            boolean r7 = r6.mCollapsedOnDown
            if (r7 == 0) goto L_0x007a
            com.android.systemui.statusbar.phone.HeadsUpManagerPhone r7 = r6.mHeadsUpManager
            java.util.Objects.requireNonNull(r7)
            boolean r7 = r7.mHasPinnedNotification
            if (r7 == 0) goto L_0x007a
            r7 = r3
            goto L_0x007b
        L_0x007a:
            r7 = r1
        L_0x007b:
            r6.mListenForHeadsUp = r7
            boolean r7 = r6.mExpectingSynthesizedDown
            r6.mAllowExpandForSmallExpansion = r7
            r6.mTouchSlopExceededBeforeDown = r7
            if (r7 == 0) goto L_0x0088
            r6.mLastEventSynthesizedDown = r3
            goto L_0x008d
        L_0x0088:
            r6.mLastEventSynthesizedDown = r1
            goto L_0x008d
        L_0x008b:
            r6.mLastEventSynthesizedDown = r1
        L_0x008d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.NotificationPanelViewController.m100$$Nest$minitDownStates(com.android.systemui.statusbar.phone.NotificationPanelViewController, android.view.MotionEvent):void");
    }

    /* renamed from: -$$Nest$mreInflateViews  reason: not valid java name */
    public static void m101$$Nest$mreInflateViews(NotificationPanelViewController notificationPanelViewController) {
        boolean z;
        boolean z2;
        Objects.requireNonNull(notificationPanelViewController);
        KeyguardStatusView keyguardStatusView = (KeyguardStatusView) notificationPanelViewController.mNotificationContainerParent.findViewById(2131428195);
        int indexOfChild = notificationPanelViewController.mNotificationContainerParent.indexOfChild(keyguardStatusView);
        notificationPanelViewController.mNotificationContainerParent.removeView(keyguardStatusView);
        KeyguardStatusView keyguardStatusView2 = (KeyguardStatusView) notificationPanelViewController.mLayoutInflater.inflate(2131624172, (ViewGroup) notificationPanelViewController.mNotificationContainerParent, false);
        notificationPanelViewController.mNotificationContainerParent.addView(keyguardStatusView2, indexOfChild);
        notificationPanelViewController.mStatusViewCentered = true;
        KeyguardMediaController keyguardMediaController = notificationPanelViewController.mKeyguardMediaController;
        Objects.requireNonNull(keyguardMediaController);
        keyguardMediaController.splitShadeContainer = (FrameLayout) keyguardStatusView2.findViewById(2131428935);
        keyguardMediaController.reattachHostView();
        keyguardMediaController.refreshMediaPosition();
        notificationPanelViewController.updateResources();
        notificationPanelViewController.updateUserSwitcherFlags();
        boolean isUserSwitcherEnabled = notificationPanelViewController.mUserManager.isUserSwitcherEnabled();
        boolean z3 = notificationPanelViewController.mKeyguardQsUserSwitchEnabled;
        if (!z3 || !isUserSwitcherEnabled) {
            z = false;
        } else {
            z = true;
        }
        if (z3 || !notificationPanelViewController.mKeyguardUserSwitcherEnabled || !isUserSwitcherEnabled) {
            z2 = false;
        } else {
            z2 = true;
        }
        notificationPanelViewController.updateViewControllers((KeyguardStatusView) notificationPanelViewController.mView.findViewById(2131428195), (FrameLayout) notificationPanelViewController.reInflateStub(2131428187, 2131428186, 2131624167, z), (KeyguardUserSwitcherView) notificationPanelViewController.reInflateStub(2131428198, 2131428197, 2131624173, z2));
        int indexOfChild2 = notificationPanelViewController.mView.indexOfChild(notificationPanelViewController.mKeyguardBottomArea);
        notificationPanelViewController.mView.removeView(notificationPanelViewController.mKeyguardBottomArea);
        KeyguardBottomAreaView keyguardBottomAreaView = notificationPanelViewController.mKeyguardBottomArea;
        KeyguardBottomAreaView keyguardBottomAreaView2 = (KeyguardBottomAreaView) notificationPanelViewController.mLayoutInflater.inflate(2131624152, (ViewGroup) notificationPanelViewController.mView, false);
        notificationPanelViewController.mKeyguardBottomArea = keyguardBottomAreaView2;
        Objects.requireNonNull(keyguardBottomAreaView2);
        keyguardBottomAreaView2.mStatusBar = keyguardBottomAreaView.mStatusBar;
        keyguardBottomAreaView2.updateCameraVisibility();
        if (keyguardBottomAreaView2.mAmbientIndicationArea != null) {
            View findViewById = keyguardBottomAreaView.findViewById(2131427484);
            ((ViewGroup) findViewById.getParent()).removeView(findViewById);
            ViewGroup viewGroup = (ViewGroup) keyguardBottomAreaView2.mAmbientIndicationArea.getParent();
            int indexOfChild3 = viewGroup.indexOfChild(keyguardBottomAreaView2.mAmbientIndicationArea);
            viewGroup.removeView(keyguardBottomAreaView2.mAmbientIndicationArea);
            viewGroup.addView(findViewById, indexOfChild3);
            keyguardBottomAreaView2.mAmbientIndicationArea = findViewById;
            keyguardBottomAreaView2.dozeTimeTick();
        }
        KeyguardBottomAreaView keyguardBottomAreaView3 = notificationPanelViewController.mKeyguardBottomArea;
        ViewGroup viewGroup2 = notificationPanelViewController.mPreviewContainer;
        Objects.requireNonNull(keyguardBottomAreaView3);
        keyguardBottomAreaView3.mPreviewContainer = viewGroup2;
        keyguardBottomAreaView3.inflateCameraPreview();
        keyguardBottomAreaView3.updateLeftAffordance();
        notificationPanelViewController.mView.addView(notificationPanelViewController.mKeyguardBottomArea, indexOfChild2);
        notificationPanelViewController.initBottomArea();
        notificationPanelViewController.mKeyguardIndicationController.setIndicationArea(notificationPanelViewController.mKeyguardBottomArea);
        notificationPanelViewController.mStatusBarStateListener.onDozeAmountChanged(notificationPanelViewController.mStatusBarStateController.getDozeAmount(), notificationPanelViewController.mStatusBarStateController.getInterpolatedDozeAmount());
        KeyguardStatusViewController keyguardStatusViewController = notificationPanelViewController.mKeyguardStatusViewController;
        int i = notificationPanelViewController.mBarState;
        Objects.requireNonNull(keyguardStatusViewController);
        keyguardStatusViewController.mKeyguardVisibilityHelper.setViewVisibility(i, false, false, i);
        KeyguardQsUserSwitchController keyguardQsUserSwitchController = notificationPanelViewController.mKeyguardQsUserSwitchController;
        if (keyguardQsUserSwitchController != null) {
            int i2 = notificationPanelViewController.mBarState;
            keyguardQsUserSwitchController.mKeyguardVisibilityHelper.setViewVisibility(i2, false, false, i2);
        }
        KeyguardUserSwitcherController keyguardUserSwitcherController = notificationPanelViewController.mKeyguardUserSwitcherController;
        if (keyguardUserSwitcherController != null) {
            int i3 = notificationPanelViewController.mBarState;
            keyguardUserSwitcherController.mKeyguardVisibilityHelper.setViewVisibility(i3, false, false, i3);
        }
        notificationPanelViewController.setKeyguardBottomAreaVisibility(notificationPanelViewController.mBarState, false);
        notificationPanelViewController.mKeyguardUnfoldTransition.ifPresent(new ActivityStarterDelegate$$ExternalSyntheticLambda0(notificationPanelViewController, 1));
        notificationPanelViewController.mNotificationPanelUnfoldAnimationController.ifPresent(new ShellCommandHandlerImpl$$ExternalSyntheticLambda1(notificationPanelViewController, 2));
    }

    /* renamed from: -$$Nest$mupdateMaxHeadsUpTranslation  reason: not valid java name */
    public static void m102$$Nest$mupdateMaxHeadsUpTranslation(NotificationPanelViewController notificationPanelViewController) {
        Objects.requireNonNull(notificationPanelViewController);
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = notificationPanelViewController.mNotificationStackScrollLayoutController;
        int height = notificationPanelViewController.mView.getHeight();
        int i = notificationPanelViewController.mNavigationBarBottomHeight;
        Objects.requireNonNull(notificationStackScrollLayoutController);
        NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
        Objects.requireNonNull(notificationStackScrollLayout);
        AmbientState ambientState = notificationStackScrollLayout.mAmbientState;
        Objects.requireNonNull(ambientState);
        ambientState.mMaxHeadsUpTranslation = height - i;
        StackStateAnimator stackStateAnimator = notificationStackScrollLayout.mStateAnimator;
        Objects.requireNonNull(stackStateAnimator);
        stackStateAnimator.mHeadsUpAppearHeightBottom = height;
        notificationStackScrollLayout.requestChildrenUpdate();
    }

    /* renamed from: -$$Nest$mupdateQSMinHeight  reason: not valid java name */
    public static void m103$$Nest$mupdateQSMinHeight(NotificationPanelViewController notificationPanelViewController) {
        int i;
        Objects.requireNonNull(notificationPanelViewController);
        float f = notificationPanelViewController.mQsMinExpansionHeight;
        if (notificationPanelViewController.mKeyguardShowing) {
            i = 0;
        } else {
            i = notificationPanelViewController.mQs.getQsMinExpansionHeight();
        }
        notificationPanelViewController.mQsMinExpansionHeight = i;
        if (notificationPanelViewController.mQsExpansionHeight == f) {
            notificationPanelViewController.mQsExpansionHeight = i;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x003c A[RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean canShowViewOnLockscreen(com.android.systemui.statusbar.notification.row.ExpandableView r6) {
        /*
            r5 = this;
            java.util.Objects.requireNonNull(r6)
            boolean r0 = r6 instanceof com.android.systemui.statusbar.NotificationShelf
            r1 = 0
            if (r0 == 0) goto L_0x0009
            return r1
        L_0x0009:
            boolean r0 = r6 instanceof com.android.systemui.statusbar.notification.row.ExpandableNotificationRow
            r2 = 1
            if (r0 == 0) goto L_0x003d
            r0 = r6
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r0 = (com.android.systemui.statusbar.notification.row.ExpandableNotificationRow) r0
            com.android.systemui.statusbar.notification.collection.legacy.NotificationGroupManagerLegacy r3 = r5.mGroupManager
            if (r3 == 0) goto L_0x0024
            com.android.systemui.statusbar.notification.collection.NotificationEntry r4 = r0.mEntry
            java.util.Objects.requireNonNull(r4)
            android.service.notification.StatusBarNotification r4 = r4.mSbn
            boolean r3 = r3.isSummaryOfSuppressedGroup(r4)
            if (r3 == 0) goto L_0x0024
            r3 = r2
            goto L_0x0025
        L_0x0024:
            r3 = r1
        L_0x0025:
            if (r3 == 0) goto L_0x0029
        L_0x0027:
            r5 = r1
            goto L_0x003a
        L_0x0029:
            com.android.systemui.statusbar.NotificationLockscreenUserManager r5 = r5.mLockscreenUserManager
            com.android.systemui.statusbar.notification.collection.NotificationEntry r3 = r0.mEntry
            boolean r5 = r5.shouldShowOnKeyguard(r3)
            if (r5 != 0) goto L_0x0034
            goto L_0x0027
        L_0x0034:
            boolean r5 = r0.mRemoved
            if (r5 == 0) goto L_0x0039
            goto L_0x0027
        L_0x0039:
            r5 = r2
        L_0x003a:
            if (r5 != 0) goto L_0x003d
            return r1
        L_0x003d:
            int r5 = r6.getVisibility()
            r6 = 8
            if (r5 != r6) goto L_0x0046
            return r1
        L_0x0046:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.NotificationPanelViewController.canShowViewOnLockscreen(com.android.systemui.statusbar.notification.row.ExpandableView):boolean");
    }

    public final void collapse(boolean z, float f) {
        if (canPanelBeCollapsed()) {
            if (this.mQsExpanded) {
                this.mQsExpandImmediate = true;
                this.mNotificationStackScrollLayoutController.setShouldShowShelfOnly(true);
            }
            if (canPanelBeCollapsed()) {
                cancelHeightAnimator();
                notifyExpandingStarted();
                setIsClosing(true);
                if (z) {
                    this.mNextCollapseSpeedUpFactor = f;
                    super.mView.postDelayed(this.mFlingCollapseRunnable, 120L);
                    return;
                }
                fling(0.0f, false, f, false);
            }
        }
    }

    @Override // com.android.systemui.statusbar.phone.PanelViewController
    public final void expand(boolean z) {
        if (isFullyCollapsed() || isCollapsing()) {
            this.mInstantExpanding = true;
            this.mAnimateAfterExpanding = z;
            this.mUpdateFlingOnLayout = false;
            cancelHeightAnimator();
            super.mView.removeCallbacks(this.mFlingCollapseRunnable);
            if (this.mTracking) {
                onTrackingStopped(true);
            }
            if (this.mExpanding) {
                notifyExpandingFinished();
            }
            updatePanelExpansionAndVisibility();
            super.mView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.android.systemui.statusbar.phone.PanelViewController.6
                @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
                public final void onGlobalLayout() {
                    PanelViewController panelViewController = this;
                    if (!panelViewController.mInstantExpanding) {
                        panelViewController.mView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        return;
                    }
                    StatusBar statusBar = panelViewController.mStatusBar;
                    Objects.requireNonNull(statusBar);
                    if (statusBar.mNotificationShadeWindowView.isVisibleToUser()) {
                        this.mView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        PanelViewController panelViewController2 = this;
                        if (panelViewController2.mAnimateAfterExpanding) {
                            panelViewController2.notifyExpandingStarted();
                            this.beginJankMonitoring();
                            NotificationPanelViewController notificationPanelViewController = (NotificationPanelViewController) this;
                            Objects.requireNonNull(notificationPanelViewController);
                            Objects.requireNonNull(notificationPanelViewController.mStatusBar);
                            notificationPanelViewController.fling(0.0f, true, 1.0f, false);
                        } else {
                            panelViewController2.setExpandedHeightInternal(panelViewController2.getMaxPanelHeight() * 1.0f);
                        }
                        this.mInstantExpanding = false;
                    }
                }
            });
            super.mView.requestLayout();
        }
        setListening(true);
    }

    public final void expandWithQs() {
        if (isQsExpansionEnabled()) {
            this.mQsExpandImmediate = true;
            this.mNotificationStackScrollLayoutController.setShouldShowShelfOnly(true);
        }
        if (isFullyCollapsed()) {
            expand(true);
            return;
        }
        traceQsJank(true, false);
        flingSettings(0.0f, 0, null, false);
    }

    public final void setQSClippingBounds() {
        final boolean z;
        int i;
        int i2;
        int i3;
        int i4;
        int calculateQsBottomPosition = calculateQsBottomPosition(computeQsExpansionFraction());
        if (computeQsExpansionFraction() > 0.0f || calculateQsBottomPosition > 0) {
            z = true;
        } else {
            z = false;
        }
        if (this.mShouldUseSplitNotificationShade) {
            i = Math.min(calculateQsBottomPosition, this.mSplitShadeStatusBarHeight);
        } else {
            if (this.mTransitioningToFullShadeProgress > 0.0f) {
                calculateQsBottomPosition = this.mTransitionToFullShadeQSPosition;
            } else {
                float qSEdgePosition = getQSEdgePosition();
                if (!isOnKeyguard()) {
                    calculateQsBottomPosition = (int) qSEdgePosition;
                } else if (!this.mKeyguardBypassController.getBypassEnabled()) {
                    calculateQsBottomPosition = (int) Math.min(calculateQsBottomPosition, qSEdgePosition);
                }
            }
            i = (int) (calculateQsBottomPosition + this.mOverStretchAmount);
            float f = this.mMinFraction;
            if (f > 0.0f && f < 1.0f) {
                i = (int) (MathUtils.saturate(((this.mExpandedFraction - f) / (1.0f - f)) / f) * i);
            }
        }
        if (this.mShouldUseSplitNotificationShade) {
            i2 = (this.mNotificationStackScrollLayoutController.getHeight() + i) - this.mSplitShadeNotificationsScrimMarginBottom;
        } else {
            i2 = super.mView.getBottom();
        }
        if (this.mIsFullWidth) {
            i3 = 0;
        } else {
            NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mNotificationStackScrollLayoutController;
            Objects.requireNonNull(notificationStackScrollLayoutController);
            i3 = notificationStackScrollLayoutController.mView.getLeft();
        }
        if (this.mIsFullWidth) {
            i4 = super.mView.getRight() + this.mDisplayRightInset;
        } else {
            NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = this.mNotificationStackScrollLayoutController;
            Objects.requireNonNull(notificationStackScrollLayoutController2);
            i4 = notificationStackScrollLayoutController2.mView.getRight();
        }
        int min = Math.min(i, i2);
        if (this.mAnimateNextNotificationBounds && !this.mKeyguardStatusAreaClipBounds.isEmpty()) {
            this.mQsClippingAnimationEndBounds.set(i3, min, i4, i2);
            Rect rect = this.mKeyguardStatusAreaClipBounds;
            final int i5 = rect.left;
            final int i6 = rect.top;
            final int i7 = rect.right;
            final int i8 = rect.bottom;
            ValueAnimator valueAnimator = this.mQsClippingAnimation;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            this.mQsClippingAnimation = ofFloat;
            ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
            this.mQsClippingAnimation.setDuration(this.mNotificationBoundsAnimationDuration);
            this.mQsClippingAnimation.setStartDelay(this.mNotificationBoundsAnimationDelay);
            this.mQsClippingAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController$$ExternalSyntheticLambda1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
                    int i9 = i5;
                    int i10 = i6;
                    int i11 = i7;
                    int i12 = i8;
                    boolean z2 = z;
                    Objects.requireNonNull(notificationPanelViewController);
                    float animatedFraction = valueAnimator2.getAnimatedFraction();
                    notificationPanelViewController.applyQSClippingImmediately((int) MathUtils.lerp(i9, notificationPanelViewController.mQsClippingAnimationEndBounds.left, animatedFraction), (int) MathUtils.lerp(i10, notificationPanelViewController.mQsClippingAnimationEndBounds.top, animatedFraction), (int) MathUtils.lerp(i11, notificationPanelViewController.mQsClippingAnimationEndBounds.right, animatedFraction), (int) MathUtils.lerp(i12, notificationPanelViewController.mQsClippingAnimationEndBounds.bottom, animatedFraction), z2);
                }
            });
            this.mQsClippingAnimation.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController.9
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
                    notificationPanelViewController.mQsClippingAnimation = null;
                    notificationPanelViewController.mIsQsTranslationResetAnimator = false;
                    notificationPanelViewController.mIsPulseExpansionResetAnimator = false;
                }
            });
            this.mQsClippingAnimation.start();
        } else if (this.mQsClippingAnimation != null) {
            this.mQsClippingAnimationEndBounds.set(i3, min, i4, i2);
        } else {
            applyQSClippingImmediately(i3, min, i4, i2, z);
        }
        this.mAnimateNextNotificationBounds = false;
        this.mNotificationBoundsAnimationDelay = 0L;
    }

    /* JADX WARN: Removed duplicated region for block: B:55:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean shouldQuickSettingsIntercept(float r8, float r9, float r10) {
        /*
            Method dump skipped, instructions count: 229
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.NotificationPanelViewController.shouldQuickSettingsIntercept(float, float, float):boolean");
    }

    public final void updatePanelExpanded() {
        boolean z;
        QS qs;
        if (!isFullyCollapsed() || this.mExpectingSynthesizedDown) {
            z = true;
        } else {
            z = false;
        }
        if (this.mPanelExpanded != z) {
            HeadsUpManagerPhone headsUpManagerPhone = this.mHeadsUpManager;
            Objects.requireNonNull(headsUpManagerPhone);
            if (z != headsUpManagerPhone.mIsExpanded) {
                headsUpManagerPhone.mIsExpanded = z;
                if (z) {
                    headsUpManagerPhone.mHeadsUpGoingAway = false;
                }
            }
            StatusBarTouchableRegionManager statusBarTouchableRegionManager = this.mStatusBarTouchableRegionManager;
            Objects.requireNonNull(statusBarTouchableRegionManager);
            if (z != statusBarTouchableRegionManager.mIsStatusBarExpanded) {
                statusBarTouchableRegionManager.mIsStatusBarExpanded = z;
                if (z) {
                    statusBarTouchableRegionManager.mForceCollapsedUntilLayout = false;
                }
                statusBarTouchableRegionManager.updateTouchableRegion();
            }
            StatusBar statusBar = this.mStatusBar;
            Objects.requireNonNull(statusBar);
            if (statusBar.mPanelExpanded != z) {
                NotificationLogger notificationLogger = statusBar.mNotificationLogger;
                Objects.requireNonNull(notificationLogger);
                notificationLogger.mPanelExpanded = Boolean.valueOf(z);
                synchronized (notificationLogger.mDozingLock) {
                    notificationLogger.maybeUpdateLoggingStatus();
                }
            }
            statusBar.mPanelExpanded = z;
            StatusBarHideIconsForBouncerManager statusBarHideIconsForBouncerManager = statusBar.mStatusBarHideIconsForBouncerManager;
            Objects.requireNonNull(statusBarHideIconsForBouncerManager);
            statusBarHideIconsForBouncerManager.panelExpanded = z;
            statusBarHideIconsForBouncerManager.updateHideIconsForBouncer(false);
            statusBar.mNotificationShadeWindowController.setPanelExpanded(z);
            statusBar.mStatusBarStateController.setPanelExpanded(z);
            if (z && statusBar.mStatusBarStateController.getState() != 1) {
                try {
                    statusBar.mBarService.clearNotificationEffects();
                } catch (RemoteException unused) {
                }
            }
            if (!z) {
                NotificationRemoteInputManager notificationRemoteInputManager = statusBar.mRemoteInputManager;
                Objects.requireNonNull(notificationRemoteInputManager);
                NotificationRemoteInputManager.RemoteInputListener remoteInputListener = notificationRemoteInputManager.mRemoteInputListener;
                if (remoteInputListener != null) {
                    remoteInputListener.onPanelCollapsed();
                }
            }
            this.mPanelExpanded = z;
            if (!z && (qs = this.mQs) != null && qs.isCustomizing()) {
                this.mQs.closeCustomizer();
            }
        }
    }
}

package com.android.systemui.navigationbar;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.inputmethodservice.InputMethodService;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowManagerGlobal;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import androidx.leanback.R$color;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.Dependency;
import com.android.systemui.RegionInterceptingFrameLayout$$ExternalSyntheticLambda0;
import com.android.systemui.accessibility.SystemActions$$ExternalSyntheticLambda1;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.navigationbar.buttons.ButtonDispatcher;
import com.android.systemui.navigationbar.buttons.ButtonInterface;
import com.android.systemui.navigationbar.buttons.ContextualButton;
import com.android.systemui.navigationbar.buttons.ContextualButtonGroup;
import com.android.systemui.navigationbar.buttons.DeadZone;
import com.android.systemui.navigationbar.buttons.KeyButtonDrawable;
import com.android.systemui.navigationbar.buttons.NearestTouchFrame;
import com.android.systemui.navigationbar.buttons.RotationContextButton;
import com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler;
import com.android.systemui.plugins.NavigationEdgeBackPlugin;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.recents.Recents;
import com.android.systemui.shared.navigationbar.RegionSamplingHelper;
import com.android.systemui.shared.recents.IOverviewProxy;
import com.android.systemui.shared.rotation.FloatingRotationButton;
import com.android.systemui.shared.rotation.RotationButton;
import com.android.systemui.shared.rotation.RotationButtonController;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.SysUiStatsLog;
import com.android.systemui.shared.system.TaskStackChangeListeners;
import com.android.systemui.shared.system.WindowManagerWrapper;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.phone.AutoHideController;
import com.android.systemui.statusbar.phone.BarTransitions;
import com.android.systemui.statusbar.phone.LightBarTransitionsController;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.util.Utils;
import com.android.wifitrackerlib.BaseWifiTracker$$ExternalSyntheticLambda1;
import com.android.wm.shell.ShellTaskOrganizer$$ExternalSyntheticLambda0;
import com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda9;
import com.android.wm.shell.pip.phone.PipMotionHelper$$ExternalSyntheticLambda1;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class NavigationBarView extends FrameLayout implements NavigationModeController.ModeChangedListener {
    public static final /* synthetic */ int $r8$clinit = 0;
    public AutoHideController mAutoHideController;
    public KeyButtonDrawable mBackIcon;
    public final SparseArray<ButtonDispatcher> mButtonDispatchers;
    public final ContextualButtonGroup mContextualButtonGroup;
    public int mDarkIconColor;
    public KeyButtonDrawable mDockedIcon;
    public boolean mDockedStackExists;
    public EdgeBackGestureHandler mEdgeBackGestureHandler;
    public KeyButtonDrawable mHomeDefaultIcon;
    public View mHorizontal;
    public boolean mImeDrawsImeNavBar;
    public boolean mImeVisible;
    public ContextThemeWrapper mLightContext;
    public int mLightIconColor;
    public NavigationBarOverlayController mNavBarOverlayController;
    public final NavigationBarView$$ExternalSyntheticLambda2 mNavbarOverlayVisibilityChangeCallback;
    public NavigationBarInflaterView mNavigationInflaterView;
    public OnVerticalChangedListener mOnVerticalChangedListener;
    public Rect mOrientedHandleSamplingRegion;
    public NotificationPanelViewController mPanelView;
    public KeyButtonDrawable mRecentIcon;
    public RotationButtonController mRotationButtonController;
    public RotationContextButton mRotationContextButton;
    public View mVertical;
    public boolean mWakeAndUnlocking;
    public View mCurrentView = null;
    public int mCurrentRotation = -1;
    public int mDisabledFlags = 0;
    public int mNavigationIconHints = 0;
    public final Region mTmpRegion = new Region();
    public final int[] mTmpPosition = new int[2];
    public Rect mTmpBounds = new Rect();
    public HashMap mButtonFullTouchableRegions = new HashMap();
    public boolean mDeadZoneConsuming = false;
    public final NavTransitionListener mTransitionListener = new NavTransitionListener();
    public boolean mLayoutTransitionsEnabled = true;
    public boolean mInCarMode = false;
    public boolean mScreenOn = true;
    public Optional<Recents> mRecentsOptional = Optional.empty();
    public Rect mSamplingBounds = new Rect();
    public final boolean mImeCanRenderGesturalNavButtons = InputMethodService.canImeRenderGesturalNavButtons();
    public final AnonymousClass1 mQuickStepAccessibilityDelegate = new View.AccessibilityDelegate() { // from class: com.android.systemui.navigationbar.NavigationBarView.1
        public AccessibilityNodeInfo.AccessibilityAction mToggleOverviewAction;

        @Override // android.view.View.AccessibilityDelegate
        public final void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
            if (this.mToggleOverviewAction == null) {
                this.mToggleOverviewAction = new AccessibilityNodeInfo.AccessibilityAction(2131427454, NavigationBarView.this.getContext().getString(2131953148));
            }
            accessibilityNodeInfo.addAction(this.mToggleOverviewAction);
        }

        @Override // android.view.View.AccessibilityDelegate
        public final boolean performAccessibilityAction(View view, int i, Bundle bundle) {
            if (i != 2131427454) {
                return super.performAccessibilityAction(view, i, bundle);
            }
            NavigationBarView.this.mRecentsOptional.ifPresent(SystemActions$$ExternalSyntheticLambda1.INSTANCE);
            return true;
        }
    };
    public final RegionInterceptingFrameLayout$$ExternalSyntheticLambda0 mOnComputeInternalInsetsListener = new RegionInterceptingFrameLayout$$ExternalSyntheticLambda0(this, 1);
    public final AnonymousClass2 mRotationButtonListener = new AnonymousClass2();
    public final BubbleController$$ExternalSyntheticLambda9 mDockedListener = new BubbleController$$ExternalSyntheticLambda9(this, 2);
    public final PipMotionHelper$$ExternalSyntheticLambda1 mPipListener = new PipMotionHelper$$ExternalSyntheticLambda1(this, 1);
    public boolean mIsVertical = false;
    public int mNavBarMode = ((NavigationModeController) Dependency.get(NavigationModeController.class)).addListener(this);
    public final SysUiState mSysUiFlagContainer = (SysUiState) Dependency.get(SysUiState.class);
    public FloatingRotationButton mFloatingRotationButton = new FloatingRotationButton(((FrameLayout) this).mContext);
    public final OverviewProxyService mOverviewProxyService = (OverviewProxyService) Dependency.get(OverviewProxyService.class);
    public Configuration mConfiguration = new Configuration();
    public Configuration mTmpLastConfiguration = new Configuration();
    public ScreenPinningNotify mScreenPinningNotify = new ScreenPinningNotify(((FrameLayout) this).mContext);
    public final NavigationBarTransitions mBarTransitions = new NavigationBarTransitions(this, (CommandQueue) Dependency.get(CommandQueue.class));
    public final DeadZone mDeadZone = new DeadZone(this);
    public final int mNavColorSampleMargin = getResources().getDimensionPixelSize(2131166606);
    public final RegionSamplingHelper mRegionSamplingHelper = new RegionSamplingHelper(this, new RegionSamplingHelper.SamplingCallback() { // from class: com.android.systemui.navigationbar.NavigationBarView.3
        @Override // com.android.systemui.shared.navigationbar.RegionSamplingHelper.SamplingCallback
        public final Rect getSampledRegion() {
            int i;
            NavigationBarView navigationBarView = NavigationBarView.this;
            Rect rect = navigationBarView.mOrientedHandleSamplingRegion;
            if (rect != null) {
                return rect;
            }
            navigationBarView.mSamplingBounds.setEmpty();
            ButtonDispatcher buttonDispatcher = navigationBarView.mButtonDispatchers.get(2131428097);
            Objects.requireNonNull(buttonDispatcher);
            View view = buttonDispatcher.mCurrentView;
            if (view != null) {
                int[] iArr = new int[2];
                view.getLocationOnScreen(iArr);
                Point point = new Point();
                view.getContext().getDisplay().getRealSize(point);
                int i2 = iArr[0] - navigationBarView.mNavColorSampleMargin;
                int i3 = point.y;
                if (navigationBarView.mIsVertical) {
                    i = navigationBarView.getResources().getDimensionPixelSize(17105366);
                } else {
                    i = navigationBarView.getResources().getDimensionPixelSize(17105364);
                }
                navigationBarView.mSamplingBounds.set(new Rect(i2, i3 - i, view.getWidth() + iArr[0] + navigationBarView.mNavColorSampleMargin, point.y));
            }
            return NavigationBarView.this.mSamplingBounds;
        }

        @Override // com.android.systemui.shared.navigationbar.RegionSamplingHelper.SamplingCallback
        public final boolean isSamplingEnabled() {
            return Utils.isGesturalModeOnDefaultDisplay(NavigationBarView.this.getContext(), NavigationBarView.this.mNavBarMode);
        }

        @Override // com.android.systemui.shared.navigationbar.RegionSamplingHelper.SamplingCallback
        public final void onRegionDarknessChanged(boolean z) {
            NavigationBarView.this.getLightTransitionsController().setIconsDark(!z, true);
        }
    }, (Executor) Dependency.get(Dependency.BACKGROUND_EXECUTOR));

    /* renamed from: com.android.systemui.navigationbar.NavigationBarView$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass2 implements RotationButton.RotationButtonUpdatesCallback {
        public AnonymousClass2() {
        }

        public final void onVisibilityChanged(boolean z) {
            AutoHideController autoHideController;
            if (z && (autoHideController = NavigationBarView.this.mAutoHideController) != null) {
                autoHideController.touchAutoHide();
            }
            NavigationBarView.this.notifyActiveTouchRegions();
        }
    }

    /* loaded from: classes.dex */
    public class NavTransitionListener implements LayoutTransition.TransitionListener {
        public boolean mBackTransitioning;
        public long mDuration;
        public boolean mHomeAppearing;
        public TimeInterpolator mInterpolator;
        public long mStartDelay;

        public NavTransitionListener() {
        }

        @Override // android.animation.LayoutTransition.TransitionListener
        public final void endTransition(LayoutTransition layoutTransition, ViewGroup viewGroup, View view, int i) {
            if (view.getId() == 2131427535) {
                this.mBackTransitioning = false;
            } else if (view.getId() == 2131428094 && i == 2) {
                this.mHomeAppearing = false;
            }
        }

        @Override // android.animation.LayoutTransition.TransitionListener
        public final void startTransition(LayoutTransition layoutTransition, ViewGroup viewGroup, View view, int i) {
            if (view.getId() == 2131427535) {
                this.mBackTransitioning = true;
            } else if (view.getId() == 2131428094 && i == 2) {
                this.mHomeAppearing = true;
                this.mStartDelay = layoutTransition.getStartDelay(i);
                this.mDuration = layoutTransition.getDuration(i);
                this.mInterpolator = layoutTransition.getInterpolator(i);
            }
        }
    }

    /* loaded from: classes.dex */
    public interface OnVerticalChangedListener {
    }

    public static String visibilityToString(int i) {
        return i != 4 ? i != 8 ? "VISIBLE" : "GONE" : "INVISIBLE";
    }

    public final Region getButtonLocations(boolean z, boolean z2, boolean z3) {
        FrameLayout frameLayout;
        if (z3 && !z2) {
            z3 = false;
        }
        this.mTmpRegion.setEmpty();
        if (this.mIsVertical) {
            frameLayout = this.mNavigationInflaterView.mVertical;
        } else {
            frameLayout = this.mNavigationInflaterView.mHorizontal;
        }
        NearestTouchFrame nearestTouchFrame = (NearestTouchFrame) frameLayout.findViewById(2131428478);
        Objects.requireNonNull(nearestTouchFrame);
        HashMap hashMap = new HashMap(nearestTouchFrame.mTouchableRegions.size());
        nearestTouchFrame.getLocationOnScreen(nearestTouchFrame.mTmpInt);
        for (Map.Entry entry : nearestTouchFrame.mTouchableRegions.entrySet()) {
            Rect rect = new Rect((Rect) entry.getValue());
            int[] iArr = nearestTouchFrame.mTmpInt;
            rect.offset(iArr[0], iArr[1]);
            hashMap.put((View) entry.getKey(), rect);
        }
        this.mButtonFullTouchableRegions = hashMap;
        updateButtonLocation(getBackButton(), z2, z3);
        updateButtonLocation(getHomeButton(), z2, z3);
        updateButtonLocation(getRecentsButton(), z2, z3);
        updateButtonLocation(this.mButtonDispatchers.get(2131428119), z2, z3);
        updateButtonLocation(this.mButtonDispatchers.get(2131427368), z2, z3);
        if (z) {
            FloatingRotationButton floatingRotationButton = this.mFloatingRotationButton;
            Objects.requireNonNull(floatingRotationButton);
            if (floatingRotationButton.mIsShowing) {
                FloatingRotationButton floatingRotationButton2 = this.mFloatingRotationButton;
                Objects.requireNonNull(floatingRotationButton2);
                updateButtonLocation(floatingRotationButton2.mKeyButtonView, z2);
                if (z && this.mNavBarOverlayController.isNavigationBarOverlayEnabled() && this.mNavBarOverlayController.isVisible()) {
                    updateButtonLocation(this.mNavBarOverlayController.getCurrentView(), z2);
                }
                return this.mTmpRegion;
            }
        }
        updateButtonLocation((RotationContextButton) this.mButtonDispatchers.get(2131428715), z2, z3);
        if (z) {
            updateButtonLocation(this.mNavBarOverlayController.getCurrentView(), z2);
        }
        return this.mTmpRegion;
    }

    public final void updateButtonLocation(ButtonDispatcher buttonDispatcher, boolean z, boolean z2) {
        View view;
        if (buttonDispatcher != null && (view = buttonDispatcher.mCurrentView) != null && buttonDispatcher.isVisible()) {
            if (!z2 || !this.mButtonFullTouchableRegions.containsKey(view)) {
                updateButtonLocation(view, z);
            } else {
                this.mTmpRegion.op((Rect) this.mButtonFullTouchableRegions.get(view), Region.Op.UNION);
            }
        }
    }

    /* JADX WARN: Type inference failed for: r7v3, types: [com.android.systemui.navigationbar.NavigationBarView$1] */
    public NavigationBarView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        SparseArray<ButtonDispatcher> sparseArray = new SparseArray<>();
        this.mButtonDispatchers = sparseArray;
        NavigationBarView$$ExternalSyntheticLambda2 navigationBarView$$ExternalSyntheticLambda2 = new NavigationBarView$$ExternalSyntheticLambda2(this, 0);
        this.mNavbarOverlayVisibilityChangeCallback = navigationBarView$$ExternalSyntheticLambda2;
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, com.android.settingslib.Utils.getThemeAttr(context, 2130968913));
        ContextThemeWrapper contextThemeWrapper2 = new ContextThemeWrapper(context, com.android.settingslib.Utils.getThemeAttr(context, 2130969364));
        this.mLightContext = contextThemeWrapper2;
        this.mLightIconColor = com.android.settingslib.Utils.getColorAttrDefaultColor(contextThemeWrapper2, 2130969739);
        this.mDarkIconColor = com.android.settingslib.Utils.getColorAttrDefaultColor(contextThemeWrapper, 2130969739);
        NavigationModeController navigationModeController = (NavigationModeController) Dependency.get(NavigationModeController.class);
        Objects.requireNonNull(navigationModeController);
        this.mImeDrawsImeNavBar = navigationModeController.mCurrentUserContext.getResources().getBoolean(17891674);
        ContextualButtonGroup contextualButtonGroup = new ContextualButtonGroup();
        this.mContextualButtonGroup = contextualButtonGroup;
        ContextualButton contextualButton = new ContextualButton(2131428119, this.mLightContext, 2131232007);
        ContextualButton contextualButton2 = new ContextualButton(2131427368, this.mLightContext, 2131232278);
        contextualButtonGroup.addButton(contextualButton);
        contextualButtonGroup.addButton(contextualButton2);
        this.mRotationContextButton = new RotationContextButton(this.mLightContext);
        this.mRotationButtonController = new RotationButtonController(this.mLightContext, this.mLightIconColor, this.mDarkIconColor, new NavigationBarView$$ExternalSyntheticLambda3(this, 0));
        updateRotationButton();
        this.mConfiguration.updateFrom(context.getResources().getConfiguration());
        sparseArray.put(2131427535, new ButtonDispatcher(2131427535));
        sparseArray.put(2131428094, new ButtonDispatcher(2131428094));
        sparseArray.put(2131428097, new ButtonDispatcher(2131428097));
        sparseArray.put(2131428669, new ButtonDispatcher(2131428669));
        sparseArray.put(2131428119, contextualButton);
        sparseArray.put(2131427368, contextualButton2);
        sparseArray.put(2131428365, contextualButtonGroup);
        EdgeBackGestureHandler create = ((EdgeBackGestureHandler.Factory) Dependency.get(EdgeBackGestureHandler.Factory.class)).create(((FrameLayout) this).mContext);
        this.mEdgeBackGestureHandler = create;
        create.mStateChangeCallback = new BaseWifiTracker$$ExternalSyntheticLambda1(this, 2);
        NavigationBarOverlayController navigationBarOverlayController = (NavigationBarOverlayController) Dependency.get(NavigationBarOverlayController.class);
        this.mNavBarOverlayController = navigationBarOverlayController;
        if (navigationBarOverlayController.isNavigationBarOverlayEnabled()) {
            NavigationBarOverlayController navigationBarOverlayController2 = this.mNavBarOverlayController;
            EdgeBackGestureHandler edgeBackGestureHandler = this.mEdgeBackGestureHandler;
            Objects.requireNonNull(edgeBackGestureHandler);
            navigationBarOverlayController2.init(navigationBarView$$ExternalSyntheticLambda2, new ShellTaskOrganizer$$ExternalSyntheticLambda0(edgeBackGestureHandler, 2));
        }
    }

    public static void dumpButton(PrintWriter printWriter, String str, ButtonDispatcher buttonDispatcher) {
        printWriter.print("      " + str + ": ");
        if (buttonDispatcher == null) {
            printWriter.print("null");
        } else {
            printWriter.print(visibilityToString(buttonDispatcher.getVisibility()) + " alpha=" + buttonDispatcher.getAlpha());
        }
        printWriter.println();
    }

    public final ButtonDispatcher getBackButton() {
        return this.mButtonDispatchers.get(2131427535);
    }

    public final KeyButtonDrawable getDrawable(int i) {
        return KeyButtonDrawable.create(this.mLightContext, this.mLightIconColor, this.mDarkIconColor, i, true);
    }

    public final ButtonDispatcher getHomeButton() {
        return this.mButtonDispatchers.get(2131428094);
    }

    public final LightBarTransitionsController getLightTransitionsController() {
        NavigationBarTransitions navigationBarTransitions = this.mBarTransitions;
        Objects.requireNonNull(navigationBarTransitions);
        return navigationBarTransitions.mLightTransitionsController;
    }

    public final ButtonDispatcher getRecentsButton() {
        return this.mButtonDispatchers.get(2131428669);
    }

    @VisibleForTesting
    public boolean isRecentsButtonDisabled() {
        boolean z;
        if ((this.mDisabledFlags & 16777216) == 0) {
            z = true;
        } else {
            z = false;
        }
        if (!z || getContext().getDisplayId() != 0) {
            return true;
        }
        return false;
    }

    public final void notifyActiveTouchRegions() {
        OverviewProxyService overviewProxyService = this.mOverviewProxyService;
        Region buttonLocations = getButtonLocations(true, true, true);
        Objects.requireNonNull(overviewProxyService);
        overviewProxyService.mActiveNavBarRegion = buttonLocations;
        IOverviewProxy iOverviewProxy = overviewProxyService.mOverviewProxy;
        if (iOverviewProxy != null && buttonLocations != null) {
            try {
                iOverviewProxy.onActiveNavBarRegionChanges(buttonLocations);
            } catch (RemoteException e) {
                Log.e("OverviewProxyService", "Failed to call onActiveNavBarRegionChanges()", e);
            }
        }
    }

    public final void notifyVerticalChangedListener(final boolean z) {
        OnVerticalChangedListener onVerticalChangedListener = this.mOnVerticalChangedListener;
        if (onVerticalChangedListener != null) {
            NavigationBar$$ExternalSyntheticLambda11 navigationBar$$ExternalSyntheticLambda11 = (NavigationBar$$ExternalSyntheticLambda11) onVerticalChangedListener;
            Objects.requireNonNull(navigationBar$$ExternalSyntheticLambda11);
            NavigationBar navigationBar = navigationBar$$ExternalSyntheticLambda11.f$0;
            Objects.requireNonNull(navigationBar);
            navigationBar.mStatusBarOptionalLazy.get().ifPresent(new Consumer() { // from class: com.android.systemui.navigationbar.NavigationBar$$ExternalSyntheticLambda15
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    StatusBar statusBar = (StatusBar) obj;
                    boolean z2 = true;
                    boolean z3 = !z;
                    Objects.requireNonNull(statusBar);
                    NotificationPanelViewController notificationPanelViewController = statusBar.mNotificationPanelViewController;
                    Objects.requireNonNull(notificationPanelViewController);
                    if (notificationPanelViewController.mQsScrimEnabled == z3) {
                        z2 = false;
                    }
                    notificationPanelViewController.mQsScrimEnabled = z3;
                    if (z2) {
                        notificationPanelViewController.updateQsState();
                    }
                }
            });
        }
    }

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
        DeadZone deadZone = this.mDeadZone;
        Objects.requireNonNull(deadZone);
        if (deadZone.mShouldFlash && deadZone.mFlashFrac > 0.0f) {
            int size = (int) deadZone.getSize(SystemClock.uptimeMillis());
            if (!deadZone.mVertical) {
                canvas.clipRect(0, 0, canvas.getWidth(), size);
            } else if (deadZone.mDisplayRotation == 3) {
                canvas.clipRect(canvas.getWidth() - size, 0, canvas.getWidth(), canvas.getHeight());
            } else {
                canvas.clipRect(0, 0, size, canvas.getHeight());
            }
            canvas.drawARGB((int) (deadZone.mFlashFrac * 255.0f), 221, 238, 170);
        }
        super.onDraw(canvas);
    }

    @Override // android.view.ViewGroup
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (R$color.isGesturalMode(this.mNavBarMode) && this.mImeVisible && motionEvent.getAction() == 0) {
            SysUiStatsLog.write(304, (int) motionEvent.getX(), (int) motionEvent.getY());
        }
        if (shouldDeadZoneConsumeTouchEvents(motionEvent) || super.onInterceptTouchEvent(motionEvent)) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.navigationbar.NavigationModeController.ModeChangedListener
    public final void onNavigationModeChanged(int i) {
        this.mNavBarMode = i;
        NavigationModeController navigationModeController = (NavigationModeController) Dependency.get(NavigationModeController.class);
        Objects.requireNonNull(navigationModeController);
        this.mImeDrawsImeNavBar = navigationModeController.mCurrentUserContext.getResources().getBoolean(17891674);
        NavigationBarTransitions navigationBarTransitions = this.mBarTransitions;
        int i2 = this.mNavBarMode;
        Objects.requireNonNull(navigationBarTransitions);
        navigationBarTransitions.mNavBarMode = i2;
        EdgeBackGestureHandler edgeBackGestureHandler = this.mEdgeBackGestureHandler;
        int i3 = this.mNavBarMode;
        Objects.requireNonNull(edgeBackGestureHandler);
        edgeBackGestureHandler.mIsGesturalModeEnabled = R$color.isGesturalMode(i3);
        edgeBackGestureHandler.updateIsEnabled();
        edgeBackGestureHandler.updateCurrentUserResources();
        updateRotationButton();
        if (R$color.isGesturalMode(this.mNavBarMode)) {
            this.mRegionSamplingHelper.start(this.mSamplingBounds);
        } else {
            this.mRegionSamplingHelper.stop();
        }
    }

    public final void orientBackButton(KeyButtonDrawable keyButtonDrawable) {
        boolean z;
        boolean z2;
        float f;
        int i;
        if ((this.mNavigationIconHints & 1) != 0) {
            z = true;
        } else {
            z = false;
        }
        if (this.mConfiguration.getLayoutDirection() == 1) {
            z2 = true;
        } else {
            z2 = false;
        }
        float f2 = 0.0f;
        if (z) {
            if (z2) {
                i = 90;
            } else {
                i = -90;
            }
            f = i;
        } else {
            f = 0.0f;
        }
        Objects.requireNonNull(keyButtonDrawable);
        if (keyButtonDrawable.mState.mRotateDegrees != f) {
            if (R$color.isGesturalMode(this.mNavBarMode)) {
                keyButtonDrawable.setRotation(f);
                return;
            }
            if (!this.mOverviewProxyService.shouldShowSwipeUpUI() && !this.mIsVertical && z) {
                f2 = -getResources().getDimension(2131166590);
            }
            ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(keyButtonDrawable, PropertyValuesHolder.ofFloat(KeyButtonDrawable.KEY_DRAWABLE_ROTATE, f), PropertyValuesHolder.ofFloat(KeyButtonDrawable.KEY_DRAWABLE_TRANSLATE_Y, f2));
            ofPropertyValuesHolder.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
            ofPropertyValuesHolder.setDuration(200L);
            ofPropertyValuesHolder.start();
        }
    }

    public final void setDisabledFlags(int i) {
        boolean z;
        int i2 = this.mDisabledFlags;
        if (i2 != i) {
            boolean z2 = true;
            if ((i2 & 16777216) == 0) {
                z = true;
            } else {
                z = false;
            }
            this.mDisabledFlags = i;
            if (!z) {
                if ((i & 16777216) != 0) {
                    z2 = false;
                }
                if (z2) {
                    updateIcons(Configuration.EMPTY);
                }
            }
            updateNavButtonIcons();
            updateSlippery();
            updateDisabledSystemUiStateFlags();
        }
    }

    @Override // android.view.View
    public final void setLayoutDirection(int i) {
        updateIcons(Configuration.EMPTY);
        super.setLayoutDirection(i);
    }

    public final void setNavigationIconHints(int i) {
        boolean z;
        boolean z2;
        int i2 = this.mNavigationIconHints;
        if (i != i2) {
            if ((i & 1) != 0) {
                z = true;
            } else {
                z = false;
            }
            if ((i2 & 1) != 0) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (z != z2) {
                if (!z) {
                    NavTransitionListener navTransitionListener = this.mTransitionListener;
                    Objects.requireNonNull(navTransitionListener);
                    ButtonDispatcher backButton = NavigationBarView.this.getBackButton();
                    if (!navTransitionListener.mBackTransitioning && backButton.getVisibility() == 0 && navTransitionListener.mHomeAppearing && NavigationBarView.this.getHomeButton().getAlpha() == 0.0f) {
                        ButtonDispatcher backButton2 = NavigationBarView.this.getBackButton();
                        Objects.requireNonNull(backButton2);
                        backButton2.setAlpha(0.0f, false, true);
                        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(backButton, "alpha", 0.0f, 1.0f);
                        ofFloat.setStartDelay(navTransitionListener.mStartDelay);
                        ofFloat.setDuration(navTransitionListener.mDuration);
                        ofFloat.setInterpolator(navTransitionListener.mInterpolator);
                        ofFloat.start();
                    }
                }
                this.mImeVisible = z;
                RotationButtonController rotationButtonController = this.mRotationButtonController;
                Objects.requireNonNull(rotationButtonController);
                rotationButtonController.mRotationButton.setCanShowRotationButton(!this.mImeVisible);
                if (this.mNavBarOverlayController.isNavigationBarOverlayEnabled()) {
                    this.mNavBarOverlayController.setCanShow(!this.mImeVisible);
                }
            }
            this.mNavigationIconHints = i;
            updateNavButtonIcons();
        }
    }

    public final void updateCurrentView() {
        View view;
        this.mHorizontal.setVisibility(8);
        this.mVertical.setVisibility(8);
        if (this.mIsVertical) {
            view = this.mVertical;
        } else {
            view = this.mHorizontal;
        }
        this.mCurrentView = view;
        boolean z = false;
        view.setVisibility(0);
        NavigationBarInflaterView navigationBarInflaterView = this.mNavigationInflaterView;
        boolean z2 = this.mIsVertical;
        Objects.requireNonNull(navigationBarInflaterView);
        if (z2 != navigationBarInflaterView.mIsVertical) {
            navigationBarInflaterView.mIsVertical = z2;
        }
        int rotation = getContext().getDisplay().getRotation();
        this.mCurrentRotation = rotation;
        NavigationBarInflaterView navigationBarInflaterView2 = this.mNavigationInflaterView;
        if (rotation == 1) {
            z = true;
        }
        Objects.requireNonNull(navigationBarInflaterView2);
        if (z != navigationBarInflaterView2.mAlternativeOrder) {
            navigationBarInflaterView2.mAlternativeOrder = z;
            navigationBarInflaterView2.updateAlternativeOrder();
        }
        this.mNavigationInflaterView.updateButtonDispatchersCurrentView();
        updateLayoutTransitionsEnabled();
    }

    public final void updateDisabledSystemUiStateFlags() {
        boolean z;
        boolean z2;
        int displayId = ((FrameLayout) this).mContext.getDisplayId();
        SysUiState sysUiState = this.mSysUiFlagContainer;
        Objects.requireNonNull(ActivityManagerWrapper.sInstance);
        boolean z3 = true;
        sysUiState.setFlag(1, ActivityManagerWrapper.isScreenPinningActive());
        if ((this.mDisabledFlags & 16777216) != 0) {
            z = true;
        } else {
            z = false;
        }
        sysUiState.setFlag(128, z);
        if ((this.mDisabledFlags & 2097152) != 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        sysUiState.setFlag(256, z2);
        if ((this.mDisabledFlags & 33554432) == 0) {
            z3 = false;
        }
        sysUiState.setFlag(1024, z3);
        sysUiState.commitUpdate(displayId);
    }

    public final void updateIcons(Configuration configuration) {
        boolean z;
        boolean z2;
        int i;
        KeyButtonDrawable keyButtonDrawable;
        float f;
        int i2 = configuration.orientation;
        Configuration configuration2 = this.mConfiguration;
        boolean z3 = true;
        if (i2 != configuration2.orientation) {
            z = true;
        } else {
            z = false;
        }
        if (configuration.densityDpi != configuration2.densityDpi) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (configuration.getLayoutDirection() == this.mConfiguration.getLayoutDirection()) {
            z3 = false;
        }
        if (z || z2) {
            this.mDockedIcon = getDrawable(2131232281);
            if (this.mOverviewProxyService.shouldShowSwipeUpUI()) {
                keyButtonDrawable = getDrawable(2131232283);
            } else {
                keyButtonDrawable = getDrawable(2131232282);
            }
            if (this.mIsVertical) {
                f = 90.0f;
            } else {
                f = 0.0f;
            }
            keyButtonDrawable.setRotation(f);
            this.mHomeDefaultIcon = keyButtonDrawable;
        }
        if (z2 || z3) {
            this.mRecentIcon = getDrawable(2131232289);
            ContextualButtonGroup contextualButtonGroup = this.mContextualButtonGroup;
            int i3 = this.mLightIconColor;
            int i4 = this.mDarkIconColor;
            Objects.requireNonNull(contextualButtonGroup);
            Iterator it = contextualButtonGroup.mButtonData.iterator();
            while (it.hasNext()) {
                ((ContextualButtonGroup.ButtonData) it.next()).button.updateIcon(i3, i4);
            }
        }
        if (z || z2 || z3) {
            if (this.mOverviewProxyService.shouldShowSwipeUpUI()) {
                i = 2131232280;
            } else {
                i = 2131232279;
            }
            KeyButtonDrawable drawable = getDrawable(i);
            orientBackButton(drawable);
            this.mBackIcon = drawable;
        }
    }

    public final void updateLayoutTransitionsEnabled() {
        boolean z;
        if (this.mWakeAndUnlocking || !this.mLayoutTransitionsEnabled) {
            z = false;
        } else {
            z = true;
        }
        LayoutTransition layoutTransition = ((ViewGroup) this.mCurrentView.findViewById(2131428478)).getLayoutTransition();
        if (layoutTransition == null) {
            return;
        }
        if (z) {
            layoutTransition.enableTransitionType(2);
            layoutTransition.enableTransitionType(3);
            layoutTransition.enableTransitionType(0);
            layoutTransition.enableTransitionType(1);
            return;
        }
        layoutTransition.disableTransitionType(2);
        layoutTransition.disableTransitionType(3);
        layoutTransition.disableTransitionType(0);
        layoutTransition.disableTransitionType(1);
    }

    /* JADX WARN: Code restructure failed: missing block: B:44:0x009c, code lost:
        if ((r10.mDisabledFlags & 4194304) == 0) goto L_0x009e;
     */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00b5  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x00c9  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x00da  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x00f7  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x00f9  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x0103  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0105  */
    /* JADX WARN: Removed duplicated region for block: B:78:0x010f  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0111  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0122  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateNavButtonIcons() {
        /*
            Method dump skipped, instructions count: 298
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.navigationbar.NavigationBarView.updateNavButtonIcons():void");
    }

    public final void updateRecentsIcon() {
        float f;
        KeyButtonDrawable keyButtonDrawable;
        KeyButtonDrawable keyButtonDrawable2 = this.mDockedIcon;
        if (!this.mDockedStackExists || !this.mIsVertical) {
            f = 0.0f;
        } else {
            f = 90.0f;
        }
        keyButtonDrawable2.setRotation(f);
        ButtonDispatcher recentsButton = getRecentsButton();
        if (this.mDockedStackExists) {
            keyButtonDrawable = this.mDockedIcon;
        } else {
            keyButtonDrawable = this.mRecentIcon;
        }
        recentsButton.setImageDrawable(keyButtonDrawable);
        NavigationBarTransitions navigationBarTransitions = this.mBarTransitions;
        Objects.requireNonNull(navigationBarTransitions);
        LightBarTransitionsController lightBarTransitionsController = navigationBarTransitions.mLightTransitionsController;
        Objects.requireNonNull(lightBarTransitionsController);
        navigationBarTransitions.applyDarkIntensity(lightBarTransitionsController.mDarkIntensity);
    }

    public final void updateRotationButton() {
        ContextualButton contextualButton;
        if (R$color.isGesturalMode(this.mNavBarMode)) {
            ContextualButtonGroup contextualButtonGroup = this.mContextualButtonGroup;
            Objects.requireNonNull(contextualButtonGroup);
            int contextButtonIndex = contextualButtonGroup.getContextButtonIndex(2131428715);
            if (contextButtonIndex != -1) {
                contextualButtonGroup.mButtonData.remove(contextButtonIndex);
            }
            this.mButtonDispatchers.remove(2131428715);
            this.mRotationButtonController.setRotationButton(this.mFloatingRotationButton, this.mRotationButtonListener);
            return;
        }
        ContextualButtonGroup contextualButtonGroup2 = this.mContextualButtonGroup;
        Objects.requireNonNull(contextualButtonGroup2);
        int contextButtonIndex2 = contextualButtonGroup2.getContextButtonIndex(2131428715);
        if (contextButtonIndex2 != -1) {
            contextualButton = ((ContextualButtonGroup.ButtonData) contextualButtonGroup2.mButtonData.get(contextButtonIndex2)).button;
        } else {
            contextualButton = null;
        }
        if (contextualButton == null) {
            this.mContextualButtonGroup.addButton(this.mRotationContextButton);
            this.mButtonDispatchers.put(2131428715, this.mRotationContextButton);
            this.mRotationButtonController.setRotationButton(this.mRotationContextButton, this.mRotationButtonListener);
        }
    }

    public final void updateSlippery() {
        boolean z;
        NotificationPanelViewController notificationPanelViewController;
        boolean z2;
        boolean z3 = false;
        if (this.mOverviewProxyService.shouldShowSwipeUpUI()) {
            if ((this.mDisabledFlags & 16777216) == 0) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (z2) {
                z = true;
                if (z || ((notificationPanelViewController = this.mPanelView) != null && notificationPanelViewController.isFullyExpanded() && !this.mPanelView.isCollapsing())) {
                    z3 = true;
                }
                setSlippery(z3);
            }
        }
        z = false;
        if (z) {
        }
        z3 = true;
        setSlippery(z3);
    }

    public final void updateStates() {
        AnonymousClass1 r4;
        boolean shouldShowSwipeUpUI = this.mOverviewProxyService.shouldShowSwipeUpUI();
        NavigationBarInflaterView navigationBarInflaterView = this.mNavigationInflaterView;
        if (navigationBarInflaterView != null) {
            String defaultLayout = navigationBarInflaterView.getDefaultLayout();
            if (!Objects.equals(navigationBarInflaterView.mCurrentLayout, defaultLayout)) {
                navigationBarInflaterView.clearViews();
                navigationBarInflaterView.inflateLayout(defaultLayout);
            }
        }
        updateSlippery();
        updateIcons(Configuration.EMPTY);
        updateNavButtonIcons();
        boolean z = !shouldShowSwipeUpUI;
        Objects.requireNonNull(WindowManagerWrapper.sInstance);
        try {
            WindowManagerGlobal.getWindowManagerService().setNavBarVirtualKeyHapticFeedbackEnabled(z);
        } catch (RemoteException e) {
            Log.w("WindowManagerWrapper", "Failed to enable or disable navigation bar button haptics: ", e);
        }
        ButtonDispatcher homeButton = getHomeButton();
        if (shouldShowSwipeUpUI) {
            r4 = this.mQuickStepAccessibilityDelegate;
        } else {
            r4 = null;
        }
        Objects.requireNonNull(homeButton);
        homeButton.mAccessibilityDelegate = r4;
        int size = homeButton.mViews.size();
        for (int i = 0; i < size; i++) {
            homeButton.mViews.get(i).setAccessibilityDelegate(r4);
        }
    }

    public final boolean isRecentsButtonVisible() {
        if (getRecentsButton().getVisibility() == 0) {
            return true;
        }
        return false;
    }

    @Override // android.view.View
    public final WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        boolean z;
        int systemWindowInsetLeft = windowInsets.getSystemWindowInsetLeft();
        int systemWindowInsetRight = windowInsets.getSystemWindowInsetRight();
        setPadding(systemWindowInsetLeft, windowInsets.getSystemWindowInsetTop(), systemWindowInsetRight, windowInsets.getSystemWindowInsetBottom());
        EdgeBackGestureHandler edgeBackGestureHandler = this.mEdgeBackGestureHandler;
        Objects.requireNonNull(edgeBackGestureHandler);
        edgeBackGestureHandler.mLeftInset = systemWindowInsetLeft;
        edgeBackGestureHandler.mRightInset = systemWindowInsetRight;
        NavigationEdgeBackPlugin navigationEdgeBackPlugin = edgeBackGestureHandler.mEdgeBackPlugin;
        if (navigationEdgeBackPlugin != null) {
            navigationEdgeBackPlugin.setInsets(systemWindowInsetLeft, systemWindowInsetRight);
        }
        if (!R$color.isGesturalMode(this.mNavBarMode) || windowInsets.getSystemWindowInsetBottom() == 0) {
            z = true;
        } else {
            z = false;
        }
        setClipChildren(z);
        setClipToPadding(z);
        return super.onApplyWindowInsets(windowInsets);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mEdgeBackGestureHandler.onNavBarAttached();
        requestApplyInsets();
        reorient();
        onNavigationModeChanged(this.mNavBarMode);
        RotationButtonController rotationButtonController = this.mRotationButtonController;
        if (rotationButtonController != null && !rotationButtonController.mListenersRegistered && !rotationButtonController.mContext.getPackageManager().hasSystemFeature("android.hardware.type.pc")) {
            rotationButtonController.mListenersRegistered = true;
            try {
                WindowManagerGlobal.getWindowManagerService().watchRotation(rotationButtonController.mRotationWatcher, 0);
            } catch (RemoteException e) {
                Log.e("StatusBar/RotationButtonController", "RegisterListeners caught a RemoteException", e);
            } catch (IllegalArgumentException unused) {
                rotationButtonController.mListenersRegistered = false;
                Log.w("StatusBar/RotationButtonController", "RegisterListeners for the display failed");
            }
            TaskStackChangeListeners.INSTANCE.registerTaskStackListener(rotationButtonController.mTaskStackListener);
        }
        if (this.mNavBarOverlayController.isNavigationBarOverlayEnabled()) {
            this.mNavBarOverlayController.registerListeners();
        }
        getViewTreeObserver().addOnComputeInternalInsetsListener(this.mOnComputeInternalInsetsListener);
        updateNavButtonIcons();
    }

    @Override // android.view.View
    public final void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mTmpLastConfiguration.updateFrom(this.mConfiguration);
        int updateFrom = this.mConfiguration.updateFrom(configuration);
        FloatingRotationButton floatingRotationButton = this.mFloatingRotationButton;
        if (!((updateFrom & 4096) == 0 && (updateFrom & 1024) == 0)) {
            floatingRotationButton.updateDimensionResources();
            if (floatingRotationButton.mIsShowing) {
                floatingRotationButton.mWindowManager.updateViewLayout(floatingRotationButton.mKeyButtonContainer, floatingRotationButton.adjustViewPositionAndCreateLayoutParams());
            }
        }
        if ((updateFrom & 4) != 0) {
            floatingRotationButton.mKeyButtonView.setContentDescription(floatingRotationButton.mContext.getString(floatingRotationButton.mContentDescriptionResource));
        } else {
            Objects.requireNonNull(floatingRotationButton);
        }
        Configuration configuration2 = this.mConfiguration;
        boolean z = false;
        if (configuration2 != null) {
            if ((configuration2.uiMode & 15) == 3) {
                z = true;
            }
            if (z != this.mInCarMode) {
                this.mInCarMode = z;
            }
        }
        updateIcons(this.mTmpLastConfiguration);
        updateRecentsIcon();
        this.mEdgeBackGestureHandler.onConfigurationChanged(this.mConfiguration);
        Configuration configuration3 = this.mTmpLastConfiguration;
        if (configuration3.densityDpi != this.mConfiguration.densityDpi || configuration3.getLayoutDirection() != this.mConfiguration.getLayoutDirection()) {
            updateNavButtonIcons();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        NavigationModeController navigationModeController = (NavigationModeController) Dependency.get(NavigationModeController.class);
        Objects.requireNonNull(navigationModeController);
        navigationModeController.mListeners.remove(this);
        for (int i = 0; i < this.mButtonDispatchers.size(); i++) {
            Objects.requireNonNull(this.mButtonDispatchers.valueAt(i));
        }
        if (this.mRotationButtonController != null) {
            this.mFloatingRotationButton.hide();
            RotationButtonController rotationButtonController = this.mRotationButtonController;
            Objects.requireNonNull(rotationButtonController);
            if (rotationButtonController.mListenersRegistered) {
                rotationButtonController.mListenersRegistered = false;
                try {
                    WindowManagerGlobal.getWindowManagerService().removeRotationWatcher(rotationButtonController.mRotationWatcher);
                    TaskStackChangeListeners.INSTANCE.unregisterTaskStackListener(rotationButtonController.mTaskStackListener);
                } catch (RemoteException e) {
                    Log.e("StatusBar/RotationButtonController", "UnregisterListeners caught a RemoteException", e);
                }
            }
        }
        if (this.mNavBarOverlayController.isNavigationBarOverlayEnabled()) {
            this.mNavBarOverlayController.unregisterListeners();
        }
        this.mEdgeBackGestureHandler.onNavBarDetached();
        getViewTreeObserver().removeOnComputeInternalInsetsListener(this.mOnComputeInternalInsetsListener);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        NavigationBarInflaterView navigationBarInflaterView = (NavigationBarInflaterView) findViewById(2131428488);
        this.mNavigationInflaterView = navigationBarInflaterView;
        SparseArray<ButtonDispatcher> sparseArray = this.mButtonDispatchers;
        Objects.requireNonNull(navigationBarInflaterView);
        navigationBarInflaterView.mButtonDispatchers = sparseArray;
        for (int i = 0; i < sparseArray.size(); i++) {
            ButtonDispatcher valueAt = sparseArray.valueAt(i);
            NavigationBarInflaterView.addAll(valueAt, (ViewGroup) navigationBarInflaterView.mHorizontal.findViewById(2131427921));
            NavigationBarInflaterView.addAll(valueAt, (ViewGroup) navigationBarInflaterView.mHorizontal.findViewById(2131427677));
            NavigationBarInflaterView.addAll(valueAt, (ViewGroup) navigationBarInflaterView.mVertical.findViewById(2131427921));
            NavigationBarInflaterView.addAll(valueAt, (ViewGroup) navigationBarInflaterView.mVertical.findViewById(2131427677));
        }
        this.mHorizontal = findViewById(2131428099);
        this.mVertical = findViewById(2131429179);
        updateCurrentView();
        updateIcons(Configuration.EMPTY);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        notifyActiveTouchRegions();
    }

    @Override // android.widget.FrameLayout, android.view.View
    public final void onMeasure(int i, int i2) {
        boolean z;
        int i3;
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        if (size <= 0 || size2 <= size || R$color.isGesturalMode(this.mNavBarMode)) {
            z = false;
        } else {
            z = true;
        }
        if (z != this.mIsVertical) {
            this.mIsVertical = z;
            reorient();
            notifyVerticalChangedListener(z);
        }
        if (R$color.isGesturalMode(this.mNavBarMode)) {
            if (this.mIsVertical) {
                i3 = getResources().getDimensionPixelSize(17105366);
            } else {
                i3 = getResources().getDimensionPixelSize(17105364);
            }
            int dimensionPixelSize = getResources().getDimensionPixelSize(17105360);
            NavigationBarTransitions navigationBarTransitions = this.mBarTransitions;
            Rect rect = new Rect(0, dimensionPixelSize - i3, size, size2);
            Objects.requireNonNull(navigationBarTransitions);
            BarTransitions.BarBackgroundDrawable barBackgroundDrawable = navigationBarTransitions.mBarBackground;
            Objects.requireNonNull(barBackgroundDrawable);
            barBackgroundDrawable.mFrame = rect;
        } else {
            NavigationBarTransitions navigationBarTransitions2 = this.mBarTransitions;
            Objects.requireNonNull(navigationBarTransitions2);
            BarTransitions.BarBackgroundDrawable barBackgroundDrawable2 = navigationBarTransitions2.mBarBackground;
            Objects.requireNonNull(barBackgroundDrawable2);
            barBackgroundDrawable2.mFrame = null;
        }
        super.onMeasure(i, i2);
    }

    @Override // android.view.View
    public final boolean onTouchEvent(MotionEvent motionEvent) {
        shouldDeadZoneConsumeTouchEvents(motionEvent);
        return super.onTouchEvent(motionEvent);
    }

    public final void reorient() {
        updateCurrentView();
        NavigationBarFrame navigationBarFrame = (NavigationBarFrame) getRootView();
        DeadZone deadZone = this.mDeadZone;
        Objects.requireNonNull(navigationBarFrame);
        navigationBarFrame.mDeadZone = deadZone;
        this.mDeadZone.onConfigurationChanged(this.mCurrentRotation);
        NavigationBarTransitions navigationBarTransitions = this.mBarTransitions;
        Objects.requireNonNull(navigationBarTransitions);
        navigationBarTransitions.applyModeBackground(navigationBarTransitions.mMode, false);
        navigationBarTransitions.applyLightsOut(false, true);
        if (!isLayoutDirectionResolved()) {
            resolveLayoutDirection();
        }
        updateNavButtonIcons();
        ButtonDispatcher homeButton = getHomeButton();
        boolean z = this.mIsVertical;
        Objects.requireNonNull(homeButton);
        homeButton.mVertical = z;
        int size = homeButton.mViews.size();
        for (int i = 0; i < size; i++) {
            View view = homeButton.mViews.get(i);
            if (view instanceof ButtonInterface) {
                ((ButtonInterface) view).setVertical(z);
            }
        }
    }

    public final void setSlippery(boolean z) {
        WindowManager.LayoutParams layoutParams;
        boolean z2;
        ViewGroup viewGroup = (ViewGroup) getParent();
        if (viewGroup != null && (layoutParams = (WindowManager.LayoutParams) viewGroup.getLayoutParams()) != null) {
            int i = layoutParams.flags;
            if ((i & 536870912) != 0) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (z != z2) {
                if (z) {
                    layoutParams.flags = 536870912 | i;
                } else {
                    layoutParams.flags = (-536870913) & i;
                }
                ((WindowManager) getContext().getSystemService(WindowManager.class)).updateViewLayout(viewGroup, layoutParams);
            }
        }
    }

    public final boolean shouldDeadZoneConsumeTouchEvents(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.mDeadZoneConsuming = false;
        }
        if (!this.mDeadZone.onTouchEvent(motionEvent) && !this.mDeadZoneConsuming) {
            return false;
        }
        if (actionMasked == 0) {
            setSlippery(true);
            this.mDeadZoneConsuming = true;
        } else if (actionMasked == 1 || actionMasked == 3) {
            updateSlippery();
            this.mDeadZoneConsuming = false;
        }
        return true;
    }

    public final void updateButtonLocation(View view, boolean z) {
        if (z) {
            view.getBoundsOnScreen(this.mTmpBounds);
        } else {
            view.getLocationInWindow(this.mTmpPosition);
            Rect rect = this.mTmpBounds;
            int[] iArr = this.mTmpPosition;
            rect.set(iArr[0], iArr[1], view.getWidth() + iArr[0], view.getHeight() + this.mTmpPosition[1]);
        }
        this.mTmpRegion.op(this.mTmpBounds, Region.Op.UNION);
    }
}

package com.android.systemui.statusbar.phone;

import android.app.IActivityManager;
import android.content.Context;
import android.hidl.base.V1_0.DebugInfo$$ExternalSyntheticOutline0;
import android.os.Binder;
import android.os.Trace;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.android.internal.colorextraction.ColorExtractor;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.LockIconView$$ExternalSyntheticOutline0;
import com.android.systemui.DejankUtils;
import com.android.systemui.Dumpable;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.colorextraction.SysuiColorExtractor;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.plugins.OverlayPlugin;
import com.android.systemui.qs.QSAnimator$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.wifitrackerlib.WifiPickerTracker$$ExternalSyntheticLambda27;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
/* loaded from: classes.dex */
public final class NotificationShadeWindowControllerImpl implements NotificationShadeWindowController, Dumpable, ConfigurationController.ConfigurationListener {
    public final IActivityManager mActivityManager;
    public final AuthController mAuthController;
    public final SysuiColorExtractor mColorExtractor;
    public final Context mContext;
    public int mDeferWindowLayoutParams;
    public final DozeParameters mDozeParameters;
    public NotificationShadeWindowController.ForcePluginOpenListener mForcePluginOpenListener;
    public boolean mHasTopUi;
    public boolean mHasTopUiChanged;
    public final KeyguardBypassController mKeyguardBypassController;
    public final float mKeyguardMaxRefreshRate;
    public final float mKeyguardPreferredRefreshRate;
    public final KeyguardStateController mKeyguardStateController;
    public final KeyguardViewMediator mKeyguardViewMediator;
    public NotificationShadeWindowController.OtherwisedCollapsedListener mListener;
    public final long mLockScreenDisplayTimeout;
    public WindowManager.LayoutParams mLp;
    public ViewGroup mNotificationShadeView;
    public float mScreenBrightnessDoze;
    public final ScreenOffAnimationController mScreenOffAnimationController;
    public Consumer<Integer> mScrimsVisibilityListener;
    public final AnonymousClass1 mStateListener;
    public final WindowManager mWindowManager;
    public final State mCurrentState = new State();
    public final ArrayList<WeakReference<StatusBarWindowCallback>> mCallbacks = new ArrayList<>();
    public float mFaceAuthDisplayBrightness = -1.0f;
    public final HashSet mForceOpenTokens = new HashSet();
    public final WindowManager.LayoutParams mLpChanged = new WindowManager.LayoutParams();

    /* loaded from: classes.dex */
    public static class State {
        public boolean mBackdropShowing;
        public int mBackgroundBlurRadius;
        public boolean mBouncerShowing;
        public HashSet mComponentsForcingTopUi = new HashSet();
        public boolean mDozing;
        public boolean mForceCollapsed;
        public boolean mForceDozeBrightness;
        public boolean mForcePluginOpen;
        public boolean mHeadsUpShowing;
        public boolean mKeyguardFadingAway;
        public boolean mKeyguardGoingAway;
        public boolean mKeyguardNeedsInput;
        public boolean mKeyguardOccluded;
        public boolean mKeyguardShowing;
        public boolean mLaunchingActivity;
        public boolean mLightRevealScrimOpaque;
        public boolean mNotTouchable;
        public boolean mNotificationShadeFocusable;
        public boolean mPanelExpanded;
        public boolean mPanelVisible;
        public boolean mQsExpanded;
        public boolean mRemoteInputActive;
        public int mScrimsVisibility;
        public int mStatusBarState;

        public final String toString() {
            Field[] declaredFields;
            StringBuilder m = DebugInfo$$ExternalSyntheticOutline0.m("Window State {", "\n");
            for (Field field : State.class.getDeclaredFields()) {
                m.append("  ");
                try {
                    m.append(field.getName());
                    m.append(": ");
                    m.append(field.get(this));
                } catch (IllegalAccessException unused) {
                }
                m.append("\n");
            }
            m.append("}");
            return m.toString();
        }

        /* renamed from: -$$Nest$misKeyguardShowingAndNotOccluded  reason: not valid java name */
        public static boolean m104$$Nest$misKeyguardShowingAndNotOccluded(State state) {
            Objects.requireNonNull(state);
            if (!state.mKeyguardShowing || state.mKeyguardOccluded) {
                return false;
            }
            return true;
        }
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final void registerCallback(StatusBarWindowCallback statusBarWindowCallback) {
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            if (this.mCallbacks.get(i).get() == statusBarWindowCallback) {
                return;
            }
        }
        this.mCallbacks.add(new WeakReference<>(statusBarWindowCallback));
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final void setDozeScreenBrightness(int i) {
        this.mScreenBrightnessDoze = i / 255.0f;
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final void unregisterCallback(StatusBarWindowCallback statusBarWindowCallback) {
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            if (this.mCallbacks.get(i).get() == statusBarWindowCallback) {
                this.mCallbacks.remove(i);
                return;
            }
        }
    }

    public static boolean isExpanded(State state) {
        if ((state.mForceCollapsed || (!State.m104$$Nest$misKeyguardShowingAndNotOccluded(state) && !state.mPanelVisible && !state.mKeyguardFadingAway && !state.mBouncerShowing && !state.mHeadsUpShowing && state.mScrimsVisibility == 0)) && state.mBackgroundBlurRadius <= 0 && !state.mLaunchingActivity) {
            return false;
        }
        return true;
    }

    public final void apply(State state) {
        boolean z;
        boolean z2;
        boolean z3;
        WindowManager.LayoutParams layoutParams;
        long j;
        boolean z4;
        boolean z5;
        boolean z6 = false;
        if (state.mScrimsVisibility == 2 || state.mLightRevealScrimOpaque) {
            z = true;
        } else {
            z = false;
        }
        if (state.mKeyguardShowing || (state.mDozing && this.mDozeParameters.getAlwaysOn())) {
            z2 = true;
        } else {
            z2 = false;
        }
        if ((!z2 || state.mBackdropShowing || z) && !this.mKeyguardViewMediator.isAnimatingBetweenKeyguardAndSurfaceBehindOrWillBe()) {
            this.mLpChanged.flags &= -1048577;
        } else {
            this.mLpChanged.flags |= 1048576;
        }
        if (state.mDozing) {
            this.mLpChanged.privateFlags |= 524288;
        } else {
            this.mLpChanged.privateFlags &= -524289;
        }
        if (this.mKeyguardPreferredRefreshRate > 0.0f) {
            if (state.mStatusBarState != 1 || state.mKeyguardFadingAway || state.mKeyguardGoingAway) {
                z5 = false;
            } else {
                z5 = true;
            }
            if (!z5 || !this.mAuthController.isUdfpsEnrolled(KeyguardUpdateMonitor.getCurrentUser())) {
                this.mLpChanged.preferredMaxDisplayRefreshRate = 0.0f;
            } else {
                this.mLpChanged.preferredMaxDisplayRefreshRate = this.mKeyguardPreferredRefreshRate;
            }
            Trace.setCounter("display_set_preferred_refresh_rate", this.mKeyguardPreferredRefreshRate);
        } else if (this.mKeyguardMaxRefreshRate > 0.0f) {
            if (!this.mKeyguardBypassController.getBypassEnabled() || state.mStatusBarState != 1 || state.mKeyguardFadingAway || state.mKeyguardGoingAway) {
                z4 = false;
            } else {
                z4 = true;
            }
            if (state.mDozing || z4) {
                this.mLpChanged.preferredMaxDisplayRefreshRate = this.mKeyguardMaxRefreshRate;
            } else {
                this.mLpChanged.preferredMaxDisplayRefreshRate = 0.0f;
            }
            Trace.setCounter("display_max_refresh_rate", this.mLpChanged.preferredMaxDisplayRefreshRate);
        }
        if (!state.mNotificationShadeFocusable || !state.mPanelExpanded) {
            z3 = false;
        } else {
            z3 = true;
        }
        if ((state.mBouncerShowing && (state.mKeyguardOccluded || state.mKeyguardNeedsInput)) || ((NotificationRemoteInputManager.ENABLE_REMOTE_INPUT && state.mRemoteInputActive) || this.mScreenOffAnimationController.shouldIgnoreKeyguardTouches())) {
            WindowManager.LayoutParams layoutParams2 = this.mLpChanged;
            layoutParams2.flags = layoutParams2.flags & (-9) & (-131073);
        } else if (State.m104$$Nest$misKeyguardShowingAndNotOccluded(state) || z3) {
            this.mLpChanged.flags &= -9;
            if (!state.mKeyguardNeedsInput || !State.m104$$Nest$misKeyguardShowingAndNotOccluded(state)) {
                this.mLpChanged.flags |= 131072;
            } else {
                this.mLpChanged.flags &= -131073;
            }
        } else {
            WindowManager.LayoutParams layoutParams3 = this.mLpChanged;
            layoutParams3.flags = (layoutParams3.flags | 8) & (-131073);
        }
        WindowManager.LayoutParams layoutParams4 = this.mLpChanged;
        layoutParams4.softInputMode = 16;
        if (state.mPanelExpanded || state.mBouncerShowing || (NotificationRemoteInputManager.ENABLE_REMOTE_INPUT && state.mRemoteInputActive)) {
            layoutParams4.privateFlags |= 8388608;
        } else {
            layoutParams4.privateFlags &= -8388609;
        }
        if (!State.m104$$Nest$misKeyguardShowingAndNotOccluded(state) && !state.mDozing) {
            this.mLpChanged.screenOrientation = -1;
        } else if (this.mKeyguardStateController.isKeyguardScreenRotationAllowed()) {
            this.mLpChanged.screenOrientation = 2;
        } else {
            this.mLpChanged.screenOrientation = 5;
        }
        final boolean isExpanded = isExpanded(state);
        if (state.mForcePluginOpen) {
            NotificationShadeWindowController.OtherwisedCollapsedListener otherwisedCollapsedListener = this.mListener;
            if (otherwisedCollapsedListener != null) {
                StatusBar.AnonymousClass2.Callback callback = (StatusBar.AnonymousClass2.Callback) ((StatusBar$2$Callback$$ExternalSyntheticLambda0) otherwisedCollapsedListener).f$0;
                Objects.requireNonNull(callback);
                StatusBar.AnonymousClass2.this.mOverlays.forEach(new Consumer() { // from class: com.android.systemui.statusbar.phone.StatusBar$2$Callback$$ExternalSyntheticLambda1
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        ((OverlayPlugin) obj).setCollapseDesired(isExpanded);
                    }
                });
            }
            isExpanded = true;
        }
        if (isExpanded) {
            this.mNotificationShadeView.setVisibility(0);
        } else {
            this.mNotificationShadeView.setVisibility(4);
        }
        if (!State.m104$$Nest$misKeyguardShowingAndNotOccluded(state) || state.mStatusBarState != 1 || state.mQsExpanded) {
            this.mLpChanged.userActivityTimeout = -1L;
        } else {
            WindowManager.LayoutParams layoutParams5 = this.mLpChanged;
            if (state.mBouncerShowing) {
                j = 10000;
            } else {
                j = this.mLockScreenDisplayTimeout;
            }
            layoutParams5.userActivityTimeout = j;
        }
        if (!State.m104$$Nest$misKeyguardShowingAndNotOccluded(state) || state.mStatusBarState != 1 || state.mQsExpanded) {
            this.mLpChanged.inputFeatures &= -5;
        } else {
            WindowManager.LayoutParams layoutParams6 = this.mLpChanged;
            layoutParams6.inputFeatures = 4 | layoutParams6.inputFeatures;
        }
        boolean z7 = !State.m104$$Nest$misKeyguardShowingAndNotOccluded(state);
        ViewGroup viewGroup = this.mNotificationShadeView;
        if (!(viewGroup == null || viewGroup.getFitsSystemWindows() == z7)) {
            this.mNotificationShadeView.setFitsSystemWindows(z7);
            this.mNotificationShadeView.requestApplyInsets();
        }
        if (state.mHeadsUpShowing) {
            this.mLpChanged.flags |= 32;
        } else {
            this.mLpChanged.flags &= -33;
        }
        if (state.mForceDozeBrightness) {
            this.mLpChanged.screenBrightness = this.mScreenBrightnessDoze;
        } else {
            this.mLpChanged.screenBrightness = this.mFaceAuthDisplayBrightness;
        }
        if (!state.mComponentsForcingTopUi.isEmpty() || isExpanded(state)) {
            z6 = true;
        }
        this.mHasTopUiChanged = z6;
        if (state.mNotTouchable) {
            this.mLpChanged.flags |= 16;
        } else {
            this.mLpChanged.flags &= -17;
        }
        if (!isExpanded(state)) {
            this.mLpChanged.privateFlags |= 16777216;
        } else {
            this.mLpChanged.privateFlags &= -16777217;
        }
        if (!(this.mDeferWindowLayoutParams != 0 || (layoutParams = this.mLp) == null || layoutParams.copyFrom(this.mLpChanged) == 0)) {
            this.mWindowManager.updateViewLayout(this.mNotificationShadeView, this.mLp);
        }
        if (this.mHasTopUi != this.mHasTopUiChanged) {
            DejankUtils.whitelistIpcs(new QSAnimator$$ExternalSyntheticLambda0(this, 5));
        }
        notifyStateChangedCallbacks();
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final void attach$1() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, -1, 2040, -2138832824, -3);
        this.mLp = layoutParams;
        layoutParams.token = new Binder();
        WindowManager.LayoutParams layoutParams2 = this.mLp;
        layoutParams2.gravity = 48;
        boolean z = false;
        layoutParams2.setFitInsetsTypes(0);
        WindowManager.LayoutParams layoutParams3 = this.mLp;
        layoutParams3.softInputMode = 16;
        layoutParams3.setTitle("NotificationShade");
        this.mLp.packageName = this.mContext.getPackageName();
        WindowManager.LayoutParams layoutParams4 = this.mLp;
        layoutParams4.layoutInDisplayCutoutMode = 3;
        layoutParams4.privateFlags |= 134217728;
        layoutParams4.insetsFlags.behavior = 2;
        this.mWindowManager.addView(this.mNotificationShadeView, layoutParams4);
        this.mLpChanged.copyFrom(this.mLp);
        onThemeChanged();
        KeyguardViewMediator keyguardViewMediator = this.mKeyguardViewMediator;
        Objects.requireNonNull(keyguardViewMediator);
        if (keyguardViewMediator.mShowing && !keyguardViewMediator.mOccluded) {
            z = true;
        }
        if (z) {
            setKeyguardShowing(true);
        }
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final void batchApplyWindowLayoutParams(Runnable runnable) {
        WindowManager.LayoutParams layoutParams;
        this.mDeferWindowLayoutParams++;
        runnable.run();
        int i = this.mDeferWindowLayoutParams - 1;
        this.mDeferWindowLayoutParams = i;
        if (i == 0 && (layoutParams = this.mLp) != null && layoutParams.copyFrom(this.mLpChanged) != 0) {
            this.mWindowManager.updateViewLayout(this.mNotificationShadeView, this.mLp);
        }
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        StringBuilder m = LockIconView$$ExternalSyntheticOutline0.m(printWriter, "NotificationShadeWindowController:", "  mKeyguardMaxRefreshRate=");
        m.append(this.mKeyguardMaxRefreshRate);
        printWriter.println(m.toString());
        printWriter.println("  mKeyguardPreferredRefreshRate=" + this.mKeyguardPreferredRefreshRate);
        printWriter.println("  mDeferWindowLayoutParams=" + this.mDeferWindowLayoutParams);
        printWriter.println(this.mCurrentState);
        ViewGroup viewGroup = this.mNotificationShadeView;
        if (viewGroup != null && viewGroup.getViewRootImpl() != null) {
            this.mNotificationShadeView.getViewRootImpl().dump("  ", printWriter);
        }
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final boolean getForcePluginOpen() {
        return this.mCurrentState.mForcePluginOpen;
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final boolean getPanelExpanded() {
        return this.mCurrentState.mPanelExpanded;
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final boolean isLaunchingActivity() {
        return this.mCurrentState.mLaunchingActivity;
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final boolean isShowingWallpaper() {
        return !this.mCurrentState.mBackdropShowing;
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final void notifyStateChangedCallbacks() {
        for (StatusBarWindowCallback statusBarWindowCallback : (List) this.mCallbacks.stream().map(NotificationShadeWindowControllerImpl$$ExternalSyntheticLambda0.INSTANCE).filter(WifiPickerTracker$$ExternalSyntheticLambda27.INSTANCE$2).collect(Collectors.toList())) {
            State state = this.mCurrentState;
            statusBarWindowCallback.onStateChanged(state.mKeyguardShowing, state.mKeyguardOccluded, state.mBouncerShowing, state.mDozing);
        }
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController, com.android.systemui.statusbar.RemoteInputController.Callback
    public final void onRemoteInputActive(boolean z) {
        State state = this.mCurrentState;
        state.mRemoteInputActive = z;
        apply(state);
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public final void onThemeChanged() {
        ColorExtractor.GradientColors gradientColors;
        int i;
        if (this.mNotificationShadeView != null) {
            SysuiColorExtractor sysuiColorExtractor = this.mColorExtractor;
            Objects.requireNonNull(sysuiColorExtractor);
            if (sysuiColorExtractor.mHasMediaArtwork) {
                gradientColors = sysuiColorExtractor.mBackdropColors;
            } else {
                gradientColors = sysuiColorExtractor.mNeutralColorsLock;
            }
            boolean supportsDarkText = gradientColors.supportsDarkText();
            int systemUiVisibility = this.mNotificationShadeView.getSystemUiVisibility();
            if (supportsDarkText) {
                i = systemUiVisibility | 16 | 8192;
            } else {
                i = systemUiVisibility & (-17) & (-8193);
            }
            this.mNotificationShadeView.setSystemUiVisibility(i);
        }
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final void setBackdropShowing(boolean z) {
        State state = this.mCurrentState;
        state.mBackdropShowing = z;
        apply(state);
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final void setBackgroundBlurRadius(int i) {
        State state = this.mCurrentState;
        if (state.mBackgroundBlurRadius != i) {
            state.mBackgroundBlurRadius = i;
            apply(state);
        }
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final void setBouncerShowing(boolean z) {
        State state = this.mCurrentState;
        state.mBouncerShowing = z;
        apply(state);
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final void setForceDozeBrightness(boolean z) {
        State state = this.mCurrentState;
        state.mForceDozeBrightness = z;
        apply(state);
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final void setForcePluginOpen(boolean z, Object obj) {
        if (z) {
            this.mForceOpenTokens.add(obj);
        } else {
            this.mForceOpenTokens.remove(obj);
        }
        State state = this.mCurrentState;
        boolean z2 = state.mForcePluginOpen;
        state.mForcePluginOpen = !this.mForceOpenTokens.isEmpty();
        State state2 = this.mCurrentState;
        if (z2 != state2.mForcePluginOpen) {
            apply(state2);
            NotificationShadeWindowController.ForcePluginOpenListener forcePluginOpenListener = this.mForcePluginOpenListener;
            if (forcePluginOpenListener != null) {
                boolean z3 = this.mCurrentState.mForcePluginOpen;
                StatusBarTouchableRegionManager statusBarTouchableRegionManager = ((StatusBarTouchableRegionManager$$ExternalSyntheticLambda0) forcePluginOpenListener).f$0;
                Objects.requireNonNull(statusBarTouchableRegionManager);
                statusBarTouchableRegionManager.updateTouchableRegion();
            }
        }
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final void setForceWindowCollapsed(boolean z) {
        State state = this.mCurrentState;
        state.mForceCollapsed = z;
        apply(state);
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final void setHeadsUpShowing(boolean z) {
        State state = this.mCurrentState;
        state.mHeadsUpShowing = z;
        apply(state);
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final void setKeyguardFadingAway(boolean z) {
        State state = this.mCurrentState;
        state.mKeyguardFadingAway = z;
        apply(state);
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final void setKeyguardGoingAway(boolean z) {
        State state = this.mCurrentState;
        state.mKeyguardGoingAway = z;
        apply(state);
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final void setKeyguardNeedsInput(boolean z) {
        State state = this.mCurrentState;
        state.mKeyguardNeedsInput = z;
        apply(state);
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final void setKeyguardOccluded(boolean z) {
        State state = this.mCurrentState;
        state.mKeyguardOccluded = z;
        apply(state);
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final void setKeyguardShowing(boolean z) {
        State state = this.mCurrentState;
        state.mKeyguardShowing = z;
        apply(state);
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final void setLaunchingActivity(boolean z) {
        State state = this.mCurrentState;
        state.mLaunchingActivity = z;
        apply(state);
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final void setLightRevealScrimOpaque(boolean z) {
        State state = this.mCurrentState;
        if (state.mLightRevealScrimOpaque != z) {
            state.mLightRevealScrimOpaque = z;
            apply(state);
        }
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final void setNotTouchable() {
        State state = this.mCurrentState;
        state.mNotTouchable = false;
        apply(state);
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final void setNotificationShadeFocusable(boolean z) {
        State state = this.mCurrentState;
        state.mNotificationShadeFocusable = z;
        apply(state);
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final void setPanelExpanded(boolean z) {
        State state = this.mCurrentState;
        state.mPanelExpanded = z;
        apply(state);
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final void setPanelVisible(boolean z) {
        State state = this.mCurrentState;
        state.mPanelVisible = z;
        state.mNotificationShadeFocusable = z;
        apply(state);
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final void setQsExpanded(boolean z) {
        State state = this.mCurrentState;
        state.mQsExpanded = z;
        apply(state);
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final void setRequestTopUi(boolean z, String str) {
        if (z) {
            this.mCurrentState.mComponentsForcingTopUi.add(str);
        } else {
            this.mCurrentState.mComponentsForcingTopUi.remove(str);
        }
        apply(this.mCurrentState);
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final void setScrimsVisibility(int i) {
        State state = this.mCurrentState;
        state.mScrimsVisibility = i;
        apply(state);
        this.mScrimsVisibilityListener.accept(Integer.valueOf(i));
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final void setScrimsVisibilityListener(NotificationShadeDepthController.AnonymousClass1 r2) {
        if (this.mScrimsVisibilityListener != r2) {
            this.mScrimsVisibilityListener = r2;
        }
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final void setWallpaperSupportsAmbientMode() {
        Objects.requireNonNull(this.mCurrentState);
        apply(this.mCurrentState);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.plugins.statusbar.StatusBarStateController$StateListener, com.android.systemui.statusbar.phone.NotificationShadeWindowControllerImpl$1] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public NotificationShadeWindowControllerImpl(android.content.Context r3, android.view.WindowManager r4, android.app.IActivityManager r5, com.android.systemui.statusbar.phone.DozeParameters r6, com.android.systemui.plugins.statusbar.StatusBarStateController r7, com.android.systemui.statusbar.policy.ConfigurationController r8, com.android.systemui.keyguard.KeyguardViewMediator r9, com.android.systemui.statusbar.phone.KeyguardBypassController r10, com.android.systemui.colorextraction.SysuiColorExtractor r11, com.android.systemui.dump.DumpManager r12, com.android.systemui.statusbar.policy.KeyguardStateController r13, com.android.systemui.statusbar.phone.ScreenOffAnimationController r14, com.android.systemui.biometrics.AuthController r15) {
        /*
            r2 = this;
            r2.<init>()
            com.android.systemui.statusbar.phone.NotificationShadeWindowControllerImpl$State r0 = new com.android.systemui.statusbar.phone.NotificationShadeWindowControllerImpl$State
            r0.<init>()
            r2.mCurrentState = r0
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            r2.mCallbacks = r0
            r0 = -1082130432(0xffffffffbf800000, float:-1.0)
            r2.mFaceAuthDisplayBrightness = r0
            java.util.HashSet r1 = new java.util.HashSet
            r1.<init>()
            r2.mForceOpenTokens = r1
            com.android.systemui.statusbar.phone.NotificationShadeWindowControllerImpl$1 r1 = new com.android.systemui.statusbar.phone.NotificationShadeWindowControllerImpl$1
            r1.<init>()
            r2.mStateListener = r1
            r2.mContext = r3
            r2.mWindowManager = r4
            r2.mActivityManager = r5
            r2.mDozeParameters = r6
            r2.mKeyguardStateController = r13
            java.util.Objects.requireNonNull(r6)
            android.content.res.Resources r4 = r6.mResources
            r5 = 17694919(0x10e00c7, float:2.608184E-38)
            int r4 = r4.getInteger(r5)
            float r4 = (float) r4
            r5 = 1132396544(0x437f0000, float:255.0)
            float r4 = r4 / r5
            r2.mScreenBrightnessDoze = r4
            android.view.WindowManager$LayoutParams r4 = new android.view.WindowManager$LayoutParams
            r4.<init>()
            r2.mLpChanged = r4
            r2.mKeyguardViewMediator = r9
            r2.mKeyguardBypassController = r10
            r2.mColorExtractor = r11
            r2.mScreenOffAnimationController = r14
            java.lang.Class<com.android.systemui.statusbar.phone.NotificationShadeWindowControllerImpl> r4 = com.android.systemui.statusbar.phone.NotificationShadeWindowControllerImpl.class
            java.lang.String r4 = r4.getName()
            r12.registerDumpable(r4, r2)
            r2.mAuthController = r15
            android.content.res.Resources r4 = r3.getResources()
            r5 = 2131492896(0x7f0c0020, float:1.8609257E38)
            int r4 = r4.getInteger(r5)
            long r4 = (long) r4
            r2.mLockScreenDisplayTimeout = r4
            com.android.systemui.statusbar.SysuiStatusBarStateController r7 = (com.android.systemui.statusbar.SysuiStatusBarStateController) r7
            r4 = 1
            r7.addCallback(r1, r4)
            r8.addCallback(r2)
            android.content.res.Resources r4 = r3.getResources()
            r5 = 2131492895(0x7f0c001f, float:1.8609255E38)
            int r4 = r4.getInteger(r5)
            float r4 = (float) r4
            int r5 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1))
            if (r5 <= 0) goto L_0x00a9
            android.view.Display r5 = r3.getDisplay()
            android.view.Display$Mode[] r5 = r5.getSupportedModes()
            int r6 = r5.length
            r7 = 0
        L_0x008a:
            if (r7 >= r6) goto L_0x00a9
            r8 = r5[r7]
            float r9 = r8.getRefreshRate()
            float r9 = r9 - r4
            float r9 = java.lang.Math.abs(r9)
            double r9 = (double) r9
            r11 = 4591870180066957722(0x3fb999999999999a, double:0.1)
            int r9 = (r9 > r11 ? 1 : (r9 == r11 ? 0 : -1))
            if (r9 > 0) goto L_0x00a6
            float r0 = r8.getRefreshRate()
            goto L_0x00a9
        L_0x00a6:
            int r7 = r7 + 1
            goto L_0x008a
        L_0x00a9:
            r2.mKeyguardPreferredRefreshRate = r0
            android.content.res.Resources r3 = r3.getResources()
            r4 = 2131492894(0x7f0c001e, float:1.8609253E38)
            int r3 = r3.getInteger(r4)
            float r3 = (float) r3
            r2.mKeyguardMaxRefreshRate = r3
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.NotificationShadeWindowControllerImpl.<init>(android.content.Context, android.view.WindowManager, android.app.IActivityManager, com.android.systemui.statusbar.phone.DozeParameters, com.android.systemui.plugins.statusbar.StatusBarStateController, com.android.systemui.statusbar.policy.ConfigurationController, com.android.systemui.keyguard.KeyguardViewMediator, com.android.systemui.statusbar.phone.KeyguardBypassController, com.android.systemui.colorextraction.SysuiColorExtractor, com.android.systemui.dump.DumpManager, com.android.systemui.statusbar.policy.KeyguardStateController, com.android.systemui.statusbar.phone.ScreenOffAnimationController, com.android.systemui.biometrics.AuthController):void");
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final void setForcePluginOpenListener(StatusBarTouchableRegionManager$$ExternalSyntheticLambda0 statusBarTouchableRegionManager$$ExternalSyntheticLambda0) {
        this.mForcePluginOpenListener = statusBarTouchableRegionManager$$ExternalSyntheticLambda0;
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final void setNotificationShadeView(NotificationShadeWindowView notificationShadeWindowView) {
        this.mNotificationShadeView = notificationShadeWindowView;
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final void setStateListener(StatusBar$2$Callback$$ExternalSyntheticLambda0 statusBar$2$Callback$$ExternalSyntheticLambda0) {
        this.mListener = statusBar$2$Callback$$ExternalSyntheticLambda0;
    }

    @Override // com.android.systemui.statusbar.NotificationShadeWindowController
    public final ViewGroup getNotificationShadeView() {
        return this.mNotificationShadeView;
    }
}

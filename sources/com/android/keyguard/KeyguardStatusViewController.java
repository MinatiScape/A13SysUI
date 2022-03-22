package com.android.keyguard;

import android.app.smartspace.SmartspaceSession;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Trace;
import android.text.TextUtils;
import android.util.Log;
import android.util.Slog;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.slice.Slice;
import androidx.slice.SliceConvert;
import androidx.slice.SliceViewManagerWrapper;
import com.android.systemui.communal.CommunalStateController;
import com.android.systemui.keyguard.KeyguardSliceProvider;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import com.android.systemui.plugins.ClockPlugin;
import com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController;
import com.android.systemui.statusbar.notification.AnimatableProperty;
import com.android.systemui.statusbar.notification.PropertyAnimator;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.ViewController;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
/* loaded from: classes.dex */
public final class KeyguardStatusViewController extends ViewController<KeyguardStatusView> {
    public static final AnimationProperties CLOCK_ANIMATION_PROPERTIES;
    public static final boolean DEBUG = KeyguardConstants.DEBUG;
    public final ConfigurationController mConfigurationController;
    public final KeyguardClockSwitchController mKeyguardClockSwitchController;
    public final KeyguardSliceViewController mKeyguardSliceViewController;
    public final KeyguardStateController mKeyguardStateController;
    public final KeyguardUnlockAnimationController mKeyguardUnlockAnimationController;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final KeyguardVisibilityHelper mKeyguardVisibilityHelper;
    public final Rect mClipBounds = new Rect();
    public final AnonymousClass1 mConfigurationListener = new ConfigurationController.ConfigurationListener() { // from class: com.android.keyguard.KeyguardStatusViewController.1
        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public final void onDensityOrFontScaleChanged() {
            KeyguardClockSwitchController keyguardClockSwitchController = KeyguardStatusViewController.this.mKeyguardClockSwitchController;
            Objects.requireNonNull(keyguardClockSwitchController);
            ((KeyguardClockSwitch) keyguardClockSwitchController.mView).onDensityOrFontScaleChanged();
            keyguardClockSwitchController.mKeyguardClockTopMargin = ((KeyguardClockSwitch) keyguardClockSwitchController.mView).getResources().getDimensionPixelSize(2131165844);
            keyguardClockSwitchController.updateClockLayout();
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public final void onLocaleListChanged() {
            KeyguardStatusViewController.this.refreshTime();
        }
    };
    public AnonymousClass2 mInfoCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.keyguard.KeyguardStatusViewController.2
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onKeyguardVisibilityChanged(boolean z) {
            if (z) {
                if (KeyguardStatusViewController.DEBUG) {
                    Slog.v("KeyguardStatusViewController", "refresh statusview showing:" + z);
                }
                KeyguardStatusViewController.this.refreshTime();
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onTimeChanged() {
            KeyguardStatusViewController.this.refreshTime();
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onTimeFormatChanged(String str) {
            KeyguardClockSwitchController keyguardClockSwitchController = KeyguardStatusViewController.this.mKeyguardClockSwitchController;
            Objects.requireNonNull(keyguardClockSwitchController);
            AnimatableClockController animatableClockController = keyguardClockSwitchController.mClockViewController;
            if (animatableClockController != null) {
                ((AnimatableClockView) animatableClockController.mView).refreshFormat();
                AnimatableClockController animatableClockController2 = keyguardClockSwitchController.mLargeClockViewController;
                Objects.requireNonNull(animatableClockController2);
                ((AnimatableClockView) animatableClockController2.mView).refreshFormat();
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onTimeZoneChanged(TimeZone timeZone) {
            KeyguardClockSwitchController keyguardClockSwitchController = KeyguardStatusViewController.this.mKeyguardClockSwitchController;
            Objects.requireNonNull(keyguardClockSwitchController);
            KeyguardClockSwitch keyguardClockSwitch = (KeyguardClockSwitch) keyguardClockSwitchController.mView;
            Objects.requireNonNull(keyguardClockSwitch);
            ClockPlugin clockPlugin = keyguardClockSwitch.mClockPlugin;
            if (clockPlugin != null) {
                clockPlugin.onTimeZoneChanged(timeZone);
            }
            AnimatableClockController animatableClockController = keyguardClockSwitchController.mClockViewController;
            if (animatableClockController != null) {
                AnimatableClockView animatableClockView = (AnimatableClockView) animatableClockController.mView;
                Objects.requireNonNull(animatableClockView);
                animatableClockView.time.setTimeZone(timeZone);
                animatableClockView.refreshFormat();
                AnimatableClockController animatableClockController2 = keyguardClockSwitchController.mLargeClockViewController;
                Objects.requireNonNull(animatableClockController2);
                AnimatableClockView animatableClockView2 = (AnimatableClockView) animatableClockController2.mView;
                Objects.requireNonNull(animatableClockView2);
                animatableClockView2.time.setTimeZone(timeZone);
                animatableClockView2.refreshFormat();
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onUserSwitchComplete(int i) {
            KeyguardClockSwitchController keyguardClockSwitchController = KeyguardStatusViewController.this.mKeyguardClockSwitchController;
            Objects.requireNonNull(keyguardClockSwitchController);
            AnimatableClockController animatableClockController = keyguardClockSwitchController.mClockViewController;
            if (animatableClockController != null) {
                ((AnimatableClockView) animatableClockController.mView).refreshFormat();
                AnimatableClockController animatableClockController2 = keyguardClockSwitchController.mLargeClockViewController;
                Objects.requireNonNull(animatableClockController2);
                ((AnimatableClockView) animatableClockController2.mView).refreshFormat();
            }
        }
    };
    public AnonymousClass3 mKeyguardStateControllerCallback = new KeyguardStateController.Callback() { // from class: com.android.keyguard.KeyguardStatusViewController.3
        @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
        public final void onKeyguardShowingChanged() {
            if (KeyguardStatusViewController.this.mKeyguardStateController.isShowing()) {
                ((KeyguardStatusView) KeyguardStatusViewController.this.mView).setChildrenAlphaExcludingClockView(1.0f);
            }
        }
    };

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.keyguard.KeyguardStatusViewController$1] */
    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.keyguard.KeyguardStatusViewController$2] */
    /* JADX WARN: Type inference failed for: r1v3, types: [com.android.keyguard.KeyguardStatusViewController$3] */
    public KeyguardStatusViewController(KeyguardStatusView keyguardStatusView, KeyguardSliceViewController keyguardSliceViewController, KeyguardClockSwitchController keyguardClockSwitchController, KeyguardStateController keyguardStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, CommunalStateController communalStateController, ConfigurationController configurationController, KeyguardUnlockAnimationController keyguardUnlockAnimationController, ScreenOffAnimationController screenOffAnimationController) {
        super(keyguardStatusView);
        this.mKeyguardSliceViewController = keyguardSliceViewController;
        this.mKeyguardClockSwitchController = keyguardClockSwitchController;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mConfigurationController = configurationController;
        this.mKeyguardStateController = keyguardStateController;
        this.mKeyguardVisibilityHelper = new KeyguardVisibilityHelper(keyguardStatusView, communalStateController, keyguardStateController, screenOffAnimationController, true, false);
        this.mKeyguardUnlockAnimationController = keyguardUnlockAnimationController;
    }

    static {
        AnimationProperties animationProperties = new AnimationProperties();
        animationProperties.duration = 360L;
        CLOCK_ANIMATION_PROPERTIES = animationProperties;
    }

    @Override // com.android.systemui.util.ViewController
    public final void onInit() {
        this.mKeyguardClockSwitchController.init();
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewAttached() {
        this.mKeyguardUpdateMonitor.registerCallback(this.mInfoCallback);
        this.mConfigurationController.addCallback(this.mConfigurationListener);
        this.mKeyguardStateController.addCallback(this.mKeyguardStateControllerCallback);
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewDetached() {
        this.mKeyguardUpdateMonitor.removeCallback(this.mInfoCallback);
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
        this.mKeyguardStateController.removeCallback(this.mKeyguardStateControllerCallback);
    }

    public final void refreshTime() {
        SmartspaceSession smartspaceSession;
        KeyguardClockSwitchController keyguardClockSwitchController = this.mKeyguardClockSwitchController;
        Objects.requireNonNull(keyguardClockSwitchController);
        AnimatableClockController animatableClockController = keyguardClockSwitchController.mClockViewController;
        if (animatableClockController != null) {
            ((AnimatableClockView) animatableClockController.mView).refreshTime();
            AnimatableClockController animatableClockController2 = keyguardClockSwitchController.mLargeClockViewController;
            Objects.requireNonNull(animatableClockController2);
            ((AnimatableClockView) animatableClockController2.mView).refreshTime();
        }
        LockscreenSmartspaceController lockscreenSmartspaceController = keyguardClockSwitchController.mSmartspaceController;
        if (!(lockscreenSmartspaceController == null || (smartspaceSession = lockscreenSmartspaceController.session) == null)) {
            smartspaceSession.requestSmartspaceUpdate();
        }
        KeyguardClockSwitch keyguardClockSwitch = (KeyguardClockSwitch) keyguardClockSwitchController.mView;
        Objects.requireNonNull(keyguardClockSwitch);
        ClockPlugin clockPlugin = keyguardClockSwitch.mClockPlugin;
        if (clockPlugin != null) {
            clockPlugin.onTimeTick();
        }
    }

    public final void updatePosition(int i, int i2, float f, boolean z) {
        AnimationProperties animationProperties = CLOCK_ANIMATION_PROPERTIES;
        PropertyAnimator.setProperty((KeyguardStatusView) this.mView, AnimatableProperty.Y, i2, animationProperties, z);
        KeyguardClockSwitchController keyguardClockSwitchController = this.mKeyguardClockSwitchController;
        Objects.requireNonNull(keyguardClockSwitchController);
        if (TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == 1) {
            i = -i;
        }
        FrameLayout frameLayout = keyguardClockSwitchController.mClockFrame;
        AnimatableProperty.AnonymousClass7 r0 = AnimatableProperty.TRANSLATION_X;
        float f2 = i;
        PropertyAnimator.setProperty(frameLayout, r0, f2, animationProperties, z);
        PropertyAnimator.setProperty(keyguardClockSwitchController.mLargeClockFrame, AnimatableProperty.SCALE_X, f, animationProperties, z);
        PropertyAnimator.setProperty(keyguardClockSwitchController.mLargeClockFrame, AnimatableProperty.SCALE_Y, f, animationProperties, z);
        ViewGroup viewGroup = keyguardClockSwitchController.mStatusArea;
        if (viewGroup != null) {
            PropertyAnimator.setProperty(viewGroup, r0, f2, animationProperties, z);
            KeyguardUnlockAnimationController keyguardUnlockAnimationController = keyguardClockSwitchController.mKeyguardUnlockAnimationController;
            Objects.requireNonNull(keyguardUnlockAnimationController);
            if (keyguardUnlockAnimationController.unlockingWithSmartspaceTransition) {
                KeyguardUnlockAnimationController keyguardUnlockAnimationController2 = keyguardClockSwitchController.mKeyguardUnlockAnimationController;
                Objects.requireNonNull(keyguardUnlockAnimationController2);
                keyguardUnlockAnimationController2.setSmartspaceProgressToDestinationBounds(keyguardUnlockAnimationController2.smartspaceUnlockProgress);
            }
        }
    }

    public final void dozeTimeTick() {
        refreshTime();
        KeyguardSliceViewController keyguardSliceViewController = this.mKeyguardSliceViewController;
        Objects.requireNonNull(keyguardSliceViewController);
        Trace.beginSection("KeyguardSliceViewController#refresh");
        Slice slice = null;
        if ("content://com.android.systemui.keyguard/main".equals(keyguardSliceViewController.mKeyguardSliceUri.toString())) {
            KeyguardSliceProvider keyguardSliceProvider = KeyguardSliceProvider.sInstance;
            if (keyguardSliceProvider != null) {
                slice = keyguardSliceProvider.onBindSlice();
            } else {
                Log.w("KeyguardSliceViewCtrl", "Keyguard slice not bound yet?");
            }
        } else {
            SliceViewManagerWrapper sliceViewManagerWrapper = new SliceViewManagerWrapper(((KeyguardSliceView) keyguardSliceViewController.mView).getContext());
            Uri uri = keyguardSliceViewController.mKeyguardSliceUri;
            if (!sliceViewManagerWrapper.isAuthoritySuspended(uri.getAuthority())) {
                slice = SliceConvert.wrap(sliceViewManagerWrapper.mManager.bindSlice(uri, sliceViewManagerWrapper.mSpecs), sliceViewManagerWrapper.mContext);
            }
        }
        keyguardSliceViewController.mObserver.onChanged(slice);
        Trace.endSection();
    }
}

package com.android.systemui.biometrics;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.hardware.biometrics.BiometricSourceType;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import androidx.preference.R$id;
import com.android.internal.graphics.ColorUtils;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.settingslib.Utils;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.biometrics.AuthController;
import com.android.systemui.biometrics.UdfpsController;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.CircleReveal;
import com.android.systemui.statusbar.LiftReveal;
import com.android.systemui.statusbar.LightRevealScrim;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.charging.DwellRippleShader;
import com.android.systemui.statusbar.charging.RippleShader;
import com.android.systemui.statusbar.commandline.Command;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.ViewController;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import javax.inject.Provider;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: AuthRippleController.kt */
/* loaded from: classes.dex */
public final class AuthRippleController extends ViewController<AuthRippleView> implements KeyguardStateController.Callback, WakefulnessLifecycle.Observer {
    public final AuthController authController;
    public final BiometricUnlockController biometricUnlockController;
    public final KeyguardBypassController bypassController;
    public CircleReveal circleReveal;
    public final CommandRegistry commandRegistry;
    public final ConfigurationController configurationController;
    public PointF faceSensorLocation;
    public PointF fingerprintSensorLocation;
    public final KeyguardStateController keyguardStateController;
    public final KeyguardUpdateMonitor keyguardUpdateMonitor;
    public final NotificationShadeWindowController notificationShadeWindowController;
    public boolean startLightRevealScrimOnKeyguardFadingAway;
    public final StatusBar statusBar;
    public final StatusBarStateController statusBarStateController;
    public final Context sysuiContext;
    public UdfpsController udfpsController;
    public final Provider<UdfpsController> udfpsControllerProvider;
    public final WakefulnessLifecycle wakefulnessLifecycle;
    public float udfpsRadius = -1.0f;
    public final AuthRippleController$keyguardUpdateMonitorCallback$1 keyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.systemui.biometrics.AuthRippleController$keyguardUpdateMonitorCallback$1
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onBiometricAuthFailed(BiometricSourceType biometricSourceType) {
            ((AuthRippleView) AuthRippleController.this.mView).retractRipple();
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onBiometricAuthenticated(int i, BiometricSourceType biometricSourceType, boolean z) {
            AuthRippleController.this.showRipple(biometricSourceType);
        }
    };
    public final AuthRippleController$configurationChangedListener$1 configurationChangedListener = new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.biometrics.AuthRippleController$configurationChangedListener$1
        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public final void onThemeChanged() {
            AuthRippleController.this.updateRippleColor();
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public final void onUiModeChanged() {
            AuthRippleController.this.updateRippleColor();
        }
    };
    public final AuthRippleController$udfpsControllerCallback$1 udfpsControllerCallback = new UdfpsController.Callback() { // from class: com.android.systemui.biometrics.AuthRippleController$udfpsControllerCallback$1
        @Override // com.android.systemui.biometrics.UdfpsController.Callback
        public final void onFingerDown() {
            AuthRippleController authRippleController = AuthRippleController.this;
            Objects.requireNonNull(authRippleController);
            if (authRippleController.fingerprintSensorLocation == null) {
                Log.e("AuthRipple", "fingerprintSensorLocation=null onFingerDown. Skip showing dwell ripple");
                return;
            }
            AuthRippleController authRippleController2 = AuthRippleController.this;
            Objects.requireNonNull(authRippleController2);
            PointF pointF = authRippleController2.fingerprintSensorLocation;
            Intrinsics.checkNotNull(pointF);
            ((AuthRippleView) authRippleController2.mView).setFingerprintSensorLocation(pointF, AuthRippleController.this.udfpsRadius);
            AuthRippleController.access$showDwellRipple(AuthRippleController.this);
        }

        @Override // com.android.systemui.biometrics.UdfpsController.Callback
        public final void onFingerUp() {
            ((AuthRippleView) AuthRippleController.this.mView).retractRipple();
        }
    };
    public final AuthRippleController$authControllerCallback$1 authControllerCallback = new AuthController.Callback() { // from class: com.android.systemui.biometrics.AuthRippleController$authControllerCallback$1
        @Override // com.android.systemui.biometrics.AuthController.Callback
        public final void onEnrollmentsChanged() {
        }

        @Override // com.android.systemui.biometrics.AuthController.Callback
        public final void onAllAuthenticatorsRegistered() {
            AuthRippleController.this.updateUdfpsDependentParams();
            AuthRippleController.this.updateSensorLocation();
        }
    };

    /* compiled from: AuthRippleController.kt */
    /* loaded from: classes.dex */
    public final class AuthRippleCommand implements Command {
        public AuthRippleCommand() {
        }

        public final void invalidCommand(PrintWriter printWriter) {
            printWriter.println("invalid command");
            printWriter.println("Usage: adb shell cmd statusbar auth-ripple <command>");
            printWriter.println("Available commands:");
            printWriter.println("  dwell");
            printWriter.println("  fingerprint");
            printWriter.println("  face");
            printWriter.println("  custom <x-location: int> <y-location: int>");
        }

        /* JADX WARN: Removed duplicated region for block: B:23:0x00a5  */
        @Override // com.android.systemui.statusbar.commandline.Command
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void execute(java.io.PrintWriter r6, java.util.List<java.lang.String> r7) {
            /*
                Method dump skipped, instructions count: 340
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.biometrics.AuthRippleController.AuthRippleCommand.execute(java.io.PrintWriter, java.util.List):void");
        }
    }

    public static /* synthetic */ void getStartLightRevealScrimOnKeyguardFadingAway$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
    public final void onStartedGoingToSleep() {
        this.startLightRevealScrimOnKeyguardFadingAway = false;
    }

    @Override // com.android.systemui.util.ViewController
    public final void onInit() {
        AuthRippleView authRippleView = (AuthRippleView) this.mView;
        Objects.requireNonNull(authRippleView);
        authRippleView.alphaInDuration = this.sysuiContext.getResources().getInteger(2131492868);
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
    public final void onKeyguardFadingAwayChanged() {
        if (this.keyguardStateController.isKeyguardFadingAway()) {
            StatusBar statusBar = this.statusBar;
            Objects.requireNonNull(statusBar);
            final LightRevealScrim lightRevealScrim = statusBar.mLightRevealScrim;
            if (this.startLightRevealScrimOnKeyguardFadingAway && lightRevealScrim != null) {
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.1f, 1.0f);
                ofFloat.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
                ofFloat.setDuration(1533L);
                ofFloat.setStartDelay(this.keyguardStateController.getKeyguardFadingAwayDelay());
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.AuthRippleController$onKeyguardFadingAwayChanged$1$1
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        LightRevealScrim lightRevealScrim2 = LightRevealScrim.this;
                        Objects.requireNonNull(lightRevealScrim2);
                        if (Intrinsics.areEqual(lightRevealScrim2.revealEffect, this.circleReveal)) {
                            LightRevealScrim lightRevealScrim3 = LightRevealScrim.this;
                            Object animatedValue = valueAnimator.getAnimatedValue();
                            Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                            lightRevealScrim3.setRevealAmount(((Float) animatedValue).floatValue());
                        }
                    }
                });
                ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.biometrics.AuthRippleController$onKeyguardFadingAwayChanged$1$2
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationEnd(Animator animator) {
                        LightRevealScrim lightRevealScrim2 = LightRevealScrim.this;
                        Objects.requireNonNull(lightRevealScrim2);
                        if (Intrinsics.areEqual(lightRevealScrim2.revealEffect, this.circleReveal)) {
                            LightRevealScrim.this.setRevealEffect(LiftReveal.INSTANCE);
                        }
                    }
                });
                ofFloat.start();
                this.startLightRevealScrimOnKeyguardFadingAway = false;
            }
        }
    }

    @Override // com.android.systemui.util.ViewController
    public void onViewAttached() {
        this.authController.addCallback(this.authControllerCallback);
        updateRippleColor();
        updateSensorLocation();
        updateUdfpsDependentParams();
        UdfpsController udfpsController = this.udfpsController;
        if (udfpsController != null) {
            udfpsController.mCallbacks.add(this.udfpsControllerCallback);
        }
        this.configurationController.addCallback(this.configurationChangedListener);
        this.keyguardUpdateMonitor.registerCallback(this.keyguardUpdateMonitorCallback);
        this.keyguardStateController.addCallback(this);
        WakefulnessLifecycle wakefulnessLifecycle = this.wakefulnessLifecycle;
        Objects.requireNonNull(wakefulnessLifecycle);
        wakefulnessLifecycle.mObservers.add(this);
        this.commandRegistry.registerCommand("auth-ripple", new AuthRippleController$onViewAttached$1(this));
    }

    @Override // com.android.systemui.util.ViewController
    public void onViewDetached() {
        UdfpsController udfpsController = this.udfpsController;
        if (udfpsController != null) {
            udfpsController.mCallbacks.remove(this.udfpsControllerCallback);
        }
        AuthController authController = this.authController;
        AuthRippleController$authControllerCallback$1 authRippleController$authControllerCallback$1 = this.authControllerCallback;
        Objects.requireNonNull(authController);
        authController.mCallbacks.remove(authRippleController$authControllerCallback$1);
        this.keyguardUpdateMonitor.removeCallback(this.keyguardUpdateMonitorCallback);
        this.configurationController.removeCallback(this.configurationChangedListener);
        this.keyguardStateController.removeCallback(this);
        WakefulnessLifecycle wakefulnessLifecycle = this.wakefulnessLifecycle;
        Objects.requireNonNull(wakefulnessLifecycle);
        wakefulnessLifecycle.mObservers.remove(this);
        CommandRegistry commandRegistry = this.commandRegistry;
        Objects.requireNonNull(commandRegistry);
        synchronized (commandRegistry) {
            commandRegistry.commandMap.remove("auth-ripple");
        }
        this.notificationShadeWindowController.setForcePluginOpen(false, this);
    }

    public final void showRipple(BiometricSourceType biometricSourceType) {
        boolean z;
        PointF pointF;
        KeyguardUpdateMonitor keyguardUpdateMonitor = this.keyguardUpdateMonitor;
        Objects.requireNonNull(keyguardUpdateMonitor);
        if (keyguardUpdateMonitor.mKeyguardIsVisible) {
            KeyguardUpdateMonitor keyguardUpdateMonitor2 = this.keyguardUpdateMonitor;
            Objects.requireNonNull(keyguardUpdateMonitor2);
            if (keyguardUpdateMonitor2.mStrongAuthTracker.getStrongAuthForUser(KeyguardUpdateMonitor.getCurrentUser()) != 0) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                updateSensorLocation();
                if (biometricSourceType == BiometricSourceType.FINGERPRINT && (pointF = this.fingerprintSensorLocation) != null) {
                    ((AuthRippleView) this.mView).setFingerprintSensorLocation(pointF, this.udfpsRadius);
                    showUnlockedRipple();
                } else if (biometricSourceType == BiometricSourceType.FACE && this.faceSensorLocation != null && this.bypassController.canBypass()) {
                    PointF pointF2 = this.faceSensorLocation;
                    Intrinsics.checkNotNull(pointF2);
                    ((AuthRippleView) this.mView).setSensorLocation(pointF2);
                    showUnlockedRipple();
                }
            }
        }
    }

    /* JADX WARN: Type inference failed for: r2v2, types: [com.android.systemui.biometrics.AuthRippleController$showUnlockedRipple$2] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void showUnlockedRipple() {
        /*
            r7 = this;
            com.android.systemui.statusbar.NotificationShadeWindowController r0 = r7.notificationShadeWindowController
            r1 = 1
            r0.setForcePluginOpen(r1, r7)
            com.android.systemui.statusbar.phone.StatusBar r0 = r7.statusBar
            java.util.Objects.requireNonNull(r0)
            com.android.systemui.statusbar.LightRevealScrim r0 = r0.mLightRevealScrim
            com.android.systemui.plugins.statusbar.StatusBarStateController r2 = r7.statusBarStateController
            boolean r2 = r2.isDozing()
            if (r2 != 0) goto L_0x001d
            com.android.systemui.statusbar.phone.BiometricUnlockController r2 = r7.biometricUnlockController
            boolean r2 = r2.isWakeAndUnlock()
            if (r2 == 0) goto L_0x002a
        L_0x001d:
            com.android.systemui.statusbar.CircleReveal r2 = r7.circleReveal
            if (r2 != 0) goto L_0x0022
            goto L_0x002a
        L_0x0022:
            if (r0 != 0) goto L_0x0025
            goto L_0x0028
        L_0x0025:
            r0.setRevealEffect(r2)
        L_0x0028:
            r7.startLightRevealScrimOnKeyguardFadingAway = r1
        L_0x002a:
            T extends android.view.View r0 = r7.mView
            com.android.systemui.biometrics.AuthRippleView r0 = (com.android.systemui.biometrics.AuthRippleView) r0
            com.android.systemui.biometrics.AuthRippleController$showUnlockedRipple$2 r2 = new com.android.systemui.biometrics.AuthRippleController$showUnlockedRipple$2
            r2.<init>()
            java.util.Objects.requireNonNull(r0)
            boolean r7 = r0.unlockedRippleInProgress
            if (r7 == 0) goto L_0x003b
            goto L_0x0087
        L_0x003b:
            r7 = 2
            float[] r3 = new float[r7]
            r3 = {x0088: FILL_ARRAY_DATA  , data: [0, 1065353216} // fill-array
            android.animation.ValueAnimator r3 = android.animation.ValueAnimator.ofFloat(r3)
            android.view.animation.PathInterpolator r4 = com.android.systemui.animation.Interpolators.LINEAR_OUT_SLOW_IN
            r3.setInterpolator(r4)
            r4 = 1533(0x5fd, double:7.574E-321)
            r3.setDuration(r4)
            com.android.systemui.biometrics.AuthRippleView$startUnlockedRipple$rippleAnimator$1$1 r4 = new com.android.systemui.biometrics.AuthRippleView$startUnlockedRipple$rippleAnimator$1$1
            r4.<init>()
            r3.addUpdateListener(r4)
            int[] r4 = new int[r7]
            r4 = {x0090: FILL_ARRAY_DATA  , data: [0, 255} // fill-array
            android.animation.ValueAnimator r4 = android.animation.ValueAnimator.ofInt(r4)
            long r5 = r0.alphaInDuration
            r4.setDuration(r5)
            com.android.systemui.biometrics.AuthRippleView$startUnlockedRipple$alphaInAnimator$1$1 r5 = new com.android.systemui.biometrics.AuthRippleView$startUnlockedRipple$alphaInAnimator$1$1
            r5.<init>()
            r4.addUpdateListener(r5)
            android.animation.AnimatorSet r5 = new android.animation.AnimatorSet
            r5.<init>()
            android.animation.Animator[] r7 = new android.animation.Animator[r7]
            r6 = 0
            r7[r6] = r3
            r7[r1] = r4
            r5.playTogether(r7)
            com.android.systemui.biometrics.AuthRippleView$startUnlockedRipple$animatorSet$1$1 r7 = new com.android.systemui.biometrics.AuthRippleView$startUnlockedRipple$animatorSet$1$1
            r7.<init>()
            r5.addListener(r7)
            r5.start()
        L_0x0087:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.biometrics.AuthRippleController.showUnlockedRipple():void");
    }

    public final void updateRippleColor() {
        AuthRippleView authRippleView = (AuthRippleView) this.mView;
        int colorAttrDefaultColor = Utils.getColorAttrDefaultColor(this.sysuiContext, 2130970104);
        Objects.requireNonNull(authRippleView);
        authRippleView.lockScreenColorVal = colorAttrDefaultColor;
        authRippleView.rippleShader.setColor(colorAttrDefaultColor);
        RippleShader rippleShader = authRippleView.rippleShader;
        Objects.requireNonNull(rippleShader);
        rippleShader.setColor(ColorUtils.setAlphaComponent(rippleShader.color, 255));
    }

    public final void updateSensorLocation() {
        PointF pointF;
        PointF pointF2;
        StatusBar statusBar;
        StatusBar statusBar2;
        PointF pointF3;
        PointF pointF4;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display display = this.sysuiContext.getDisplay();
        if (display != null) {
            display.getRealMetrics(displayMetrics);
        }
        int i = displayMetrics.widthPixels;
        int i2 = displayMetrics.heightPixels;
        AuthController authController = this.authController;
        Objects.requireNonNull(authController);
        if (authController.getUdfpsSensorLocation() != null) {
            pointF = authController.getUdfpsSensorLocation();
        } else {
            pointF = authController.mFingerprintLocation;
        }
        if (pointF != null) {
            int rotation = R$id.getRotation(this.sysuiContext);
            if (rotation != 1) {
                if (rotation == 2) {
                    pointF3 = new PointF(i - pointF.x, i2 - pointF.y);
                } else if (rotation != 3) {
                    pointF3 = new PointF(pointF.x, pointF.y);
                } else {
                    float f = i;
                    float f2 = i2;
                    pointF4 = new PointF((1 - (pointF.y / f)) * f, f2 * (pointF.x / f2));
                }
                this.fingerprintSensorLocation = pointF3;
            } else {
                float f3 = i;
                float f4 = i2;
                pointF4 = new PointF(f3 * (pointF.y / f3), (1 - (pointF.x / f4)) * f4);
            }
            pointF3 = pointF4;
            this.fingerprintSensorLocation = pointF3;
        }
        AuthController authController2 = this.authController;
        Objects.requireNonNull(authController2);
        if (authController2.mFaceProps == null || authController2.mFaceAuthSensorLocation == null) {
            pointF2 = null;
        } else {
            PointF pointF5 = authController2.mFaceAuthSensorLocation;
            pointF2 = new PointF(pointF5.x, pointF5.y);
        }
        this.faceSensorLocation = pointF2;
        PointF pointF6 = this.fingerprintSensorLocation;
        if (pointF6 != null) {
            float f5 = pointF6.x;
            float f6 = pointF6.y;
            Objects.requireNonNull(this.statusBar);
            float max = Math.max(f5, statusBar.mDisplayMetrics.widthPixels - pointF6.x);
            float f7 = pointF6.y;
            Objects.requireNonNull(this.statusBar);
            this.circleReveal = new CircleReveal(f5, f6, Math.max(max, Math.max(f7, statusBar2.mDisplayMetrics.heightPixels - pointF6.y)));
        }
    }

    public final void updateUdfpsDependentParams() {
        UdfpsController udfpsController;
        AuthController authController = this.authController;
        Objects.requireNonNull(authController);
        ArrayList arrayList = authController.mUdfpsProps;
        if (arrayList != null && arrayList.size() > 0) {
            this.udfpsRadius = ((FingerprintSensorPropertiesInternal) arrayList.get(0)).getLocation().sensorRadius;
            this.udfpsController = this.udfpsControllerProvider.mo144get();
            if (((AuthRippleView) this.mView).isAttachedToWindow() && (udfpsController = this.udfpsController) != null) {
                udfpsController.mCallbacks.add(this.udfpsControllerCallback);
            }
        }
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.systemui.biometrics.AuthRippleController$keyguardUpdateMonitorCallback$1] */
    /* JADX WARN: Type inference failed for: r1v3, types: [com.android.systemui.biometrics.AuthRippleController$configurationChangedListener$1] */
    /* JADX WARN: Type inference failed for: r1v4, types: [com.android.systemui.biometrics.AuthRippleController$udfpsControllerCallback$1] */
    /* JADX WARN: Type inference failed for: r1v5, types: [com.android.systemui.biometrics.AuthRippleController$authControllerCallback$1] */
    public AuthRippleController(StatusBar statusBar, Context context, AuthController authController, ConfigurationController configurationController, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardStateController keyguardStateController, WakefulnessLifecycle wakefulnessLifecycle, CommandRegistry commandRegistry, NotificationShadeWindowController notificationShadeWindowController, KeyguardBypassController keyguardBypassController, BiometricUnlockController biometricUnlockController, Provider<UdfpsController> provider, StatusBarStateController statusBarStateController, AuthRippleView authRippleView) {
        super(authRippleView);
        this.statusBar = statusBar;
        this.sysuiContext = context;
        this.authController = authController;
        this.configurationController = configurationController;
        this.keyguardUpdateMonitor = keyguardUpdateMonitor;
        this.keyguardStateController = keyguardStateController;
        this.wakefulnessLifecycle = wakefulnessLifecycle;
        this.commandRegistry = commandRegistry;
        this.notificationShadeWindowController = notificationShadeWindowController;
        this.bypassController = keyguardBypassController;
        this.biometricUnlockController = biometricUnlockController;
        this.udfpsControllerProvider = provider;
        this.statusBarStateController = statusBarStateController;
    }

    public static final void access$showDwellRipple(AuthRippleController authRippleController) {
        boolean z;
        Objects.requireNonNull(authRippleController);
        final AuthRippleView authRippleView = (AuthRippleView) authRippleController.mView;
        boolean isDozing = authRippleController.statusBarStateController.isDozing();
        Objects.requireNonNull(authRippleView);
        if (!authRippleView.unlockedRippleInProgress) {
            AnimatorSet animatorSet = authRippleView.dwellPulseOutAnimator;
            if (animatorSet != null && animatorSet.isRunning()) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                if (isDozing) {
                    DwellRippleShader dwellRippleShader = authRippleView.dwellShader;
                    Objects.requireNonNull(dwellRippleShader);
                    dwellRippleShader.color = -1;
                    dwellRippleShader.setColorUniform("in_color", -1);
                } else {
                    DwellRippleShader dwellRippleShader2 = authRippleView.dwellShader;
                    int i = authRippleView.lockScreenColorVal;
                    Objects.requireNonNull(dwellRippleShader2);
                    dwellRippleShader2.color = i;
                    dwellRippleShader2.setColorUniform("in_color", i);
                }
                DwellRippleShader dwellRippleShader3 = authRippleView.dwellShader;
                Objects.requireNonNull(dwellRippleShader3);
                int alphaComponent = ColorUtils.setAlphaComponent(dwellRippleShader3.color, 255);
                dwellRippleShader3.color = alphaComponent;
                dwellRippleShader3.setColorUniform("in_color", alphaComponent);
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 0.8f);
                ofFloat.setInterpolator(Interpolators.LINEAR);
                ofFloat.setDuration(authRippleView.dwellPulseDuration);
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.AuthRippleView$startDwellRipple$dwellPulseOutRippleAnimator$1$1
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        long currentPlayTime = valueAnimator.getCurrentPlayTime();
                        DwellRippleShader dwellRippleShader4 = AuthRippleView.this.dwellShader;
                        Object animatedValue = valueAnimator.getAnimatedValue();
                        Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                        dwellRippleShader4.setProgress(((Float) animatedValue).floatValue());
                        AuthRippleView.this.dwellShader.setTime((float) currentPlayTime);
                        AuthRippleView.this.invalidate();
                    }
                });
                ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.8f, 1.0f);
                ofFloat2.setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN);
                ofFloat2.setDuration(authRippleView.dwellExpandDuration);
                ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.biometrics.AuthRippleView$startDwellRipple$expandDwellRippleAnimator$1$1
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        long currentPlayTime = valueAnimator.getCurrentPlayTime();
                        DwellRippleShader dwellRippleShader4 = AuthRippleView.this.dwellShader;
                        Object animatedValue = valueAnimator.getAnimatedValue();
                        Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                        dwellRippleShader4.setProgress(((Float) animatedValue).floatValue());
                        AuthRippleView.this.dwellShader.setTime((float) currentPlayTime);
                        AuthRippleView.this.invalidate();
                    }
                });
                AnimatorSet animatorSet2 = new AnimatorSet();
                animatorSet2.playSequentially(ofFloat, ofFloat2);
                animatorSet2.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.biometrics.AuthRippleView$startDwellRipple$1$1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationEnd(Animator animator) {
                        AuthRippleView authRippleView2 = AuthRippleView.this;
                        authRippleView2.drawDwell = false;
                        RippleShader rippleShader = authRippleView2.rippleShader;
                        Objects.requireNonNull(rippleShader);
                        rippleShader.setColor(ColorUtils.setAlphaComponent(rippleShader.color, 255));
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationStart(Animator animator) {
                        AnimatorSet animatorSet3 = AuthRippleView.this.retractAnimator;
                        if (animatorSet3 != null) {
                            animatorSet3.cancel();
                        }
                        AuthRippleView.this.setVisibility(0);
                        AuthRippleView.this.drawDwell = true;
                    }
                });
                animatorSet2.start();
                authRippleView.dwellPulseOutAnimator = animatorSet2;
            }
        }
    }
}

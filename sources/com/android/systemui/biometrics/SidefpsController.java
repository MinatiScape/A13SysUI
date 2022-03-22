package com.android.systemui.biometrics;

import android.app.ActivityTaskManager;
import android.content.Context;
import android.graphics.Rect;
import android.hardware.biometrics.SensorLocationInternal;
import android.hardware.fingerprint.FingerprintSensorPropertiesInternal;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.WindowManager;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieComposition;
import com.airbnb.lottie.LottieOnCompositionLoadedListener;
import com.android.internal.annotations.VisibleForTesting;
import java.util.Objects;
import kotlin.Pair;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SidefpsController.kt */
/* loaded from: classes.dex */
public final class SidefpsController {
    public final ActivityTaskManager activityTaskManager;
    public final long animationDuration;
    public final Context context;
    public final Handler handler;
    public final LayoutInflater layoutInflater;
    public final BiometricDisplayListener orientationListener;
    public ViewPropertyAnimator overlayHideAnimator;
    public SensorLocationInternal overlayOffsets;
    public View overlayView;
    public final WindowManager.LayoutParams overlayViewParams;
    public final SidefpsController$overviewProxyListener$1 overviewProxyListener;
    public final FingerprintSensorPropertiesInternal sensorProps;
    public final WindowManager windowManager;

    @VisibleForTesting
    public static /* synthetic */ void getOrientationListener$annotations() {
    }

    @VisibleForTesting
    public static /* synthetic */ void getOverviewProxyListener$annotations() {
    }

    @VisibleForTesting
    public static /* synthetic */ void getSensorProps$annotations() {
    }

    public final View createOverlayForDisplay() {
        boolean z;
        Rect rect = null;
        boolean z2 = false;
        final View inflate = this.layoutInflater.inflate(2131624487, (ViewGroup) null, false);
        final Display display = this.context.getDisplay();
        Intrinsics.checkNotNull(display);
        SensorLocationInternal location = this.sensorProps.getLocation(display.getUniqueId());
        if (location == null) {
            Log.w("SidefpsController", Intrinsics.stringPlus("No location specified for display: ", display.getUniqueId()));
        }
        if (location == null) {
            location = this.sensorProps.getLocation();
        }
        this.overlayOffsets = location;
        View findViewById = inflate.findViewById(2131428859);
        Objects.requireNonNull(findViewById, "null cannot be cast to non-null type com.airbnb.lottie.LottieAnimationView");
        final LottieAnimationView lottieAnimationView = (LottieAnimationView) findViewById;
        if (location.sensorLocationY != 0) {
            z = true;
        } else {
            z = false;
        }
        int rotation = display.getRotation();
        float f = 0.0f;
        if (rotation == 1 ? !z : rotation == 2 || (rotation == 3 && z)) {
            f = 180.0f;
        }
        inflate.setRotation(f);
        LottieComposition composition = lottieAnimationView.getComposition();
        if (composition != null) {
            rect = composition.bounds;
        }
        if (rect == null) {
            rect = new Rect();
        }
        updateOverlayParams(display, rect);
        if (location.sensorLocationY != 0) {
            z2 = true;
        }
        int rotation2 = display.getRotation();
        int i = 2131886087;
        if (rotation2 == 0 ? !z2 : rotation2 == 2 ? !z2 : z2) {
            i = 2131886088;
        }
        lottieAnimationView.setAnimation(i);
        lottieAnimationView.addLottieOnCompositionLoadedListener(new LottieOnCompositionLoadedListener() { // from class: com.android.systemui.biometrics.SidefpsController$createOverlayForDisplay$1
            @Override // com.airbnb.lottie.LottieOnCompositionLoadedListener
            public final void onCompositionLoaded(LottieComposition lottieComposition) {
                if (Intrinsics.areEqual(SidefpsController.this.overlayView, inflate)) {
                    SidefpsController sidefpsController = SidefpsController.this;
                    Display display2 = display;
                    Objects.requireNonNull(lottieComposition);
                    sidefpsController.updateOverlayParams(display2, lottieComposition.bounds);
                    SidefpsController sidefpsController2 = SidefpsController.this;
                    sidefpsController2.windowManager.updateViewLayout(sidefpsController2.overlayView, sidefpsController2.overlayViewParams);
                }
            }
        });
        final Context context = this.context;
        if (lottieAnimationView.getComposition() != null) {
            SidefpsControllerKt.addOverlayDynamicColor$update(context, lottieAnimationView);
        } else {
            lottieAnimationView.addLottieOnCompositionLoadedListener(new LottieOnCompositionLoadedListener() { // from class: com.android.systemui.biometrics.SidefpsControllerKt$addOverlayDynamicColor$1
                @Override // com.airbnb.lottie.LottieOnCompositionLoadedListener
                public final void onCompositionLoaded(LottieComposition lottieComposition) {
                    SidefpsControllerKt.addOverlayDynamicColor$update(context, lottieAnimationView);
                }
            });
        }
        return inflate;
    }

    public final void setOverlayView(View view) {
        View view2 = this.overlayView;
        if (view2 != null) {
            this.windowManager.removeView(view2);
            this.orientationListener.disable();
        }
        ViewPropertyAnimator viewPropertyAnimator = this.overlayHideAnimator;
        if (viewPropertyAnimator != null) {
            viewPropertyAnimator.cancel();
        }
        this.overlayHideAnimator = null;
        this.overlayView = view;
        if (view != null) {
            this.windowManager.addView(view, this.overlayViewParams);
            updateOverlayVisibility(view);
            this.orientationListener.enable();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:22:0x004e, code lost:
        if (r2 == false) goto L_0x0050;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0062, code lost:
        if (r4 == false) goto L_0x0064;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0064, code lost:
        r6.overlayHideAnimator = r7.animate().alpha(0.0f).setStartDelay(3000).setDuration(r6.animationDuration).setListener(new com.android.systemui.biometrics.SidefpsController$updateOverlayVisibility$1(r7, r6));
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:?, code lost:
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateOverlayVisibility(final android.view.View r7) {
        /*
            r6 = this;
            android.view.View r0 = r6.overlayView
            boolean r0 = kotlin.jvm.internal.Intrinsics.areEqual(r7, r0)
            if (r0 != 0) goto L_0x0009
            return
        L_0x0009:
            android.content.Context r0 = r6.context
            android.view.Display r0 = r0.getDisplay()
            r1 = 0
            if (r0 != 0) goto L_0x0014
            r0 = r1
            goto L_0x001c
        L_0x0014:
            int r0 = r0.getRotation()
            java.lang.Integer r0 = java.lang.Integer.valueOf(r0)
        L_0x001c:
            android.view.WindowManager r2 = r6.windowManager
            android.view.WindowMetrics r2 = r2.getCurrentWindowMetrics()
            android.view.WindowInsets r2 = r2.getWindowInsets()
            int r3 = android.view.WindowInsets.Type.navigationBars()
            android.graphics.Insets r2 = r2.getInsets(r3)
            int r2 = r2.bottom
            r3 = 70
            r4 = 1
            r5 = 0
            if (r2 < r3) goto L_0x0038
            r2 = r4
            goto L_0x0039
        L_0x0038:
            r2 = r5
        L_0x0039:
            if (r2 == 0) goto L_0x0085
            r2 = 3
            if (r0 != 0) goto L_0x003f
            goto L_0x0050
        L_0x003f:
            int r3 = r0.intValue()
            if (r3 != r2) goto L_0x0050
            android.hardware.biometrics.SensorLocationInternal r2 = r6.overlayOffsets
            int r2 = r2.sensorLocationY
            if (r2 == 0) goto L_0x004d
            r2 = r4
            goto L_0x004e
        L_0x004d:
            r2 = r5
        L_0x004e:
            if (r2 != 0) goto L_0x0064
        L_0x0050:
            r2 = 2
            if (r0 != 0) goto L_0x0054
            goto L_0x0085
        L_0x0054:
            int r0 = r0.intValue()
            if (r0 != r2) goto L_0x0085
            android.hardware.biometrics.SensorLocationInternal r0 = r6.overlayOffsets
            int r0 = r0.sensorLocationY
            if (r0 == 0) goto L_0x0061
            goto L_0x0062
        L_0x0061:
            r4 = r5
        L_0x0062:
            if (r4 != 0) goto L_0x0085
        L_0x0064:
            android.view.ViewPropertyAnimator r0 = r7.animate()
            r1 = 0
            android.view.ViewPropertyAnimator r0 = r0.alpha(r1)
            r1 = 3000(0xbb8, double:1.482E-320)
            android.view.ViewPropertyAnimator r0 = r0.setStartDelay(r1)
            long r1 = r6.animationDuration
            android.view.ViewPropertyAnimator r0 = r0.setDuration(r1)
            com.android.systemui.biometrics.SidefpsController$updateOverlayVisibility$1 r1 = new com.android.systemui.biometrics.SidefpsController$updateOverlayVisibility$1
            r1.<init>()
            android.view.ViewPropertyAnimator r7 = r0.setListener(r1)
            r6.overlayHideAnimator = r7
            goto L_0x0097
        L_0x0085:
            android.view.ViewPropertyAnimator r0 = r6.overlayHideAnimator
            if (r0 != 0) goto L_0x008a
            goto L_0x008d
        L_0x008a:
            r0.cancel()
        L_0x008d:
            r6.overlayHideAnimator = r1
            r6 = 1065353216(0x3f800000, float:1.0)
            r7.setAlpha(r6)
            r7.setVisibility(r5)
        L_0x0097:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.biometrics.SidefpsController.updateOverlayVisibility(android.view.View):void");
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r7v4, types: [com.android.systemui.recents.OverviewProxyService$OverviewProxyListener, com.android.systemui.biometrics.SidefpsController$overviewProxyListener$1] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public SidefpsController(android.content.Context r7, android.view.LayoutInflater r8, android.hardware.fingerprint.FingerprintManager r9, android.view.WindowManager r10, android.app.ActivityTaskManager r11, com.android.systemui.recents.OverviewProxyService r12, android.hardware.display.DisplayManager r13, final com.android.systemui.util.concurrency.DelayableExecutor r14, android.os.Handler r15) {
        /*
            r6 = this;
            r6.<init>()
            r6.context = r7
            r6.layoutInflater = r8
            r6.windowManager = r10
            r6.activityTaskManager = r11
            r6.handler = r15
            r7 = 0
            if (r9 != 0) goto L_0x0011
            goto L_0x0032
        L_0x0011:
            java.util.List r8 = r9.getSensorPropertiesInternal()
            if (r8 != 0) goto L_0x0018
            goto L_0x0032
        L_0x0018:
            java.util.Iterator r8 = r8.iterator()
        L_0x001c:
            boolean r10 = r8.hasNext()
            if (r10 == 0) goto L_0x0030
            java.lang.Object r10 = r8.next()
            r11 = r10
            android.hardware.fingerprint.FingerprintSensorPropertiesInternal r11 = (android.hardware.fingerprint.FingerprintSensorPropertiesInternal) r11
            boolean r11 = r11.isAnySidefpsType()
            if (r11 == 0) goto L_0x001c
            r7 = r10
        L_0x0030:
            android.hardware.fingerprint.FingerprintSensorPropertiesInternal r7 = (android.hardware.fingerprint.FingerprintSensorPropertiesInternal) r7
        L_0x0032:
            if (r7 == 0) goto L_0x009a
            r6.sensorProps = r7
            com.android.systemui.biometrics.BiometricDisplayListener r8 = new com.android.systemui.biometrics.BiometricDisplayListener
            android.content.Context r10 = r6.context
            android.os.Handler r3 = r6.handler
            com.android.systemui.biometrics.BiometricDisplayListener$SensorType$SideFingerprint r4 = new com.android.systemui.biometrics.BiometricDisplayListener$SensorType$SideFingerprint
            r4.<init>(r7)
            com.android.systemui.biometrics.SidefpsController$orientationListener$1 r5 = new com.android.systemui.biometrics.SidefpsController$orientationListener$1
            r5.<init>(r6)
            r0 = r8
            r1 = r10
            r2 = r13
            r0.<init>(r1, r2, r3, r4, r5)
            r6.orientationListener = r8
            com.android.systemui.biometrics.SidefpsController$overviewProxyListener$1 r7 = new com.android.systemui.biometrics.SidefpsController$overviewProxyListener$1
            r7.<init>()
            r6.overviewProxyListener = r7
            android.content.res.Resources r8 = r10.getResources()
            r10 = 17694721(0x10e0001, float:2.6081284E-38)
            int r8 = r8.getInteger(r10)
            long r10 = (long) r8
            r6.animationDuration = r10
            android.hardware.biometrics.SensorLocationInternal r8 = android.hardware.biometrics.SensorLocationInternal.DEFAULT
            r6.overlayOffsets = r8
            android.view.WindowManager$LayoutParams r8 = new android.view.WindowManager$LayoutParams
            r1 = -2
            r2 = -2
            r3 = 2010(0x7da, float:2.817E-42)
            r4 = 16777512(0x1000128, float:2.3510717E-38)
            r5 = -3
            r0 = r8
            r0.<init>(r1, r2, r3, r4, r5)
            java.lang.String r10 = "SidefpsController"
            r8.setTitle(r10)
            r10 = 0
            r8.setFitInsetsTypes(r10)
            r10 = 51
            r8.gravity = r10
            r10 = 3
            r8.layoutInDisplayCutoutMode = r10
            r10 = 536870912(0x20000000, float:1.0842022E-19)
            r8.privateFlags = r10
            r6.overlayViewParams = r8
            if (r9 != 0) goto L_0x008e
            goto L_0x0096
        L_0x008e:
            com.android.systemui.biometrics.SidefpsController$1 r8 = new com.android.systemui.biometrics.SidefpsController$1
            r8.<init>()
            r9.setSidefpsController(r8)
        L_0x0096:
            r12.addCallback(r7)
            return
        L_0x009a:
            java.lang.IllegalStateException r6 = new java.lang.IllegalStateException
            java.lang.String r7 = "no side fingerprint sensor"
            r6.<init>(r7)
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.biometrics.SidefpsController.<init>(android.content.Context, android.view.LayoutInflater, android.hardware.fingerprint.FingerprintManager, android.view.WindowManager, android.app.ActivityTaskManager, com.android.systemui.recents.OverviewProxyService, android.hardware.display.DisplayManager, com.android.systemui.util.concurrency.DelayableExecutor, android.os.Handler):void");
    }

    public final void updateOverlayParams(Display display, Rect rect) {
        boolean z;
        int i;
        int i2;
        Pair pair;
        boolean z2 = false;
        if (display.getRotation() == 0 || display.getRotation() == 2) {
            z = true;
        } else {
            z = false;
        }
        Rect bounds = this.windowManager.getMaximumWindowMetrics().getBounds();
        if (z) {
            i = bounds.width();
        } else {
            i = bounds.height();
        }
        if (z) {
            i2 = bounds.height();
        } else {
            i2 = bounds.width();
        }
        if (this.overlayOffsets.sensorLocationY != 0) {
            z2 = true;
        }
        if (z2) {
            int rotation = display.getRotation();
            if (rotation == 1) {
                pair = new Pair(Integer.valueOf(this.overlayOffsets.sensorLocationY), 0);
            } else if (rotation == 2) {
                pair = new Pair(0, Integer.valueOf((i2 - this.overlayOffsets.sensorLocationY) - rect.height()));
            } else if (rotation != 3) {
                pair = new Pair(Integer.valueOf(i), Integer.valueOf(this.overlayOffsets.sensorLocationY));
            } else {
                pair = new Pair(Integer.valueOf((i2 - this.overlayOffsets.sensorLocationY) - rect.width()), Integer.valueOf(rect.height() + i));
            }
        } else {
            int rotation2 = display.getRotation();
            if (rotation2 == 1) {
                pair = new Pair(0, Integer.valueOf((i - this.overlayOffsets.sensorLocationX) - rect.height()));
            } else if (rotation2 == 2) {
                pair = new Pair(Integer.valueOf((i - this.overlayOffsets.sensorLocationX) - rect.width()), Integer.valueOf(i2));
            } else if (rotation2 != 3) {
                pair = new Pair(Integer.valueOf(this.overlayOffsets.sensorLocationX), 0);
            } else {
                pair = new Pair(Integer.valueOf(i), Integer.valueOf(this.overlayOffsets.sensorLocationX - rect.height()));
            }
        }
        int intValue = ((Number) pair.component1()).intValue();
        int intValue2 = ((Number) pair.component2()).intValue();
        WindowManager.LayoutParams layoutParams = this.overlayViewParams;
        layoutParams.x = intValue;
        layoutParams.y = intValue2;
    }
}

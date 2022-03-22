package com.android.keyguard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.icu.text.NumberFormat;
import com.android.settingslib.Utils;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.util.ViewController;
import java.util.Locale;
import java.util.Objects;
/* loaded from: classes.dex */
public final class AnimatableClockController extends ViewController<AnimatableClockView> {
    public final BatteryController mBatteryController;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public final float mBurmeseLineSpacing;
    public final String mBurmeseNumerals;
    public final float mDefaultLineSpacing;
    public float mDozeAmount;
    public boolean mIsCharging;
    public boolean mIsDozing;
    public boolean mKeyguardShowing;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public Locale mLocale;
    public final StatusBarStateController mStatusBarStateController;
    public final AnonymousClass1 mBatteryCallback = new BatteryController.BatteryStateChangeCallback() { // from class: com.android.keyguard.AnimatableClockController.1
        /* JADX WARN: Code restructure failed: missing block: B:17:0x004c, code lost:
            if (r11 != false) goto L_0x0061;
         */
        /* JADX WARN: Code restructure failed: missing block: B:22:0x005f, code lost:
            if (r11 != false) goto L_0x0061;
         */
        /* JADX WARN: Code restructure failed: missing block: B:23:0x0061, code lost:
            r10 = r10 + 100;
         */
        /* JADX WARN: Code restructure failed: missing block: B:24:0x0063, code lost:
            r0.setTextStyle(r10, null, true, 500, r0.chargeAnimationDelay, r8);
         */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r8v0, types: [com.android.keyguard.AnimatableClockView$animateCharge$startAnimPhase2$1] */
        /* JADX WARN: Unknown variable types count: 1 */
        @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void onBatteryLevelChanged(int r10, boolean r11, boolean r12) {
            /*
                r9 = this;
                com.android.keyguard.AnimatableClockController r10 = com.android.keyguard.AnimatableClockController.this
                boolean r11 = r10.mKeyguardShowing
                if (r11 == 0) goto L_0x006e
                boolean r11 = r10.mIsCharging
                if (r11 != 0) goto L_0x006e
                if (r12 == 0) goto L_0x006e
                T extends android.view.View r11 = r10.mView
                r0 = r11
                com.android.keyguard.AnimatableClockView r0 = (com.android.keyguard.AnimatableClockView) r0
                com.android.systemui.plugins.statusbar.StatusBarStateController r10 = r10.mStatusBarStateController
                java.util.Objects.requireNonNull(r10)
                com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda4 r11 = new com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda4
                r11.<init>(r10)
                java.util.Objects.requireNonNull(r0)
                com.android.keyguard.TextAnimator r1 = r0.textAnimator
                if (r1 == 0) goto L_0x006e
                android.animation.ValueAnimator r1 = r1.animator
                boolean r1 = r1.isRunning()
                if (r1 == 0) goto L_0x002b
                goto L_0x006e
            L_0x002b:
                com.android.keyguard.AnimatableClockView$animateCharge$startAnimPhase2$1 r8 = new com.android.keyguard.AnimatableClockView$animateCharge$startAnimPhase2$1
                r8.<init>()
                com.android.systemui.plugins.statusbar.StatusBarStateController r10 = (com.android.systemui.plugins.statusbar.StatusBarStateController) r10
                boolean r10 = r10.isDozing()
                r11 = 1
                r1 = 0
                r2 = 100
                if (r10 == 0) goto L_0x004f
                android.content.res.Resources r10 = r0.getResources()
                android.content.res.Configuration r10 = r10.getConfiguration()
                int r10 = r10.fontWeightAdjustment
                if (r10 <= r2) goto L_0x0049
                goto L_0x004a
            L_0x0049:
                r11 = r1
            L_0x004a:
                int r10 = r0.lockScreenWeightInternal
                if (r11 == 0) goto L_0x0063
                goto L_0x0061
            L_0x004f:
                android.content.res.Resources r10 = r0.getResources()
                android.content.res.Configuration r10 = r10.getConfiguration()
                int r10 = r10.fontWeightAdjustment
                if (r10 <= r2) goto L_0x005c
                goto L_0x005d
            L_0x005c:
                r11 = r1
            L_0x005d:
                int r10 = r0.dozingWeightInternal
                if (r11 == 0) goto L_0x0063
            L_0x0061:
                int r10 = r10 + 100
            L_0x0063:
                r1 = r10
                r2 = 0
                r3 = 1
                r4 = 500(0x1f4, double:2.47E-321)
                int r10 = r0.chargeAnimationDelay
                long r6 = (long) r10
                r0.setTextStyle(r1, r2, r3, r4, r6, r8)
            L_0x006e:
                com.android.keyguard.AnimatableClockController r9 = com.android.keyguard.AnimatableClockController.this
                r9.mIsCharging = r12
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.keyguard.AnimatableClockController.AnonymousClass1.onBatteryLevelChanged(int, boolean, boolean):void");
        }
    };
    public final AnonymousClass2 mLocaleBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.keyguard.AnimatableClockController.2
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            AnimatableClockController.this.updateLocale();
        }
    };
    public final AnonymousClass3 mStatusBarStateListener = new StatusBarStateController.StateListener() { // from class: com.android.keyguard.AnimatableClockController.3
        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public final void onDozeAmountChanged(float f, float f2) {
            boolean z;
            AnimatableClockController animatableClockController = AnimatableClockController.this;
            float f3 = animatableClockController.mDozeAmount;
            boolean z2 = false;
            if ((f3 == 0.0f && f == 1.0f) || (f3 == 1.0f && f == 0.0f)) {
                z = true;
            } else {
                z = false;
            }
            if (f > f3) {
                z2 = true;
            }
            animatableClockController.mDozeAmount = f;
            if (animatableClockController.mIsDozing != z2) {
                animatableClockController.mIsDozing = z2;
                ((AnimatableClockView) animatableClockController.mView).animateDoze(z2, !z);
            }
        }
    };
    public final AnonymousClass4 mKeyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.keyguard.AnimatableClockController.4
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onKeyguardVisibilityChanged(boolean z) {
            AnimatableClockController animatableClockController = AnimatableClockController.this;
            animatableClockController.mKeyguardShowing = z;
            if (!z) {
                Objects.requireNonNull(animatableClockController);
                ((AnimatableClockView) animatableClockController.mView).animateDoze(animatableClockController.mIsDozing, false);
            }
        }
    };

    public final void animateFoldAppear() {
        AnimatableClockView animatableClockView = (AnimatableClockView) this.mView;
        Objects.requireNonNull(animatableClockView);
        if (animatableClockView.textAnimator != null) {
            animatableClockView.setTextStyle(animatableClockView.lockScreenWeightInternal, Integer.valueOf(animatableClockView.lockScreenColor), false, 0L, 0L, null);
            animatableClockView.setTextStyle(animatableClockView.dozingWeightInternal, Integer.valueOf(animatableClockView.dozingColor), true, Interpolators.EMPHASIZED_DECELERATE, 600L, 0L, null);
        }
    }

    @Override // com.android.systemui.util.ViewController
    public final void onInit() {
        this.mIsDozing = this.mStatusBarStateController.isDozing();
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewDetached() {
        this.mBroadcastDispatcher.unregisterReceiver(this.mLocaleBroadcastReceiver);
        this.mKeyguardUpdateMonitor.removeCallback(this.mKeyguardUpdateMonitorCallback);
        this.mBatteryController.removeCallback(this.mBatteryCallback);
        this.mStatusBarStateController.removeCallback(this.mStatusBarStateListener);
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.keyguard.AnimatableClockController$1] */
    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.keyguard.AnimatableClockController$2] */
    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.keyguard.AnimatableClockController$3] */
    /* JADX WARN: Type inference failed for: r0v3, types: [com.android.keyguard.AnimatableClockController$4] */
    public AnimatableClockController(AnimatableClockView animatableClockView, StatusBarStateController statusBarStateController, BroadcastDispatcher broadcastDispatcher, BatteryController batteryController, KeyguardUpdateMonitor keyguardUpdateMonitor, Resources resources) {
        super(animatableClockView);
        NumberFormat instance = NumberFormat.getInstance(Locale.forLanguageTag("my"));
        this.mStatusBarStateController = statusBarStateController;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mBatteryController = batteryController;
        this.mBurmeseNumerals = instance.format(1234567890L);
        this.mBurmeseLineSpacing = resources.getFloat(2131165842);
        this.mDefaultLineSpacing = resources.getFloat(2131165841);
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewAttached() {
        boolean z;
        updateLocale();
        this.mBroadcastDispatcher.registerReceiver(this.mLocaleBroadcastReceiver, new IntentFilter("android.intent.action.LOCALE_CHANGED"));
        this.mDozeAmount = this.mStatusBarStateController.getDozeAmount();
        if (this.mStatusBarStateController.isDozing() || this.mDozeAmount != 0.0f) {
            z = true;
        } else {
            z = false;
        }
        this.mIsDozing = z;
        this.mBatteryController.addCallback(this.mBatteryCallback);
        this.mKeyguardUpdateMonitor.registerCallback(this.mKeyguardUpdateMonitorCallback);
        this.mStatusBarStateController.addCallback(this.mStatusBarStateListener);
        ((AnimatableClockView) this.mView).refreshTime();
        int colorAttrDefaultColor = Utils.getColorAttrDefaultColor(getContext(), 2130970104);
        AnimatableClockView animatableClockView = (AnimatableClockView) this.mView;
        Objects.requireNonNull(animatableClockView);
        animatableClockView.dozingColor = -1;
        animatableClockView.lockScreenColor = colorAttrDefaultColor;
        ((AnimatableClockView) this.mView).animateDoze(this.mIsDozing, false);
        ((AnimatableClockView) this.mView).animateDoze(this.mIsDozing, false);
    }

    public final void updateLocale() {
        Locale locale = Locale.getDefault();
        if (!Objects.equals(locale, this.mLocale)) {
            this.mLocale = locale;
            if (NumberFormat.getInstance(locale).format(1234567890L).equals(this.mBurmeseNumerals)) {
                AnimatableClockView animatableClockView = (AnimatableClockView) this.mView;
                float f = this.mBurmeseLineSpacing;
                Objects.requireNonNull(animatableClockView);
                animatableClockView.setLineSpacing(0.0f, f);
            } else {
                AnimatableClockView animatableClockView2 = (AnimatableClockView) this.mView;
                float f2 = this.mDefaultLineSpacing;
                Objects.requireNonNull(animatableClockView2);
                animatableClockView2.setLineSpacing(0.0f, f2);
            }
            ((AnimatableClockView) this.mView).refreshFormat();
        }
    }

    public boolean isDozing() {
        return this.mIsDozing;
    }
}

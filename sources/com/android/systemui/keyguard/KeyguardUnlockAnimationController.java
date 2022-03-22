package com.android.systemui.keyguard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Handler;
import android.os.RemoteException;
import android.view.IRemoteAnimationFinishedCallback;
import android.view.RemoteAnimationTarget;
import android.view.SyncRtSurfaceTransactionApplier;
import android.view.View;
import android.view.animation.PathInterpolator;
import com.android.keyguard.KeyguardViewController;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.smartspace.ILauncherUnlockAnimationController;
import com.android.systemui.shared.system.smartspace.ISysuiUnlockAnimationController$Stub;
import com.android.systemui.shared.system.smartspace.SmartspaceState;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.Lazy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: KeyguardUnlockAnimationController.kt */
/* loaded from: classes.dex */
public final class KeyguardUnlockAnimationController extends ISysuiUnlockAnimationController$Stub implements KeyguardStateController.Callback {
    public boolean attemptedSmartSpaceTransitionForThisSwipe;
    public final Lazy<BiometricUnlockController> biometricUnlockControllerLazy;
    public final FeatureFlags featureFlags;
    public final KeyguardStateController keyguardStateController;
    public final KeyguardViewController keyguardViewController;
    public final Lazy<KeyguardViewMediator> keyguardViewMediator;
    public SmartspaceState launcherSmartspaceState;
    public ILauncherUnlockAnimationController launcherUnlockController;
    public View lockscreenSmartspace;
    public boolean playingCannedUnlockAnimation;
    public float roundedCornerRadius;
    public float smartspaceUnlockProgress;
    public final ValueAnimator surfaceBehindEntryAnimator;
    public SyncRtSurfaceTransactionApplier.SurfaceParams surfaceBehindParams;
    public long surfaceBehindRemoteAnimationStartTime;
    public RemoteAnimationTarget surfaceBehindRemoteAnimationTarget;
    public SyncRtSurfaceTransactionApplier surfaceTransactionApplier;
    public boolean unlockingToLauncherWithInWindowAnimations;
    public boolean unlockingWithSmartspaceTransition;
    public final ArrayList<KeyguardUnlockAnimationListener> listeners = new ArrayList<>();
    public float surfaceBehindAlpha = 1.0f;
    public ValueAnimator surfaceBehindAlphaAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
    public ValueAnimator smartspaceAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
    public final Matrix surfaceBehindMatrix = new Matrix();
    public final Rect smartspaceOriginBounds = new Rect();
    public final Rect smartspaceDestBounds = new Rect();
    public final Handler handler = new Handler();

    /* compiled from: KeyguardUnlockAnimationController.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public static boolean isNexusLauncherUnderneath() {
            ComponentName componentName;
            String className;
            ActivityManager.RunningTaskInfo runningTask = ActivityManagerWrapper.sInstance.getRunningTask();
            if (runningTask == null || (componentName = runningTask.topActivity) == null || (className = componentName.getClassName()) == null) {
                return false;
            }
            return className.equals("com.google.android.apps.nexuslauncher.NexusLauncherActivity");
        }
    }

    /* compiled from: KeyguardUnlockAnimationController.kt */
    /* loaded from: classes.dex */
    public interface KeyguardUnlockAnimationListener {
        default void onSmartspaceSharedElementTransitionStarted() {
        }

        default void onUnlockAnimationFinished() {
        }

        default void onUnlockAnimationStarted(boolean z, boolean z2) {
        }
    }

    public static /* synthetic */ void getSurfaceBehindEntryAnimator$annotations() {
    }

    public static /* synthetic */ void getSurfaceTransactionApplier$annotations() {
    }

    public final void finishKeyguardExitRemoteAnimationIfReachThreshold() {
        if (KeyguardService.sEnableRemoteKeyguardGoingAwayAnimation && this.keyguardViewController.isShowing()) {
            KeyguardViewMediator keyguardViewMediator = this.keyguardViewMediator.get();
            Objects.requireNonNull(keyguardViewMediator);
            if (keyguardViewMediator.mSurfaceBehindRemoteAnimationRequested && this.keyguardViewMediator.get().isAnimatingBetweenKeyguardAndSurfaceBehindOrWillBe()) {
                float dismissAmount = this.keyguardStateController.getDismissAmount();
                if (dismissAmount >= 1.0f || (this.keyguardStateController.isDismissingFromSwipe() && !this.keyguardStateController.isFlingingToDismissKeyguardDuringSwipeGesture() && dismissAmount >= 0.4f)) {
                    setSurfaceBehindAppearAmount(1.0f);
                    this.keyguardViewMediator.get().onKeyguardExitRemoteAnimationFinished(false);
                }
            }
        }
    }

    public final void notifyStartSurfaceBehindRemoteAnimation(RemoteAnimationTarget remoteAnimationTarget, long j, boolean z) {
        boolean z2;
        int i;
        if (this.surfaceTransactionApplier == null) {
            this.surfaceTransactionApplier = new SyncRtSurfaceTransactionApplier(this.keyguardViewController.getViewRootImpl().getView());
        }
        this.surfaceBehindParams = null;
        this.surfaceBehindRemoteAnimationTarget = remoteAnimationTarget;
        this.surfaceBehindRemoteAnimationStartTime = j;
        if (!z) {
            this.playingCannedUnlockAnimation = true;
            if (!Companion.isNexusLauncherUnderneath() || this.launcherUnlockController == null) {
                z2 = false;
            } else {
                z2 = true;
            }
            if (z2) {
                this.unlockingToLauncherWithInWindowAnimations = true;
                if (prepareForSmartspaceTransition()) {
                    this.smartspaceAnimator.start();
                    Iterator<KeyguardUnlockAnimationListener> it = this.listeners.iterator();
                    while (it.hasNext()) {
                        it.next().onSmartspaceSharedElementTransitionStarted();
                    }
                }
                ILauncherUnlockAnimationController iLauncherUnlockAnimationController = this.launcherUnlockController;
                if (iLauncherUnlockAnimationController != null) {
                    boolean z3 = this.unlockingWithSmartspaceTransition;
                    BcSmartspaceDataPlugin.SmartspaceView smartspaceView = (BcSmartspaceDataPlugin.SmartspaceView) this.lockscreenSmartspace;
                    if (smartspaceView == null) {
                        i = -1;
                    } else {
                        i = smartspaceView.getSelectedPage();
                    }
                    iLauncherUnlockAnimationController.prepareForUnlock(z3, i);
                }
                ILauncherUnlockAnimationController iLauncherUnlockAnimationController2 = this.launcherUnlockController;
                if (iLauncherUnlockAnimationController2 != null) {
                    iLauncherUnlockAnimationController2.playUnlockAnimation();
                }
                if (!this.unlockingWithSmartspaceTransition) {
                    this.handler.postDelayed(new Runnable() { // from class: com.android.systemui.keyguard.KeyguardUnlockAnimationController$unlockToLauncherWithInWindowAnimations$2
                        @Override // java.lang.Runnable
                        public final void run() {
                            KeyguardUnlockAnimationController.this.keyguardViewMediator.get().onKeyguardExitRemoteAnimationFinished(false);
                        }
                    }, 200L);
                }
                setSurfaceBehindAppearAmount(1.0f);
            } else if (!this.biometricUnlockControllerLazy.get().isWakeAndUnlock()) {
                this.surfaceBehindEntryAnimator.start();
            } else {
                setSurfaceBehindAppearAmount(1.0f);
                this.keyguardViewMediator.get().onKeyguardExitRemoteAnimationFinished(false);
            }
            if (this.biometricUnlockControllerLazy.get().isWakeAndUnlock()) {
                this.keyguardViewController.hide(this.surfaceBehindRemoteAnimationStartTime, 350L);
            }
        }
        Iterator<KeyguardUnlockAnimationListener> it2 = this.listeners.iterator();
        while (it2.hasNext()) {
            it2.next().onUnlockAnimationStarted(this.playingCannedUnlockAnimation, this.biometricUnlockControllerLazy.get().isWakeAndUnlock());
        }
        finishKeyguardExitRemoteAnimationIfReachThreshold();
    }

    /* JADX WARN: Code restructure failed: missing block: B:80:0x0143, code lost:
        if (r2 != false) goto L_0x0145;
     */
    @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onKeyguardDismissAmountChanged() {
        /*
            Method dump skipped, instructions count: 357
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.keyguard.KeyguardUnlockAnimationController.onKeyguardDismissAmountChanged():void");
    }

    public final boolean prepareForSmartspaceTransition() {
        SmartspaceState smartspaceState;
        boolean z;
        boolean z2;
        int i;
        if (!this.featureFlags.isEnabled(Flags.SMARTSPACE_SHARED_ELEMENT_TRANSITION_ENABLED) || this.launcherUnlockController == null || this.lockscreenSmartspace == null || (smartspaceState = this.launcherSmartspaceState) == null) {
            return false;
        }
        if (smartspaceState.visibleOnScreen) {
            z = true;
        } else {
            z = false;
        }
        if (!z || !Companion.isNexusLauncherUnderneath() || this.biometricUnlockControllerLazy.get().isWakeAndUnlock()) {
            return false;
        }
        if (!this.keyguardStateController.canDismissLockScreen()) {
            BiometricUnlockController biometricUnlockController = this.biometricUnlockControllerLazy.get();
            Objects.requireNonNull(biometricUnlockController);
            if (biometricUnlockController.isWakeAndUnlock() || (i = biometricUnlockController.mMode) == 5 || i == 7) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (!z2) {
                return false;
            }
        }
        this.unlockingWithSmartspaceTransition = true;
        this.smartspaceDestBounds.setEmpty();
        View view = this.lockscreenSmartspace;
        Intrinsics.checkNotNull(view);
        view.setTranslationX(0.0f);
        view.setTranslationY(0.0f);
        view.getBoundsOnScreen(this.smartspaceOriginBounds);
        Rect rect = this.smartspaceDestBounds;
        SmartspaceState smartspaceState2 = this.launcherSmartspaceState;
        Intrinsics.checkNotNull(smartspaceState2);
        rect.set(smartspaceState2.boundsOnScreen);
        View view2 = this.lockscreenSmartspace;
        Intrinsics.checkNotNull(view2);
        View view3 = this.lockscreenSmartspace;
        Intrinsics.checkNotNull(view3);
        rect.offset(-view2.getPaddingLeft(), -view3.getPaddingTop());
        return true;
    }

    public final void setSmartspaceProgressToDestinationBounds(float f) {
        if (!this.smartspaceDestBounds.isEmpty()) {
            float min = Math.min(1.0f, f);
            Rect rect = this.smartspaceDestBounds;
            int i = rect.left;
            Rect rect2 = this.smartspaceOriginBounds;
            float f2 = (i - rect2.left) * min;
            float f3 = (rect.top - rect2.top) * min;
            Rect rect3 = new Rect();
            View view = this.lockscreenSmartspace;
            Intrinsics.checkNotNull(view);
            view.getBoundsOnScreen(rect3);
            Rect rect4 = this.smartspaceOriginBounds;
            float f4 = (rect4.left + f2) - rect3.left;
            float f5 = (rect4.top + f3) - rect3.top;
            View view2 = this.lockscreenSmartspace;
            Intrinsics.checkNotNull(view2);
            view2.setTranslationX(view2.getTranslationX() + f4);
            view2.setTranslationY(view2.getTranslationY() + f5);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x0022  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void setSurfaceBehindAppearAmount(float r10) {
        /*
            r9 = this;
            android.view.RemoteAnimationTarget r0 = r9.surfaceBehindRemoteAnimationTarget
            if (r0 != 0) goto L_0x0005
            return
        L_0x0005:
            boolean r1 = r9.unlockingToLauncherWithInWindowAnimations
            r2 = 1065353216(0x3f800000, float:1.0)
            r3 = 0
            r4 = 1
            if (r1 == 0) goto L_0x0051
            android.view.SyncRtSurfaceTransactionApplier$SurfaceParams r0 = r9.surfaceBehindParams
            if (r0 != 0) goto L_0x0012
            goto L_0x001f
        L_0x0012:
            float r0 = r0.alpha
            int r0 = (r0 > r2 ? 1 : (r0 == r2 ? 0 : -1))
            if (r0 >= 0) goto L_0x001a
            r0 = r4
            goto L_0x001b
        L_0x001a:
            r0 = r3
        L_0x001b:
            if (r0 != 0) goto L_0x001f
            r0 = r4
            goto L_0x0020
        L_0x001f:
            r0 = r3
        L_0x0020:
            if (r0 != 0) goto L_0x0044
            android.view.SyncRtSurfaceTransactionApplier$SurfaceParams$Builder r0 = new android.view.SyncRtSurfaceTransactionApplier$SurfaceParams$Builder
            android.view.RemoteAnimationTarget r1 = r9.surfaceBehindRemoteAnimationTarget
            kotlin.jvm.internal.Intrinsics.checkNotNull(r1)
            android.view.SurfaceControl r1 = r1.leash
            r0.<init>(r1)
            android.view.SyncRtSurfaceTransactionApplier$SurfaceParams$Builder r0 = r0.withAlpha(r2)
            android.view.SyncRtSurfaceTransactionApplier$SurfaceParams r0 = r0.build()
            android.view.SyncRtSurfaceTransactionApplier r1 = r9.surfaceTransactionApplier
            kotlin.jvm.internal.Intrinsics.checkNotNull(r1)
            android.view.SyncRtSurfaceTransactionApplier$SurfaceParams[] r2 = new android.view.SyncRtSurfaceTransactionApplier.SurfaceParams[r4]
            r2[r3] = r0
            r1.scheduleApply(r2)
            r9.surfaceBehindParams = r0
        L_0x0044:
            boolean r0 = r9.playingCannedUnlockAnimation
            if (r0 != 0) goto L_0x00c1
            com.android.systemui.shared.system.smartspace.ILauncherUnlockAnimationController r9 = r9.launcherUnlockController
            if (r9 != 0) goto L_0x004d
            goto L_0x00c1
        L_0x004d:
            r9.setUnlockAmount(r10)
            goto L_0x00c1
        L_0x0051:
            android.graphics.Rect r0 = r0.screenSpaceBounds
            int r0 = r0.height()
            r1 = 1064514355(0x3f733333, float:0.95)
            r5 = 1028443344(0x3d4cccd0, float:0.050000012)
            r6 = 0
            float r7 = kotlinx.atomicfu.AtomicFU.clamp(r10, r6, r2)
            float r7 = r7 * r5
            float r7 = r7 + r1
            android.graphics.Matrix r1 = r9.surfaceBehindMatrix
            android.view.RemoteAnimationTarget r5 = r9.surfaceBehindRemoteAnimationTarget
            kotlin.jvm.internal.Intrinsics.checkNotNull(r5)
            android.graphics.Rect r5 = r5.screenSpaceBounds
            int r5 = r5.width()
            float r5 = (float) r5
            r8 = 1073741824(0x40000000, float:2.0)
            float r5 = r5 / r8
            float r0 = (float) r0
            r8 = 1059648963(0x3f28f5c3, float:0.66)
            float r8 = r8 * r0
            r1.setScale(r7, r7, r5, r8)
            android.graphics.Matrix r1 = r9.surfaceBehindMatrix
            r5 = 1028443341(0x3d4ccccd, float:0.05)
            float r0 = r0 * r5
            float r2 = r2 - r10
            float r2 = r2 * r0
            r1.postTranslate(r6, r2)
            com.android.systemui.statusbar.policy.KeyguardStateController r0 = r9.keyguardStateController
            boolean r0 = r0.isSnappingKeyguardBackAfterSwipe()
            if (r0 == 0) goto L_0x0091
            goto L_0x0093
        L_0x0091:
            float r10 = r9.surfaceBehindAlpha
        L_0x0093:
            android.view.SyncRtSurfaceTransactionApplier$SurfaceParams$Builder r0 = new android.view.SyncRtSurfaceTransactionApplier$SurfaceParams$Builder
            android.view.RemoteAnimationTarget r1 = r9.surfaceBehindRemoteAnimationTarget
            kotlin.jvm.internal.Intrinsics.checkNotNull(r1)
            android.view.SurfaceControl r1 = r1.leash
            r0.<init>(r1)
            android.graphics.Matrix r1 = r9.surfaceBehindMatrix
            android.view.SyncRtSurfaceTransactionApplier$SurfaceParams$Builder r0 = r0.withMatrix(r1)
            float r1 = r9.roundedCornerRadius
            android.view.SyncRtSurfaceTransactionApplier$SurfaceParams$Builder r0 = r0.withCornerRadius(r1)
            android.view.SyncRtSurfaceTransactionApplier$SurfaceParams$Builder r10 = r0.withAlpha(r10)
            android.view.SyncRtSurfaceTransactionApplier$SurfaceParams r10 = r10.build()
            android.view.SyncRtSurfaceTransactionApplier r0 = r9.surfaceTransactionApplier
            kotlin.jvm.internal.Intrinsics.checkNotNull(r0)
            android.view.SyncRtSurfaceTransactionApplier$SurfaceParams[] r1 = new android.view.SyncRtSurfaceTransactionApplier.SurfaceParams[r4]
            r1[r3] = r10
            r0.scheduleApply(r1)
            r9.surfaceBehindParams = r10
        L_0x00c1:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.keyguard.KeyguardUnlockAnimationController.setSurfaceBehindAppearAmount(float):void");
    }

    public final void updateSurfaceBehindAppearAmount() {
        if (this.surfaceBehindRemoteAnimationTarget == null || this.playingCannedUnlockAnimation) {
            return;
        }
        if (this.keyguardStateController.isFlingingToDismissKeyguard()) {
            setSurfaceBehindAppearAmount(this.keyguardStateController.getDismissAmount());
        } else if (this.keyguardStateController.isDismissingFromSwipe() || this.keyguardStateController.isSnappingKeyguardBackAfterSwipe()) {
            setSurfaceBehindAppearAmount((this.keyguardStateController.getDismissAmount() - 0.25f) / 0.15f);
        }
    }

    public KeyguardUnlockAnimationController(Context context, KeyguardStateController keyguardStateController, Lazy<KeyguardViewMediator> lazy, KeyguardViewController keyguardViewController, FeatureFlags featureFlags, Lazy<BiometricUnlockController> lazy2) {
        this.keyguardStateController = keyguardStateController;
        this.keyguardViewMediator = lazy;
        this.keyguardViewController = keyguardViewController;
        this.featureFlags = featureFlags;
        this.biometricUnlockControllerLazy = lazy2;
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.surfaceBehindEntryAnimator = ofFloat;
        ValueAnimator valueAnimator = this.surfaceBehindAlphaAnimator;
        valueAnimator.setDuration(150L);
        PathInterpolator pathInterpolator = Interpolators.TOUCH_RESPONSE;
        valueAnimator.setInterpolator(pathInterpolator);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.keyguard.KeyguardUnlockAnimationController$1$1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                KeyguardUnlockAnimationController keyguardUnlockAnimationController = KeyguardUnlockAnimationController.this;
                Object animatedValue = valueAnimator2.getAnimatedValue();
                Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                keyguardUnlockAnimationController.surfaceBehindAlpha = ((Float) animatedValue).floatValue();
                KeyguardUnlockAnimationController.this.updateSurfaceBehindAppearAmount();
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.keyguard.KeyguardUnlockAnimationController$1$2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                boolean z;
                KeyguardUnlockAnimationController keyguardUnlockAnimationController = KeyguardUnlockAnimationController.this;
                if (keyguardUnlockAnimationController.surfaceBehindAlpha == 0.0f) {
                    z = true;
                } else {
                    z = false;
                }
                if (z) {
                    KeyguardViewMediator keyguardViewMediator = keyguardUnlockAnimationController.keyguardViewMediator.get();
                    Objects.requireNonNull(keyguardViewMediator);
                    if (keyguardViewMediator.mSurfaceBehindRemoteAnimationRunning) {
                        keyguardViewMediator.mSurfaceBehindRemoteAnimationRunning = false;
                        IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback = keyguardViewMediator.mSurfaceBehindRemoteAnimationFinishedCallback;
                        if (iRemoteAnimationFinishedCallback != null) {
                            try {
                                iRemoteAnimationFinishedCallback.onAnimationFinished();
                                keyguardViewMediator.mSurfaceBehindRemoteAnimationFinishedCallback = null;
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
        ofFloat.setDuration(200L);
        ofFloat.setStartDelay(75L);
        ofFloat.setInterpolator(pathInterpolator);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.keyguard.KeyguardUnlockAnimationController$2$1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                KeyguardUnlockAnimationController keyguardUnlockAnimationController = KeyguardUnlockAnimationController.this;
                Object animatedValue = valueAnimator2.getAnimatedValue();
                Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                keyguardUnlockAnimationController.surfaceBehindAlpha = ((Float) animatedValue).floatValue();
                KeyguardUnlockAnimationController keyguardUnlockAnimationController2 = KeyguardUnlockAnimationController.this;
                Object animatedValue2 = valueAnimator2.getAnimatedValue();
                Objects.requireNonNull(animatedValue2, "null cannot be cast to non-null type kotlin.Float");
                keyguardUnlockAnimationController2.setSurfaceBehindAppearAmount(((Float) animatedValue2).floatValue());
            }
        });
        ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.keyguard.KeyguardUnlockAnimationController$2$2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                KeyguardUnlockAnimationController keyguardUnlockAnimationController = KeyguardUnlockAnimationController.this;
                Objects.requireNonNull(keyguardUnlockAnimationController);
                keyguardUnlockAnimationController.playingCannedUnlockAnimation = false;
                KeyguardUnlockAnimationController.this.keyguardViewMediator.get().onKeyguardExitRemoteAnimationFinished(false);
            }
        });
        ValueAnimator valueAnimator2 = this.smartspaceAnimator;
        valueAnimator2.setDuration(200L);
        valueAnimator2.setInterpolator(pathInterpolator);
        valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.keyguard.KeyguardUnlockAnimationController$3$1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator3) {
                KeyguardUnlockAnimationController keyguardUnlockAnimationController = KeyguardUnlockAnimationController.this;
                Object animatedValue = valueAnimator3.getAnimatedValue();
                Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                keyguardUnlockAnimationController.smartspaceUnlockProgress = ((Float) animatedValue).floatValue();
            }
        });
        valueAnimator2.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.keyguard.KeyguardUnlockAnimationController$3$2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                ILauncherUnlockAnimationController iLauncherUnlockAnimationController = KeyguardUnlockAnimationController.this.launcherUnlockController;
                if (iLauncherUnlockAnimationController != null) {
                    iLauncherUnlockAnimationController.setSmartspaceVisibility(0);
                }
                KeyguardUnlockAnimationController.this.keyguardViewMediator.get().onKeyguardExitRemoteAnimationFinished(false);
            }
        });
        keyguardStateController.addCallback(this);
        this.roundedCornerRadius = context.getResources().getDimensionPixelSize(17105512);
    }
}

package com.android.systemui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Point;
import android.os.RemoteException;
import android.util.Slog;
import android.view.IRemoteAnimationFinishedCallback;
import android.view.RemoteAnimationTarget;
import android.view.SyncRtSurfaceTransactionApplier;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.keyguard.KeyguardViewMediator;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ScreenDecorations$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ CoreStartable f$0;
    public final /* synthetic */ Object f$1;
    public final /* synthetic */ Object f$2;

    public /* synthetic */ ScreenDecorations$$ExternalSyntheticLambda0(CoreStartable coreStartable, Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = coreStartable;
        this.f$1 = obj;
        this.f$2 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                ScreenDecorations screenDecorations = (ScreenDecorations) this.f$0;
                String str = (String) this.f$1;
                String str2 = (String) this.f$2;
                boolean z = ScreenDecorations.DEBUG_DISABLE_SCREEN_DECORATIONS;
                Objects.requireNonNull(screenDecorations);
                if (screenDecorations.mOverlays != null && "sysui_rounded_size".equals(str)) {
                    Point point = screenDecorations.mRoundedDefault;
                    Point point2 = screenDecorations.mRoundedDefaultTop;
                    Point point3 = screenDecorations.mRoundedDefaultBottom;
                    if (str2 != null) {
                        try {
                            int parseInt = (int) (Integer.parseInt(str2) * screenDecorations.mDensity);
                            point = new Point(parseInt, parseInt);
                        } catch (Exception unused) {
                        }
                    }
                    screenDecorations.updateRoundedCornerSize(point, point2, point3);
                    return;
                }
                return;
            default:
                final KeyguardViewMediator keyguardViewMediator = (KeyguardViewMediator) this.f$0;
                final IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback = (IRemoteAnimationFinishedCallback) this.f$1;
                RemoteAnimationTarget[] remoteAnimationTargetArr = (RemoteAnimationTarget[]) this.f$2;
                boolean z2 = KeyguardViewMediator.DEBUG;
                if (iRemoteAnimationFinishedCallback == null) {
                    keyguardViewMediator.mInteractionJankMonitor.end(29);
                    return;
                }
                Objects.requireNonNull(keyguardViewMediator);
                final SyncRtSurfaceTransactionApplier syncRtSurfaceTransactionApplier = new SyncRtSurfaceTransactionApplier(keyguardViewMediator.mKeyguardViewControllerLazy.get().getViewRootImpl().getView());
                final RemoteAnimationTarget remoteAnimationTarget = remoteAnimationTargetArr[0];
                ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
                ofFloat.setDuration(400L);
                ofFloat.setInterpolator(Interpolators.LINEAR);
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.keyguard.KeyguardViewMediator$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        RemoteAnimationTarget remoteAnimationTarget2 = remoteAnimationTarget;
                        SyncRtSurfaceTransactionApplier syncRtSurfaceTransactionApplier2 = syncRtSurfaceTransactionApplier;
                        boolean z3 = KeyguardViewMediator.DEBUG;
                        syncRtSurfaceTransactionApplier2.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(remoteAnimationTarget2.leash).withAlpha(valueAnimator.getAnimatedFraction()).build()});
                    }
                });
                ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.keyguard.KeyguardViewMediator.12
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationCancel(Animator animator) {
                        KeyguardViewMediator keyguardViewMediator2;
                        try {
                            try {
                                iRemoteAnimationFinishedCallback.onAnimationFinished();
                                keyguardViewMediator2 = keyguardViewMediator;
                            } catch (RemoteException unused2) {
                                Slog.e("KeyguardViewMediator", "RemoteException");
                                keyguardViewMediator2 = keyguardViewMediator;
                            }
                            keyguardViewMediator2.mInteractionJankMonitor.cancel(29);
                        } catch (Throwable th) {
                            keyguardViewMediator.mInteractionJankMonitor.cancel(29);
                            throw th;
                        }
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationEnd(Animator animator) {
                        KeyguardViewMediator keyguardViewMediator2;
                        try {
                            try {
                                iRemoteAnimationFinishedCallback.onAnimationFinished();
                                keyguardViewMediator2 = keyguardViewMediator;
                            } catch (RemoteException unused2) {
                                Slog.e("KeyguardViewMediator", "RemoteException");
                                keyguardViewMediator2 = keyguardViewMediator;
                            }
                            keyguardViewMediator2.mInteractionJankMonitor.end(29);
                        } catch (Throwable th) {
                            keyguardViewMediator.mInteractionJankMonitor.end(29);
                            throw th;
                        }
                    }
                });
                ofFloat.start();
                return;
        }
    }
}

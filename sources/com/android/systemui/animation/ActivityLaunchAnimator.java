package com.android.systemui.animation;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.view.IRemoteAnimationFinishedCallback;
import android.view.IRemoteAnimationRunner;
import android.view.RemoteAnimationAdapter;
import android.view.RemoteAnimationTarget;
import android.view.SyncRtSurfaceTransactionApplier;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.PathInterpolator;
import androidx.leanback.R$drawable;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.policy.ScreenDecorationsUtils;
import com.android.systemui.SystemUIApplication$$ExternalSyntheticLambda1;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.LaunchAnimator;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarLaunchAnimatorController;
import com.android.wm.shell.startingsurface.SplashscreenContentDrawer;
import java.util.LinkedHashSet;
import java.util.Objects;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ActivityLaunchAnimator.kt */
/* loaded from: classes.dex */
public final class ActivityLaunchAnimator {
    public static final LaunchAnimator.Interpolators INTERPOLATORS;
    public Callback callback;
    public final LaunchAnimator dialogToAppAnimator;
    public final LaunchAnimator launchAnimator;
    public final LinkedHashSet<Listener> listeners = new LinkedHashSet<>();
    public static final LaunchAnimator.Timings TIMINGS = new LaunchAnimator.Timings(500, 0, 150, 150, 183);
    public static final LaunchAnimator.Timings DIALOG_TIMINGS = new LaunchAnimator.Timings(500, 0, 200, 200, 183);
    public static final long ANIMATION_DELAY_NAV_FADE_IN = 234;
    public static final PathInterpolator NAV_FADE_IN_INTERPOLATOR = Interpolators.STANDARD_DECELERATE;
    public static final PathInterpolator NAV_FADE_OUT_INTERPOLATOR = new PathInterpolator(0.2f, 0.0f, 1.0f, 1.0f);

    /* compiled from: ActivityLaunchAnimator.kt */
    /* loaded from: classes.dex */
    public interface Callback {
    }

    /* compiled from: ActivityLaunchAnimator.kt */
    /* loaded from: classes.dex */
    public interface Listener {
        default void onLaunchAnimationEnd() {
        }

        default void onLaunchAnimationProgress(float f) {
        }

        default void onLaunchAnimationStart() {
        }
    }

    /* compiled from: ActivityLaunchAnimator.kt */
    /* loaded from: classes.dex */
    public interface PendingIntentStarter {
        int startPendingIntent(RemoteAnimationAdapter remoteAnimationAdapter) throws PendingIntent.CanceledException;
    }

    /* compiled from: ActivityLaunchAnimator.kt */
    @VisibleForTesting
    /* loaded from: classes.dex */
    public final class Runner extends IRemoteAnimationRunner.Stub {
        public static final /* synthetic */ int $r8$clinit = 0;
        public LaunchAnimator$startAnimation$3 animation;
        public boolean cancelled;
        public final Context context;
        public final Controller controller;
        public final Matrix invertMatrix;
        public final ViewGroup launchContainer;
        public final Matrix matrix;
        public ActivityLaunchAnimator$Runner$onTimeout$1 onTimeout;
        public boolean timedOut;
        public final SyncRtSurfaceTransactionApplier transactionApplier;
        public final View transactionApplierView;
        public Rect windowCrop;
        public RectF windowCropF;

        /* JADX WARN: Type inference failed for: r1v9, types: [com.android.systemui.animation.ActivityLaunchAnimator$Runner$onTimeout$1] */
        public Runner(Controller controller) {
            this.controller = controller;
            ViewGroup launchContainer = controller.getLaunchContainer();
            this.launchContainer = launchContainer;
            this.context = launchContainer.getContext();
            View openingWindowSyncView = controller.getOpeningWindowSyncView();
            openingWindowSyncView = openingWindowSyncView == null ? controller.getLaunchContainer() : openingWindowSyncView;
            this.transactionApplierView = openingWindowSyncView;
            this.transactionApplier = new SyncRtSurfaceTransactionApplier(openingWindowSyncView);
            this.matrix = new Matrix();
            this.invertMatrix = new Matrix();
            this.windowCrop = new Rect();
            this.windowCropF = new RectF();
            this.onTimeout = new Runnable() { // from class: com.android.systemui.animation.ActivityLaunchAnimator$Runner$onTimeout$1
                @Override // java.lang.Runnable
                public final void run() {
                    ActivityLaunchAnimator.Runner runner = ActivityLaunchAnimator.Runner.this;
                    int i = ActivityLaunchAnimator.Runner.$r8$clinit;
                    Objects.requireNonNull(runner);
                    if (!runner.cancelled) {
                        Log.i("ActivityLaunchAnimator", "Remote animation timed out");
                        runner.timedOut = true;
                        runner.controller.onLaunchAnimationCancelled();
                    }
                }
            };
        }

        public final void onAnimationCancelled() {
            if (!this.timedOut) {
                Log.i("ActivityLaunchAnimator", "Remote animation was cancelled");
                this.cancelled = true;
                this.launchContainer.removeCallbacks(this.onTimeout);
                this.context.getMainExecutor().execute(new Runnable() { // from class: com.android.systemui.animation.ActivityLaunchAnimator$Runner$onAnimationCancelled$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        LaunchAnimator$startAnimation$3 launchAnimator$startAnimation$3 = ActivityLaunchAnimator.Runner.this.animation;
                        if (launchAnimator$startAnimation$3 != null) {
                            launchAnimator$startAnimation$3.$cancelled.element = true;
                            launchAnimator$startAnimation$3.$animator.cancel();
                        }
                        ActivityLaunchAnimator.Runner.this.controller.onLaunchAnimationCancelled();
                    }
                });
            }
        }

        public final void onAnimationStart(int i, final RemoteAnimationTarget[] remoteAnimationTargetArr, RemoteAnimationTarget[] remoteAnimationTargetArr2, final RemoteAnimationTarget[] remoteAnimationTargetArr3, final IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback) {
            this.launchContainer.removeCallbacks(this.onTimeout);
            if (this.timedOut) {
                if (iRemoteAnimationFinishedCallback != null) {
                    try {
                        iRemoteAnimationFinishedCallback.onAnimationFinished();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            } else if (!this.cancelled) {
                this.context.getMainExecutor().execute(new Runnable() { // from class: com.android.systemui.animation.ActivityLaunchAnimator$Runner$onAnimationStart$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        final RemoteAnimationTarget remoteAnimationTarget;
                        final RemoteAnimationTarget remoteAnimationTarget2;
                        int i2;
                        LaunchAnimator launchAnimator;
                        float f;
                        boolean z;
                        boolean z2;
                        final ActivityLaunchAnimator.Runner runner = ActivityLaunchAnimator.Runner.this;
                        RemoteAnimationTarget[] remoteAnimationTargetArr4 = remoteAnimationTargetArr;
                        RemoteAnimationTarget[] remoteAnimationTargetArr5 = remoteAnimationTargetArr3;
                        final IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback2 = iRemoteAnimationFinishedCallback;
                        int i3 = ActivityLaunchAnimator.Runner.$r8$clinit;
                        Objects.requireNonNull(runner);
                        if (remoteAnimationTargetArr4 != null) {
                            int length = remoteAnimationTargetArr4.length;
                            int i4 = 0;
                            while (i4 < length) {
                                RemoteAnimationTarget remoteAnimationTarget3 = remoteAnimationTargetArr4[i4];
                                i4++;
                                if (remoteAnimationTarget3.mode == 0) {
                                    z2 = true;
                                    continue;
                                } else {
                                    z2 = false;
                                    continue;
                                }
                                if (z2) {
                                    remoteAnimationTarget = remoteAnimationTarget3;
                                    break;
                                }
                            }
                        }
                        remoteAnimationTarget = null;
                        if (remoteAnimationTarget == null) {
                            Log.i("ActivityLaunchAnimator", "Aborting the animation as no window is opening");
                            runner.launchContainer.removeCallbacks(runner.onTimeout);
                            if (iRemoteAnimationFinishedCallback2 != null) {
                                try {
                                    iRemoteAnimationFinishedCallback2.onAnimationFinished();
                                } catch (RemoteException e2) {
                                    e2.printStackTrace();
                                }
                            }
                            runner.controller.onLaunchAnimationCancelled();
                            return;
                        }
                        if (remoteAnimationTargetArr5 != null) {
                            int length2 = remoteAnimationTargetArr5.length;
                            int i5 = 0;
                            while (i5 < length2) {
                                RemoteAnimationTarget remoteAnimationTarget4 = remoteAnimationTargetArr5[i5];
                                i5++;
                                if (remoteAnimationTarget4.windowType == 2019) {
                                    z = true;
                                    continue;
                                } else {
                                    z = false;
                                    continue;
                                }
                                if (z) {
                                    remoteAnimationTarget2 = remoteAnimationTarget4;
                                    break;
                                }
                            }
                        }
                        remoteAnimationTarget2 = null;
                        Rect rect = remoteAnimationTarget.screenSpaceBounds;
                        LaunchAnimator.State state = new LaunchAnimator.State(rect.top, rect.bottom, rect.left, rect.right, 0.0f, 0.0f, 48);
                        ActivityLaunchAnimator activityLaunchAnimator = ActivityLaunchAnimator.this;
                        Objects.requireNonNull(activityLaunchAnimator);
                        ActivityLaunchAnimator.Callback callback = activityLaunchAnimator.callback;
                        Intrinsics.checkNotNull(callback);
                        ActivityManager.RunningTaskInfo runningTaskInfo = remoteAnimationTarget.taskInfo;
                        StatusBar.AnonymousClass23 r0 = (StatusBar.AnonymousClass23) callback;
                        if (!StatusBar.this.mStartingSurfaceOptional.isPresent()) {
                            Log.w("StatusBar", "No starting surface, defaulting to SystemBGColor");
                            i2 = SplashscreenContentDrawer.getSystemBGColor();
                        } else {
                            i2 = StatusBar.this.mStartingSurfaceOptional.get().getBackgroundColor(runningTaskInfo);
                        }
                        if (runner.controller.isDialogLaunch()) {
                            launchAnimator = ActivityLaunchAnimator.this.dialogToAppAnimator;
                        } else {
                            launchAnimator = ActivityLaunchAnimator.this.launchAnimator;
                        }
                        if (launchAnimator.isExpandingFullyAbove$frameworks__base__packages__SystemUI__animation__android_common__SystemUIAnimationLib(runner.controller.getLaunchContainer(), state)) {
                            f = ScreenDecorationsUtils.getWindowCornerRadius(runner.context);
                        } else {
                            f = 0.0f;
                        }
                        state.topCornerRadius = f;
                        state.bottomCornerRadius = f;
                        final ActivityLaunchAnimator.Controller controller = runner.controller;
                        final ActivityLaunchAnimator activityLaunchAnimator2 = ActivityLaunchAnimator.this;
                        runner.animation = launchAnimator.startAnimation(new LaunchAnimator.Controller(activityLaunchAnimator2, iRemoteAnimationFinishedCallback2, runner, remoteAnimationTarget, remoteAnimationTarget2) { // from class: com.android.systemui.animation.ActivityLaunchAnimator$Runner$startAnimation$controller$1
                            public final /* synthetic */ ActivityLaunchAnimator.Controller $$delegate_0;
                            public final /* synthetic */ IRemoteAnimationFinishedCallback $iCallback;
                            public final /* synthetic */ RemoteAnimationTarget $navigationBar;
                            public final /* synthetic */ RemoteAnimationTarget $window;
                            public final /* synthetic */ ActivityLaunchAnimator this$0;
                            public final /* synthetic */ ActivityLaunchAnimator.Runner this$1;

                            @Override // com.android.systemui.animation.LaunchAnimator.Controller
                            public final LaunchAnimator.State createAnimatorState() {
                                return this.$$delegate_0.createAnimatorState();
                            }

                            @Override // com.android.systemui.animation.LaunchAnimator.Controller
                            public final ViewGroup getLaunchContainer() {
                                return this.$$delegate_0.getLaunchContainer();
                            }

                            @Override // com.android.systemui.animation.LaunchAnimator.Controller
                            public final View getOpeningWindowSyncView() {
                                return this.$$delegate_0.getOpeningWindowSyncView();
                            }

                            {
                                this.this$0 = activityLaunchAnimator2;
                                this.$iCallback = iRemoteAnimationFinishedCallback2;
                                this.this$1 = runner;
                                this.$window = remoteAnimationTarget;
                                this.$navigationBar = remoteAnimationTarget2;
                                this.$$delegate_0 = ActivityLaunchAnimator.Controller.this;
                            }

                            @Override // com.android.systemui.animation.LaunchAnimator.Controller
                            public final void onLaunchAnimationEnd(boolean z3) {
                                for (ActivityLaunchAnimator.Listener listener : this.this$0.listeners) {
                                    listener.onLaunchAnimationEnd();
                                }
                                IRemoteAnimationFinishedCallback iRemoteAnimationFinishedCallback3 = this.$iCallback;
                                if (iRemoteAnimationFinishedCallback3 != null) {
                                    ActivityLaunchAnimator.Runner runner2 = this.this$1;
                                    int i6 = ActivityLaunchAnimator.Runner.$r8$clinit;
                                    Objects.requireNonNull(runner2);
                                    try {
                                        iRemoteAnimationFinishedCallback3.onAnimationFinished();
                                    } catch (RemoteException e3) {
                                        e3.printStackTrace();
                                    }
                                }
                                ActivityLaunchAnimator.Controller.this.onLaunchAnimationEnd(z3);
                            }

                            @Override // com.android.systemui.animation.LaunchAnimator.Controller
                            public final void onLaunchAnimationProgress(LaunchAnimator.State state2, float f2, float f3) {
                                int i6;
                                int i7;
                                int i8;
                                int i9;
                                if (!state2.visible) {
                                    ActivityLaunchAnimator.Runner runner2 = this.this$1;
                                    RemoteAnimationTarget remoteAnimationTarget5 = this.$window;
                                    int i10 = ActivityLaunchAnimator.Runner.$r8$clinit;
                                    Objects.requireNonNull(runner2);
                                    if (runner2.transactionApplierView.getViewRootImpl() != null) {
                                        Rect rect2 = remoteAnimationTarget5.screenSpaceBounds;
                                        int i11 = rect2.left;
                                        int i12 = rect2.right;
                                        float f4 = (i11 + i12) / 2.0f;
                                        int i13 = rect2.top;
                                        float f5 = rect2.bottom - i13;
                                        float max = Math.max((state2.right - state2.left) / (i12 - i11), (state2.bottom - state2.top) / f5);
                                        runner2.matrix.reset();
                                        runner2.matrix.setScale(max, max, f4, (i13 + i6) / 2.0f);
                                        float f6 = (f5 * max) - f5;
                                        runner2.matrix.postTranslate((((state2.right - i7) / 2.0f) + state2.left) - f4, (f6 / 2.0f) + (state2.top - rect2.top));
                                        float f7 = state2.left - rect2.left;
                                        float f8 = state2.top - rect2.top;
                                        runner2.windowCropF.set(f7, f8, (state2.right - i8) + f7, (state2.bottom - i9) + f8);
                                        runner2.matrix.invert(runner2.invertMatrix);
                                        runner2.invertMatrix.mapRect(runner2.windowCropF);
                                        runner2.windowCrop.set(R$drawable.roundToInt(runner2.windowCropF.left), R$drawable.roundToInt(runner2.windowCropF.top), R$drawable.roundToInt(runner2.windowCropF.right), R$drawable.roundToInt(runner2.windowCropF.bottom));
                                        runner2.transactionApplier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(remoteAnimationTarget5.leash).withAlpha(1.0f).withMatrix(runner2.matrix).withWindowCrop(runner2.windowCrop).withCornerRadius(Math.max(state2.topCornerRadius, state2.bottomCornerRadius) / max).withVisibility(true).build()});
                                    }
                                }
                                RemoteAnimationTarget remoteAnimationTarget6 = this.$navigationBar;
                                if (remoteAnimationTarget6 != null) {
                                    ActivityLaunchAnimator.Runner runner3 = this.this$1;
                                    int i14 = ActivityLaunchAnimator.Runner.$r8$clinit;
                                    Objects.requireNonNull(runner3);
                                    if (runner3.transactionApplierView.getViewRootImpl() != null) {
                                        PorterDuffXfermode porterDuffXfermode = LaunchAnimator.SRC_MODE;
                                        LaunchAnimator.Timings timings = ActivityLaunchAnimator.TIMINGS;
                                        float progress = LaunchAnimator.Companion.getProgress(timings, f3, ActivityLaunchAnimator.ANIMATION_DELAY_NAV_FADE_IN, 133L);
                                        SyncRtSurfaceTransactionApplier.SurfaceParams.Builder builder = new SyncRtSurfaceTransactionApplier.SurfaceParams.Builder(remoteAnimationTarget6.leash);
                                        if (progress > 0.0f) {
                                            runner3.matrix.reset();
                                            runner3.matrix.setTranslate(0.0f, state2.top - remoteAnimationTarget6.sourceContainerBounds.top);
                                            runner3.windowCrop.set(state2.left, 0, state2.right, state2.bottom - state2.top);
                                            builder.withAlpha(ActivityLaunchAnimator.NAV_FADE_IN_INTERPOLATOR.getInterpolation(progress)).withMatrix(runner3.matrix).withWindowCrop(runner3.windowCrop).withVisibility(true);
                                        } else {
                                            builder.withAlpha(1.0f - ActivityLaunchAnimator.NAV_FADE_OUT_INTERPOLATOR.getInterpolation(LaunchAnimator.Companion.getProgress(timings, f3, 0L, 133L)));
                                        }
                                        runner3.transactionApplier.scheduleApply(new SyncRtSurfaceTransactionApplier.SurfaceParams[]{builder.build()});
                                    }
                                }
                                for (ActivityLaunchAnimator.Listener listener : this.this$0.listeners) {
                                    listener.onLaunchAnimationProgress(f3);
                                }
                                ActivityLaunchAnimator.Controller.this.onLaunchAnimationProgress(state2, f2, f3);
                            }

                            @Override // com.android.systemui.animation.LaunchAnimator.Controller
                            public final void onLaunchAnimationStart(boolean z3) {
                                for (ActivityLaunchAnimator.Listener listener : this.this$0.listeners) {
                                    listener.onLaunchAnimationStart();
                                }
                                ActivityLaunchAnimator.Controller.this.onLaunchAnimationStart(z3);
                            }
                        }, state, i2, true);
                    }
                });
            }
        }
    }

    /* compiled from: ActivityLaunchAnimator.kt */
    /* loaded from: classes.dex */
    public interface Controller extends LaunchAnimator.Controller {
        default boolean isDialogLaunch() {
            return false;
        }

        default void onIntentStarted(boolean z) {
        }

        default void onLaunchAnimationCancelled() {
        }

        static GhostedViewLaunchAnimatorController fromView(View view, Integer num) {
            if (view.getParent() instanceof ViewGroup) {
                return new GhostedViewLaunchAnimatorController(view, num, 4);
            }
            Log.wtf("ActivityLaunchAnimator", "Skipping animation as view " + view + " is not attached to a ViewGroup", new Exception());
            return null;
        }
    }

    static {
        PathInterpolator pathInterpolator = Interpolators.EMPHASIZED;
        Path path = new Path();
        path.moveTo(0.0f, 0.0f);
        path.cubicTo(0.1217f, 0.0462f, 0.15f, 0.4686f, 0.1667f, 0.66f);
        path.cubicTo(0.1834f, 0.8878f, 0.1667f, 1.0f, 1.0f, 1.0f);
        INTERPOLATORS = new LaunchAnimator.Interpolators(pathInterpolator, new PathInterpolator(path), Interpolators.LINEAR_OUT_SLOW_IN, new PathInterpolator(0.0f, 0.0f, 0.6f, 1.0f));
    }

    public ActivityLaunchAnimator() {
        LaunchAnimator.Timings timings = TIMINGS;
        LaunchAnimator.Interpolators interpolators = INTERPOLATORS;
        LaunchAnimator launchAnimator = new LaunchAnimator(timings, interpolators);
        LaunchAnimator launchAnimator2 = new LaunchAnimator(DIALOG_TIMINGS, interpolators);
        this.launchAnimator = launchAnimator;
        this.dialogToAppAnimator = launchAnimator2;
    }

    @VisibleForTesting
    public final Runner createRunner(Controller controller) {
        return new Runner(controller);
    }

    public final void startIntentWithAnimation(Controller controller, boolean z, String str, boolean z2, Function1<? super RemoteAnimationAdapter, Integer> function1) {
        boolean z3;
        boolean z4;
        boolean z5 = false;
        RemoteAnimationAdapter remoteAnimationAdapter = null;
        if (controller == null || !z) {
            Log.i("ActivityLaunchAnimator", "Starting intent with no animation");
            function1.invoke(null);
            if (controller != null) {
                callOnIntentStartedOnMainThread(controller, false);
                return;
            }
            return;
        }
        Callback callback = this.callback;
        if (callback != null) {
            Runner runner = new Runner(controller);
            StatusBar.AnonymousClass23 r7 = (StatusBar.AnonymousClass23) callback;
            if (!StatusBar.this.mKeyguardStateController.isShowing() || z2) {
                z3 = false;
            } else {
                z3 = true;
            }
            if (!z3) {
                z4 = z3;
                remoteAnimationAdapter = new RemoteAnimationAdapter(runner, 500L, 500 - 150);
            } else {
                z4 = z3;
            }
            if (!(str == null || remoteAnimationAdapter == null)) {
                try {
                    ActivityTaskManager.getService().registerRemoteAnimationForNextActivityStart(str, remoteAnimationAdapter);
                } catch (RemoteException e) {
                    Log.w("ActivityLaunchAnimator", "Unable to register the remote animation", e);
                }
            }
            int intValue = function1.invoke(remoteAnimationAdapter).intValue();
            if (intValue == 2 || intValue == 0 || (intValue == 3 && z4)) {
                z5 = true;
            }
            Log.i("ActivityLaunchAnimator", "launchResult=" + intValue + " willAnimate=" + z5 + " hideKeyguardWithAnimation=" + z4);
            callOnIntentStartedOnMainThread(controller, z5);
            if (z5) {
                runner.launchContainer.postDelayed(runner.onTimeout, 1000L);
                if (z4) {
                    StatusBar.this.mMainExecutor.execute(new SystemUIApplication$$ExternalSyntheticLambda1(r7, runner, 2));
                    return;
                }
                return;
            }
            return;
        }
        throw new IllegalStateException("ActivityLaunchAnimator.callback must be set before using this animator");
    }

    public final void startPendingIntentWithAnimation(StatusBarLaunchAnimatorController statusBarLaunchAnimatorController, boolean z, String str, PendingIntentStarter pendingIntentStarter) throws PendingIntent.CanceledException {
        startIntentWithAnimation(statusBarLaunchAnimatorController, z, str, false, new ActivityLaunchAnimator$startPendingIntentWithAnimation$1(pendingIntentStarter));
    }

    public static void callOnIntentStartedOnMainThread(final Controller controller, final boolean z) {
        if (!Intrinsics.areEqual(Looper.myLooper(), Looper.getMainLooper())) {
            controller.getLaunchContainer().getContext().getMainExecutor().execute(new Runnable() { // from class: com.android.systemui.animation.ActivityLaunchAnimator$callOnIntentStartedOnMainThread$1
                @Override // java.lang.Runnable
                public final void run() {
                    ActivityLaunchAnimator.Controller.this.onIntentStarted(z);
                }
            });
        } else {
            controller.onIntentStarted(z);
        }
    }
}

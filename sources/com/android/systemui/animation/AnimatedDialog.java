package com.android.systemui.animation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Looper;
import android.service.dreams.IDreamManager;
import android.util.Log;
import android.util.MathUtils;
import android.view.GhostView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.leanback.R$drawable;
import com.android.systemui.animation.AnimatedDialog;
import com.android.systemui.animation.LaunchAnimator;
import java.util.Objects;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: DialogLaunchAnimator.kt */
/* loaded from: classes.dex */
public final class AnimatedDialog {
    public final AnimatedBoundsLayoutListener backgroundLayoutListener;
    public AnimatedDialog$start$dialogContentWithBackground$2 decorViewLayoutListener;
    public final Dialog dialog;
    public ViewGroup dialogContentWithBackground;
    public boolean dismissRequested;
    public final IDreamManager dreamManager;
    public boolean exitAnimationDisabled;
    public final boolean forceDisableSynchronization;
    public boolean isDismissing;
    public boolean isOriginalDialogViewLaidOut;
    public boolean isTouchSurfaceGhostDrawn;
    public final LaunchAnimator launchAnimator;
    public final Function1<AnimatedDialog, Unit> onDialogDismissed;
    public final AnimatedDialog parentAnimatedDialog;
    public View touchSurface;
    public final Lazy decorView$delegate = LazyKt__LazyJVMKt.lazy(new AnimatedDialog$decorView$2(this));
    public int originalDialogBackgroundColor = -16777216;
    public boolean isLaunching = true;

    /* compiled from: DialogLaunchAnimator.kt */
    /* loaded from: classes.dex */
    public static final class AnimatedBoundsLayoutListener implements View.OnLayoutChangeListener {
        public ValueAnimator currentAnimator;
        public Rect lastBounds;

        @Override // android.view.View.OnLayoutChangeListener
        public final void onLayoutChange(final View view, final int i, final int i2, final int i3, final int i4, int i5, int i6, int i7, int i8) {
            if (i == i5 && i2 == i6) {
                if (i3 == i7 && i4 == i8) {
                    Rect rect = this.lastBounds;
                    if (rect != null) {
                        view.setLeft(rect.left);
                        view.setTop(rect.top);
                        view.setRight(rect.right);
                        view.setBottom(rect.bottom);
                        return;
                    }
                    return;
                }
            }
            if (this.lastBounds == null) {
                this.lastBounds = new Rect(i5, i6, i7, i8);
            }
            final Rect rect2 = this.lastBounds;
            Intrinsics.checkNotNull(rect2);
            final int i9 = rect2.left;
            final int i10 = rect2.top;
            final int i11 = rect2.right;
            final int i12 = rect2.bottom;
            ValueAnimator valueAnimator = this.currentAnimator;
            if (valueAnimator != null) {
                valueAnimator.cancel();
            }
            this.currentAnimator = null;
            ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
            ofFloat.setDuration(500L);
            ofFloat.setInterpolator(Interpolators.STANDARD);
            ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.animation.AnimatedDialog$AnimatedBoundsLayoutListener$onLayoutChange$animator$1$1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    AnimatedDialog.AnimatedBoundsLayoutListener.this.currentAnimator = null;
                }
            });
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.animation.AnimatedDialog$AnimatedBoundsLayoutListener$onLayoutChange$animator$1$2
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    float animatedFraction = valueAnimator2.getAnimatedFraction();
                    rect2.left = R$drawable.roundToInt(MathUtils.lerp(i9, i, animatedFraction));
                    rect2.top = R$drawable.roundToInt(MathUtils.lerp(i10, i2, animatedFraction));
                    rect2.right = R$drawable.roundToInt(MathUtils.lerp(i11, i3, animatedFraction));
                    rect2.bottom = R$drawable.roundToInt(MathUtils.lerp(i12, i4, animatedFraction));
                    view.setLeft(rect2.left);
                    view.setTop(rect2.top);
                    view.setRight(rect2.right);
                    view.setBottom(rect2.bottom);
                }
            });
            this.currentAnimator = ofFloat;
            ofFloat.start();
        }
    }

    public static ViewGroup findFirstViewGroupWithBackground(View view) {
        if (!(view instanceof ViewGroup)) {
            return null;
        }
        ViewGroup viewGroup = (ViewGroup) view;
        if (viewGroup.getBackground() != null) {
            return viewGroup;
        }
        int i = 0;
        int childCount = viewGroup.getChildCount();
        while (i < childCount) {
            int i2 = i + 1;
            ViewGroup findFirstViewGroupWithBackground = findFirstViewGroupWithBackground(viewGroup.getChildAt(i));
            if (findFirstViewGroupWithBackground != null) {
                return findFirstViewGroupWithBackground;
            }
            i = i2;
        }
        return null;
    }

    public final ViewGroup getDecorView() {
        return (ViewGroup) this.decorView$delegate.getValue();
    }

    public final View prepareForStackDismiss() {
        AnimatedDialog animatedDialog = this.parentAnimatedDialog;
        if (animatedDialog == null) {
            return this.touchSurface;
        }
        animatedDialog.exitAnimationDisabled = true;
        animatedDialog.dialog.hide();
        View prepareForStackDismiss = this.parentAnimatedDialog.prepareForStackDismiss();
        this.parentAnimatedDialog.dialog.dismiss();
        prepareForStackDismiss.setVisibility(4);
        return prepareForStackDismiss;
    }

    public final void startAnimation(boolean z, final Function0<Unit> function0, final Function0<Unit> function02) {
        View view;
        View view2;
        if (z) {
            view = this.touchSurface;
        } else {
            view = this.dialogContentWithBackground;
            Intrinsics.checkNotNull(view);
        }
        if (z) {
            view2 = this.dialogContentWithBackground;
            Intrinsics.checkNotNull(view2);
        } else {
            view2 = this.touchSurface;
        }
        final GhostedViewLaunchAnimatorController ghostedViewLaunchAnimatorController = new GhostedViewLaunchAnimatorController(view, (Integer) null, 6);
        final GhostedViewLaunchAnimatorController ghostedViewLaunchAnimatorController2 = new GhostedViewLaunchAnimatorController(view2, (Integer) null, 6);
        ghostedViewLaunchAnimatorController.launchContainer = getDecorView();
        ghostedViewLaunchAnimatorController2.launchContainer = getDecorView();
        final LaunchAnimator.State createAnimatorState = ghostedViewLaunchAnimatorController2.createAnimatorState();
        LaunchAnimator.Controller animatedDialog$startAnimation$controller$1 = new LaunchAnimator.Controller() { // from class: com.android.systemui.animation.AnimatedDialog$startAnimation$controller$1
            @Override // com.android.systemui.animation.LaunchAnimator.Controller
            public final LaunchAnimator.State createAnimatorState() {
                return GhostedViewLaunchAnimatorController.this.createAnimatorState();
            }

            @Override // com.android.systemui.animation.LaunchAnimator.Controller
            public final ViewGroup getLaunchContainer() {
                GhostedViewLaunchAnimatorController ghostedViewLaunchAnimatorController3 = GhostedViewLaunchAnimatorController.this;
                Objects.requireNonNull(ghostedViewLaunchAnimatorController3);
                return ghostedViewLaunchAnimatorController3.launchContainer;
            }

            @Override // com.android.systemui.animation.LaunchAnimator.Controller
            public final void onLaunchAnimationEnd(boolean z2) {
                GhostedViewLaunchAnimatorController.this.onLaunchAnimationEnd(z2);
                ghostedViewLaunchAnimatorController2.onLaunchAnimationEnd(z2);
                function02.invoke();
            }

            @Override // com.android.systemui.animation.LaunchAnimator.Controller
            public final void onLaunchAnimationProgress(LaunchAnimator.State state, float f, float f2) {
                GhostedViewLaunchAnimatorController.this.onLaunchAnimationProgress(state, f, f2);
                state.visible = !state.visible;
                ghostedViewLaunchAnimatorController2.onLaunchAnimationProgress(state, f, f2);
                ghostedViewLaunchAnimatorController2.fillGhostedViewState(createAnimatorState);
            }

            @Override // com.android.systemui.animation.LaunchAnimator.Controller
            public final void onLaunchAnimationStart(boolean z2) {
                function0.invoke();
                GhostedViewLaunchAnimatorController.this.onLaunchAnimationStart(z2);
                ghostedViewLaunchAnimatorController2.onLaunchAnimationStart(z2);
            }
        };
        LaunchAnimator launchAnimator = this.launchAnimator;
        int i = this.originalDialogBackgroundColor;
        PorterDuffXfermode porterDuffXfermode = LaunchAnimator.SRC_MODE;
        launchAnimator.startAnimation(animatedDialog$startAnimation$controller$1, createAnimatorState, i, false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public AnimatedDialog(LaunchAnimator launchAnimator, IDreamManager iDreamManager, View view, Function1<? super AnimatedDialog, Unit> function1, Dialog dialog, boolean z, AnimatedDialog animatedDialog, boolean z2) {
        AnimatedBoundsLayoutListener animatedBoundsLayoutListener;
        this.launchAnimator = launchAnimator;
        this.dreamManager = iDreamManager;
        this.touchSurface = view;
        this.onDialogDismissed = function1;
        this.dialog = dialog;
        this.parentAnimatedDialog = animatedDialog;
        this.forceDisableSynchronization = z2;
        if (z) {
            animatedBoundsLayoutListener = new AnimatedBoundsLayoutListener();
        } else {
            animatedBoundsLayoutListener = null;
        }
        this.backgroundLayoutListener = animatedBoundsLayoutListener;
    }

    public static final void access$maybeStartLaunchAnimation(AnimatedDialog animatedDialog) {
        Objects.requireNonNull(animatedDialog);
        if (animatedDialog.isTouchSurfaceGhostDrawn && animatedDialog.isOriginalDialogViewLaidOut) {
            animatedDialog.dialog.getWindow().addFlags(2);
            animatedDialog.startAnimation(true, new AnimatedDialog$maybeStartLaunchAnimation$1(animatedDialog), new AnimatedDialog$maybeStartLaunchAnimation$2(animatedDialog));
        }
    }

    public final void addTouchSurfaceGhost() {
        LaunchableView launchableView;
        if (getDecorView().getViewRootImpl() == null) {
            getDecorView().post(new Runnable() { // from class: com.android.systemui.animation.AnimatedDialog$addTouchSurfaceGhost$1
                @Override // java.lang.Runnable
                public final void run() {
                    AnimatedDialog.this.addTouchSurfaceGhost();
                }
            });
            return;
        }
        AnimatedDialog$addTouchSurfaceGhost$2 animatedDialog$addTouchSurfaceGhost$2 = new AnimatedDialog$addTouchSurfaceGhost$2(this);
        if (this.forceDisableSynchronization) {
            animatedDialog$addTouchSurfaceGhost$2.invoke();
        } else {
            boolean z = ViewRootSync.forceDisableSynchronization;
            ViewRootSync.synchronizeNextDraw(this.touchSurface, getDecorView(), animatedDialog$addTouchSurfaceGhost$2);
        }
        GhostView.addGhost(this.touchSurface, getDecorView());
        View view = this.touchSurface;
        if (view instanceof LaunchableView) {
            launchableView = (LaunchableView) view;
        } else {
            launchableView = null;
        }
        if (launchableView != null) {
            launchableView.setShouldBlockVisibilityChanges(true);
        }
    }

    public final void onDialogDismissed() {
        View view;
        if (!Intrinsics.areEqual(Looper.myLooper(), Looper.getMainLooper())) {
            this.dialog.getContext().getMainExecutor().execute(new Runnable() { // from class: com.android.systemui.animation.AnimatedDialog$onDialogDismissed$1
                @Override // java.lang.Runnable
                public final void run() {
                    AnimatedDialog.this.onDialogDismissed();
                }
            });
            return;
        }
        boolean z = true;
        if (this.isLaunching) {
            this.dismissRequested = true;
        } else if (!this.isDismissing) {
            this.isDismissing = true;
            AnimatedDialog$onDialogDismissed$2 animatedDialog$onDialogDismissed$2 = new AnimatedDialog$onDialogDismissed$2(this);
            if (this.decorViewLayoutListener != null) {
                getDecorView().removeOnLayoutChangeListener(this.decorViewLayoutListener);
            }
            LaunchableView launchableView = null;
            if (this.exitAnimationDisabled || !this.dialog.isShowing() || this.dreamManager.isDreaming() || this.touchSurface.getVisibility() != 4 || !this.touchSurface.isAttachedToWindow()) {
                z = false;
            } else {
                ViewParent parent = this.touchSurface.getParent();
                if (parent instanceof View) {
                    view = (View) parent;
                } else {
                    view = null;
                }
                if (view != null) {
                    z = view.isShown();
                }
            }
            if (!z) {
                Log.i("DialogLaunchAnimator", "Skipping animation of dialog into the touch surface");
                View view2 = this.touchSurface;
                if (view2 instanceof LaunchableView) {
                    launchableView = (LaunchableView) view2;
                }
                if (launchableView != null) {
                    launchableView.setShouldBlockVisibilityChanges(false);
                }
                if (this.touchSurface.getVisibility() == 4) {
                    this.touchSurface.setVisibility(0);
                }
                animatedDialog$onDialogDismissed$2.invoke(Boolean.FALSE);
                this.onDialogDismissed.invoke(this);
                return;
            }
            startAnimation(false, new AnimatedDialog$hideDialogIntoView$1(this), new AnimatedDialog$hideDialogIntoView$2(this, animatedDialog$onDialogDismissed$2));
        }
    }
}

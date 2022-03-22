package com.android.systemui.animation;

import android.app.Dialog;
import android.service.dreams.IDreamManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.LaunchAnimator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: DialogLaunchAnimator.kt */
/* loaded from: classes.dex */
public final class DialogLaunchAnimator {
    @Deprecated
    public static final LaunchAnimator.Interpolators INTERPOLATORS;
    public final IDreamManager dreamManager;
    public final LaunchAnimator launchAnimator;
    @Deprecated
    public static final LaunchAnimator.Timings TIMINGS = ActivityLaunchAnimator.TIMINGS;
    @Deprecated
    public static final int TAG_LAUNCH_ANIMATION_RUNNING = 2131428205;
    public final boolean isForTesting = false;
    public final HashSet<AnimatedDialog> openedDialogs = new HashSet<>();

    static {
        LaunchAnimator.Timings timings = ActivityLaunchAnimator.TIMINGS;
        LaunchAnimator.Interpolators interpolators = ActivityLaunchAnimator.INTERPOLATORS;
        Objects.requireNonNull(interpolators);
        Interpolator interpolator = interpolators.positionInterpolator;
        Interpolator interpolator2 = interpolators.contentBeforeFadeOutInterpolator;
        Interpolator interpolator3 = interpolators.contentAfterFadeInInterpolator;
        Objects.requireNonNull(interpolators);
        INTERPOLATORS = new LaunchAnimator.Interpolators(interpolator, interpolator, interpolator2, interpolator3);
    }

    public DialogLaunchAnimator(IDreamManager iDreamManager) {
        LaunchAnimator launchAnimator = new LaunchAnimator(TIMINGS, INTERPOLATORS);
        this.dreamManager = iDreamManager;
        this.launchAnimator = launchAnimator;
    }

    public final void disableAllCurrentDialogsExitAnimations() {
        for (AnimatedDialog animatedDialog : this.openedDialogs) {
            Objects.requireNonNull(animatedDialog);
            animatedDialog.exitAnimationDisabled = true;
        }
    }

    public final void dismissStack(Dialog dialog) {
        Object obj;
        Iterator<T> it = this.openedDialogs.iterator();
        while (true) {
            if (!it.hasNext()) {
                obj = null;
                break;
            }
            obj = it.next();
            AnimatedDialog animatedDialog = (AnimatedDialog) obj;
            Objects.requireNonNull(animatedDialog);
            if (Intrinsics.areEqual(animatedDialog.dialog, dialog)) {
                break;
            }
        }
        AnimatedDialog animatedDialog2 = (AnimatedDialog) obj;
        if (animatedDialog2 != null) {
            animatedDialog2.touchSurface = animatedDialog2.prepareForStackDismiss();
        }
        dialog.dismiss();
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.animation.DialogLaunchAnimator$createActivityLaunchController$1] */
    public static DialogLaunchAnimator$createActivityLaunchController$1 createActivityLaunchController$default(DialogLaunchAnimator dialogLaunchAnimator, View view) {
        AnimatedDialog animatedDialog;
        ViewGroup viewGroup;
        GhostedViewLaunchAnimatorController ghostedViewLaunchAnimatorController;
        Objects.requireNonNull(dialogLaunchAnimator);
        Iterator<AnimatedDialog> it = dialogLaunchAnimator.openedDialogs.iterator();
        while (true) {
            if (!it.hasNext()) {
                animatedDialog = null;
                break;
            }
            animatedDialog = it.next();
            AnimatedDialog animatedDialog2 = animatedDialog;
            Objects.requireNonNull(animatedDialog2);
            if (Intrinsics.areEqual(animatedDialog2.dialog.getWindow().getDecorView().getViewRootImpl(), view.getViewRootImpl())) {
                break;
            }
        }
        AnimatedDialog animatedDialog3 = animatedDialog;
        if (animatedDialog3 == null) {
            return null;
        }
        animatedDialog3.exitAnimationDisabled = true;
        Dialog dialog = animatedDialog3.dialog;
        if (!dialog.isShowing() || (viewGroup = animatedDialog3.dialogContentWithBackground) == null) {
            return null;
        }
        if (!(viewGroup.getParent() instanceof ViewGroup)) {
            Log.wtf("ActivityLaunchAnimator", "Skipping animation as view " + viewGroup + " is not attached to a ViewGroup", new Exception());
            ghostedViewLaunchAnimatorController = null;
        } else {
            ghostedViewLaunchAnimatorController = new GhostedViewLaunchAnimatorController(viewGroup, (Integer) null, 4);
        }
        if (ghostedViewLaunchAnimatorController == null) {
            return null;
        }
        return new ActivityLaunchAnimator.Controller(ghostedViewLaunchAnimatorController, dialog, animatedDialog3) { // from class: com.android.systemui.animation.DialogLaunchAnimator$createActivityLaunchController$1
            public final /* synthetic */ ActivityLaunchAnimator.Controller $$delegate_0;
            public final /* synthetic */ AnimatedDialog $animatedDialog;
            public final /* synthetic */ ActivityLaunchAnimator.Controller $controller;
            public final /* synthetic */ Dialog $dialog;

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

            @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
            public final boolean isDialogLaunch() {
                return true;
            }

            @Override // com.android.systemui.animation.LaunchAnimator.Controller
            public final void onLaunchAnimationProgress(LaunchAnimator.State state, float f, float f2) {
                this.$$delegate_0.onLaunchAnimationProgress(state, f, f2);
            }

            @Override // com.android.systemui.animation.LaunchAnimator.Controller
            public final void setLaunchContainer(ViewGroup viewGroup2) {
                this.$$delegate_0.setLaunchContainer(viewGroup2);
            }

            {
                this.$controller = ghostedViewLaunchAnimatorController;
                this.$dialog = dialog;
                this.$animatedDialog = animatedDialog3;
                this.$$delegate_0 = ghostedViewLaunchAnimatorController;
            }

            @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
            public final void onIntentStarted(boolean z) {
                this.$controller.onIntentStarted(z);
                if (!z) {
                    this.$dialog.dismiss();
                }
            }

            @Override // com.android.systemui.animation.ActivityLaunchAnimator.Controller
            public final void onLaunchAnimationCancelled() {
                this.$controller.onLaunchAnimationCancelled();
                this.$dialog.setDismissOverride(new DialogLaunchAnimator$createActivityLaunchController$1$enableDialogDismiss$1(this.$animatedDialog));
                this.$dialog.dismiss();
            }

            @Override // com.android.systemui.animation.LaunchAnimator.Controller
            public final void onLaunchAnimationEnd(boolean z) {
                this.$controller.onLaunchAnimationEnd(z);
                this.$dialog.hide();
                this.$dialog.setDismissOverride(new DialogLaunchAnimator$createActivityLaunchController$1$enableDialogDismiss$1(this.$animatedDialog));
                this.$dialog.dismiss();
            }

            @Override // com.android.systemui.animation.LaunchAnimator.Controller
            public final void onLaunchAnimationStart(boolean z) {
                this.$controller.onLaunchAnimationStart(z);
                this.$dialog.setDismissOverride(DialogLaunchAnimator$createActivityLaunchController$1$disableDialogDismiss$1.INSTANCE);
                AnimatedDialog animatedDialog4 = this.$animatedDialog;
                animatedDialog4.touchSurface = animatedDialog4.prepareForStackDismiss();
                this.$dialog.getWindow().clearFlags(2);
            }
        };
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r13v21, types: [com.android.systemui.animation.AnimatedDialog$start$dialogContentWithBackground$2] */
    /* JADX WARN: Type inference failed for: r15v11, types: [android.widget.FrameLayout, android.view.View, android.view.ViewGroup] */
    /* JADX WARN: Type inference failed for: r2v0 */
    /* JADX WARN: Type inference failed for: r2v1, types: [android.view.View, android.view.ViewGroup] */
    /* JADX WARN: Type inference failed for: r2v4, types: [android.widget.FrameLayout, android.view.View, android.view.ViewGroup] */
    /* JADX WARN: Type inference failed for: r2v5 */
    /* JADX WARN: Type inference failed for: r2v6 */
    /* JADX WARN: Type inference failed for: r2v8, types: [android.view.ViewGroup] */
    /* JADX WARN: Type inference failed for: r2v9 */
    /* JADX WARN: Unknown variable types count: 2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void showFromView(android.app.Dialog r13, android.view.View r14, boolean r15) {
        /*
            Method dump skipped, instructions count: 455
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.animation.DialogLaunchAnimator.showFromView(android.app.Dialog, android.view.View, boolean):void");
    }
}

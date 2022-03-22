package com.android.systemui.shared.animation;

import android.graphics.Point;
import android.view.View;
import android.view.WindowManager;
import com.android.systemui.statusbar.phone.PhoneStatusBarViewController;
import com.android.systemui.statusbar.phone.StatusBarMoveFromCenterAnimationController;
import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* compiled from: UnfoldMoveFromCenterAnimator.kt */
/* loaded from: classes.dex */
public final class UnfoldMoveFromCenterAnimator implements UnfoldTransitionProgressProvider.TransitionProgressListener {
    public final AlphaProvider alphaProvider;
    public boolean isVerticalFold;
    public float lastAnimationProgress;
    public final TranslationApplier translationApplier;
    public final ViewCenterProvider viewCenterProvider;
    public final WindowManager windowManager;
    public final Point screenSize = new Point();
    public final ArrayList animatedViews = new ArrayList();

    /* compiled from: UnfoldMoveFromCenterAnimator.kt */
    /* loaded from: classes.dex */
    public interface AlphaProvider {
        float getAlpha(float f);
    }

    /* compiled from: UnfoldMoveFromCenterAnimator.kt */
    /* loaded from: classes.dex */
    public static final class AnimatedView {
        public float startTranslationX;
        public float startTranslationY;
        public final WeakReference<View> view;

        public AnimatedView() {
            throw null;
        }

        public AnimatedView(WeakReference weakReference) {
            this.view = weakReference;
            this.startTranslationX = 0.0f;
            this.startTranslationY = 0.0f;
        }
    }

    /* compiled from: UnfoldMoveFromCenterAnimator.kt */
    /* loaded from: classes.dex */
    public interface TranslationApplier {
        void apply(View view, float f, float f2);
    }

    /* compiled from: UnfoldMoveFromCenterAnimator.kt */
    /* loaded from: classes.dex */
    public interface ViewCenterProvider {

        /* compiled from: UnfoldMoveFromCenterAnimator.kt */
        /* loaded from: classes.dex */
        public static final class DefaultImpls {
            public static void getViewCenter(View view, Point point) {
                int[] iArr = new int[2];
                view.getLocationOnScreen(iArr);
                int i = iArr[0];
                int i2 = iArr[1];
                point.x = (view.getWidth() / 2) + i;
                point.y = (view.getHeight() / 2) + i2;
            }
        }

        void getViewCenter(View view, Point point);
    }

    @Override // com.android.systemui.unfold.UnfoldTransitionProgressProvider.TransitionProgressListener
    public final void onTransitionFinished() {
    }

    @Override // com.android.systemui.unfold.UnfoldTransitionProgressProvider.TransitionProgressListener
    public final void onTransitionStarted() {
    }

    public UnfoldMoveFromCenterAnimator(WindowManager windowManager, PhoneStatusBarViewController.StatusBarViewsCenterProvider statusBarViewsCenterProvider, StatusBarMoveFromCenterAnimationController.StatusBarIconsAlphaProvider statusBarIconsAlphaProvider) {
        TranslationApplier translationApplier = new TranslationApplier() { // from class: com.android.systemui.shared.animation.UnfoldMoveFromCenterAnimator.1
            @Override // com.android.systemui.shared.animation.UnfoldMoveFromCenterAnimator.TranslationApplier
            public final void apply(View view, float f, float f2) {
                view.setTranslationX(f);
                view.setTranslationY(f2);
            }
        };
        this.windowManager = windowManager;
        this.translationApplier = translationApplier;
        this.viewCenterProvider = statusBarViewsCenterProvider;
        this.alphaProvider = statusBarIconsAlphaProvider;
    }

    @Override // com.android.systemui.unfold.UnfoldTransitionProgressProvider.TransitionProgressListener
    public final void onTransitionProgress(float f) {
        View view;
        Iterator it = this.animatedViews.iterator();
        while (it.hasNext()) {
            AnimatedView animatedView = (AnimatedView) it.next();
            Objects.requireNonNull(animatedView);
            View view2 = animatedView.view.get();
            if (view2 != null) {
                float f2 = 1 - f;
                this.translationApplier.apply(view2, animatedView.startTranslationX * f2, animatedView.startTranslationY * f2);
            }
            if (!(this.alphaProvider == null || (view = animatedView.view.get()) == null)) {
                view.setAlpha(this.alphaProvider.getAlpha(f));
            }
        }
        this.lastAnimationProgress = f;
    }

    public final AnimatedView updateAnimatedView(AnimatedView animatedView, View view) {
        Point point = new Point();
        this.viewCenterProvider.getViewCenter(view, point);
        int i = point.x;
        int i2 = point.y;
        if (this.isVerticalFold) {
            animatedView.startTranslationX = ((this.screenSize.x / 2) - i) * 0.3f;
            animatedView.startTranslationY = 0.0f;
        } else {
            animatedView.startTranslationX = 0.0f;
            animatedView.startTranslationY = ((this.screenSize.y / 2) - i2) * 0.3f;
        }
        return animatedView;
    }

    public final void updateDisplayProperties() {
        boolean z;
        this.windowManager.getDefaultDisplay().getSize(this.screenSize);
        if (this.windowManager.getDefaultDisplay().getRotation() == 0 || this.windowManager.getDefaultDisplay().getRotation() == 2) {
            z = true;
        } else {
            z = false;
        }
        this.isVerticalFold = z;
    }
}

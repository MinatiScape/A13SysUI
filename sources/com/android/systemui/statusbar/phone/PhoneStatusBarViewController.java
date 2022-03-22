package com.android.systemui.statusbar.phone;

import android.content.res.Configuration;
import android.graphics.Point;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import com.android.systemui.shared.animation.UnfoldMoveFromCenterAnimator;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.StatusBarMoveFromCenterAnimationController;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.unfold.SysUIUnfoldComponent;
import com.android.systemui.unfold.util.ScopedUnfoldTransitionProgressProvider;
import com.android.systemui.util.ViewController;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
/* compiled from: PhoneStatusBarViewController.kt */
/* loaded from: classes.dex */
public final class PhoneStatusBarViewController extends ViewController<PhoneStatusBarView> {
    public final ConfigurationController configurationController;
    public final PhoneStatusBarViewController$configurationListener$1 configurationListener = new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.statusbar.phone.PhoneStatusBarViewController$configurationListener$1
        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public final void onConfigChanged(Configuration configuration) {
            ((PhoneStatusBarView) PhoneStatusBarViewController.this.mView).updateResources();
        }
    };
    public final StatusBarMoveFromCenterAnimationController moveFromCenterAnimationController;
    public final ScopedUnfoldTransitionProgressProvider progressProvider;
    public final StatusBarUserSwitcherController userSwitcherController;

    /* compiled from: PhoneStatusBarViewController.kt */
    /* loaded from: classes.dex */
    public static final class Factory {
        public final ConfigurationController configurationController;
        public final Optional<ScopedUnfoldTransitionProgressProvider> progressProvider;
        public final Optional<SysUIUnfoldComponent> unfoldComponent;
        public final StatusBarUserSwitcherController userSwitcherController;

        public Factory(Optional<SysUIUnfoldComponent> optional, Optional<ScopedUnfoldTransitionProgressProvider> optional2, StatusBarUserSwitcherController statusBarUserSwitcherController, ConfigurationController configurationController) {
            this.unfoldComponent = optional;
            this.progressProvider = optional2;
            this.userSwitcherController = statusBarUserSwitcherController;
            this.configurationController = configurationController;
        }
    }

    /* compiled from: PhoneStatusBarViewController.kt */
    /* loaded from: classes.dex */
    public static final class StatusBarViewsCenterProvider implements UnfoldMoveFromCenterAnimator.ViewCenterProvider {
        public static void getViewEdgeCenter(View view, Point point, boolean z) {
            boolean z2;
            int i;
            if (view.getResources().getConfiguration().getLayoutDirection() == 1) {
                z2 = true;
            } else {
                z2 = false;
            }
            boolean z3 = z ^ z2;
            int[] iArr = new int[2];
            view.getLocationOnScreen(iArr);
            int i2 = iArr[0];
            int i3 = iArr[1];
            if (z3) {
                i = view.getHeight() / 2;
            } else {
                i = view.getWidth() - (view.getHeight() / 2);
            }
            point.x = i2 + i;
            point.y = (view.getHeight() / 2) + i3;
        }

        @Override // com.android.systemui.shared.animation.UnfoldMoveFromCenterAnimator.ViewCenterProvider
        public final void getViewCenter(View view, Point point) {
            int id = view.getId();
            if (id == 2131428928) {
                getViewEdgeCenter(view, point, true);
            } else if (id == 2131429001) {
                getViewEdgeCenter(view, point, false);
            } else {
                UnfoldMoveFromCenterAnimator.ViewCenterProvider.DefaultImpls.getViewCenter(view, point);
            }
        }
    }

    @Override // com.android.systemui.util.ViewController
    public final void onInit() {
        this.userSwitcherController.init();
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewAttached() {
        if (this.moveFromCenterAnimationController != null) {
            final View[] viewArr = {((PhoneStatusBarView) this.mView).findViewById(2131428928), (ViewGroup) ((PhoneStatusBarView) this.mView).findViewById(2131429001)};
            ((PhoneStatusBarView) this.mView).getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: com.android.systemui.statusbar.phone.PhoneStatusBarViewController$onViewAttached$1
                @Override // android.view.ViewTreeObserver.OnPreDrawListener
                public final boolean onPreDraw() {
                    StatusBarMoveFromCenterAnimationController statusBarMoveFromCenterAnimationController = PhoneStatusBarViewController.this.moveFromCenterAnimationController;
                    View[] viewArr2 = viewArr;
                    Objects.requireNonNull(statusBarMoveFromCenterAnimationController);
                    statusBarMoveFromCenterAnimationController.moveFromCenterAnimator.updateDisplayProperties();
                    int length = viewArr2.length;
                    int i = 0;
                    while (i < length) {
                        View view = viewArr2[i];
                        i++;
                        UnfoldMoveFromCenterAnimator unfoldMoveFromCenterAnimator = statusBarMoveFromCenterAnimationController.moveFromCenterAnimator;
                        Objects.requireNonNull(unfoldMoveFromCenterAnimator);
                        UnfoldMoveFromCenterAnimator.AnimatedView animatedView = new UnfoldMoveFromCenterAnimator.AnimatedView(new WeakReference(view));
                        unfoldMoveFromCenterAnimator.updateAnimatedView(animatedView, view);
                        unfoldMoveFromCenterAnimator.animatedViews.add(animatedView);
                    }
                    ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider = statusBarMoveFromCenterAnimationController.progressProvider;
                    StatusBarMoveFromCenterAnimationController.TransitionListener transitionListener = statusBarMoveFromCenterAnimationController.transitionListener;
                    Objects.requireNonNull(scopedUnfoldTransitionProgressProvider);
                    scopedUnfoldTransitionProgressProvider.listeners.add(transitionListener);
                    ((PhoneStatusBarView) PhoneStatusBarViewController.this.mView).getViewTreeObserver().removeOnPreDrawListener(this);
                    return true;
                }
            });
            ((PhoneStatusBarView) this.mView).addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.android.systemui.statusbar.phone.PhoneStatusBarViewController$onViewAttached$2
                @Override // android.view.View.OnLayoutChangeListener
                public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                    boolean z;
                    if (i3 - i != i7 - i5) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (z) {
                        StatusBarMoveFromCenterAnimationController statusBarMoveFromCenterAnimationController = PhoneStatusBarViewController.this.moveFromCenterAnimationController;
                        Objects.requireNonNull(statusBarMoveFromCenterAnimationController);
                        statusBarMoveFromCenterAnimationController.moveFromCenterAnimator.updateDisplayProperties();
                        UnfoldMoveFromCenterAnimator unfoldMoveFromCenterAnimator = statusBarMoveFromCenterAnimationController.moveFromCenterAnimator;
                        Objects.requireNonNull(unfoldMoveFromCenterAnimator);
                        Iterator it = unfoldMoveFromCenterAnimator.animatedViews.iterator();
                        while (it.hasNext()) {
                            UnfoldMoveFromCenterAnimator.AnimatedView animatedView = (UnfoldMoveFromCenterAnimator.AnimatedView) it.next();
                            Objects.requireNonNull(animatedView);
                            View view2 = animatedView.view.get();
                            if (view2 != null) {
                                unfoldMoveFromCenterAnimator.updateAnimatedView(animatedView, view2);
                            }
                        }
                        unfoldMoveFromCenterAnimator.onTransitionProgress(unfoldMoveFromCenterAnimator.lastAnimationProgress);
                    }
                }
            });
            ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider = this.progressProvider;
            if (scopedUnfoldTransitionProgressProvider != null) {
                scopedUnfoldTransitionProgressProvider.setReadyToHandleTransition(true);
            }
            this.configurationController.addCallback(this.configurationListener);
        }
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewDetached() {
        ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider = this.progressProvider;
        if (scopedUnfoldTransitionProgressProvider != null) {
            scopedUnfoldTransitionProgressProvider.setReadyToHandleTransition(false);
        }
        StatusBarMoveFromCenterAnimationController statusBarMoveFromCenterAnimationController = this.moveFromCenterAnimationController;
        if (statusBarMoveFromCenterAnimationController != null) {
            ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider2 = statusBarMoveFromCenterAnimationController.progressProvider;
            StatusBarMoveFromCenterAnimationController.TransitionListener transitionListener = statusBarMoveFromCenterAnimationController.transitionListener;
            Objects.requireNonNull(scopedUnfoldTransitionProgressProvider2);
            scopedUnfoldTransitionProgressProvider2.listeners.remove(transitionListener);
            UnfoldMoveFromCenterAnimator unfoldMoveFromCenterAnimator = statusBarMoveFromCenterAnimationController.moveFromCenterAnimator;
            Objects.requireNonNull(unfoldMoveFromCenterAnimator);
            unfoldMoveFromCenterAnimator.onTransitionProgress(1.0f);
            unfoldMoveFromCenterAnimator.animatedViews.clear();
        }
        this.configurationController.removeCallback(this.configurationListener);
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [com.android.systemui.statusbar.phone.PhoneStatusBarViewController$configurationListener$1] */
    public PhoneStatusBarViewController(PhoneStatusBarView phoneStatusBarView, ScopedUnfoldTransitionProgressProvider scopedUnfoldTransitionProgressProvider, StatusBarMoveFromCenterAnimationController statusBarMoveFromCenterAnimationController, StatusBarUserSwitcherController statusBarUserSwitcherController, NotificationPanelViewController.AnonymousClass18 r5, ConfigurationController configurationController) {
        super(phoneStatusBarView);
        this.progressProvider = scopedUnfoldTransitionProgressProvider;
        this.moveFromCenterAnimationController = statusBarMoveFromCenterAnimationController;
        this.userSwitcherController = statusBarUserSwitcherController;
        this.configurationController = configurationController;
        Objects.requireNonNull(phoneStatusBarView);
        phoneStatusBarView.mTouchEventHandler = r5;
    }
}

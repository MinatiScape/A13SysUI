package com.android.systemui.statusbar.events;

import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.preference.R$id;
import com.android.internal.annotations.GuardedBy;
import com.android.systemui.ScreenDecorations;
import com.android.systemui.ScreenDecorations$2$$ExternalSyntheticLambda0;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.events.PrivacyDotViewController;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsChangedListener;
import com.android.systemui.statusbar.phone.StatusBarContentInsetsProvider;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt__SequencesKt;
/* compiled from: PrivacyDotViewController.kt */
/* loaded from: classes.dex */
public final class PrivacyDotViewController {
    public final SystemStatusAnimationScheduler animationScheduler;
    public View bl;
    public View br;
    public Runnable cancelRunnable;
    public final ConfigurationController configurationController;
    public final StatusBarContentInsetsProvider contentInsetsProvider;
    public ViewState currentViewState;
    public final Executor mainExecutor;
    @GuardedBy({"lock"})
    public ViewState nextViewState;
    public ShowingListener showingListener;
    public final StatusBarStateController stateController;
    public View tl;
    public View tr;
    public DelayableExecutor uiExecutor;
    public final Object lock = new Object();
    public final PrivacyDotViewController$systemStatusAnimationCallback$1 systemStatusAnimationCallback = new SystemStatusAnimationCallback() { // from class: com.android.systemui.statusbar.events.PrivacyDotViewController$systemStatusAnimationCallback$1
        @Override // com.android.systemui.statusbar.events.SystemStatusAnimationCallback
        public final void onHidePersistentDot() {
            PrivacyDotViewController privacyDotViewController = PrivacyDotViewController.this;
            synchronized (privacyDotViewController.lock) {
                privacyDotViewController.setNextViewState(ViewState.copy$default(privacyDotViewController.nextViewState, false, false, false, false, null, null, null, null, false, 0, 0, 0, null, null, 16381));
            }
        }

        @Override // com.android.systemui.statusbar.events.SystemStatusAnimationCallback
        public final void onSystemStatusAnimationTransitionToPersistentDot(String str) {
            PrivacyDotViewController privacyDotViewController = PrivacyDotViewController.this;
            synchronized (privacyDotViewController.lock) {
                privacyDotViewController.setNextViewState(ViewState.copy$default(privacyDotViewController.nextViewState, false, true, false, false, null, null, null, null, false, 0, 0, 0, null, str, 8189));
            }
        }
    };

    /* compiled from: PrivacyDotViewController.kt */
    /* loaded from: classes.dex */
    public interface ShowingListener {
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.statusbar.events.PrivacyDotViewController$systemStatusAnimationCallback$1] */
    public PrivacyDotViewController(Executor executor, StatusBarStateController statusBarStateController, ConfigurationController configurationController, StatusBarContentInsetsProvider statusBarContentInsetsProvider, SystemStatusAnimationScheduler systemStatusAnimationScheduler) {
        this.mainExecutor = executor;
        this.stateController = statusBarStateController;
        this.configurationController = configurationController;
        this.contentInsetsProvider = statusBarContentInsetsProvider;
        this.animationScheduler = systemStatusAnimationScheduler;
        ViewState viewState = new ViewState(0);
        this.currentViewState = viewState;
        this.nextViewState = ViewState.copy$default(viewState, false, false, false, false, null, null, null, null, false, 0, 0, 0, null, null, 16383);
        StatusBarContentInsetsChangedListener statusBarContentInsetsChangedListener = new StatusBarContentInsetsChangedListener() { // from class: com.android.systemui.statusbar.events.PrivacyDotViewController.1
            @Override // com.android.systemui.statusbar.phone.StatusBarContentInsetsChangedListener
            public final void onStatusBarContentInsetsChanged() {
                PrivacyDotViewController privacyDotViewController = PrivacyDotViewController.this;
                Objects.requireNonNull(privacyDotViewController);
                List listOf = SetsKt__SetsKt.listOf(privacyDotViewController.contentInsetsProvider.getStatusBarContentAreaForRotation(3), privacyDotViewController.contentInsetsProvider.getStatusBarContentAreaForRotation(0), privacyDotViewController.contentInsetsProvider.getStatusBarContentAreaForRotation(1), privacyDotViewController.contentInsetsProvider.getStatusBarContentAreaForRotation(2));
                synchronized (privacyDotViewController.lock) {
                    privacyDotViewController.setNextViewState(ViewState.copy$default(privacyDotViewController.nextViewState, false, false, false, false, (Rect) listOf.get(1), (Rect) listOf.get(2), (Rect) listOf.get(3), (Rect) listOf.get(0), false, 0, 0, 0, null, null, 16143));
                }
            }
        };
        Objects.requireNonNull(statusBarContentInsetsProvider);
        statusBarContentInsetsProvider.listeners.add(statusBarContentInsetsChangedListener);
        configurationController.addCallback(new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.statusbar.events.PrivacyDotViewController.2
            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public final void onLayoutDirectionChanged(final boolean z) {
                final PrivacyDotViewController privacyDotViewController = PrivacyDotViewController.this;
                DelayableExecutor delayableExecutor = privacyDotViewController.uiExecutor;
                if (delayableExecutor != null) {
                    delayableExecutor.execute(new Runnable() { // from class: com.android.systemui.statusbar.events.PrivacyDotViewController$2$onLayoutDirectionChanged$1
                        @Override // java.lang.Runnable
                        public final void run() {
                            PrivacyDotViewController.this.setCornerVisibilities();
                            PrivacyDotViewController.AnonymousClass2 r1 = this;
                            PrivacyDotViewController privacyDotViewController2 = PrivacyDotViewController.this;
                            boolean z2 = z;
                            synchronized (r1) {
                                ViewState viewState2 = privacyDotViewController2.nextViewState;
                                Objects.requireNonNull(viewState2);
                                privacyDotViewController2.setNextViewState(ViewState.copy$default(privacyDotViewController2.nextViewState, false, false, false, false, null, null, null, null, z2, 0, 0, 0, privacyDotViewController2.selectDesignatedCorner(viewState2.rotation, z2), null, 12031));
                            }
                        }
                    });
                }
            }
        });
        statusBarStateController.addCallback(new StatusBarStateController.StateListener() { // from class: com.android.systemui.statusbar.events.PrivacyDotViewController.3
            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public final void onExpandedChanged(boolean z) {
                PrivacyDotViewController.access$updateStatusBarState(PrivacyDotViewController.this);
            }

            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public final void onStateChanged(int i) {
                PrivacyDotViewController.access$updateStatusBarState(PrivacyDotViewController.this);
            }
        });
    }

    public static final void access$updateStatusBarState(PrivacyDotViewController privacyDotViewController) {
        boolean z;
        Objects.requireNonNull(privacyDotViewController);
        synchronized (privacyDotViewController.lock) {
            ViewState viewState = privacyDotViewController.nextViewState;
            if ((!privacyDotViewController.stateController.isExpanded() || privacyDotViewController.stateController.getState() != 0) && privacyDotViewController.stateController.getState() != 2) {
                z = false;
            } else {
                z = true;
            }
            privacyDotViewController.setNextViewState(ViewState.copy$default(viewState, false, false, z, false, null, null, null, null, false, 0, 0, 0, null, null, 16379));
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:15:0x0021, code lost:
        if (r8 != false) goto L_0x0014;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:?, code lost:
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x0010, code lost:
        if (r8 != false) goto L_0x0012;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final int activeRotationForCorner(android.view.View r7, boolean r8) {
        /*
            r6 = this;
            android.view.View r0 = r6.tr
            r1 = 0
            if (r0 != 0) goto L_0x0006
            r0 = r1
        L_0x0006:
            boolean r0 = kotlin.jvm.internal.Intrinsics.areEqual(r7, r0)
            r2 = 2
            r3 = 3
            r4 = 1
            r5 = 0
            if (r0 == 0) goto L_0x0016
            if (r8 == 0) goto L_0x0014
        L_0x0012:
            r2 = r4
            goto L_0x0038
        L_0x0014:
            r2 = r5
            goto L_0x0038
        L_0x0016:
            android.view.View r0 = r6.tl
            if (r0 != 0) goto L_0x001b
            r0 = r1
        L_0x001b:
            boolean r0 = kotlin.jvm.internal.Intrinsics.areEqual(r7, r0)
            if (r0 == 0) goto L_0x0026
            if (r8 == 0) goto L_0x0024
            goto L_0x0014
        L_0x0024:
            r2 = r3
            goto L_0x0038
        L_0x0026:
            android.view.View r6 = r6.br
            if (r6 != 0) goto L_0x002b
            goto L_0x002c
        L_0x002b:
            r1 = r6
        L_0x002c:
            boolean r6 = kotlin.jvm.internal.Intrinsics.areEqual(r7, r1)
            if (r6 == 0) goto L_0x0035
            if (r8 == 0) goto L_0x0012
            goto L_0x0038
        L_0x0035:
            if (r8 == 0) goto L_0x0038
            goto L_0x0024
        L_0x0038:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.events.PrivacyDotViewController.activeRotationForCorner(android.view.View, boolean):int");
    }

    public final int cornerForView(View view) {
        View view2 = this.tl;
        View view3 = null;
        if (view2 == null) {
            view2 = null;
        }
        if (Intrinsics.areEqual(view, view2)) {
            return 0;
        }
        View view4 = this.tr;
        if (view4 == null) {
            view4 = null;
        }
        if (Intrinsics.areEqual(view, view4)) {
            return 1;
        }
        View view5 = this.bl;
        if (view5 == null) {
            view5 = null;
        }
        if (Intrinsics.areEqual(view, view5)) {
            return 3;
        }
        View view6 = this.br;
        if (view6 != null) {
            view3 = view6;
        }
        if (Intrinsics.areEqual(view, view3)) {
            return 2;
        }
        throw new IllegalArgumentException("not a corner view");
    }

    public final Sequence<View> getViews() {
        View view = this.tl;
        if (view == null) {
            return SequencesKt__SequencesKt.sequenceOf(new View[0]);
        }
        View[] viewArr = new View[4];
        View view2 = null;
        if (view == null) {
            view = null;
        }
        viewArr[0] = view;
        View view3 = this.tr;
        if (view3 == null) {
            view3 = null;
        }
        viewArr[1] = view3;
        View view4 = this.br;
        if (view4 == null) {
            view4 = null;
        }
        viewArr[2] = view4;
        View view5 = this.bl;
        if (view5 != null) {
            view2 = view5;
        }
        viewArr[3] = view2;
        return SequencesKt__SequencesKt.sequenceOf(viewArr);
    }

    public final View selectDesignatedCorner(int i, boolean z) {
        View view = this.tl;
        if (view == null) {
            return null;
        }
        if (i != 0) {
            if (i != 1) {
                if (i != 2) {
                    if (i != 3) {
                        throw new IllegalStateException("unknown rotation");
                    } else if (z) {
                        View view2 = this.bl;
                        if (view2 != null) {
                            return view2;
                        }
                    } else if (view != null) {
                        return view;
                    }
                } else if (z) {
                    View view3 = this.br;
                    if (view3 != null) {
                        return view3;
                    }
                } else {
                    View view4 = this.bl;
                    if (view4 != null) {
                        return view4;
                    }
                }
            } else if (z) {
                View view5 = this.tr;
                if (view5 != null) {
                    return view5;
                }
            } else {
                View view6 = this.br;
                if (view6 != null) {
                    return view6;
                }
            }
        } else if (!z) {
            View view7 = this.tr;
            if (view7 != null) {
                return view7;
            }
        } else if (view != null) {
            return view;
        }
        return null;
    }

    public final void setNextViewState(ViewState viewState) {
        Runnable runnable;
        this.nextViewState = viewState;
        Runnable runnable2 = this.cancelRunnable;
        if (runnable2 != null) {
            runnable2.run();
        }
        DelayableExecutor delayableExecutor = this.uiExecutor;
        if (delayableExecutor == null) {
            runnable = null;
        } else {
            runnable = delayableExecutor.executeDelayed(new Runnable() { // from class: com.android.systemui.statusbar.events.PrivacyDotViewController$scheduleUpdate$1
                @Override // java.lang.Runnable
                public final void run() {
                    ViewState copy$default;
                    boolean z;
                    boolean z2;
                    final View view;
                    View view2;
                    View view3;
                    boolean z3;
                    int i;
                    int i2;
                    int i3;
                    final PrivacyDotViewController privacyDotViewController = PrivacyDotViewController.this;
                    Objects.requireNonNull(privacyDotViewController);
                    synchronized (privacyDotViewController.lock) {
                        copy$default = ViewState.copy$default(privacyDotViewController.nextViewState, false, false, false, false, null, null, null, null, false, 0, 0, 0, null, null, 16383);
                    }
                    Intrinsics.stringPlus("resolveState ", copy$default);
                    if (copy$default.viewInitialized && !Intrinsics.areEqual(copy$default, privacyDotViewController.currentViewState)) {
                        int i4 = copy$default.rotation;
                        ViewState viewState2 = privacyDotViewController.currentViewState;
                        Objects.requireNonNull(viewState2);
                        boolean z4 = true;
                        if (i4 != viewState2.rotation) {
                            int i5 = copy$default.rotation;
                            int i6 = copy$default.paddingTop;
                            for (View view4 : privacyDotViewController.getViews()) {
                                view4.setPadding(0, i6, 0, 0);
                                int cornerForView = privacyDotViewController.cornerForView(view4) - i5;
                                if (cornerForView < 0) {
                                    cornerForView += 4;
                                }
                                ViewGroup.LayoutParams layoutParams = view4.getLayoutParams();
                                Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
                                FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) layoutParams;
                                if (cornerForView == 0) {
                                    i3 = 51;
                                } else if (cornerForView == 1) {
                                    i3 = 53;
                                } else if (cornerForView == 2) {
                                    i3 = 85;
                                } else if (cornerForView == 3) {
                                    i3 = 83;
                                } else {
                                    throw new IllegalArgumentException("Not a corner");
                                }
                                layoutParams2.gravity = i3;
                                ViewGroup.LayoutParams layoutParams3 = view4.findViewById(2131428619).getLayoutParams();
                                Objects.requireNonNull(layoutParams3, "null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
                                FrameLayout.LayoutParams layoutParams4 = (FrameLayout.LayoutParams) layoutParams3;
                                int i7 = 19;
                                if (cornerForView != 0) {
                                    if (!(cornerForView == 1 || cornerForView == 2)) {
                                        if (cornerForView != 3) {
                                            throw new IllegalArgumentException("Not a corner");
                                        }
                                    }
                                    layoutParams4.gravity = i7;
                                }
                                i7 = 21;
                                layoutParams4.gravity = i7;
                            }
                        }
                        ViewState viewState3 = privacyDotViewController.currentViewState;
                        if (copy$default.rotation != viewState3.rotation || copy$default.layoutRtl != viewState3.layoutRtl || !Intrinsics.areEqual(copy$default.portraitRect, viewState3.portraitRect) || !Intrinsics.areEqual(copy$default.landscapeRect, viewState3.landscapeRect) || !Intrinsics.areEqual(copy$default.upsideDownRect, viewState3.upsideDownRect) || !Intrinsics.areEqual(copy$default.seascapeRect, viewState3.seascapeRect)) {
                            z = true;
                        } else {
                            z = false;
                        }
                        if (z) {
                            boolean z5 = copy$default.layoutRtl;
                            Point point = new Point();
                            View view5 = privacyDotViewController.tl;
                            if (view5 == null) {
                                view5 = null;
                            }
                            view5.getContext().getDisplay().getRealSize(point);
                            View view6 = privacyDotViewController.tl;
                            if (view6 == null) {
                                view6 = null;
                            }
                            int exactRotation = R$id.getExactRotation(view6.getContext());
                            if (exactRotation == 1 || exactRotation == 3) {
                                i2 = point.y;
                                i = point.x;
                            } else {
                                i2 = point.x;
                                i = point.y;
                            }
                            View view7 = privacyDotViewController.tl;
                            if (view7 == null) {
                                view7 = null;
                            }
                            Rect contentRectForRotation = copy$default.contentRectForRotation(privacyDotViewController.activeRotationForCorner(view7, z5));
                            View view8 = privacyDotViewController.tl;
                            if (view8 == null) {
                                view8 = null;
                            }
                            view8.setPadding(0, copy$default.paddingTop, 0, 0);
                            View view9 = privacyDotViewController.tl;
                            if (view9 == null) {
                                view9 = null;
                            }
                            ViewGroup.LayoutParams layoutParams5 = view9.getLayoutParams();
                            Objects.requireNonNull(layoutParams5, "null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
                            FrameLayout.LayoutParams layoutParams6 = (FrameLayout.LayoutParams) layoutParams5;
                            layoutParams6.height = contentRectForRotation.height();
                            if (z5) {
                                layoutParams6.width = contentRectForRotation.left;
                            } else {
                                layoutParams6.width = i - contentRectForRotation.right;
                            }
                            View view10 = privacyDotViewController.tr;
                            if (view10 == null) {
                                view10 = null;
                            }
                            Rect contentRectForRotation2 = copy$default.contentRectForRotation(privacyDotViewController.activeRotationForCorner(view10, z5));
                            View view11 = privacyDotViewController.tr;
                            if (view11 == null) {
                                view11 = null;
                            }
                            view11.setPadding(0, copy$default.paddingTop, 0, 0);
                            View view12 = privacyDotViewController.tr;
                            if (view12 == null) {
                                view12 = null;
                            }
                            ViewGroup.LayoutParams layoutParams7 = view12.getLayoutParams();
                            Objects.requireNonNull(layoutParams7, "null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
                            FrameLayout.LayoutParams layoutParams8 = (FrameLayout.LayoutParams) layoutParams7;
                            layoutParams8.height = contentRectForRotation2.height();
                            if (z5) {
                                layoutParams8.width = contentRectForRotation2.left;
                            } else {
                                layoutParams8.width = i2 - contentRectForRotation2.right;
                            }
                            View view13 = privacyDotViewController.br;
                            if (view13 == null) {
                                view13 = null;
                            }
                            Rect contentRectForRotation3 = copy$default.contentRectForRotation(privacyDotViewController.activeRotationForCorner(view13, z5));
                            View view14 = privacyDotViewController.br;
                            if (view14 == null) {
                                view14 = null;
                            }
                            view14.setPadding(0, copy$default.paddingTop, 0, 0);
                            View view15 = privacyDotViewController.br;
                            if (view15 == null) {
                                view15 = null;
                            }
                            ViewGroup.LayoutParams layoutParams9 = view15.getLayoutParams();
                            Objects.requireNonNull(layoutParams9, "null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
                            FrameLayout.LayoutParams layoutParams10 = (FrameLayout.LayoutParams) layoutParams9;
                            layoutParams10.height = contentRectForRotation3.height();
                            if (z5) {
                                layoutParams10.width = contentRectForRotation3.left;
                            } else {
                                layoutParams10.width = i - contentRectForRotation3.right;
                            }
                            View view16 = privacyDotViewController.bl;
                            if (view16 == null) {
                                view16 = null;
                            }
                            Rect contentRectForRotation4 = copy$default.contentRectForRotation(privacyDotViewController.activeRotationForCorner(view16, z5));
                            View view17 = privacyDotViewController.bl;
                            if (view17 == null) {
                                view17 = null;
                            }
                            view17.setPadding(0, copy$default.paddingTop, 0, 0);
                            View view18 = privacyDotViewController.bl;
                            if (view18 == null) {
                                view18 = null;
                            }
                            ViewGroup.LayoutParams layoutParams11 = view18.getLayoutParams();
                            Objects.requireNonNull(layoutParams11, "null cannot be cast to non-null type android.widget.FrameLayout.LayoutParams");
                            FrameLayout.LayoutParams layoutParams12 = (FrameLayout.LayoutParams) layoutParams11;
                            layoutParams12.height = contentRectForRotation4.height();
                            if (z5) {
                                layoutParams12.width = contentRectForRotation4.left;
                            } else {
                                layoutParams12.width = i2 - contentRectForRotation4.right;
                            }
                            for (View view19 : privacyDotViewController.getViews()) {
                                view19.requestLayout();
                            }
                        }
                        View view20 = copy$default.designatedCorner;
                        ViewState viewState4 = privacyDotViewController.currentViewState;
                        Objects.requireNonNull(viewState4);
                        if (!Intrinsics.areEqual(view20, viewState4.designatedCorner)) {
                            ViewState viewState5 = privacyDotViewController.currentViewState;
                            Objects.requireNonNull(viewState5);
                            View view21 = viewState5.designatedCorner;
                            if (view21 != null) {
                                view21.setContentDescription(null);
                            }
                            View view22 = copy$default.designatedCorner;
                            if (view22 != null) {
                                view22.setContentDescription(copy$default.contentDescription);
                            }
                            View view23 = copy$default.designatedCorner;
                            if (!copy$default.systemPrivacyEventIsActive || copy$default.shadeExpanded || copy$default.qsExpanded) {
                                z3 = false;
                            } else {
                                z3 = true;
                            }
                            if (z3) {
                                PrivacyDotViewController.ShowingListener showingListener = privacyDotViewController.showingListener;
                                if (showingListener != null) {
                                    ((ScreenDecorations.AnonymousClass2) showingListener).onPrivacyDotShown(view23);
                                }
                                if (view23 != null) {
                                    view23.clearAnimation();
                                    view23.setVisibility(0);
                                    view23.setAlpha(0.0f);
                                    view23.animate().alpha(1.0f).setDuration(300L).start();
                                }
                            }
                        } else {
                            String str = copy$default.contentDescription;
                            ViewState viewState6 = privacyDotViewController.currentViewState;
                            Objects.requireNonNull(viewState6);
                            if (!Intrinsics.areEqual(str, viewState6.contentDescription) && (view3 = copy$default.designatedCorner) != null) {
                                view3.setContentDescription(copy$default.contentDescription);
                            }
                        }
                        if (!copy$default.systemPrivacyEventIsActive || copy$default.shadeExpanded || copy$default.qsExpanded) {
                            z2 = false;
                        } else {
                            z2 = true;
                        }
                        ViewState viewState7 = privacyDotViewController.currentViewState;
                        Objects.requireNonNull(viewState7);
                        if (!viewState7.systemPrivacyEventIsActive || viewState7.shadeExpanded || viewState7.qsExpanded) {
                            z4 = false;
                        }
                        if (z2 != z4) {
                            if (z2 && (view2 = copy$default.designatedCorner) != null) {
                                PrivacyDotViewController.ShowingListener showingListener2 = privacyDotViewController.showingListener;
                                if (showingListener2 != null) {
                                    ((ScreenDecorations.AnonymousClass2) showingListener2).onPrivacyDotShown(view2);
                                }
                                view2.clearAnimation();
                                view2.setVisibility(0);
                                view2.setAlpha(0.0f);
                                view2.animate().alpha(1.0f).setDuration(160L).setInterpolator(Interpolators.ALPHA_IN).start();
                            } else if (!z2 && (view = copy$default.designatedCorner) != null) {
                                view.clearAnimation();
                                view.animate().setDuration(160L).setInterpolator(Interpolators.ALPHA_OUT).alpha(0.0f).withEndAction(new Runnable() { // from class: com.android.systemui.statusbar.events.PrivacyDotViewController$hideDotView$1
                                    @Override // java.lang.Runnable
                                    public final void run() {
                                        view.setVisibility(4);
                                        PrivacyDotViewController.ShowingListener showingListener3 = privacyDotViewController.showingListener;
                                        if (showingListener3 != null) {
                                            View view24 = view;
                                            ScreenDecorations.AnonymousClass2 r0 = (ScreenDecorations.AnonymousClass2) showingListener3;
                                            ScreenDecorations screenDecorations = ScreenDecorations.this;
                                            if (screenDecorations.mHwcScreenDecorationSupport != null && view24 != null) {
                                                screenDecorations.mExecutor.execute(new ScreenDecorations$2$$ExternalSyntheticLambda0(r0, view24, 0));
                                            }
                                        }
                                    }
                                }).start();
                            }
                        }
                        privacyDotViewController.currentViewState = copy$default;
                    }
                }
            }, 100L);
        }
        this.cancelRunnable = runnable;
    }

    public final void setCornerVisibilities() {
        for (View view : getViews()) {
            view.setVisibility(4);
            ShowingListener showingListener = this.showingListener;
            if (showingListener != null) {
                ScreenDecorations.AnonymousClass2 r2 = (ScreenDecorations.AnonymousClass2) showingListener;
                ScreenDecorations screenDecorations = ScreenDecorations.this;
                if (screenDecorations.mHwcScreenDecorationSupport != null) {
                    screenDecorations.mExecutor.execute(new ScreenDecorations$2$$ExternalSyntheticLambda0(r2, view, 0));
                }
            }
        }
    }
}

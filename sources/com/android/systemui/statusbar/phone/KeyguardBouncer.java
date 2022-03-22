package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Handler;
import android.util.Log;
import android.util.MathUtils;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import com.android.internal.policy.SystemBarUtils;
import com.android.keyguard.KeyguardHostView;
import com.android.keyguard.KeyguardHostViewController;
import com.android.keyguard.KeyguardInputView;
import com.android.keyguard.KeyguardSecurityContainer;
import com.android.keyguard.KeyguardSecurityContainerController;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.keyguard.KeyguardSecurityViewFlipper;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.ViewMediatorCallback;
import com.android.keyguard.dagger.KeyguardBouncerComponent;
import com.android.settingslib.Utils;
import com.android.systemui.DejankUtils;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.keyguard.DismissCallbackRegistry;
import com.android.systemui.qs.QSAnimator$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.util.Assert;
import com.android.systemui.util.ListenerSet;
import com.android.wm.shell.pip.phone.PipMenuView$$ExternalSyntheticLambda7;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class KeyguardBouncer {
    public int mBouncerPromptReason;
    public final ViewMediatorCallback mCallback;
    public final ViewGroup mContainer;
    public final Context mContext;
    public final DismissCallbackRegistry mDismissCallbackRegistry;
    public final ArrayList mExpansionCallbacks;
    public final FalsingCollector mFalsingCollector;
    public final Handler mHandler;
    public boolean mInitialized;
    public boolean mIsAnimatingAway;
    public boolean mIsScrimmed;
    public final KeyguardBouncerComponent.Factory mKeyguardBouncerComponentFactory;
    public final KeyguardBypassController mKeyguardBypassController;
    public final KeyguardSecurityModel mKeyguardSecurityModel;
    public final KeyguardStateController mKeyguardStateController;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public KeyguardHostViewController mKeyguardViewController;
    public boolean mShowingSoon;
    public int mStatusBarHeight;
    public final AnonymousClass1 mUpdateMonitorCallback;
    public final PipMenuView$$ExternalSyntheticLambda7 mRemoveViewRunnable = new PipMenuView$$ExternalSyntheticLambda7(this, 3);
    public final ListenerSet<KeyguardResetCallback> mResetCallbacks = new ListenerSet<>();
    public final QSAnimator$$ExternalSyntheticLambda0 mResetRunnable = new QSAnimator$$ExternalSyntheticLambda0(this, 4);
    public float mExpansion = 1.0f;
    public final AnonymousClass2 mShowRunnable = new Runnable() { // from class: com.android.systemui.statusbar.phone.KeyguardBouncer.2
        @Override // java.lang.Runnable
        public final void run() {
            KeyguardSecurityModel.SecurityMode securityMode = KeyguardSecurityModel.SecurityMode.None;
            KeyguardBouncer.this.setVisibility(0);
            KeyguardBouncer keyguardBouncer = KeyguardBouncer.this;
            keyguardBouncer.showPromptReason(keyguardBouncer.mBouncerPromptReason);
            CharSequence consumeCustomMessage = KeyguardBouncer.this.mCallback.consumeCustomMessage();
            if (consumeCustomMessage != null) {
                KeyguardHostViewController keyguardHostViewController = KeyguardBouncer.this.mKeyguardViewController;
                Objects.requireNonNull(keyguardHostViewController);
                ColorStateList colorAttr = Utils.getColorAttr(((KeyguardHostView) keyguardHostViewController.mView).getContext(), 16844099);
                KeyguardSecurityContainerController keyguardSecurityContainerController = keyguardHostViewController.mKeyguardSecurityContainerController;
                Objects.requireNonNull(keyguardSecurityContainerController);
                if (keyguardSecurityContainerController.mCurrentSecurityMode != securityMode) {
                    keyguardSecurityContainerController.getCurrentSecurityController().showMessage(consumeCustomMessage, colorAttr);
                }
            }
            KeyguardBouncer keyguardBouncer2 = KeyguardBouncer.this;
            final KeyguardHostViewController keyguardHostViewController2 = keyguardBouncer2.mKeyguardViewController;
            int i = keyguardBouncer2.mStatusBarHeight;
            Objects.requireNonNull(keyguardHostViewController2);
            if (((KeyguardHostView) keyguardHostViewController2.mView).getHeight() == 0 || ((KeyguardHostView) keyguardHostViewController2.mView).getHeight() == i) {
                ((KeyguardHostView) keyguardHostViewController2.mView).getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: com.android.keyguard.KeyguardHostViewController.3
                    @Override // android.view.ViewTreeObserver.OnPreDrawListener
                    public final boolean onPreDraw() {
                        ((KeyguardHostView) keyguardHostViewController2.mView).getViewTreeObserver().removeOnPreDrawListener(this);
                        KeyguardSecurityContainerController keyguardSecurityContainerController2 = keyguardHostViewController2.mKeyguardSecurityContainerController;
                        Objects.requireNonNull(keyguardSecurityContainerController2);
                        KeyguardSecurityModel.SecurityMode securityMode2 = keyguardSecurityContainerController2.mCurrentSecurityMode;
                        if (securityMode2 == KeyguardSecurityModel.SecurityMode.None) {
                            return true;
                        }
                        KeyguardSecurityContainer keyguardSecurityContainer = (KeyguardSecurityContainer) keyguardSecurityContainerController2.mView;
                        Objects.requireNonNull(keyguardSecurityContainer);
                        keyguardSecurityContainer.mViewMode.startAppearAnimation(securityMode2);
                        keyguardSecurityContainerController2.getCurrentSecurityController().startAppearAnimation();
                        return true;
                    }
                });
                ((KeyguardHostView) keyguardHostViewController2.mView).requestLayout();
            } else {
                KeyguardSecurityContainerController keyguardSecurityContainerController2 = keyguardHostViewController2.mKeyguardSecurityContainerController;
                Objects.requireNonNull(keyguardSecurityContainerController2);
                KeyguardSecurityModel.SecurityMode securityMode2 = keyguardSecurityContainerController2.mCurrentSecurityMode;
                if (securityMode2 != securityMode) {
                    KeyguardSecurityContainer keyguardSecurityContainer = (KeyguardSecurityContainer) keyguardSecurityContainerController2.mView;
                    Objects.requireNonNull(keyguardSecurityContainer);
                    keyguardSecurityContainer.mViewMode.startAppearAnimation(securityMode2);
                    keyguardSecurityContainerController2.getCurrentSecurityController().startAppearAnimation();
                }
            }
            KeyguardBouncer keyguardBouncer3 = KeyguardBouncer.this;
            keyguardBouncer3.mShowingSoon = false;
            if (keyguardBouncer3.mExpansion == 0.0f) {
                keyguardBouncer3.mKeyguardViewController.onResume();
                KeyguardBouncer.this.mKeyguardViewController.resetSecurityContainer();
                KeyguardBouncer keyguardBouncer4 = KeyguardBouncer.this;
                keyguardBouncer4.showPromptReason(keyguardBouncer4.mBouncerPromptReason);
            }
        }
    };

    /* loaded from: classes.dex */
    public interface BouncerExpansionCallback {
        default void onExpansionChanged(float f) {
        }

        void onFullyHidden();

        void onFullyShown();

        void onStartingToHide();

        void onStartingToShow();

        default void onVisibilityChanged(boolean z) {
        }
    }

    /* loaded from: classes.dex */
    public interface KeyguardResetCallback {
        void onKeyguardReset();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v0, types: [com.android.keyguard.KeyguardUpdateMonitorCallback, com.android.systemui.statusbar.phone.KeyguardBouncer$1] */
    /* JADX WARN: Type inference failed for: r4v4, types: [com.android.systemui.statusbar.phone.KeyguardBouncer$2] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public KeyguardBouncer(android.content.Context r7, com.android.keyguard.ViewMediatorCallback r8, android.view.ViewGroup r9, com.android.systemui.keyguard.DismissCallbackRegistry r10, com.android.systemui.classifier.FalsingCollector r11, com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.AnonymousClass1 r12, com.android.systemui.statusbar.policy.KeyguardStateController r13, com.android.keyguard.KeyguardUpdateMonitor r14, com.android.systemui.statusbar.phone.KeyguardBypassController r15, android.os.Handler r16, com.android.keyguard.KeyguardSecurityModel r17, com.android.keyguard.dagger.KeyguardBouncerComponent.Factory r18) {
        /*
            r6 = this;
            r0 = r6
            r1 = r14
            r6.<init>()
            java.util.ArrayList r2 = new java.util.ArrayList
            r2.<init>()
            r0.mExpansionCallbacks = r2
            com.android.systemui.statusbar.phone.KeyguardBouncer$1 r3 = new com.android.systemui.statusbar.phone.KeyguardBouncer$1
            r3.<init>()
            r0.mUpdateMonitorCallback = r3
            com.android.wm.shell.pip.phone.PipMenuView$$ExternalSyntheticLambda7 r4 = new com.android.wm.shell.pip.phone.PipMenuView$$ExternalSyntheticLambda7
            r5 = 3
            r4.<init>(r6, r5)
            r0.mRemoveViewRunnable = r4
            com.android.systemui.util.ListenerSet r4 = new com.android.systemui.util.ListenerSet
            r4.<init>()
            r0.mResetCallbacks = r4
            com.android.systemui.qs.QSAnimator$$ExternalSyntheticLambda0 r4 = new com.android.systemui.qs.QSAnimator$$ExternalSyntheticLambda0
            r5 = 4
            r4.<init>(r6, r5)
            r0.mResetRunnable = r4
            r4 = 1065353216(0x3f800000, float:1.0)
            r0.mExpansion = r4
            com.android.systemui.statusbar.phone.KeyguardBouncer$2 r4 = new com.android.systemui.statusbar.phone.KeyguardBouncer$2
            r4.<init>()
            r0.mShowRunnable = r4
            r4 = r7
            r0.mContext = r4
            r4 = r8
            r0.mCallback = r4
            r4 = r9
            r0.mContainer = r4
            r0.mKeyguardUpdateMonitor = r1
            r4 = r11
            r0.mFalsingCollector = r4
            r4 = r10
            r0.mDismissCallbackRegistry = r4
            r4 = r16
            r0.mHandler = r4
            r4 = r13
            r0.mKeyguardStateController = r4
            r4 = r17
            r0.mKeyguardSecurityModel = r4
            r4 = r18
            r0.mKeyguardBouncerComponentFactory = r4
            r14.registerCallback(r3)
            r1 = r15
            r0.mKeyguardBypassController = r1
            r0 = r12
            r2.add(r12)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.KeyguardBouncer.<init>(android.content.Context, com.android.keyguard.ViewMediatorCallback, android.view.ViewGroup, com.android.systemui.keyguard.DismissCallbackRegistry, com.android.systemui.classifier.FalsingCollector, com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager$1, com.android.systemui.statusbar.policy.KeyguardStateController, com.android.keyguard.KeyguardUpdateMonitor, com.android.systemui.statusbar.phone.KeyguardBypassController, android.os.Handler, com.android.keyguard.KeyguardSecurityModel, com.android.keyguard.dagger.KeyguardBouncerComponent$Factory):void");
    }

    /* loaded from: classes.dex */
    public static class Factory {
        public final ViewMediatorCallback mCallback;
        public final Context mContext;
        public final DismissCallbackRegistry mDismissCallbackRegistry;
        public final FalsingCollector mFalsingCollector;
        public final Handler mHandler;
        public final KeyguardBouncerComponent.Factory mKeyguardBouncerComponentFactory;
        public final KeyguardBypassController mKeyguardBypassController;
        public final KeyguardSecurityModel mKeyguardSecurityModel;
        public final KeyguardStateController mKeyguardStateController;
        public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;

        public Factory(Context context, ViewMediatorCallback viewMediatorCallback, DismissCallbackRegistry dismissCallbackRegistry, FalsingCollector falsingCollector, KeyguardStateController keyguardStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardBypassController keyguardBypassController, Handler handler, KeyguardSecurityModel keyguardSecurityModel, KeyguardBouncerComponent.Factory factory) {
            this.mContext = context;
            this.mCallback = viewMediatorCallback;
            this.mDismissCallbackRegistry = dismissCallbackRegistry;
            this.mFalsingCollector = falsingCollector;
            this.mKeyguardStateController = keyguardStateController;
            this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
            this.mKeyguardBypassController = keyguardBypassController;
            this.mHandler = handler;
            this.mKeyguardSecurityModel = keyguardSecurityModel;
            this.mKeyguardBouncerComponentFactory = factory;
        }
    }

    public final void ensureView() {
        boolean hasCallbacks = this.mHandler.hasCallbacks(this.mRemoveViewRunnable);
        if (!this.mInitialized || hasCallbacks) {
            this.mContainer.removeAllViews();
            this.mInitialized = false;
            this.mHandler.removeCallbacks(this.mRemoveViewRunnable);
            KeyguardHostViewController keyguardHostViewController = this.mKeyguardBouncerComponentFactory.create(this.mContainer).getKeyguardHostViewController();
            this.mKeyguardViewController = keyguardHostViewController;
            keyguardHostViewController.init();
            this.mStatusBarHeight = SystemBarUtils.getStatusBarHeight(this.mContext);
            setVisibility(4);
            WindowInsets rootWindowInsets = this.mContainer.getRootWindowInsets();
            if (rootWindowInsets != null) {
                this.mContainer.dispatchApplyWindowInsets(rootWindowInsets);
            }
            this.mInitialized = true;
        }
    }

    public final boolean isShowing() {
        if ((this.mShowingSoon || this.mContainer.getVisibility() == 0) && this.mExpansion == 0.0f && !this.mIsAnimatingAway) {
            return true;
        }
        return false;
    }

    public final boolean needsFullscreenBouncer() {
        KeyguardSecurityModel.SecurityMode securityMode = this.mKeyguardSecurityModel.getSecurityMode(KeyguardUpdateMonitor.getCurrentUser());
        if (securityMode == KeyguardSecurityModel.SecurityMode.SimPin || securityMode == KeyguardSecurityModel.SecurityMode.SimPuk) {
            return true;
        }
        return false;
    }

    public final void setExpansion(float f) {
        boolean z;
        String str;
        KeyguardHostView keyguardHostView;
        float f2 = this.mExpansion;
        if (f2 != f) {
            z = true;
        } else {
            z = false;
        }
        this.mExpansion = f;
        KeyguardHostViewController keyguardHostViewController = this.mKeyguardViewController;
        if (keyguardHostViewController != null && !this.mIsAnimatingAway) {
            ((KeyguardHostView) keyguardHostViewController.mView).setAlpha(MathUtils.constrain(MathUtils.map(0.95f, 1.0f, 1.0f, 0.0f, f), 0.0f, 1.0f));
            ((KeyguardHostView) keyguardHostViewController.mView).setTranslationY(keyguardHostView.getHeight() * f);
        }
        int i = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
        if (i == 0 && f2 != 0.0f) {
            this.mFalsingCollector.onBouncerShown();
            KeyguardHostViewController keyguardHostViewController2 = this.mKeyguardViewController;
            if (keyguardHostViewController2 == null) {
                Log.wtf("KeyguardBouncer", "onFullyShown when view was null");
            } else {
                keyguardHostViewController2.onResume();
                ViewGroup viewGroup = this.mContainer;
                KeyguardHostViewController keyguardHostViewController3 = this.mKeyguardViewController;
                Objects.requireNonNull(keyguardHostViewController3);
                KeyguardSecurityContainerController keyguardSecurityContainerController = keyguardHostViewController3.mKeyguardSecurityContainerController;
                Objects.requireNonNull(keyguardSecurityContainerController);
                KeyguardSecurityContainer keyguardSecurityContainer = (KeyguardSecurityContainer) keyguardSecurityContainerController.mView;
                Objects.requireNonNull(keyguardSecurityContainer);
                KeyguardSecurityViewFlipper keyguardSecurityViewFlipper = keyguardSecurityContainer.mSecurityViewFlipper;
                Objects.requireNonNull(keyguardSecurityViewFlipper);
                KeyguardInputView securityView = keyguardSecurityViewFlipper.getSecurityView();
                if (securityView != null) {
                    str = securityView.getTitle();
                } else {
                    str = "";
                }
                viewGroup.announceForAccessibility(str);
            }
            Iterator it = this.mExpansionCallbacks.iterator();
            while (it.hasNext()) {
                ((BouncerExpansionCallback) it.next()).onFullyShown();
            }
        } else if (f == 1.0f && f2 != 1.0f) {
            AnonymousClass2 r9 = this.mShowRunnable;
            boolean z2 = DejankUtils.STRICT_MODE_ENABLED;
            Assert.isMainThread();
            DejankUtils.sPendingRunnables.remove(r9);
            DejankUtils.sHandler.removeCallbacks(r9);
            this.mHandler.removeCallbacks(this.mShowRunnable);
            this.mShowingSoon = false;
            setVisibility(4);
            this.mFalsingCollector.onBouncerHidden();
            DejankUtils.postAfterTraversal(this.mResetRunnable);
            Iterator it2 = this.mExpansionCallbacks.iterator();
            while (it2.hasNext()) {
                ((BouncerExpansionCallback) it2.next()).onFullyHidden();
            }
        } else if (i != 0 && f2 == 0.0f) {
            Iterator it3 = this.mExpansionCallbacks.iterator();
            while (it3.hasNext()) {
                ((BouncerExpansionCallback) it3.next()).onStartingToHide();
            }
            KeyguardHostViewController keyguardHostViewController4 = this.mKeyguardViewController;
            if (keyguardHostViewController4 != null) {
                keyguardHostViewController4.mKeyguardSecurityContainerController.onStartingToHide();
            }
        }
        if (z) {
            Iterator it4 = this.mExpansionCallbacks.iterator();
            while (it4.hasNext()) {
                ((BouncerExpansionCallback) it4.next()).onExpansionChanged(this.mExpansion);
            }
        }
    }

    public final void setVisibility(int i) {
        boolean z;
        this.mContainer.setVisibility(i);
        Iterator it = this.mExpansionCallbacks.iterator();
        while (it.hasNext()) {
            BouncerExpansionCallback bouncerExpansionCallback = (BouncerExpansionCallback) it.next();
            if (this.mContainer.getVisibility() == 0) {
                z = true;
            } else {
                z = false;
            }
            bouncerExpansionCallback.onVisibilityChanged(z);
        }
    }

    public final void showPromptReason(int i) {
        KeyguardHostViewController keyguardHostViewController = this.mKeyguardViewController;
        if (keyguardHostViewController != null) {
            Objects.requireNonNull(keyguardHostViewController);
            KeyguardSecurityContainerController keyguardSecurityContainerController = keyguardHostViewController.mKeyguardSecurityContainerController;
            Objects.requireNonNull(keyguardSecurityContainerController);
            if (keyguardSecurityContainerController.mCurrentSecurityMode != KeyguardSecurityModel.SecurityMode.None) {
                if (i != 0) {
                    Log.i("KeyguardSecurityView", "Strong auth required, reason: " + i);
                }
                keyguardSecurityContainerController.getCurrentSecurityController().showPromptReason(i);
                return;
            }
            return;
        }
        Log.w("KeyguardBouncer", "Trying to show prompt reason on empty bouncer");
    }

    /* JADX WARN: Removed duplicated region for block: B:50:0x00e3 A[LOOP:0: B:48:0x00dd->B:50:0x00e3, LOOP_END] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void show(boolean r5, boolean r6) {
        /*
            Method dump skipped, instructions count: 238
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.KeyguardBouncer.show(boolean, boolean):void");
    }
}

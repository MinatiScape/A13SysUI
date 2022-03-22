package com.android.keyguard;

import android.opengl.EGL14;
import android.os.Trace;
import androidx.core.widget.ContentLoadingProgressBar;
import com.android.internal.widget.ViewClippingUtil;
import com.android.settingslib.mobile.MobileStatusTracker;
import com.android.systemui.ImageWallpaper;
import com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView;
import com.android.systemui.glwallpaper.EglHelper;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.screenshot.MagnifierView;
import com.android.systemui.statusbar.connectivity.MobileSignalController;
import com.android.systemui.statusbar.notification.stack.NotificationSwipeHelper;
import com.android.systemui.statusbar.phone.HeadsUpAppearanceController;
import com.android.systemui.statusbar.policy.KeyguardUserDetailItemView;
import com.android.wm.shell.bubbles.animation.ExpandedAnimationController;
import com.android.wm.shell.pip.phone.PipTouchHandler;
import com.google.android.systemui.dreamliner.DockIndicationController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class KeyguardUpdateMonitor$$ExternalSyntheticLambda6 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ KeyguardUpdateMonitor$$ExternalSyntheticLambda6(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                KeyguardUpdateMonitor keyguardUpdateMonitor = (KeyguardUpdateMonitor) this.f$0;
                Objects.requireNonNull(keyguardUpdateMonitor);
                keyguardUpdateMonitor.updateBiometricListeningState(2);
                return;
            case 1:
                ContentLoadingProgressBar contentLoadingProgressBar = (ContentLoadingProgressBar) this.f$0;
                int i = ContentLoadingProgressBar.$r8$clinit;
                Objects.requireNonNull(contentLoadingProgressBar);
                contentLoadingProgressBar.setVisibility(8);
                return;
            case 2:
                MobileStatusTracker mobileStatusTracker = (MobileStatusTracker) this.f$0;
                Objects.requireNonNull(mobileStatusTracker);
                ((MobileSignalController.AnonymousClass1) mobileStatusTracker.mCallback).onMobileStatusChanged(false, new MobileStatusTracker.MobileStatus(mobileStatusTracker.mMobileStatus));
                return;
            case 3:
                ImageWallpaper.GLEngine gLEngine = (ImageWallpaper.GLEngine) this.f$0;
                int i2 = ImageWallpaper.GLEngine.MIN_SURFACE_WIDTH;
                Objects.requireNonNull(gLEngine);
                Trace.beginSection("ImageWallpaper#finishRendering");
                EglHelper eglHelper = gLEngine.mEglHelper;
                if (eglHelper != null) {
                    eglHelper.destroyEglSurface();
                    EglHelper eglHelper2 = gLEngine.mEglHelper;
                    Objects.requireNonNull(eglHelper2);
                    if (eglHelper2.hasEglContext()) {
                        EGL14.eglDestroyContext(eglHelper2.mEglDisplay, eglHelper2.mEglContext);
                        eglHelper2.mEglContext = EGL14.EGL_NO_CONTEXT;
                    }
                }
                Trace.endSection();
                return;
            case 4:
                AccessibilityFloatingMenuView accessibilityFloatingMenuView = (AccessibilityFloatingMenuView) this.f$0;
                int i3 = AccessibilityFloatingMenuView.$r8$clinit;
                Objects.requireNonNull(accessibilityFloatingMenuView);
                accessibilityFloatingMenuView.setAlpha(1.0f);
                return;
            case 5:
                MagnifierView magnifierView = (MagnifierView) this.f$0;
                int i4 = MagnifierView.$r8$clinit;
                Objects.requireNonNull(magnifierView);
                magnifierView.setVisibility(4);
                return;
            case FalsingManager.VERSION /* 6 */:
                NotificationSwipeHelper notificationSwipeHelper = (NotificationSwipeHelper) this.f$0;
                Objects.requireNonNull(notificationSwipeHelper);
                notificationSwipeHelper.resetExposedMenuView(true, true);
                return;
            case 7:
                HeadsUpAppearanceController headsUpAppearanceController = (HeadsUpAppearanceController) this.f$0;
                Objects.requireNonNull(headsUpAppearanceController);
                ViewClippingUtil.setClippingDeactivated(headsUpAppearanceController.mView, false, headsUpAppearanceController.mParentClippingParams);
                return;
            case 8:
                KeyguardUserDetailItemView keyguardUserDetailItemView = (KeyguardUserDetailItemView) this.f$0;
                boolean z = KeyguardUserDetailItemView.DEBUG;
                Objects.requireNonNull(keyguardUserDetailItemView);
                keyguardUserDetailItemView.mName.setVisibility(8);
                keyguardUserDetailItemView.mName.setAlpha(1.0f);
                return;
            case 9:
                ExpandedAnimationController expandedAnimationController = (ExpandedAnimationController) this.f$0;
                Objects.requireNonNull(expandedAnimationController);
                expandedAnimationController.mAnimatingCollapse = false;
                Runnable runnable = expandedAnimationController.mAfterCollapse;
                if (runnable != null) {
                    runnable.run();
                }
                expandedAnimationController.mAfterCollapse = null;
                return;
            case 10:
                ((PipTouchHandler) this.f$0).animateToUnStashedState();
                return;
            default:
                DockIndicationController dockIndicationController = (DockIndicationController) this.f$0;
                String str = DockIndicationController.ACTION_ASSISTANT_POODLE;
                Objects.requireNonNull(dockIndicationController);
                if (dockIndicationController.mDocking && dockIndicationController.mDozing) {
                    dockIndicationController.mTopIndicationView.setAccessibilityLiveRegion(0);
                    return;
                }
                return;
        }
    }
}

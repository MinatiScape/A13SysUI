package com.android.keyguard;

import com.android.systemui.qrcodescanner.controller.QRCodeScannerController;
import com.android.systemui.qs.tiles.dialog.InternetDialog;
import com.android.systemui.screenshot.ScreenshotEvent;
import com.android.systemui.screenshot.ScreenshotView;
import com.android.systemui.screenshot.SwipeDismissHandler;
import com.android.systemui.statusbar.KeyguardIndicationController;
import com.android.systemui.statusbar.connectivity.NetworkControllerImpl;
import com.android.systemui.statusbar.notification.stack.NotificationChildrenContainer;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.StatusBar;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class KeyguardVisibilityHelper$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ KeyguardVisibilityHelper$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                KeyguardVisibilityHelper keyguardVisibilityHelper = (KeyguardVisibilityHelper) this.f$0;
                Objects.requireNonNull(keyguardVisibilityHelper);
                keyguardVisibilityHelper.mKeyguardViewVisibilityAnimating = false;
                return;
            case 1:
                QRCodeScannerController.AnonymousClass1 r4 = (QRCodeScannerController.AnonymousClass1) this.f$0;
                int i = QRCodeScannerController.AnonymousClass1.$r8$clinit;
                Objects.requireNonNull(r4);
                QRCodeScannerController.this.updateQRCodeScannerPreferenceDetails(false);
                return;
            case 2:
                InternetDialog internetDialog = (InternetDialog) this.f$0;
                boolean z = InternetDialog.DEBUG;
                Objects.requireNonNull(internetDialog);
                internetDialog.updateDialog(true);
                return;
            case 3:
                ScreenshotView screenshotView = (ScreenshotView) this.f$0;
                int i2 = ScreenshotView.$r8$clinit;
                Objects.requireNonNull(screenshotView);
                screenshotView.mUiEventLogger.log(ScreenshotEvent.SCREENSHOT_SMART_ACTION_TAPPED, 0, screenshotView.mPackageName);
                SwipeDismissHandler swipeDismissHandler = screenshotView.mSwipeDismissHandler;
                Objects.requireNonNull(swipeDismissHandler);
                swipeDismissHandler.dismiss(1.0f);
                return;
            case 4:
                NetworkControllerImpl networkControllerImpl = (NetworkControllerImpl) this.f$0;
                boolean z2 = NetworkControllerImpl.DEBUG;
                Objects.requireNonNull(networkControllerImpl);
                networkControllerImpl.registerListeners();
                return;
            case 5:
                NotificationChildrenContainer notificationChildrenContainer = (NotificationChildrenContainer) this.f$0;
                int i3 = NotificationChildrenContainer.NUMBER_OF_CHILDREN_WHEN_COLLAPSED;
                Objects.requireNonNull(notificationChildrenContainer);
                notificationChildrenContainer.updateHeaderVisibility(false);
                return;
            default:
                NotificationPanelViewController.KeyguardAffordanceHelperCallback keyguardAffordanceHelperCallback = (NotificationPanelViewController.KeyguardAffordanceHelperCallback) this.f$0;
                Objects.requireNonNull(keyguardAffordanceHelperCallback);
                NotificationPanelViewController notificationPanelViewController = NotificationPanelViewController.this;
                notificationPanelViewController.mHintAnimationRunning = false;
                StatusBar statusBar = notificationPanelViewController.mStatusBar;
                Objects.requireNonNull(statusBar);
                KeyguardIndicationController keyguardIndicationController = statusBar.mKeyguardIndicationController;
                Objects.requireNonNull(keyguardIndicationController);
                KeyguardIndicationController.AnonymousClass5 r42 = keyguardIndicationController.mHandler;
                r42.sendMessageDelayed(r42.obtainMessage(1), 1200L);
                return;
        }
    }
}

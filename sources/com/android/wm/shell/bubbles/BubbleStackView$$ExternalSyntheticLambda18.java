package com.android.wm.shell.bubbles;

import com.android.systemui.clipboardoverlay.ClipboardListener;
import com.android.systemui.qs.tiles.dialog.InternetDialog;
import com.android.systemui.statusbar.KeyguardIndicationController;
import com.android.systemui.statusbar.notification.InstantAppNotifier;
import com.android.systemui.statusbar.phone.AutoTileManager;
import com.android.systemui.statusbar.phone.ShadeController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BubbleStackView$$ExternalSyntheticLambda18 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ BubbleStackView$$ExternalSyntheticLambda18(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        boolean z;
        switch (this.$r8$classId) {
            case 0:
                BubbleStackView bubbleStackView = (BubbleStackView) this.f$0;
                Objects.requireNonNull(bubbleStackView);
                int bubbleCount = bubbleStackView.getBubbleCount();
                for (int i = 0; i < bubbleCount; i++) {
                    BadgedImageView badgedImageView = (BadgedImageView) bubbleStackView.mBubbleContainer.getChildAt(i);
                    if (i < 2) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (!z) {
                        badgedImageView.animate().translationZ(0.0f).start();
                    }
                }
                return;
            case 1:
                ClipboardListener clipboardListener = (ClipboardListener) this.f$0;
                Objects.requireNonNull(clipboardListener);
                clipboardListener.mClipboardOverlayController = null;
                return;
            case 2:
                InternetDialog internetDialog = (InternetDialog) this.f$0;
                boolean z2 = InternetDialog.DEBUG;
                Objects.requireNonNull(internetDialog);
                internetDialog.updateDialog(true);
                return;
            case 3:
                KeyguardIndicationController keyguardIndicationController = (KeyguardIndicationController) this.f$0;
                Objects.requireNonNull(keyguardIndicationController);
                keyguardIndicationController.mWakeLock.setAcquired(false);
                return;
            case 4:
                InstantAppNotifier.AnonymousClass1 r6 = (InstantAppNotifier.AnonymousClass1) this.f$0;
                int i2 = InstantAppNotifier.AnonymousClass1.$r8$clinit;
                Objects.requireNonNull(r6);
                InstantAppNotifier.this.updateForegroundInstantApps();
                return;
            case 5:
                AutoTileManager.AnonymousClass7 r62 = (AutoTileManager.AnonymousClass7) this.f$0;
                Objects.requireNonNull(r62);
                AutoTileManager autoTileManager = AutoTileManager.this;
                autoTileManager.mCastController.removeCallback(autoTileManager.mCastCallback);
                return;
            default:
                ((ShadeController) this.f$0).animateCollapsePanels();
                return;
        }
    }
}

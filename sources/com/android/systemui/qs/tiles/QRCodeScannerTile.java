package com.android.systemui.qs.tiles;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.animation.GhostedViewLaunchAnimatorController;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qrcodescanner.controller.QRCodeScannerController;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import java.util.Objects;
/* loaded from: classes.dex */
public final class QRCodeScannerTile extends QSTileImpl<QSTile.State> {
    public final AnonymousClass1 mCallback;
    public final String mLabel = this.mContext.getString(2131953042);
    public final QRCodeScannerController mQRCodeScannerController;

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final Intent getLongClickIntent() {
        return null;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final int getMetricsCategory() {
        return 0;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleClick(View view) {
        GhostedViewLaunchAnimatorController ghostedViewLaunchAnimatorController;
        QRCodeScannerController qRCodeScannerController = this.mQRCodeScannerController;
        Objects.requireNonNull(qRCodeScannerController);
        Intent intent = qRCodeScannerController.mIntent;
        if (intent == null) {
            Log.e("QRCodeScanner", "Expected a non-null intent");
            return;
        }
        if (view == null) {
            ghostedViewLaunchAnimatorController = null;
        } else {
            ghostedViewLaunchAnimatorController = ActivityLaunchAnimator.Controller.fromView(view, 32);
        }
        this.mActivityStarter.startActivity(intent, true, (ActivityLaunchAnimator.Controller) ghostedViewLaunchAnimatorController, true);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleInitialize() {
        this.mQRCodeScannerController.registerQRCodeScannerChangeObservers(0);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleUpdateState(QSTile.State state, Object obj) {
        boolean z;
        String string = this.mContext.getString(2131953042);
        state.label = string;
        state.contentDescription = string;
        state.icon = QSTileImpl.ResourceIcon.get(2131232216);
        QRCodeScannerController qRCodeScannerController = this.mQRCodeScannerController;
        Objects.requireNonNull(qRCodeScannerController);
        int i = 0;
        if (qRCodeScannerController.mIntent != null) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            i = 2;
        }
        state.state = i;
        state.secondaryLabel = this.mContext.getString(2131953041);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final boolean isAvailable() {
        return this.mQRCodeScannerController.isCameraAvailable();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final QSTile.State newTileState() {
        QSTile.State state = new QSTile.State();
        state.handlesLongClick = false;
        return state;
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [java.lang.Object, com.android.systemui.qs.tiles.QRCodeScannerTile$1] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public QRCodeScannerTile(com.android.systemui.qs.QSHost r1, android.os.Looper r2, android.os.Handler r3, com.android.systemui.plugins.FalsingManager r4, com.android.internal.logging.MetricsLogger r5, com.android.systemui.plugins.statusbar.StatusBarStateController r6, com.android.systemui.plugins.ActivityStarter r7, com.android.systemui.qs.logging.QSLogger r8, com.android.systemui.qrcodescanner.controller.QRCodeScannerController r9) {
        /*
            r0 = this;
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8)
            android.content.Context r1 = r0.mContext
            r2 = 2131953042(0x7f130592, float:1.9542544E38)
            java.lang.String r1 = r1.getString(r2)
            r0.mLabel = r1
            com.android.systemui.qs.tiles.QRCodeScannerTile$1 r1 = new com.android.systemui.qs.tiles.QRCodeScannerTile$1
            r1.<init>()
            r0.mCallback = r1
            r0.mQRCodeScannerController = r9
            androidx.lifecycle.LifecycleRegistry r0 = r0.mLifecycle
            r9.observe(r0, r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.tiles.QRCodeScannerTile.<init>(com.android.systemui.qs.QSHost, android.os.Looper, android.os.Handler, com.android.systemui.plugins.FalsingManager, com.android.internal.logging.MetricsLogger, com.android.systemui.plugins.statusbar.StatusBarStateController, com.android.systemui.plugins.ActivityStarter, com.android.systemui.qs.logging.QSLogger, com.android.systemui.qrcodescanner.controller.QRCodeScannerController):void");
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleDestroy() {
        super.handleDestroy();
        this.mQRCodeScannerController.unregisterQRCodeScannerChangeObservers(0);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final CharSequence getTileLabel() {
        return this.mLabel;
    }
}

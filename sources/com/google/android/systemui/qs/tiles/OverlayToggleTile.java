package com.google.android.systemui.qs.tiles;

import android.content.Intent;
import android.content.om.OverlayInfo;
import android.content.om.OverlayManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.os.UserHandle;
import android.util.Slog;
import android.view.View;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.theme.ThemeOverlayApplier;
import java.util.Iterator;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: OverlayToggleTile.kt */
/* loaded from: classes.dex */
public final class OverlayToggleTile extends QSTileImpl<QSTile.BooleanState> {
    public final OverlayManager om;
    public CharSequence overlayLabel;
    public String overlayPackage;

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final Intent getLongClickIntent() {
        return null;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final int getMetricsCategory() {
        return -1;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final CharSequence getTileLabel() {
        return "Overlay";
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleLongClick(View view) {
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleClick(View view) {
        QSTile.BooleanState booleanState;
        boolean z;
        String str = this.overlayPackage;
        if (str != null && (booleanState = (QSTile.BooleanState) this.mState) != null) {
            if (booleanState.state != 2) {
                z = true;
            } else {
                z = false;
            }
            String str2 = this.TAG;
            Slog.v(str2, "Setting enable state of " + str + " to " + z);
            this.om.setEnabled(str, z, UserHandle.CURRENT);
            refreshState("Restarting...");
            Thread.sleep(250L);
            Slog.v(this.TAG, "Restarting System UI to react to overlay changes");
            Process.killProcess(Process.myPid());
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        int i;
        Object obj2;
        String str;
        QSTile.BooleanState booleanState2 = booleanState;
        PackageManager packageManager = this.mContext.getPackageManager();
        booleanState2.state = 0;
        booleanState2.label = "No overlay";
        List overlayInfosForTarget = this.om.getOverlayInfosForTarget(ThemeOverlayApplier.SYSUI_PACKAGE, UserHandle.CURRENT);
        if (overlayInfosForTarget != null) {
            Iterator it = overlayInfosForTarget.iterator();
            do {
                i = 2;
                obj2 = null;
                if (!it.hasNext()) {
                    break;
                }
                obj2 = it.next();
            } while (!((OverlayInfo) obj2).packageName.startsWith("com.google."));
            OverlayInfo overlayInfo = (OverlayInfo) obj2;
            if (overlayInfo != null) {
                if (!Intrinsics.areEqual(this.overlayPackage, overlayInfo.packageName)) {
                    String str2 = overlayInfo.packageName;
                    this.overlayPackage = str2;
                    this.overlayLabel = packageManager.getPackageInfo(str2, 0).applicationInfo.loadLabel(packageManager);
                }
                booleanState2.value = overlayInfo.isEnabled();
                if (!overlayInfo.isEnabled()) {
                    i = 1;
                }
                booleanState2.state = i;
                booleanState2.icon = QSTileImpl.ResourceIcon.get(17303574);
                booleanState2.label = this.overlayLabel;
                if (obj != null) {
                    str = String.valueOf(obj);
                } else if (overlayInfo.isEnabled()) {
                    str = "Enabled";
                } else {
                    str = "Disabled";
                }
                booleanState2.secondaryLabel = str;
            }
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    public OverlayToggleTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, OverlayManager overlayManager) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        this.om = overlayManager;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final boolean isAvailable() {
        return Build.IS_DEBUGGABLE;
    }
}

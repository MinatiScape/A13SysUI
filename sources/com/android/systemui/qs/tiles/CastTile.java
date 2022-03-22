package com.android.systemui.qs.tiles;

import android.app.Dialog;
import android.content.Intent;
import android.media.MediaRouter;
import android.view.View;
import android.widget.Button;
import androidx.core.view.ViewCompat$$ExternalSyntheticLambda0;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.statusbar.policy.CastController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.wm.shell.TaskView$$ExternalSyntheticLambda4;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class CastTile extends QSTileImpl<QSTile.BooleanState> {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final CastController mController;
    public final DialogLaunchAnimator mDialogLaunchAnimator;
    public final AnonymousClass2 mHotspotCallback;
    public boolean mHotspotConnected;
    public final KeyguardStateController mKeyguard;
    public final NetworkController mNetworkController;
    public final AnonymousClass1 mSignalCallback;
    public boolean mWifiConnected;

    /* loaded from: classes.dex */
    public final class Callback implements CastController.Callback, KeyguardStateController.Callback {
        public Callback() {
        }

        @Override // com.android.systemui.statusbar.policy.CastController.Callback
        public final void onCastDevicesChanged() {
            CastTile castTile = CastTile.this;
            Objects.requireNonNull(castTile);
            castTile.refreshState(null);
        }

        @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
        public final void onKeyguardShowingChanged() {
            CastTile castTile = CastTile.this;
            Objects.requireNonNull(castTile);
            castTile.refreshState(null);
        }
    }

    /* loaded from: classes.dex */
    public static class DialogHolder {
        public Dialog mDialog;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final int getMetricsCategory() {
        return 114;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleUserSwitch(int i) {
        handleRefreshState(null);
        this.mController.setCurrentUserId(i);
    }

    static {
        new Intent("android.settings.CAST_SETTINGS");
    }

    public final ArrayList getActiveDevices() {
        ArrayList arrayList = new ArrayList();
        for (CastController.CastDevice castDevice : this.mController.getCastDevices()) {
            int i = castDevice.state;
            if (i == 2 || i == 1) {
                arrayList.add(castDevice);
            }
        }
        return arrayList;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final Intent getLongClickIntent() {
        return new Intent("android.settings.CAST_SETTINGS");
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final CharSequence getTileLabel() {
        return this.mContext.getString(2131953069);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleClick(View view) {
        boolean z;
        if (((QSTile.BooleanState) this.mState).state != 0) {
            ArrayList activeDevices = getActiveDevices();
            ArrayList activeDevices2 = getActiveDevices();
            if (activeDevices2.isEmpty() || (((CastController.CastDevice) activeDevices2.get(0)).tag instanceof MediaRouter.RouteInfo)) {
                z = true;
            } else {
                z = false;
            }
            if (!z) {
                this.mController.stopCasting((CastController.CastDevice) activeDevices.get(0));
            } else if (!this.mKeyguard.isShowing()) {
                this.mUiHandler.post(new CastTile$$ExternalSyntheticLambda1(this, view, 0));
            } else {
                this.mActivityStarter.postQSRunnableDismissingKeyguard(new TaskView$$ExternalSyntheticLambda4(this, 7));
            }
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        int i;
        int i2;
        boolean z;
        QSTile.BooleanState booleanState2 = booleanState;
        String string = this.mContext.getString(2131953069);
        booleanState2.label = string;
        booleanState2.contentDescription = string;
        booleanState2.stateDescription = "";
        boolean z2 = false;
        booleanState2.value = false;
        Iterator it = this.mController.getCastDevices().iterator();
        boolean z3 = false;
        while (true) {
            i = 2;
            if (!it.hasNext()) {
                break;
            }
            CastController.CastDevice castDevice = (CastController.CastDevice) it.next();
            int i3 = castDevice.state;
            if (i3 == 2) {
                booleanState2.value = true;
                String str = castDevice.name;
                if (str == null) {
                    str = this.mContext.getString(2131953067);
                }
                booleanState2.secondaryLabel = str;
                booleanState2.stateDescription = ((Object) booleanState2.stateDescription) + "," + this.mContext.getString(2131951686, booleanState2.label);
                z3 = false;
            } else if (i3 == 1) {
                z3 = true;
            }
        }
        if (z3 && !booleanState2.value) {
            booleanState2.secondaryLabel = this.mContext.getString(2131953082);
        }
        if (booleanState2.value) {
            i2 = 2131231779;
        } else {
            i2 = 2131231778;
        }
        booleanState2.icon = QSTileImpl.ResourceIcon.get(i2);
        if (this.mWifiConnected || this.mHotspotConnected) {
            z = true;
        } else {
            z = false;
        }
        if (z || booleanState2.value) {
            boolean z4 = booleanState2.value;
            if (!z4) {
                i = 1;
            }
            booleanState2.state = i;
            if (!z4) {
                booleanState2.secondaryLabel = "";
            }
            booleanState2.expandedAccessibilityClassName = Button.class.getName();
            ArrayList activeDevices = getActiveDevices();
            if (activeDevices.isEmpty() || (((CastController.CastDevice) activeDevices.get(0)).tag instanceof MediaRouter.RouteInfo)) {
                z2 = true;
            }
            booleanState2.forceExpandIcon = z2;
        } else {
            booleanState2.state = 0;
            booleanState2.secondaryLabel = this.mContext.getString(2131953068);
            booleanState2.forceExpandIcon = false;
        }
        booleanState2.stateDescription = ((Object) booleanState2.stateDescription) + ", " + ((Object) booleanState2.secondaryLabel);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final QSTile.BooleanState newTileState() {
        QSTile.BooleanState booleanState = new QSTile.BooleanState();
        booleanState.handlesLongClick = false;
        return booleanState;
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [com.android.systemui.qs.tiles.CastTile$1, java.lang.Object] */
    /* JADX WARN: Type inference failed for: r3v1, types: [com.android.systemui.qs.tiles.CastTile$2, java.lang.Object] */
    /* JADX WARN: Unknown variable types count: 2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public CastTile(com.android.systemui.qs.QSHost r1, android.os.Looper r2, android.os.Handler r3, com.android.systemui.plugins.FalsingManager r4, com.android.internal.logging.MetricsLogger r5, com.android.systemui.plugins.statusbar.StatusBarStateController r6, com.android.systemui.plugins.ActivityStarter r7, com.android.systemui.qs.logging.QSLogger r8, com.android.systemui.statusbar.policy.CastController r9, com.android.systemui.statusbar.policy.KeyguardStateController r10, com.android.systemui.statusbar.connectivity.NetworkController r11, com.android.systemui.statusbar.policy.HotspotController r12, com.android.systemui.animation.DialogLaunchAnimator r13) {
        /*
            r0 = this;
            r0.<init>(r1, r2, r3, r4, r5, r6, r7, r8)
            com.android.systemui.qs.tiles.CastTile$Callback r1 = new com.android.systemui.qs.tiles.CastTile$Callback
            r1.<init>()
            com.android.systemui.qs.tiles.CastTile$1 r2 = new com.android.systemui.qs.tiles.CastTile$1
            r2.<init>()
            r0.mSignalCallback = r2
            com.android.systemui.qs.tiles.CastTile$2 r3 = new com.android.systemui.qs.tiles.CastTile$2
            r3.<init>()
            r0.mHotspotCallback = r3
            r0.mController = r9
            r0.mKeyguard = r10
            r0.mNetworkController = r11
            r0.mDialogLaunchAnimator = r13
            r9.observe(r0, r1)
            r10.observe(r0, r1)
            r11.observe(r0, r2)
            r12.observe(r0, r3)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.tiles.CastTile.<init>(com.android.systemui.qs.QSHost, android.os.Looper, android.os.Handler, com.android.systemui.plugins.FalsingManager, com.android.internal.logging.MetricsLogger, com.android.systemui.plugins.statusbar.StatusBarStateController, com.android.systemui.plugins.ActivityStarter, com.android.systemui.qs.logging.QSLogger, com.android.systemui.statusbar.policy.CastController, com.android.systemui.statusbar.policy.KeyguardStateController, com.android.systemui.statusbar.connectivity.NetworkController, com.android.systemui.statusbar.policy.HotspotController, com.android.systemui.animation.DialogLaunchAnimator):void");
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleSetListening(boolean z) {
        super.handleSetListening(z);
        if (QSTileImpl.DEBUG) {
            ViewCompat$$ExternalSyntheticLambda0.m("handleSetListening ", z, this.TAG);
        }
        if (!z) {
            this.mController.setDiscovering();
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleLongClick(View view) {
        handleClick(view);
    }
}

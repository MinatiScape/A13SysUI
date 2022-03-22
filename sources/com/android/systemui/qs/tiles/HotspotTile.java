package com.android.systemui.qs.tiles;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.policy.DataSaverController;
import com.android.systemui.statusbar.policy.HotspotController;
/* loaded from: classes.dex */
public final class HotspotTile extends QSTileImpl<QSTile.BooleanState> {
    public final DataSaverController mDataSaverController;
    public final QSTile.Icon mEnabledStatic = QSTileImpl.ResourceIcon.get(2131232006);
    public final HotspotController mHotspotController;
    public boolean mListening;

    /* loaded from: classes.dex */
    public static final class CallbackInfo {
        public boolean isDataSaverEnabled;
        public boolean isHotspotEnabled;
        public int numConnectedDevices;

        public final String toString() {
            StringBuilder sb = new StringBuilder("CallbackInfo[");
            sb.append("isHotspotEnabled=");
            sb.append(this.isHotspotEnabled);
            sb.append(",numConnectedDevices=");
            sb.append(this.numConnectedDevices);
            sb.append(",isDataSaverEnabled=");
            return LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0.m(sb, this.isDataSaverEnabled, ']');
        }
    }

    /* loaded from: classes.dex */
    public final class HotspotAndDataSaverCallbacks implements HotspotController.Callback, DataSaverController.Listener {
        public CallbackInfo mCallbackInfo = new CallbackInfo();

        public HotspotAndDataSaverCallbacks() {
        }

        @Override // com.android.systemui.statusbar.policy.DataSaverController.Listener
        public final void onDataSaverChanged(boolean z) {
            CallbackInfo callbackInfo = this.mCallbackInfo;
            callbackInfo.isDataSaverEnabled = z;
            HotspotTile.this.refreshState(callbackInfo);
        }

        @Override // com.android.systemui.statusbar.policy.HotspotController.Callback
        public final void onHotspotAvailabilityChanged(boolean z) {
            if (!z) {
                Log.d(HotspotTile.this.TAG, "Tile removed. Hotspot no longer available");
                HotspotTile hotspotTile = HotspotTile.this;
                hotspotTile.mHost.removeTile(hotspotTile.mTileSpec);
            }
        }

        @Override // com.android.systemui.statusbar.policy.HotspotController.Callback
        public final void onHotspotChanged(boolean z, int i) {
            CallbackInfo callbackInfo = this.mCallbackInfo;
            callbackInfo.isHotspotEnabled = z;
            callbackInfo.numConnectedDevices = i;
            HotspotTile.this.refreshState(callbackInfo);
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final int getMetricsCategory() {
        return 120;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final Intent getLongClickIntent() {
        return new Intent("com.android.settings.WIFI_TETHER_SETTINGS");
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final CharSequence getTileLabel() {
        return this.mContext.getString(2131953112);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleClick(View view) {
        Object obj;
        boolean z = ((QSTile.BooleanState) this.mState).value;
        if (z || !this.mDataSaverController.isDataSaverEnabled()) {
            if (z) {
                obj = null;
            } else {
                obj = QSTileImpl.ARG_SHOW_TRANSIENT_ENABLING;
            }
            refreshState(obj);
            this.mHotspotController.setHotspotEnabled(!z);
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        boolean z;
        boolean z2;
        boolean z3;
        int i;
        boolean z4;
        boolean z5;
        String str;
        int i2;
        boolean z6;
        boolean z7;
        QSTile.BooleanState booleanState2 = booleanState;
        if (obj == QSTileImpl.ARG_SHOW_TRANSIENT_ENABLING) {
            z = true;
        } else {
            z = false;
        }
        if (booleanState2.slash == null) {
            booleanState2.slash = new QSTile.SlashState();
        }
        if (z || this.mHotspotController.isHotspotTransient()) {
            z2 = true;
        } else {
            z2 = false;
        }
        checkIfRestrictionEnforcedByAdminOnly(booleanState2, "no_config_tethering");
        if (obj instanceof CallbackInfo) {
            CallbackInfo callbackInfo = (CallbackInfo) obj;
            if (z || callbackInfo.isHotspotEnabled) {
                z7 = true;
            } else {
                z7 = false;
            }
            booleanState2.value = z7;
            i = callbackInfo.numConnectedDevices;
            z3 = callbackInfo.isDataSaverEnabled;
        } else {
            if (z || this.mHotspotController.isHotspotEnabled()) {
                z6 = true;
            } else {
                z6 = false;
            }
            booleanState2.value = z6;
            i = this.mHotspotController.getNumConnectedDevices();
            z3 = this.mDataSaverController.isDataSaverEnabled();
        }
        booleanState2.icon = this.mEnabledStatic;
        booleanState2.label = this.mContext.getString(2131953112);
        booleanState2.isTransient = z2;
        QSTile.SlashState slashState = booleanState2.slash;
        if (booleanState2.value || z2) {
            z4 = false;
        } else {
            z4 = true;
        }
        slashState.isSlashed = z4;
        if (z2) {
            booleanState2.icon = QSTileImpl.ResourceIcon.get(17302461);
        }
        booleanState2.expandedAccessibilityClassName = Switch.class.getName();
        booleanState2.contentDescription = booleanState2.label;
        if (booleanState2.value || booleanState2.isTransient) {
            z5 = true;
        } else {
            z5 = false;
        }
        if (z3) {
            booleanState2.state = 0;
        } else {
            if (z5) {
                i2 = 2;
            } else {
                i2 = 1;
            }
            booleanState2.state = i2;
        }
        if (z2) {
            str = this.mContext.getString(2131953114);
        } else if (z3) {
            str = this.mContext.getString(2131953113);
        } else if (i <= 0 || !z5) {
            str = null;
        } else {
            str = this.mContext.getResources().getQuantityString(2131820555, i, Integer.valueOf(i));
        }
        booleanState2.secondaryLabel = str;
        booleanState2.stateDescription = str;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final boolean isAvailable() {
        return this.mHotspotController.isHotspotSupported();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    public HotspotTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, HotspotController hotspotController, DataSaverController dataSaverController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        HotspotAndDataSaverCallbacks hotspotAndDataSaverCallbacks = new HotspotAndDataSaverCallbacks();
        this.mHotspotController = hotspotController;
        this.mDataSaverController = dataSaverController;
        hotspotController.observe((LifecycleOwner) this, (HotspotTile) hotspotAndDataSaverCallbacks);
        dataSaverController.observe((LifecycleOwner) this, (HotspotTile) hotspotAndDataSaverCallbacks);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleSetListening(boolean z) {
        super.handleSetListening(z);
        if (this.mListening != z) {
            this.mListening = z;
            if (z) {
                refreshState(null);
            }
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleDestroy() {
        super.handleDestroy();
    }
}

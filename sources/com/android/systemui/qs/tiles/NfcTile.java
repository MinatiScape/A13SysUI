package com.android.systemui.qs.tiles;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Switch;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import java.util.Objects;
/* loaded from: classes.dex */
public final class NfcTile extends QSTileImpl<QSTile.BooleanState> {
    public NfcAdapter mAdapter;
    public BroadcastDispatcher mBroadcastDispatcher;
    public final QSTile.Icon mIcon = QSTileImpl.ResourceIcon.get(2131232226);
    public AnonymousClass1 mNfcReceiver = new BroadcastReceiver() { // from class: com.android.systemui.qs.tiles.NfcTile.1
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            NfcTile nfcTile = NfcTile.this;
            Objects.requireNonNull(nfcTile);
            nfcTile.refreshState(null);
        }
    };

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final int getMetricsCategory() {
        return 800;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleUserSwitch(int i) {
    }

    public final NfcAdapter getAdapter() {
        if (this.mAdapter == null) {
            try {
                this.mAdapter = NfcAdapter.getDefaultAdapter(this.mContext);
            } catch (UnsupportedOperationException unused) {
                this.mAdapter = null;
            }
        }
        return this.mAdapter;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final Intent getLongClickIntent() {
        return new Intent("android.settings.NFC_SETTINGS");
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final CharSequence getTileLabel() {
        return this.mContext.getString(2131953123);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        boolean z;
        QSTile.BooleanState booleanState2 = booleanState;
        int i = 1;
        if (getAdapter() == null || !getAdapter().isEnabled()) {
            z = false;
        } else {
            z = true;
        }
        booleanState2.value = z;
        if (getAdapter() == null) {
            i = 0;
        } else if (booleanState2.value) {
            i = 2;
        }
        booleanState2.state = i;
        booleanState2.icon = this.mIcon;
        booleanState2.label = this.mContext.getString(2131953123);
        booleanState2.expandedAccessibilityClassName = Switch.class.getName();
        booleanState2.contentDescription = booleanState2.label;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final boolean isAvailable() {
        if (this.mContext.getString(2131953141).contains("nfc")) {
            return this.mContext.getPackageManager().hasSystemFeature("android.hardware.nfc");
        }
        return false;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [com.android.systemui.qs.tiles.NfcTile$1] */
    public NfcTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, BroadcastDispatcher broadcastDispatcher) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        this.mBroadcastDispatcher = broadcastDispatcher;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleClick(View view) {
        if (getAdapter() != null) {
            if (!getAdapter().isEnabled()) {
                getAdapter().enable();
            } else {
                getAdapter().disable();
            }
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleSetListening(boolean z) {
        super.handleSetListening(z);
        if (z) {
            this.mBroadcastDispatcher.registerReceiver(this.mNfcReceiver, new IntentFilter("android.nfc.action.ADAPTER_STATE_CHANGED"));
        } else {
            this.mBroadcastDispatcher.unregisterReceiver(this.mNfcReceiver);
        }
    }
}

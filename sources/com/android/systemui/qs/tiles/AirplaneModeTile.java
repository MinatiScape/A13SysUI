package com.android.systemui.qs.tiles;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.os.Looper;
import android.sysprop.TelephonyProperties;
import android.view.View;
import android.widget.Switch;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.SettingObserver;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.util.settings.GlobalSettings;
import dagger.Lazy;
import java.util.Objects;
/* loaded from: classes.dex */
public final class AirplaneModeTile extends QSTileImpl<QSTile.BooleanState> {
    public final BroadcastDispatcher mBroadcastDispatcher;
    public final Lazy<ConnectivityManager> mLazyConnectivityManager;
    public boolean mListening;
    public final AnonymousClass1 mSetting;
    public final QSTile.Icon mIcon = QSTileImpl.ResourceIcon.get(17302820);
    public final AnonymousClass2 mReceiver = new BroadcastReceiver() { // from class: com.android.systemui.qs.tiles.AirplaneModeTile.2
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            if ("android.intent.action.AIRPLANE_MODE".equals(intent.getAction())) {
                AirplaneModeTile airplaneModeTile = AirplaneModeTile.this;
                Objects.requireNonNull(airplaneModeTile);
                airplaneModeTile.refreshState(null);
            }
        }
    };

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final int getMetricsCategory() {
        return 112;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final Intent getLongClickIntent() {
        return new Intent("android.settings.AIRPLANE_MODE_SETTINGS");
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final CharSequence getTileLabel() {
        return this.mContext.getString(2131951873);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleClick(View view) {
        boolean z = ((QSTile.BooleanState) this.mState).value;
        MetricsLogger.action(this.mContext, 112, !z);
        if (z || !((Boolean) TelephonyProperties.in_ecm_mode().orElse(Boolean.FALSE)).booleanValue()) {
            this.mLazyConnectivityManager.get().setAirplaneMode(!z);
            return;
        }
        this.mActivityStarter.postStartActivityDismissingKeyguard(new Intent("android.telephony.action.SHOW_NOTICE_ECM_BLOCK_OTHERS"), 0);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleUpdateState(QSTile.BooleanState booleanState, Object obj) {
        int i;
        boolean z;
        QSTile.BooleanState booleanState2 = booleanState;
        checkIfRestrictionEnforcedByAdminOnly(booleanState2, "no_airplane_mode");
        if (obj instanceof Integer) {
            i = ((Integer) obj).intValue();
        } else {
            i = getValue();
        }
        int i2 = 1;
        if (i != 0) {
            z = true;
        } else {
            z = false;
        }
        booleanState2.value = z;
        booleanState2.label = this.mContext.getString(2131951873);
        booleanState2.icon = this.mIcon;
        if (booleanState2.slash == null) {
            booleanState2.slash = new QSTile.SlashState();
        }
        booleanState2.slash.isSlashed = !z;
        if (z) {
            i2 = 2;
        }
        booleanState2.state = i2;
        booleanState2.contentDescription = booleanState2.label;
        booleanState2.expandedAccessibilityClassName = Switch.class.getName();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final QSTile.BooleanState newTileState() {
        return new QSTile.BooleanState();
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [com.android.systemui.qs.tiles.AirplaneModeTile$2] */
    /* JADX WARN: Type inference failed for: r1v4, types: [com.android.systemui.qs.tiles.AirplaneModeTile$1] */
    public AirplaneModeTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, BroadcastDispatcher broadcastDispatcher, Lazy<ConnectivityManager> lazy, GlobalSettings globalSettings) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mLazyConnectivityManager = lazy;
        this.mSetting = new SettingObserver(globalSettings, this.mHandler) { // from class: com.android.systemui.qs.tiles.AirplaneModeTile.1
            @Override // com.android.systemui.qs.SettingObserver
            public final void handleValueChanged(int i, boolean z) {
                AirplaneModeTile.this.handleRefreshState(Integer.valueOf(i));
            }
        };
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleSetListening(boolean z) {
        super.handleSetListening(z);
        if (this.mListening != z) {
            this.mListening = z;
            if (z) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.intent.action.AIRPLANE_MODE");
                this.mBroadcastDispatcher.registerReceiver(this.mReceiver, intentFilter);
            } else {
                this.mBroadcastDispatcher.unregisterReceiver(this.mReceiver);
            }
            setListening(z);
        }
    }
}

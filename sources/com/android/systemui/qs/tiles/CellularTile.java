package com.android.systemui.qs.tiles;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.view.View;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleRegistry;
import com.android.internal.logging.MetricsLogger;
import com.android.settingslib.net.DataUsageController;
import com.android.systemui.Prefs;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSIconView;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.SignalTileView;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.connectivity.IconState;
import com.android.systemui.statusbar.connectivity.MobileDataIndicators;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.statusbar.connectivity.SignalCallback;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.util.Objects;
/* loaded from: classes.dex */
public final class CellularTile extends QSTileImpl<QSTile.SignalState> {
    public final NetworkController mController;
    public final DataUsageController mDataController;
    public final KeyguardStateController mKeyguard;
    public final CellSignalCallback mSignalCallback;

    /* loaded from: classes.dex */
    public static final class CallbackInfo {
        public boolean activityIn;
        public boolean activityOut;
        public boolean airplaneModeEnabled;
        public CharSequence dataContentDescription;
        public String dataSubscriptionName;
        public boolean multipleSubs;
        public boolean noSim;
        public boolean roaming;

        public CallbackInfo() {
        }

        public CallbackInfo(int i) {
        }
    }

    /* loaded from: classes.dex */
    public final class CellSignalCallback implements SignalCallback {
        public final CallbackInfo mInfo = new CallbackInfo(0);

        public CellSignalCallback() {
        }

        @Override // com.android.systemui.statusbar.connectivity.SignalCallback
        public final void setIsAirplaneMode(IconState iconState) {
            CallbackInfo callbackInfo = this.mInfo;
            callbackInfo.airplaneModeEnabled = iconState.visible;
            CellularTile.this.refreshState(callbackInfo);
        }

        @Override // com.android.systemui.statusbar.connectivity.SignalCallback
        public final void setMobileDataIndicators(MobileDataIndicators mobileDataIndicators) {
            CharSequence charSequence;
            if (mobileDataIndicators.qsIcon != null) {
                this.mInfo.dataSubscriptionName = CellularTile.this.mController.getMobileDataNetworkName();
                CallbackInfo callbackInfo = this.mInfo;
                if (mobileDataIndicators.qsDescription != null) {
                    charSequence = mobileDataIndicators.typeContentDescriptionHtml;
                } else {
                    charSequence = null;
                }
                callbackInfo.dataContentDescription = charSequence;
                callbackInfo.activityIn = mobileDataIndicators.activityIn;
                callbackInfo.activityOut = mobileDataIndicators.activityOut;
                callbackInfo.roaming = mobileDataIndicators.roaming;
                boolean z = true;
                if (CellularTile.this.mController.getNumberSubscriptions() <= 1) {
                    z = false;
                }
                callbackInfo.multipleSubs = z;
                CellularTile.this.refreshState(this.mInfo);
            }
        }

        @Override // com.android.systemui.statusbar.connectivity.SignalCallback
        public final void setNoSims(boolean z, boolean z2) {
            CallbackInfo callbackInfo = this.mInfo;
            callbackInfo.noSim = z;
            CellularTile.this.refreshState(callbackInfo);
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final int getMetricsCategory() {
        return 115;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final QSIconView createTileView(Context context) {
        return new SignalTileView(context);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final Intent getLongClickIntent() {
        if (((QSTile.SignalState) this.mState).state == 0) {
            return new Intent("android.settings.WIRELESS_SETTINGS");
        }
        Intent intent = new Intent("android.settings.NETWORK_OPERATOR_SETTINGS");
        if (SubscriptionManager.getDefaultDataSubscriptionId() != -1) {
            intent.putExtra("android.provider.extra.SUB_ID", SubscriptionManager.getDefaultDataSubscriptionId());
        }
        return intent;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final CharSequence getTileLabel() {
        return this.mContext.getString(2131953077);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleClick(View view) {
        if (((QSTile.SignalState) this.mState).state != 0) {
            DataUsageController dataUsageController = this.mDataController;
            Objects.requireNonNull(dataUsageController);
            if (!dataUsageController.getTelephonyManager().isDataEnabled()) {
                this.mDataController.setMobileDataEnabled(true);
            } else if (Prefs.getBoolean(this.mContext, "QsHasTurnedOffMobileData")) {
                this.mDataController.setMobileDataEnabled(false);
            } else {
                String mobileDataNetworkName = this.mController.getMobileDataNetworkName();
                boolean isMobileDataNetworkInService = this.mController.isMobileDataNetworkInService();
                if (TextUtils.isEmpty(mobileDataNetworkName) || !isMobileDataNetworkInService) {
                    mobileDataNetworkName = this.mContext.getString(2131952769);
                }
                AlertDialog create = new AlertDialog.Builder(this.mContext).setTitle(2131952770).setMessage(this.mContext.getString(2131952768, mobileDataNetworkName)).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).setPositiveButton(17039652, new DialogInterface.OnClickListener() { // from class: com.android.systemui.qs.tiles.CellularTile$$ExternalSyntheticLambda0
                    @Override // android.content.DialogInterface.OnClickListener
                    public final void onClick(DialogInterface dialogInterface, int i) {
                        CellularTile cellularTile = CellularTile.this;
                        Objects.requireNonNull(cellularTile);
                        cellularTile.mDataController.setMobileDataEnabled(false);
                        Prefs.putBoolean(cellularTile.mContext, "QsHasTurnedOffMobileData", true);
                    }
                }).create();
                create.getWindow().setType(2009);
                SystemUIDialog.setShowForAllUsers(create);
                SystemUIDialog.registerDismissListener(create);
                SystemUIDialog.setWindowOnTop(create, this.mKeyguard.isShowing());
                create.show();
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x0070  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x007a  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0089  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0096  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0133  */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0136  */
    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void handleUpdateState(com.android.systemui.plugins.qs.QSTile.SignalState r10, java.lang.Object r11) {
        /*
            Method dump skipped, instructions count: 315
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.tiles.CellularTile.handleUpdateState(com.android.systemui.plugins.qs.QSTile$State, java.lang.Object):void");
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final boolean isAvailable() {
        if (!this.mController.hasMobileDataFeature() || this.mHost.getUserContext().getUserId() != 0) {
            return false;
        }
        return true;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final QSTile.SignalState newTileState() {
        return new QSTile.SignalState();
    }

    public CellularTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, NetworkController networkController, KeyguardStateController keyguardStateController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        CellSignalCallback cellSignalCallback = new CellSignalCallback();
        this.mSignalCallback = cellSignalCallback;
        this.mController = networkController;
        this.mKeyguard = keyguardStateController;
        this.mDataController = networkController.getMobileDataController();
        networkController.observe((Lifecycle) this.mLifecycle, (LifecycleRegistry) cellSignalCallback);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleSecondaryClick(View view) {
        handleLongClick(view);
    }
}

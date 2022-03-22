package com.android.systemui.qs.tiles;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleRegistry;
import androidx.recyclerview.widget.LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0;
import com.android.internal.logging.MetricsLogger;
import com.android.keyguard.KeyguardUpdateMonitor$18$$ExternalSyntheticOutline0;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSIconView;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.AlphaControlledSignalTileView;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.statusbar.connectivity.AccessPointController;
import com.android.systemui.statusbar.connectivity.IconState;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.statusbar.connectivity.SignalCallback;
import com.android.systemui.statusbar.connectivity.WifiIndicators;
import com.android.wm.shell.TaskView$$ExternalSyntheticLambda3;
import java.util.Objects;
/* loaded from: classes.dex */
public final class WifiTile extends QSTileImpl<QSTile.SignalState> {
    public static final Intent WIFI_SETTINGS = new Intent("android.settings.WIFI_SETTINGS");
    public final NetworkController mController;
    public boolean mExpectDisabled;
    public final WifiSignalCallback mSignalCallback;
    public final QSTile.SignalState mStateBeforeClick;
    public final AccessPointController mWifiController;

    /* loaded from: classes.dex */
    public static final class CallbackInfo {
        public boolean activityIn;
        public boolean activityOut;
        public boolean connected;
        public boolean enabled;
        public boolean isTransient;
        public String ssid;
        public String statusLabel;
        public String wifiSignalContentDescription;
        public int wifiSignalIconId;

        public final String toString() {
            StringBuilder sb = new StringBuilder("CallbackInfo[");
            sb.append("enabled=");
            sb.append(this.enabled);
            sb.append(",connected=");
            sb.append(this.connected);
            sb.append(",wifiSignalIconId=");
            sb.append(this.wifiSignalIconId);
            sb.append(",ssid=");
            sb.append(this.ssid);
            sb.append(",activityIn=");
            sb.append(this.activityIn);
            sb.append(",activityOut=");
            sb.append(this.activityOut);
            sb.append(",wifiSignalContentDescription=");
            sb.append(this.wifiSignalContentDescription);
            sb.append(",isTransient=");
            return LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0.m(sb, this.isTransient, ']');
        }
    }

    /* loaded from: classes.dex */
    public final class WifiSignalCallback implements SignalCallback {
        public final CallbackInfo mInfo = new CallbackInfo();

        public WifiSignalCallback() {
        }

        @Override // com.android.systemui.statusbar.connectivity.SignalCallback
        public final void setWifiIndicators(WifiIndicators wifiIndicators) {
            if (QSTileImpl.DEBUG) {
                KeyguardUpdateMonitor$18$$ExternalSyntheticOutline0.m(VendorAtomValue$$ExternalSyntheticOutline1.m("onWifiSignalChanged enabled="), wifiIndicators.enabled, WifiTile.this.TAG);
            }
            IconState iconState = wifiIndicators.qsIcon;
            if (iconState != null) {
                CallbackInfo callbackInfo = this.mInfo;
                callbackInfo.enabled = wifiIndicators.enabled;
                callbackInfo.connected = iconState.visible;
                callbackInfo.wifiSignalIconId = iconState.icon;
                callbackInfo.ssid = wifiIndicators.description;
                callbackInfo.activityIn = wifiIndicators.activityIn;
                callbackInfo.activityOut = wifiIndicators.activityOut;
                callbackInfo.wifiSignalContentDescription = iconState.contentDescription;
                callbackInfo.isTransient = wifiIndicators.isTransient;
                callbackInfo.statusLabel = wifiIndicators.statusLabel;
                WifiTile wifiTile = WifiTile.this;
                Objects.requireNonNull(wifiTile);
                wifiTile.refreshState(null);
            }
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final int getMetricsCategory() {
        return 126;
    }

    public static String removeDoubleQuotes(String str) {
        if (str == null) {
            return null;
        }
        int length = str.length();
        if (length <= 1 || str.charAt(0) != '\"') {
            return str;
        }
        int i = length - 1;
        if (str.charAt(i) == '\"') {
            return str.substring(1, i);
        }
        return str;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final QSIconView createTileView(Context context) {
        return new AlphaControlledSignalTileView(context);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final CharSequence getTileLabel() {
        return this.mContext.getString(2131953145);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleClick(View view) {
        Object obj;
        ((QSTile.SignalState) this.mState).copyTo(this.mStateBeforeClick);
        boolean z = ((QSTile.SignalState) this.mState).value;
        if (z) {
            obj = null;
        } else {
            obj = QSTileImpl.ARG_SHOW_TRANSIENT_ENABLING;
        }
        refreshState(obj);
        this.mController.setWifiEnabled(!z);
        this.mExpectDisabled = z;
        if (z) {
            this.mHandler.postDelayed(new TaskView$$ExternalSyntheticLambda3(this, 3), 350L);
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleSecondaryClick(View view) {
        if (!this.mWifiController.canConfigWifi()) {
            this.mActivityStarter.postStartActivityDismissingKeyguard(new Intent("android.settings.WIFI_SETTINGS"), 0);
        } else if (!((QSTile.SignalState) this.mState).value) {
            this.mController.setWifiEnabled(true);
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleUpdateState(QSTile.SignalState signalState, Object obj) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        boolean z7;
        CharSequence charSequence;
        int i;
        QSTile.SignalState signalState2 = signalState;
        if (QSTileImpl.DEBUG) {
            Log.d(this.TAG, "handleUpdateState arg=" + obj);
        }
        CallbackInfo callbackInfo = this.mSignalCallback.mInfo;
        if (this.mExpectDisabled) {
            if (!callbackInfo.enabled) {
                this.mExpectDisabled = false;
            } else {
                return;
            }
        }
        if (obj == QSTileImpl.ARG_SHOW_TRANSIENT_ENABLING) {
            z = true;
        } else {
            z = false;
        }
        if (!callbackInfo.enabled || (i = callbackInfo.wifiSignalIconId) <= 0 || (callbackInfo.ssid == null && i == 17302891)) {
            z2 = false;
        } else {
            z2 = true;
        }
        if (callbackInfo.ssid == null && callbackInfo.wifiSignalIconId == 17302891) {
            z3 = true;
        } else {
            z3 = false;
        }
        if (signalState2.slash == null) {
            QSTile.SlashState slashState = new QSTile.SlashState();
            signalState2.slash = slashState;
            slashState.rotation = 6.0f;
        }
        signalState2.slash.isSlashed = false;
        if (z || callbackInfo.isTransient) {
            z4 = true;
        } else {
            z4 = false;
        }
        String str = callbackInfo.statusLabel;
        if (z4) {
            str = this.mContext.getString(2131953146);
        }
        signalState2.secondaryLabel = str;
        signalState2.state = 2;
        signalState2.dualTarget = true;
        if (z || callbackInfo.enabled) {
            z5 = true;
        } else {
            z5 = false;
        }
        signalState2.value = z5;
        boolean z8 = callbackInfo.enabled;
        if (!z8 || !callbackInfo.activityIn) {
            z6 = false;
        } else {
            z6 = true;
        }
        signalState2.activityIn = z6;
        if (!z8 || !callbackInfo.activityOut) {
            z7 = false;
        } else {
            z7 = true;
        }
        signalState2.activityOut = z7;
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBuffer2 = new StringBuffer();
        Resources resources = this.mContext.getResources();
        if (z4) {
            signalState2.icon = QSTileImpl.ResourceIcon.get(17302859);
            signalState2.label = resources.getString(2131953145);
        } else if (!signalState2.value) {
            signalState2.slash.isSlashed = true;
            signalState2.state = 1;
            signalState2.icon = QSTileImpl.ResourceIcon.get(17302891);
            signalState2.label = resources.getString(2131953145);
        } else if (z2) {
            signalState2.icon = QSTileImpl.ResourceIcon.get(callbackInfo.wifiSignalIconId);
            String str2 = callbackInfo.ssid;
            if (str2 != null) {
                charSequence = removeDoubleQuotes(str2);
            } else {
                charSequence = getTileLabel();
            }
            signalState2.label = charSequence;
        } else if (z3) {
            signalState2.icon = QSTileImpl.ResourceIcon.get(17302891);
            signalState2.label = resources.getString(2131953145);
        } else {
            signalState2.icon = QSTileImpl.ResourceIcon.get(17302891);
            signalState2.label = resources.getString(2131953145);
        }
        stringBuffer.append(this.mContext.getString(2131953145));
        stringBuffer.append(",");
        if (signalState2.value && z2) {
            stringBuffer2.append(callbackInfo.wifiSignalContentDescription);
            stringBuffer.append(removeDoubleQuotes(callbackInfo.ssid));
            if (!TextUtils.isEmpty(signalState2.secondaryLabel)) {
                stringBuffer.append(",");
                stringBuffer.append(signalState2.secondaryLabel);
            }
        }
        signalState2.stateDescription = stringBuffer2.toString();
        signalState2.contentDescription = stringBuffer.toString();
        signalState2.dualLabelContentDescription = resources.getString(2131951795, getTileLabel());
        signalState2.expandedAccessibilityClassName = Switch.class.getName();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final boolean isAvailable() {
        return this.mContext.getPackageManager().hasSystemFeature("android.hardware.wifi");
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final QSTile.SignalState newTileState() {
        return new QSTile.SignalState();
    }

    public WifiTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, NetworkController networkController, AccessPointController accessPointController) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        QSTile.SignalState signalState = new QSTile.SignalState();
        this.mStateBeforeClick = signalState;
        WifiSignalCallback wifiSignalCallback = new WifiSignalCallback();
        this.mSignalCallback = wifiSignalCallback;
        this.mController = networkController;
        this.mWifiController = accessPointController;
        networkController.observe((Lifecycle) this.mLifecycle, (LifecycleRegistry) wifiSignalCallback);
        signalState.spec = "wifi";
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final Intent getLongClickIntent() {
        return WIFI_SETTINGS;
    }
}

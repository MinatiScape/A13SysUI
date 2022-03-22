package com.android.systemui.qs.tiles;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline2;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleRegistry;
import androidx.recyclerview.widget.LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0;
import com.android.internal.logging.MetricsLogger;
import com.android.keyguard.KeyguardUpdateMonitor$18$$ExternalSyntheticOutline0;
import com.android.settingslib.graph.SignalDrawable;
import com.android.settingslib.net.DataUsageController;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSIconView;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.AlphaControlledSignalTileView;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.qs.tiles.dialog.InternetDialogFactory;
import com.android.systemui.statusbar.connectivity.AccessPointController;
import com.android.systemui.statusbar.connectivity.IconState;
import com.android.systemui.statusbar.connectivity.MobileDataIndicators;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.statusbar.connectivity.SignalCallback;
import com.android.systemui.statusbar.connectivity.WifiIndicators;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Objects;
/* loaded from: classes.dex */
public final class InternetTile extends QSTileImpl<QSTile.SignalState> {
    public static final Intent WIFI_SETTINGS = new Intent("android.settings.WIFI_SETTINGS");
    public final AccessPointController mAccessPointController;
    public final NetworkController mController;
    public final DataUsageController mDataController;
    public final Handler mHandler;
    public final InternetDialogFactory mInternetDialogFactory;
    public int mLastTileState = -1;
    public final InternetSignalCallback mSignalCallback;

    /* loaded from: classes.dex */
    public static final class CellularCallbackInfo {
        public boolean mActivityIn;
        public boolean mActivityOut;
        public boolean mAirplaneModeEnabled;
        public CharSequence mDataContentDescription;
        public CharSequence mDataSubscriptionName;
        public int mMobileSignalIconId;
        public boolean mMultipleSubs;
        public boolean mNoDefaultNetwork;
        public boolean mNoNetworksAvailable;
        public boolean mNoSim;
        public boolean mNoValidatedNetwork;
        public int mQsTypeIcon;
        public boolean mRoaming;

        public CellularCallbackInfo() {
        }

        public CellularCallbackInfo(int i) {
        }

        public final String toString() {
            StringBuilder sb = new StringBuilder("CellularCallbackInfo[");
            sb.append("mAirplaneModeEnabled=");
            sb.append(this.mAirplaneModeEnabled);
            sb.append(",mDataSubscriptionName=");
            sb.append(this.mDataSubscriptionName);
            sb.append(",mDataContentDescription=");
            sb.append(this.mDataContentDescription);
            sb.append(",mMobileSignalIconId=");
            sb.append(this.mMobileSignalIconId);
            sb.append(",mQsTypeIcon=");
            sb.append(this.mQsTypeIcon);
            sb.append(",mActivityIn=");
            sb.append(this.mActivityIn);
            sb.append(",mActivityOut=");
            sb.append(this.mActivityOut);
            sb.append(",mNoSim=");
            sb.append(this.mNoSim);
            sb.append(",mRoaming=");
            sb.append(this.mRoaming);
            sb.append(",mMultipleSubs=");
            sb.append(this.mMultipleSubs);
            sb.append(",mNoDefaultNetwork=");
            sb.append(this.mNoDefaultNetwork);
            sb.append(",mNoValidatedNetwork=");
            sb.append(this.mNoValidatedNetwork);
            sb.append(",mNoNetworksAvailable=");
            return LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0.m(sb, this.mNoNetworksAvailable, ']');
        }
    }

    /* loaded from: classes.dex */
    public static final class EthernetCallbackInfo {
        public boolean mConnected;
        public String mEthernetContentDescription;
        public int mEthernetSignalIconId;

        public EthernetCallbackInfo() {
        }

        public EthernetCallbackInfo(int i) {
        }

        public final String toString() {
            return "EthernetCallbackInfo[mConnected=" + this.mConnected + ",mEthernetSignalIconId=" + this.mEthernetSignalIconId + ",mEthernetContentDescription=" + this.mEthernetContentDescription + ']';
        }
    }

    /* loaded from: classes.dex */
    public final class InternetSignalCallback implements SignalCallback {
        public final WifiCallbackInfo mWifiInfo = new WifiCallbackInfo(0);
        public final CellularCallbackInfo mCellularInfo = new CellularCallbackInfo(0);
        public final EthernetCallbackInfo mEthernetInfo = new EthernetCallbackInfo(0);

        public InternetSignalCallback() {
        }

        @Override // com.android.systemui.statusbar.connectivity.SignalCallback
        public final void setConnectivityStatus(boolean z, boolean z2, boolean z3) {
            if (QSTileImpl.DEBUG) {
                String str = InternetTile.this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("setConnectivityStatus: noDefaultNetwork = ");
                sb.append(z);
                sb.append(",noValidatedNetwork = ");
                sb.append(z2);
                sb.append(",noNetworksAvailable = ");
                KeyguardUpdateMonitor$18$$ExternalSyntheticOutline0.m(sb, z3, str);
            }
            CellularCallbackInfo cellularCallbackInfo = this.mCellularInfo;
            cellularCallbackInfo.mNoDefaultNetwork = z;
            cellularCallbackInfo.mNoValidatedNetwork = z2;
            cellularCallbackInfo.mNoNetworksAvailable = z3;
            WifiCallbackInfo wifiCallbackInfo = this.mWifiInfo;
            wifiCallbackInfo.mNoDefaultNetwork = z;
            wifiCallbackInfo.mNoValidatedNetwork = z2;
            wifiCallbackInfo.mNoNetworksAvailable = z3;
            InternetTile.this.refreshState(wifiCallbackInfo);
        }

        @Override // com.android.systemui.statusbar.connectivity.SignalCallback
        public final void setEthernetIndicators(IconState iconState) {
            String str;
            if (QSTileImpl.DEBUG) {
                String str2 = InternetTile.this.TAG;
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("setEthernetIndicators: icon = ");
                if (iconState == null) {
                    str = "";
                } else {
                    str = iconState.toString();
                }
                ExifInterface$$ExternalSyntheticOutline2.m(m, str, str2);
            }
            EthernetCallbackInfo ethernetCallbackInfo = this.mEthernetInfo;
            boolean z = iconState.visible;
            ethernetCallbackInfo.mConnected = z;
            ethernetCallbackInfo.mEthernetSignalIconId = iconState.icon;
            ethernetCallbackInfo.mEthernetContentDescription = iconState.contentDescription;
            if (z) {
                InternetTile.this.refreshState(ethernetCallbackInfo);
            }
        }

        @Override // com.android.systemui.statusbar.connectivity.SignalCallback
        public final void setIsAirplaneMode(IconState iconState) {
            String str;
            if (QSTileImpl.DEBUG) {
                String str2 = InternetTile.this.TAG;
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("setIsAirplaneMode: icon = ");
                if (iconState == null) {
                    str = "";
                } else {
                    str = iconState.toString();
                }
                ExifInterface$$ExternalSyntheticOutline2.m(m, str, str2);
            }
            CellularCallbackInfo cellularCallbackInfo = this.mCellularInfo;
            boolean z = cellularCallbackInfo.mAirplaneModeEnabled;
            boolean z2 = iconState.visible;
            if (z != z2) {
                cellularCallbackInfo.mAirplaneModeEnabled = z2;
                WifiCallbackInfo wifiCallbackInfo = this.mWifiInfo;
                wifiCallbackInfo.mAirplaneModeEnabled = z2;
                InternetTile internetTile = InternetTile.this;
                if (internetTile.mSignalCallback.mEthernetInfo.mConnected) {
                    return;
                }
                if (!wifiCallbackInfo.mEnabled || wifiCallbackInfo.mWifiSignalIconId <= 0 || wifiCallbackInfo.mSsid == null) {
                    internetTile.refreshState(cellularCallbackInfo);
                } else {
                    internetTile.refreshState(wifiCallbackInfo);
                }
            }
        }

        @Override // com.android.systemui.statusbar.connectivity.SignalCallback
        public final void setMobileDataIndicators(MobileDataIndicators mobileDataIndicators) {
            CharSequence charSequence;
            if (QSTileImpl.DEBUG) {
                String str = InternetTile.this.TAG;
                Log.d(str, "setMobileDataIndicators: " + mobileDataIndicators);
            }
            if (mobileDataIndicators.qsIcon != null) {
                CellularCallbackInfo cellularCallbackInfo = this.mCellularInfo;
                CharSequence charSequence2 = mobileDataIndicators.qsDescription;
                if (charSequence2 == null) {
                    charSequence2 = InternetTile.this.mController.getMobileDataNetworkName();
                }
                cellularCallbackInfo.mDataSubscriptionName = charSequence2;
                CellularCallbackInfo cellularCallbackInfo2 = this.mCellularInfo;
                if (mobileDataIndicators.qsDescription != null) {
                    charSequence = mobileDataIndicators.typeContentDescriptionHtml;
                } else {
                    charSequence = null;
                }
                cellularCallbackInfo2.mDataContentDescription = charSequence;
                cellularCallbackInfo2.mMobileSignalIconId = mobileDataIndicators.qsIcon.icon;
                cellularCallbackInfo2.mQsTypeIcon = mobileDataIndicators.qsType;
                cellularCallbackInfo2.mActivityIn = mobileDataIndicators.activityIn;
                cellularCallbackInfo2.mActivityOut = mobileDataIndicators.activityOut;
                cellularCallbackInfo2.mRoaming = mobileDataIndicators.roaming;
                boolean z = true;
                if (InternetTile.this.mController.getNumberSubscriptions() <= 1) {
                    z = false;
                }
                cellularCallbackInfo2.mMultipleSubs = z;
                InternetTile.this.refreshState(this.mCellularInfo);
            }
        }

        @Override // com.android.systemui.statusbar.connectivity.SignalCallback
        public final void setNoSims(boolean z, boolean z2) {
            if (QSTileImpl.DEBUG) {
                String str = InternetTile.this.TAG;
                Log.d(str, "setNoSims: show = " + z + ",simDetected = " + z2);
            }
            CellularCallbackInfo cellularCallbackInfo = this.mCellularInfo;
            cellularCallbackInfo.mNoSim = z;
            if (z) {
                cellularCallbackInfo.mMobileSignalIconId = 0;
                cellularCallbackInfo.mQsTypeIcon = 0;
            }
        }

        @Override // com.android.systemui.statusbar.connectivity.SignalCallback
        public final void setWifiIndicators(WifiIndicators wifiIndicators) {
            if (QSTileImpl.DEBUG) {
                String str = InternetTile.this.TAG;
                Log.d(str, "setWifiIndicators: " + wifiIndicators);
            }
            WifiCallbackInfo wifiCallbackInfo = this.mWifiInfo;
            boolean z = wifiIndicators.enabled;
            wifiCallbackInfo.mEnabled = z;
            IconState iconState = wifiIndicators.qsIcon;
            if (iconState != null) {
                wifiCallbackInfo.mConnected = iconState.visible;
                wifiCallbackInfo.mWifiSignalIconId = iconState.icon;
                wifiCallbackInfo.mWifiSignalContentDescription = iconState.contentDescription;
                wifiCallbackInfo.mEnabled = z;
                wifiCallbackInfo.mSsid = wifiIndicators.description;
                wifiCallbackInfo.mActivityIn = wifiIndicators.activityIn;
                wifiCallbackInfo.mActivityOut = wifiIndicators.activityOut;
                wifiCallbackInfo.mIsTransient = wifiIndicators.isTransient;
                Objects.requireNonNull(wifiCallbackInfo);
                InternetTile.this.refreshState(this.mWifiInfo);
            }
        }

        public final String toString() {
            return "InternetSignalCallback[mWifiInfo=" + this.mWifiInfo + ",mCellularInfo=" + this.mCellularInfo + ",mEthernetInfo=" + this.mEthernetInfo + ']';
        }
    }

    /* loaded from: classes.dex */
    public static class SignalIcon extends QSTile.Icon {
        public final int mState;

        @Override // com.android.systemui.plugins.qs.QSTile.Icon
        public final Drawable getDrawable(Context context) {
            SignalDrawable signalDrawable = new SignalDrawable(context);
            signalDrawable.setLevel(this.mState);
            return signalDrawable;
        }

        public SignalIcon(int i) {
            this.mState = i;
        }
    }

    /* loaded from: classes.dex */
    public static final class WifiCallbackInfo {
        public boolean mActivityIn;
        public boolean mActivityOut;
        public boolean mAirplaneModeEnabled;
        public boolean mConnected;
        public boolean mEnabled;
        public boolean mIsTransient;
        public boolean mNoDefaultNetwork;
        public boolean mNoNetworksAvailable;
        public boolean mNoValidatedNetwork;
        public String mSsid;
        public String mWifiSignalContentDescription;
        public int mWifiSignalIconId;

        public WifiCallbackInfo() {
        }

        public WifiCallbackInfo(int i) {
        }

        public final String toString() {
            StringBuilder sb = new StringBuilder("WifiCallbackInfo[");
            sb.append("mAirplaneModeEnabled=");
            sb.append(this.mAirplaneModeEnabled);
            sb.append(",mEnabled=");
            sb.append(this.mEnabled);
            sb.append(",mConnected=");
            sb.append(this.mConnected);
            sb.append(",mWifiSignalIconId=");
            sb.append(this.mWifiSignalIconId);
            sb.append(",mSsid=");
            sb.append(this.mSsid);
            sb.append(",mActivityIn=");
            sb.append(this.mActivityIn);
            sb.append(",mActivityOut=");
            sb.append(this.mActivityOut);
            sb.append(",mWifiSignalContentDescription=");
            sb.append(this.mWifiSignalContentDescription);
            sb.append(",mIsTransient=");
            sb.append(this.mIsTransient);
            sb.append(",mNoDefaultNetwork=");
            sb.append(this.mNoDefaultNetwork);
            sb.append(",mNoValidatedNetwork=");
            sb.append(this.mNoValidatedNetwork);
            sb.append(",mNoNetworksAvailable=");
            return LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0.m(sb, this.mNoNetworksAvailable, ']');
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

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("InternetTile:");
        printWriter.print("    ");
        printWriter.println(((QSTile.SignalState) this.mState).toString());
        printWriter.print("    ");
        printWriter.println("mLastTileState=" + this.mLastTileState);
        printWriter.print("    ");
        printWriter.println("mSignalCallback=" + this.mSignalCallback.toString());
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final CharSequence getTileLabel() {
        return this.mContext.getString(2131953115);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleClick(View view) {
        this.mHandler.post(new InternetTile$$ExternalSyntheticLambda0(this, view, 0));
    }

    /* JADX WARN: Removed duplicated region for block: B:30:0x008a  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00aa  */
    /* JADX WARN: Removed duplicated region for block: B:41:0x00dc  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0166  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0169  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x016f  */
    /* JADX WARN: Removed duplicated region for block: B:75:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void handleUpdateCellularState(com.android.systemui.plugins.qs.QSTile.SignalState r11, java.lang.Object r12) {
        /*
            Method dump skipped, instructions count: 390
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.tiles.InternetTile.handleUpdateCellularState(com.android.systemui.plugins.qs.QSTile$SignalState, java.lang.Object):void");
    }

    public final void handleUpdateEthernetState(QSTile.SignalState signalState, Object obj) {
        EthernetCallbackInfo ethernetCallbackInfo = (EthernetCallbackInfo) obj;
        boolean z = QSTileImpl.DEBUG;
        if (z) {
            String str = this.TAG;
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("handleUpdateEthernetState: EthernetCallbackInfo = ");
            m.append(ethernetCallbackInfo.toString());
            Log.d(str, m.toString());
        }
        signalState.label = this.mContext.getResources().getString(2131953115);
        signalState.state = 2;
        signalState.icon = QSTileImpl.ResourceIcon.get(ethernetCallbackInfo.mEthernetSignalIconId);
        signalState.secondaryLabel = ethernetCallbackInfo.mEthernetContentDescription;
        if (z) {
            String str2 = this.TAG;
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("handleUpdateEthernetState: SignalState = ");
            m2.append(signalState.toString());
            Log.d(str2, m2.toString());
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final void handleUpdateState(QSTile.SignalState signalState, Object obj) {
        QSTile.SignalState signalState2 = signalState;
        if (obj instanceof CellularCallbackInfo) {
            this.mLastTileState = 0;
            handleUpdateCellularState(signalState2, obj);
        } else if (obj instanceof WifiCallbackInfo) {
            this.mLastTileState = 1;
            handleUpdateWifiState(signalState2, obj);
        } else if (obj instanceof EthernetCallbackInfo) {
            this.mLastTileState = 2;
            handleUpdateEthernetState(signalState2, obj);
        } else {
            int i = this.mLastTileState;
            if (i == 0) {
                handleUpdateCellularState(signalState2, this.mSignalCallback.mCellularInfo);
            } else if (i == 1) {
                handleUpdateWifiState(signalState2, this.mSignalCallback.mWifiInfo);
            } else if (i == 2) {
                handleUpdateEthernetState(signalState2, this.mSignalCallback.mEthernetInfo);
            }
        }
    }

    public final void handleUpdateWifiState(QSTile.SignalState signalState, Object obj) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        WifiCallbackInfo wifiCallbackInfo = (WifiCallbackInfo) obj;
        boolean z5 = QSTileImpl.DEBUG;
        if (z5) {
            String str = this.TAG;
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("handleUpdateWifiState: WifiCallbackInfo = ");
            m.append(wifiCallbackInfo.toString());
            Log.d(str, m.toString());
        }
        if (!wifiCallbackInfo.mEnabled || wifiCallbackInfo.mWifiSignalIconId <= 0 || wifiCallbackInfo.mSsid == null) {
            z = false;
        } else {
            z = true;
        }
        if (wifiCallbackInfo.mWifiSignalIconId <= 0 || wifiCallbackInfo.mSsid != null) {
            z2 = false;
        } else {
            z2 = true;
        }
        if (signalState.slash == null) {
            QSTile.SlashState slashState = new QSTile.SlashState();
            signalState.slash = slashState;
            slashState.rotation = 6.0f;
        }
        signalState.slash.isSlashed = false;
        boolean z6 = wifiCallbackInfo.mIsTransient;
        String removeDoubleQuotes = removeDoubleQuotes(wifiCallbackInfo.mSsid);
        if (z6) {
            removeDoubleQuotes = this.mContext.getString(2131953146);
        }
        signalState.secondaryLabel = removeDoubleQuotes;
        signalState.state = 2;
        signalState.dualTarget = true;
        boolean z7 = wifiCallbackInfo.mEnabled;
        signalState.value = z7;
        if (!z7 || !wifiCallbackInfo.mActivityIn) {
            z3 = false;
        } else {
            z3 = true;
        }
        signalState.activityIn = z3;
        if (!z7 || !wifiCallbackInfo.mActivityOut) {
            z4 = false;
        } else {
            z4 = true;
        }
        signalState.activityOut = z4;
        StringBuffer stringBuffer = new StringBuffer();
        StringBuffer stringBuffer2 = new StringBuffer();
        Resources resources = this.mContext.getResources();
        signalState.label = resources.getString(2131953115);
        if (wifiCallbackInfo.mAirplaneModeEnabled) {
            if (!signalState.value) {
                signalState.state = 1;
                signalState.icon = QSTileImpl.ResourceIcon.get(2131232229);
                signalState.secondaryLabel = resources.getString(2131953313);
            } else if (!z) {
                signalState.icon = QSTileImpl.ResourceIcon.get(2131232229);
                if (wifiCallbackInfo.mNoNetworksAvailable) {
                    signalState.secondaryLabel = resources.getString(2131953122);
                } else {
                    signalState.secondaryLabel = resources.getString(2131953121);
                }
            } else {
                signalState.icon = QSTileImpl.ResourceIcon.get(wifiCallbackInfo.mWifiSignalIconId);
            }
        } else if (wifiCallbackInfo.mNoDefaultNetwork) {
            if (wifiCallbackInfo.mNoNetworksAvailable || !wifiCallbackInfo.mEnabled) {
                signalState.icon = QSTileImpl.ResourceIcon.get(2131232229);
                signalState.secondaryLabel = resources.getString(2131953122);
            } else {
                signalState.icon = QSTileImpl.ResourceIcon.get(2131232228);
                signalState.secondaryLabel = resources.getString(2131953121);
            }
        } else if (wifiCallbackInfo.mIsTransient) {
            signalState.icon = QSTileImpl.ResourceIcon.get(17302859);
        } else if (!signalState.value) {
            signalState.slash.isSlashed = true;
            signalState.state = 1;
            signalState.icon = QSTileImpl.ResourceIcon.get(17302891);
        } else if (z) {
            signalState.icon = QSTileImpl.ResourceIcon.get(wifiCallbackInfo.mWifiSignalIconId);
        } else if (z2) {
            signalState.icon = QSTileImpl.ResourceIcon.get(17302891);
        } else {
            signalState.icon = QSTileImpl.ResourceIcon.get(17302891);
        }
        stringBuffer.append(this.mContext.getString(2131953115));
        stringBuffer.append(",");
        if (signalState.value && z) {
            stringBuffer2.append(wifiCallbackInfo.mWifiSignalContentDescription);
            stringBuffer.append(removeDoubleQuotes(wifiCallbackInfo.mSsid));
        } else if (!TextUtils.isEmpty(signalState.secondaryLabel)) {
            stringBuffer.append(",");
            stringBuffer.append(signalState.secondaryLabel);
        }
        signalState.stateDescription = stringBuffer2.toString();
        signalState.contentDescription = stringBuffer.toString();
        signalState.dualLabelContentDescription = resources.getString(2131951795, getTileLabel());
        signalState.expandedAccessibilityClassName = Switch.class.getName();
        if (z5) {
            String str2 = this.TAG;
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("handleUpdateWifiState: SignalState = ");
            m2.append(signalState.toString());
            Log.d(str2, m2.toString());
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final boolean isAvailable() {
        if (this.mContext.getPackageManager().hasSystemFeature("android.hardware.wifi") || (this.mController.hasMobileDataFeature() && this.mHost.getUserContext().getUserId() == 0)) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final QSTile.SignalState newTileState() {
        QSTile.SignalState signalState = new QSTile.SignalState();
        signalState.forceExpandIcon = true;
        return signalState;
    }

    public InternetTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, NetworkController networkController, AccessPointController accessPointController, InternetDialogFactory internetDialogFactory) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        InternetSignalCallback internetSignalCallback = new InternetSignalCallback();
        this.mSignalCallback = internetSignalCallback;
        this.mInternetDialogFactory = internetDialogFactory;
        this.mHandler = handler;
        this.mController = networkController;
        this.mAccessPointController = accessPointController;
        this.mDataController = networkController.getMobileDataController();
        networkController.observe((Lifecycle) this.mLifecycle, (LifecycleRegistry) internetSignalCallback);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public final Intent getLongClickIntent() {
        return WIFI_SETTINGS;
    }
}

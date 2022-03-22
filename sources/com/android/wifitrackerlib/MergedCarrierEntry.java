package com.android.wifitrackerlib;

import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;
import com.android.systemui.power.PowerUI$$ExternalSyntheticLambda0;
import com.android.systemui.qs.tiles.dialog.InternetDialogController;
import java.util.StringJoiner;
/* loaded from: classes.dex */
public final class MergedCarrierEntry extends WifiEntry {
    public final Context mContext;
    public boolean mIsCellDefaultRoute;
    public final String mKey;
    public final int mSubscriptionId;

    public MergedCarrierEntry(Handler handler, WifiManager wifiManager, Context context, int i) throws IllegalArgumentException {
        super(handler, wifiManager, false);
        this.mContext = context;
        this.mSubscriptionId = i;
        this.mKey = VendorAtomValue$$ExternalSyntheticOutline0.m("MergedCarrierEntry:", i);
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public final synchronized void connect(InternetDialogController.WifiEntryConnectCallback wifiEntryConnectCallback) {
        connect(wifiEntryConnectCallback, true);
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public final String getSummary(boolean z) {
        StringJoiner stringJoiner = new StringJoiner(this.mContext.getString(2131953628));
        if (!z) {
            String verboseLoggingDescription = Utils.getVerboseLoggingDescription(this);
            if (!TextUtils.isEmpty(verboseLoggingDescription)) {
                stringJoiner.add(verboseLoggingDescription);
            }
        }
        return stringJoiner.toString();
    }

    public final synchronized void connect(InternetDialogController.WifiEntryConnectCallback wifiEntryConnectCallback, boolean z) {
        this.mConnectCallback = wifiEntryConnectCallback;
        this.mWifiManager.startRestrictingAutoJoinToSubscriptionId(this.mSubscriptionId);
        if (z) {
            Toast.makeText(this.mContext, 2131953669, 0).show();
        }
        if (this.mConnectCallback != null) {
            this.mCallbackHandler.post(new PowerUI$$ExternalSyntheticLambda0(this, 4));
        }
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public final boolean connectionInfoMatches(WifiInfo wifiInfo) {
        if (!wifiInfo.isCarrierMerged() || this.mSubscriptionId != wifiInfo.getSubscriptionId()) {
            return false;
        }
        return true;
    }

    @Override // com.android.wifitrackerlib.WifiEntry
    public final String getKey() {
        return this.mKey;
    }
}

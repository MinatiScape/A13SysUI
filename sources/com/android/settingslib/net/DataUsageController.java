package com.android.settingslib.net;

import android.app.usage.NetworkStatsManager;
import android.content.Context;
import android.net.NetworkPolicyManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.ArrayUtils;
import com.android.systemui.statusbar.connectivity.MobileSignalController;
import com.android.systemui.statusbar.connectivity.NetworkControllerImpl;
import java.util.Formatter;
import java.util.Locale;
import java.util.Objects;
/* loaded from: classes.dex */
public final class DataUsageController {
    public Callback mCallback;
    public final Context mContext;
    public int mSubscriptionId = -1;

    /* loaded from: classes.dex */
    public interface Callback {
    }

    static {
        Log.isLoggable("DataUsageController", 3);
        new Formatter(new StringBuilder(50), Locale.getDefault());
    }

    @VisibleForTesting
    public TelephonyManager getTelephonyManager() {
        int i = this.mSubscriptionId;
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            i = SubscriptionManager.getDefaultDataSubscriptionId();
        }
        if (!SubscriptionManager.isValidSubscriptionId(i)) {
            int[] activeSubscriptionIdList = SubscriptionManager.from(this.mContext).getActiveSubscriptionIdList();
            if (!ArrayUtils.isEmpty(activeSubscriptionIdList)) {
                i = activeSubscriptionIdList[0];
            }
        }
        return ((TelephonyManager) this.mContext.getSystemService(TelephonyManager.class)).createForSubscriptionId(i);
    }

    public final void setMobileDataEnabled(boolean z) {
        Log.d("DataUsageController", "setMobileDataEnabled: enabled=" + z);
        getTelephonyManager().setDataEnabled(z);
        Callback callback = this.mCallback;
        if (callback != null) {
            NetworkControllerImpl.AnonymousClass2 r2 = (NetworkControllerImpl.AnonymousClass2) callback;
            NetworkControllerImpl.this.mCallbackHandler.setMobileDataEnabled(z);
            NetworkControllerImpl networkControllerImpl = NetworkControllerImpl.this;
            Objects.requireNonNull(networkControllerImpl);
            for (int i = 0; i < networkControllerImpl.mMobileSignalControllers.size(); i++) {
                MobileSignalController valueAt = networkControllerImpl.mMobileSignalControllers.valueAt(i);
                Objects.requireNonNull(valueAt);
                valueAt.checkDefaultData();
                valueAt.notifyListenersIfNecessary();
            }
        }
    }

    public DataUsageController(Context context) {
        this.mContext = context;
        NetworkPolicyManager.from(context);
        NetworkStatsManager networkStatsManager = (NetworkStatsManager) context.getSystemService(NetworkStatsManager.class);
    }
}

package com.android.systemui.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
import android.util.SparseBooleanArray;
import com.android.systemui.broadcast.BroadcastDispatcher;
/* loaded from: classes.dex */
public final class CarrierConfigTracker extends BroadcastReceiver {
    public final CarrierConfigManager mCarrierConfigManager;
    public boolean mDefaultCallStrengthConfig;
    public boolean mDefaultCallStrengthConfigLoaded;
    public boolean mDefaultCarrierProvisionsWifiMergedNetworks;
    public boolean mDefaultCarrierProvisionsWifiMergedNetworksLoaded;
    public boolean mDefaultNoCallingConfig;
    public boolean mDefaultNoCallingConfigLoaded;
    public final SparseBooleanArray mCallStrengthConfigs = new SparseBooleanArray();
    public final SparseBooleanArray mNoCallingConfigs = new SparseBooleanArray();
    public final SparseBooleanArray mCarrierProvisionsWifiMergedNetworks = new SparseBooleanArray();

    public final boolean getCallStrengthConfig(int i) {
        synchronized (this.mCallStrengthConfigs) {
            if (this.mCallStrengthConfigs.indexOfKey(i) >= 0) {
                return this.mCallStrengthConfigs.get(i);
            }
            if (!this.mDefaultCallStrengthConfigLoaded) {
                this.mDefaultCallStrengthConfig = CarrierConfigManager.getDefaultConfig().getBoolean("display_call_strength_indicator_bool");
                this.mDefaultCallStrengthConfigLoaded = true;
            }
            return this.mDefaultCallStrengthConfig;
        }
    }

    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        PersistableBundle configForSubId;
        if ("android.telephony.action.CARRIER_CONFIG_CHANGED".equals(intent.getAction())) {
            int intExtra = intent.getIntExtra("android.telephony.extra.SUBSCRIPTION_INDEX", -1);
            if (SubscriptionManager.isValidSubscriptionId(intExtra) && (configForSubId = this.mCarrierConfigManager.getConfigForSubId(intExtra)) != null) {
                synchronized (this.mCallStrengthConfigs) {
                    this.mCallStrengthConfigs.put(intExtra, configForSubId.getBoolean("display_call_strength_indicator_bool"));
                }
                synchronized (this.mNoCallingConfigs) {
                    this.mNoCallingConfigs.put(intExtra, configForSubId.getBoolean("use_ip_for_calling_indicator_bool"));
                }
                synchronized (this.mCarrierProvisionsWifiMergedNetworks) {
                    this.mCarrierProvisionsWifiMergedNetworks.put(intExtra, configForSubId.getBoolean("carrier_provisions_wifi_merged_networks_bool"));
                }
            }
        }
    }

    public CarrierConfigTracker(Context context, BroadcastDispatcher broadcastDispatcher) {
        this.mCarrierConfigManager = (CarrierConfigManager) context.getSystemService(CarrierConfigManager.class);
        broadcastDispatcher.registerReceiver(this, new IntentFilter("android.telephony.action.CARRIER_CONFIG_CHANGED"));
    }
}

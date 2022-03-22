package com.android.systemui.statusbar.policy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.telephony.SubscriptionInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.Dependency;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class EmergencyCryptkeeperText extends TextView {
    public KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final AnonymousClass1 mCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.systemui.statusbar.policy.EmergencyCryptkeeperText.1
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onPhoneStateChanged() {
            EmergencyCryptkeeperText.this.update();
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onRefreshCarrierInfo() {
            EmergencyCryptkeeperText.this.update();
        }
    };
    public final AnonymousClass2 mReceiver = new BroadcastReceiver() { // from class: com.android.systemui.statusbar.policy.EmergencyCryptkeeperText.2
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            if ("android.intent.action.AIRPLANE_MODE".equals(intent.getAction())) {
                EmergencyCryptkeeperText.this.update();
            }
        }
    };

    public final void update() {
        boolean z;
        boolean z2;
        boolean isDataCapable = ((TelephonyManager) ((TextView) this).mContext.getSystemService(TelephonyManager.class)).isDataCapable();
        int i = 0;
        if (Settings.Global.getInt(((TextView) this).mContext.getContentResolver(), "airplane_mode_on", 0) == 1) {
            z = true;
        } else {
            z = false;
        }
        if (!isDataCapable || z) {
            setText((CharSequence) null);
            setVisibility(8);
            return;
        }
        ArrayList filteredSubscriptionInfo = this.mKeyguardUpdateMonitor.getFilteredSubscriptionInfo();
        int size = filteredSubscriptionInfo.size();
        boolean z3 = true;
        CharSequence charSequence = null;
        for (int i2 = 0; i2 < size; i2++) {
            int simState = this.mKeyguardUpdateMonitor.getSimState(((SubscriptionInfo) filteredSubscriptionInfo.get(i2)).getSubscriptionId());
            CharSequence carrierName = ((SubscriptionInfo) filteredSubscriptionInfo.get(i2)).getCarrierName();
            if (simState == 2 || simState == 3 || simState == 4 || simState == 5 || simState == 6 || simState == 7 || simState == 8 || simState == 9 || simState == 10) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (z2 && !TextUtils.isEmpty(carrierName)) {
                z3 = false;
                charSequence = carrierName;
            }
        }
        if (z3) {
            if (size != 0) {
                charSequence = ((SubscriptionInfo) filteredSubscriptionInfo.get(0)).getCarrierName();
            } else {
                charSequence = getContext().getText(17040189);
                Intent registerReceiver = getContext().registerReceiver(null, new IntentFilter("android.telephony.action.SERVICE_PROVIDERS_UPDATED"));
                if (registerReceiver != null) {
                    charSequence = registerReceiver.getStringExtra("android.telephony.extra.PLMN");
                }
            }
        }
        setText(charSequence);
        if (TextUtils.isEmpty(charSequence)) {
            i = 8;
        }
        setVisibility(i);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.statusbar.policy.EmergencyCryptkeeperText$1] */
    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.systemui.statusbar.policy.EmergencyCryptkeeperText$2] */
    public EmergencyCryptkeeperText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setVisibility(8);
    }

    @Override // android.widget.TextView, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        KeyguardUpdateMonitor keyguardUpdateMonitor = (KeyguardUpdateMonitor) Dependency.get(KeyguardUpdateMonitor.class);
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        keyguardUpdateMonitor.registerCallback(this.mCallback);
        getContext().registerReceiver(this.mReceiver, new IntentFilter("android.intent.action.AIRPLANE_MODE"));
        update();
    }

    @Override // android.view.View
    public final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        KeyguardUpdateMonitor keyguardUpdateMonitor = this.mKeyguardUpdateMonitor;
        if (keyguardUpdateMonitor != null) {
            keyguardUpdateMonitor.removeCallback(this.mCallback);
        }
        getContext().unregisterReceiver(this.mReceiver);
    }
}

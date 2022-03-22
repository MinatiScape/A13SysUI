package com.android.keyguard;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.UserHandle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.euicc.EuiccManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class KeyguardEsimArea extends Button implements View.OnClickListener {
    public EuiccManager mEuiccManager;
    public AnonymousClass1 mReceiver;
    public int mSubscriptionId;

    public KeyguardEsimArea(Context context) {
        this(context, null);
    }

    public KeyguardEsimArea(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public static boolean isEsimLocked(Context context, int i) {
        SubscriptionInfo activeSubscriptionInfo;
        if (((EuiccManager) context.getSystemService("euicc")).isEnabled() && (activeSubscriptionInfo = SubscriptionManager.from(context).getActiveSubscriptionInfo(i)) != null && activeSubscriptionInfo.isEmbedded()) {
            return true;
        }
        return false;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        SubscriptionInfo activeSubscriptionInfo = SubscriptionManager.from(((Button) this).mContext).getActiveSubscriptionInfo(this.mSubscriptionId);
        if (activeSubscriptionInfo == null) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("No active subscription with subscriptionId: ");
            m.append(this.mSubscriptionId);
            Log.e("KeyguardEsimArea", m.toString());
            return;
        }
        Intent intent = new Intent("com.android.keyguard.disable_esim");
        intent.setPackage(((Button) this).mContext.getPackageName());
        this.mEuiccManager.switchToSubscription(-1, activeSubscriptionInfo.getPortIndex(), PendingIntent.getBroadcastAsUser(((Button) this).mContext, 0, intent, 167772160, UserHandle.SYSTEM));
    }

    @Override // android.view.View
    public final void onDetachedFromWindow() {
        ((Button) this).mContext.unregisterReceiver(this.mReceiver);
        super.onDetachedFromWindow();
    }

    public KeyguardEsimArea(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 16974425);
    }

    @Override // android.widget.TextView, android.view.View
    public final void onAttachedToWindow() {
        super.onAttachedToWindow();
        ((Button) this).mContext.registerReceiver(this.mReceiver, new IntentFilter("com.android.keyguard.disable_esim"), "com.android.systemui.permission.SELF", null, 2);
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [com.android.keyguard.KeyguardEsimArea$1] */
    public KeyguardEsimArea(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mReceiver = new BroadcastReceiver() { // from class: com.android.keyguard.KeyguardEsimArea.1
            @Override // android.content.BroadcastReceiver
            public final void onReceive(Context context2, Intent intent) {
                int resultCode;
                if ("com.android.keyguard.disable_esim".equals(intent.getAction()) && (resultCode = getResultCode()) != 0) {
                    Log.e("KeyguardEsimArea", "Error disabling esim, result code = " + resultCode);
                    AlertDialog create = new AlertDialog.Builder(((Button) KeyguardEsimArea.this).mContext).setMessage(2131952334).setTitle(2131952335).setCancelable(false).setPositiveButton(2131952934, (DialogInterface.OnClickListener) null).create();
                    create.getWindow().setType(2009);
                    create.show();
                }
            }
        };
        this.mEuiccManager = (EuiccManager) context.getSystemService("euicc");
        setOnClickListener(this);
    }
}

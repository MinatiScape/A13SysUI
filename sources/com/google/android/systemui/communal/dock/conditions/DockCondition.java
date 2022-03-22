package com.google.android.systemui.communal.dock.conditions;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.util.Log;
import com.android.keyguard.KeyguardUpdateMonitor$18$$ExternalSyntheticOutline0;
import com.android.systemui.util.condition.Condition;
/* loaded from: classes.dex */
public final class DockCondition extends Condition {
    public static final boolean DEBUG = Log.isLoggable("DockCondition", 3);
    public final Context mContext;
    public AnonymousClass1 mReceiver = new BroadcastReceiver() { // from class: com.google.android.systemui.communal.dock.conditions.DockCondition.1
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            DockCondition.this.processDockIntent(intent);
        }
    };

    public final void processDockIntent(Intent intent) {
        if (intent != null) {
            boolean z = false;
            int intExtra = intent.getIntExtra("android.intent.extra.DOCK_STATE", 0);
            if (intExtra != 0) {
                z = true;
            }
            if (DEBUG) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("onReceive: intent=");
                m.append(intent.getAction());
                m.append(" dock state=");
                m.append(intExtra);
                m.append(" docked=");
                KeyguardUpdateMonitor$18$$ExternalSyntheticOutline0.m(m, z, "DockCondition");
            }
            updateCondition(z);
        } else if (DEBUG) {
            Log.d("DockCondition", "null intent. ignoring");
        }
    }

    @Override // com.android.systemui.util.condition.Condition
    public final void start() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.DOCK_EVENT");
        processDockIntent(this.mContext.registerReceiver(this.mReceiver, intentFilter));
    }

    @Override // com.android.systemui.util.condition.Condition
    public final void stop() {
        this.mContext.unregisterReceiver(this.mReceiver);
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.google.android.systemui.communal.dock.conditions.DockCondition$1] */
    public DockCondition(Context context) {
        this.mContext = context;
    }
}

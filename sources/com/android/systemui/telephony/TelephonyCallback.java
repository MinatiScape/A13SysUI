package com.android.systemui.telephony;

import android.telephony.ServiceState;
import android.telephony.TelephonyCallback;
import com.android.systemui.doze.DozeTriggers$$ExternalSyntheticLambda4;
import java.util.ArrayList;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final class TelephonyCallback extends android.telephony.TelephonyCallback implements TelephonyCallback.ActiveDataSubscriptionIdListener, TelephonyCallback.CallStateListener, TelephonyCallback.ServiceStateListener {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final ArrayList mActiveDataSubscriptionIdListeners = new ArrayList();
    public final ArrayList mCallStateListeners = new ArrayList();
    public final ArrayList mServiceStateListeners = new ArrayList();

    public final boolean hasAnyListeners() {
        if (!this.mActiveDataSubscriptionIdListeners.isEmpty() || !this.mCallStateListeners.isEmpty() || !this.mServiceStateListeners.isEmpty()) {
            return true;
        }
        return false;
    }

    public final void onActiveDataSubscriptionIdChanged(final int i) {
        ArrayList arrayList;
        synchronized (this.mActiveDataSubscriptionIdListeners) {
            arrayList = new ArrayList(this.mActiveDataSubscriptionIdListeners);
        }
        arrayList.forEach(new Consumer() { // from class: com.android.systemui.telephony.TelephonyCallback$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((TelephonyCallback.ActiveDataSubscriptionIdListener) obj).onActiveDataSubscriptionIdChanged(i);
            }
        });
    }

    public final void onCallStateChanged(final int i) {
        ArrayList arrayList;
        synchronized (this.mCallStateListeners) {
            arrayList = new ArrayList(this.mCallStateListeners);
        }
        arrayList.forEach(new Consumer() { // from class: com.android.systemui.telephony.TelephonyCallback$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ((TelephonyCallback.CallStateListener) obj).onCallStateChanged(i);
            }
        });
    }

    public final void onServiceStateChanged(ServiceState serviceState) {
        ArrayList arrayList;
        synchronized (this.mServiceStateListeners) {
            arrayList = new ArrayList(this.mServiceStateListeners);
        }
        arrayList.forEach(new DozeTriggers$$ExternalSyntheticLambda4(serviceState, 2));
    }
}

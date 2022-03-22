package com.android.systemui.statusbar.policy;

import android.content.Context;
import android.net.NetworkPolicyManager;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import com.android.systemui.statusbar.policy.DataSaverController;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class DataSaverControllerImpl implements DataSaverController {
    public final Handler mHandler = new Handler(Looper.getMainLooper());
    public final ArrayList<DataSaverController.Listener> mListeners = new ArrayList<>();
    public final AnonymousClass1 mPolicyListener = new AnonymousClass1();
    public final NetworkPolicyManager mPolicyManager;

    /* renamed from: com.android.systemui.statusbar.policy.DataSaverControllerImpl$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends NetworkPolicyManager.Listener {
        public AnonymousClass1() {
        }

        public final void onRestrictBackgroundChanged(final boolean z) {
            DataSaverControllerImpl.this.mHandler.post(new Runnable() { // from class: com.android.systemui.statusbar.policy.DataSaverControllerImpl.1.1
                @Override // java.lang.Runnable
                public final void run() {
                    DataSaverControllerImpl dataSaverControllerImpl = DataSaverControllerImpl.this;
                    boolean z2 = z;
                    Objects.requireNonNull(dataSaverControllerImpl);
                    synchronized (dataSaverControllerImpl.mListeners) {
                        for (int i = 0; i < dataSaverControllerImpl.mListeners.size(); i++) {
                            dataSaverControllerImpl.mListeners.get(i).onDataSaverChanged(z2);
                        }
                    }
                }
            });
        }
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void addCallback(DataSaverController.Listener listener) {
        DataSaverController.Listener listener2 = listener;
        synchronized (this.mListeners) {
            this.mListeners.add(listener2);
            if (this.mListeners.size() == 1) {
                this.mPolicyManager.registerListener(this.mPolicyListener);
            }
        }
        listener2.onDataSaverChanged(isDataSaverEnabled());
    }

    @Override // com.android.systemui.statusbar.policy.DataSaverController
    public final boolean isDataSaverEnabled() {
        return this.mPolicyManager.getRestrictBackground();
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void removeCallback(DataSaverController.Listener listener) {
        DataSaverController.Listener listener2 = listener;
        synchronized (this.mListeners) {
            this.mListeners.remove(listener2);
            if (this.mListeners.size() == 0) {
                this.mPolicyManager.unregisterListener(this.mPolicyListener);
            }
        }
    }

    @Override // com.android.systemui.statusbar.policy.DataSaverController
    public final void setDataSaverEnabled(boolean z) {
        this.mPolicyManager.setRestrictBackground(z);
        try {
            this.mPolicyListener.onRestrictBackgroundChanged(z);
        } catch (RemoteException unused) {
        }
    }

    public DataSaverControllerImpl(Context context) {
        this.mPolicyManager = NetworkPolicyManager.from(context);
    }
}

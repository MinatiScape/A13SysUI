package com.google.android.systemui.columbus;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
/* compiled from: ColumbusServiceProxy.kt */
/* loaded from: classes.dex */
public final class ColumbusServiceProxy extends Service {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final ArrayList columbusServiceListeners = new ArrayList();
    public final ColumbusServiceProxy$binder$1 binder = new ColumbusServiceProxy$binder$1(this);

    /* compiled from: ColumbusServiceProxy.kt */
    /* loaded from: classes.dex */
    public static final class ColumbusServiceListener implements IBinder.DeathRecipient {
        public IColumbusServiceListener listener;
        public IBinder token;

        @Override // android.os.IBinder.DeathRecipient
        public final void binderDied() {
            Log.w("Columbus/ColumbusProxy", "ColumbusServiceListener binder died");
            this.token = null;
            this.listener = null;
        }

        public ColumbusServiceListener(IBinder iBinder, IColumbusServiceListener iColumbusServiceListener) {
            this.token = iBinder;
            this.listener = iColumbusServiceListener;
            if (iBinder != null) {
                try {
                    iBinder.linkToDeath(this, 0);
                } catch (RemoteException e) {
                    Log.e("Columbus/ColumbusProxy", "Unable to linkToDeath", e);
                }
            }
        }
    }

    @Override // android.app.Service
    public final int onStartCommand(Intent intent, int i, int i2) {
        return 0;
    }

    @Override // android.app.Service
    public final IBinder onBind(Intent intent) {
        return this.binder;
    }
}

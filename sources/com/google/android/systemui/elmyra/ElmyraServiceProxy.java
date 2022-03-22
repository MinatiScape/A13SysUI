package com.google.android.systemui.elmyra;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.systemui.elmyra.IElmyraService;
import com.google.android.systemui.elmyra.IElmyraServiceListener;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public class ElmyraServiceProxy extends Service {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final ArrayList mElmyraServiceListeners = new ArrayList();
    public final AnonymousClass1 mBinder = new AnonymousClass1();

    /* renamed from: com.google.android.systemui.elmyra.ElmyraServiceProxy$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends IElmyraService.Stub {
        public AnonymousClass1() {
        }

        @Override // com.google.android.systemui.elmyra.IElmyraService
        public final void registerServiceListener(IBinder iBinder, IBinder iBinder2) {
            IElmyraServiceListener iElmyraServiceListener;
            ElmyraServiceProxy elmyraServiceProxy = ElmyraServiceProxy.this;
            int i = ElmyraServiceProxy.$r8$clinit;
            Objects.requireNonNull(elmyraServiceProxy);
            elmyraServiceProxy.enforceCallingOrSelfPermission("com.google.android.elmyra.permission.CONFIGURE_ASSIST_GESTURE", "Must have com.google.android.elmyra.permission.CONFIGURE_ASSIST_GESTURE permission");
            if (iBinder == null) {
                Log.e("Elmyra/ElmyraServiceProxy", "Binder token must not be null");
            } else if (iBinder2 == null) {
                for (int i2 = 0; i2 < ElmyraServiceProxy.this.mElmyraServiceListeners.size(); i2++) {
                    ElmyraServiceListener elmyraServiceListener = (ElmyraServiceListener) ElmyraServiceProxy.this.mElmyraServiceListeners.get(i2);
                    Objects.requireNonNull(elmyraServiceListener);
                    if (iBinder.equals(elmyraServiceListener.mToken)) {
                        ElmyraServiceListener elmyraServiceListener2 = (ElmyraServiceListener) ElmyraServiceProxy.this.mElmyraServiceListeners.get(i2);
                        Objects.requireNonNull(elmyraServiceListener2);
                        IBinder iBinder3 = elmyraServiceListener2.mToken;
                        if (iBinder3 != null) {
                            iBinder3.unlinkToDeath(elmyraServiceListener2, 0);
                        }
                        ElmyraServiceProxy.this.mElmyraServiceListeners.remove(i2);
                        return;
                    }
                }
            } else {
                ArrayList arrayList = ElmyraServiceProxy.this.mElmyraServiceListeners;
                IInterface queryLocalInterface = iBinder2.queryLocalInterface("com.google.android.systemui.elmyra.IElmyraServiceListener");
                if (queryLocalInterface == null || !(queryLocalInterface instanceof IElmyraServiceListener)) {
                    iElmyraServiceListener = new IElmyraServiceListener.Stub.Proxy(iBinder2);
                } else {
                    iElmyraServiceListener = (IElmyraServiceListener) queryLocalInterface;
                }
                arrayList.add(new ElmyraServiceListener(iBinder, iElmyraServiceListener));
            }
        }
    }

    /* loaded from: classes.dex */
    public class ElmyraServiceListener implements IBinder.DeathRecipient {
        public IElmyraServiceListener mListener;
        public IBinder mToken;

        @Override // android.os.IBinder.DeathRecipient
        public final void binderDied() {
            Log.w("Elmyra/ElmyraServiceProxy", "ElmyraServiceListener binder died");
            this.mToken = null;
            this.mListener = null;
        }

        public ElmyraServiceListener(IBinder iBinder, IElmyraServiceListener iElmyraServiceListener) {
            this.mToken = iBinder;
            this.mListener = iElmyraServiceListener;
            if (iBinder != null) {
                try {
                    iBinder.linkToDeath(this, 0);
                } catch (RemoteException e) {
                    Log.e("Elmyra/ElmyraServiceProxy", "Unable to linkToDeath", e);
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
        return this.mBinder;
    }
}

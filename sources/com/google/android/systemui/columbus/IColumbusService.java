package com.google.android.systemui.columbus;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.systemui.columbus.ColumbusServiceProxy;
import java.util.Objects;
/* loaded from: classes.dex */
public interface IColumbusService extends IInterface {

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IColumbusService {
        public static final /* synthetic */ int $r8$clinit = 0;

        @Override // android.os.IInterface
        public final IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public final boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface("com.google.android.systemui.columbus.IColumbusService");
            }
            if (i != 1598968902) {
                if (i == 1) {
                    IBinder readStrongBinder = parcel.readStrongBinder();
                    IBinder readStrongBinder2 = parcel.readStrongBinder();
                    parcel.enforceNoDataAvail();
                    ColumbusServiceProxy$binder$1 columbusServiceProxy$binder$1 = (ColumbusServiceProxy$binder$1) this;
                    ColumbusServiceProxy columbusServiceProxy = columbusServiceProxy$binder$1.this$0;
                    int i3 = ColumbusServiceProxy.$r8$clinit;
                    Objects.requireNonNull(columbusServiceProxy);
                    columbusServiceProxy.enforceCallingOrSelfPermission("com.google.android.columbus.permission.CONFIGURE_COLUMBUS_GESTURE", "Must have com.google.android.columbus.permission.CONFIGURE_COLUMBUS_GESTURE permission");
                    int size = columbusServiceProxy$binder$1.this$0.columbusServiceListeners.size() - 1;
                    if (size >= 0) {
                        while (true) {
                            int i4 = size - 1;
                            ColumbusServiceProxy.ColumbusServiceListener columbusServiceListener = (ColumbusServiceProxy.ColumbusServiceListener) columbusServiceProxy$binder$1.this$0.columbusServiceListeners.get(size);
                            Objects.requireNonNull(columbusServiceListener);
                            IColumbusServiceListener iColumbusServiceListener = columbusServiceListener.listener;
                            if (iColumbusServiceListener == null) {
                                columbusServiceProxy$binder$1.this$0.columbusServiceListeners.remove(size);
                            } else {
                                try {
                                    iColumbusServiceListener.setListener(readStrongBinder, readStrongBinder2);
                                } catch (RemoteException e) {
                                    Log.e("Columbus/ColumbusProxy", "Cannot set listener", e);
                                    columbusServiceProxy$binder$1.this$0.columbusServiceListeners.remove(size);
                                }
                            }
                            if (i4 < 0) {
                                break;
                            }
                            size = i4;
                        }
                    }
                } else if (i != 2) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    IBinder readStrongBinder3 = parcel.readStrongBinder();
                    IBinder readStrongBinder4 = parcel.readStrongBinder();
                    parcel.enforceNoDataAvail();
                    ((ColumbusServiceProxy$binder$1) this).registerServiceListener(readStrongBinder3, readStrongBinder4);
                }
                return true;
            }
            parcel2.writeString("com.google.android.systemui.columbus.IColumbusService");
            return true;
        }

        /* loaded from: classes.dex */
        public static class Proxy implements IColumbusService {
            public IBinder mRemote;

            public Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // com.google.android.systemui.columbus.IColumbusService
            public final void registerServiceListener(IBinder iBinder, IBinder iBinder2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.systemui.columbus.IColumbusService");
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeStrongBinder(iBinder2);
                    this.mRemote.transact(2, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.os.IInterface
            public final IBinder asBinder() {
                return this.mRemote;
            }
        }

        public Stub() {
            attachInterface(this, "com.google.android.systemui.columbus.IColumbusService");
        }
    }

    void registerServiceListener(IBinder iBinder, IBinder iBinder2) throws RemoteException;
}

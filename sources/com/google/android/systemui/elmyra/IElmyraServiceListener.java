package com.google.android.systemui.elmyra;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.systemui.elmyra.actions.ServiceAction;
/* loaded from: classes.dex */
public interface IElmyraServiceListener extends IInterface {

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IElmyraServiceListener {
        @Override // android.os.IInterface
        public final IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public final boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface("com.google.android.systemui.elmyra.IElmyraServiceListener");
            }
            if (i != 1598968902) {
                if (i == 1) {
                    IBinder readStrongBinder = parcel.readStrongBinder();
                    IBinder readStrongBinder2 = parcel.readStrongBinder();
                    parcel.enforceNoDataAvail();
                    ((ServiceAction.ElmyraServiceListener) this).setListener(readStrongBinder, readStrongBinder2);
                } else if (i != 2) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    ((ServiceAction.ElmyraServiceListener) this).triggerAction();
                }
                return true;
            }
            parcel2.writeString("com.google.android.systemui.elmyra.IElmyraServiceListener");
            return true;
        }

        /* loaded from: classes.dex */
        public static class Proxy implements IElmyraServiceListener {
            public IBinder mRemote;

            public Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // com.google.android.systemui.elmyra.IElmyraServiceListener
            public final void setListener(IBinder iBinder, IBinder iBinder2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.systemui.elmyra.IElmyraServiceListener");
                    obtain.writeStrongBinder(iBinder);
                    obtain.writeStrongBinder(iBinder2);
                    this.mRemote.transact(1, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.google.android.systemui.elmyra.IElmyraServiceListener
            public final void triggerAction() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.systemui.elmyra.IElmyraServiceListener");
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
            attachInterface(this, "com.google.android.systemui.elmyra.IElmyraServiceListener");
        }
    }

    void setListener(IBinder iBinder, IBinder iBinder2) throws RemoteException;

    void triggerAction() throws RemoteException;
}

package com.google.android.setupcompat;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface ISetupCompatService extends IInterface {

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements ISetupCompatService {
        public static final /* synthetic */ int $r8$clinit = 0;

        /* loaded from: classes.dex */
        public static class Proxy implements ISetupCompatService {
            public IBinder mRemote;

            @Override // com.google.android.setupcompat.ISetupCompatService
            public final void logMetric(int i, Bundle bundle) throws RemoteException {
                Bundle bundle2 = Bundle.EMPTY;
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.setupcompat.ISetupCompatService");
                    obtain.writeInt(i);
                    _Parcel.m162$$Nest$smwriteTypedObject(obtain, bundle);
                    _Parcel.m162$$Nest$smwriteTypedObject(obtain, bundle2);
                    this.mRemote.transact(2, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            public Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // com.google.android.setupcompat.ISetupCompatService
            public final void onFocusStatusChanged(Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.setupcompat.ISetupCompatService");
                    _Parcel.m162$$Nest$smwriteTypedObject(obtain, bundle);
                    this.mRemote.transact(3, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // com.google.android.setupcompat.ISetupCompatService
            public final void validateActivity(String str, Bundle bundle) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("com.google.android.setupcompat.ISetupCompatService");
                    obtain.writeString(str);
                    _Parcel.m162$$Nest$smwriteTypedObject(obtain, bundle);
                    this.mRemote.transact(1, obtain, null, 1);
                } finally {
                    obtain.recycle();
                }
            }

            @Override // android.os.IInterface
            public final IBinder asBinder() {
                return this.mRemote;
            }
        }
    }

    /* loaded from: classes.dex */
    public static class _Parcel {
        /* renamed from: -$$Nest$smwriteTypedObject  reason: not valid java name */
        public static void m162$$Nest$smwriteTypedObject(Parcel parcel, Parcelable parcelable) {
            if (parcelable != null) {
                parcel.writeInt(1);
                parcelable.writeToParcel(parcel, 0);
                return;
            }
            parcel.writeInt(0);
        }
    }

    void logMetric(int i, Bundle bundle) throws RemoteException;

    void onFocusStatusChanged(Bundle bundle) throws RemoteException;

    void validateActivity(String str, Bundle bundle) throws RemoteException;
}

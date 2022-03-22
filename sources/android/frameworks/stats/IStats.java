package android.frameworks.stats;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public interface IStats extends IInterface {
    public static final String DESCRIPTOR = "android$frameworks$stats$IStats".replace('$', '.');

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IStats {
        public static final /* synthetic */ int $r8$clinit = 0;

        /* loaded from: classes.dex */
        public static class Proxy implements IStats {
            public IBinder mRemote;

            public Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.frameworks.stats.IStats
            public final void reportVendorAtom(VendorAtom vendorAtom) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(IStats.DESCRIPTOR);
                    obtain.writeTypedObject(vendorAtom, 0);
                    if (!this.mRemote.transact(1, obtain, null, 1)) {
                        throw new RemoteException("Method reportVendorAtom is unimplemented.");
                    }
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

    void reportVendorAtom(VendorAtom vendorAtom) throws RemoteException;
}

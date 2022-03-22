package android.support.v4.os;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.v4.os.ResultReceiver;
import java.util.Objects;
/* loaded from: classes.dex */
public interface IResultReceiver extends IInterface {

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IResultReceiver {
        public static final /* synthetic */ int $r8$clinit = 0;

        @Override // android.os.IInterface
        public final IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public final boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            Bundle bundle;
            if (i == 1) {
                parcel.enforceInterface("android.support.v4.os.IResultReceiver");
                int readInt = parcel.readInt();
                if (parcel.readInt() != 0) {
                    bundle = (Bundle) Bundle.CREATOR.createFromParcel(parcel);
                } else {
                    bundle = null;
                }
                ResultReceiver.MyResultReceiver myResultReceiver = (ResultReceiver.MyResultReceiver) this;
                Objects.requireNonNull(ResultReceiver.this);
                ResultReceiver.this.onReceiveResult(readInt, bundle);
                return true;
            } else if (i != 1598968902) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel2.writeString("android.support.v4.os.IResultReceiver");
                return true;
            }
        }

        /* loaded from: classes.dex */
        public static class Proxy implements IResultReceiver {
            public IBinder mRemote;

            public Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            @Override // android.os.IInterface
            public final IBinder asBinder() {
                return this.mRemote;
            }
        }

        public Stub() {
            attachInterface(this, "android.support.v4.os.IResultReceiver");
        }
    }
}

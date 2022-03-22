package com.android.wm.shell.startingsurface;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.wm.shell.common.ExecutorUtils;
import com.android.wm.shell.startingsurface.StartingWindowController;
import java.util.Objects;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public interface IStartingWindow extends IInterface {

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IStartingWindow {
        @Override // android.os.IInterface
        public final IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public final boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            final IStartingWindowListener iStartingWindowListener;
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface("com.android.wm.shell.startingsurface.IStartingWindow");
            }
            if (i == 1598968902) {
                parcel2.writeString("com.android.wm.shell.startingsurface.IStartingWindow");
                return true;
            } else if (i != 44) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                final IBinder readStrongBinder = parcel.readStrongBinder();
                if (readStrongBinder == null) {
                    iStartingWindowListener = null;
                } else {
                    IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.android.wm.shell.startingsurface.IStartingWindowListener");
                    if (queryLocalInterface == null || !(queryLocalInterface instanceof IStartingWindowListener)) {
                        iStartingWindowListener = new IStartingWindowListener(readStrongBinder) { // from class: com.android.wm.shell.startingsurface.IStartingWindowListener$Stub$Proxy
                            public IBinder mRemote;

                            {
                                this.mRemote = readStrongBinder;
                            }

                            @Override // com.android.wm.shell.startingsurface.IStartingWindowListener
                            public final void onTaskLaunching(int i3, int i4, int i5) throws RemoteException {
                                Parcel obtain = Parcel.obtain();
                                try {
                                    obtain.writeInterfaceToken("com.android.wm.shell.startingsurface.IStartingWindowListener");
                                    obtain.writeInt(i3);
                                    obtain.writeInt(i4);
                                    obtain.writeInt(i5);
                                    this.mRemote.transact(1, obtain, null, 1);
                                } finally {
                                    obtain.recycle();
                                }
                            }

                            @Override // android.os.IInterface
                            public final IBinder asBinder() {
                                return this.mRemote;
                            }
                        };
                    } else {
                        iStartingWindowListener = (IStartingWindowListener) queryLocalInterface;
                    }
                }
                parcel.enforceNoDataAvail();
                final StartingWindowController.IStartingWindowImpl iStartingWindowImpl = (StartingWindowController.IStartingWindowImpl) this;
                ExecutorUtils.executeRemoteCallWithTaskPermission(iStartingWindowImpl.mController, "setStartingWindowListener", new Consumer() { // from class: com.android.wm.shell.startingsurface.StartingWindowController$IStartingWindowImpl$$ExternalSyntheticLambda1
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        StartingWindowController.IStartingWindowImpl iStartingWindowImpl2 = StartingWindowController.IStartingWindowImpl.this;
                        IStartingWindowListener iStartingWindowListener2 = iStartingWindowListener;
                        StartingWindowController startingWindowController = (StartingWindowController) obj;
                        Objects.requireNonNull(iStartingWindowImpl2);
                        if (iStartingWindowListener2 != null) {
                            iStartingWindowImpl2.mListener.register(iStartingWindowListener2);
                        } else {
                            iStartingWindowImpl2.mListener.unregister();
                        }
                    }
                }, false);
                return true;
            }
        }

        public Stub() {
            attachInterface(this, "com.android.wm.shell.startingsurface.IStartingWindow");
        }
    }
}

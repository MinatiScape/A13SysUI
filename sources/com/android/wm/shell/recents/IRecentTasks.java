package com.android.wm.shell.recents;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.wm.shell.ShellTaskOrganizer$$ExternalSyntheticLambda0;
import com.android.wm.shell.common.ExecutorUtils;
import com.android.wm.shell.recents.RecentTasksController;
import com.android.wm.shell.util.GroupedRecentTaskInfo;
import java.util.Objects;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public interface IRecentTasks extends IInterface {

    /* loaded from: classes.dex */
    public static abstract class Stub extends Binder implements IRecentTasks {
        @Override // android.os.IInterface
        public final IBinder asBinder() {
            return this;
        }

        @Override // android.os.Binder
        public final boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            IInterface queryLocalInterface;
            GroupedRecentTaskInfo[] groupedRecentTaskInfoArr;
            if (i >= 1 && i <= 16777215) {
                parcel.enforceInterface("com.android.wm.shell.recents.IRecentTasks");
            }
            if (i != 1598968902) {
                final IRecentTasksListener iRecentTasksListener = null;
                if (i == 2) {
                    final IBinder readStrongBinder = parcel.readStrongBinder();
                    if (readStrongBinder != null) {
                        IInterface queryLocalInterface2 = readStrongBinder.queryLocalInterface("com.android.wm.shell.recents.IRecentTasksListener");
                        if (queryLocalInterface2 == null || !(queryLocalInterface2 instanceof IRecentTasksListener)) {
                            iRecentTasksListener = new IRecentTasksListener(readStrongBinder) { // from class: com.android.wm.shell.recents.IRecentTasksListener$Stub$Proxy
                                public IBinder mRemote;

                                {
                                    this.mRemote = readStrongBinder;
                                }

                                @Override // com.android.wm.shell.recents.IRecentTasksListener
                                public final void onRecentTasksChanged() throws RemoteException {
                                    Parcel obtain = Parcel.obtain();
                                    try {
                                        obtain.writeInterfaceToken("com.android.wm.shell.recents.IRecentTasksListener");
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
                            iRecentTasksListener = (IRecentTasksListener) queryLocalInterface2;
                        }
                    }
                    parcel.enforceNoDataAvail();
                    final RecentTasksController.IRecentTasksImpl iRecentTasksImpl = (RecentTasksController.IRecentTasksImpl) this;
                    ExecutorUtils.executeRemoteCallWithTaskPermission(iRecentTasksImpl.mController, "registerRecentTasksListener", new Consumer() { // from class: com.android.wm.shell.recents.RecentTasksController$IRecentTasksImpl$$ExternalSyntheticLambda0
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            RecentTasksController.IRecentTasksImpl iRecentTasksImpl2 = RecentTasksController.IRecentTasksImpl.this;
                            IRecentTasksListener iRecentTasksListener2 = iRecentTasksListener;
                            RecentTasksController recentTasksController = (RecentTasksController) obj;
                            Objects.requireNonNull(iRecentTasksImpl2);
                            iRecentTasksImpl2.mListener.register(iRecentTasksListener2);
                        }
                    }, false);
                } else if (i == 3) {
                    IBinder readStrongBinder2 = parcel.readStrongBinder();
                    if (!(readStrongBinder2 == null || (queryLocalInterface = readStrongBinder2.queryLocalInterface("com.android.wm.shell.recents.IRecentTasksListener")) == null || !(queryLocalInterface instanceof IRecentTasksListener))) {
                        IRecentTasksListener iRecentTasksListener2 = (IRecentTasksListener) queryLocalInterface;
                    }
                    parcel.enforceNoDataAvail();
                    RecentTasksController.IRecentTasksImpl iRecentTasksImpl2 = (RecentTasksController.IRecentTasksImpl) this;
                    ExecutorUtils.executeRemoteCallWithTaskPermission(iRecentTasksImpl2.mController, "unregisterRecentTasksListener", new ShellTaskOrganizer$$ExternalSyntheticLambda0(iRecentTasksImpl2, 5), false);
                } else if (i != 4) {
                    return super.onTransact(i, parcel, parcel2, i2);
                } else {
                    final int readInt = parcel.readInt();
                    final int readInt2 = parcel.readInt();
                    final int readInt3 = parcel.readInt();
                    parcel.enforceNoDataAvail();
                    RecentTasksController recentTasksController = ((RecentTasksController.IRecentTasksImpl) this).mController;
                    if (recentTasksController == null) {
                        groupedRecentTaskInfoArr = new GroupedRecentTaskInfo[0];
                    } else {
                        final GroupedRecentTaskInfo[][] groupedRecentTaskInfoArr2 = {null};
                        ExecutorUtils.executeRemoteCallWithTaskPermission(recentTasksController, "getRecentTasks", new Consumer() { // from class: com.android.wm.shell.recents.RecentTasksController$IRecentTasksImpl$$ExternalSyntheticLambda1
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj) {
                                groupedRecentTaskInfoArr2[0] = (GroupedRecentTaskInfo[]) ((RecentTasksController) obj).getRecentTasks(readInt, readInt2, readInt3).toArray(new GroupedRecentTaskInfo[0]);
                            }
                        }, true);
                        groupedRecentTaskInfoArr = groupedRecentTaskInfoArr2[0];
                    }
                    parcel2.writeNoException();
                    parcel2.writeTypedArray(groupedRecentTaskInfoArr, 1);
                }
                return true;
            }
            parcel2.writeString("com.android.wm.shell.recents.IRecentTasks");
            return true;
        }

        public Stub() {
            attachInterface(this, "com.android.wm.shell.recents.IRecentTasks");
        }
    }
}

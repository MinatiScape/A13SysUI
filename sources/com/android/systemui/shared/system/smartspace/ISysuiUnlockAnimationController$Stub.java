package com.android.systemui.shared.system.smartspace;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import com.android.systemui.keyguard.KeyguardUnlockAnimationController;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class ISysuiUnlockAnimationController$Stub extends Binder implements IInterface {
    @Override // android.os.IInterface
    public final IBinder asBinder() {
        return this;
    }

    @Override // android.os.Binder
    public final boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        ILauncherUnlockAnimationController iLauncherUnlockAnimationController;
        IBinder asBinder;
        if (i >= 1 && i <= 16777215) {
            parcel.enforceInterface("com.android.systemui.shared.system.smartspace.ISysuiUnlockAnimationController");
        }
        if (i != 1598968902) {
            if (i == 1) {
                final IBinder readStrongBinder = parcel.readStrongBinder();
                if (readStrongBinder == null) {
                    iLauncherUnlockAnimationController = null;
                } else {
                    IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.android.systemui.shared.system.smartspace.ILauncherUnlockAnimationController");
                    if (queryLocalInterface == null || !(queryLocalInterface instanceof ILauncherUnlockAnimationController)) {
                        iLauncherUnlockAnimationController = new ILauncherUnlockAnimationController(readStrongBinder) { // from class: com.android.systemui.shared.system.smartspace.ILauncherUnlockAnimationController$Stub$Proxy
                            public IBinder mRemote;

                            {
                                this.mRemote = readStrongBinder;
                            }

                            @Override // com.android.systemui.shared.system.smartspace.ILauncherUnlockAnimationController
                            public final void playUnlockAnimation() throws RemoteException {
                                Parcel obtain = Parcel.obtain();
                                try {
                                    obtain.writeInterfaceToken("com.android.systemui.shared.system.smartspace.ILauncherUnlockAnimationController");
                                    obtain.writeBoolean(true);
                                    obtain.writeLong(200L);
                                    this.mRemote.transact(3, obtain, null, 1);
                                } finally {
                                    obtain.recycle();
                                }
                            }

                            @Override // com.android.systemui.shared.system.smartspace.ILauncherUnlockAnimationController
                            public final void prepareForUnlock(boolean z, int i3) throws RemoteException {
                                Parcel obtain = Parcel.obtain();
                                Parcel obtain2 = Parcel.obtain();
                                try {
                                    obtain.writeInterfaceToken("com.android.systemui.shared.system.smartspace.ILauncherUnlockAnimationController");
                                    obtain.writeBoolean(z);
                                    obtain.writeInt(i3);
                                    this.mRemote.transact(1, obtain, obtain2, 0);
                                    obtain2.readException();
                                } finally {
                                    obtain2.recycle();
                                    obtain.recycle();
                                }
                            }

                            @Override // com.android.systemui.shared.system.smartspace.ILauncherUnlockAnimationController
                            public final void setSmartspaceVisibility(int i3) throws RemoteException {
                                Parcel obtain = Parcel.obtain();
                                Parcel obtain2 = Parcel.obtain();
                                try {
                                    obtain.writeInterfaceToken("com.android.systemui.shared.system.smartspace.ILauncherUnlockAnimationController");
                                    obtain.writeInt(i3);
                                    this.mRemote.transact(5, obtain, obtain2, 0);
                                    obtain2.readException();
                                } finally {
                                    obtain2.recycle();
                                    obtain.recycle();
                                }
                            }

                            @Override // com.android.systemui.shared.system.smartspace.ILauncherUnlockAnimationController
                            public final void setUnlockAmount(float f) throws RemoteException {
                                Parcel obtain = Parcel.obtain();
                                try {
                                    obtain.writeInterfaceToken("com.android.systemui.shared.system.smartspace.ILauncherUnlockAnimationController");
                                    obtain.writeFloat(f);
                                    this.mRemote.transact(2, obtain, null, 1);
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
                        iLauncherUnlockAnimationController = (ILauncherUnlockAnimationController) queryLocalInterface;
                    }
                }
                parcel.enforceNoDataAvail();
                final KeyguardUnlockAnimationController keyguardUnlockAnimationController = (KeyguardUnlockAnimationController) this;
                keyguardUnlockAnimationController.launcherUnlockController = iLauncherUnlockAnimationController;
                if (!(iLauncherUnlockAnimationController == null || (asBinder = iLauncherUnlockAnimationController.asBinder()) == null)) {
                    asBinder.linkToDeath(new IBinder.DeathRecipient() { // from class: com.android.systemui.keyguard.KeyguardUnlockAnimationController$setLauncherUnlockController$1
                        @Override // android.os.IBinder.DeathRecipient
                        public final void binderDied() {
                            KeyguardUnlockAnimationController keyguardUnlockAnimationController2 = KeyguardUnlockAnimationController.this;
                            keyguardUnlockAnimationController2.launcherUnlockController = null;
                            Objects.requireNonNull(keyguardUnlockAnimationController2);
                            keyguardUnlockAnimationController2.launcherSmartspaceState = null;
                        }
                    }, 0);
                }
            } else if (i != 2) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                parcel.enforceNoDataAvail();
                ((KeyguardUnlockAnimationController) this).launcherSmartspaceState = (SmartspaceState) parcel.readTypedObject(SmartspaceState.CREATOR);
            }
            return true;
        }
        parcel2.writeString("com.android.systemui.shared.system.smartspace.ISysuiUnlockAnimationController");
        return true;
    }

    public ISysuiUnlockAnimationController$Stub() {
        attachInterface(this, "com.android.systemui.shared.system.smartspace.ISysuiUnlockAnimationController");
    }
}

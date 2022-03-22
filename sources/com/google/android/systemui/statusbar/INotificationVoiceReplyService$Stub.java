package com.google.android.systemui.statusbar;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
/* loaded from: classes.dex */
public abstract class INotificationVoiceReplyService$Stub extends Binder implements IInterface {
    @Override // android.os.IInterface
    public final IBinder asBinder() {
        return this;
    }

    @Override // android.os.Binder
    public final boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        INotificationVoiceReplyServiceCallbacks iNotificationVoiceReplyServiceCallbacks;
        if (i >= 1 && i <= 16777215) {
            parcel.enforceInterface("com.google.android.systemui.statusbar.INotificationVoiceReplyService");
        }
        if (i != 1598968902) {
            if (i == 1) {
                final IBinder readStrongBinder = parcel.readStrongBinder();
                if (readStrongBinder == null) {
                    iNotificationVoiceReplyServiceCallbacks = null;
                } else {
                    IInterface queryLocalInterface = readStrongBinder.queryLocalInterface("com.google.android.systemui.statusbar.INotificationVoiceReplyServiceCallbacks");
                    if (queryLocalInterface == null || !(queryLocalInterface instanceof INotificationVoiceReplyServiceCallbacks)) {
                        iNotificationVoiceReplyServiceCallbacks = new INotificationVoiceReplyServiceCallbacks(readStrongBinder) { // from class: com.google.android.systemui.statusbar.INotificationVoiceReplyServiceCallbacks$Stub$Proxy
                            public IBinder mRemote;

                            {
                                this.mRemote = readStrongBinder;
                            }

                            @Override // com.google.android.systemui.statusbar.INotificationVoiceReplyServiceCallbacks
                            public final void onNotifAvailableForQuickPhraseChanged(boolean z) throws RemoteException {
                                Parcel obtain = Parcel.obtain();
                                try {
                                    obtain.writeInterfaceToken("com.google.android.systemui.statusbar.INotificationVoiceReplyServiceCallbacks");
                                    obtain.writeBoolean(z);
                                    this.mRemote.transact(3, obtain, null, 1);
                                } finally {
                                    obtain.recycle();
                                }
                            }

                            @Override // com.google.android.systemui.statusbar.INotificationVoiceReplyServiceCallbacks
                            public final void onNotifAvailableForReplyChanged(boolean z) throws RemoteException {
                                Parcel obtain = Parcel.obtain();
                                try {
                                    obtain.writeInterfaceToken("com.google.android.systemui.statusbar.INotificationVoiceReplyServiceCallbacks");
                                    obtain.writeBoolean(z);
                                    this.mRemote.transact(1, obtain, null, 1);
                                } finally {
                                    obtain.recycle();
                                }
                            }

                            @Override // com.google.android.systemui.statusbar.INotificationVoiceReplyServiceCallbacks
                            public final void onVoiceReplyHandled(int i3, int i4) throws RemoteException {
                                Parcel obtain = Parcel.obtain();
                                try {
                                    obtain.writeInterfaceToken("com.google.android.systemui.statusbar.INotificationVoiceReplyServiceCallbacks");
                                    obtain.writeInt(i3);
                                    obtain.writeInt(i4);
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
                        iNotificationVoiceReplyServiceCallbacks = (INotificationVoiceReplyServiceCallbacks) queryLocalInterface;
                    }
                }
                parcel.enforceNoDataAvail();
                NotificationVoiceReplyManagerService$binder$1 notificationVoiceReplyManagerService$binder$1 = (NotificationVoiceReplyManagerService$binder$1) this;
                NotificationVoiceReplyManagerService.access$ensureCallerIsAgsa(notificationVoiceReplyManagerService$binder$1.this$0);
                NotificationVoiceReplyManagerService notificationVoiceReplyManagerService = notificationVoiceReplyManagerService$binder$1.this$0;
                NotificationVoiceReplyManagerService.access$serially(notificationVoiceReplyManagerService, new NotificationVoiceReplyManagerService$binder$1$registerCallbacks$1(notificationVoiceReplyManagerService, notificationVoiceReplyManagerService$binder$1, iNotificationVoiceReplyServiceCallbacks, null));
            } else if (i == 2) {
                int readInt = parcel.readInt();
                String readString = parcel.readString();
                parcel.enforceNoDataAvail();
                NotificationVoiceReplyManagerService$binder$1 notificationVoiceReplyManagerService$binder$12 = (NotificationVoiceReplyManagerService$binder$1) this;
                NotificationVoiceReplyManagerService.access$ensureCallerIsAgsa(notificationVoiceReplyManagerService$binder$12.this$0);
                NotificationVoiceReplyManagerService notificationVoiceReplyManagerService2 = notificationVoiceReplyManagerService$binder$12.this$0;
                NotificationVoiceReplyManagerService.access$serially(notificationVoiceReplyManagerService2, new NotificationVoiceReplyManagerService$binder$1$startVoiceReply$1(notificationVoiceReplyManagerService2, notificationVoiceReplyManagerService$binder$12, readInt, readString, null));
            } else if (i == 3) {
                int readInt2 = parcel.readInt();
                int readInt3 = parcel.readInt();
                parcel.enforceNoDataAvail();
                NotificationVoiceReplyManagerService$binder$1 notificationVoiceReplyManagerService$binder$13 = (NotificationVoiceReplyManagerService$binder$1) this;
                NotificationVoiceReplyManagerService.access$ensureCallerIsAgsa(notificationVoiceReplyManagerService$binder$13.this$0);
                NotificationVoiceReplyManagerService notificationVoiceReplyManagerService3 = notificationVoiceReplyManagerService$binder$13.this$0;
                NotificationVoiceReplyManagerService.access$serially(notificationVoiceReplyManagerService3, new NotificationVoiceReplyManagerService$binder$1$onVoiceAuthStateChanged$1(notificationVoiceReplyManagerService3, notificationVoiceReplyManagerService$binder$13, readInt2, readInt3, null));
            } else if (i == 4) {
                int readInt4 = parcel.readInt();
                parcel.enforceNoDataAvail();
                NotificationVoiceReplyManagerService$binder$1 notificationVoiceReplyManagerService$binder$14 = (NotificationVoiceReplyManagerService$binder$1) this;
                NotificationVoiceReplyManagerService.access$ensureCallerIsAgsa(notificationVoiceReplyManagerService$binder$14.this$0);
                NotificationVoiceReplyManagerService notificationVoiceReplyManagerService4 = notificationVoiceReplyManagerService$binder$14.this$0;
                NotificationVoiceReplyManagerService.access$serially(notificationVoiceReplyManagerService4, new NotificationVoiceReplyManagerService$binder$1$setFeatureEnabled$1(notificationVoiceReplyManagerService4, notificationVoiceReplyManagerService$binder$14, readInt4, null));
            } else if (i != 5) {
                return super.onTransact(i, parcel, parcel2, i2);
            } else {
                NotificationVoiceReplyManagerService$binder$1 notificationVoiceReplyManagerService$binder$15 = (NotificationVoiceReplyManagerService$binder$1) this;
                NotificationVoiceReplyManagerService.access$ensureCallerIsAgsa(notificationVoiceReplyManagerService$binder$15.this$0);
                NotificationVoiceReplyManagerService notificationVoiceReplyManagerService5 = notificationVoiceReplyManagerService$binder$15.this$0;
                NotificationVoiceReplyManagerService.access$serially(notificationVoiceReplyManagerService5, new NotificationVoiceReplyManagerService$binder$1$hideVisibleQuickPhraseCta$1(notificationVoiceReplyManagerService5, null));
            }
            return true;
        }
        parcel2.writeString("com.google.android.systemui.statusbar.INotificationVoiceReplyService");
        return true;
    }

    public INotificationVoiceReplyService$Stub() {
        attachInterface(this, "com.google.android.systemui.statusbar.INotificationVoiceReplyService");
    }
}

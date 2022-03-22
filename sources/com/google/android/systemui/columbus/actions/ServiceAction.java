package com.google.android.systemui.columbus.actions;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.systemui.columbus.ColumbusServiceProxy;
import com.google.android.systemui.columbus.IColumbusService;
import com.google.android.systemui.columbus.IColumbusServiceGestureListener;
import com.google.android.systemui.columbus.IColumbusServiceListener;
import java.util.Objects;
import kotlin.collections.EmptySet;
/* compiled from: ServiceAction.kt */
/* loaded from: classes.dex */
public abstract class ServiceAction extends Action implements IBinder.DeathRecipient {
    public IColumbusService columbusService;
    public IColumbusServiceGestureListener columbusServiceGestureListener;
    public final ColumbusServiceListener columbusServiceListener;
    public final Binder token = new Binder();

    /* compiled from: ServiceAction.kt */
    /* loaded from: classes.dex */
    public final class ColumbusServiceConnection implements ServiceConnection {
        public final /* synthetic */ ServiceAction this$0;

        public ColumbusServiceConnection(SettingsAction settingsAction) {
            this.this$0 = settingsAction;
        }

        @Override // android.content.ServiceConnection
        public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            IColumbusService iColumbusService;
            ServiceAction serviceAction = this.this$0;
            int i = IColumbusService.Stub.$r8$clinit;
            if (iBinder == null) {
                iColumbusService = null;
            } else {
                IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.systemui.columbus.IColumbusService");
                if (queryLocalInterface == null || !(queryLocalInterface instanceof IColumbusService)) {
                    iColumbusService = new IColumbusService.Stub.Proxy(iBinder);
                } else {
                    iColumbusService = (IColumbusService) queryLocalInterface;
                }
            }
            serviceAction.columbusService = iColumbusService;
            try {
                ServiceAction serviceAction2 = this.this$0;
                IColumbusService iColumbusService2 = serviceAction2.columbusService;
                if (iColumbusService2 != null) {
                    iColumbusService2.registerServiceListener(serviceAction2.token, serviceAction2.columbusServiceListener);
                }
            } catch (RemoteException e) {
                Log.e("Columbus/ServiceAction", "Error registering listener", e);
            }
            Objects.requireNonNull(this.this$0);
        }

        @Override // android.content.ServiceConnection
        public final void onServiceDisconnected(ComponentName componentName) {
            ServiceAction serviceAction = this.this$0;
            serviceAction.columbusService = null;
            Objects.requireNonNull(serviceAction);
        }
    }

    /* compiled from: ServiceAction.kt */
    /* loaded from: classes.dex */
    public final class ColumbusServiceListener extends IColumbusServiceListener.Stub {
        public final /* synthetic */ ServiceAction this$0;

        public ColumbusServiceListener(SettingsAction settingsAction) {
            this.this$0 = settingsAction;
        }

        /* JADX WARN: Removed duplicated region for block: B:14:0x003e A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:15:0x003f  */
        @Override // com.google.android.systemui.columbus.IColumbusServiceListener
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void setListener(android.os.IBinder r8, final android.os.IBinder r9) {
            /*
                r7 = this;
                java.lang.String r0 = "Columbus/ServiceAction"
                com.google.android.systemui.columbus.actions.ServiceAction r1 = r7.this$0
                java.util.Objects.requireNonNull(r1)
                android.content.Context r2 = r1.context
                android.content.pm.PackageManager r2 = r2.getPackageManager()
                int r3 = android.os.Binder.getCallingUid()
                java.lang.String[] r2 = r2.getPackagesForUid(r3)
                r3 = 0
                r4 = 0
                if (r2 != 0) goto L_0x001a
                goto L_0x003b
            L_0x001a:
                com.google.android.systemui.columbus.actions.SettingsAction r1 = (com.google.android.systemui.columbus.actions.SettingsAction) r1
                java.util.Set<java.lang.String> r1 = r1.supportedCallerPackages
                java.util.Iterator r1 = r1.iterator()
            L_0x0022:
                boolean r5 = r1.hasNext()
                if (r5 == 0) goto L_0x0036
                java.lang.Object r5 = r1.next()
                r6 = r5
                java.lang.String r6 = (java.lang.String) r6
                boolean r6 = kotlin.collections.ArraysKt___ArraysKt.contains(r2, r6)
                if (r6 == 0) goto L_0x0022
                goto L_0x0037
            L_0x0036:
                r5 = r3
            L_0x0037:
                if (r5 == 0) goto L_0x003b
                r1 = 1
                goto L_0x003c
            L_0x003b:
                r1 = r4
            L_0x003c:
                if (r1 != 0) goto L_0x003f
                return
            L_0x003f:
                if (r9 != 0) goto L_0x0048
                com.google.android.systemui.columbus.actions.ServiceAction r1 = r7.this$0
                com.google.android.systemui.columbus.IColumbusServiceGestureListener r1 = r1.columbusServiceGestureListener
                if (r1 != 0) goto L_0x0048
                return
            L_0x0048:
                com.google.android.systemui.columbus.actions.ServiceAction r1 = r7.this$0
                if (r9 != 0) goto L_0x004d
                goto L_0x0062
            L_0x004d:
                java.lang.String r2 = "com.google.android.systemui.columbus.IColumbusServiceGestureListener"
                android.os.IInterface r2 = r9.queryLocalInterface(r2)
                if (r2 == 0) goto L_0x005d
                boolean r3 = r2 instanceof com.google.android.systemui.columbus.IColumbusServiceGestureListener
                if (r3 == 0) goto L_0x005d
                r3 = r2
                com.google.android.systemui.columbus.IColumbusServiceGestureListener r3 = (com.google.android.systemui.columbus.IColumbusServiceGestureListener) r3
                goto L_0x0062
            L_0x005d:
                com.google.android.systemui.columbus.IColumbusServiceGestureListener$Stub$Proxy r3 = new com.google.android.systemui.columbus.IColumbusServiceGestureListener$Stub$Proxy
                r3.<init>(r9)
            L_0x0062:
                r1.columbusServiceGestureListener = r3
                com.google.android.systemui.columbus.actions.ServiceAction r1 = r7.this$0
                r1.updateAvailable()
                if (r8 != 0) goto L_0x006c
                goto L_0x0085
            L_0x006c:
                com.google.android.systemui.columbus.actions.ServiceAction r7 = r7.this$0     // Catch: NoSuchElementException -> 0x0078, RemoteException -> 0x007f
                if (r9 != 0) goto L_0x0074
                r8.unlinkToDeath(r7, r4)     // Catch: NoSuchElementException -> 0x0078, RemoteException -> 0x007f
                goto L_0x0085
            L_0x0074:
                r8.linkToDeath(r7, r4)     // Catch: NoSuchElementException -> 0x0078, RemoteException -> 0x007f
                goto L_0x0085
            L_0x0078:
                r7 = move-exception
                java.lang.String r8 = "NoSuchElementException during linkToDeath"
                android.util.Log.e(r0, r8, r7)
                goto L_0x0085
            L_0x007f:
                r7 = move-exception
                java.lang.String r8 = "RemoteException during linkToDeath"
                android.util.Log.e(r0, r8, r7)
            L_0x0085:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.columbus.actions.ServiceAction.ColumbusServiceListener.setListener(android.os.IBinder, android.os.IBinder):void");
        }
    }

    public ServiceAction(Context context) {
        super(context, null);
        EmptySet emptySet = EmptySet.INSTANCE;
        SettingsAction settingsAction = (SettingsAction) this;
        ColumbusServiceConnection columbusServiceConnection = new ColumbusServiceConnection(settingsAction);
        this.columbusServiceListener = new ColumbusServiceListener(settingsAction);
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(context, ColumbusServiceProxy.class));
            context.bindService(intent, columbusServiceConnection, 1);
        } catch (SecurityException e) {
            Log.e("Columbus/ServiceAction", "Unable to bind to ColumbusServiceProxy", e);
        }
        updateAvailable();
    }

    @Override // android.os.IBinder.DeathRecipient
    public final void binderDied() {
        Log.w("Columbus/ServiceAction", "Binder died");
        this.columbusServiceGestureListener = null;
        updateAvailable();
    }

    public final void updateAvailable() {
        boolean z;
        if (this.columbusServiceGestureListener != null) {
            z = true;
        } else {
            z = false;
        }
        setAvailable(z);
    }
}

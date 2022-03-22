package com.android.systemui.qs.external;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.service.quicksettings.IQSService;
import android.service.quicksettings.IQSTileService;
import android.util.ArraySet;
import android.util.Log;
import com.android.keyguard.KeyguardDisplayManager$$ExternalSyntheticLambda1;
import com.android.systemui.broadcast.BroadcastDispatcher;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: classes.dex */
public final class TileLifecycleManager extends BroadcastReceiver implements IQSTileService, ServiceConnection, IBinder.DeathRecipient {
    public int mBindTryCount;
    public boolean mBound;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public TileChangeListener mChangeListener;
    public IBinder mClickBinder;
    public final Context mContext;
    public final Handler mHandler;
    public final Intent mIntent;
    public boolean mIsBound;
    public boolean mListening;
    public final PackageManagerAdapter mPackageManagerAdapter;
    public final Binder mToken;
    public boolean mUnbindImmediate;
    public final UserHandle mUser;
    public QSTileServiceWrapper mWrapper;
    public ArraySet mQueuedMessages = new ArraySet();
    public int mBindRetryDelay = 1000;
    public AtomicBoolean mPackageReceiverRegistered = new AtomicBoolean(false);
    public AtomicBoolean mUserReceiverRegistered = new AtomicBoolean(false);

    /* loaded from: classes.dex */
    public interface Factory {
        TileLifecycleManager create(Intent intent, UserHandle userHandle);
    }

    /* loaded from: classes.dex */
    public interface TileChangeListener {
    }

    @Override // android.content.ServiceConnection
    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        ArraySet arraySet;
        this.mBindTryCount = 0;
        QSTileServiceWrapper qSTileServiceWrapper = new QSTileServiceWrapper(IQSTileService.Stub.asInterface(iBinder));
        try {
            iBinder.linkToDeath(this, 0);
        } catch (RemoteException unused) {
        }
        this.mWrapper = qSTileServiceWrapper;
        synchronized (this.mQueuedMessages) {
            arraySet = new ArraySet((Collection) this.mQueuedMessages);
            this.mQueuedMessages.clear();
        }
        if (arraySet.contains(0)) {
            onTileAdded();
        }
        if (this.mListening) {
            onStartListening();
        }
        if (arraySet.contains(2)) {
            if (!this.mListening) {
                Log.w("TileLifecycleManager", "Managed to get click on non-listening state...");
            } else {
                onClick(this.mClickBinder);
            }
        }
        if (arraySet.contains(3)) {
            if (!this.mListening) {
                Log.w("TileLifecycleManager", "Managed to get unlock on non-listening state...");
            } else {
                onUnlockComplete();
            }
        }
        if (arraySet.contains(1)) {
            if (this.mListening) {
                Log.w("TileLifecycleManager", "Managed to get remove in listening state...");
                onStopListening();
            }
            onTileRemoved();
        }
        if (this.mUnbindImmediate) {
            this.mUnbindImmediate = false;
            setBindService(false);
        }
    }

    public final void onStartListening() {
        boolean z = true;
        this.mListening = true;
        QSTileServiceWrapper qSTileServiceWrapper = this.mWrapper;
        if (qSTileServiceWrapper != null) {
            Objects.requireNonNull(qSTileServiceWrapper);
            try {
                ((IQSTileService) qSTileServiceWrapper.mService).onStartListening();
            } catch (Exception e) {
                Log.d("IQSTileServiceWrapper", "Caught exception from TileService", e);
                z = false;
            }
            if (!z) {
                handleDeath();
            }
        }
    }

    public final void onStopListening() {
        boolean z = false;
        this.mListening = false;
        QSTileServiceWrapper qSTileServiceWrapper = this.mWrapper;
        if (qSTileServiceWrapper != null) {
            Objects.requireNonNull(qSTileServiceWrapper);
            try {
                ((IQSTileService) qSTileServiceWrapper.mService).onStopListening();
                z = true;
            } catch (Exception e) {
                Log.d("IQSTileServiceWrapper", "Caught exception from TileService", e);
            }
            if (!z) {
                handleDeath();
            }
        }
    }

    public final IBinder asBinder() {
        QSTileServiceWrapper qSTileServiceWrapper = this.mWrapper;
        if (qSTileServiceWrapper != null) {
            return ((IQSTileService) qSTileServiceWrapper.mService).asBinder();
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0051 A[RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean checkComponentState() {
        /*
            r7 = this;
            android.content.Intent r0 = r7.mIntent
            android.content.ComponentName r0 = r0.getComponent()
            java.lang.String r0 = r0.getPackageName()
            r1 = 1
            r2 = 0
            com.android.systemui.qs.external.PackageManagerAdapter r3 = r7.mPackageManagerAdapter     // Catch: NameNotFoundException -> 0x001e
            android.os.UserHandle r4 = r7.mUser     // Catch: NameNotFoundException -> 0x001e
            int r4 = r4.getIdentifier()     // Catch: NameNotFoundException -> 0x001e
            java.util.Objects.requireNonNull(r3)     // Catch: NameNotFoundException -> 0x001e
            android.content.pm.PackageManager r3 = r3.mPackageManager     // Catch: NameNotFoundException -> 0x001e
            r3.getPackageInfoAsUser(r0, r2, r4)     // Catch: NameNotFoundException -> 0x001e
            r0 = r1
            goto L_0x0026
        L_0x001e:
            java.lang.String r3 = "Package not available: "
            java.lang.String r4 = "TileLifecycleManager"
            androidx.fragment.app.DialogFragment$$ExternalSyntheticOutline0.m(r3, r0, r4)
            r0 = r2
        L_0x0026:
            if (r0 == 0) goto L_0x0052
            android.content.Intent r0 = r7.mIntent
            android.content.ComponentName r0 = r0.getComponent()
            r0.getPackageName()
            com.android.systemui.qs.external.PackageManagerAdapter r0 = r7.mPackageManagerAdapter     // Catch: RemoteException -> 0x004d
            android.content.Intent r3 = r7.mIntent     // Catch: RemoteException -> 0x004d
            android.content.ComponentName r3 = r3.getComponent()     // Catch: RemoteException -> 0x004d
            android.os.UserHandle r4 = r7.mUser     // Catch: RemoteException -> 0x004d
            int r4 = r4.getIdentifier()     // Catch: RemoteException -> 0x004d
            java.util.Objects.requireNonNull(r0)     // Catch: RemoteException -> 0x004d
            android.content.pm.IPackageManager r0 = r0.mIPackageManager     // Catch: RemoteException -> 0x004d
            long r5 = (long) r2     // Catch: RemoteException -> 0x004d
            android.content.pm.ServiceInfo r0 = r0.getServiceInfo(r3, r5, r4)     // Catch: RemoteException -> 0x004d
            if (r0 == 0) goto L_0x004d
            r0 = r1
            goto L_0x004e
        L_0x004d:
            r0 = r2
        L_0x004e:
            if (r0 != 0) goto L_0x0051
            goto L_0x0052
        L_0x0051:
            return r1
        L_0x0052:
            r7.startPackageListening()
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.external.TileLifecycleManager.checkComponentState():boolean");
    }

    public final void freeWrapper() {
        QSTileServiceWrapper qSTileServiceWrapper = this.mWrapper;
        if (qSTileServiceWrapper != null) {
            try {
                ((IQSTileService) qSTileServiceWrapper.mService).asBinder().unlinkToDeath(this, 0);
            } catch (NoSuchElementException unused) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Trying to unlink not linked recipient for component");
                m.append(this.mIntent.getComponent().flattenToShortString());
                Log.w("TileLifecycleManager", m.toString());
            }
            this.mWrapper = null;
        }
    }

    public final void handleDeath() {
        if (this.mWrapper != null) {
            freeWrapper();
            this.mIsBound = false;
            if (this.mBound && checkComponentState()) {
                this.mHandler.postDelayed(new Runnable() { // from class: com.android.systemui.qs.external.TileLifecycleManager.1
                    @Override // java.lang.Runnable
                    public final void run() {
                        TileLifecycleManager tileLifecycleManager = TileLifecycleManager.this;
                        if (tileLifecycleManager.mBound) {
                            tileLifecycleManager.setBindService(true);
                        }
                    }
                }, this.mBindRetryDelay);
            }
        }
    }

    public final void onClick(IBinder iBinder) {
        boolean z;
        QSTileServiceWrapper qSTileServiceWrapper = this.mWrapper;
        if (qSTileServiceWrapper != null) {
            Objects.requireNonNull(qSTileServiceWrapper);
            try {
                ((IQSTileService) qSTileServiceWrapper.mService).onClick(iBinder);
                z = true;
            } catch (Exception e) {
                Log.d("IQSTileServiceWrapper", "Caught exception from TileService", e);
                z = false;
            }
            if (z) {
                return;
            }
        }
        this.mClickBinder = iBinder;
        queueMessage(2);
        handleDeath();
    }

    public final void onTileAdded() {
        boolean z;
        QSTileServiceWrapper qSTileServiceWrapper = this.mWrapper;
        if (qSTileServiceWrapper != null) {
            Objects.requireNonNull(qSTileServiceWrapper);
            try {
                ((IQSTileService) qSTileServiceWrapper.mService).onTileAdded();
                z = true;
            } catch (Exception e) {
                Log.d("IQSTileServiceWrapper", "Caught exception from TileService", e);
                z = false;
            }
            if (z) {
                return;
            }
        }
        queueMessage(0);
        handleDeath();
    }

    public final void onTileRemoved() {
        boolean z;
        QSTileServiceWrapper qSTileServiceWrapper = this.mWrapper;
        if (qSTileServiceWrapper != null) {
            Objects.requireNonNull(qSTileServiceWrapper);
            try {
                ((IQSTileService) qSTileServiceWrapper.mService).onTileRemoved();
                z = true;
            } catch (Exception e) {
                Log.d("IQSTileServiceWrapper", "Caught exception from TileService", e);
                z = false;
            }
            if (z) {
                return;
            }
        }
        queueMessage(1);
        handleDeath();
    }

    public final void onUnlockComplete() {
        boolean z;
        QSTileServiceWrapper qSTileServiceWrapper = this.mWrapper;
        if (qSTileServiceWrapper != null) {
            Objects.requireNonNull(qSTileServiceWrapper);
            try {
                ((IQSTileService) qSTileServiceWrapper.mService).onUnlockComplete();
                z = true;
            } catch (Exception e) {
                Log.d("IQSTileServiceWrapper", "Caught exception from TileService", e);
                z = false;
            }
            if (z) {
                return;
            }
        }
        queueMessage(3);
        handleDeath();
    }

    public final void queueMessage(int i) {
        synchronized (this.mQueuedMessages) {
            this.mQueuedMessages.add(Integer.valueOf(i));
        }
    }

    public final void setBindService(boolean z) {
        if (!this.mBound || !this.mUnbindImmediate) {
            this.mBound = z;
            if (!z) {
                this.mBindTryCount = 0;
                freeWrapper();
                if (this.mIsBound) {
                    try {
                        this.mContext.unbindService(this);
                    } catch (Exception e) {
                        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Failed to unbind service ");
                        m.append(this.mIntent.getComponent().flattenToShortString());
                        Log.e("TileLifecycleManager", m.toString(), e);
                    }
                    this.mIsBound = false;
                }
            } else if (this.mBindTryCount == 5) {
                startPackageListening();
            } else if (checkComponentState()) {
                this.mBindTryCount++;
                try {
                    this.mIsBound = this.mContext.bindServiceAsUser(this.mIntent, this, 34603041, this.mUser);
                } catch (SecurityException e2) {
                    Log.e("TileLifecycleManager", "Failed to bind to service", e2);
                    this.mIsBound = false;
                }
            }
        } else {
            this.mUnbindImmediate = false;
        }
    }

    public final void startPackageListening() {
        IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_ADDED");
        intentFilter.addAction("android.intent.action.PACKAGE_CHANGED");
        intentFilter.addDataScheme("package");
        try {
            this.mPackageReceiverRegistered.set(true);
            this.mContext.registerReceiverAsUser(this, this.mUser, intentFilter, null, this.mHandler, 2);
        } catch (Exception e) {
            this.mPackageReceiverRegistered.set(false);
            Log.e("TileLifecycleManager", "Could not register package receiver", e);
        }
        IntentFilter intentFilter2 = new IntentFilter("android.intent.action.USER_UNLOCKED");
        try {
            this.mUserReceiverRegistered.set(true);
            this.mBroadcastDispatcher.registerReceiverWithHandler(this, intentFilter2, this.mHandler, this.mUser);
        } catch (Exception e2) {
            this.mUserReceiverRegistered.set(false);
            Log.e("TileLifecycleManager", "Could not register unlock receiver", e2);
        }
    }

    public final void stopPackageListening() {
        if (this.mUserReceiverRegistered.compareAndSet(true, false)) {
            this.mBroadcastDispatcher.unregisterReceiver(this);
        }
        if (this.mPackageReceiverRegistered.compareAndSet(true, false)) {
            this.mContext.unregisterReceiver(this);
        }
    }

    public TileLifecycleManager(Handler handler, Context context, IQSService iQSService, PackageManagerAdapter packageManagerAdapter, BroadcastDispatcher broadcastDispatcher, Intent intent, UserHandle userHandle) {
        Binder binder = new Binder();
        this.mToken = binder;
        this.mContext = context;
        this.mHandler = handler;
        this.mIntent = intent;
        intent.putExtra("service", iQSService.asBinder());
        intent.putExtra("token", binder);
        this.mUser = userHandle;
        this.mPackageManagerAdapter = packageManagerAdapter;
        this.mBroadcastDispatcher = broadcastDispatcher;
    }

    @Override // android.content.BroadcastReceiver
    public final void onReceive(Context context, Intent intent) {
        TileChangeListener tileChangeListener;
        if ("android.intent.action.USER_UNLOCKED".equals(intent.getAction()) || Objects.equals(intent.getData().getEncodedSchemeSpecificPart(), this.mIntent.getComponent().getPackageName())) {
            if ("android.intent.action.PACKAGE_CHANGED".equals(intent.getAction()) && (tileChangeListener = this.mChangeListener) != null) {
                this.mIntent.getComponent();
                CustomTile customTile = (CustomTile) tileChangeListener;
                Objects.requireNonNull(customTile);
                customTile.mHandler.post(new KeyguardDisplayManager$$ExternalSyntheticLambda1(customTile, 3));
            }
            stopPackageListening();
            if (this.mBound) {
                setBindService(true);
            }
        }
    }

    @Override // android.content.ServiceConnection
    public final void onServiceDisconnected(ComponentName componentName) {
        handleDeath();
    }

    @Override // android.os.IBinder.DeathRecipient
    public final void binderDied() {
        handleDeath();
    }
}

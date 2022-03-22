package com.google.android.systemui.communal.dock.callbacks;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.IBinder;
import android.util.Log;
import androidx.core.view.ViewCompat$$ExternalSyntheticLambda0;
import com.android.systemui.util.condition.Monitor;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class ServiceBinderCallback implements Monitor.Callback {
    public static final boolean DEBUG = Log.isLoggable("ServiceBinderCallback", 3);
    public boolean mBound;
    public final Context mContext;
    public final Executor mExecutor;
    public final Intent mIntent;
    public final AnonymousClass1 mServiceConnection = new ServiceConnection() { // from class: com.google.android.systemui.communal.dock.callbacks.ServiceBinderCallback.1
        @Override // android.content.ServiceConnection
        public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            if (ServiceBinderCallback.DEBUG) {
                Log.d("ServiceBinderCallback", "onServiceConnected:" + componentName);
            }
        }

        @Override // android.content.ServiceConnection
        public final void onServiceDisconnected(ComponentName componentName) {
            if (ServiceBinderCallback.DEBUG) {
                Log.d("ServiceBinderCallback", "onServiceDisconnected:" + componentName);
            }
        }
    };

    @Override // com.android.systemui.util.condition.Monitor.Callback
    public final void onConditionsChanged(boolean z) {
        boolean z2 = DEBUG;
        if (z2) {
            ViewCompat$$ExternalSyntheticLambda0.m("onConditionsChanged:", z, "ServiceBinderCallback");
        }
        boolean z3 = this.mBound;
        if (z3 && !z) {
            if (z2) {
                Log.d("ServiceBinderCallback", "unbinding");
            }
            this.mContext.unbindService(this.mServiceConnection);
            this.mBound = false;
        } else if (!z3 && z) {
            if (z2) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("binding with intent:");
                m.append(this.mIntent);
                Log.d("ServiceBinderCallback", m.toString());
            }
            this.mContext.bindService(this.mIntent, 1, this.mExecutor, this.mServiceConnection);
            this.mBound = true;
        }
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.google.android.systemui.communal.dock.callbacks.ServiceBinderCallback$1] */
    public ServiceBinderCallback(Context context, Executor executor, Intent intent) {
        this.mContext = context;
        this.mExecutor = executor;
        this.mIntent = intent;
    }
}

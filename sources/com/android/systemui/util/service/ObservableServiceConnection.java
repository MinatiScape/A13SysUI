package com.android.systemui.util.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import androidx.core.view.ViewCompat$$ExternalSyntheticLambda0;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline1;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda33;
import com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda8;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda21;
import com.google.android.systemui.communal.dock.callbacks.mediashell.dagger.MediaShellComponent$MediaShellModule$$ExternalSyntheticLambda0;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final class ObservableServiceConnection<T> implements ServiceConnection {
    public static final boolean DEBUG = Log.isLoggable("ObservableSvcConn", 3);
    public boolean mBoundCalled;
    public final Context mContext;
    public final Executor mExecutor;
    public T mProxy;
    public final Intent mServiceIntent;
    public final ServiceTransformer<T> mTransformer;
    public final ArrayList<WeakReference<Callback<T>>> mCallbacks = new ArrayList<>();
    public Optional<Integer> mLastDisconnectReason = Optional.empty();

    /* loaded from: classes.dex */
    public interface Callback<T> {
        void onConnected(Object obj);

        void onDisconnected();
    }

    /* loaded from: classes.dex */
    public interface ServiceTransformer<T> {
        IBinder convert(IBinder iBinder);
    }

    @Override // android.content.ServiceConnection
    public final void onBindingDied(ComponentName componentName) {
        onDisconnected(2);
    }

    @Override // android.content.ServiceConnection
    public final void onNullBinding(ComponentName componentName) {
        onDisconnected(1);
    }

    @Override // android.content.ServiceConnection
    public final void onServiceDisconnected(ComponentName componentName) {
        onDisconnected(2);
    }

    public ObservableServiceConnection(Context context, Intent intent, Executor executor) {
        MediaShellComponent$MediaShellModule$$ExternalSyntheticLambda0 mediaShellComponent$MediaShellModule$$ExternalSyntheticLambda0 = MediaShellComponent$MediaShellModule$$ExternalSyntheticLambda0.INSTANCE;
        this.mContext = context;
        this.mServiceIntent = intent;
        this.mExecutor = executor;
        this.mTransformer = mediaShellComponent$MediaShellModule$$ExternalSyntheticLambda0;
    }

    public final void addCallback(Callback<T> callback) {
        if (DEBUG) {
            Log.d("ObservableSvcConn", "addCallback:" + callback);
        }
        this.mExecutor.execute(new BubbleStackView$$ExternalSyntheticLambda21(this, callback, 3));
    }

    public final void applyToCallbacksLocked(Consumer<Callback<T>> consumer) {
        Iterator<WeakReference<Callback<T>>> it = this.mCallbacks.iterator();
        while (it.hasNext()) {
            Callback<T> callback = it.next().get();
            if (callback != null) {
                consumer.accept(callback);
            } else {
                it.remove();
            }
        }
    }

    public final boolean bind() {
        boolean bindService = this.mContext.bindService(this.mServiceIntent, 1, this.mExecutor, this);
        this.mBoundCalled = true;
        if (DEBUG) {
            ViewCompat$$ExternalSyntheticLambda0.m("bind. bound:", bindService, "ObservableSvcConn");
        }
        return bindService;
    }

    public final void onDisconnected(int i) {
        if (DEBUG) {
            ExifInterface$$ExternalSyntheticOutline1.m("onDisconnected:", i, "ObservableSvcConn");
        }
        if (this.mBoundCalled) {
            this.mLastDisconnectReason = Optional.of(Integer.valueOf(i));
            unbind();
            this.mProxy = null;
            applyToCallbacksLocked(new StatusBar$$ExternalSyntheticLambda33(this, 3));
        }
    }

    @Override // android.content.ServiceConnection
    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        if (DEBUG) {
            Log.d("ObservableSvcConn", "onServiceConnected");
        }
        this.mProxy = (T) this.mTransformer.convert(iBinder);
        applyToCallbacksLocked(new BubbleController$$ExternalSyntheticLambda8(this, 3));
    }

    public final void unbind() {
        if (this.mBoundCalled) {
            this.mBoundCalled = false;
            this.mContext.unbindService(this);
            onDisconnected(4);
        }
    }
}

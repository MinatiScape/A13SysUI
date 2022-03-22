package com.android.systemui;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.os.StrictMode;
import android.os.SystemProperties;
import android.view.Choreographer;
import androidx.recyclerview.R$dimen;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.util.Assert;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public final class DejankUtils {
    public static final boolean STRICT_MODE_ENABLED;
    public static final DejankUtils$$ExternalSyntheticLambda0 sAnimationCallbackRunnable;
    public static Stack<String> sBlockingIpcs;
    public static final Choreographer sChoreographer;
    public static final Handler sHandler;
    public static boolean sImmediate;
    public static final Object sLock;
    public static final ArrayList<Runnable> sPendingRunnables;
    public static final AnonymousClass1 sProxy;
    public static boolean sTemporarilyIgnoreStrictMode;
    public static final HashSet<String> sWhitelistedFrameworkClasses;

    public static void whitelistIpcs(Runnable runnable) {
        if (!STRICT_MODE_ENABLED || sTemporarilyIgnoreStrictMode) {
            runnable.run();
            return;
        }
        Object obj = sLock;
        synchronized (obj) {
            sTemporarilyIgnoreStrictMode = true;
        }
        try {
            runnable.run();
            synchronized (obj) {
                sTemporarilyIgnoreStrictMode = false;
            }
        } catch (Throwable th) {
            synchronized (sLock) {
                sTemporarilyIgnoreStrictMode = false;
                throw th;
            }
        }
    }

    static {
        boolean z = false;
        if (Build.IS_ENG || SystemProperties.getBoolean("persist.sysui.strictmode", false)) {
            z = true;
        }
        STRICT_MODE_ENABLED = z;
        sChoreographer = Choreographer.getInstance();
        sHandler = new Handler();
        sPendingRunnables = new ArrayList<>();
        sBlockingIpcs = new Stack<>();
        HashSet<String> hashSet = new HashSet<>();
        sWhitelistedFrameworkClasses = hashSet;
        sLock = new Object();
        Binder.ProxyTransactListener proxyTransactListener = new Binder.ProxyTransactListener() { // from class: com.android.systemui.DejankUtils.1
            public final void onTransactEnded(Object obj) {
            }

            public final Object onTransactStarted(IBinder iBinder, int i) {
                return null;
            }

            public final Object onTransactStarted(IBinder iBinder, int i, int i2) {
                String interfaceDescriptor;
                Object obj = DejankUtils.sLock;
                synchronized (obj) {
                    boolean z2 = true;
                    if ((i2 & 1) != 1) {
                        if (!DejankUtils.sBlockingIpcs.empty()) {
                            if (R$dimen.sMainThread == null) {
                                R$dimen.sMainThread = Looper.getMainLooper().getThread();
                            }
                            if (Thread.currentThread() != R$dimen.sMainThread) {
                                z2 = false;
                            }
                            if (z2 && !DejankUtils.sTemporarilyIgnoreStrictMode) {
                                try {
                                    interfaceDescriptor = iBinder.getInterfaceDescriptor();
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                                synchronized (obj) {
                                    if (DejankUtils.sWhitelistedFrameworkClasses.contains(interfaceDescriptor)) {
                                        return null;
                                    }
                                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("IPC detected on critical path: ");
                                    m.append(DejankUtils.sBlockingIpcs.peek());
                                    StrictMode.noteSlowCall(m.toString());
                                    return null;
                                }
                            }
                        }
                    }
                    return null;
                }
            }
        };
        sProxy = proxyTransactListener;
        if (z) {
            hashSet.add("android.view.IWindowSession");
            hashSet.add("com.android.internal.policy.IKeyguardStateCallback");
            hashSet.add("android.os.IPowerManager");
            hashSet.add("com.android.internal.statusbar.IStatusBarService");
            Binder.setProxyTransactListener(proxyTransactListener);
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectCustomSlowCalls().penaltyFlashScreen().penaltyLog().build());
        }
        sAnimationCallbackRunnable = DejankUtils$$ExternalSyntheticLambda0.INSTANCE;
    }

    public static void postAfterTraversal(Runnable runnable) {
        if (sImmediate) {
            runnable.run();
            return;
        }
        Assert.isMainThread();
        sPendingRunnables.add(runnable);
        sChoreographer.postCallback(1, sAnimationCallbackRunnable, null);
    }

    public static void startDetectingBlockingIpcs(String str) {
        if (STRICT_MODE_ENABLED) {
            synchronized (sLock) {
                sBlockingIpcs.push(str);
            }
        }
    }

    public static void stopDetectingBlockingIpcs(String str) {
        if (STRICT_MODE_ENABLED) {
            synchronized (sLock) {
                sBlockingIpcs.remove(str);
            }
        }
    }

    public static <T> T whitelistIpcs(Supplier<T> supplier) {
        if (!STRICT_MODE_ENABLED || sTemporarilyIgnoreStrictMode) {
            return supplier.get();
        }
        Object obj = sLock;
        synchronized (obj) {
            sTemporarilyIgnoreStrictMode = true;
        }
        try {
            T t = supplier.get();
            synchronized (obj) {
                sTemporarilyIgnoreStrictMode = false;
            }
            return t;
        } catch (Throwable th) {
            synchronized (sLock) {
                sTemporarilyIgnoreStrictMode = false;
                throw th;
            }
        }
    }

    @VisibleForTesting
    public static void setImmediate(boolean z) {
        sImmediate = z;
    }
}

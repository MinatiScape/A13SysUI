package com.android.systemui.util;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Looper;
/* loaded from: classes.dex */
public final class Assert {
    public static final Looper sMainLooper = Looper.getMainLooper();
    public static Thread sTestThread = null;

    public static void isMainThread() {
        Looper looper = sMainLooper;
        if (!looper.isCurrentThread()) {
            Thread thread = sTestThread;
            if (thread == null || thread != Thread.currentThread()) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("should be called from the main thread. sMainLooper.threadName=");
                m.append(looper.getThread().getName());
                m.append(" Thread.currentThread()=");
                m.append(Thread.currentThread().getName());
                throw new IllegalStateException(m.toString());
            }
        }
    }

    public static void isNotMainThread() {
        if (sMainLooper.isCurrentThread()) {
            Thread thread = sTestThread;
            if (thread == null || thread == Thread.currentThread()) {
                throw new IllegalStateException("should not be called from the main thread.");
            }
        }
    }

    public static void setTestableLooper(Looper looper) {
        Thread thread;
        if (looper == null) {
            thread = null;
        } else {
            thread = looper.getThread();
        }
        setTestThread(thread);
    }

    public static void setTestThread(Thread thread) {
        sTestThread = thread;
    }
}

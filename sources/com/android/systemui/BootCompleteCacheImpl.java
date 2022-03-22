package com.android.systemui;

import com.android.internal.annotations.GuardedBy;
import com.android.systemui.BootCompleteCache;
import com.android.systemui.dump.DumpManager;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: BootCompleteCacheImpl.kt */
/* loaded from: classes.dex */
public final class BootCompleteCacheImpl implements BootCompleteCache, Dumpable {
    @GuardedBy({"listeners"})
    public final ArrayList listeners = new ArrayList();
    public final AtomicBoolean bootComplete = new AtomicBoolean(false);

    @Override // com.android.systemui.BootCompleteCache
    public final boolean addListener(BootCompleteCache.BootCompleteListener bootCompleteListener) {
        if (this.bootComplete.get()) {
            return true;
        }
        synchronized (this.listeners) {
            if (this.bootComplete.get()) {
                return true;
            }
            this.listeners.add(new WeakReference(bootCompleteListener));
            return false;
        }
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("BootCompleteCache state:");
        printWriter.println(Intrinsics.stringPlus("  boot complete: ", Boolean.valueOf(isBootComplete())));
        if (!isBootComplete()) {
            printWriter.println("  listeners:");
            synchronized (this.listeners) {
                Iterator it = this.listeners.iterator();
                while (it.hasNext()) {
                    printWriter.println(Intrinsics.stringPlus("    ", (WeakReference) it.next()));
                }
            }
        }
    }

    @Override // com.android.systemui.BootCompleteCache
    public final boolean isBootComplete() {
        return this.bootComplete.get();
    }

    public final void setBootComplete() {
        if (this.bootComplete.compareAndSet(false, true)) {
            synchronized (this.listeners) {
                Iterator it = this.listeners.iterator();
                while (it.hasNext()) {
                    BootCompleteCache.BootCompleteListener bootCompleteListener = (BootCompleteCache.BootCompleteListener) ((WeakReference) it.next()).get();
                    if (bootCompleteListener != null) {
                        bootCompleteListener.onBootComplete();
                    }
                }
                this.listeners.clear();
            }
        }
    }

    public BootCompleteCacheImpl(DumpManager dumpManager) {
        dumpManager.registerDumpable("BootCompleteCacheImpl", this);
    }
}

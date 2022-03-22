package com.android.systemui.statusbar;

import android.app.ActivityManager;
import android.content.res.Resources;
import android.os.SystemProperties;
import android.util.IndentingPrintWriter;
import android.util.MathUtils;
import android.view.CrossWindowBlurListeners;
import android.view.SurfaceControl;
import android.view.ViewRootImpl;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: BlurUtils.kt */
/* loaded from: classes.dex */
public final class BlurUtils implements Dumpable {
    public final CrossWindowBlurListeners crossWindowBlurListeners;
    public int lastAppliedBlur;
    public final int maxBlurRadius;
    public final int minBlurRadius;

    public final float blurRadiusOfRatio(float f) {
        boolean z;
        if (f == 0.0f) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            return 0.0f;
        }
        return MathUtils.lerp(this.minBlurRadius, this.maxBlurRadius, f);
    }

    public final void applyBlur(ViewRootImpl viewRootImpl, int i, boolean z) {
        if (viewRootImpl != null && viewRootImpl.getSurfaceControl().isValid()) {
            SurfaceControl.Transaction createTransaction = createTransaction();
            th = null;
            try {
                if (supportsBlursOnWindows()) {
                    createTransaction.setBackgroundBlurRadius(viewRootImpl.getSurfaceControl(), i);
                    if (this.lastAppliedBlur == 0 && i != 0) {
                        createTransaction.setEarlyWakeupStart();
                    }
                    if (this.lastAppliedBlur != 0 && i == 0) {
                        createTransaction.setEarlyWakeupEnd();
                    }
                    this.lastAppliedBlur = i;
                }
                createTransaction.setOpaque(viewRootImpl.getSurfaceControl(), z);
                createTransaction.apply();
            } catch (Throwable th) {
                try {
                    throw th;
                } finally {
                    CloseableKt.closeFinally(createTransaction, th);
                }
            }
        }
    }

    public SurfaceControl.Transaction createTransaction() {
        return new SurfaceControl.Transaction();
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.println("BlurUtils:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println(Intrinsics.stringPlus("minBlurRadius: ", Integer.valueOf(this.minBlurRadius)));
        indentingPrintWriter.println(Intrinsics.stringPlus("maxBlurRadius: ", Integer.valueOf(this.maxBlurRadius)));
        indentingPrintWriter.println(Intrinsics.stringPlus("supportsBlursOnWindows: ", Boolean.valueOf(supportsBlursOnWindows())));
        indentingPrintWriter.println(Intrinsics.stringPlus("CROSS_WINDOW_BLUR_SUPPORTED: ", Boolean.valueOf(CrossWindowBlurListeners.CROSS_WINDOW_BLUR_SUPPORTED)));
        indentingPrintWriter.println(Intrinsics.stringPlus("isHighEndGfx: ", Boolean.valueOf(ActivityManager.isHighEndGfx())));
    }

    public final boolean supportsBlursOnWindows() {
        if (!CrossWindowBlurListeners.CROSS_WINDOW_BLUR_SUPPORTED || !ActivityManager.isHighEndGfx() || !this.crossWindowBlurListeners.isCrossWindowBlurEnabled() || SystemProperties.getBoolean("persist.sysui.disableBlur", false)) {
            return false;
        }
        return true;
    }

    public BlurUtils(Resources resources, CrossWindowBlurListeners crossWindowBlurListeners, DumpManager dumpManager) {
        this.crossWindowBlurListeners = crossWindowBlurListeners;
        this.minBlurRadius = resources.getDimensionPixelSize(2131166369);
        this.maxBlurRadius = resources.getDimensionPixelSize(2131166339);
        dumpManager.registerDumpable(BlurUtils.class.getName(), this);
    }
}

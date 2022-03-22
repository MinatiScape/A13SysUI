package com.android.systemui;

import android.content.Context;
import android.content.res.Configuration;
import com.android.internal.annotations.VisibleForTesting;
import java.io.FileDescriptor;
import java.io.PrintWriter;
/* loaded from: classes.dex */
public abstract class CoreStartable implements Dumpable {
    public final Context mContext;

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
    }

    @VisibleForTesting
    public void onBootCompleted() {
    }

    public void onConfigurationChanged(Configuration configuration) {
    }

    public abstract void start();

    public CoreStartable(Context context) {
        this.mContext = context;
    }
}

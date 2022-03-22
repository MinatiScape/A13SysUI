package com.google.android.systemui.communal.dock;

import android.content.Context;
import android.util.Log;
import com.android.systemui.CoreStartable;
import com.android.systemui.util.condition.Monitor;
import dagger.Lazy;
/* loaded from: classes.dex */
public class DockMonitor extends CoreStartable {
    public final Lazy<Monitor> mMonitorFetcher;

    @Override // com.android.systemui.CoreStartable
    public final void start() {
        Log.d("DockMonitor", "started");
        this.mMonitorFetcher.get();
    }

    public DockMonitor(Context context, Lazy<Monitor> lazy) {
        super(context);
        this.mMonitorFetcher = lazy;
    }
}

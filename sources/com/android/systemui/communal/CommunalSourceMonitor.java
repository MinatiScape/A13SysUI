package com.android.systemui.communal;

import android.util.Log;
import androidx.core.view.ViewCompat$$ExternalSyntheticLambda0;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.util.condition.Monitor;
import com.android.wm.shell.TaskView$$ExternalSyntheticLambda4;
import com.google.android.collect.Lists;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class CommunalSourceMonitor {
    public static final boolean DEBUG = Log.isLoggable("CommunalSourceMonitor", 3);
    public final Monitor mConditionsMonitor;
    public CommunalSource mCurrentSource;
    public final Executor mExecutor;
    public final ArrayList<WeakReference<Callback>> mCallbacks = Lists.newArrayList();
    public boolean mAllCommunalConditionsMet = false;
    public boolean mListeningForConditions = false;
    public final CommunalSourceMonitor$$ExternalSyntheticLambda0 mConditionsCallback = new Monitor.Callback() { // from class: com.android.systemui.communal.CommunalSourceMonitor$$ExternalSyntheticLambda0
        @Override // com.android.systemui.util.condition.Monitor.Callback
        public final void onConditionsChanged(boolean z) {
            CommunalSourceMonitor communalSourceMonitor = CommunalSourceMonitor.this;
            Objects.requireNonNull(communalSourceMonitor);
            if (communalSourceMonitor.mAllCommunalConditionsMet != z) {
                if (CommunalSourceMonitor.DEBUG) {
                    ViewCompat$$ExternalSyntheticLambda0.m("communal conditions changed: ", z, "CommunalSourceMonitor");
                }
                communalSourceMonitor.mAllCommunalConditionsMet = z;
                communalSourceMonitor.mExecutor.execute(new TaskView$$ExternalSyntheticLambda4(communalSourceMonitor, 5));
            }
        }
    };

    /* loaded from: classes.dex */
    public interface Callback {
        void onSourceAvailable(WeakReference<CommunalSource> weakReference);
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.systemui.communal.CommunalSourceMonitor$$ExternalSyntheticLambda0] */
    @VisibleForTesting
    public CommunalSourceMonitor(Executor executor, Monitor monitor) {
        this.mExecutor = executor;
        this.mConditionsMonitor = monitor;
    }
}

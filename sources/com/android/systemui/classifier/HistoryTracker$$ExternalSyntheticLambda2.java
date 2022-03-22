package com.android.systemui.classifier;

import android.view.View;
import com.android.systemui.classifier.HistoryTracker;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.google.android.systemui.lowlightclock.LowLightClockControllerImpl;
import com.google.android.systemui.smartspace.BcSmartspaceDataProvider;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class HistoryTracker$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ HistoryTracker$$ExternalSyntheticLambda2(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                HistoryTracker historyTracker = (HistoryTracker) this.f$0;
                Objects.requireNonNull(historyTracker);
                ((HistoryTracker.BeliefListener) obj).onBeliefChanged(historyTracker.falseBelief());
                return;
            case 1:
                Map.Entry entry = (Map.Entry) obj;
                Objects.requireNonNull((LowLightClockControllerImpl) this.f$0);
                View view = (View) entry.getKey();
                entry.setValue(Float.valueOf(view.getAlpha()));
                view.setAlpha(0.0f);
                return;
            default:
                BcSmartspaceDataProvider bcSmartspaceDataProvider = (BcSmartspaceDataProvider) this.f$0;
                Objects.requireNonNull(bcSmartspaceDataProvider);
                ((BcSmartspaceDataPlugin.SmartspaceTargetListener) obj).onSmartspaceTargetsUpdated(bcSmartspaceDataProvider.mSmartspaceTargets);
                return;
        }
    }
}

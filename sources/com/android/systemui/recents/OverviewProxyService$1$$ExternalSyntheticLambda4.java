package com.android.systemui.recents;

import com.android.systemui.recents.OverviewProxyService;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class OverviewProxyService$1$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ OverviewProxyService$1$$ExternalSyntheticLambda4(Object obj, boolean z, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                final OverviewProxyService.AnonymousClass1 r0 = (OverviewProxyService.AnonymousClass1) this.f$0;
                final boolean z = this.f$1;
                Objects.requireNonNull(r0);
                OverviewProxyService.this.mHandler.post(new Runnable() { // from class: com.android.systemui.recents.OverviewProxyService$1$$ExternalSyntheticLambda9
                    @Override // java.lang.Runnable
                    public final void run() {
                        OverviewProxyService.AnonymousClass1 r02 = OverviewProxyService.AnonymousClass1.this;
                        boolean z2 = z;
                        Objects.requireNonNull(r02);
                        OverviewProxyService overviewProxyService = OverviewProxyService.this;
                        Objects.requireNonNull(overviewProxyService);
                        int size = overviewProxyService.mConnectionCallbacks.size();
                        while (true) {
                            size--;
                            if (size >= 0) {
                                ((OverviewProxyService.OverviewProxyListener) overviewProxyService.mConnectionCallbacks.get(size)).onHomeRotationEnabled(z2);
                            } else {
                                return;
                            }
                        }
                    }
                });
                return;
            default:
                LegacySplitScreenController legacySplitScreenController = (LegacySplitScreenController) this.f$0;
                boolean z2 = this.f$1;
                Objects.requireNonNull(legacySplitScreenController);
                if (legacySplitScreenController.mVisible) {
                    legacySplitScreenController.setHomeMinimized(z2);
                    return;
                }
                return;
        }
    }
}

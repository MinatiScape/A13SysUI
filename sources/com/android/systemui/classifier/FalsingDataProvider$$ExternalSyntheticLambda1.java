package com.android.systemui.classifier;

import com.android.systemui.classifier.FalsingDataProvider;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class FalsingDataProvider$$ExternalSyntheticLambda1 implements Consumer {
    public static final /* synthetic */ FalsingDataProvider$$ExternalSyntheticLambda1 INSTANCE = new FalsingDataProvider$$ExternalSyntheticLambda1(0);
    public static final /* synthetic */ FalsingDataProvider$$ExternalSyntheticLambda1 INSTANCE$1 = new FalsingDataProvider$$ExternalSyntheticLambda1(1);
    public final /* synthetic */ int $r8$classId;

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                ((FalsingDataProvider.SessionListener) obj).onSessionEnded();
                return;
            default:
                ((KeyguardStateController.Callback) obj).onLaunchTransitionFadingAwayChanged();
                return;
        }
    }

    public /* synthetic */ FalsingDataProvider$$ExternalSyntheticLambda1(int i) {
        this.$r8$classId = i;
    }
}

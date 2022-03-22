package com.android.systemui.tuner;

import com.android.systemui.fragments.FragmentHostManager;
import com.android.systemui.fragments.FragmentService;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class TunerActivity$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public static final /* synthetic */ TunerActivity$$ExternalSyntheticLambda0 INSTANCE$1 = new TunerActivity$$ExternalSyntheticLambda0(1);
    public static final /* synthetic */ TunerActivity$$ExternalSyntheticLambda0 INSTANCE = new TunerActivity$$ExternalSyntheticLambda0(0);

    public /* synthetic */ TunerActivity$$ExternalSyntheticLambda0(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                FragmentService fragmentService = (FragmentService) obj;
                int i = TunerActivity.$r8$clinit;
                Objects.requireNonNull(fragmentService);
                for (FragmentService.FragmentHostState fragmentHostState : fragmentService.mHosts.values()) {
                    FragmentHostManager fragmentHostManager = fragmentHostState.mFragmentHostManager;
                    Objects.requireNonNull(fragmentHostManager);
                    fragmentHostManager.mFragments.dispatchDestroy();
                }
                return;
            default:
                ((KeyguardStateController.Callback) obj).onKeyguardDismissAmountChanged();
                return;
        }
    }
}

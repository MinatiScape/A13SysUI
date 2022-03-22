package com.android.systemui.dagger;

import android.os.RemoteException;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class SysUIComponent$$ExternalSyntheticLambda2 implements Consumer {
    public static final /* synthetic */ SysUIComponent$$ExternalSyntheticLambda2 INSTANCE = new SysUIComponent$$ExternalSyntheticLambda2(0);
    public static final /* synthetic */ SysUIComponent$$ExternalSyntheticLambda2 INSTANCE$1 = new SysUIComponent$$ExternalSyntheticLambda2(1);
    public final /* synthetic */ int $r8$classId;

    public /* synthetic */ SysUIComponent$$ExternalSyntheticLambda2(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        boolean z;
        switch (this.$r8$classId) {
            case 0:
                NaturalRotationUnfoldProgressProvider naturalRotationUnfoldProgressProvider = (NaturalRotationUnfoldProgressProvider) obj;
                Objects.requireNonNull(naturalRotationUnfoldProgressProvider);
                try {
                    naturalRotationUnfoldProgressProvider.windowManagerInterface.watchRotation(naturalRotationUnfoldProgressProvider.rotationWatcher, naturalRotationUnfoldProgressProvider.context.getDisplay().getDisplayId());
                    int rotation = naturalRotationUnfoldProgressProvider.context.getDisplay().getRotation();
                    if (rotation == 0 || rotation == 2) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (naturalRotationUnfoldProgressProvider.isNaturalRotation != z) {
                        naturalRotationUnfoldProgressProvider.isNaturalRotation = z;
                        naturalRotationUnfoldProgressProvider.scopedUnfoldTransitionProgressProvider.setReadyToHandleTransition(z);
                        return;
                    }
                    return;
                } catch (RemoteException e) {
                    throw e.rethrowFromSystemServer();
                }
            default:
                ((ScreenLifecycle.Observer) obj).onScreenTurningOff();
                return;
        }
    }
}

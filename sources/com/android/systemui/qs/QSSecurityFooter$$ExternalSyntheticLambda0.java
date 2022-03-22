package com.android.systemui.qs;

import com.android.wm.shell.transition.DefaultTransitionHandler;
import java.util.Objects;
import java.util.concurrent.Callable;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class QSSecurityFooter$$ExternalSyntheticLambda0 implements Callable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ QSSecurityFooter$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.concurrent.Callable
    public final Object call() {
        switch (this.$r8$classId) {
            case 0:
                QSSecurityFooter qSSecurityFooter = (QSSecurityFooter) this.f$0;
                Objects.requireNonNull(qSSecurityFooter);
                return qSSecurityFooter.mContext.getString(2131952785);
            default:
                DefaultTransitionHandler defaultTransitionHandler = (DefaultTransitionHandler) this.f$0;
                boolean z = DefaultTransitionHandler.sDisableCustomTaskAnimationProperty;
                Objects.requireNonNull(defaultTransitionHandler);
                return defaultTransitionHandler.mContext.getDrawable(17302392);
        }
    }
}

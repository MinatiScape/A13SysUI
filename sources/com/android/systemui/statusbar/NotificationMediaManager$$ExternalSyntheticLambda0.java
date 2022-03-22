package com.android.systemui.statusbar;

import android.app.smartspace.SmartspaceTarget;
import com.android.systemui.statusbar.phone.StatusBar;
import java.util.Objects;
import java.util.function.Function;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class NotificationMediaManager$$ExternalSyntheticLambda0 implements Function {
    public static final /* synthetic */ NotificationMediaManager$$ExternalSyntheticLambda0 INSTANCE = new NotificationMediaManager$$ExternalSyntheticLambda0(0);
    public static final /* synthetic */ NotificationMediaManager$$ExternalSyntheticLambda0 INSTANCE$1 = new NotificationMediaManager$$ExternalSyntheticLambda0(1);
    public final /* synthetic */ int $r8$classId;

    public /* synthetic */ NotificationMediaManager$$ExternalSyntheticLambda0(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                StatusBar statusBar = (StatusBar) obj;
                Objects.requireNonNull(statusBar);
                return Boolean.valueOf(statusBar.mIsOccluded);
            default:
                return ((SmartspaceTarget) obj).getSmartspaceTargetId();
        }
    }
}

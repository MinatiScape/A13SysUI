package com.android.systemui;

import com.android.systemui.statusbar.StatusBarIconView;
import java.util.Objects;
import java.util.function.Function;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class SystemUIApplication$$ExternalSyntheticLambda3 implements Function {
    public static final /* synthetic */ SystemUIApplication$$ExternalSyntheticLambda3 INSTANCE = new SystemUIApplication$$ExternalSyntheticLambda3(0);
    public static final /* synthetic */ SystemUIApplication$$ExternalSyntheticLambda3 INSTANCE$1 = new SystemUIApplication$$ExternalSyntheticLambda3(1);
    public final /* synthetic */ int $r8$classId;

    public /* synthetic */ SystemUIApplication$$ExternalSyntheticLambda3(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                return ((Class) obj).getName();
            default:
                StatusBarIconView statusBarIconView = (StatusBarIconView) obj;
                Objects.requireNonNull(statusBarIconView);
                return statusBarIconView.mIcon.icon;
        }
    }
}

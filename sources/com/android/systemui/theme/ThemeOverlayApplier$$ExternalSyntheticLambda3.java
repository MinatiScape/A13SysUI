package com.android.systemui.theme;

import android.app.smartspace.SmartspaceAction;
import android.content.om.OverlayInfo;
import android.util.Pair;
import com.google.android.systemui.smartspace.BcSmartspaceCardDoorbell;
import java.util.function.Function;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ThemeOverlayApplier$$ExternalSyntheticLambda3 implements Function {
    public static final /* synthetic */ ThemeOverlayApplier$$ExternalSyntheticLambda3 INSTANCE = new ThemeOverlayApplier$$ExternalSyntheticLambda3(0);
    public static final /* synthetic */ ThemeOverlayApplier$$ExternalSyntheticLambda3 INSTANCE$1 = new ThemeOverlayApplier$$ExternalSyntheticLambda3(1);
    public final /* synthetic */ int $r8$classId;

    public /* synthetic */ ThemeOverlayApplier$$ExternalSyntheticLambda3(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                OverlayInfo overlayInfo = (OverlayInfo) obj;
                boolean z = ThemeOverlayApplier.DEBUG;
                return new Pair(overlayInfo.category, overlayInfo.packageName);
            default:
                int i = BcSmartspaceCardDoorbell.$r8$clinit;
                return ((SmartspaceAction) obj).getExtras().getString("imageUri");
        }
    }
}

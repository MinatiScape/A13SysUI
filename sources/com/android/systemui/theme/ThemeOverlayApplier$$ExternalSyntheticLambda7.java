package com.android.systemui.theme;

import android.app.people.ConversationStatus;
import android.app.smartspace.SmartspaceAction;
import android.content.om.OverlayInfo;
import com.android.systemui.people.PeopleTileViewHelper;
import com.google.android.systemui.smartspace.BcSmartspaceCardDoorbell;
import java.util.function.Predicate;
import java.util.regex.Pattern;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ThemeOverlayApplier$$ExternalSyntheticLambda7 implements Predicate {
    public final /* synthetic */ int $r8$classId;
    public static final /* synthetic */ ThemeOverlayApplier$$ExternalSyntheticLambda7 INSTANCE$1 = new ThemeOverlayApplier$$ExternalSyntheticLambda7(1);
    public static final /* synthetic */ ThemeOverlayApplier$$ExternalSyntheticLambda7 INSTANCE = new ThemeOverlayApplier$$ExternalSyntheticLambda7(0);
    public static final /* synthetic */ ThemeOverlayApplier$$ExternalSyntheticLambda7 INSTANCE$2 = new ThemeOverlayApplier$$ExternalSyntheticLambda7(2);

    public /* synthetic */ ThemeOverlayApplier$$ExternalSyntheticLambda7(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                boolean z = ThemeOverlayApplier.DEBUG;
                return ((OverlayInfo) obj).isEnabled();
            case 1:
                Pattern pattern = PeopleTileViewHelper.DOUBLE_EXCLAMATION_PATTERN;
                if (((ConversationStatus) obj).getActivity() == 3) {
                    return true;
                }
                return false;
            default:
                int i = BcSmartspaceCardDoorbell.$r8$clinit;
                return ((SmartspaceAction) obj).getExtras().containsKey("imageUri");
        }
    }
}

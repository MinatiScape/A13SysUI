package com.android.systemui.people;

import android.app.people.ConversationStatus;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import java.util.function.Predicate;
import java.util.regex.Pattern;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class PeopleTileViewHelper$$ExternalSyntheticLambda4 implements Predicate {
    public static final /* synthetic */ PeopleTileViewHelper$$ExternalSyntheticLambda4 INSTANCE = new PeopleTileViewHelper$$ExternalSyntheticLambda4(0);
    public static final /* synthetic */ PeopleTileViewHelper$$ExternalSyntheticLambda4 INSTANCE$1 = new PeopleTileViewHelper$$ExternalSyntheticLambda4(1);
    public final /* synthetic */ int $r8$classId;

    public /* synthetic */ PeopleTileViewHelper$$ExternalSyntheticLambda4(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                Pattern pattern = PeopleTileViewHelper.DOUBLE_EXCLAMATION_PATTERN;
                if (((ConversationStatus) obj).getActivity() == 1) {
                    return true;
                }
                return false;
            default:
                return ((UserSwitcherController.UserRecord) obj).isCurrent;
        }
    }
}

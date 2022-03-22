package com.android.systemui.privacy.television;

import com.android.systemui.privacy.PrivacyItem;
import com.android.systemui.privacy.PrivacyType;
import com.google.android.systemui.smartspace.BcSmartspaceCardDoorbell;
import java.util.Objects;
import java.util.function.Predicate;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class TvOngoingPrivacyChip$$ExternalSyntheticLambda0 implements Predicate {
    public static final /* synthetic */ TvOngoingPrivacyChip$$ExternalSyntheticLambda0 INSTANCE = new TvOngoingPrivacyChip$$ExternalSyntheticLambda0(0);
    public static final /* synthetic */ TvOngoingPrivacyChip$$ExternalSyntheticLambda0 INSTANCE$1 = new TvOngoingPrivacyChip$$ExternalSyntheticLambda0(1);
    public final /* synthetic */ int $r8$classId;

    public /* synthetic */ TvOngoingPrivacyChip$$ExternalSyntheticLambda0(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                PrivacyItem privacyItem = (PrivacyItem) obj;
                Objects.requireNonNull(privacyItem);
                if (privacyItem.privacyType == PrivacyType.TYPE_LOCATION) {
                    return true;
                }
                return false;
            default:
                return Objects.nonNull((BcSmartspaceCardDoorbell.DrawableWithUri) obj);
        }
    }
}

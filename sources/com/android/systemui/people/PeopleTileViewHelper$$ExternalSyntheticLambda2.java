package com.android.systemui.people;

import android.app.people.ConversationStatus;
import android.os.Parcelable;
import java.util.function.Function;
import java.util.regex.Pattern;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class PeopleTileViewHelper$$ExternalSyntheticLambda2 implements Function {
    public static final /* synthetic */ PeopleTileViewHelper$$ExternalSyntheticLambda2 INSTANCE = new PeopleTileViewHelper$$ExternalSyntheticLambda2(0);
    public static final /* synthetic */ PeopleTileViewHelper$$ExternalSyntheticLambda2 INSTANCE$1 = new PeopleTileViewHelper$$ExternalSyntheticLambda2(1);
    public final /* synthetic */ int $r8$classId;

    public /* synthetic */ PeopleTileViewHelper$$ExternalSyntheticLambda2(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                Pattern pattern = PeopleTileViewHelper.DOUBLE_EXCLAMATION_PATTERN;
                return Long.valueOf(((ConversationStatus) obj).getStartTimeMillis());
            default:
                return (Parcelable) obj;
        }
    }
}

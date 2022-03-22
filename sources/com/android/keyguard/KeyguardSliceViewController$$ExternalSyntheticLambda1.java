package com.android.keyguard;

import androidx.slice.widget.SliceContent;
import com.android.wm.shell.compatui.CompatUIWindowManagerAbstract;
import java.util.Objects;
import java.util.function.Predicate;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class KeyguardSliceViewController$$ExternalSyntheticLambda1 implements Predicate {
    public static final /* synthetic */ KeyguardSliceViewController$$ExternalSyntheticLambda1 INSTANCE = new KeyguardSliceViewController$$ExternalSyntheticLambda1(0);
    public static final /* synthetic */ KeyguardSliceViewController$$ExternalSyntheticLambda1 INSTANCE$1 = new KeyguardSliceViewController$$ExternalSyntheticLambda1(1);
    public final /* synthetic */ int $r8$classId;

    public /* synthetic */ KeyguardSliceViewController$$ExternalSyntheticLambda1(int i) {
        this.$r8$classId = i;
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                SliceContent sliceContent = (SliceContent) obj;
                Objects.requireNonNull(sliceContent);
                return !"content://com.android.systemui.keyguard/action".equals(sliceContent.mSliceItem.getSlice().getUri().toString());
            default:
                CompatUIWindowManagerAbstract compatUIWindowManagerAbstract = (CompatUIWindowManagerAbstract) obj;
                return true;
        }
    }
}

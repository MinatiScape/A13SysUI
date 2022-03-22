package com.android.systemui.util.leak;

import com.android.systemui.util.leak.TrackedObjects;
import java.util.Collection;
import java.util.function.Predicate;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class LeakDetector$$ExternalSyntheticLambda0 implements Predicate {
    public static final /* synthetic */ LeakDetector$$ExternalSyntheticLambda0 INSTANCE = new LeakDetector$$ExternalSyntheticLambda0();

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        boolean z = LeakDetector.ENABLED;
        return !(((Collection) obj) instanceof TrackedObjects.TrackedClass);
    }
}

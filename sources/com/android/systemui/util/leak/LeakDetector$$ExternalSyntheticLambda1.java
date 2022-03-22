package com.android.systemui.util.leak;

import com.android.systemui.util.leak.TrackedObjects;
import java.util.Collection;
import java.util.function.Predicate;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class LeakDetector$$ExternalSyntheticLambda1 implements Predicate {
    public static final /* synthetic */ LeakDetector$$ExternalSyntheticLambda1 INSTANCE = new LeakDetector$$ExternalSyntheticLambda1();

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        return ((Collection) obj) instanceof TrackedObjects.TrackedClass;
    }
}

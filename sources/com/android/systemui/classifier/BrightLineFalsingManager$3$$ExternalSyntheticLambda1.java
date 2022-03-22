package com.android.systemui.classifier;

import com.android.systemui.classifier.FalsingClassifier;
import java.util.Objects;
import java.util.function.Predicate;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BrightLineFalsingManager$3$$ExternalSyntheticLambda1 implements Predicate {
    public static final /* synthetic */ BrightLineFalsingManager$3$$ExternalSyntheticLambda1 INSTANCE = new BrightLineFalsingManager$3$$ExternalSyntheticLambda1();

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        FalsingClassifier.Result result = (FalsingClassifier.Result) obj;
        Objects.requireNonNull(result);
        return result.mFalsed;
    }
}

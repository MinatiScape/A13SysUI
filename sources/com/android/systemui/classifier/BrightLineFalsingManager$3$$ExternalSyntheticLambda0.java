package com.android.systemui.classifier;

import com.android.systemui.classifier.FalsingClassifier;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BrightLineFalsingManager$3$$ExternalSyntheticLambda0 implements Consumer {
    public static final /* synthetic */ BrightLineFalsingManager$3$$ExternalSyntheticLambda0 INSTANCE = new BrightLineFalsingManager$3$$ExternalSyntheticLambda0();

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        String reason;
        FalsingClassifier.Result result = (FalsingClassifier.Result) obj;
        Objects.requireNonNull(result);
        if (result.mFalsed && (reason = result.getReason()) != null) {
            BrightLineFalsingManager.logInfo(reason);
        }
    }
}

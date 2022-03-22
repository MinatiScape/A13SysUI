package com.android.systemui.classifier;

import com.android.systemui.classifier.FalsingDataProvider;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class FalsingDataProvider$$ExternalSyntheticLambda0 implements Consumer {
    public static final /* synthetic */ FalsingDataProvider$$ExternalSyntheticLambda0 INSTANCE = new FalsingDataProvider$$ExternalSyntheticLambda0();

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((FalsingDataProvider.SessionListener) obj).onSessionStarted();
    }
}

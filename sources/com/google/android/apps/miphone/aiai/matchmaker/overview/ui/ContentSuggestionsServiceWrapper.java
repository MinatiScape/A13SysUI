package com.google.android.apps.miphone.aiai.matchmaker.overview.ui;

import android.os.Bundle;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public abstract class ContentSuggestionsServiceWrapper {
    public final Executor asyncExecutor;
    public final Executor callbackExecutor;
    public final Executor loggingExecutor;
    public final Executor uiExecutor;

    /* loaded from: classes.dex */
    public interface BundleCallback {
        void onResult(Bundle bundle);
    }

    public ContentSuggestionsServiceWrapper(SuggestController$$ExternalSyntheticLambda2 suggestController$$ExternalSyntheticLambda2, Executor executor, Executor executor2) {
        ContentSuggestionsServiceWrapperImpl$$ExternalSyntheticLambda3 contentSuggestionsServiceWrapperImpl$$ExternalSyntheticLambda3 = ContentSuggestionsServiceWrapperImpl$$ExternalSyntheticLambda3.INSTANCE;
        this.uiExecutor = suggestController$$ExternalSyntheticLambda2;
        this.asyncExecutor = executor;
        this.loggingExecutor = executor2;
        this.callbackExecutor = contentSuggestionsServiceWrapperImpl$$ExternalSyntheticLambda3;
    }
}

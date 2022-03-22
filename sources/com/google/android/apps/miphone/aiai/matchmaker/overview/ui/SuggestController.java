package com.google.android.apps.miphone.aiai.matchmaker.overview.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.FeedbackParcelables$Feedback;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.FeedbackParcelables$FeedbackBatch;
import com.google.android.apps.miphone.aiai.matchmaker.overview.common.BundleUtils;
import com.google.android.apps.miphone.aiai.matchmaker.overview.ui.utils.Utils;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public final class SuggestController {
    public final BundleUtils bundleUtils = new BundleUtils();
    public final Context uiContext;
    public final ContentSuggestionsServiceWrapperImpl wrapper;

    public SuggestController(Context context, Context context2, SuggestController$$ExternalSyntheticLambda2 suggestController$$ExternalSyntheticLambda2, Executor executor, Executor executor2) {
        this.uiContext = context2;
        this.wrapper = new ContentSuggestionsServiceWrapperImpl(context, suggestController$$ExternalSyntheticLambda2, executor, executor2);
    }

    @VisibleForTesting
    public void reportMetricsToService(String id, final FeedbackParcelables$FeedbackBatch feedbackBatch, @Nullable final SuggestListener suggestListener) {
        Objects.requireNonNull(feedbackBatch);
        List<FeedbackParcelables$Feedback> list = feedbackBatch.feedback;
        int i = Utils.$r8$clinit;
        Objects.requireNonNull(list);
        if (!list.isEmpty()) {
            this.wrapper.notifyInteractionAsync(id, new Supplier() { // from class: com.google.android.apps.miphone.aiai.matchmaker.overview.ui.SuggestController$$ExternalSyntheticLambda3
                @Override // java.util.function.Supplier
                public final Object get() {
                    SuggestController suggestController = SuggestController.this;
                    FeedbackParcelables$FeedbackBatch feedbackParcelables$FeedbackBatch = feedbackBatch;
                    Objects.requireNonNull(suggestController);
                    Objects.requireNonNull(suggestController.bundleUtils);
                    return BundleUtils.createFeedbackRequest(feedbackParcelables$FeedbackBatch);
                }
            }, suggestListener, feedbackBatch);
        }
    }
}

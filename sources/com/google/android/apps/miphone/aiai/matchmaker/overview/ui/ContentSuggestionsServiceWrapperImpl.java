package com.google.android.apps.miphone.aiai.matchmaker.overview.ui;

import android.app.contentsuggestions.ClassificationsRequest;
import android.app.contentsuggestions.ContentClassification;
import android.app.contentsuggestions.ContentSelection;
import android.app.contentsuggestions.ContentSuggestionsManager;
import android.app.contentsuggestions.SelectionsRequest;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2.FeedbackParcelables$FeedbackBatch;
import com.google.android.apps.miphone.aiai.matchmaker.overview.ui.ContentSuggestionsServiceClient;
import com.google.android.apps.miphone.aiai.matchmaker.overview.ui.ContentSuggestionsServiceWrapper;
import com.google.android.apps.miphone.aiai.matchmaker.overview.ui.ContentSuggestionsServiceWrapperImpl;
import com.google.android.apps.miphone.aiai.matchmaker.overview.ui.utils.LogUtils;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public final class ContentSuggestionsServiceWrapperImpl extends ContentSuggestionsServiceWrapper {
    public final ContentSuggestionsManager contentSuggestionsManager;
    public final Map<Object, ContentSuggestionsServiceWrapper.BundleCallback> pendingCallbacks = Collections.synchronizedMap(new WeakHashMap());

    /* loaded from: classes.dex */
    public static class BundleCallbackWrapper implements ContentSuggestionsServiceWrapper.BundleCallback {
        public final Object key;
        public final WeakReference<ContentSuggestionsServiceWrapperImpl> parentRef;

        @Override // com.google.android.apps.miphone.aiai.matchmaker.overview.ui.ContentSuggestionsServiceWrapper.BundleCallback
        public final void onResult(Bundle bundle) {
            ContentSuggestionsServiceWrapper.BundleCallback remove;
            ContentSuggestionsServiceWrapperImpl contentSuggestionsServiceWrapperImpl = this.parentRef.get();
            if (contentSuggestionsServiceWrapperImpl != null && (remove = contentSuggestionsServiceWrapperImpl.pendingCallbacks.remove(this.key)) != null) {
                remove.onResult(bundle);
            }
        }

        public BundleCallbackWrapper(ContentSuggestionsServiceWrapper.BundleCallback original, ContentSuggestionsServiceWrapperImpl parent) {
            Object obj = new Object();
            this.key = obj;
            parent.pendingCallbacks.put(obj, original);
            this.parentRef = new WeakReference<>(parent);
        }
    }

    public final void classifyContentSelections(Bundle bundle, final ContentSuggestionsServiceClient.AnonymousClass1.C00081 classificationsCallback) {
        try {
            ClassificationsRequest build = new ClassificationsRequest.Builder(new ArrayList()).setExtras(bundle).build();
            final BundleCallbackWrapper bundleCallbackWrapper = new BundleCallbackWrapper(classificationsCallback, this);
            this.contentSuggestionsManager.classifyContentSelections(build, this.callbackExecutor, new ContentSuggestionsManager.ClassificationsCallback() { // from class: com.google.android.apps.miphone.aiai.matchmaker.overview.ui.ContentSuggestionsServiceWrapperImpl$$ExternalSyntheticLambda0
                public final void onContentClassificationsAvailable(int i, List list) {
                    ContentSuggestionsServiceWrapperImpl.BundleCallbackWrapper.this.onResult(((ContentClassification) list.get(0)).getExtras());
                }
            });
        } catch (Throwable th) {
            LogUtils.e("Failed to classifyContentSelections", th);
        }
    }

    public final void notifyInteractionAsync(final String requestId, final Supplier<Bundle> interaction, @Nullable final SuggestListener listener, @Nullable final FeedbackParcelables$FeedbackBatch feedbackBatch) {
        this.loggingExecutor.execute(new Runnable() { // from class: com.google.android.apps.miphone.aiai.matchmaker.overview.ui.ContentSuggestionsServiceWrapperImpl$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                ContentSuggestionsServiceWrapperImpl contentSuggestionsServiceWrapperImpl = ContentSuggestionsServiceWrapperImpl.this;
                Supplier supplier = interaction;
                String str = requestId;
                SuggestListener suggestListener = listener;
                FeedbackParcelables$FeedbackBatch feedbackParcelables$FeedbackBatch = feedbackBatch;
                Objects.requireNonNull(contentSuggestionsServiceWrapperImpl);
                try {
                    contentSuggestionsServiceWrapperImpl.contentSuggestionsManager.notifyInteraction(str, (Bundle) supplier.get());
                    if (suggestListener != null && feedbackParcelables$FeedbackBatch != null) {
                        suggestListener.onFeedbackBatchSent();
                    }
                } catch (Throwable th) {
                    LogUtils.e("Failed to notifyInteraction", th);
                }
            }
        });
    }

    public final void suggestContentSelections(int taskId, Bundle bundle, final ContentSuggestionsServiceWrapper.BundleCallback selectionsCallback) {
        SelectionsRequest build = new SelectionsRequest.Builder(taskId).setExtras(bundle).build();
        try {
            final BundleCallbackWrapper bundleCallbackWrapper = new BundleCallbackWrapper(selectionsCallback, this);
            this.contentSuggestionsManager.suggestContentSelections(build, this.callbackExecutor, new ContentSuggestionsManager.SelectionsCallback() { // from class: com.google.android.apps.miphone.aiai.matchmaker.overview.ui.ContentSuggestionsServiceWrapperImpl$$ExternalSyntheticLambda1
                public final void onContentSelectionsAvailable(int i, List list) {
                    ContentSuggestionsServiceWrapperImpl.BundleCallbackWrapper.this.onResult(((ContentSelection) list.get(0)).getExtras());
                }
            });
        } catch (Throwable th) {
            LogUtils.e("Failed to suggestContentSelections", th);
        }
    }

    public ContentSuggestionsServiceWrapperImpl(Context userProfileDataContext, SuggestController$$ExternalSyntheticLambda2 uiExecutor, Executor asyncExecutor, Executor loggingExecutor) {
        super(uiExecutor, asyncExecutor, loggingExecutor);
        this.contentSuggestionsManager = (ContentSuggestionsManager) userProfileDataContext.getSystemService(ContentSuggestionsManager.class);
    }
}

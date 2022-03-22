package com.android.settingslib.suggestions;

import android.content.AsyncTaskLoader;
import android.service.settings.suggestions.Suggestion;
import java.util.List;
@Deprecated
/* loaded from: classes.dex */
public final class SuggestionLoader extends AsyncTaskLoader<List<Suggestion>> {
    public List<Suggestion> mResult;

    public SuggestionLoader() {
        super(null);
    }

    /* renamed from: onStartLoading$com$android$settingslib$utils$AsyncLoader */
    public final void onStartLoading() {
        List<Suggestion> list = this.mResult;
        if (list != null) {
            deliverResult(list);
        }
        if (takeContentChanged() || this.mResult == null) {
            forceLoad();
        }
    }

    /* renamed from: deliverResult$com$android$settingslib$utils$AsyncLoader */
    public final void deliverResult(List<Suggestion> list) {
        if (!isReset()) {
            List<Suggestion> list2 = this.mResult;
            this.mResult = list;
            if (isStarted()) {
                super.deliverResult(list);
            }
            if (list2 != null && list2 == this.mResult) {
            }
        } else if (list == null) {
        }
    }

    /* renamed from: onCanceled$com$android$settingslib$utils$AsyncLoader */
    public final void onCanceled(List<Suggestion> list) {
        super.onCanceled(list);
        if (list == null) {
        }
    }

    /* renamed from: onReset$com$android$settingslib$utils$AsyncLoader */
    public final void onReset() {
        super.onReset();
        cancelLoad();
        if (this.mResult != null) {
        }
        this.mResult = null;
    }

    @Override // android.content.AsyncTaskLoader
    public final List<Suggestion> loadInBackground() {
        throw null;
    }

    @Override // android.content.Loader
    public final void onStopLoading() {
        cancelLoad();
    }
}

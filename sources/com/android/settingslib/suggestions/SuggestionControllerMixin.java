package com.android.settingslib.suggestions;

import android.app.LoaderManager;
import android.content.Loader;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.os.Bundle;
import android.service.settings.suggestions.Suggestion;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import java.util.List;
@Deprecated
/* loaded from: classes.dex */
public class SuggestionControllerMixin implements LifecycleObserver, LoaderManager.LoaderCallbacks<List<Suggestion>> {
    @Override // android.app.LoaderManager.LoaderCallbacks
    public final void onLoaderReset(Loader<List<Suggestion>> loader) {
    }

    @Override // android.app.LoaderManager.LoaderCallbacks
    public final Loader<List<Suggestion>> onCreateLoader(int i, Bundle bundle) {
        if (i == 42) {
            return new SuggestionLoader();
        }
        throw new IllegalArgumentException(VendorAtomValue$$ExternalSyntheticOutline0.m("This loader id is not supported ", i));
    }

    @Override // android.app.LoaderManager.LoaderCallbacks
    public final void onLoadFinished(Loader<List<Suggestion>> loader, List<Suggestion> list) {
        throw null;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        throw null;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onStop() {
        throw null;
    }
}

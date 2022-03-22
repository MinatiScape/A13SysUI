package com.android.systemui.util.service;
/* loaded from: classes.dex */
public interface Observer {

    /* loaded from: classes.dex */
    public interface Callback {
        void onSourceChanged();
    }

    void addCallback(PersistentConnectionManager$$ExternalSyntheticLambda0 persistentConnectionManager$$ExternalSyntheticLambda0);

    void removeCallback(PersistentConnectionManager$$ExternalSyntheticLambda0 persistentConnectionManager$$ExternalSyntheticLambda0);
}

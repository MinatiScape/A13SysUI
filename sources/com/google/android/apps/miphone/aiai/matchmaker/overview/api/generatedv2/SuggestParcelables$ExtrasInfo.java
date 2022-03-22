package com.google.android.apps.miphone.aiai.matchmaker.overview.api.generatedv2;

import android.os.Bundle;
/* loaded from: classes.dex */
public final class SuggestParcelables$ExtrasInfo {
    public boolean containsBitmaps;
    public boolean containsPendingIntents;

    public SuggestParcelables$ExtrasInfo(Bundle bundle) {
        if (bundle.containsKey("containsPendingIntents")) {
            this.containsPendingIntents = bundle.getBoolean("containsPendingIntents");
        }
        if (bundle.containsKey("containsBitmaps")) {
            this.containsBitmaps = bundle.getBoolean("containsBitmaps");
        }
    }
}
